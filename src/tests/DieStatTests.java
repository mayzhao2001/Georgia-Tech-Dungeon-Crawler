package tests;

import javafx.scene.control.Label;
import javafx.stage.Stage;
import main.*;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

public class DieStatTests extends ApplicationTest {
    private WinScreen winScreen;
    private GameScreen1 gameScreen;
    private DieScreen dieScreen;

    @Override
    public void start(Stage primaryStage) throws Exception {

        ConfigScreen.setGameState(new GameState(ConfigScreen.Weapon.PENCIL,
                ConfigScreen.Difficulty.IN_STATE));

        gameScreen = new GameScreen1(ConfigScreen.Difficulty.IN_STATE,
                ConfigScreen.Weapon.CALCULATOR, true);
        dieScreen = new DieScreen();
        primaryStage.setScene(dieScreen.getScene());
        primaryStage.show();

    }

    //test if stats label is correct for die screen
    @Test
    public void testDieStats() {

        Label stat1Label = (Label) dieScreen.getScene().lookup("#stat1");
        int currMoney = Integer.parseInt(stat1Label.getText().substring(8));
        assert (currMoney == dieScreen.getMoney());

        Label stat2Label = (Label) dieScreen.getScene().lookup("#stat2");
        int currHealth = Integer.parseInt(stat2Label.getText().substring(18)); //i think 18
        //assert(currMoney == winScreen.getHealth());

        Label stat3Label = (Label) dieScreen.getScene().lookup("#stat3");
        //assert(currMoney == winScreen.difficulty);

        Label stat4Label = (Label) dieScreen.getScene().lookup("#stat4");
        //assert(currMoney == winScreen.getMoney());

    }

}
