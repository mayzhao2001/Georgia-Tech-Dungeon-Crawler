package tests;

//import com.sun.tools.javac.Main;
import javafx.application.Platform;
import main.ConfigScreen;
import main.GameScreen1;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

public class GameScreen1UnitTests {
    private static final int TIMEOUT = 500;
    private GameScreen1 screen1;
    private GameScreen1 screen2;
    private GameScreen1 screen3;

    @Before
    public void setUp() {
        try {
            //initialize JavaFX env without launching a window
            Platform.startup(() -> {

            });
        } catch (IllegalStateException e) {
            //the javaFX environment was already set up
        }

        //setup dummy screens on the GameScreen1 page for testing
        screen1 = new GameScreen1(
                ConfigScreen.Difficulty.IN_STATE, ConfigScreen.Weapon.CALCULATOR, true
        );
        screen2 = new GameScreen1(
                ConfigScreen.Difficulty.OUT_OF_STATE, ConfigScreen.Weapon.PENCIL, true
        );
        screen3 = new GameScreen1(
                ConfigScreen.Difficulty.INTERNATIONAL, ConfigScreen.Weapon.TEXTBOOK, true
        );
    }

    @Test(timeout = TIMEOUT)
    public void testFxmlLoading() {
        //if the constructor cannot find InitialGameScreen.fxml, the root will be null and the scene
        //cannot be properly constructed
        assert (screen1.getRoot() != null);
        assert (screen1.getScene().getRoot() != null);
    }

    //test that all 4 doors lead to correct room

    //test that shop leads to correct screen

    //test that label shows correct amount of starting money
    @Test(timeout = TIMEOUT)
    public void correctStartingMoney() {
        assert (screen1.getStartingMoney() == 500);
        assert (screen2.getStartingMoney() == 300);
        assert (screen3.getStartingMoney() == 100);
    }
    //test if weapon chosen in configScreen is the same as the weapon in game
    @Test(timeout = TIMEOUT)
    public void correctWeapon() {
        assert (Objects.equals(screen1.getWeapon(), ConfigScreen.Weapon.CALCULATOR));
        assert Objects.equals(screen2.getWeapon(), ConfigScreen.Weapon.PENCIL);
        assert (Objects.equals(screen3.getWeapon(), ConfigScreen.Weapon.TEXTBOOK));
    }

    @Test(timeout = TIMEOUT)
    public void testBackground() {
        //if the constructor cannot find InitialGameScreen.fxml, the root will be null and the scene
        //cannot be properly constructed
        assert (screen1.getRoot().getStyle() != null);
        assert (screen1.getScene().getRoot().getStyle() != null);

    }
}
