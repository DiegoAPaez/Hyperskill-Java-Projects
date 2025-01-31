package machine;

public class Menu {
    Drink[] menu;

    public Menu() {
        Drink espresso = new Drink(Coffee.ESPRESSO, 250,0,16,4);
        Drink latte = new Drink(Coffee.LATTE, 350,75,20,7);
        Drink cappuccino = new Drink(Coffee.CAPPUCCINO, 200,100,12,6);
        this.menu = new Drink[]{espresso, latte, cappuccino};
    }

    public Drink[] getMenu() {
        return menu;
    }
}
