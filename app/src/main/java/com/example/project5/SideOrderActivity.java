
package com.example.project5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.Project5.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for handling side dish orders in the application.
 * Allows users to customize their side dish with different options and sizes.
 * Manages the order process including adding to cart and checkout.
 *
 * @author Daisy Hernandez
 */

public class SideOrderActivity extends AppCompatActivity {

    private static final int MIN_QUANTITY = 1;
    private static final int MAX_QUANTITY = 10;
    private static final int DEFAULT_SELECTION = 0;

    private RadioGroup sideRadioGroup, sizeRadioGroup;
    private RadioButton chipsButton, friesButton, onionRingsButton, appleSlicesButton;
    private RadioButton smallButton, mediumButton, largeButton;
    private Spinner quantitySpinner;
    private TextView priceLabel;
    private ImageView sideImage;
    private Button addToCartButton, checkOutButton, clearOrderButton, cancelOrderButton, comboButton;

    private Sides selectedSide;
    private Size selectedSize;
    private int quantity = MIN_QUANTITY;

    /**
     * Initializes the activity and sets up the UI components.
     * Configures radio buttons, spinners, and buttons for side dish customization.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                          this Bundle contains the data it most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_order);

        sideRadioGroup = findViewById(R.id.sideRadioGroup);
        sizeRadioGroup = findViewById(R.id.sizeRadioGroup);
        chipsButton = findViewById(R.id.radioChips);
        friesButton = findViewById(R.id.radioFries);
        onionRingsButton = findViewById(R.id.radioOnionRings);
        appleSlicesButton = findViewById(R.id.radioAppleSlices);

        smallButton = findViewById(R.id.radioSmall);
        mediumButton = findViewById(R.id.radioMedium);
        largeButton = findViewById(R.id.radioLarge);

        quantitySpinner = findViewById(R.id.quantitySpinner);
        priceLabel = findViewById(R.id.priceLabel);
        sideImage = findViewById(R.id.sideImage);

        addToCartButton = findViewById(R.id.addToCartButton);
        checkOutButton = findViewById(R.id.checkOutButton);
        clearOrderButton = findViewById(R.id.clearOrderButton);
        cancelOrderButton = findViewById(R.id.cancelOrderButton);
        comboButton = findViewById(R.id.comboButton);

        setupQuantitySpinner();
        setupListeners();
        resetSelections();
    }

    /**
     * Sets up the quantity spinner with options from MIN_QUANTITY to MAX_QUANTITY.
     * Creates an adapter for the spinner and sets the default selection.
     */
    private void setupQuantitySpinner() {
        List<Integer> qtyOptions = new ArrayList<>();
        for (int i = MIN_QUANTITY; i <= MAX_QUANTITY; i++) {
            qtyOptions.add(i);
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, qtyOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantitySpinner.setAdapter(adapter);
        quantitySpinner.setSelection(DEFAULT_SELECTION);
    }

    /**
     * Sets up event listeners for all UI components.
     * Handles side selection, size selection, quantity changes, and button clicks.
     */
    private void setupListeners() {
        sideRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            updatePrice();
            updateSideImage();
        });

        sizeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> updatePrice());

        quantitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updatePrice();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        addToCartButton.setOnClickListener(v -> handleAddToCart());
        checkOutButton.setOnClickListener(v -> handleCheckOut());
        clearOrderButton.setOnClickListener(v -> handleClearOrder());
        cancelOrderButton.setOnClickListener(v -> handleCancelOrder());
        comboButton.setOnClickListener(v -> handleOrderAsCombo());
    }

    /**
     * Resets all selections to their default values.
     * Sets chips and small size as default, resets quantity to minimum.
     */
    private void resetSelections() {
        chipsButton.setChecked(true);
        smallButton.setChecked(true);
        quantitySpinner.setSelection(DEFAULT_SELECTION);
        selectedSide = Sides.CHIPS;
        selectedSize = Size.SMALL;
        quantity = MIN_QUANTITY;
        updatePrice();
        updateSideImage();
    }

    /**
     * Handles the "Add to Cart" button click.
     * Validates the order and adds the side dish to the shared order.
     */
    private void handleAddToCart() {
        selectedSide = getSelectedSide();
        selectedSize = getSelectedSize();
        quantity = (Integer) quantitySpinner.getSelectedItem();

        if (selectedSide == null || selectedSize == null || quantity <= 0) {
            showToast(getString(R.string.incomplete_selection));
            return;
        }

        Side side = new Side(selectedSide, selectedSize);
        side.setQuantity(quantity);
        SharedOrder.getInstance().add(side);

        showToast(getString(R.string.Side_added));
        finish();
    }

    /**
     * Handles the "Check Out" button click.
     * Navigates to the checkout activity.
     */
    private void handleCheckOut() {
        Intent intent = new Intent(this, CheckOutActivity.class);
        startActivity(intent);
    }

    /**
     * Handles the "Clear Order" button click.
     * Resets all selections to their default values.
     */
    private void handleClearOrder() {
        resetSelections();
        showToast(getString(R.string.clear_order));
    }

    /**
     * Handles the "Cancel Order" button click.
     * Closes the current activity.
     */
    private void handleCancelOrder() {
        showToast(getString(R.string.cancel_order));
        finish();
    }

    /**
     * Handles the "Order as Combo" button click.
     * Navigates to the combo order activity.
     */
    private void handleOrderAsCombo() {
        Intent intent = new Intent(this, ComboOrderActivity.class);
        startActivity(intent);
    }

    /**
     * Updates the side dish image based on the current selection.
     * Shows the appropriate image for the selected side dish.
     */
    private void updateSideImage() {
        selectedSide = getSelectedSide();
        if (selectedSide == null) {
            sideImage.setImageResource(R.drawable.fries);
            return;
        }

        switch (selectedSide) {
            case CHIPS:
                sideImage.setImageResource(R.drawable.chips);
                break;
            case FRIES:
                sideImage.setImageResource(R.drawable.fries);
                break;
            case ONION_RINGS:
                sideImage.setImageResource(R.drawable.onion_rings);
                break;
            case APPLE_SLICES:
                sideImage.setImageResource(R.drawable.apple_slices);
                break;
            default:
                sideImage.setImageResource(R.drawable.fries);
        }
    }

    /**
     * Updates the price display based on current selections.
     * Calculates the total price considering side type, size, and quantity.
     */
    private void updatePrice() {
        selectedSide = getSelectedSide();
        selectedSize = getSelectedSize();
        quantity = (Integer) quantitySpinner.getSelectedItem();

        if (selectedSide == null || selectedSize == null || quantity <= 0) {
            priceLabel.setText(getString(R.string.price_default));
            return;
        }

        Side side = new Side(selectedSide, selectedSize);
        side.setQuantity(quantity);
        priceLabel.setText(String.format("$%.2f", side.price()));
    }

    /**
     * Gets the currently selected side dish type.
     * @return The selected Sides enum value, or null if none selected
     */
    private Sides getSelectedSide() {
        int id = sideRadioGroup.getCheckedRadioButtonId();
        if (id == R.id.radioChips) return Sides.CHIPS;
        if (id == R.id.radioFries) return Sides.FRIES;
        if (id == R.id.radioOnionRings) return Sides.ONION_RINGS;
        if (id == R.id.radioAppleSlices) return Sides.APPLE_SLICES;
        return null;
    }

    /**
     * Gets the currently selected size.
     * @return The selected Size enum value, or null if none selected
     */
    private Size getSelectedSize() {
        int id = sizeRadioGroup.getCheckedRadioButtonId();
        if (id == R.id.radioSmall) return Size.SMALL;
        if (id == R.id.radioMedium) return Size.MEDIUM;
        if (id == R.id.radioLarge) return Size.LARGE;
        return null;
    }

    /**
     * Displays a toast message to the user.
     * @param message The message to display
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
