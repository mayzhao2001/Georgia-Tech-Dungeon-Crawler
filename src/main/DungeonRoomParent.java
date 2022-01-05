package main;

import javafx.animation.Timeline;
import javafx.scene.Scene;

abstract class DungeonRoomParent {
    protected Timeline monsterAttackThread;
    DungeonRoomParent() {

    }

    /**
     * Called when the window is entered after being exited
     */
    abstract void update();

    /**
     * Returns the scene associated with the given dungeon room
     * @return Scene scene associated with given dungeon room.
     */
    abstract Scene getScene();


}
