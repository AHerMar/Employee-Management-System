create database your-database-name;

#use your-database-name;

create table Employee (
    id int primary key NOT NULL,
    first_name varchar(100) NOT NULL,
    last_name varchar(100) NOT NULL
);

CREATE TABLE Trainings (
    PN_ID VARCHAR(13) PRIMARY KEY,
    PN_DESCRIPTION TEXT,
    latest_revision VARCHAR(10),
    assembly_description TEXT
);

CREATE TABLE Employee_Training (
    id INT,
    PN_ID VARCHAR(13),
    trained_revision_1 varchar(10),
    trained_revision_2 varchar(10),
    trained_revision_3 varchar(10),
    trained_revision_4 varchar(10),
    trained_revision_5 varchar(10),
    trained_revision_6 varchar(10),
    trained_revision_7 varchar(10),
    trained_revision_8 varchar(10),
    trained_revision_9 varchar(10),
    trained_revision_10 varchar(10),
    PRIMARY KEY (id, PN_ID),
    FOREIGN KEY (id) REFERENCES Employee(id),
    FOREIGN KEY (PN_ID) REFERENCES Trainings(PN_ID)
);
