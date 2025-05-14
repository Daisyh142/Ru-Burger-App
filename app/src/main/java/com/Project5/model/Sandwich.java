package com.Project5.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Customizable sandwich menu item with various bread, protein, and add-on options.
 * Base pricing depends on protein type with additional charges for add-ons.
 *  
 * @author Daisy Hernandez  
 */
public class Sandwich extends MenuItem {
    protected Bread bread;
    protected Protein protein;
    protected List<AddOns> addOns;

    /**
     * Default constructor with standard options.
     */
    public Sandwich() {
        this.bread = Bread.BRIOCHE;
        this.protein = Protein.CHICKEN;
        this.addOns = new ArrayList<>();
        this.quantity = 1;
    }

    /**
     * Full customization constructor.
     */
    public Sandwich(Bread bread, Protein protein, List<AddOns> addOns) {
        this.bread = bread;
        this.protein = protein;
        this.addOns = addOns;
        this.quantity = 1;
    }

    /**
     * Returns the price of the given add-on.
     * @param addOn the AddOns enum value
     * @return price of the add-on
     */
    protected double getAddOnPrice(AddOns addOn) {
        if (addOn == null) {
            return 0.0;
        }

        switch (addOn) {
            case LETTUCE:
            case TOMATOES:
                return 0.30;
            case ONIONS:
                return 0.35;
            case AVOCADO:
                return 0.50;
            case CHEESE:
                return 1.00;
            default:
                return 0.0;
        }
    }

    /**
     * Returns the price of the selected protein.
     * @param protein the Protein enum value
     * @return base price of the protein
     */
    protected double getProteinPrice(Protein protein) {
        if (protein == null) {
            return 0.0;
        }

        switch (protein) {
            case ROAST_BEEF:
                return 10.99;
            case CHICKEN:
                return 8.99;
            case SALMON:
                return 9.99;
            default:
                return 0.0;
        }
    }

    /**
     * Calculates the price of one sandwich (excluding quantity).
     * @return The unit price of the sandwich
     */
    @Override
    public double itemPrice() {
        double addOnCost = 0.0;
        for (AddOns addOn : addOns) {
            addOnCost += getAddOnPrice(addOn);
        }
        return getProteinPrice(protein) + addOnCost;
    }

    /**
     * Returns the total price including quantity.
     * @return Total price of the sandwich
     */
    @Override
    public double price() {
        return itemPrice() * quantity;
    }

    /**
     * Returns a string representation of the sandwich.
     * Includes bread, protein, add-ons, quantity, and price.
     * @return String representation of the sandwich
     */
    @Override
    public String toString() {
        StringBuilder description = new StringBuilder();
        description.append("Sandwich [").append(protein).append(" on ").append(bread);

        if (addOns != null && !addOns.isEmpty()) {
            description.append(" with ");
            for (int i = 0; i < addOns.size(); i++) {
                description.append(addOns.get(i));
                if (i < addOns.size() - 1) {
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
     * Returns the bread of the sandwich.
     * @return Bread of the sandwich
     */
    public Bread getBread() {
        return bread;
    }

    /**
     * Sets the bread of the sandwich.
     * @param bread Bread to set
     */ 
    public void setBread(Bread bread) {
        this.bread = bread;
    }

    /**
     * Returns the protein of the sandwich.
     * @return Protein of the sandwich
     */
    public Protein getProtein() {
        return protein;
    }

    /**
     * Sets the protein of the sandwich.
     * @param protein Protein to set
     */
    public void setProtein(Protein protein) {
        this.protein = protein;
    }

    /**
     * Returns the add-ons of the sandwich.
     * @return Add-ons of the sandwich
     */ 
    public List<AddOns> getAddOns() {
        return addOns;
    }

    /**
     * Sets the add-ons of the sandwich.
     * @param addOns Add-ons to set
     */ 
    public void setAddOns(List<AddOns> addOns) {
        this.addOns = addOns;
    }
}
