package tests;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import main.ConfigScreen;
import main.GameScreen1;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.testfx.api.FxAssert.verifyThat;

public class TriTest extends ApplicationTest {

    private static final int TIMEOUT = 500;
    private GameScreen1 startScreen;

    @Override
    public void start(Stage primaryStage) throws Exception {
        startScreen = new GameScreen1(
                ConfigScreen.Difficulty.IN_STATE, ConfigScreen.Weapon.CALCULATOR, true);
        primaryStage.setScene(startScreen.getScene());
        primaryStage.show();
    }

    @Test
    public void checkGameScreen4Doors() {
        Button door1 = (Button) startScreen.getScene().lookup("#door1");
        Button door2 = (Button) startScreen.getScene().lookup("#door2");
        Button door3 = (Button) startScreen.getScene().lookup("#door3");
        Button door4 = (Button) startScreen.getScene().lookup("#door4");
        assertEquals(true, door1.isVisible());
        assertEquals(true, door2.isVisible());
        assertEquals(true, door3.isVisible());
        assertEquals(true, door4.isVisible());
    }

    @Test
    public void checkRandomizedPathOrder() {
        List<Integer> mazeOrder1 = startScreen.getMazeOrder1();
        for (int i : mazeOrder1) {
            System.out.print(i + " ");
        }
        clickOn("#door1");
        int path1 = mazeOrder1.get(0);
        String expectedPath0 = String.valueOf(path1);
        verifyThat("#roomNum", (Label a) -> a.getText().contains(expectedPath0));

        clickOn("#nextDoorRight");
        int path2 = mazeOrder1.get(1);
        String expectedPath1 = String.valueOf(path2);
        verifyThat("#roomNum", (Label a) -> a.getText().contains(expectedPath1));

        clickOn("#nextDoorRight");
        int path3 = mazeOrder1.get(2);
        String expectedPath2 = String.valueOf(path3);
        verifyThat("#roomNum", (Label a) -> a.getText().contains(expectedPath2));

        clickOn("#nextDoorRight");
        int path4 = mazeOrder1.get(3);
        String expectedPath3 = String.valueOf(path4);
        verifyThat("#roomNum", (Label a) -> a.getText().contains(expectedPath3));

        clickOn("#nextDoorRight");
        int path5 = mazeOrder1.get(4);
        String expectedPath4 = String.valueOf(path5);
        verifyThat("#roomNum", (Label a) -> a.getText().contains(expectedPath4));

        clickOn("#nextDoorRight");
        int path6 = mazeOrder1.get(5);
        String expectedPath5 = String.valueOf(path6);
        verifyThat("#roomNum", (Label a) -> a.getText().contains(expectedPath5));
    }

    @Test
    public void testBackWardRoom() {
        List<Integer> mazeOrder1 = startScreen.getMazeOrder1();
        for (int i : mazeOrder1) {
            System.out.print(i + " ");
        }
        clickOn("#door1");
        int path1 = mazeOrder1.get(0);
        String expectedPath0 = String.valueOf(path1);
        verifyThat("#roomNum", (Label a) -> a.getText().contains(expectedPath0));

        clickOn("#nextDoorRight");
        int path2 = mazeOrder1.get(1);
        String expectedPath1 = String.valueOf(path2);
        verifyThat("#roomNum", (Label a) -> a.getText().contains(expectedPath1));

        clickOn("#nextDoorRight");
        int path3 = mazeOrder1.get(2);
        String expectedPath2 = String.valueOf(path3);
        verifyThat("#roomNum", (Label a) -> a.getText().contains(expectedPath2));

        clickOn("#prevDoorRight");
        verifyThat("#roomNum", (Label a) -> a.getText().contains(expectedPath1));

        clickOn("#prevDoorRight");
        verifyThat("#roomNum", (Label a) -> a.getText().contains(expectedPath0));
    }
}