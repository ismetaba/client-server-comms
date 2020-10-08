package test.clientserver;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

public class DatabaseTest {
    private String url = "jdbc:mysql://192.168.64.2:3306/";
    private String db = "messages";
    private String userName ="root2";
    private String password = "111";
    private Connection connect;

    /**
     * this method text connection and tables
     * @throws SQLException
     */
    @Test
    void TestDatabaseTables() throws SQLException {
        connect = connectDatabase();
        connect.createStatement().executeQuery("select receiver, sender, subject, cc, message, priority from low");// checks low table
        connect.createStatement().executeQuery("select receiver, sender, subject, cc, message, priority from high");// checks normal table
        connect.createStatement().executeQuery("select receiver, sender, subject, cc, message, priority from high");// checks high table
        connect.close();
    }

    Connection connectDatabase() throws SQLException {
        return DriverManager.getConnection(url + db +"?user="+ userName +"&password=" + password);
    }
}