import com.application.dashboard.ReadFile;
import com.application.database.DbConnection;
import com.application.database.UserManager;

import com.application.database.DataManager;

import com.application.files.FileChooserWindow;
import com.application.files.FilePathHandler;
import com.application.login.LoginApplication;
import com.application.login.LoginController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testng.AssertJUnit.*;
import static org.testng.AssertJUnit.assertFalse;
public class BasicTest {

    LoginController lc = new LoginController();
    FilePathHandler fph = new FilePathHandler();
    FileChooserWindow fc = new FileChooserWindow();
    ReadFile rc = new ReadFile();

    DbConnection dbConnection = new DbConnection();
    UserManager um = new UserManager();

    @Test
    public void testLogin() throws SQLException {
        assertTrue("Login successfull for user.",um.selectUser("gesh","1234"));
        assertTrue("Login successfull for admin.",um.selectAdmin("gesh2","1234"));
        assertFalse("Fakse user can't login.", um.selectUser("14sdfs","aesfsdfsd") );
        assertFalse("Fakse user can't login.", um.selectAdmin("4433","dfggg") );
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
}
