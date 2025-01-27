import java.sql.*;
import java.util.Scanner;


public class EmployeeDatabase {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/EmployeeDB";
    private static final String USER = "root";
    private static final String PASSWORD = "Fero21!@23";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            Scanner sc = new Scanner(System.in);
            while (true) {
                System.out.println("\n--- Employee Database Menu ---");
                System.out.println("1. Create Employee");
                System.out.println("2. View Employee");
                System.out.println("3. Update Employee");
                System.out.println("4. Delete Employee");
                System.out.println("5. Search Employee");
                System.out.println("6. Add New Training");
                System.out.println("7. Assign Training to Employee");
                System.out.println("8. Update Training Completion Level");
                System.out.println("9. View Employee Training Progress");
                System.out.println("10. Get Training Details");
                System.out.println("11. Modify Training Details");
                System.out.println("12. Delete Training Details");
                System.out.println("13. Exit");
                System.out.print("Select an option by typing a number 1-13: ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        insertEmployee(connection, sc);
                        break;
                    case 2:
                        viewEmployees(connection);
                        break;
                    case 3:
                        updateEmployee(connection, sc);
                        break;
                    case 4:
                        deleteEmployee(connection, sc);
                        break;
                    case 5:
                        searchEmployee(connection, sc);
                        break;
                    case 6:
                        addTraining(connection,sc);
                        break;
                    case 7:
                        assignTraining(connection, sc);
                        break;
                    case 8:
                        updateCompletionLevel(connection, sc);
                        break;
                    case 9:
                        viewTrainingProgress(connection, sc);
                        break;
                    case 10:
                        getTrainingDetails(connection, sc);
                        break;
                    case 11:
                        modifyTraining(connection, sc);
                        break;
                    case 12:
                        deleteTraining(connection, sc);
                        break;
                    case 13:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid option! Please try again! ");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static void insertEmployee(Connection connection, Scanner sc) {
        try {
            System.out.print("Enter Employee ID: ");
            int id = sc.nextInt();
            System.out.print("Enter Employee's First Name: ");
            String first_name = sc.next();
            System.out.print("Enter Employee's Last Name: ");
            String last_name = sc.next();

            String sql = "INSERT INTO Employee (id, first_name, last_name) VALUES (?, ?, ?) ";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, id);
                stmt.setString(2, first_name);
                stmt.setString(3, last_name);
                stmt.executeUpdate();
                System.out.println("Employee added successfully");
            } catch (SQLException e) {
//            if (e.getErrorCode() == 1062) {
//                System.out.println("Error: Employee ID already exists. Please use a unique ID.");
//            } else {
                e.printStackTrace();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private static void viewEmployees(Connection connection) {
        try {
            String sql = "SELECT * FROM Employee";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                System.out.println("\n--- Employee List ---");
                while (rs.next()) {
                    System.out.printf("ID: %d, First Name: %s, Last Name: %s\n" ,
                            rs.getInt("id"), rs.getString("first_name"),
                            rs.getString("last_name"));
                    System.out.println("----------------------");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateEmployee(Connection connection, Scanner sc) {
        try {
            System.out.print("Enter Employee ID to update: ");
            int id = sc.nextInt();

            System.out.print("Enter new first name: ");
            String first_name = sc.next();
            System.out.print("Enter new last name:");
            String last_name = sc.next();


            String sql = "UPDATE Employee SET first_name = ?, last_name = ? WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, first_name);
                stmt.setString(2, last_name);
                stmt.setInt(3, id);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Employee updated successfully");
                } else {
                    System.out.println("Employee not found.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteEmployee(Connection connection, Scanner sc) {
        try {
            System.out.print("Enter Employee ID to delete: ");
            int id = sc.nextInt();

            //Delete associated records in Employee Training
            String deleteTrainingsql = "DELETE FROM Employee_Training WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deleteTrainingsql)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
            //Delete employee record
            String deleteEmployeeesql = "DELETE FROM Employee WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deleteEmployeeesql)) {
                stmt.setInt(1, id);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Employee deleted successfully");
                } else {
                    System.out.println("Employee not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void searchEmployee(Connection connection, Scanner sc) {
    try {
        System.out.println("Search by:");
        System.out.println("1. Name");
        System.out.println("2. ID");
        System.out.print("Choose an option: ");
        int option = sc.nextInt();

        String sql;
        if (option == 1) {
            System.out.print("Enter first name to search: ");
            String name = sc.next();
            sql = "SELECT * FROM Employee WHERE first_name LIKE ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, "%" + name + "%");
                executeSearch(stmt);
            }
        } else if (option == 2) {
            System.out.print("Enter ID to search: ");
            int id = sc.nextInt();
            sql = "SELECT * FROM Employee WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, id);
                executeSearch(stmt);
            }
        } else {
            System.out.println("Invalid option.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    }

    private static void executeSearch(PreparedStatement stmt) throws SQLException {
        try (ResultSet rs = stmt.executeQuery()) {
            System.out.println("\n--- Search results ---");
            boolean found = false;
            while (rs.next()) {
                System.out.printf("ID: %d, First Name: %s, Last Name: %s",
                        rs.getInt("id"), rs.getString("first_name"),
                        rs.getString("last_name"));
                found = true;
            }
            if (!found) {
                System.out.println("Employee not found.");
            }
        }
    }

    private static void addTraining(Connection connection, Scanner sc) throws SQLException {
        System.out.print("Enter PN ID: ");
        String PN_id = sc.next();
        System.out.print("Enter PN description: ");
        sc.nextLine();
        String PN_description = sc.nextLine();
        System.out.print("Enter the latest revision: ");
        String latest_revision = sc.next();
        System.out.print("Enter the assembly description: ");
        sc.nextLine();
        String assembly_description = sc.nextLine();

        String sql = "INSERT INTO Trainings (PN_ID, PN_DESCRIPTION, latest_revision, assembly_description) VALUES (?,?,?,?) ";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, PN_id);
            stmt.setString(2, PN_description);
            stmt.setString(3, latest_revision);
            stmt.setString(4, assembly_description);
            stmt.executeUpdate();
            System.out.println("Training added successfully");
        }
            //System.out.println("Training not found.");
    }

    private static void deleteTraining(Connection connection, Scanner sc) throws SQLException {
        System.out.print("Enter PN ID to delete: ");
        String PN_id = sc.next();

        //SQL to delete training
        // Delete associated records in Employee_Training
        String deleteTrainingAssociationsSql = "DELETE FROM Employee_Training WHERE PN_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteTrainingAssociationsSql)) {
            stmt.setString(1, PN_id);
            stmt.executeUpdate();
        }

        // Delete the training record
        String deleteTrainingSql = "DELETE FROM Trainings WHERE PN_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteTrainingSql)) {
            stmt.setString(1, PN_id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Training deleted successfully.");
            } else {
                System.out.println("No training found with the given ID.");
            }
        }
    }

    private static void modifyTraining(Connection connection, Scanner sc) throws SQLException {
        System.out.print("Enter PN ID to modify: ");
        String PN_id = sc.next();
        System.out.print("Enter new PN description: ");
        sc.nextLine();
        String PN_description = sc.nextLine();
        System.out.print("Enter updated latest revision: ");
        String latest_revision = sc.next();
        System.out.print("Enter updated assembly description: ");
        sc.nextLine();
        String assembly_description = sc.nextLine();

        String sql = "UPDATE Trainings " +
                "SET PN_DESCRIPTION = ?, latest_revision = ?, assembly_description = ? " +
                "WHERE PN_ID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, PN_description);
            stmt.setString(2, latest_revision);
            stmt.setString(3, assembly_description);
            stmt.setString(4, PN_id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Training updated successfully");
            } else {
                System.out.println("Training not found with the given ID.");
            }
        }


    }

    private static void getTrainingDetails(Connection connection, Scanner sc) throws SQLException {
        System.out.print("Enter PN ID to search: ");
        String PN_id = sc.next();
        String sql = "SELECT * FROM Trainings WHERE PN_ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, PN_id);
            executeTrainingSearch(stmt);
        }

    }

    private static void executeTrainingSearch(PreparedStatement stmt) throws SQLException {
        try (ResultSet rs = stmt.executeQuery()) {
            System.out.println("\n--- Search results ---");
            boolean found = false;
            while (rs.next()) {
                System.out.printf("PN: %s, PN_DESCRIPTION: %s, Latest Revision: %s, Assembly Description: %s",
                        rs.getString("PN_ID"), rs.getString("PN_DESCRIPTION"), rs.getString("latest_revision"),
                        rs.getString("assembly_description"));
                found = true;
            }
            if (!found) {
                System.out.println("Training not found.");
            }
        }
    }

    private static void assignTraining(Connection connection, Scanner sc) throws SQLException {
        System.out.print("Enter employee ID: ");
        int id = sc.nextInt();
        System.out.print("Enter training ID (PN-ID): ");
        String PN_id = sc.next();
        System.out.print("Did they complete trained revision 1? (Y/N): ");
        String trained_revision_1 = sc.next();
        System.out.print("Did they complete trained revision 2? (Y/N): ");
        String trained_revision_2 = sc.next();
        System.out.print("Did they complete trained revision 3? (Y/N): ");
        String trained_revision_3 = sc.next();
        System.out.print("Did they complete trained revision 4? (Y/N): ");
        String trained_revision_4 = sc.next();
        System.out.print("Did they complete trained revision 5? (Y/N): ");
        String trained_revision_5 = sc.next();
        System.out.print("Did they complete trained revision 6? (Y/N): ");
        String trained_revision_6 = sc.next();
        System.out.print("Did they complete trained revision 7? (Y/N): ");
        String trained_revision_7 = sc.next();
        System.out.print("Did they complete trained revision 8? (Y/N): ");
        String trained_revision_8 = sc.next();
        System.out.print("Did they complete trained revision 9? (Y/N): ");
        String trained_revision_9 = sc.next();
        System.out.print("Did they complete trained revision 10? (Y/N): ");
        String trained_revision_10 = sc.next();

        String sql = "INSERT INTO Employee_Training (id, PN_ID, trained_revision_1, trained_revision_2, trained_revision_3," +
                "trained_revision_4, trained_revision_5, trained_revision_6, trained_revision_7, trained_revision_8," +
                "trained_revision_9, trained_revision_10) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setString(2, PN_id);
            stmt.setString(3, trained_revision_1);
            stmt.setString(4, trained_revision_2);
            stmt.setString(5, trained_revision_3);
            stmt.setString(6, trained_revision_4);
            stmt.setString(7, trained_revision_5);
            stmt.setString(8, trained_revision_6);
            stmt.setString(9, trained_revision_7);
            stmt.setString(10, trained_revision_8);
            stmt.setString(11, trained_revision_9);
            stmt.setString(12, trained_revision_10);
            stmt.executeUpdate();
            System.out.println("Training assigned to employee successfully");
        }
    }

    private static void updateCompletionLevel(Connection connection, Scanner sc) throws SQLException {
        System.out.print("Enter employee ID: ");
        int id = sc.nextInt();
        System.out.print("Enter PN_ID: ");
        String PN_id = sc.next();
        System.out.print("Enter completion level for each trained revision by typing either Y or N.");
        System.out.print("Enter new trained revision 1 (Y/N): ");
        String trained_revision_1 = sc.next();
        System.out.print("Enter new trained revision 2 (Y/N): ");
        String trained_revision_2 = sc.next();
        System.out.print("Enter new trained revision 3 (Y/N): ");
        String trained_revision_3 = sc.next();
        System.out.print("Enter new trained revision 4 (Y/N): ");
        String trained_revision_4 = sc.next();
        System.out.print("Enter new trained revision 5 (Y/N): ");
        String trained_revision_5 = sc.next();
        System.out.print("Enter new trained revision 6 (Y/N): ");
        String trained_revision_6 = sc.next();
        System.out.print("Enter new trained revision 7 (Y/N): ");
        String trained_revision_7 = sc.next();
        System.out.print("Enter new trained revision 8 (Y/N): ");
        String trained_revision_8 = sc.next();
        System.out.print("Enter new trained revision 9 (Y/N): ");
        String trained_revision_9 = sc.next();
        System.out.print("Enter new trained revision 10 (Y/N): ");
        String trained_revision_10 = sc.next();

        String sql = "UPDATE Employee_Training " +
                "SET trained_revision_1 = ?, trained_revision_2 = ?, trained_revision_3 = ?, trained_revision_4 = ?, " +
                "trained_revision_5 = ?, trained_revision_6 = ?, trained_revision_7 = ?, trained_revision_8 = ?, trained_revision_9 = ?, trained_revision_10 = ? " +
                "WHERE id = ? AND PN_ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, trained_revision_1);
            stmt.setString(2, trained_revision_2);
            stmt.setString(3, trained_revision_3);
            stmt.setString(4, trained_revision_4);
            stmt.setString(5, trained_revision_5);
            stmt.setString(6, trained_revision_6);
            stmt.setString(7, trained_revision_7);
            stmt.setString(8, trained_revision_8);
            stmt.setString(9, trained_revision_9);
            stmt.setString(10, trained_revision_10);
            stmt.setInt(11, id);
            stmt.setString(12, PN_id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Completion level updated successfully.");
            } else{
                System.out.println("No matching record found.");
            }
        }
    }

    private static void viewTrainingProgress(Connection connection, Scanner sc) throws SQLException {
        System.out.print("Enter employee ID: ");
        int id = sc.nextInt();

        String sql = "SELECT t.PN_DESCRIPTION, et.trained_revision_1, et.trained_revision_2, et.trained_revision_3, et.trained_revision_4, " +
                "et.trained_revision_5, et.trained_revision_6, et.trained_revision_7, et.trained_revision_8, et.trained_revision_9, " +
                "et.trained_revision_10 " +
                "FROM Employee_Training et " +
                "JOIN Trainings t ON et.PN_ID = t.PN_ID " +
                "WHERE et.id = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            System.out.println("Training Progress:");
            while (rs.next()) {
                String PN_Description = rs.getString("PN_DESCRIPTION");
                String trained_revision_1 = rs.getString("trained_revision_1");
                String trained_revision_2 = rs.getString("trained_revision_2");
                String trained_revision_3 = rs.getString("trained_revision_3");
                String trained_revision_4 = rs.getString("trained_revision_4");
                String trained_revision_5 = rs.getString("trained_revision_5");
                String trained_revision_6 = rs.getString("trained_revision_6");
                String trained_revision_7 = rs.getString("trained_revision_7");
                String trained_revision_8 = rs.getString("trained_revision_8");
                String trained_revision_9 = rs.getString("trained_revision_9");
                String trained_revision_10 = rs.getString("trained_revision_10");

                System.out.printf("Training: %s \n| Trained Revision 1: %s | Trained Revision 2: %s | Trained Revision 3: %s\n" +
                        "| Trained Revision 4: %s | Trained Revision 5: %s | Trained Revision 6: %s\n| Trained Revision 7: %s " +
                        "| Trained Revision 8: %s | Trained Revision 9: %s\n| Trained Revision 10: %s", PN_Description,
                        trained_revision_1, trained_revision_2, trained_revision_3, trained_revision_4, trained_revision_5
                , trained_revision_6, trained_revision_7, trained_revision_8, trained_revision_9, trained_revision_10);

                System.out.println("\n-----------------------------------");
            }
        }
    }
}











