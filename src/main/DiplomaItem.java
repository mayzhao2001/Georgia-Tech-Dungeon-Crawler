package main;

public class DiplomaItem implements Item {
    public void useItem() {
        //Diploma has no functionality (for now?)
    }

    @Override
    public String getImage() {
        return "../resources/Diploma.png";
    }

    @Override
    public boolean isSingleUse() {
        return false;
    }
}
