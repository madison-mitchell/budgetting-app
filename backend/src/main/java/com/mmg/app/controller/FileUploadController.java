package com.mmg.app.controller;

import com.mmg.app.model.BankAccount;
import com.mmg.app.model.CategoryParentChildRelations;
import com.mmg.app.model.Transactions;
import com.mmg.app.model.User;
import com.mmg.app.repository.BankAccountRepository;
import com.mmg.app.repository.CategoryParentChildRelationsRepository;
import com.mmg.app.repository.TransactionsRepository;
import com.mmg.app.repository.UserRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/upload")
public class FileUploadController {

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
            return ResponseEntity.status(HttpStatus.OK).body("PDF file uploaded and transactions saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process the PDF file!");
        }
    }

    private List<Transactions> parseAppleCardStatement(String text, BankAccount bankAccount, User user) {
        List<Transactions> transactions = new ArrayList<>();
        String[] lines = text.split("\\r?\\n");

        for (String line : lines) {
            if (line.matches("\\d{2}/\\d{2}/\\d{4}.*")) { // Match lines that start with a date
                String[] parts = line.split("\\s+");

                try {
                    Date date = parseDate(parts[0]);
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
                    transaction.setAmount(amount);
                    transaction.setType("Apple Card");
                    transaction.setAccountId(bankAccount);
                    transaction.setUser(user);

                    // For now, we'll set the category as "Other" or a default category
                    CategoryParentChildRelations categoryEntity = categoryRepository.findByChildCategoryName("Other");
                    transaction.setCategoryId(categoryEntity);

                    transactions.add(transaction);
                } catch (ParseException | NumberFormatException e) {
                    // Log error and continue parsing the next line
                    System.err.println("Skipping line due to parsing error: " + line);
                }
            }
        }

        return transactions;
    }

    private Date parseDate(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        return dateFormat.parse(date);
    }
}
