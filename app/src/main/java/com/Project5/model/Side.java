package com.Project5.model;

/**
 * Side dish menu item in the RUBurger app with size options.
 * Price varies based on side type and size selection.
 * @author Daisy Hernandez
 */
public class Side extends MenuItem {
    private final Sides side;
    private final Size size;

    /**
     * Constructs a new side dish with the specified type and size.
     *
     * @param side The side dish type (enum).
     * @param size The size of the side (SMALL, MEDIUM, LARGE).
     */
    public Side(Sides side, Size size) {
        this.side = side;
        this.size = size;
        this.quantity = 1;
    }

    /**
     * Constructs a new side dish with just the name.
     *
     * @param name The name of the side dish.
     */
    public Side(String name) {
        this.side = Sides.valueOf(name);
        this.size = Size.SMALL;
        this.quantity = 1;
    }

    /**
     * Returns the price of one side dish unit (excluding quantity).
     *
     * @return Unit price of the side dish
     */
    @Override
    public double itemPrice() {
        return getSidePrice(side, size);
    }

    /**
     * Returns the total price including quantity.
     *
     * @return Total price of the side dish
     */
    @Override
    public double price() {
        return itemPrice() * quantity;
    }

    @Override
    public String toString() {
        return "Side [" + side + ", " + size + "] x" + quantity +
                " - $" + String.format("%.2f", price());
    }

    /**
     * Returns the price of side dish and size.
     * @param side The side dish type (enum).
     * @param size The size of the side (SMALL, MEDIUM, LARGE).
    */
    private double getSidePrice(Sides side, Size size) {
        double basePrice = 0.00;
        switch (side) {
            case CHIPS:
                basePrice = 1.99;
                break;
            case FRIES:
                basePrice = 2.49;
                break;
            case ONION_RINGS:
                basePrice = 3.29;
                break;
            case APPLE_SLICES:
                basePrice = 1.29;
                break;
            default:
                return basePrice;
        }
        switch (size) {
            case SMALL:
                return basePrice;
            case MEDIUM:
                return basePrice + 0.50;
            case LARGE:
                return basePrice + 1.00;
            default:
                return basePrice;
        }
    }

    /**
     * Returns the side of the side dish.
     * @return Side of the side dish
     */
    public Sides getSide() {
        return this.side;
    }

}
