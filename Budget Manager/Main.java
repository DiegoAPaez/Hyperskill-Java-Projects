package budget;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static File file = new File("purchases.txt");
    static Map<String, List<Item>> purchaseList = new HashMap<>();
    static double income = 0;
    static double balance = 0;
    static double total = 0;
    static boolean running = true;


    public static void main(String[] args) {
        while (running) {
            showMenu();
            performAction();
        }
    }

    public static void showMenu() {
        System.out.println("""
                \nChoose your action:
                1) Add income
                2) Add purchase
                3) Show list of purchases
                4) Balance
                5) Save
                6) Load
                7) Analyze (Sort)
                0) Exit""");
    }

    public static int showPurchaseCategories() {
        System.out.println("""
                \nChoose the type of purchase
                1) Food
                2) Clothes
                3) Entertainment
                4) Other
                5) Back""");
        return Integer.parseInt(scanner.nextLine());
    }

    public static int showListOptions() {
        System.out.println("""
                \nChoose the type of purchases
                1) Food
                2) Clothes
                3) Entertainment
                4) Other
                5) All
                6) Back""");
        return Integer.parseInt(scanner.nextLine());
    }

    public static void performAction() {
        int action = Integer.parseInt(scanner.nextLine());
        switch (action) {
            case 1 -> addIncome();
            case 2 -> selectPurchaseCategories();
            case 3 -> selectCategoriesList();
            case 4 -> showBalance();
            case 5 -> saveToFile();
            case 6 -> loadFile();
            case 7 -> analyzeMenu();
            case 0 -> exitProgram();
            default -> performAction();
        }
    }

    public static void analyzeMenu(){
        while (true) {
            System.out.println("""
                \nHow do you want to sort?
                1) Sort all purchases
                2) Sort by type
                3) Sort certain type
                4) Back""");

            int option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1 -> sortAllPurchases();
                case 2 -> sortByType();
                case 3 -> sortCertainType();
                case 4 -> {
                    return;
                }
            }
        }
    }

    public static void sortAllPurchases() {
        System.out.println();
        if (purchaseList.isEmpty()) {
            System.out.println("The purchase list is empty!");
            return;
        }

        List<Item> allItems = new ArrayList<>();
        for (List<Item> items : purchaseList.values()) {
            allItems.addAll(items);
        }

        if (allItems.isEmpty()) {
            System.out.println("The purchase list is empty!");
            return;
        }

        // Sort items by price (descending)
        allItems.sort((item1, item2) -> Double.compare(item2.getPrice(), item1.getPrice()));

        System.out.println("All:");
        double total = 0;
        for (Item item : allItems) {
            System.out.println(item);
            total += item.getPrice();
        }
        System.out.printf("Total: $%.2f\n", total);
    }

    public static void sortByType() {
        System.out.println();
        Map<String, Double> categoryTotals = new HashMap<>();

        // Initialize all categories with $0
        categoryTotals.put("Food", 0.0);
        categoryTotals.put("Entertainment", 0.0);
        categoryTotals.put("Clothes", 0.0);
        categoryTotals.put("Other", 0.0);

        // Calculate totals for each category
        for (Map.Entry<String, List<Item>> entry : purchaseList.entrySet()) {
            String category = entry.getKey();
            double categoryTotal = 0;

            for (Item item : entry.getValue()) {
                categoryTotal += item.getPrice();
            }

            categoryTotals.put(category, categoryTotal);
        }

        // Print types by total amount
        System.out.println("Types:");
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            System.out.printf("%s - $%.2f\n", entry.getKey(), entry.getValue());
        }

        // Calculate and print total sum
        double totalSum = categoryTotals.values().stream().mapToDouble(Double::doubleValue).sum();
        System.out.printf("Total sum: $%.2f\n", totalSum);
    }

    public static void sortCertainType() {
        System.out.println();
        System.out.println("Choose the type of purchase");
        System.out.println("1) Food");
        System.out.println("2) Clothes");
        System.out.println("3) Entertainment");
        System.out.println("4) Other");

        int typeChoice = Integer.parseInt(scanner.nextLine());
        String category;

        switch (typeChoice) {
            case 1 -> category = "Food";
            case 2 -> category = "Clothes";
            case 3 -> category = "Entertainment";
            case 4 -> category = "Other";
            default -> {
                return;
            }
        }

        System.out.println();
        List<Item> items = purchaseList.getOrDefault(category, Collections.emptyList());

        if (items.isEmpty()) {
            System.out.println("The purchase list is empty!");
            return;
        }

        // Sort items by price (descending)
        items.sort((item1, item2) -> Double.compare(item2.getPrice(), item1.getPrice()));

        // Print sorted items
        System.out.println(category + ":");
        double total = 0;
        for (Item item : items) {
            System.out.println(item);
            total += item.getPrice();
        }
        System.out.printf("Total sum: $%.2f\n", total);
    }

    private static void addIncome() {
        System.out.println("\nEnter income:");
        double amount = Double.parseDouble(scanner.nextLine());
        income += amount;
        balance += amount;
        System.out.println("Income was added!");
    }

    private static void selectPurchaseCategories() {
        int option = showPurchaseCategories();
        if (option != 5) {
            switch (option) {
                case 1 -> addItem("Food");
                case 2 -> addItem("Clothes");
                case 3 -> addItem("Entertainment");
                case 4 -> addItem("Other");
            }
            selectPurchaseCategories();
        }
    }

    public static void addItem(String category) {
        if (!purchaseList.containsKey(category)) {
            purchaseList.put(category, new ArrayList<>());
        }

        List<Item> items = purchaseList.get(category);

        System.out.println("\nEnter purchase name:");
        String name = scanner.nextLine();
        System.out.println("Enter its price:");
        double price = Double.parseDouble(scanner.nextLine());

        balance -= price;
        total += price;

        items.add(new Item(name, price));
        purchaseList.put(category, items);

        System.out.println("Purchase was added!");
    }

    private static void selectCategoriesList() {
        if (!purchaseList.isEmpty()) {
            int option = showListOptions();
            if (option != 6) {
                switch (option) {
                    case 1 -> printList("Food");
                    case 2 -> printList("Clothes");
                    case 3 -> printList("Entertainment");
                    case 4 -> printList("Other");
                    case 5 -> printAllLists();
                }
                selectCategoriesList();
            }
        } else {
            System.out.println("The purchase list is empty!");
        }
    }

    public static void printList(String category) {
        double categoryTotal = 0;

        List<Item> items = purchaseList.getOrDefault(category, Collections.emptyList());
        System.out.println();
        if (items.isEmpty()) {
            System.out.println("The purchase list is empty!");
        } else {
            System.out.println(category + ":");
            for (Item item : items) {
                System.out.println(item.toString());
                categoryTotal += item.getPrice();
            }
            System.out.printf("Total sum: $%.2f\n", categoryTotal);
        }
    }

    public static void printAllLists() {
        if (purchaseList.isEmpty()) {
            System.out.println("\nThe purchase list is empty!");
            return;
        }

        System.out.println("\nAll:");
        double allTotal = 0;

        for (Map.Entry<String, List<Item>> entry : purchaseList.entrySet()) {
            for (Item item : entry.getValue()) {
                System.out.println(item.toString());
                allTotal += item.getPrice();
            }
        }

        System.out.printf("Total sum: $%.2f\n", allTotal);
    }

    private static void showBalance() {
        System.out.printf("\nBalance: $%.2f\n", balance);
    }

    private static void exitProgram() {
        System.out.println("\nBye!");
        running = false;
    }

    public static void saveToFile() {
        try (PrintWriter writer = new PrintWriter(file)) {
            // Save financial data
            writer.println("Income:" + income);
            writer.println("Balance:" + balance);
            writer.println("Total:" + total);

            // Save purchases by category
            for (Map.Entry<String, List<Item>> entry : purchaseList.entrySet()) {
                writer.println(entry.getKey());
                for (Item item : entry.getValue()) {
                    writer.println(item.toString());
                }
            }

            System.out.println("\nPurchases were saved!");
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    public static void loadFile() {
        if (!file.exists()) {
            System.out.println("\nPurchases were loaded!");
            return;
        }

        try (Scanner fileScanner = new Scanner(file)) {
            // Clear existing data
            purchaseList.clear();

            String currentCategory = null;

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();

                if (line.isEmpty()) {
                    continue;
                }

                if (line.startsWith("Income:")) {
                    income = Double.parseDouble(line.substring("Income:".length()));
                    continue;
                }

                if (line.startsWith("Balance:")) {
                    balance = Double.parseDouble(line.substring("Balance:".length()));
                    continue;
                }

                if (line.startsWith("Total:")) {
                    total = Double.parseDouble(line.substring("Total:".length()));
                    continue;
                }

                // Category check
                if (line.equals("Food") || line.equals("Clothes") ||
                        line.equals("Entertainment") || line.equals("Other")) {
                    currentCategory = line;
                    if (!purchaseList.containsKey(currentCategory)) {
                        purchaseList.put(currentCategory, new ArrayList<>());
                    }
                    continue;
                }

                // It must be an item if we have a category
                if (currentCategory != null && line.contains("$")) {
                    int dollarIndex = line.lastIndexOf("$");
                    if (dollarIndex != -1) {
                        String name = line.substring(0, dollarIndex).trim();
                        String priceStr = line.substring(dollarIndex + 1).trim();
                        try {
                            double price = Double.parseDouble(priceStr);
                            purchaseList.get(currentCategory).add(new Item(name, price));
                        } catch (NumberFormatException e) {
                            System.out.println("Error parsing price: " + priceStr);
                        }
                    }
                }
            }

            System.out.println("\nPurchases were loaded!");
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }
}