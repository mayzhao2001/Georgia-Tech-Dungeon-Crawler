package main;

public class GameState {
    private int money;
    private int playerHealth;
    private ConfigScreen.Weapon weapon;
    private ConfigScreen.Difficulty difficulty;
    private Armour armour = null;
    private int roomOrder;
    private int roomIndex;
    private LastRoom lastRoom;
    private boolean attackPotion;

    private GameScreen1 gameScreen1;

    private InteriorRoom[][] interiorRooms;

    private boolean didChallengeTechTowers = false;
    private boolean didChallengeHonors = false;

    public GameState(ConfigScreen.Weapon weapon, ConfigScreen.Difficulty difficulty) {

        // int[] monsters = new int[]{100,100,100,100,100};
        interiorRooms = new InteriorRoom[4][5];

        this.weapon = weapon;
        this.difficulty = difficulty;
        this.playerHealth = 100;
        setStartingMoney(difficulty);
    }



    public void setStartingMoney(ConfigScreen.Difficulty difficulty) {
        switch (difficulty) {
        case IN_STATE:
            money = 500;
            break;
        case OUT_OF_STATE:
            money = 300;
            break;
        case INTERNATIONAL:
            money = 100;
            break;
        default:
            break;
        }
    }

    public void setMoney(int money) {
        this.money = money;
    }
    public int getMoney() {
        return money;
    }

    public void setWeapon(ConfigScreen.Weapon weapon) {
        this.weapon = weapon;
    }
    public ConfigScreen.Weapon getWeapon() {
        return this.weapon;
    }

    public void setDifficulty(ConfigScreen.Difficulty difficulty) {
        this.difficulty = difficulty;
    }
    public ConfigScreen.Difficulty getDifficulty() {
        return difficulty;
    }

    public void setArmour(Armour armour) {
        this.armour = armour;
    }
    public Armour getArmour() {
        return armour;
    }

    public void setInteriorRoom(int roomOrder, int roomIndex, InteriorRoom interiorRoom) {
        interiorRooms[roomOrder][roomIndex] = interiorRoom;
    }
    public InteriorRoom getInteriorRoom(int roomOrder, int roomIndex) {
        return interiorRooms[roomOrder][roomIndex];
    }

    public void setGameScreen1(GameScreen1 gameScreen1) {
        this.gameScreen1 = gameScreen1;
    }
    public GameScreen1 getGameScreen1() {
        return gameScreen1;
    }

    public int getPlayerHealth() {
        return this.playerHealth;
    }

    public void setPlayerHealth(int newHealth) {
        this.playerHealth = newHealth;
    }

    public void damagePlayer(int damage) {
        this.playerHealth -= damage;
    }

    public boolean isPlayerAlive() {
        return this.playerHealth > 0;
    }

    public void setRoomOrder(int order) {
        this.roomOrder = order;
    }

    public void setRoomIndex(int roomIndex) {
        this.roomIndex = roomIndex;
    }

    public int getRoomOrder() {
        return this.roomOrder;
    }

    public int getRoomIndex() {
        return this.roomIndex;
    }

    public void setLastRoom(LastRoom lastRoom) {
        this.lastRoom = lastRoom;
    }

    public LastRoom getLastRoom() {
        return this.lastRoom;
    }

    public void setAttackPotion() {
        this.attackPotion = true;
    }

    public boolean getAttackPotion() {
        return this.attackPotion;
    }

    public void setTechTowersChallengeComplete(boolean didComplete) {
        this.didChallengeTechTowers = didComplete;
    }

    public void setHonorsComplete(boolean didComplete) {
        this.didChallengeHonors = didComplete;
    }

    public boolean getDidChallengeTechTowers() {
        return didChallengeTechTowers;
    }

    public boolean getDidChallengeHonors() {
        return didChallengeHonors;
    }
}
