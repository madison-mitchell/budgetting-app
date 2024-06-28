-- Database: budgetting_app

-- DROP DATABASE IF EXISTS budgetting_app;

CREATE DATABASE budgetting_app
    WITH
    OWNER = postgres
ENCODING = 'UTF8'
    LC_COLLATE = 'C'
    LC_CTYPE = 'C'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;






-- TABLES

-- Table: public.bank_account

-- DROP TABLE IF EXISTS public.bank_account;

CREATE TABLE IF NOT EXISTS public.bank_account
(
    id bigint NOT NULL DEFAULT nextval('bankaccounts_id_seq'::regclass),
    user_id bigint NOT NULL,
    bank_name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    account_number character varying(50) COLLATE pg_catalog."default" NOT NULL,
    name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    account_type character varying(50) COLLATE pg_catalog."default",
    payment_method character varying(50) COLLATE pg_catalog."default" NOT NULL,
    created_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    notes text COLLATE pg_catalog."default",
    last_updated timestamp with time zone,
    reviewed character varying COLLATE pg_catalog."default" DEFAULT 'Unreviewed'::character varying,
    initial_balance double precision NOT NULL DEFAULT 0,
    current_balance double precision NOT NULL DEFAULT 0,
    CONSTRAINT bankaccounts_pkey PRIMARY KEY (id),
    CONSTRAINT bankaccounts_name_key UNIQUE (name),
    CONSTRAINT bankaccounts_userid_fkey FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.bank_account
    OWNER to postgres;



-- Table: public.bill

-- DROP TABLE IF EXISTS public.bill;

CREATE TABLE IF NOT EXISTS public.bill
(
    id bigint NOT NULL DEFAULT nextval('bills_id_seq'::regclass),
    user_id bigint NOT NULL,
    amount double precision NOT NULL,
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    category_id bigint NOT NULL,
    description character varying(255) COLLATE pg_catalog."default",
    due_date timestamp with time zone NOT NULL,
    recurring boolean DEFAULT false,
    frequency character varying(255) COLLATE pg_catalog."default",
    notes character varying(255) COLLATE pg_catalog."default",
    status character varying(255) COLLATE pg_catalog."default" DEFAULT 'Unpaid'::character varying,
    reviewed character varying(255) COLLATE pg_catalog."default" DEFAULT 'Unreviewed'::character varying,
    CONSTRAINT bills_pkey PRIMARY KEY (id),
    CONSTRAINT bills_name_key UNIQUE (name),
    CONSTRAINT bills_categoryid_fkey FOREIGN KEY (category_id)
        REFERENCES public.category_parent_child_relation (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT bills_userid_fkey FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.bill
    OWNER to postgres;


-- Table: public.category_child

-- DROP TABLE IF EXISTS public.category_child;

CREATE TABLE IF NOT EXISTS public.category_child
(
    id bigint NOT NULL DEFAULT nextval('categorychild_id_seq'::regclass),
    name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT categorychild_pkey PRIMARY KEY (id),
    CONSTRAINT categorychild_name_key UNIQUE (name)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.category_child
    OWNER to postgres;


-- Table: public.category_parent

-- DROP TABLE IF EXISTS public.category_parent;

CREATE TABLE IF NOT EXISTS public.category_parent
(
    id bigint NOT NULL DEFAULT nextval('categoryparent_id_seq'::regclass),
    name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT categoryparent_pkey PRIMARY KEY (id),
    CONSTRAINT categoryparent_name_key UNIQUE (name)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.category_parent
    OWNER to postgres;


-- Table: public.category_parent_child_relation

-- DROP TABLE IF EXISTS public.category_parent_child_relation;

CREATE TABLE IF NOT EXISTS public.category_parent_child_relation
(
    id bigint NOT NULL DEFAULT nextval('categoryparentchildrelations_id_seq'::regclass),
    full_name character varying(120) COLLATE pg_catalog."default" NOT NULL,
    parent_category_id bigint NOT NULL,
    child_category_id bigint NOT NULL,
    budget double precision,
    CONSTRAINT categoryparentchildrelations_pkey PRIMARY KEY (id),
    CONSTRAINT categoryparentchildrelations_fullname_key UNIQUE (full_name),
    CONSTRAINT categoryparentchildrelations_childcategoryid_fkey FOREIGN KEY (child_category_id)
        REFERENCES public.category_child (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT categoryparentchildrelations_parentcategoryid_fkey FOREIGN KEY (parent_category_id)
        REFERENCES public.category_parent (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.category_parent_child_relation
    OWNER to postgres;

-- Trigger: trg_set_fullname

-- DROP TRIGGER IF EXISTS trg_set_fullname ON public.category_parent_child_relation;

CREATE OR REPLACE TRIGGER trg_set_fullname
    BEFORE INSERT
    ON public.category_parent_child_relation
    FOR EACH ROW
    EXECUTE FUNCTION public.set_fullname();



-- Table: public.category_budget

-- DROP TABLE IF EXISTS public.category_budget;

CREATE SEQUENCE category_budget_id_seq;

CREATE TABLE IF NOT EXISTS public.category_budget
(
    id bigint NOT NULL DEFAULT nextval('category_budget_id_seq'::regclass),
    category_id bigint NOT NULL,
    year integer NOT NULL,
    month integer NOT NULL,
    budget_amount double precision NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    amount_spent double precision DEFAULT 0,
    CONSTRAINT unique_category_month_year UNIQUE (category_id, year, month),
    CONSTRAINT fk_category FOREIGN KEY (category_id)
        REFERENCES public.category_parent_child_relation (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.category_budget
    OWNER to postgres;




-- Table: public.expense

-- DROP TABLE IF EXISTS public.expense;

CREATE TABLE IF NOT EXISTS public.expense
(
    id bigint NOT NULL DEFAULT nextval('expenses_id_seq'::regclass),
    user_id bigint NOT NULL,
    amount double precision NOT NULL,
    category_id bigint NOT NULL,
    description character varying(255) COLLATE pg_catalog."default",
    due_date timestamp with time zone NOT NULL,
    notes character varying(255) COLLATE pg_catalog."default",
    account_id bigint NOT NULL,
    merchant character varying(255) COLLATE pg_catalog."default",
    recurring boolean DEFAULT false,
    frequency character varying(255) COLLATE pg_catalog."default",
    reviewed character varying(255) COLLATE pg_catalog."default" DEFAULT 'Unreviewed'::character varying,
    date timestamp(6) without time zone,
    CONSTRAINT expenses_pkey PRIMARY KEY (id),
    CONSTRAINT expenses_bankaccountid_fkey FOREIGN KEY (account_id)
        REFERENCES public.bank_account (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE RESTRICT,
    CONSTRAINT expenses_categoryid_fkey FOREIGN KEY (category_id)
        REFERENCES public.category_parent_child_relation (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT expenses_userid_fkey FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.expense
    OWNER to postgres;


-- Table: public.goal

-- DROP TABLE IF EXISTS public.goal;

CREATE TABLE IF NOT EXISTS public.goal
(
    id bigint NOT NULL DEFAULT nextval('goals_id_seq'::regclass),
    user_id bigint NOT NULL,
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    target_amount double precision NOT NULL,
    saved_amount double precision DEFAULT 0,
    due_date timestamp with time zone,
    notes character varying(255) COLLATE pg_catalog."default",
    priority character varying(255) COLLATE pg_catalog."default",
    reviewed character varying(20) COLLATE pg_catalog."default" DEFAULT 'Unreviewed'::character varying,
    CONSTRAINT goals_pkey PRIMARY KEY (id),
    CONSTRAINT goals_userid_fkey FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.goal
    OWNER to postgres;


-- Table: public.income

-- DROP TABLE IF EXISTS public.income;

CREATE TABLE IF NOT EXISTS public.income
(
    id bigint NOT NULL DEFAULT nextval('income_id_seq'::regclass),
    user_id bigint NOT NULL,
    amount double precision NOT NULL,
    category_id bigint NOT NULL,
    source character varying(255) COLLATE pg_catalog."default",
    date timestamp(6) without time zone NOT NULL,
    notes character varying(255) COLLATE pg_catalog."default",
    recurring boolean DEFAULT false,
    frequency character varying(255) COLLATE pg_catalog."default",
    reviewed character varying(255) COLLATE pg_catalog."default" DEFAULT 'Unreviewed'::character varying,
    CONSTRAINT income_pkey PRIMARY KEY (id),
    CONSTRAINT income_categoryid_fkey FOREIGN KEY (category_id)
        REFERENCES public.category_parent_child_relation (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT income_userid_fkey FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.income
    OWNER to postgres;


-- Table: public.shared_bill

-- DROP TABLE IF EXISTS public.shared_bill;

CREATE TABLE IF NOT EXISTS public.shared_bill
(
    id bigint NOT NULL DEFAULT nextval('sharedbills_id_seq'::regclass),
    bill_id bigint NOT NULL,
    user_id bigint NOT NULL,
    name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    person_a_portion double precision NOT NULL,
    person_b_portion double precision NOT NULL,
    is_percentage boolean NOT NULL,
    percentage_split double precision,
    notes text COLLATE pg_catalog."default",
    reviewed character varying(20) COLLATE pg_catalog."default" DEFAULT 'Unreviewed'::character varying,
    CONSTRAINT sharedbills_pkey PRIMARY KEY (id),
    CONSTRAINT sharedbills_name_key UNIQUE (name),
    CONSTRAINT sharedbills_billid_fkey FOREIGN KEY (bill_id)
        REFERENCES public.bill (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT sharedbills_userid_fkey FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.shared_bill
    OWNER to postgres;


-- Table: public.transaction

-- DROP TABLE IF EXISTS public.transaction;

CREATE TABLE IF NOT EXISTS public.transaction
(
    id bigint NOT NULL DEFAULT nextval('transactions_id_seq'::regclass),
    account_id bigint NOT NULL,
    amount double precision NOT NULL,
    category_id bigint NOT NULL,
    description character varying(255) COLLATE pg_catalog."default",
    time_of_transaction timestamp without time zone NOT NULL,
    notes character varying(255) COLLATE pg_catalog."default",
    merchant character varying(255) COLLATE pg_catalog."default",
    recurring boolean DEFAULT false,
    frequency character varying(255) COLLATE pg_catalog."default",
    included boolean DEFAULT true,
    reviewed character varying(255) COLLATE pg_catalog."default" DEFAULT 'Unreviewed'::character varying,
    type character varying(255) COLLATE pg_catalog."default" NOT NULL DEFAULT 'Debit'::character varying,
    user_id bigint NOT NULL,
    is_planned boolean NOT NULL DEFAULT false,
    planned_amount double precision NOT NULL DEFAULT 0.00,
    has_split boolean NOT NULL DEFAULT false,
    account_balance double precision DEFAULT 0,
    CONSTRAINT transactions_pkey PRIMARY KEY (id),
    CONSTRAINT fk_user FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT transactions_bankaccountid_fkey FOREIGN KEY (account_id)
        REFERENCES public.bank_account (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT transactions_categoryid_fkey FOREIGN KEY (category_id)
        REFERENCES public.category_parent_child_relation (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.transaction
    OWNER to postgres;
-- Index: idx_transaction_time

-- DROP INDEX IF EXISTS public.idx_transaction_time;

CREATE INDEX IF NOT EXISTS idx_transaction_time
    ON public.transaction USING btree
    (account_id ASC NULLS LAST, time_of_transaction ASC NULLS LAST)
    TABLESPACE pg_default;

-- Trigger: before_insert_trigger__update_account_balance_on_insert

-- DROP TRIGGER IF EXISTS before_insert_trigger__update_account_balance_on_insert ON public.transaction;

CREATE OR REPLACE TRIGGER before_insert_trigger__update_account_balance_on_insert
    BEFORE INSERT
    ON public.transaction
    FOR EACH ROW
    EXECUTE FUNCTION public.update_account_balance_on_insert();

ALTER TABLE public.transaction
    DISABLE TRIGGER before_insert_trigger__update_account_balance_on_insert;

-- Trigger: before_update_trigger__update_account_balance_on_update

-- DROP TRIGGER IF EXISTS before_update_trigger__update_account_balance_on_update ON public.transaction;

CREATE OR REPLACE TRIGGER before_update_trigger__update_account_balance_on_update
    BEFORE UPDATE 
    ON public.transaction
    FOR EACH ROW
    EXECUTE FUNCTION public.update_account_balance_on_update();

ALTER TABLE public.transaction
    DISABLE TRIGGER before_update_trigger__update_account_balance_on_update;

-- Trigger: test_trigger_update_balance_on_insert

-- DROP TRIGGER IF EXISTS test_trigger_update_balance_on_insert ON public.transaction;

CREATE OR REPLACE TRIGGER test_trigger_update_balance_on_insert
    BEFORE INSERT
    ON public.transaction
    FOR EACH ROW
    EXECUTE FUNCTION public.test_update_balance_on_insert();

-- Trigger: test_trigger_update_balance_on_update

-- DROP TRIGGER IF EXISTS test_trigger_update_balance_on_update ON public.transaction;

CREATE OR REPLACE TRIGGER test_trigger_update_balance_on_update
    BEFORE UPDATE 
    ON public.transaction
    FOR EACH ROW
    EXECUTE FUNCTION public.test_update_balance_on_update();


-- Table: public.transaction_split

-- DROP TABLE IF EXISTS public.transaction_split;

CREATE TABLE IF NOT EXISTS public.transaction_split
(
    id bigint NOT NULL DEFAULT nextval('transactionsplit_id_seq'::regclass),
    transaction_id bigint NOT NULL,
    category_id bigint NOT NULL,
    amount double precision NOT NULL,
    CONSTRAINT transactionsplit_pkey PRIMARY KEY (id),
    CONSTRAINT fk_category FOREIGN KEY (category_id)
        REFERENCES public.category_parent_child_relation (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_transaction FOREIGN KEY (transaction_id)
        REFERENCES public.transaction (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.transaction_split
    OWNER to postgres;


-- Table: public.type

-- DROP TABLE IF EXISTS public.type;

CREATE TABLE IF NOT EXISTS public.type
(
    id bigint NOT NULL DEFAULT nextval('type_id_seq'::regclass),
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT type_pkey PRIMARY KEY (id),
    CONSTRAINT type_name_key UNIQUE (name)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.type
    OWNER to postgres;


-- Table: public.users

-- DROP TABLE IF EXISTS public.users;

CREATE TABLE IF NOT EXISTS public.users
(
    id bigint NOT NULL DEFAULT nextval('users_id_seq'::regclass),
    first_name character varying(120) COLLATE pg_catalog."default" NOT NULL,
    last_name character varying(120) COLLATE pg_catalog."default" NOT NULL,
    username character varying(50) COLLATE pg_catalog."default" NOT NULL,
    password_hash character varying(255) COLLATE pg_catalog."default" NOT NULL,
    email character varying(100) COLLATE pg_catalog."default" NOT NULL,
    phone_number character varying(15) COLLATE pg_catalog."default",
    address text COLLATE pg_catalog."default",
    date_of_birth timestamp with time zone,
    created_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    last_login timestamp without time zone,
    profile_picture text COLLATE pg_catalog."default",
    role character varying(20) COLLATE pg_catalog."default" DEFAULT 'user'::character varying,
    is_active boolean DEFAULT true,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT users_email_key UNIQUE (email),
    CONSTRAINT users_username_key UNIQUE (username)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.users
    OWNER to postgres;

    







-- FUNCTIONS

-- FUNCTION: public.insert_category_if_not_exists(character varying, integer)

-- DROP FUNCTION IF EXISTS public.insert_category_if_not_exists(character varying, integer);

CREATE OR REPLACE FUNCTION public.insert_category_if_not_exists(
	category_name character varying,
	parent_category_id integer)
    RETURNS void
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM Categories WHERE CategoryName = category_name) THEN
        INSERT INTO Categories (CategoryName, ParentCategoryId) VALUES (category_name, parent_category_id);
    END IF;
END;
$BODY$;

ALTER FUNCTION public.insert_category_if_not_exists(character varying, integer)
    OWNER TO postgres;



-- FUNCTION: public.predict_future_balance(integer, date)

-- DROP FUNCTION IF EXISTS public.predict_future_balance(integer, date);

CREATE OR REPLACE FUNCTION public.predict_future_balance(
	account_id integer,
	prediction_date date)
    RETURNS numeric
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
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
$BODY$;

ALTER FUNCTION public.predict_future_balance(integer, date)
    OWNER TO postgres;






-- TRIGGER FUNCTIONS

-- FUNCTION: public.set_fullname()

-- DROP FUNCTION IF EXISTS public.set_fullname();

CREATE OR REPLACE FUNCTION public.set_fullname()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
BEGIN
    NEW.Full_Name := (SELECT p.Name || ' - ' || c.Name
                     FROM Category_Parent p, Category_Child c
                     WHERE p.Id = NEW.Parent_Category_Id
                     AND c.Id = NEW.Child_Category_Id);
    RETURN NEW;
END;
$BODY$;

ALTER FUNCTION public.set_fullname()
    OWNER TO postgres;


-- FUNCTION: public.update_account_balance_on_insert()

-- DROP FUNCTION IF EXISTS public.update_account_balance_on_insert();

CREATE OR REPLACE FUNCTION public.update_account_balance_on_insert()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
DECLARE
    current_balance numeric;
BEGIN
    -- Fetch the current balance of the related bank account
    SELECT current_balance INTO current_balance
    FROM bank_account
    WHERE id = NEW.account_id;
    
    -- Update the bank account balance by adding the transaction amount
    UPDATE bank_account
    SET current_balance = current_balance + NEW.amount
    WHERE id = NEW.account_id;
    
    -- Set the account_balance in the transaction to the updated balance
    NEW.account_balance = current_balance + NEW.amount;
    
    RETURN NEW;
END;
$BODY$;

ALTER FUNCTION public.update_account_balance_on_insert()
    OWNER TO postgres;



-- FUNCTION: public.update_account_balance_on_update()

-- DROP FUNCTION IF EXISTS public.update_account_balance_on_update();

CREATE OR REPLACE FUNCTION public.update_account_balance_on_update()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
DECLARE
    old_balance numeric;
    new_balance numeric;
BEGIN
    -- Fetch the current balance of the related bank account before the update
    SELECT current_balance INTO old_balance
    FROM bank_account
    WHERE id = OLD.account_id;

    -- Revert the balance by subtracting the old transaction amount
    UPDATE bank_account
    SET current_balance = current_balance - OLD.amount
    WHERE id = OLD.account_id;

    -- Update the bank account balance by adding the new transaction amount
    UPDATE bank_account
    SET current_balance = current_balance + NEW.amount
    WHERE id = NEW.account_id;

    -- Fetch the new balance after the update
    SELECT current_balance INTO new_balance
    FROM bank_account
    WHERE id = NEW.account_id;

    -- Set the account_balance in the transaction to the updated balance
    NEW.account_balance = new_balance;

    RETURN NEW;
END;
$BODY$;

ALTER FUNCTION public.update_account_balance_on_update()
    OWNER TO postgres;





-- TRIGGERS

-- Trigger: test_trigger_update_balance_on_insert

-- DROP TRIGGER IF EXISTS test_trigger_update_balance_on_insert ON public.transaction;

CREATE OR REPLACE TRIGGER test_trigger_update_balance_on_insert
    BEFORE INSERT
    ON public.transaction
    FOR EACH ROW
    EXECUTE FUNCTION public.test_update_balance_on_insert();


-- Trigger: test_trigger_update_balance_on_update

-- DROP TRIGGER IF EXISTS test_trigger_update_balance_on_update ON public.transaction;

CREATE OR REPLACE TRIGGER test_trigger_update_balance_on_update
    BEFORE UPDATE 
    ON public.transaction
    FOR EACH ROW
    EXECUTE FUNCTION public.test_update_balance_on_update();