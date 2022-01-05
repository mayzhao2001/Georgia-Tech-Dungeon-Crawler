package main;

public interface Item {

    String getImage();
    void useItem();

    default boolean isSingleUse() {
        return true;
    }

}
