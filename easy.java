import java.sql.*;
import java.util.Scanner;

public class JavaApplication1 {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/company";
        String user = "root";
        String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter Employee Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter Employee Salary: ");
            double salary = scanner.nextDouble();

            String insertQuery = "INSERT INTO Employee (Name, Salary) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertQuery);
            pstmt.setString(1, name);
            pstmt.setDouble(2, salary);
            pstmt.executeUpdate();

            String selectQuery = "SELECT * FROM Employee";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(selectQuery);

            System.out.println("\nEmpID | Name       | Salary");
            System.out.println("---------------------------");
            while (rs.next()) {
                System.out.printf("%-5d | %-10s | %.2f%n", rs.getInt("EmpID"), rs.getString("Name"), rs.getDouble("Salary"));
            }

            rs.close();
            stmt.close();
            pstmt.close();
            conn.close();
            scanner.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
