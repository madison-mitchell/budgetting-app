package com.mmg.app.controller;

import com.mmg.app.model.BankAccount;
import com.mmg.app.model.CategoryParentChildRelations;
import com.mmg.app.model.Transactions;
import com.mmg.app.model.User;
import com.mmg.app.repository.BankAccountRepository;
import com.mmg.app.repository.CategoryParentChildRelationsRepository;
import com.mmg.app.repository.TransactionsRepository;
import com.mmg.app.repository.UserRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/api/upload")
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private TransactionsRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private CategoryParentChildRelationsRepository categoryRepository;

    private static final String DATE_FORMAT = "MM/dd/yyyy";

    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("accountId") Long accountId) {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);
        if (bankAccount == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid bank account ID.");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user.");
        }

        if (file.getContentType().equals("application/pdf")) {
            return handlePdfUpload(file, bankAccount, user);
        } else if (file.getContentType().equals("text/csv") || file.getContentType().equals("application/vnd.ms-excel")) {
            return handleCsvUpload(file, bankAccount, user);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unsupported file type.");
        }
    }

    private ResponseEntity<String> handlePdfUpload(MultipartFile file, BankAccount bankAccount, User user) {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            List<Transactions> transactions = parseAppleCardStatement(text, bankAccount, user);
            transactionRepository.saveAll(transactions);
            logger.info("PDF file uploaded and transactions saved successfully!");
            return ResponseEntity.status(HttpStatus.OK).body("PDF file uploaded and transactions saved successfully!");
        } catch (IOException e) {
            logger.error("Failed to process the PDF file!", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process the PDF file!");
        }
    }

    private ResponseEntity<String> handleCsvUpload(MultipartFile file, BankAccount bankAccount, User user) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            List<Transactions> transactions = new ArrayList<>();
            for (CSVRecord record : csvParser) {
                Transactions transaction = new Transactions();

                // Parse date using the correct format
                transaction.setTimeOfTransaction(parseDate(record.get("Date")));
                transaction.setDescription(record.get("Description"));
                transaction.setAmount(Double.parseDouble(record.get("Amount")));
                transaction.setType(record.get("Category")); // Category maps to type
                transaction.setAccountId(bankAccount); // Set the bank account
                transaction.setUser(user); // Set the user

                // Fetch the related category entity based on the child category name (tags)
                CategoryParentChildRelations category = categoryRepository.findByChildCategoryName(record.get("Tags"));
                if (category == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid category name: " + record.get("Tags"));
                }
                transaction.setCategoryId(category); // Set the category

                transactions.add(transaction);
            }

            transactionRepository.saveAll(transactions);
            return ResponseEntity.status(HttpStatus.OK).body("File uploaded and transactions saved successfully!");

        } catch (IOException | ParseException e) {
            logger.error("Failed to process the CSV file!", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process the CSV file!");
        }
    }

    private Date parseDate(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        return dateFormat.parse(date);
    }

    private List<Transactions> parseAppleCardStatement(String text, BankAccount bankAccount, User user) {
        List<Transactions> transactions = new ArrayList<>();
        Map<Date, List<Transactions>> transactionsByDate = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        String[] lines = text.split("\\r?\\n");
        boolean isPaymentSection = false;
        boolean isTransactionSection = false;

        for (String line : lines) {
            if (line.contains("Payments made by")) {
                isPaymentSection = true;
                isTransactionSection = false;
                logger.info("Entering Payments section");
                continue;
            } else if (line.contains("Transactions")) {
                isPaymentSection = false;
                isTransactionSection = true;
                logger.info("Entering Transactions section");
                continue;
            }

            if (isPaymentSection || isTransactionSection) {
                if (line.matches("\\d{2}/\\d{2}/\\d{4}.*")) { // Match lines that start with a date
                    String[] parts = line.split("\\s+");
                    try {
                        Date date = dateFormat.parse(parts[0]);
                        StringBuilder descriptionBuilder = new StringBuilder();
                        int i = 1;
                        while (i < parts.length && !parts[i].matches("\\d+%")) {
                            descriptionBuilder.append(parts[i]).append(" ");
                            i++;
                        }
                        String description = descriptionBuilder.toString().trim();
                        double amount = Double.parseDouble(parts[parts.length - 1].replace(",", "").replace("$", ""));

                        Transactions transaction = new Transactions();
                        transaction.setTimeOfTransaction(date);
                        transaction.setDescription(description);
                        transaction.setAmount(-amount); // Positive for payments, negative for transactions
                        transaction.setType(isPaymentSection ? "Payment" : "Transaction");
                        transaction.setAccountId(bankAccount);
                        transaction.setUser(user);

                        logger.debug("Parsed transaction: date={}, description={}, amount={}, type={}", date, description, amount, transaction.getType());

                        // Set the category to default to "Other"
                        CategoryParentChildRelations categoryEntity = categoryRepository.findByChildCategoryName("Other");
                        transaction.setCategoryId(categoryEntity);

                        if (isPaymentSection) {
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(date);
                            cal.add(Calendar.DAY_OF_MONTH, 1); // Add one day to move the payment to midnight the next day
                            cal.set(Calendar.HOUR_OF_DAY, 0);
                            cal.set(Calendar.MINUTE, 0);
                            cal.set(Calendar.SECOND, 0);
                            cal.set(Calendar.MILLISECOND, 0);
                            transaction.setTimeOfTransaction(cal.getTime());
                            logger.debug("Adjusted payment date to next day midnight: {}", transaction.getTimeOfTransaction());
                        }

                        transactionsByDate.putIfAbsent(date, new ArrayList<>());
                        transactionsByDate.get(date).add(transaction);

                    } catch (ParseException | NumberFormatException e) {
                        logger.error("Skipping line due to parsing error: " + line, e);
                    }
                }
            }
        }

        // Flatten the map to a sorted list of transactions
        transactionsByDate.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    List<Transactions> dayTransactions = entry.getValue();
                    dayTransactions.sort(Comparator.comparing(Transactions::getTimeOfTransaction));
                    transactions.addAll(dayTransactions);
                });

        logger.info("Parsed a total of {} transactions", transactions.size());
        return transactions;
    }
}
