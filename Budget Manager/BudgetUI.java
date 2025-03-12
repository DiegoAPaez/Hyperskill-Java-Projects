package budget;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class BudgetUI {
    private final Scanner scanner;
    private final BudgetManager budgetManager; // Now references the singleton
    private final FileManager fileManager;
    private boolean running;

    public BudgetUI() {
        this.scanner = new Scanner(System.in);
        // Get the singleton instance instead of creating a new instance
        this.budgetManager = BudgetManager.getInstance();
        this.fileManager = new FileManager("purchases.txt");
        this.running = true;
    }

    public void start() {
        while (running) {
            showMenu();
            performAction();
        }
    }

    public void showMenu() {
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

    public int showPurchaseCategories() {
        System.out.println("""
                \nChoose the type of purchase
                1) Food
                2) Clothes
                3) Entertainment
                4) Other
                5) Back""");
        return Integer.parseInt(scanner.nextLine());
    }

    public int showListOptions() {
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

    public void performAction() {
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

    public void analyzeMenu() {
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

    public void sortAllPurchases() {
        System.out.println();
        PurchaseManager purchaseManager = budgetManager.getPurchaseManager();

        if (purchaseManager.isEmpty()) {
            System.out.println("The purchase list is empty!");
            return;
        }

        List<Item> allItems = purchaseManager.getAllItems();

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

    public void sortByType() {
        System.out.println();
        Map<String, Double> categoryTotals = budgetManager.getPurchaseManager().getCategoryTotals();

        // Print types by total amount
        System.out.println("Types:");
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            System.out.printf("%s - $%.2f\n", entry.getKey(), entry.getValue());
        }

        // Calculate and print total sum
        double totalSum = categoryTotals.values().stream().mapToDouble(Double::doubleValue).sum();
        System.out.printf("Total sum: $%.2f\n", totalSum);
    }

    public void sortCertainType() {
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
        List<Item> items = budgetManager.getPurchaseManager().getItemsByCategory(category);

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

    private void addIncome() {
        System.out.println("\nEnter income:");
        double amount = Double.parseDouble(scanner.nextLine());
        budgetManager.addIncome(amount);
        System.out.println("Income was added!");
    }

    private void selectPurchaseCategories() {
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

    public void addItem(String category) {
        System.out.println("\nEnter purchase name:");
        String name = scanner.nextLine();
        System.out.println("Enter its price:");
        double price = Double.parseDouble(scanner.nextLine());

        budgetManager.addPurchase(category, name, price);

        System.out.println("Purchase was added!");
    }

    private void selectCategoriesList() {
        if (!budgetManager.getPurchaseManager().isEmpty()) {
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

    public void printList(String category) {
        double categoryTotal = 0;

        List<Item> items = budgetManager.getPurchaseManager().getItemsByCategory(category);
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

    public void printAllLists() {
        PurchaseManager purchaseManager = budgetManager.getPurchaseManager();

        if (purchaseManager.isEmpty()) {
            System.out.println("\nThe purchase list is empty!");
            return;
        }

        System.out.println("\nAll:");
        double allTotal = 0;

        for (Map.Entry<String, List<Item>> entry : purchaseManager.getAllPurchases().entrySet()) {
            for (Item item : entry.getValue()) {
                System.out.println(item.toString());
                allTotal += item.getPrice();
            }
        }

        System.out.printf("Total sum: $%.2f\n", allTotal);
    }

    private void showBalance() {
        System.out.printf("\nBalance: $%.2f\n", budgetManager.getBalance());
    }

    private void exitProgram() {
        System.out.println("\nBye!");
        running = false;
    }

    public void saveToFile() {
        try {
            fileManager.saveBudgetData(budgetManager);
            System.out.println("\nPurchases were saved!");
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    public void loadFile() {
        if (!fileManager.exists()) {
            System.out.println("\nPurchases were loaded!");
            return;
        }

        try {
            fileManager.loadBudgetData(budgetManager);
            System.out.println("\nPurchases were loaded!");
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }
}