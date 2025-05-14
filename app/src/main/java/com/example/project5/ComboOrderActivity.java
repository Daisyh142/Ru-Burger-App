package com.example.project5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.Project5.model.*;

import java.util.ArrayList;

/**
 * Activity for handling combo meal orders in the application.
 * Allows users to create a combo by selecting a base item (burger or sandwich),
 * a side, and a beverage. Manages the order process including price calculation,
 * image updates, and adding to cart.
 *
 * @author Daisy Hernandez
 */

public class ComboOrderActivity extends AppCompatActivity {

    private Spinner baseItemSpinner, sideSpinner, drinkSpinner, quantitySpinner;
    private TextView priceLabel;
    private Button addToCartButton, cancelButton, clearButton, checkoutButton;
    private ImageView baseImageView, sideImageView, drinkImageView;
    private Sandwich sandwich;

    /**
     * Initializes the activity and sets up the UI components.
     * Configures spinners, buttons, and image views, then sets up event listeners.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                          this Bundle contains the data it most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combo_order);

        baseItemSpinner = findViewById(R.id.baseItemSpinner);
        sideSpinner = findViewById(R.id.sideSpinner);
        drinkSpinner = findViewById(R.id.drinkSpinner);
        quantitySpinner = findViewById(R.id.quantitySpinner);

        priceLabel = findViewById(R.id.priceLabel);
        addToCartButton = findViewById(R.id.addToCartButton);
        cancelButton = findViewById(R.id.cancelButton);
        clearButton = findViewById(R.id.clearButton);
        checkoutButton = findViewById(R.id.checkoutButton);

        baseImageView = findViewById(R.id.baseImageView);
        sideImageView = findViewById(R.id.sideImageView);
        drinkImageView = findViewById(R.id.drinkImageView);

        setupSpinners();
        setupListeners();
        updatePrice();
    }

    /**
     * Sets up the spinners with their respective options.
     * Configures adapters for:
     * - Base item (Burger/Sandwich)
     * - Side options (Chips/Apple Slices)
     * - Drink options (Cola/Iced Tea/Apple Juice)
     * - Quantity (1-5)
     */
    private void setupSpinners() {
        ArrayAdapter<String> baseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"Burger", "Sandwich"});
        baseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        baseItemSpinner.setAdapter(baseAdapter);

        ArrayAdapter<Sides> sideAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new Sides[]{Sides.CHIPS, Sides.APPLE_SLICES});
        sideAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sideSpinner.setAdapter(sideAdapter);

        ArrayAdapter<Flavor> drinkAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new Flavor[]{Flavor.COLA, Flavor.ICED_TEA, Flavor.APPLE_JUICE});
        drinkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drinkSpinner.setAdapter(drinkAdapter);

        Integer[] qtyOptions = {1, 2, 3, 4, 5};
        ArrayAdapter<Integer> qtyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, qtyOptions);
        qtyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantitySpinner.setAdapter(qtyAdapter);
    }

    /**
     * Sets up event listeners for all UI components.
     * Handles spinner selection changes and button clicks:
     * - Base item: Updates image and price
     * - Side: Updates image and price
     * - Drink: Updates image and price
     * - Quantity: Updates price
     * - Buttons: Add to cart, cancel, clear, checkout
     */
    private void setupListeners() {
        baseItemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                handleBaseItemSelection();
                updateBaseImage();
                updatePrice();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        sideSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateSideImage();
                updatePrice();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        drinkSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateDrinkImage();
                updatePrice();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        quantitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updatePrice();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        addToCartButton.setOnClickListener(v -> addToCart());
        cancelButton.setOnClickListener(v -> finish());
        clearButton.setOnClickListener(v -> clearSelections());
        checkoutButton.setOnClickListener(v -> goToCheckout());
    }

    /**
     * Handles the selection of the base item (burger or sandwich).
     * Creates a new sandwich instance based on the user's selection:
     * - Burger: Creates with default Brioche bread and no add-ons
     * - Sandwich: Creates with default Brioche bread and Chicken protein
     */
    private void handleBaseItemSelection() {
        String choice = (String) baseItemSpinner.getSelectedItem();
        if (choice == null) return;

        if (choice.equalsIgnoreCase("Burger")) {
            sandwich = new Burger(Bread.BRIOCHE, new ArrayList<>(), false);
        } else {
            sandwich = new Sandwich(Bread.BRIOCHE, Protein.CHICKEN, new ArrayList<>());
        }
    }

    /**
     * Updates the price display based on current selections.
     * Creates a combo with:
     * - Selected base item (burger/sandwich)
     * - Small-sized side (chips/apple slices)
     * - Medium-sized drink (cola/iced tea/apple juice)
     * Calculates total price including quantity and displays it.
     */
    private void updatePrice() {
        try {
            if (sandwich == null || sideSpinner.getSelectedItem() == null || drinkSpinner.getSelectedItem() == null) {
                priceLabel.setText(getString(R.string.incomplete_selection));
                return;
            }

            Side side = new Side((Sides) sideSpinner.getSelectedItem(), Size.SMALL);
            Beverage drink = new Beverage(Size.MEDIUM, (Flavor) drinkSpinner.getSelectedItem());
            Combo combo = new Combo(sandwich, side, drink);

            int qty = (Integer) quantitySpinner.getSelectedItem();
            combo.setQuantity(qty);

            priceLabel.setText(String.format("Total: $%.2f", combo.price()));
        } catch (Exception e) {
            priceLabel.setText(getString(R.string.combo_selection_error) + e.getMessage());
        }
    }

    /**
     * Updates the base item image based on the current selection.
     * Shows either a burger or sandwich image depending on the spinner selection.
     */
    private void updateBaseImage() {
        String choice = (String) baseItemSpinner.getSelectedItem();
        if (choice == null) return;

        if (choice.equalsIgnoreCase("Burger")) {
            baseImageView.setImageResource(R.drawable.burger);
        } else {
            baseImageView.setImageResource(R.drawable.sandwich);
        }
    }

    /**
     * Updates the side image based on the current selection.
     * Shows the appropriate side dish image (chips, fries, onion rings, or apple slices).
     */
    private void updateSideImage() {
        Sides side = (Sides) sideSpinner.getSelectedItem();
        if (side == null) return;

        switch (side) {
            case CHIPS: sideImageView.setImageResource(R.drawable.chips); break;
            case FRIES: sideImageView.setImageResource(R.drawable.fries); break;
            case ONION_RINGS: sideImageView.setImageResource(R.drawable.onion_rings); break;
            case APPLE_SLICES: sideImageView.setImageResource(R.drawable.apple_slices); break;
        }
    }

    /**
     * Updates the drink image based on the current selection.
     * Shows the appropriate beverage image based on the selected flavor.
     * Supports multiple drink options including cola, tea, juices, and other beverages.
     */
    private void updateDrinkImage() {
        Flavor drink = (Flavor) drinkSpinner.getSelectedItem();
        if (drink == null) return;

        switch (drink) {
            case COLA: drinkImageView.setImageResource(R.drawable.cola); break;
            case LEMONADE: drinkImageView.setImageResource(R.drawable.lemonade); break;
            case ORANGE_JUICE: drinkImageView.setImageResource(R.drawable.orange_juice); break;
            case ICED_TEA: drinkImageView.setImageResource(R.drawable.strawberry_iced_tea); break;
            case WATER: drinkImageView.setImageResource(R.drawable.water); break;
            case ICED_COFFEE: drinkImageView.setImageResource(R.drawable.iced_coffee); break;
            case PEPSI: drinkImageView.setImageResource(R.drawable.cola); break;
            case SPRITE: drinkImageView.setImageResource(R.drawable.sprite); break;
            case APPLE_JUICE: drinkImageView.setImageResource(R.drawable.apple_juice); break;
            case GATORADE: drinkImageView.setImageResource(R.drawable.gatorade); break;
        }
    }

    /**
     * Adds the current combo to the cart.
     * Validates that all selections are complete, then:
     * 1. Creates side and drink components with required sizes
     * 2. Creates a combo with the selected items
     * 3. Sets the selected quantity
     * 4. Adds to the shared order
     * 5. Shows confirmation and closes activity
     */
    private void addToCart() {
        if (sandwich == null || sideSpinner.getSelectedItem() == null || drinkSpinner.getSelectedItem() == null) {
            showToast(getString(R.string.incomplete_selection));
            return;
        }

        Side side = new Side((Sides) sideSpinner.getSelectedItem(), Size.SMALL);
        Beverage drink = new Beverage(Size.MEDIUM, (Flavor) drinkSpinner.getSelectedItem());
        Combo combo = new Combo(sandwich, side, drink);

        int qty = (Integer) quantitySpinner.getSelectedItem();
        combo.setQuantity(qty);

        SharedOrder.getInstance().add(combo);

        showToast(getString(R.string.Order_Added));
        finish();
    }

    /**
     * Resets all selections to their default values.
     * - Sets all spinners to first option
     * - Updates base item selection
     * - Refreshes all images
     * - Updates price display
     * - Shows confirmation toast
     */
    private void clearSelections() {
        baseItemSpinner.setSelection(0);
        sideSpinner.setSelection(0);
        drinkSpinner.setSelection(0);
        quantitySpinner.setSelection(0);

        handleBaseItemSelection();

        updateBaseImage();
        updateSideImage();
        updateDrinkImage();
        updatePrice();

        Toast.makeText(this, getString(R.string.clear_order), Toast.LENGTH_SHORT).show();
    }

    /**
     * Navigates to the checkout activity.
     * Creates and starts the CheckOutActivity.
     */
    private void goToCheckout() {
        Intent intent = new Intent(this, CheckOutActivity.class);
        startActivity(intent);
    }

    /**
     * Displays a toast message to the user.
     * Helper method to show short-duration toast notifications.
     * 
     * @param message The message to display in the toast
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
