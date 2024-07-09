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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    private static final String DATE_FORMAT = "MM/dd/yy";

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
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process the file!");
        }
    }

    private Date parseDate(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        return dateFormat.parse(date);
    }
}