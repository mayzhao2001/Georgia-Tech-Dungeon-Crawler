package main;

public class ChallengeItem implements Item {

    private int itemType;

    public ChallengeItem(int type) {
        this.itemType = type;
    }

    // Effect of the T's
    public void useItem() {
        GameState currGameState = ConfigScreen.getGameState();
        int currRoomOrder = currGameState.getRoomOrder();
        int currRoomIndex = currGameState.getRoomIndex();
        increaseHealth(currGameState);
        increaseAttack(currGameState);
        increaseMoney(currGameState, currRoomOrder, currRoomIndex);
    }

    private void increaseMoney(GameState currGameState, int currRoomOrder, int currRoomIndex) {
        // Add $1000 to account
        int currMoney = currGameState.getMoney();
        currGameState.setMoney(currMoney + 1000);
        if (currRoomIndex < 5) {
            InteriorRoom currRoom =
                    currGameState.getInteriorRoom(currRoomOrder, currRoomIndex);
            currRoom.updateLabels();
        } else {
            LastRoom lastRoom = currGameState.getLastRoom();
            lastRoom.update();
        }
    }

    private void increaseAttack(GameState currGameState) {
        // Double attack damage
        currGameState.setAttackPotion();
    }

    private void increaseHealth(GameState currGameState) {
        // Increase health by 100
        int currHealth = currGameState.getPlayerHealth();
        currGameState.setPlayerHealth(currHealth + 100);
    }

    public String getImage() {
        if (itemType == 1) {
            return "../resources/T.png";
        } else {
            return "../resources/Honor_Certificate.png";
        }
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int num) {
        itemType = num;
    }

}
