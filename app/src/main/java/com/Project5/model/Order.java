package com.Project5.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer's order in the RUBurger app.
 * Contains a list of menu items and handles order-specific calculations.
 *  
 * @author Daisy Hernandez
 */
public class Order {
    private final ArrayList<MenuItem> items;
    private static int nextOrderNumber = 1;
    private final int number;
    private final double taxRate = 0.06625; // NJ sales tax

    /**
     * Constructs a new order with a unique order number
     */
    public Order() {
        this.number = nextOrderNumber++;
        this.items = new ArrayList<>();
    }

    /**
     * Adds a menu item to the order.
     * @param item Menu item to add
     */ 
    public void add(MenuItem item) {
        items.add(item);
    }

    /**
     * Removes a menu item from the order.
     * @param item Menu item to remove
     */
    public void remove(MenuItem item) {
        items.remove(item);
    }

    /**
     * Clears all menu items from the order.
     */
    public void clear() {
        items.clear();
    }

    /**
     * Subtotal (sum of item prices, respecting quantity)
     */
    public double getSubtotal() {
        double subtotal = 0.0;
        for (MenuItem item : items) {
            subtotal += item.price(); 
        }
        return subtotal;
    }

    /**
     * Tax amount based on subtotal
     */
    public double getTax() {
        return getSubtotal() * taxRate;
    }

    /**
     * Total = subtotal + tax
     */
    public double getTotal() {
        return getSubtotal() + getTax();
    }

    /**
     * Returns the list of menu items in the order.
     * @return List of menu items in the order
     */
    public List<MenuItem> getItems() {
        return items;
    }

    /**
     * Returns the order number.
     * @return Order number
     */ 
    public int getOrderNumber() {
        return number;
    }

    /**
     * Returns the next order number.
     * @return Next order number
     */ 
    public static int getNextOrderNumber() {
        return nextOrderNumber;
    }

    /**
     * Returns a string representation of the order.
     * @return String representation of the order
     */ 
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Order #" + number + ":\n");
        for (MenuItem item : items) {
            sb.append(" - ").append(item).append("\n");
        }
        sb.append("Subtotal: $").append(String.format("%.2f", getSubtotal())).append("\n");
        sb.append("Tax (6.625%): $").append(String.format("%.2f", getTax())).append("\n");
        sb.append("Total: $").append(String.format("%.2f", getTotal())).append("\n");
        return sb.toString();
    }
}
