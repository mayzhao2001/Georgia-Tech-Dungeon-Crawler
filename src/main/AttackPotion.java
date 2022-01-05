package main;

public class AttackPotion implements Item {
    private int damage;

    public void useAttackPotion(ConfigScreen.Weapon weapon) {
        switch (weapon) {
        case PENCIL:
            damage = 10;
            break;
        case TEXTBOOK:
            damage = 15;
            break;
        case CALCULATOR:
            damage = 20;
            break;
        default:
            break;
        }
    }

    public void useItem() {
        // to do: make attack potion attack
        GameState currGameState = ConfigScreen.getGameState();
        currGameState.setAttackPotion();
    }

    @Override
    public String getImage() {
        return "../resources/Attack_Potion.png";
    }
}
