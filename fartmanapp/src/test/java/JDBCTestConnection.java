import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertNotNull;

public class JDBCTestConnection {
    private Connection con;
    private String name;
    private String url;
    private String password;
    private String driver;

    public JDBCTestConnection() {
        this.name = "postgres";
        this.password = "asqwzdef";
        this.url = "jdbc:postgresql://127.0.0.1:5432/graber";
        this.driver = "org.postgresql.Driver";
    }

    public Connection getConnection() {
        try {
            Connection conn = null;
            Properties connectionProps = new Properties();
            connectionProps.put("user", this.name);
            connectionProps.put("password", this.password);


            conn = DriverManager.getConnection(
                    url,
                    connectionProps);

            System.out.println("Connected to database");
            return conn;
        } catch (SQLException ex) {
            Logger.getLogger(JDBCTestConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Test
    public void testConnection() {
        JDBCTestConnection jdbcTestConnection = new JDBCTestConnection();
        assertNotNull(jdbcTestConnection.getConnection());
    }
}