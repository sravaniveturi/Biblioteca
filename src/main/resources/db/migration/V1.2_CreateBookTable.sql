CREATE TABLE IF NOT EXISTS book(
        id bigint,
        bookName varchar(100) NOT NULL,
        authorName varchar(100),
        yearOfPublishing numeric NOT NULL,
        numOfCopies numeric NOT NULL,
        PRIMARY KEY (id)
);

