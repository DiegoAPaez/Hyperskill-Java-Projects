package budget;

import java.util.*;

public class PurchaseManager {
    private final Map<String, List<Item>> purchaseList;
    private double total;

    public PurchaseManager() {
        this.purchaseList = new HashMap<>();
        this.total = 0;
    }

    public void addItem(String category, String name, double price) {
        if (!purchaseList.containsKey(category)) {
            purchaseList.put(category, new ArrayList<>());
        }

        List<Item> items = purchaseList.get(category);
        items.add(new Item(name, price));
        purchaseList.put(category, items);

        total += price;
    }

    public List<Item> getItemsByCategory(String category) {
        return purchaseList.getOrDefault(category, Collections.emptyList());
    }

    public Map<String, List<Item>> getAllPurchases() {
        return purchaseList;
    }

    public boolean isEmpty() {
        return purchaseList.isEmpty();
    }

    public void clear() {
        purchaseList.clear();
        total = 0;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<Item> getAllItems() {
        List<Item> allItems = new ArrayList<>();
        for (List<Item> items : purchaseList.values()) {
            allItems.addAll(items);
        }
        return allItems;
    }

    public Map<String, Double> getCategoryTotals() {
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

        return categoryTotals;
    }
}