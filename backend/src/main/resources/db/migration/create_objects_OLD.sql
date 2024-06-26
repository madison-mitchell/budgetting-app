-- TABLES
-- Users Table
CREATE TABLE Users (
    Id BIGINT PRIMARY KEY,
    FirstName VARCHAR(120) NOT NULL,
    LastName VARCHAR(120) NOT NULL,
    Username VARCHAR(50) NOT NULL UNIQUE,
    PasswordHash VARCHAR(255) NOT NULL,
    Email VARCHAR(100) NOT NULL UNIQUE,
    PhoneNumber VARCHAR(15),
    Address TEXT,
    DateOfBirth TIMESTAMP,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    LastLogin TIMESTAMP,
    ProfilePicture TEXT,
    Role VARCHAR(20) DEFAULT 'user',
    IsActive BOOLEAN DEFAULT TRUE
);

-- Create CategoryParent Table
CREATE TABLE CategoryParent (
    Id BIGINT PRIMARY KEY,
    Name VARCHAR(50) NOT NULL UNIQUE
);

-- Create CategoryChild Table
CREATE TABLE CategoryChild (
    Id BIGINT PRIMARY KEY,
    Name VARCHAR(50) NOT NULL UNIQUE
);

-- Create CategoryParentChildRelations Table
CREATE TABLE CategoryParentChildRelations (
    Id BIGINT PRIMARY KEY,
    FullName VARCHAR(120) NOT NULL UNIQUE,
    ParentCategoryId INT NOT NULL,
    ChildCategoryId INT NOT NULL,
    FOREIGN KEY (ParentCategoryId) REFERENCES CategoryParent(Id) ON DELETE CASCADE,
    FOREIGN KEY (ChildCategoryId) REFERENCES CategoryChild(Id) ON DELETE CASCADE
);

-- Create Type Table
CREATE TABLE Type (
    Id BIGINT PRIMARY KEY,
    Name VARCHAR(50) NOT NULL UNIQUE
);

-- Income Table
CREATE TABLE Income (
    Id BIGINT PRIMARY KEY,
    UserId INT NOT NULL,
    Amount DECIMAL(10, 2) NOT NULL,
    CategoryId INT NOT NULL,
    Source VARCHAR(100),
    Date TIMESTAMP NOT NULL,
    Notes TEXT,
    Recurring BOOLEAN DEFAULT FALSE,
    Frequency VARCHAR(20),
    Reviewed VARCHAR(20) DEFAULT 'Unreviewed',
    FOREIGN KEY (UserId) REFERENCES Users(Id) ON DELETE CASCADE,
    FOREIGN KEY (CategoryId) REFERENCES CategoryParentChildRelations(Id) ON DELETE CASCADE
);

-- Goals Table
CREATE TABLE Goals (
    Id BIGINT PRIMARY KEY,
    UserId INT NOT NULL,
    Name VARCHAR(100) NOT NULL,
    TargetAmount DECIMAL(10, 2) NOT NULL,
    SavedAmount DECIMAL(10, 2) DEFAULT 0,
    DueDate TIMESTAMP,
    Notes TEXT,
    Priority VARCHAR(20),
    Reviewed VARCHAR(20) DEFAULT 'Unreviewed',
    FOREIGN KEY (UserId) REFERENCES Users(Id) ON DELETE CASCADE
);

-- Bills Table
CREATE TABLE Bills (
    Id BIGINT PRIMARY KEY,
    UserId INT NOT NULL,
    Amount DECIMAL(10, 2) NOT NULL,
    Name VARCHAR(50) NOT NULL UNIQUE,
    CategoryId INT NOT NULL,
    Description VARCHAR(255),
    DueDate TIMESTAMP NOT NULL,
    Recurring BOOLEAN DEFAULT FALSE,
    Frequency VARCHAR(20),
    Notes TEXT,
    Status VARCHAR(20) DEFAULT 'Unpaid',
    Reviewed VARCHAR(20) DEFAULT 'Unreviewed',
    FOREIGN KEY (UserId) REFERENCES Users(Id) ON DELETE CASCADE,
    FOREIGN KEY (CategoryId) REFERENCES CategoryParentChildRelations(Id) ON DELETE CASCADE
);

-- SharedBills Table
CREATE TABLE SharedBills (
    Id BIGINT PRIMARY KEY,
    BillId INT NOT NULL,
    UserId INT NOT NULL,
    Name VARCHAR(50) NOT NULL UNIQUE,
    PersonAPortion DECIMAL(10, 2) NOT NULL,
    PersonBPortion DECIMAL(10, 2) NOT NULL,
    IsPercentage BOOLEAN NOT NULL, -- True if split by percentage, False if split by specific dollar amount
    PercentageSplit DECIMAL(5, 2), -- Nullable, used only if IsPercentage is True
    Notes TEXT,
    Reviewed VARCHAR(20) DEFAULT 'Unreviewed',
    FOREIGN KEY (BillId) REFERENCES Bills(Id) ON DELETE CASCADE,
    FOREIGN KEY (UserId) REFERENCES Users(Id) ON DELETE CASCADE
);

-- BankAccounts Table
CREATE TABLE BankAccounts (
    Id BIGINT PRIMARY KEY,
    UserId INT NOT NULL,
    BankName VARCHAR(100) NOT NULL,
    AccountNumber VARCHAR(50) NOT NULL,
    Name VARCHAR(50) NOT NULL UNIQUE,
    Balance DECIMAL(15, 2) NOT NULL,
    AccountType VARCHAR(50),
    PaymentMethod VARCHAR(50) NOT NULL,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Notes TEXT,
    LastUpdated TIMESTAMP,
    Reviewed VARCHAR(20) DEFAULT 'Unreviewed',
    FOREIGN KEY (UserId) REFERENCES Users(Id) ON DELETE CASCADE
);

-- Expenses Table
CREATE TABLE Expenses (
    Id BIGINT PRIMARY KEY,
    UserId INT NOT NULL,
    Amount DECIMAL(10, 2) NOT NULL,
    CategoryId INT NOT NULL, -- This column will reference CategoryParentChildRelations
    Description VARCHAR(255),
    Date TIMESTAMP NOT NULL,
    Notes TEXT,
    BankAccountId INT NOT NULL, -- Reference to BankAccounts
    Merchant VARCHAR(100),
    Recurring BOOLEAN DEFAULT FALSE,
    Frequency VARCHAR(20),
    Reviewed VARCHAR(20) DEFAULT 'Unreviewed',
    FOREIGN KEY (UserId) REFERENCES Users(Id) ON DELETE CASCADE,
    FOREIGN KEY (BankAccountId) REFERENCES BankAccounts(Id) ON DELETE RESTRICT,
    FOREIGN KEY (CategoryId) REFERENCES CategoryParentChildRelations(Id) ON DELETE CASCADE
);

-- Transactions Table
CREATE TABLE Transactions (
    Id BIGINT PRIMARY KEY,
    BankAccountId INT NOT NULL,
    UserId INT NOT NULL,
    Amount DECIMAL(10, 2) NOT NULL,
    CategoryId INT NOT NULL, -- This column will reference CategoryParentChildRelations
    Description VARCHAR(255),
    TimeOfTransaction TIMESTAMP NOT NULL,
    Notes TEXT,
    Merchant VARCHAR(100),
    Recurring BOOLEAN DEFAULT FALSE,
    Frequency VARCHAR(20),
    Included BOOLEAN DEFAULT TRUE, -- Indicates if the transaction should be included
    Reviewed VARCHAR(20) DEFAULT 'Unreviewed',
    Type VARCHAR(50) NOT NULL, -- Credit or Debit
    FOREIGN KEY (BankAccountId) REFERENCES BankAccounts(Id) ON DELETE CASCADE,
    FOREIGN KEY (CategoryId) REFERENCES CategoryParentChildRelations(Id) ON DELETE CASCADE
);


-- TRIGGERS
-- Create Update Balance Trigger Function
CREATE OR REPLACE FUNCTION update_balance() 
RETURNS TRIGGER AS $$
BEGIN
    IF (NEW.Type = 'Credit') THEN
        UPDATE BankAccounts
        SET Balance = Balance + NEW.Amount,
            LastUpdated = NEW.TimeOfTransaction
        WHERE Id = NEW.BankAccountId;
    ELSE
        UPDATE BankAccounts
        SET Balance = Balance - NEW.Amount,
            LastUpdated = NEW.TimeOfTransaction
        WHERE Id = NEW.BankAccountId;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create Trigger for Transactions
CREATE OR REPLACE TRIGGER update_balance_trigger
AFTER INSERT ON Transactions
FOR EACH ROW
EXECUTE FUNCTION update_balance();

-- Trigger function to automatically fill FullName in CategoryParentChildRelations
CREATE OR REPLACE FUNCTION set_fullname()
RETURNS TRIGGER AS $$
BEGIN
    NEW.FullName := (SELECT p.Name || ' - ' || c.Name
                     FROM CategoryParent p, CategoryChild c
                     WHERE p.Id = NEW.ParentCategoryId
                     AND c.Id = NEW.ChildCategoryId);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger to call the set_fullname function before inserting into CategoryParentChildRelations
CREATE TRIGGER trg_set_fullname
BEFORE INSERT ON CategoryParentChildRelations
FOR EACH ROW
EXECUTE FUNCTION set_fullname();


-- FUNCTIONS
-- Create Predict Future Balance Function
CREATE OR REPLACE FUNCTION predict_future_balance(account_id INT, prediction_date DATE)
RETURNS DECIMAL AS $$
DECLARE
    current_balance DECIMAL;
    total_recurring_income DECIMAL := 0;
    total_recurring_expenses DECIMAL := 0;
    recurring_income RECORD;
    recurring_expense RECORD;
    days_interval INT;
BEGIN
    -- Get current balance
    SELECT Balance INTO current_balance FROM BankAccounts WHERE Id = account_id;

    -- Calculate total recurring income
    FOR recurring_income IN
        SELECT Amount, Frequency FROM Transactions
        WHERE BankAccountId = account_id AND Type = 'Credit' AND Recurring = TRUE
    LOOP
        IF recurring_income.Frequency = 'Monthly' THEN
            total_recurring_income := total_recurring_income + recurring_income.Amount * (date_part('day', prediction_date - current_date) / 30);
        ELSIF recurring_income.Frequency = 'Weekly' THEN
            total_recurring_income := total_recurring_income + recurring_income.Amount * (date_part('day', prediction_date - current_date) / 7);
        END IF;
    END LOOP;

    -- Calculate total recurring expenses
    FOR recurring_expense IN
        SELECT Amount, Frequency FROM Transactions
        WHERE BankAccountId = account_id AND Type = 'Debit' AND Recurring = TRUE
    LOOP
        IF recurring_expense.Frequency = 'Monthly' THEN
            total_recurring_expenses := total_recurring_expenses + recurring_expense.Amount * (date_part('day', prediction_date - current_date) / 30);
        ELSIF recurring_expense.Frequency = 'Weekly' THEN
            total_recurring_expenses := total_recurring_expenses + recurring_expense.Amount * (date_part('day', prediction_date - current_date) / 7);
        END IF;
    END LOOP;

    -- Calculate predicted balance
    RETURN current_balance + total_recurring_income - total_recurring_expenses;
END;
$$ LANGUAGE plpgsql;

-- Create Insert Category if Not Exists Function
CREATE OR REPLACE FUNCTION insert_category_if_not_exists(category_name VARCHAR, parent_category_id INT)
RETURNS VOID AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM Categories WHERE CategoryName = category_name) THEN
        INSERT INTO Categories (CategoryName, ParentCategoryId) VALUES (category_name, parent_category_id);
    END IF;
END;
$$ LANGUAGE plpgsql;




-- VIEWS
-- Create a view for categories with their relationships
CREATE VIEW CategoryRelationships AS
SELECT 
    pc.Id AS RelationshipId,
    cp.Name AS ParentCategoryName,
    cc.Name AS ChildCategoryName,
    pc.FullName
FROM 
    CategoryParentChildRelations pc
JOIN 
    CategoryParent cp ON pc.ParentCategoryId = cp.Id
JOIN 
    CategoryChild cc ON pc.ChildCategoryId = cc.Id;

-- Create a view for detailed expenses
CREATE VIEW ExpensesDetailed AS
SELECT 
    e.Id AS ExpenseId,
	e.DueDate,
    e.Amount,
    e.Description,
    e.Notes,
    e.Merchant,
    e.Recurring,
    e.Frequency,
    e.Reviewed,
    u.FirstName || ' ' || u.LastName AS User,
    ba.Name AS BankAccountName,
    ba.AccountNumber,
    --cp.Name AS ParentCategoryName,
    --cc.Name AS ChildCategoryName,
	pc.FullName AS Category
FROM 
    Expenses e
JOIN 
    Users u ON e.UserId = u.Id
JOIN 
    BankAccounts ba ON e.BankAccountId = ba.Id
JOIN 
    CategoryParentChildRelations pc ON e.CategoryId = pc.Id
JOIN 
    CategoryParent cp ON pc.ParentCategoryId = cp.Id
JOIN 
    CategoryChild cc ON pc.ChildCategoryId = cc.Id;
