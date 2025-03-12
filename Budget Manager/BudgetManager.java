package budget;

public class BudgetManager {
    // Static instance variable
    private static BudgetManager instance;

    private double income;
    private double balance;
    private final PurchaseManager purchaseManager;

    // Private constructor prevents instantiation from outside
    private BudgetManager() {
        this.income = 0;
        this.balance = 0;
        this.purchaseManager = new PurchaseManager();
    }

    // Static method to get the singleton instance
    public static BudgetManager getInstance() {
        if (instance == null) {
            instance = new BudgetManager();
        }
        return instance;
    }

    public void addIncome(double amount) {
        this.income += amount;
        this.balance += amount;
    }

    public void addPurchase(String category, String name, double price) {
        this.balance -= price;
        this.purchaseManager.addItem(category, name, price);
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public PurchaseManager getPurchaseManager() {
        return purchaseManager;
    }

    // For testing or resetting the application
    public static void resetInstance() {
        instance = null;
    }
}