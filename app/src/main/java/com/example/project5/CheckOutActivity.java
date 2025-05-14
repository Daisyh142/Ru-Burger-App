
package com.example.project5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.Project5.model.MenuItem;
import com.Project5.model.Order;
import com.Project5.model.SharedOrder;
import com.Project5.model.StoreOrders;

import java.util.ArrayList;

/**
 * Activity for handling the checkout process in the application.
 * Manages the final review of orders, calculation of totals, and order placement.
 * Provides functionality to remove items, clear cart, and process payment.
 *
 * @author Daisy Hernandez
 */

public class CheckOutActivity extends AppCompatActivity {

    private ListView orderListView;
    private Button removeButton, clearCartButton, cancelOrderButton, placeOrderButton, addMoreItemsButton;
    private TextView subtotalLabel, taxLabel, totalLabel, orderNumberLabel;

    // Order management
    private Order currentOrder;
    private static final double TAX_RATE = 0.06625; // NJ 6.625%

    /**
     * Initializes the activity and sets up the UI components.
     * Configures the order list view and button click listeners.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        orderListView = findViewById(R.id.orderListView);
        orderListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        removeButton = findViewById(R.id.removeButton);
        clearCartButton = findViewById(R.id.clearCartButton);
        cancelOrderButton = findViewById(R.id.cancelOrderButton);
        placeOrderButton = findViewById(R.id.placeOrderButton);
        addMoreItemsButton = findViewById(R.id.addMoreItemsButton);

        // Initialize labels
        subtotalLabel = findViewById(R.id.subtotalLabel);
        taxLabel = findViewById(R.id.taxLabel);
        totalLabel = findViewById(R.id.totalLabel);
        orderNumberLabel = findViewById(R.id.orderNumberLabel);

        // Set up order and refresh view
        currentOrder = SharedOrder.getInstance();
        refreshView();

        // Set up button click listeners
        removeButton.setOnClickListener(v -> handleRemoveItem());
        clearCartButton.setOnClickListener(v -> handleClearCart());
        cancelOrderButton.setOnClickListener(v -> handleCancelOrder());
        placeOrderButton.setOnClickListener(v -> handlePlaceOrder());
        addMoreItemsButton.setOnClickListener(v -> finish());
    }

    /**
     * Refreshes the view with current order information.
     * Updates the list of items, calculates totals, and enables/disables buttons based on order state.
     */
    private void refreshView() {
        ArrayList<String> itemDescriptions = new ArrayList<>();
        for (MenuItem item : currentOrder.getItems()) {
            itemDescriptions.add(item.toString());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, itemDescriptions);
        orderListView.setAdapter(adapter);

        double subtotal = currentOrder.getSubtotal();
        double tax = subtotal * TAX_RATE;
        double total = subtotal + tax;

        subtotalLabel.setText(getString(R.string.subtotal_format, subtotal));
        taxLabel.setText(getString(R.string.tax_format, tax));
        totalLabel.setText(getString(R.string.total_format, total));

        orderNumberLabel.setText(getString(R.string.order_number_format, currentOrder.getOrderNumber()));

        boolean hasItems = !currentOrder.getItems().isEmpty();
        removeButton.setEnabled(hasItems);
        clearCartButton.setEnabled(hasItems);
        placeOrderButton.setEnabled(hasItems);
    }

    /**
     * Handles the removal of a selected item from the order.
     * Shows appropriate messages based on selection state.
     */
    private void handleRemoveItem() {
        int selectedIndex = orderListView.getCheckedItemPosition();
        if (selectedIndex != ListView.INVALID_POSITION) {
            currentOrder.getItems().remove(selectedIndex);
            refreshView();
            showToast(getString(R.string.item_removed));
        } else {
            showAlert(getString(R.string.no_selection_title), getString(R.string.no_selection_message));
        }
    }

    /**
     * Handles clearing the entire cart.
     * Shows a confirmation dialog before clearing the order.
     */
    private void handleClearCart() {
        if (currentOrder.getItems().isEmpty()) {
            showAlert(getString(R.string.empty_cart_title), getString(R.string.empty_cart_message));
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.clear_order))
                .setMessage(getString(R.string.clear_cart_message))
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    currentOrder.getItems().clear();
                    refreshView();
                    showToast(getString(R.string.cart_cleared));
                })
                .setNegativeButton(getString(R.string.no), null)
                .show();
    }

    /**
     * Handles canceling the current order.
     * Shows a confirmation dialog and resets the shared order.
     */
    private void handleCancelOrder() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.cancel_order_title))
                .setMessage(getString(R.string.cancel_order_message))
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    SharedOrder.resetOrder();
                    currentOrder = SharedOrder.getInstance();
                    showToast(getString(R.string.order_canceled));
                    finish();
                })
                .setNegativeButton(getString(R.string.no), null)
                .show();
    }

    /**
     * Handles placing the final order.
     * Validates the order, adds it to store orders, and shows a confirmation dialog.
     */
    private void handlePlaceOrder() {
        if (currentOrder.getItems().isEmpty()) {
            showAlert(getString(R.string.empty_order_title), getString(R.string.empty_order_message));
            return;
        }

        StoreOrders.getInstance().addOrder(currentOrder);

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.order_placed_title))
                .setMessage(getString(R.string.order_placed_message, currentOrder.getOrderNumber()))
                .setPositiveButton(getString(R.string.ok), (dialog, which) -> {
                    SharedOrder.resetOrder();
                    currentOrder = SharedOrder.getInstance();
                    refreshView();

                    Intent intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .show();
    }

    /**
     * Shows an alert dialog with the specified title and message.
     * 
     * @param title The title of the alert dialog
     * @param message The message to display in the alert dialog
     */
    private void showAlert(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(getString(R.string.ok), null)
                .show();
    }

    /**
     * Shows a toast message to the user.
     * 
     * @param message The message to display in the toast
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
