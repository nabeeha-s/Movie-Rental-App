package MovieRent;

import javafx.scene.control.CheckBox;

public class Movie {

    private final String title;
    private final double price;
    public CheckBox select;

    public Movie(String movieTitle, double moviePrice) {
        this.title = movieTitle;
        this.price = moviePrice;
        select = new CheckBox();
    }

    public String getTitle() {
        return this.title;
    }

    public double getPrice() {
        return this.price;
    }

    public CheckBox getSelect() {
        return select;
    }

    public void setSelect(CheckBox select) {
        this.select = select;
    }

}
