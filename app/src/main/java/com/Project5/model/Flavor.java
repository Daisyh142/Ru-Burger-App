package com.Project5.model;

/**
 * Represents the different flavors available for beverages.
 */
public enum Flavor {
    COLA,
    LEMONADE,
    ORANGE_JUICE,
    ICED_TEA,
    WATER,
    ICED_COFFEE,
    PEPSI,
    SPRITE,
    APPLE_JUICE,
    GATORADE;

    @Override
    public String toString() {
        return name().replace('_', ' ').toLowerCase();
    }

    public Flavor getFlavor(){
        return this;
    }
}
