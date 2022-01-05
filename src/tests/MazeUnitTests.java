package tests;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.Window;
import main.*;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MazeUnitTests extends ApplicationTest {
    private GameScreen1 startScreen;
    private Window window;
    private InteriorRoom room;
    private LastRoom lastRoom;

    @Override
    public void start(Stage primaryStage) throws Exception {
        startScreen = new GameScreen1(ConfigScreen.Difficulty.IN_STATE,
                ConfigScreen.Weapon.CALCULATOR, true);
        primaryStage.setScene(startScreen.getScene());
        primaryStage.show();

        //get the displayed window from javafx
        //window = ((List<Window>)Stage.getWindows().stream().filter(Window::isShowing)).get(0);
    }

    @Test
    public void testCorrectImgLoading() {
        Button door1Button = (Button) startScreen.getScene().lookup("#door1");
        clickOn(door1Button);
        //assert(window.getScene() )
    }

    /**
     * Tests that the maze rooms have the correct backgrounds
     * Meiling's test
     */
    @Test
    public void testMazeBackground() {

        Button door1Button = (Button) startScreen.getScene().lookup("#door1");
        clickOn(door1Button);
        InteriorRoom room = new InteriorRoom(1, ConfigScreen.Difficulty.IN_STATE,
                ConfigScreen.Weapon.CALCULATOR, 500, 1);
        assert (room.getBackgroundImage().equals(
                "../resources/" + GameScreen1.getBackgroundImgs()[room.getRoomNum()])
        );

        InteriorRoom room2 = new InteriorRoom(2, ConfigScreen.Difficulty.IN_STATE,
                ConfigScreen.Weapon.CALCULATOR, 500, 2);
        assert (room2.getBackgroundImage().equals(
                "../resources/" + GameScreen1.getBackgroundImgs()[room2.getRoomNum()])
        );

        InteriorRoom room3 = new InteriorRoom(3, ConfigScreen.Difficulty.IN_STATE,
                ConfigScreen.Weapon.CALCULATOR, 500, 3);
        assert (room3.getBackgroundImage().equals(
                "../resources/" + GameScreen1.getBackgroundImgs()[room3.getRoomNum()])
        );
    }

    /**
     * Tests to see if restart button goes back to welcScreen
     * Meiling's test
     */

    @Test
    public void winToWelc() {
        Button button = (Button) startScreen.getScene().lookup("#door1");
        clickOn(button);
        ArrayList<String> roomNums = new ArrayList<>();

        for (int i = 0; i < 6; ++i) {
            Window newRoom = Stage.getWindows().stream().filter(Window::isShowing)
                    .findFirst().orElse(null);

            Button nextDoorLeft = (Button) newRoom.getScene().lookup("#nextDoorLeft");

            //go to the next room
            clickOn(nextDoorLeft);
        }

        //make sure we're in the last room
        Window last = Stage.getWindows().stream().filter(Window::isShowing)
                .findFirst().orElse(null);

        Button lastDoor = (Button) last.getScene().lookup("#Door");
        clickOn(lastDoor);

        Window win = Stage.getWindows().stream().filter(Window::isShowing)
                .findFirst().orElse(null);

        assert (win.getScene().lookup("#buttonStartOver") != null);

        Button restart = (Button) last.getScene().lookup("#buttonStartOver");
        clickOn(restart);

        Window welc = Stage.getWindows().stream().filter(Window::isShowing)
                .findFirst().orElse(null);

        assert (welc.getScene().lookup("#welcScreen") != null);

    }


    /** Tests that the path backward equals the reverse of the path going forward **/
    /** also tests that paths remain fixed even after enter GameScreen1 again **/
    /** Alison's Test **/
    @Test
    public void pathBackward() {
        String[] forwardScenes = new String[8];
        forwardScenes[0] = ((Label) startScreen.getScene().lookup("#startingMoney")).getText();

        //going forward
        Button button = (Button) startScreen.getScene().lookup("#door1");
        clickOn(button);
        Window nextRoom =
                Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        forwardScenes[1] = ((Label) nextRoom.getScene().lookup("#roomNum")).getText();
        for (int i = 2; i <= 6; i++) {
            button = (Button) nextRoom.getScene().lookup("#nextDoorLeft");
            clickOn(button);
            nextRoom =
                    Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
            forwardScenes[i] = ((Label) nextRoom.getScene().lookup("#roomNum")).getText();
        }

        //going backward
        for (int i = 5; i >= 1; i--) {
            button = (Button) nextRoom.getScene().lookup("#prevDoorLeft");
            clickOn(button);
            nextRoom =
                    Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
            assertEquals(
                    forwardScenes[i], ((Label) nextRoom.getScene().lookup("#roomNum")).getText()
            );
        }
        button = (Button) nextRoom.getScene().lookup("#prevDoorLeft");
        clickOn(button);
        nextRoom =
                Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        assertEquals(
                forwardScenes[0], ((Label) nextRoom.getScene().lookup("#startingMoney")).getText()
        );

        //going forward again
        button = (Button) nextRoom.getScene().lookup("#door1");
        clickOn(button);
        nextRoom =
                Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        forwardScenes[1] = ((Label) nextRoom.getScene().lookup("#roomNum")).getText();
        for (int i = 2; i <= 6; i++) {
            button = (Button) nextRoom.getScene().lookup("#nextDoorRight");
            clickOn(button);
            nextRoom =
                    Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
            assertEquals(
                    forwardScenes[i], ((Label) nextRoom.getScene().lookup("#roomNum")).getText()
            );
        }
    }



    /** Tests room paths are randomized when you enter gameScreen1 the first time **/
    /** Alison's Test **/
    @Test
    public void randomized() {
        boolean randomized = false;
        boolean pathsDiffer = false;

        List<Integer>[][] allMazeOrders = (List<Integer>[][]) (new List[5][4]);


        //reload game 5 times and test that paths are not the same all five times
        //test that within the same game, paths remain constant
        for (int i = 0; i < 5; i++) {
            startScreen = new GameScreen1(ConfigScreen.Difficulty.IN_STATE,
                    ConfigScreen.Weapon.CALCULATOR, true);
            List<Integer> mazeOrder1 = startScreen.getMazeOrder1();
            allMazeOrders[i][0] = mazeOrder1;
            List<Integer> mazeOrder2 = startScreen.getMazeOrder2();
            allMazeOrders[i][1] = mazeOrder2;
            List<Integer> mazeOrder3 = startScreen.getMazeOrder3();
            allMazeOrders[i][2] = mazeOrder3;
            List<Integer> mazeOrder4 = startScreen.getMazeOrder4();
            allMazeOrders[i][3] = mazeOrder4;

            //check that paths in 4 doors are not equal
            for (int x = 0; x < 3; x++) {
                for (int y = 0; y < 6; y++) {
                    if (allMazeOrders[i][x].get(y) != allMazeOrders[i][x + 1].get(y)) {
                        pathsDiffer = true;
                    }
                }
            }
        }

        //check that paths for each door are not same for all 5 times load gameScreen1
        for (int i = 0; i < 4; i++) {
            for (int x = 0; x < 4; x++) {
                for (int y = 0; y < 6; y++) {
                    if (allMazeOrders[i][x].get(y) != allMazeOrders[i + 1][x].get(y)) {
                        randomized = true;
                    }
                }
            }
        }

        assertTrue(pathsDiffer);
        assertTrue(randomized);
    }

    /**
     * Tests that the win screen is 6 doors away
     *
     * Nathan Malta wrote this test
     */
    @Test
    public void test6Clicks() throws InterruptedException {
        Button door1Button = (Button) startScreen.getScene().lookup("#door3");
        clickOn(door1Button);

        for (int i = 0; i < 6; ++i) {
            Window newRoom = Stage.getWindows().stream().filter(Window::isShowing)
                    .findFirst().orElse(null);
            Button nextDoorLeft = (Button) newRoom.getScene().lookup("#nextDoorLeft");

            //make sure we see the objects that should be in a maze room
            assert (newRoom.getScene().lookup("#nextDoorLeft") != null);
            assert (newRoom.getScene().lookup("#prevDoorLeft") != null);

            //go to the next room
            clickOn(nextDoorLeft);
        }

        //make sure we're in the last room
        Window lastRoom = Stage.getWindows().stream().filter(Window::isShowing)
                .findFirst().orElse(null);
        assert (lastRoom.getScene().lookup("#lastRoom") != null);
    }

    /**
     * Tests that the same maze room doesn't show up twice
     *
     * Nathan Malta wrote this test
     */
    @Test
    public void test6RoomsUnique() throws InterruptedException {
        Button door1Button = (Button) startScreen.getScene().lookup("#door3");
        clickOn(door1Button);
        ArrayList<String> roomNums = new ArrayList<>();

        for (int i = 0; i < 6; ++i) {
            Window newRoom = Stage.getWindows().stream().filter(Window::isShowing)
                    .findFirst().orElse(null);

            //make sure we see the objects that should be in a maze room
            assert (newRoom.getScene().lookup("#nextDoorLeft") != null);
            assert (newRoom.getScene().lookup("#prevDoorLeft") != null);
            assert (newRoom.getScene().lookup("#roomNum") != null);

            Button nextDoorLeft = (Button) newRoom.getScene().lookup("#nextDoorLeft");
            Label roomNumLabel = (Label) newRoom.getScene().lookup("#roomNum");

            assert (!roomNums.contains(roomNumLabel.getText()));
            roomNums.add(roomNumLabel.getText());

            //go to the next room
            clickOn(nextDoorLeft);
        }

        //make sure we're in the last room
        Window lastRoom = Stage.getWindows().stream().filter(Window::isShowing)
                .findFirst().orElse(null);
        assert (lastRoom.getScene().lookup("#lastRoom") != null);
    }

}