package com.Project5.model;

/** 
 * Singleton class to share the same Order object across different controllers.
 *  
 * @author Daisy Hernandez
 */
public class SharedOrder {
    private static Order instance = new Order();
    
    /**
     * Private constructor to prevent instantiation
     */
    private SharedOrder() {}
    
    /**
     * Get the shared Order instance
     * @return The singleton Order instance
     */
    public static Order getInstance() {
        return instance;
    }

    /** 
     * Returns the current order instance
     * @return The singleton Order instance
     */
    public static Order getCurrentOrder() {
        return instance;
    }
    
    /**
     * Reset the order instance
     */
    public static void resetOrder() {
        instance = new Order();
    }
} 