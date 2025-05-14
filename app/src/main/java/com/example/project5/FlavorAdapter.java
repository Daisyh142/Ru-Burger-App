package com.example.project5;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.Project5.model.Flavor;
import java.util.List;

/**
 * Custom RecyclerView adapter for displaying beverage flavors.
 * Handles the presentation of flavor options in a scrollable list,
 * with each item showing a flavor name and corresponding image.
 * Supports click interactions through a callback interface.
 *
 * @author Daisy Hernandez
 */

public class FlavorAdapter extends RecyclerView.Adapter<FlavorAdapter.FlavorViewHolder> {

    /**
     * Interface for handling flavor selection events.
     * Implemented by the activity to receive click callbacks.
     */
    public interface OnFlavorClickListener {
        /**
         * Called when a flavor item is clicked.
         * @param flavor The selected Flavor enum value
         */
        void onFlavorClick(Flavor flavor);
    }

    private final List<Flavor> flavors;
    private final OnFlavorClickListener listener;

    /**
     * Constructs a new FlavorAdapter.
     *
     * @param flavors List of available Flavor options to display
     * @param listener Callback interface for handling flavor selections
     */
    public FlavorAdapter(List<Flavor> flavors, OnFlavorClickListener listener) {
        this.flavors = flavors;
        this.listener = listener;
    }

    /**
     * Creates new views for flavor items.
     * Inflates the flavor_item layout for each list item.
     *
     * @param parent The ViewGroup into which the new View will be added
     * @param viewType The view type of the new View (not used in this implementation)
     * @return A new FlavorViewHolder that holds a View of the given view type
     */
    @NonNull
    @Override
    public FlavorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.flavor_item, parent, false);
        return new FlavorViewHolder(view);
    }

    /**
     * Binds data to an existing ViewHolder.
     * Sets the flavor name, corresponding image, and click listener.
     *
     * @param holder The ViewHolder to update
     * @param position Position of the item in the data set
     */
    @Override
    public void onBindViewHolder(@NonNull FlavorViewHolder holder, int position) {
        Flavor flavor = flavors.get(position);
        holder.flavorText.setText(flavor.toString());

        int imageResId = R.drawable.water; // default
        switch (flavor) {
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
        holder.flavorImage.setImageResource(imageResId);

        holder.itemView.setOnClickListener(v -> listener.onFlavorClick(flavor));
    }

    /**
     * Returns the total number of items in the data set.
     *
     * @return The total number of available flavors
     */
    @Override
    public int getItemCount() {
        return flavors.size();
    }

    /**
     * ViewHolder class for flavor items.
     * Caches references to the views within the item layout
     * to avoid repeated findViewById calls.
     */
    static class FlavorViewHolder extends RecyclerView.ViewHolder {
        TextView flavorText;
        ImageView flavorImage;

        /**
         * Constructs a new FlavorViewHolder.
         * Finds and stores references to the views within the item layout.
         *
         * @param itemView The View that contains the UI for a single flavor item
         */
        public FlavorViewHolder(@NonNull View itemView) {
            super(itemView);
            flavorText = itemView.findViewById(R.id.flavorText);
            flavorImage = itemView.findViewById(R.id.flavorImage);
        }
    }
}
