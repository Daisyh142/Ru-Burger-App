package com.example.project5;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.Project5.model.MenuItem;
import com.Project5.model.Order;
import com.Project5.model.StoreOrders;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Activity for viewing and managing all store orders.
 * Displays a list of orders with their numbers and totals, and allows viewing detailed information user.
 *
 * @author Daisy Hernandez
 */
public class ViewAllOrdersActivity extends AppCompatActivity {

    private ListView orderListView;
    private TextView orderDetailsArea;
    private Button returnButton;
    private final DecimalFormat df = new DecimalFormat("0.00");

    /**
     * Initializes the activity and sets up the UI components.
     * Configures the order list view, details area, and button click listeners.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                            this Bundle contains the data it most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_orders);

        orderListView = findViewById(R.id.orderListView);
        orderDetailsArea = findViewById(R.id.orderDetailsArea);
        returnButton = findViewById(R.id.returnButton);

        updateOrderList();

        orderListView.setOnItemClickListener((parent, view, position, id) -> displayOrderDetails(position));

        returnButton.setOnClickListener(v -> returnToMain());
    }

    /**
     * Updates the order list view with summaries of all orders.
     * Each summary includes the order number and total amount.
     */
    private void updateOrderList() {
        ArrayList<String> summaries = new ArrayList<>();
        for (Order order : StoreOrders.getInstance().getOrders()) {
            String summary = getString(R.string.order_number_format, order.getOrderNumber()) +
                    " - " + getString(R.string.total_format, order.getTotal());
            summaries.add(summary);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, summaries);
        orderListView.setAdapter(adapter);
    }

    /**
     * Displays detailed information for the selected order.
     * Shows order number, all items, subtotal, tax, and total amount.
     *
     * @param index The position of the selected order in the list
     */
    private void displayOrderDetails(int index) {
        if (index < 0 || index >= StoreOrders.getInstance().getOrders().size()) {
            orderDetailsArea.setText("");
            return;
        }

        Order selectedOrder = StoreOrders.getInstance().getOrders().get(index);
        StringBuilder details = new StringBuilder();
        details.append(getString(R.string.order_number_format, selectedOrder.getOrderNumber())).append("\n\n");
        details.append(getString(R.string.order_items)).append("\n");

        for (MenuItem item : selectedOrder.getItems()) {
            details.append("- ").append(item.toString()).append("\n");
        }

        details.append("\n")
                .append(getString(R.string.subtotal_label)).append(" ")
                .append(String.format("$%.2f", selectedOrder.getSubtotal())).append("\n");
        details.append(getString(R.string.tax_format, selectedOrder.getTax())).append("\n");
        details.append(getString(R.string.total_format, selectedOrder.getTotal()));

        orderDetailsArea.setText(details.toString());
    }

    /**
     * Returns to the main activity.
     * Clears the activity stack to prevent going back to this screen.
     */
    private void returnToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
