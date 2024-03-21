import com.application.dashboard.ReadFile;
import com.application.database.DbConnection;
import com.application.files.FileChooserWindow;
import com.application.files.FilePathHandler;
import com.application.login.LoginApplication;
import com.application.login.LoginController;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.testng.AssertJUnit.*;
import static org.testng.AssertJUnit.assertFalse;
public class BasicTest {

    LoginController lc = new LoginController();
    FilePathHandler fph = new FilePathHandler();
    FileChooserWindow fc = new FileChooserWindow();
    ReadFile rc = new ReadFile();

    DbConnection dbConnection = new DbConnection();

    @Test
    public void testLogin() throws SQLException {
        assertTrue("Login successful.",lc.checkUser("user","0000"));
        assertTrue("Login successful.",lc.checkAdmin("admin","0000"));
        assertFalse("Login not successful.",lc.checkUser("gesh","1234"));
        assertFalse("Login not successful.",lc.checkAdmin("baba","5555"));
    }

    @Test
    public void testSQLConnection() throws Exception {
        dbConnection.makeConn();
        assertTrue("Connection is established", dbConnection.checkConn());
        assertTrue("Connection is established", dbConnection.checkConn());
        assertTrue("Connection is established", dbConnection.checkConn());
        assertTrue("Connection is established", dbConnection.checkConn());
        dbConnection.closeConnection();
        assertFalse("Connection is close", dbConnection.checkConn());
        assertFalse("Connection is close", dbConnection.checkConn());
        assertFalse("Connection is close", dbConnection.checkConn());
        assertFalse("Connection is close", dbConnection.checkConn());
        assertFalse("Connection is close", dbConnection.checkConn());
        dbConnection.makeConn();
        assertTrue("Connection is established", dbConnection.checkConn());
        assertTrue("Connection is established", dbConnection.checkConn());
        assertTrue("Connection is established", dbConnection.checkConn());
        assertTrue("Connection is established", dbConnection.checkConn());
    }

    @Test
    public void readFile(){

    }



}
