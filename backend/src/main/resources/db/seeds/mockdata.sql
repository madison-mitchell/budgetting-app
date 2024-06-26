{\rtf1\ansi\ansicpg1252\cocoartf2761
\cocoatextscaling0\cocoaplatform0{\fonttbl\f0\fswiss\fcharset0 Helvetica;}
{\colortbl;\red255\green255\blue255;}
{\*\expandedcolortbl;;}
\margl1440\margr1440\vieww11520\viewh8400\viewkind0
\pard\tx720\tx1440\tx2160\tx2880\tx3600\tx4320\tx5040\tx5760\tx6480\tx7200\tx7920\tx8640\pardirnatural\partightenfactor0

\f0\fs24 \cf0 -- Insert mock data into Users table\
INSERT INTO Users (FirstName, LastName, Username, PasswordHash, Email, PhoneNumber, Address, DateOfBirth, ProfilePicture)\
VALUES\
('John', 'Doe', 'johndoe', 'hashedpassword1', 'johndoe@example.com', '1234567890', '123 Main St, Anytown, USA', '1990-01-01', 'profile1.jpg'),\
('Jane', 'Smith', 'janesmith', 'hashedpassword2', 'janesmith@example.com', '0987654321', '456 Elm St, Othertown, USA', '1985-05-15', 'profile2.jpg');\
\
-- Insert Parent Categories\
INSERT INTO CategoryParent (Name)\
VALUES\
('Income'),\
('Expenses'),\
('Living Expenses'),\
('Transportation'),\
('Healthcare'),\
('Entertainment'),\
('Dining'),\
('Education'),\
('Personal Care'),\
('Shopping'),\
('Miscellaneous');\
\
-- Insert Child Categories\
INSERT INTO CategoryChild (Name)\
VALUES\
('Salary'),\
('Rent'),\
('Groceries'),\
('Public Transport'),\
('Fuel'),\
('Car Payment'),\
('Car Insurance'),\
('Repairs'),\
('Medical Bills'),\
('Prescriptions'),\
('Health Insurance'),\
('Dental'),\
('Vision'),\
('Movies'),\
('Concerts'),\
('Games'),\
('Hobbies'),\
('Restaurants'),\
('Fast Food'),\
('Tuition'),\
('Books'),\
('Supplies'),\
('Online Courses'),\
('Haircuts'),\
('Cosmetics'),\
('Spa'),\
('Gym Membership'),\
('Clothing'),\
('Electronics'),\
('Home Goods'),\
('Toys'),\
('Gifts'),\
('Donations'),\
('Other');\
\
-- Insert Parent-Child Relations\
INSERT INTO CategoryParentChildRelations (FullName, ParentCategoryId, ChildCategoryId)\
VALUES\
-- Income\
('Income - Salary', (SELECT Id FROM CategoryParent WHERE Name = 'Income'), (SELECT Id FROM CategoryChild WHERE Name = 'Salary')),\
\
-- Expenses\
('Expenses - Rent', (SELECT Id FROM CategoryParent WHERE Name = 'Expenses'), (SELECT Id FROM CategoryChild WHERE Name = 'Rent')),\
('Expenses - Groceries', (SELECT Id FROM CategoryParent WHERE Name = 'Expenses'), (SELECT Id FROM CategoryChild WHERE Name = 'Groceries')),\
\
-- Living Expenses\
('Living Expenses - Rent', (SELECT Id FROM CategoryParent WHERE Name = 'Living Expenses'), (SELECT Id FROM CategoryChild WHERE Name = 'Rent')),\
('Living Expenses - Groceries', (SELECT Id FROM CategoryParent WHERE Name = 'Living Expenses'), (SELECT Id FROM CategoryChild WHERE Name = 'Groceries')),\
\
-- Transportation\
('Transportation - Public Transport', (SELECT Id FROM CategoryParent WHERE Name = 'Transportation'), (SELECT Id FROM CategoryChild WHERE Name = 'Public Transport')),\
('Transportation - Fuel', (SELECT Id FROM CategoryParent WHERE Name = 'Transportation'), (SELECT Id FROM CategoryChild WHERE Name = 'Fuel')),\
('Transportation - Car Payment', (SELECT Id FROM CategoryParent WHERE Name = 'Transportation'), (SELECT Id FROM CategoryChild WHERE Name = 'Car Payment')),\
('Transportation - Car Insurance', (SELECT Id FROM CategoryParent WHERE Name = 'Transportation'), (SELECT Id FROM CategoryChild WHERE Name = 'Car Insurance')),\
('Transportation - Repairs', (SELECT Id FROM CategoryParent WHERE Name = 'Transportation'), (SELECT Id FROM CategoryChild WHERE Name = 'Repairs')),\
\
-- Healthcare\
('Healthcare - Medical Bills', (SELECT Id FROM CategoryParent WHERE Name = 'Healthcare'), (SELECT Id FROM CategoryChild WHERE Name = 'Medical Bills')),\
('Healthcare - Prescriptions', (SELECT Id FROM CategoryParent WHERE Name = 'Healthcare'), (SELECT Id FROM CategoryChild WHERE Name = 'Prescriptions')),\
('Healthcare - Health Insurance', (SELECT Id FROM CategoryParent WHERE Name = 'Healthcare'), (SELECT Id FROM CategoryChild WHERE Name = 'Health Insurance')),\
('Healthcare - Dental', (SELECT Id FROM CategoryParent WHERE Name = 'Healthcare'), (SELECT Id FROM CategoryChild WHERE Name = 'Dental')),\
('Healthcare - Vision', (SELECT Id FROM CategoryParent WHERE Name = 'Healthcare'), (SELECT Id FROM CategoryChild WHERE Name = 'Vision')),\
\
-- Entertainment\
('Entertainment - Movies', (SELECT Id FROM CategoryParent WHERE Name = 'Entertainment'), (SELECT Id FROM CategoryChild WHERE Name = 'Movies')),\
('Entertainment - Concerts', (SELECT Id FROM CategoryParent WHERE Name = 'Entertainment'), (SELECT Id FROM CategoryChild WHERE Name = 'Concerts')),\
('Entertainment - Games', (SELECT Id FROM CategoryParent WHERE Name = 'Entertainment'), (SELECT Id FROM CategoryChild WHERE Name = 'Games')),\
('Entertainment - Hobbies', (SELECT Id FROM CategoryParent WHERE Name = 'Entertainment'), (SELECT Id FROM CategoryChild WHERE Name = 'Hobbies')),\
\
-- Dining\
('Dining - Groceries', (SELECT Id FROM CategoryParent WHERE Name = 'Dining'), (SELECT Id FROM CategoryChild WHERE Name = 'Groceries')),\
('Dining - Restaurants', (SELECT Id FROM CategoryParent WHERE Name = 'Dining'), (SELECT Id FROM CategoryChild WHERE Name = 'Restaurants')),\
('Dining - Fast Food', (SELECT Id FROM CategoryParent WHERE Name = 'Dining'), (SELECT Id FROM CategoryChild WHERE Name = 'Fast Food')),\
\
-- Education\
('Education - Tuition', (SELECT Id FROM CategoryParent WHERE Name = 'Education'), (SELECT Id FROM CategoryChild WHERE Name = 'Tuition')),\
('Education - Books', (SELECT Id FROM CategoryParent WHERE Name = 'Education'), (SELECT Id FROM CategoryChild WHERE Name = 'Books')),\
('Education - Supplies', (SELECT Id FROM CategoryParent WHERE Name = 'Education'), (SELECT Id FROM CategoryChild WHERE Name = 'Supplies')),\
('Education - Online Courses', (SELECT Id FROM CategoryParent WHERE Name = 'Education'), (SELECT Id FROM CategoryChild WHERE Name = 'Online Courses')),\
\
-- Personal Care\
('Personal Care - Haircuts', (SELECT Id FROM CategoryParent WHERE Name = 'Personal Care'), (SELECT Id FROM CategoryChild WHERE Name = 'Haircuts')),\
('Personal Care - Cosmetics', (SELECT Id FROM CategoryParent WHERE Name = 'Personal Care'), (SELECT Id FROM CategoryChild WHERE Name = 'Cosmetics')),\
('Personal Care - Spa', (SELECT Id FROM CategoryParent WHERE Name = 'Personal Care'), (SELECT Id FROM CategoryChild WHERE Name = 'Spa')),\
('Personal Care - Gym Membership', (SELECT Id FROM CategoryParent WHERE Name = 'Personal Care'), (SELECT Id FROM CategoryChild WHERE Name = 'Gym Membership')),\
\
-- Shopping\
('Shopping - Clothing', (SELECT Id FROM CategoryParent WHERE Name = 'Shopping'), (SELECT Id FROM CategoryChild WHERE Name = 'Clothing')),\
('Shopping - Electronics', (SELECT Id FROM CategoryParent WHERE Name = 'Shopping'), (SELECT Id FROM CategoryChild WHERE Name = 'Electronics')),\
('Shopping - Home Goods', (SELECT Id FROM CategoryParent WHERE Name = 'Shopping'), (SELECT Id FROM CategoryChild WHERE Name = 'Home Goods')),\
('Shopping - Toys', (SELECT Id FROM CategoryParent WHERE Name = 'Shopping'), (SELECT Id FROM CategoryChild WHERE Name = 'Toys')),\
('Shopping - Gifts', (SELECT Id FROM CategoryParent WHERE Name = 'Shopping'), (SELECT Id FROM CategoryChild WHERE Name = 'Gifts')),\
\
-- Miscellaneous\
('Miscellaneous - Donations', (SELECT Id FROM CategoryParent WHERE Name = 'Miscellaneous'), (SELECT Id FROM CategoryChild WHERE Name = 'Donations')),\
('Miscellaneous - Other', (SELECT Id FROM CategoryParent WHERE Name = 'Miscellaneous'), (SELECT Id FROM CategoryChild WHERE Name = 'Other'));\
\
-- Insert mock data into Type table\
INSERT INTO Type (Name)\
VALUES\
('One-time'),\
('Recurring');\
\
-- Insert mock data into Income table\
INSERT INTO Income (UserId, Amount, CategoryId, Source, Date, Notes, Recurring, Frequency, Reviewed)\
VALUES\
(1, 5000.00, 1, 'Company ABC', '2024-01-01', 'January Salary', TRUE, 'Monthly', 'Unreviewed'),\
(2, 4000.00, 1, 'Company XYZ', '2024-01-01', 'January Salary', TRUE, 'Monthly', 'Unreviewed');\
\
-- Insert mock data into Goals table\
INSERT INTO Goals (UserId, Name, TargetAmount, SavedAmount, DueDate, Notes, Priority, Reviewed)\
VALUES\
(1, 'Vacation Fund', 2000.00, 500.00, '2024-06-01', 'Saving for summer vacation', 'High', 'Unreviewed'),\
(2, 'Emergency Fund', 5000.00, 1000.00, '2024-12-31', 'Safety net', 'Medium', 'Unreviewed');\
\
-- Insert mock data into Bills table\
INSERT INTO Bills (UserId, Amount, Name, CategoryId, Description, DueDate, Recurring, Frequency, Notes, Status, Reviewed)\
VALUES\
(1, 1500.00, 'January Rent', 2, 'Monthly rent payment', '2024-01-05', TRUE, 'Monthly', 'Pay by 5th', 'Unpaid', 'Unreviewed'),\
(2, 200.00, 'Groceries', 3, 'Weekly groceries', '2024-01-07', TRUE, 'Weekly', 'Buy from local market', 'Unpaid', 'Unreviewed');\
\
SELECT * FROM BILLS;\
\
-- Insert mock data into SharedBills table\
INSERT INTO SharedBills (BillId, UserId, Name, PersonAPortion, PersonBPortion, IsPercentage, PercentageSplit, Notes, Reviewed)\
VALUES\
(1, 1, 'Rent Split', 750.00, 750.00, FALSE, NULL, 'Split equally between roommates', 'Unreviewed'),\
(2, 2, 'Grocery Split', 100.00, 100.00, FALSE, NULL, 'Split equally between partners', 'Unreviewed');\
\
-- Insert mock data into BankAccounts table\
INSERT INTO BankAccounts (UserId, BankName, AccountNumber, Name, Balance, AccountType, PaymentMethod, Notes, LastUpdated, Reviewed)\
VALUES\
(1, 'Bank of America', '1234567890', 'Checking Account', 2500.00, 'Checking', 'Debit Card', 'Primary account', '2024-01-01', 'Unreviewed'),\
(2, 'Wells Fargo', '0987654321', 'Savings Account', 3000.00, 'Savings', 'Credit Card', 'Emergency funds', '2024-01-01', 'Unreviewed');\
\
-- Insert mock data into Expenses table\
INSERT INTO Expenses (UserId, Amount, CategoryId, Description, Date, Notes, BankAccountId, Merchant, Recurring, Frequency, Reviewed)\
VALUES\
(1, 200.00, 3, 'Groceries for the week', '2024-01-07', 'Bought from local market', 1, 'Local Market', TRUE, 'Weekly', 'Unreviewed'),\
(2, 50.00, 3, 'Dinner out', '2024-01-08', 'Dinner with friends', 2, 'Restaurant', FALSE, NULL, 'Unreviewed');\
\
SELECT * FROM BANKACCOUNTS;\
\
-- Insert mock data into Transactions table\
INSERT INTO Transactions (BankAccountId, Amount, CategoryId, Description, TimeOfTransaction, Notes, Merchant, Recurring, Frequency, Included, Reviewed)\
VALUES\
(1, -200.00, 3, 'Groceries', '2024-01-07 10:00:00', 'Weekly groceries', 'Local Market', TRUE, 'Weekly', TRUE, 'Unreviewed'),\
(1, 5000.00, 1, 'Salary', '2024-01-01 09:00:00', 'Monthly salary', 'Company ABC', TRUE, 'Monthly', TRUE, 'Unreviewed');\
\
}