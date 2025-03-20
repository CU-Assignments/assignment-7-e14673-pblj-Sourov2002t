import java.sql.*;
import java.util.Scanner;

public class Main {
    private static final String URL = "jdbc:mysql://localhost:3306/company";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            conn.setAutoCommit(false);
            Scanner scanner = new Scanner(System.in);
            int choice;

            do {
                System.out.println("\n--- Product Management ---");
                System.out.println("1. Add Product");
                System.out.println("2. View Products");
                System.out.println("3. Update Product");
                System.out.println("4. Delete Product");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1: addProduct(conn, scanner); break;
                    case 2: viewProducts(conn); break;
                    case 3: updateProduct(conn, scanner); break;
                    case 4: deleteProduct(conn, scanner); break;
                    case 5: System.out.println("Exiting..."); break;
                    default: System.out.println("Invalid choice. Try again.");
                }
            } while (choice != 5);

            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addProduct(Connection conn, Scanner scanner) {
        try {
            System.out.print("Enter Product Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter Price: ");
            double price = scanner.nextDouble();
            System.out.print("Enter Quantity: ");
            int quantity = scanner.nextInt();

            String sql = "INSERT INTO Product (ProductName, Price, Quantity) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name);
                pstmt.setDouble(2, price);
                pstmt.setInt(3, quantity);
                pstmt.executeUpdate();
                conn.commit();
                System.out.println("Product added successfully.");
            }
        } catch (Exception e) {
            rollback(conn);
            e.printStackTrace();
        }
    }

    private static void viewProducts(Connection conn) {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Product")) {
            System.out.println("\nProductID | ProductName       | Price  | Quantity");
            System.out.println("------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-9d | %-15s | %.2f | %d%n",
                        rs.getInt("ProductID"), rs.getString("ProductName"),
                        rs.getDouble("Price"), rs.getInt("Quantity"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateProduct(Connection conn, Scanner scanner) {
        try {
            System.out.print("Enter Product ID to update: ");
            int id = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter New Product Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter New Price: ");
            double price = scanner.nextDouble();
            System.out.print("Enter New Quantity: ");
            int quantity = scanner.nextInt();

            String sql = "UPDATE Product SET ProductName=?, Price=?, Quantity=? WHERE ProductID=?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name);
                pstmt.setDouble(2, price);
                pstmt.setInt(3, quantity);
                pstmt.setInt(4, id);
                int updatedRows = pstmt.executeUpdate();
                if (updatedRows > 0) {
                    conn.commit();
                    System.out.println("Product updated successfully.");
                } else {
                    System.out.println("Product ID not found.");
                }
            }
        } catch (Exception e) {
            rollback(conn);
            e.printStackTrace();
        }
    }

    private static void deleteProduct(Connection conn, Scanner scanner) {
        try {
            System.out.print("Enter Product ID to delete: ");
            int id = scanner.nextInt();

            String sql = "DELETE FROM Product WHERE ProductID=?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                int deletedRows = pstmt.executeUpdate();
                if (deletedRows > 0) {
                    conn.commit();
                    System.out.println("Product deleted successfully.");
                } else {
                    System.out.println("Product ID not found.");
                }
            }
        } catch (Exception e) {
            rollback(conn);
            e.printStackTrace();
        }
    }

    private static void rollback(Connection conn) {
        try {
            conn.rollback();
            System.out.println("Transaction rolled back.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
