import com.application.dashboard.ReadFile;
import com.application.files.FileChooser;
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
    FileChooser fc = new FileChooser();
    ReadFile rc = new ReadFile();
    @Test
    public void testLogin() throws SQLException {
        assertTrue("Login successful.",lc.checkUser("user","0000"));
        assertTrue("Login successful.",lc.checkAdmin("admin","0000"));
        assertFalse("Login not successful.",lc.checkUser("gesh","1234"));
        assertFalse("Login not successful.",lc.checkAdmin("baba","5555"));
    }

    @Test
    public void filePaths(){
        String click = "D:\\year2\\seg\\comp2211\\seg\\src\\main\\resources\\2_week_campaign_2\\click_log.csv";
        fph.setClickPath("D:\\year2\\seg\\comp2211\\seg\\src\\main\\resources\\2_week_campaign_2\\click_log.csv");

        String impression = "D:\\year2\\seg\\comp2211\\seg\\src\\main\\resources\\2_week_campaign_2\\impression_log.csv";
        fph.setImpressionPath("D:\\year2\\seg\\comp2211\\seg\\src\\main\\resources\\2_week_campaign_2\\impression_log.csv");

        String server = "D:\\year2\\seg\\comp2211\\seg\\src\\main\\resources\\2_week_campaign_2\\server_log.csv";
        fph.setServerPath("D:\\year2\\seg\\comp2211\\seg\\src\\main\\resources\\2_week_campaign_2\\server_log.csv");

        assertEquals(click, fph.getClickPath());
        assertEquals(impression, fph.getImpressionPath());
        assertEquals(server, fph.getServerPath());

    }

    @Test
    public void readFile(){
        //test read file readCSV -only uniques will count
    }



}
