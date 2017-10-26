package mutilThreadDemo;

/**
 * Producer Consumers
 * Created by damon on 2017/10/26.
 */
public class App2 {
}
class warehouse {
    String[] Shelf = new String[2];

    public String[] getShelf() {
        return Shelf;
    }

    public void setShelf(String[] shelf) {
        Shelf = shelf;
    }
    // purchase
    //        shipment

    public boolean in() {
        return true;
    }

    public boolean out() {
        return true;
    }
}
