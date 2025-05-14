package com.Project5.model;

/**
 * Abstract base class representing a menu item in the RUBurger app.
 * Provides common attributes and behaviors for all menu items.
 *  
 * @author Daisy Hernandez  
 */
public abstract class MenuItem {
    protected int quantity; // The number of this item ordered

    /**
     * Returns the price of one unit of this item (excluding quantity).
     * @return Price of one unit.
     */
    public abstract double itemPrice();

    /**
     * Returns the total price of the item including quantity.
     * @return Total price (itemPrice × quantity).
     */
    public abstract double price();

    /**
     * Returns a string representation of the menu item.
     * @return Formatted string with item details and price.
     */
    @Override
    public abstract String toString();

    /**
     * Gets the quantity of this item.
     * @return Quantity ordered.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of this item.
     * @param quantity New quantity value.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
