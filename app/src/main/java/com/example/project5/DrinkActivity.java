package com.example.project5;

import com.Project5.model.*;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Arrays;
import java.util.List;

/**
 * Activity for handling beverage orders in the application.
 * Allows users to customize their drink by selecting:
 * - Flavor (using a RecyclerView with custom adapter)
 * - Size (Small, Medium, Large via radio buttons)
 * - Quantity (1-10 via spinner)
 * Provides real-time price updates and image previews.
 *
 * @author Daisy Hernandez
 */

public class DrinkActivity extends AppCompatActivity {
    private Button addToCartButton, comboButton, checkOutButton, clearOrderButton, cancelOrderButton;
    private RadioGroup sizeRadioGroup;
    private TextView priceLabel;
    private ImageView beverageImage;
    private RecyclerView flavorRecyclerView;
    private Spinner quantitySpinner;

    private List<Flavor> flavorList;
    private Flavor selectedFlavor;
    private Beverage beverage;
    private FlavorAdapter flavorAdapter;

    /**
     * Initializes the activity and sets up the UI components.
     * Configures the RecyclerView for flavors, quantity spinner,
     * and sets up event listeners for all interactive elements.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        addToCartButton = findViewById(R.id.addToCartButton);
        comboButton = findViewById(R.id.comboButton);
        checkOutButton = findViewById(R.id.checkOutButton);
        clearOrderButton = findViewById(R.id.clearOrderButton);
        cancelOrderButton = findViewById(R.id.cancelOrderButton);

        sizeRadioGroup = findViewById(R.id.sizeRadioGroup);
        priceLabel = findViewById(R.id.priceLabel);
        beverageImage = findViewById(R.id.beverageImage);
        flavorRecyclerView = findViewById(R.id.flavorRecyclerView);
        quantitySpinner = findViewById(R.id.quantitySpinner);

        setupFlavorRecyclerView();
        setupQuantitySpinner();

        addToCartButton.setOnClickListener(v -> handleAddToOrder());
        comboButton.setOnClickListener(v -> startActivity(new Intent(this, ComboOrderActivity.class)));
        checkOutButton.setOnClickListener(v -> startActivity(new Intent(this, CheckOutActivity.class)));
        clearOrderButton.setOnClickListener(v -> clearSelections());
        cancelOrderButton.setOnClickListener(v -> finish());

        sizeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            updatePriceDisplay();
        });
    }

    /**
     * Sets up the RecyclerView for displaying beverage flavors.
     * Configures with a vertical LinearLayoutManager and custom FlavorAdapter.
     * Initializes with the first flavor selected and updates the UI accordingly.
     */
    private void setupFlavorRecyclerView() {
        flavorList = Arrays.asList(Flavor.values());
        flavorAdapter = new FlavorAdapter(flavorList, flavor -> {
            selectedFlavor = flavor;
            updateBeverageImage();
            updatePriceDisplay();
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        flavorRecyclerView.setLayoutManager(layoutManager);
        flavorRecyclerView.setAdapter(flavorAdapter);
        flavorRecyclerView.setHasFixedSize(true);

        selectedFlavor = flavorList.get(0);
        updateBeverageImage();
        updatePriceDisplay();
    }

    /**
     * Configures the quantity spinner with values from 1 to 10.
     * Sets up the adapter and selection listener to update price on change.
     */
    private void setupQuantitySpinner() {
        Integer[] quantities = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, quantities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantitySpinner.setAdapter(adapter);
        quantitySpinner.setSelection(0);
        quantitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updatePriceDisplay();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    /**
     * Determines the currently selected size from the radio group.
     * Maps radio button IDs to Size enum values.
     *
     * @return The selected Size enum value (SMALL, MEDIUM, or LARGE)
     */
    private Size getSelectedSize() {
        int selectedId = sizeRadioGroup.getCheckedRadioButtonId();
        if (selectedId == R.id.radioSmall) return Size.SMALL;
        if (selectedId == R.id.radioMedium) return Size.MEDIUM;
        if (selectedId == R.id.radioLarge) return Size.LARGE;
        return Size.SMALL;
    }

    /**
     * Updates the price display based on current selections.
     * Calculates price using:
     * - Selected size
     * - Selected quantity
     * - Current flavor
     * Creates a new Beverage instance to compute the total price.
     */
    private void updatePriceDisplay() {
        if (quantitySpinner.getSelectedItem() != null) {
            Size size = getSelectedSize();
            int quantity = (Integer) quantitySpinner.getSelectedItem();

            if (selectedFlavor != null) {
                beverage = new Beverage(size, selectedFlavor);
            } else {
                beverage = new Beverage(size, flavorList.get(0));
            }

            beverage.setQuantity(quantity);
            priceLabel.setText(String.format("$%.2f", beverage.price()));
        }
    }

    /**
     * Updates the beverage image based on the selected flavor.
     * Maps each flavor to its corresponding image resource.
     * Defaults to water image if no flavor is selected.
     */
    private void updateBeverageImage() {
        if (selectedFlavor == null) return;

        int imageResId = R.drawable.water;
        switch (selectedFlavor) {
            case COLA: imageResId = R.drawable.cola; break;
            case LEMONADE: imageResId = R.drawable.lemonade; break;
            case ORANGE_JUICE: imageResId = R.drawable.orange_juice; break;
            case ICED_TEA: imageResId = R.drawable.strawberry_iced_tea; break;
            case WATER: imageResId = R.drawable.water; break;
            case ICED_COFFEE: imageResId = R.drawable.iced_coffee; break;
            case PEPSI: imageResId = R.drawable.cola; break;
            case SPRITE: imageResId = R.drawable.sprite; break;
            case APPLE_JUICE: imageResId = R.drawable.apple_juice; break;
            case GATORADE: imageResId = R.drawable.gatorade; break;
        }
        beverageImage.setImageResource(imageResId);
    }

    /**
     * Handles adding the current beverage to the order.
     * Validates selections, creates a new Beverage instance,
     * adds it to the shared order, and closes the activity.
     * Shows appropriate toast messages for feedback.
     */
    private void handleAddToOrder() {
        if (selectedFlavor == null) {
            Toast.makeText(this, getString(R.string.select_flavor), Toast.LENGTH_SHORT).show();
            return;
        }

        Size size = getSelectedSize();
        int quantity = (Integer) quantitySpinner.getSelectedItem();
        beverage = new Beverage(size, selectedFlavor);
        beverage.setQuantity(quantity);

        SharedOrder.getInstance().add(beverage);
        Toast.makeText(this, getString(R.string.Order_Added), Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * Resets all selections to their default values:
     * - Scrolls flavor list to top
     * - Sets quantity to 1
     * - Selects small size
     * - Resets to first flavor
     * - Updates image and price
     * Shows a confirmation toast message.
     */
    private void clearSelections() {
        flavorRecyclerView.scrollToPosition(0);
        quantitySpinner.setSelection(0);
        sizeRadioGroup.check(R.id.radioSmall);
        selectedFlavor = flavorList.get(0);
        beverageImage.setImageResource(R.drawable.water);
        updatePriceDisplay();
        Toast.makeText(this, getString(R.string.clear_order), Toast.LENGTH_SHORT).show();
    }
}
