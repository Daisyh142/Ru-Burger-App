package com.Project5.model;

/**
 * Combo meal that bundles a sandwich, side, and beverage.
 * The combo side must be a small-sized item and either "chips" or "apple slices".
 * The drink must be medium-sized and one of "cola", "tea", or "apple juice".
 * 
 * @author Daisy Hernandez
 */
public class Combo extends MenuItem {
    private final Sandwich sandwich;
    private final Side side;
    private final Beverage drink;

    /**
     * Creates a combo with the specified components.
     * Validates that the side and beverage meet the combo requirements.
     *
     * @param sandwich the sandwich component (burger or sandwich)
     * @param side the side component; must be "chips" or "apple slices"
     * @param drink the beverage component; must be medium size and one of "cola", "iced tea", or "apple juice"
     * @throws IllegalArgumentException if any component does not meet the combo requirements
     */
    public Combo(Sandwich sandwich, Side side, Beverage drink) {
        if (sandwich == null || side == null || drink == null) {
            throw new IllegalArgumentException("Combo components cannot be null.");
        }
        this.sandwich = sandwich;
        this.side = comboSide(side);
        this.drink = comboDrink(drink);
        this.quantity = 1;
    }

    /**
     * Helper method to validate and configure the combo side.
     * Ensures the side is either chips or apple slices and sets it to small size.
     *
     * @param sides the original side
     * @return a valid combo side (small chips or apple slices)
     * @throws IllegalArgumentException if the side is invalid
     */
    private Side comboSide(Side sides) {
        if (sides == null) throw new IllegalArgumentException("Side cannot be null for combo.");

        Sides sideName = sides.getSide();
        if (sideName == Sides.CHIPS) {
            return new Side(Sides.CHIPS, Size.SMALL);
        } else if (sideName == Sides.APPLE_SLICES) {
            return new Side(Sides.APPLE_SLICES, Size.SMALL);
        } else {
            throw new IllegalArgumentException("Invalid side for combo: must be chips or apple slices.");
        }
    }

    /**
     * Helper method to validate and configure the combo drink.
     * Ensures the drink is medium size and one of the allowed flavors.
     *
     * @param drink the original beverage
     * @return a valid combo drink (medium cola, iced tea, or apple juice)
     * @throws IllegalArgumentException if the drink is invalid
     */
    private Beverage comboDrink(Beverage drink) {
        if (drink == null) throw new IllegalArgumentException("Drink cannot be null for combo.");

        if (drink.getSize() != Size.MEDIUM) {
            throw new IllegalArgumentException("Combo drink must be medium size.");
        }

        Flavor flavor = drink.getFlavor();
        if (flavor == Flavor.COLA || flavor == Flavor.ICED_TEA || flavor == Flavor.APPLE_JUICE) {
            return new Beverage(Size.MEDIUM, flavor);
        } else {
            throw new IllegalArgumentException("Invalid drink for combo: must be cola, iced tea, or apple juice.");
        }
    }

    /**
     * Calculates the price for a single combo (sum of components minus $1.00 discount).
     * The discount is applied to encourage combo purchases.
     *
     * @return price of a single combo
     */
    @Override
    public double itemPrice() {
        return sandwich.itemPrice() + side.itemPrice() + drink.itemPrice() - 1.00;
    }

    /**
     * Calculates the total price considering the quantity.
     * Multiplies the single combo price by the quantity.
     *
     * @return total price
     */
    @Override
    public double price() {
        return itemPrice() * quantity;
    }

    /**
     * Gets the sandwich component of the combo.
     * @return the sandwich
     */
    public Sandwich getSandwich() {
        return sandwich;
    }

    /**
     * Gets the side component of the combo.
     * @return the side
     */
    public Side getSide() {
        return side;
    }

    /**
     * Gets the beverage component of the combo.
     * @return the drink
     */
    public Beverage getDrink() {
        return drink;
    }

    /**
     * Returns a string representation of the combo.
     * Includes the type of combo, all components, quantity, and total price.
     *
     * @return string representation of the combo
     */
    @Override
    public String toString() {
        String comboType = (sandwich instanceof Burger) ? "Burger Combo" : "Sandwich Combo";
        return comboType + ":\n  " + sandwich +
                "\n  " + side +
                "\n  " + drink +
                "\nQty: " + quantity +
                "\nTotal: $" + String.format("%.2f", price());
    }
}
