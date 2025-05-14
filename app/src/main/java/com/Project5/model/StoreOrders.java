package com.Project5.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class to store all orders placed in the RUBurger app.  
 *  
 * @author Daisy Hernandez
 */
public class StoreOrders {
    private static final StoreOrders instance = new StoreOrders();
    private final ArrayList<Order> orders;
    
    /**
     * Private constructor to enforce singleton pattern
     */
    private StoreOrders() {
        orders = new ArrayList<>();
    }
    
    /**
     * Get the singleton instance
     * @return The StoreOrders instance
     */
    public static StoreOrders getInstance() {
        return instance;
    }
    
    /**
     * Add a completed order to the store records
     * @param order The order to add
     */
    public void addOrder(Order order) {
        orders.add(order);
    }
    
    /**
     * Get all orders placed
     * @return List of all orders
     */
    public List<Order> getOrders() {
        return orders;
    }
    
    /**
     * Clear all orders
     */
    public void clearOrders() {
        orders.clear();
    }
} 