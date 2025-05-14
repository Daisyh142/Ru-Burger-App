package com.example.project5;

import com.Project5.model.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * This class is the activity for the sandwich order
 * It allows the user to order a sandwich
 * The user can select a bread, protein, and add ons
 *
 * @author Daisy Hernandez
 */

public class SandwichOrderActivity extends AppCompatActivity {
    private Spinner breadSpinner, quantitySpinner, proteinSpinner;
    private CheckBox lettuceCheckbox, tomatoCheckbox, onionsCheckbox, avocadoCheckbox, cheeseCheckbox;
    private Button addToCartButton;
    private Button comboButton;
    private Button checkOutButton;
    private Button clearOrderButton;
    private TextView priceLabel;
    private Sandwich sandwich;
    private Button cancelOrderButton;

    /**
     * Initializes the activity and sets up the UI components.
     * Configures the RecyclerView for bread, protein, and quantity options.
     * Sets up event listeners for all interactive elements.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sandwich_order);

        breadSpinner = findViewById(R.id.breadSpinner);
        quantitySpinner = findViewById(R.id.quantitySpinner);
        lettuceCheckbox = findViewById(R.id.lettuceCheckbox);
        tomatoCheckbox = findViewById(R.id.tomatoCheckbox);
        onionsCheckbox = findViewById(R.id.onionsCheckbox);
        avocadoCheckbox = findViewById(R.id.avocadoCheckbox);
        cheeseCheckbox = findViewById(R.id.cheeseCheckbox);
        addToCartButton = findViewById(R.id.addToCartButton);
        comboButton = findViewById(R.id.comboButton);
        checkOutButton = findViewById(R.id.checkOutButton);
        priceLabel = findViewById(R.id.priceLabel);
        proteinSpinner = findViewById(R.id.proteinSpinner);
        clearOrderButton = findViewById(R.id.clearOrderButton);
        cancelOrderButton = findViewById(R.id.cancelOrderButton);

        setupSpinners();
        setupCheckboxes();
        setupQuantitySpinner();
        sandwich = new Sandwich(null,null, new ArrayList<>());
        updatePriceDisplay();

        checkOutButton.setOnClickListener(v -> handleCheckOut());
        addToCartButton.setOnClickListener(v -> handleAddToCart());
        clearOrderButton.setOnClickListener(v -> handleClearOrder());
        cancelOrderButton.setOnClickListener(v -> handleCancelOrder());



        /**
         * Sets up the combo button to handle order as combo
         */
        comboButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOrderAsCombo();
            }
        });
    }
    /**
     * This method handles the cancel order button
     * User can cancel the order
     */
    private void handleCancelOrder() {
        try {
            Toast.makeText(this, getString(R.string.cancel_order), Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.Error) + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method handles the clear order button
     * User can clear the order
     */
    private void handleClearOrder() {
        try {
            breadSpinner.setSelection(0);
            proteinSpinner.setSelection(0);
            quantitySpinner.setSelection(0);

            lettuceCheckbox.setChecked(false);
            tomatoCheckbox.setChecked(false);
            onionsCheckbox.setChecked(false);
            avocadoCheckbox.setChecked(false);
            cheeseCheckbox.setChecked(false);

            Bread selectedBread = (Bread) breadSpinner.getSelectedItem();
            Protein selectedProtein = (Protein) proteinSpinner.getSelectedItem();

            ArrayList<AddOns> addOns = new ArrayList<>();

            sandwich = new Sandwich(selectedBread, selectedProtein, addOns);
            sandwich.setQuantity(1);

            updatePriceDisplay();

            Toast.makeText(this, getString(R.string.clear_order), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.Error) + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }




    /**
     * This method handles the add to cart button
     * User can add the sandwich to the order
     */
    private void handleAddToCart() {
        try {
            if (sandwich == null || sandwich.getBread() == null || sandwich.getProtein() == null) {
                Toast.makeText(this, getString(R.string.incomplete_selection), Toast.LENGTH_SHORT).show();
                return;
            }

            int quantity = (Integer) quantitySpinner.getSelectedItem();
            if (quantity <= 0) {
                Toast.makeText(this, getString(R.string.select_quantity), Toast.LENGTH_SHORT).show();
                return;
            }

            sandwich.setQuantity(quantity);
            SharedOrder.getInstance().add(sandwich);

            Toast.makeText(this, getString(R.string.Order_Added), Toast.LENGTH_SHORT).show();
            finish();

        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.burger_added_error) + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * This method handles the order as combo button
     * It starts the ComboOrderActivity
     */
    private void handleOrderAsCombo() {
        Intent intent = new Intent(this, ComboOrderActivity.class);
        startActivity(intent);
    }

    /**
     * This method sets up the check out button
     * It starts the CheckOutActivity
     */
    private void handleCheckOut() {
        Intent intent = new Intent(this, CheckOutActivity.class);
        startActivity(intent);
    }

    /**
     * This updates the price on the screen
     */
    private void updatePriceDisplay() {
        priceLabel.setText(String.format("$%.2f", sandwich.price()));
    }

    /**
     * This updates the price the of the sandwich based on the selected options
     */
    private void updateSandwich() {
        if (breadSpinner.getSelectedItem() == null || proteinSpinner.getSelectedItem() == null) {
            sandwich = null;
            priceLabel.setText(getString(R.string.price_default));
            return;
        }

        ArrayList<AddOns> addOns = new ArrayList<>();
        if (lettuceCheckbox.isChecked()) addOns.add(AddOns.LETTUCE);
        if (tomatoCheckbox.isChecked()) addOns.add(AddOns.TOMATOES);
        if (onionsCheckbox.isChecked()) addOns.add(AddOns.ONIONS);
        if (avocadoCheckbox.isChecked()) addOns.add(AddOns.AVOCADO);
        if (cheeseCheckbox.isChecked()) addOns.add(AddOns.CHEESE);

        sandwich = new Sandwich((Bread) breadSpinner.getSelectedItem(), (Protein) proteinSpinner.getSelectedItem(), addOns);

        int quantity = (Integer) quantitySpinner.getSelectedItem();
        sandwich.setQuantity(quantity);
        updatePriceDisplay();
    }

    /**
     * This creates the checkboxes for the add ons and updates the price
     */
    private void setupCheckboxes() {
        View.OnClickListener checkBoxListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSandwich();
            }
        };
        lettuceCheckbox.setOnClickListener(checkBoxListener);
        tomatoCheckbox.setOnClickListener(checkBoxListener);
        onionsCheckbox.setOnClickListener(checkBoxListener);
        avocadoCheckbox.setOnClickListener(checkBoxListener);
        cheeseCheckbox.setOnClickListener(checkBoxListener);
    }

    /**
     * This method sets up the quantity spinner and its listener
     * User can select a quantity from the spinner
     */
    private void setupQuantitySpinner() {
        ArrayAdapter<Integer> quantityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        quantityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantitySpinner.setAdapter(quantityAdapter);
        quantitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateSandwich();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(SandwichOrderActivity.this, getString(R.string.incomplete_selection), Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * This method sets up the bread spinner and its listener and the protein spinner and its listener
     * User can select a bread option from the spinner
     * User can select a protein option from the spinner
     */
    private void setupSpinners(){
        ArrayAdapter<Bread> breadAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Bread.values());
        breadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        breadSpinner.setAdapter(breadAdapter);
        breadSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateSandwich();
                updatePriceDisplay();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(SandwichOrderActivity.this, getString(R.string.incomplete_selection), Toast.LENGTH_SHORT).show();
            }
        });

        ArrayAdapter<Protein> proteinAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Protein.values());
        proteinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        proteinSpinner.setAdapter(proteinAdapter);
        proteinSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateSandwich();
                updatePriceDisplay();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(SandwichOrderActivity.this, getString(R.string.incomplete_selection), Toast.LENGTH_SHORT).show();
            }
        });
    }
}