# Description
The Employee Training Management System is a Java-based console application designed to manage employee records, training programs, and their progress within an organization.
It uses MySQL as the backend database to handle employee and training data.

# Features
## Employee Management:
* Add, update, and delete employee records.
## Training Management:
* Create, modify, and remove training programs.
## Progress Tracking:
* Track and update employees' progress in multiple training programs.
* Record completion levels and dates for training milestones.
# Database Integration:
* Secure integration with MySQL using JDBC.

# Setup Instructions
## Prerequisites:
Java Development Kit (JDK) installed.
MySQL Server installed and running.
MySQL JDBC Driver downloaded and added to the project.
## Steps to Run the Project:
1. Clone the repository:
* bash
* Copy
* Edit
* git clone https://github.com/yourusername/employee-training-management.git
2. Open the project in your IDE.
3. Import the MySQL JDBC Driver.
4. Set up the database:
* Execute the schema.sql file (provided in the repository) to create the database and tables.
5. Update the database connection details in the DBConnection class:
* java
* Copy
* Edit
* private static final String URL = "jdbc:mysql://localhost:3306/EmployeeDB";
* private static final String USER = "your-username";
* private static final String PASSWORD = "your-password";
6. Compile and run the application

