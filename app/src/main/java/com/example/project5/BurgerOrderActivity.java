package com.example.project5;

import com.Project5.model.*;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/** This class represents the activity for handling burger orders in the application.
 * It allows users to customize their burger with different bread types, patty options,
 * and add-ons. Manages the order process including adding to cart and checkout.
 * @author Daisy Hernandez
*/
public class BurgerOrderActivity extends AppCompatActivity {

    private Burger burger;
    private Spinner breadSpinner, quantitySpinner;
    private TextView priceLabel;
    private RadioGroup pattyTypeRadioGroup;
    private RadioButton singlePattyRadio, doublePattyRadio;
    private CheckBox lettuceCheckbox, tomatoCheckbox, onionsCheckbox, avacadoCheckbox;
    private Button addToCartButton, comboButton, checkOutButton, clearOrderButton, cancelOrderButton;

    /**
     * Initializes the activity and sets up the UI components.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burger_order);

        breadSpinner = findViewById(R.id.breadSpinner);
        quantitySpinner = findViewById(R.id.quantitySpinner);
        priceLabel = findViewById(R.id.priceLabel);
        pattyTypeRadioGroup = findViewById(R.id.pattyTypeRadioGroup);
        singlePattyRadio = findViewById(R.id.singlePattyRadio);
        doublePattyRadio = findViewById(R.id.doublePattyRadio);
        lettuceCheckbox = findViewById(R.id.lettuceCheckbox);
        tomatoCheckbox = findViewById(R.id.tomatoCheckbox);
        onionsCheckbox = findViewById(R.id.onionsCheckbox);
        avacadoCheckbox = findViewById(R.id.avacadoCheckbox);

        // Initialize buttons
        addToCartButton = findViewById(R.id.addToCartButton);
        comboButton = findViewById(R.id.clearOrderButton);
        checkOutButton = findViewById(R.id.checkOutButton);
        clearOrderButton = findViewById(R.id.clearOrderButton);
        cancelOrderButton = findViewById(R.id.cancelOrderButton);

        setupSpinners();
        setupListeners();
        resetSelections();
    }

    /**
     * Sets up the spinners for bread selection and quantity.
     * Populates the bread spinner with available bread types and
     * the quantity spinner with numbers 1-10.
     */
    private void setupSpinners() {
        Bread[] burgerBreads = {Bread.BRIOCHE, Bread.WHEAT_BREAD, Bread.PRETZEL};

        ArrayAdapter<Bread> breadAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, burgerBreads);
        breadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        breadSpinner.setAdapter(breadAdapter);

        List<Integer> quantityOptions = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            quantityOptions.add(i);
        }
        ArrayAdapter<Integer> quantityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, quantityOptions);
        quantityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantitySpinner.setAdapter(quantityAdapter);
    }

    /**
     * Sets up event listeners for all UI components.
     * Handles user interactions with spinners, radio buttons, checkboxes, and buttons.
     */
    private void setupListeners() {
        breadSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateBurger();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        pattyTypeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> updateBurger());

        View.OnClickListener addonListener = v -> updateBurger();
        lettuceCheckbox.setOnClickListener(addonListener);
        tomatoCheckbox.setOnClickListener(addonListener);
        onionsCheckbox.setOnClickListener(addonListener);
        avacadoCheckbox.setOnClickListener(addonListener);

        quantitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateBurger();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        addToCartButton.setOnClickListener(v -> handleAddToCart());
        comboButton.setOnClickListener(v -> handleOrderAsCombo());
        checkOutButton.setOnClickListener(v -> handleCheckOut());
        clearOrderButton.setOnClickListener(v -> handleClearOrder());
        cancelOrderButton.setOnClickListener(v -> handleCancelOrder());
    }

    /**
     * Updates the burger object based on current UI selections.
     * Recalculates the price and updates the price label.
     */
    private void updateBurger() {
        try {
            Bread selectedBread = (Bread) breadSpinner.getSelectedItem();
            boolean isDoublePatty = doublePattyRadio.isChecked();

            if (selectedBread == null) {
                priceLabel.setText(getString(R.string.price_default));
                return;
            }

            List<AddOns> addOns = new ArrayList<>();
            if (lettuceCheckbox.isChecked()) addOns.add(AddOns.LETTUCE);
            if (tomatoCheckbox.isChecked()) addOns.add(AddOns.TOMATOES);
            if (onionsCheckbox.isChecked()) addOns.add(AddOns.ONIONS);
            if (avacadoCheckbox.isChecked()) addOns.add(AddOns.AVOCADO);

            burger = new Burger(selectedBread, addOns, isDoublePatty);
            int quantity = (Integer) quantitySpinner.getSelectedItem();
            burger.setQuantity(quantity);

            updatePrice();

        } catch (Exception e) {
            priceLabel.setText(getString(R.string.price_default));
            showToast(getString(R.string.burger_added_error) + e.getMessage());
        }
    }

    /**
     * Updates the price label with the current burger's price.
     * Formats the price to display with 2 decimal places.
     */
    private void updatePrice() {
        if (burger != null) {
            priceLabel.setText(String.format("$%.2f", burger.price()));
        } else {
            priceLabel.setText(getString(R.string.price_default));
        }
    }

    /**
     * Handles the "Add to Cart" button click.
     * Validates the order and adds the burger to the shared order.
     */
    private void handleAddToCart() {
        try {
            if (burger == null || burger.getBread() == null) {
                Toast.makeText(this, getString(R.string.incomplete_selection), Toast.LENGTH_SHORT).show();
                return;
            }

            int quantity = (Integer) quantitySpinner.getSelectedItem();
            if (quantity <= 0) {
                Toast.makeText(this, getString(R.string.select_quantity), Toast.LENGTH_SHORT).show();
                return;
            }

            burger.setQuantity(quantity);
            SharedOrder.getInstance().add(burger);

            Toast.makeText(this, getString(R.string.Order_Added), Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.burger_added_error) + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Handles the "Order as Combo" button click.
     * Navigates to the combo order activity if a valid burger is selected.
     */
    private void handleOrderAsCombo() {
        try {
            if (burger == null) {
                showToast(getString(R.string.select_burger));
                return;
            }
            Intent intent = new Intent(this, ComboOrderActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            showToast(getString(R.string.combo_selection_error) + e.getMessage());
        }
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
        Toast.makeText(this, getString(R.string.clear_order), Toast.LENGTH_SHORT).show();
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
     * Resets all UI components to their default values.
     * Creates a new default burger with initial settings.
     */
    private void resetSelections() {
        breadSpinner.setSelection(0);
        quantitySpinner.setSelection(0);
        singlePattyRadio.setChecked(true);
        lettuceCheckbox.setChecked(false);
        tomatoCheckbox.setChecked(false);
        onionsCheckbox.setChecked(false);
        avacadoCheckbox.setChecked(false);

        Bread selectedBread = (Bread) breadSpinner.getSelectedItem();
        boolean isDoublePatty = doublePattyRadio.isChecked();

        List<AddOns> addOns = new ArrayList<>();

        burger = new Burger(selectedBread, addOns, isDoublePatty);
        burger.setQuantity(1);

        updatePrice();
    }

    /**
     * Displays a toast message to the user.
     * @param message The message to display
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
