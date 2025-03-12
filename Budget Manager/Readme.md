# Budget Manager Documentation

## Overview
The Budget Manager is a console application that helps users track their income, expenses, and analyze their spending habits. The application allows users to add income, record purchases across different categories, view purchase lists, save and load data, and perform analysis on spending patterns.

## Architecture
The application follows the MVC (Model-View-Controller) design pattern with additional patterns like Facade and Singleton for better organization.

### Class Structure

```
budget/
├── Main.java                 - Application entry point
├── BudgetUI.java             - User interface handling
├── BudgetManager.java        - Core business logic facade (Singleton)
├── PurchaseManager.java      - Purchase data management
├── FileManager.java          - File operations
├── Item.java                 - Purchase item entity
└── Categories.java           - Enumeration of purchase categories
```

## Class Descriptions

### Main.java
The entry point of the application that initializes the UI and starts the application.

**Key Responsibilities:**
- Initialize the application
- Start the user interface

### BudgetUI.java
Handles all user interaction, including displaying menus, getting input, and showing results.

**Key Responsibilities:**
- Display menus and prompts
- Process user input
- Format and display output
- Coordinate application flow
- Access the BudgetManager singleton instance

**Key Methods:**
- `start()`: Main application loop
- `showMenu()`: Display main menu
- `performAction()`: Execute selected action
- `analyzeMenu()`: Handle analysis and sorting options
- Various methods for displaying and sorting purchases

### BudgetManager.java
Manages the core financial data and coordinates between UI and data management components. Implemented as a Singleton.

**Key Responsibilities:**
- Track income and balance
- Coordinate purchase management
- Provide a simplified interface to other components
- Ensure a single instance exists throughout the application

**Key Methods:**
- `getInstance()`: Get the singleton instance of BudgetManager
- `addIncome(double amount)`: Add income to budget
- `addPurchase(String category, String name, double price)`: Record a purchase
- Getters and setters for financial data
- `resetInstance()`: Reset the singleton instance (for testing)

### PurchaseManager.java
Handles all purchase-related operations and data.

**Key Responsibilities:**
- Store and organize purchases by category
- Calculate totals and statistics
- Provide query and filtering capabilities

**Key Methods:**
- `addItem(String category, String name, double price)`: Add a purchase
- `getItemsByCategory(String category)`: Get items in a specific category
- `getAllItems()`: Get all purchased items
- `getCategoryTotals()`: Calculate spending by category

### FileManager.java
Manages persistence of application data.

**Key Responsibilities:**
- Save budget and purchase data to file
- Load budget and purchase data from file

**Key Methods:**
- `saveBudgetData(BudgetManager budgetManager)`: Save all data
- `loadBudgetData(BudgetManager budgetManager)`: Load all data

### Item.java
Represents a purchase item with name and price.

**Key Responsibilities:**
- Store item information
- Provide string representation

**Key Methods:**
- `toString()`: Format item for display
- Various getters and setters

### Categories.java
Enumeration of purchase categories.

## User Guide

### Main Features

1. **Add Income**
   - Record incoming money

2. **Add Purchase**
   - Record expenses in different categories:
     - Food
     - Clothes
     - Entertainment
     - Other

3. **Show List of Purchases**
   - View purchases by category or all at once

4. **Balance**
   - Check current balance

5. **Save**
   - Save data to file

6. **Load**
   - Load data from file

7. **Analyze (Sort)**
   - Sort all purchases by amount
   - View spending by category
   - Sort items within a specific category

### Usage Workflow

1. Start by adding income
2. Record purchases as you make them
3. Periodically review purchases
4. Analyze spending patterns
5. Save data to preserve records

## Technical Details

### Data Storage Format
Data is stored in a text file (purchases.txt) with the following format:
```
Income:[amount]
Balance:[amount]
Total:[amount]
[Category1]
Item1 $price1
Item2 $price2
[Category2]
Item3 $price3
...
```

### Singleton Implementation
The BudgetManager class is implemented as a Singleton, ensuring that only one instance of it exists throughout the application:

```java
public class BudgetManager {
    // Static instance variable
    private static BudgetManager instance;
    
    // Private constructor prevents instantiation from outside
    private BudgetManager() {
        // Initialization
    }
    
    // Static method to get the singleton instance
    public static BudgetManager getInstance() {
        if (instance == null) {
            instance = new BudgetManager();
        }
        return instance;
    }
    
    // Other methods...
    
    // For testing or resetting the application
    public static void resetInstance() {
        instance = null;
    }
}
```

To use the BudgetManager in other classes:

```java
// Instead of: BudgetManager budgetManager = new BudgetManager();
BudgetManager budgetManager = BudgetManager.getInstance();
```

### Extension Points
The application can be extended in the following ways:

1. **New Categories**: Add new categories to the Categories enum
2. **Enhanced Analysis**: Add more analysis capabilities to the PurchaseManager
3. **Different Storage**: Modify FileManager to support different storage formats
4. **UI Improvements**: Enhance BudgetUI with more user-friendly features
5. **Thread Safety**: Enhance the Singleton implementation for thread safety if needed

## Design Patterns Used

1. **MVC (Model-View-Controller)**
   - Model: BudgetManager, PurchaseManager
   - View: BudgetUI
   - Controller: BudgetUI (action handling parts)

2. **Facade Pattern**
   - BudgetManager serves as a facade to simplify access to the underlying systems

3. **Singleton Pattern**
   - BudgetManager ensures only one instance exists throughout the application
   - Provides global access point to this instance
   - Controls instantiation process

4. **Single Responsibility Principle**
   - Each class has a well-defined responsibility

## Thread Safety Considerations

The basic Singleton implementation provided is not thread-safe. For a multi-threaded application, consider one of these alternatives:

1. **Synchronized getInstance method**:
```java
public static synchronized BudgetManager getInstance() {
    if (instance == null) {
        instance = new BudgetManager();
    }
    return instance;
}
```

2. **Double-checked locking**:
```java
public static BudgetManager getInstance() {
    if (instance == null) {
        synchronized (BudgetManager.class) {
            if (instance == null) {
                instance = new BudgetManager();
            }
        }
    }
    return instance;
}
```

3. **Initialization-on-demand holder**:
```java
public class BudgetManager {
    private BudgetManager() {}
    
    private static class BudgetManagerHolder {
        private static final BudgetManager INSTANCE = new BudgetManager();
    }
    
    public static BudgetManager getInstance() {
        return BudgetManagerHolder.INSTANCE;
    }
}
```
