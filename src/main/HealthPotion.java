package main;

public class HealthPotion implements Item {
    private GameState state = ConfigScreen.getGameState();
    private int playerHealth = state.getPlayerHealth();

    //    public HealthPotion() {
    //        setCost(50);
    //    }

    public void useItem() {
        GameState currGameState = ConfigScreen.getGameState();
        int currHealth = currGameState.getPlayerHealth();
        currGameState.setPlayerHealth(currHealth + 10);
    }

    public String getImage() {
        return "../resources/Health_Potion.png";
    }
}
