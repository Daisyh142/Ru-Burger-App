package com.Project5.model;

/**
 * Beverage menu item with size and flavor options.
 * Price is determined by size (small, medium, large).
 *  
 * @author Daisy Hernandez
 */
public class Beverage extends MenuItem {
    private Size size;
    private Flavor flavor;

    /**
     * Creates a beverage with specified size and flavor.
     */
    public Beverage(Size size, Flavor flavor) {
        this.size = size;
        this.flavor = flavor;
        this.quantity = 1;
    }

    /**
     * Returns the price of one beverage unit (excluding quantity).
     * @return Unit price of the beverage
     */
    @Override
    public double itemPrice() {
        return getSizePrice(size);
    }

    /**
     * Returns the total price including quantity.
     * @return Total price of the beverage
     */
    @Override
    public double price() {
        return itemPrice() * quantity;
    }

    /**
     * Maps size to corresponding price.
     */
    private double getSizePrice(Size size) {
        switch (size) {
            case SMALL:
                return 1.99;
            case MEDIUM:
                return 2.49;
            case LARGE:
                return 2.99;
            default:
                return 0.00;
        }
    }

    @Override
    public String toString() {
        return "Beverage [" + size + " " + flavor + "] x" + quantity +
               " - $" + String.format("%.2f", price());
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public Flavor getFlavor() {
        return flavor;
    }

    public void setFlavor(Flavor flavor) {
        this.flavor = flavor;
    }

}
