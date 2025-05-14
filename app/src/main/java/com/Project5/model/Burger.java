package com.Project5.model;

import java.util.List;

/**
 * Represents a burger menu item, which is a specific type of sandwich. 
 *  
 * @author Daisy Hernandez
 */
public class Burger extends Sandwich {
    private boolean doublePatty;

    /**
     * Creates a burger with the specified bread, add-ons, and patty type.
     * Only three bread options (Brioche, Wheat Bread, or Pretzel) are allowed for burgers.
     *
     * @param bread The chosen bread for the burger.
     * @param addOns List of add-ons for the burger.
     * @param doublePatty True if the burger has a double patty; false for a single patty.
     * @throws IllegalArgumentException if the provided bread is not one of the allowed options.
     */
    public Burger(Bread bread, List<AddOns> addOns, boolean doublePatty) {
        super(burgerBreadOptions(bread), Protein.ROAST_BEEF, addOns);
        this.doublePatty = doublePatty;
        this.quantity = 1;
    }

    /**
     * Helper method that validates the provided bread for a burger.
     * Only Brioche, Wheat Bread, or Pretzel are allowed.
     *
     * @param bread The bread option to validate.
     * @return the validated Bread object.
     * @throws IllegalArgumentException if the bread is not one of the allowed options.
     */
    private static Bread burgerBreadOptions(Bread bread) {
        if (bread == Bread.BRIOCHE || bread == Bread.WHEAT_BREAD || bread == Bread.PRETZEL) {
            return bread;
        } else {
            throw new IllegalArgumentException("Invalid bread for burger: " + bread + ". Allowed options are Brioche, Wheat Bread, or Pretzel.");
        }
    }

    /**
     * Returns the price of one burger unit (excluding quantity).
     * @return Unit price of the burger.
     */
    @Override
    public double itemPrice() {
        double basePrice = 6.99;
        if (doublePatty) {
            basePrice += 2.50;
        }

        double addOnCost = 0.0;
        for (AddOns addOn : getAddOns()) {
            addOnCost += getAddOnPrice(addOn);
        }

        return basePrice + addOnCost;
    }

    /**
     * Returns the total price including quantity.
     * @return Total price of the burger.
     */
    @Override
    public double price() {
        return itemPrice() * quantity;
    }

    /**
     * Returns a string representation of the burger, including its details.
     * @return String representation of the burger.
     */
    @Override
    public String toString() {
        StringBuilder description = new StringBuilder();
        description.append("Burger [").append(doublePatty ? "Double" : "Single").append(" Patty on ").append(getBread());

        if (getAddOns() != null && !getAddOns().isEmpty()) {
            description.append(" with ");
            for (int i = 0; i < getAddOns().size(); i++) {
                description.append(getAddOns().get(i));
                if (i < getAddOns().size() - 1) {
                    description.append(", ");
                }
            }
        } else {
            description.append(" with no add-ons");
        }

        description.append("] x").append(quantity);
        description.append(" - $").append(String.format("%.2f", price()));
        return description.toString();
    }


    /**
     * Getter method for the doublePatty field.
     * @return True if the burger has a double patty; false for a single patty.
     */
    public boolean isDoublePatty() {
        return doublePatty;
    }

    /**
     * Setter method for the doublePatty field.
     * @param doublePatty True if the burger has a double patty; false for a single patty.
     */
    public void setDoublePatty(boolean doublePatty) {
        this.doublePatty = doublePatty;
    }

}
