package main;

public class Armour implements Item {
    //use if making different types of armour -> private int protection;
    //use if making different types of armour -> private int healthDamage;
    private int cost;
    private int usesLeft;
    private boolean alive;
    private boolean didUseInInv;

    public Armour() {
        cost = 80;
        usesLeft = 5;
        alive = true;
        didUseInInv = false;
    }

    @Override
    public void useItem() {
        //usesLeft--;
        //      if (usesLeft == 0) {
        //          alive = false;
        //      }
        GameState currentState = ConfigScreen.getGameState();
        if (currentState.getArmour() == null) {
            currentState.setArmour(this);
            ConfigScreen.setGameState(currentState);
            didUseInInv = true;
        }
    }

    @Override
    public String getImage() {
        return "../resources/Armour.png";
    }

    public void buy() {
        alive = true;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }


    public int getCost() {
        return cost;
    }

    @Override
    public boolean isSingleUse() {
        return didUseInInv;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
    public boolean getAlive() {
        return alive;
    }
}
