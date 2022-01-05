package tests;

import javafx.application.Platform;
import javafx.stage.Stage;
import main.WelcScreen;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;


public class WelcScreenUnitTests extends ApplicationTest {
    private static final int TIMEOUT = 500;
    private WelcScreen welcScreen;

    @Override
    public void start(Stage primaryStage) throws Exception {
        welcScreen = new WelcScreen();
        primaryStage.setScene(welcScreen.getScene());
        primaryStage.show();
    }

    @Before
    public void setUp() {
        try {
            //initialize JavaFX env without launching a window
            Platform.startup(() -> {
            });
        } catch (IllegalStateException e) {
            //the javaFX environment was already set up
        }

        welcScreen = new WelcScreen();
    }

    /**
     * Make sure that welcome screen fxml resource is loaded
     */
    @Test(timeout = TIMEOUT)
    public void testFxmlLoading() {
        assert (welcScreen.getRoot() != null);
        assert (welcScreen.getScene().getRoot() != null);
    }

}
