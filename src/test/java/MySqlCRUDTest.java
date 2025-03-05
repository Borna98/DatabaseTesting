import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.sql.*;

import static org.testng.Assert.assertEquals;

public class MySqlCRUDTest {
    //private WebDriver driver;
    private static Connection connection;

    // MySQL Database Credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bank"; // Change to your DB name
    private static final String DB_USER = "root";  // Change to your MySQL username
    private static final String DB_PASSWORD = "";  // Change to your MySQL password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    @BeforeClass
    public void setUp() throws SQLException {
        // Establish database connection
        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    @Test(priority = 1)
    public void testInsertUser() throws SQLException {
        Statement stmt = connection.createStatement();
        //stmt.execute("CREATE TABLE users (id INT PRIMARY KEY, name VARCHAR(255));");
        stmt.execute("INSERT INTO customer (cust_id, name, gender) VALUES (5, 'SRSS', 1);");

        ResultSet rs = stmt.executeQuery("SELECT * FROM customer WHERE cust_id = 5;");
        rs.next();
        assertEquals("SRSS", rs.getString("name"));
    }

    @Test(priority = 2)
    public static void selectCustomers() {
        String query = "SELECT * FROM customer";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("cust_id") +
                        ", Name: " + rs.getString("name") +
                        ", Gender: " + rs.getInt("gender") +
                        ", Created At: " + rs.getTimestamp("creted_at"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test(priority = 3)
    public void testUpdatetUser() throws SQLException {
        Statement stmt = connection.createStatement();
        //stmt.execute("CREATE TABLE users (id INT PRIMARY KEY, name VARCHAR(255));");
        stmt.execute("UPDATE customer SET name = 'SRS' WHERE cust_id = 2");

        ResultSet rs = stmt.executeQuery("SELECT * FROM customer WHERE cust_id = 2;");
        rs.next();

        try {
            assertEquals("SRS", rs.getString("name"));
            System.out.println("Actual and Expected Matched");
        } catch(Throwable t) {
            org.testng.Assert.fail("Expected and Actual result do not match");
        }

    }

    @Test(priority = 4)
    public static void selectBorrower() {
        String query = "SELECT * FROM borrower";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (!rs.isBeforeFirst()) {
                System.out.println("No records found in the borrower table.");
                return;
            }

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("cust_id") +
                        ", Loan No: " + rs.getString("loan_no"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test(priority = 5)
    public void testInsertBorrower() throws SQLException {
        Statement stmt = connection.createStatement();
        //stmt.execute("CREATE TABLE users (id INT PRIMARY KEY, name VARCHAR(255));");
        stmt.execute("INSERT INTO borrower (cust_id, loan_no) VALUES (4, 555);");

        ResultSet rs = stmt.executeQuery("SELECT * FROM borrower WHERE cust_id = 4;");
        rs.next();
        assertEquals("555", rs.getString("loan_no"));
    }

    @AfterClass
    public void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
