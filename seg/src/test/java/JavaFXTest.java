import com.application.login.LoginApplication;
import com.application.login.LoginController;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

import static org.testng.AssertJUnit.*;


public class JavaFXTest extends ApplicationTest {

    LoginApplication la = new LoginApplication();
    LoginController lc = new LoginController();

    @Override
    public void start(Stage stage) throws IOException {
        //la.start(stage);

    }

    @Test
    public void testUserLogin(){
        assertTrue("Login successful.",lc.checkUser("user","0000"));
        assertTrue("Login successful.",lc.checkAdmin("admin","0000"));
        assertFalse("Login not successful.",lc.checkUser("gesh","1234"));
        assertFalse("Login not successful.",lc.checkAdmin("baba","5555"));
    }

    @Test
    public void generateTest()
    {
        System.out.println("test!");
//        assertTrue(1+1,2);
    }

}
