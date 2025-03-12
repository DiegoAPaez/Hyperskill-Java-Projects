package budget;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class FileManager {
    private final File file;

    public FileManager(String filename) {
        this.file = new File(filename);
    }

    public void saveBudgetData(BudgetManager budgetManager) throws IOException {
        try (PrintWriter writer = new PrintWriter(file)) {
            // Save financial data
            writer.println("Income:" + budgetManager.getIncome());
            writer.println("Balance:" + budgetManager.getBalance());
            writer.println("Total:" + budgetManager.getPurchaseManager().getTotal());

            // Save purchases by category
            PurchaseManager purchaseManager = budgetManager.getPurchaseManager();
            for (Map.Entry<String, List<Item>> entry : purchaseManager.getAllPurchases().entrySet()) {
                writer.println(entry.getKey());
                for (Item item : entry.getValue()) {
                    writer.println(item.toString());
                }
            }
        }
    }

    public void loadBudgetData(BudgetManager budgetManager) throws IOException {
        if (!file.exists()) {
            return;
        }

        PurchaseManager purchaseManager = budgetManager.getPurchaseManager();
        purchaseManager.clear();

        try (Scanner fileScanner = new Scanner(file)) {
            String currentCategory = null;

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();

                if (line.isEmpty()) {
                    continue;
                }

                if (line.startsWith("Income:")) {
                    double income = Double.parseDouble(line.substring("Income:".length()));
                    budgetManager.setIncome(income);
                    continue;
                }

                if (line.startsWith("Balance:")) {
                    double balance = Double.parseDouble(line.substring("Balance:".length()));
                    budgetManager.setBalance(balance);
                    continue;
                }

                if (line.startsWith("Total:")) {
                    double total = Double.parseDouble(line.substring("Total:".length()));
                    purchaseManager.setTotal(total);
                    continue;
                }

                // Category check
                if (line.equals("Food") || line.equals("Clothes") ||
                        line.equals("Entertainment") || line.equals("Other")) {
                    currentCategory = line;
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
                            purchaseManager.addItem(currentCategory, name, price);
                        } catch (NumberFormatException e) {
                            System.out.println("Error parsing price: " + priceStr);
                        }
                    }
                }
            }
        }
    }

    public boolean exists() {
        return file.exists();
    }
}