package com.example.project5;

import com.Project5.model.*;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

/**
 * Main activity for the RUBurger application.
 * Serves as the entry point and provides navigation to different ordering options.
 * Users can choose to order burgers, combos, or view store orders.
 *
 * @author Daisy Hernandez
 */

public class MainActivity extends AppCompatActivity {

    private Button orderBurgerButton;
    private Button orderSandwichButton;
    private Button orderSideButton;
    private Button orderDrinkButton;
    private Button orderComboButton;
    private Button checkOutButton;
    private Button viewAllOrdersButton;
    private TextView priceLabel;
    private Order order;

    /**
     * Initializes the activity and sets up the UI components.
     * Creates buttons for different ordering options and sets up their click listeners.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                          this Bundle contains the data it most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, getString(R.string.welcome_message), Toast.LENGTH_SHORT).show();

        orderBurgerButton = findViewById(R.id.orderBurger);
        orderDrinkButton = findViewById(R.id.orderDrink);
        orderSideButton = findViewById(R.id.orderSide);
        orderSandwichButton = findViewById(R.id.orderSandwich);
        orderComboButton = findViewById(R.id.orderCombo);
        checkOutButton = findViewById(R.id.checkOut);
        priceLabel = findViewById(R.id.priceLabel);
        viewAllOrdersButton = findViewById(R.id.viewAllOrders);

        order = SharedOrder.getInstance();
        updatePriceDisplay();

        orderBurgerButton.setOnClickListener(v -> openActivity(BurgerOrderActivity.class));
        orderSandwichButton.setOnClickListener(v -> openActivity(SandwichOrderActivity.class));
        orderSideButton.setOnClickListener(v -> openActivity(SideOrderActivity.class));
        orderDrinkButton.setOnClickListener(v -> openActivity(DrinkActivity.class));
        orderComboButton.setOnClickListener(v -> openActivity(ComboOrderActivity.class));
        checkOutButton.setOnClickListener(v -> openActivity(CheckOutActivity.class));
        viewAllOrdersButton.setOnClickListener(v -> openActivity(ViewAllOrdersActivity.class));
    }

   /**
    * Updates the price display with the current order total
    * @return the total price of the order
    */
   private void updatePriceDisplay() {
       if (order == null) {
           order = SharedOrder.getInstance();
       }
       if (order == null) {
           priceLabel.setText(getString(R.string.subtotal_default));
           return;
       }

       double totalPrice = order.getSubtotal();
       priceLabel.setText(getString(R.string.subtotal_format, totalPrice));
   }
 
   /**
    * Opens the activity for the given class.
    * @param cls the class of the activity to open
    */
    private void openActivity(Class<?> cls) {
        Intent intent = new Intent(MainActivity.this, cls);
        startActivity(intent);
    }

    /**
     * Updates the price display when the activity starts.
     */
    @Override
    protected void onStart() {
        super.onStart();
        updatePriceDisplay();
    }

    /**
     * Updates the price display when the activity resumes.
     */
    @Override
    protected void onResume() {
        super.onResume();
        order = SharedOrder.getInstance();
        updatePriceDisplay();
    }
}
