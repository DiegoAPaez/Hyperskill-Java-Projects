package machine;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CoffeeMachine {
    static Scanner scanner = new Scanner(System.in);
    static Menu menu = new Menu();
    static int availableWater = 400;
    static int availableMilk = 540;
    static int availableCoffee = 120;
    static int availableCups = 9;
    static int availableMoney = 550;
    static int cupsMade = 0;


    public static void main(String[] args) {
        while (true) {
            Commands op = selectCommand();
            if (op == Commands.EXIT) break;
            if (op == Commands.CLEAN) {
                cleanMachine();
            } else if (cupsMade < 10) {
                switch (op) {
                    case BUY -> buyDrink();
                    case FILL -> fillResources();
                    case TAKE -> takeMoney();
                    case REMAINING -> showResources();
                }
            } else {
                System.out.println("I need cleaning!");
            }
        }
    }

    public static Commands selectCommand() {
        boolean validOp = false;
        Commands operation = null;
        while (!validOp) {
            System.out.println("Write action (buy, fill, take, clean, remaining, exit):");
            String op = scanner.next();
            validOp = validOperation(op);
            if (validOp) {
                operation = Commands.valueOf(op.toUpperCase());
            }
        }
        return operation;
    }

    public static boolean validOperation(String op) {
        for (Commands command : Commands.values()) {
            if (command.name().equalsIgnoreCase(op)) {
                return true;
            }
        }
        return false;
    }

    // OPERATIONS
    public static void fillResources() {
        System.out.println("Write how many ml of water you want to add:");
        availableWater += scanner.nextInt();
        System.out.println("Write how many ml of milk you want to add:");
        availableMilk += scanner.nextInt();
        System.out.println("Write how many grams of coffee beans you want to add:");
        availableCoffee += scanner.nextInt();
        System.out.println("Write how many disposable cups you want to add: ");
        availableCups += scanner.nextInt();
    }

    public static void takeMoney() {
        System.out.printf("I gave you $%d\n", availableMoney);
        availableMoney = 0;
    }

    public static void buyDrink() {
        while (true) {
            System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: ");
            String command = scanner.next();
            if ("back".equals(command)) {
                break;
            }
            try {
                int option = Integer.parseInt(command);
                Drink drink = menu.getMenu()[option - 1];
                prepareDrink(drink);
                break;
            } catch (Exception ignored) {}
        }
    }

    public static void prepareDrink(Drink drink){
        if (checkSufficientResources(drink)) {
            System.out.println("I have enough resources, making you a coffee!");
            cupsMade++;
            availableWater -= drink.water;
            availableMilk -= drink.milk;
            availableCoffee -= drink.coffee;
            availableCups--;
            availableMoney += drink.cost;
        }
    }

    public static void showResources() {
        System.out.println("The coffee machine has:");
        System.out.printf("%d ml of water\n", availableWater);
        System.out.printf("%d ml of milk\n", availableMilk);
        System.out.printf("%d g of coffee beans\n", availableCoffee);
        System.out.printf("%d disposable cups\n", availableCups);
        System.out.printf("$%d of money\n", availableMoney);
    }

    public static void cleanMachine() {
        System.out.println("I have been cleaned!");
        cupsMade = 0;
    }

    // HELPERS
    public static boolean checkSufficientResources(Drink drink) {
        boolean canMake = true;
        Map<String, Integer> drinkRequirements = drink.getRequirements();
        Map<String, Integer> availableResources = mapResources();
        for (Map.Entry<String, Integer> e : availableResources.entrySet()) {
            if (drinkRequirements.get(e.getKey()) > e.getValue()) {
                System.out.printf("Sorry, not enough %s!\n", e.getKey());
                canMake = false;
            }
        }
        return canMake;
    }

    public static Map<String, Integer> mapResources() {
        Map<String, Integer> mapped = new HashMap<>();
        mapped.put("water", availableWater);
        mapped.put("milk", availableMilk);
        mapped.put("coffee", availableCoffee);
        return mapped;
    }
}