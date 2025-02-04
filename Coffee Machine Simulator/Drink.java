package machine;

import java.util.HashMap;
import java.util.Map;

public class Drink {
    Coffee name;
    int water;
    int milk;
    int coffee;
    int cost;

    public Drink(Coffee name, int water, int milk, int coffee, int cost) {
        this.coffee = coffee;
        this.milk = milk;
        this.name = name;
        this.water = water;
        this.cost = cost;
    }

    public Map<String, Integer> getRequirements() {
        Map<String, Integer> mapped = new HashMap<>();
        mapped.put("water", water);
        mapped.put("milk", milk);
        mapped.put("coffee", coffee);
        return mapped;
    }
}
