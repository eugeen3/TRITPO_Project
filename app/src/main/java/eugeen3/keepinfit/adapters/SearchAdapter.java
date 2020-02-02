package eugeen3.keepinfit.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import eugeen3.keepinfit.R;
import eugeen3.keepinfit.entities.FoodItem;

import static java.lang.String.valueOf;

public class SearchAdapter extends MealAdapter {

    public SearchAdapter(Context context, List<FoodItem> foodItems) {
        super(context, foodItems);
    }

    @Override
    public void onBindViewHolder(MealAdapter.ViewHolder holder, int position) {
        FoodItem foodItem = foodItems.get(position);
        holder.nameView.setText(foodItem.getName());
        holder.massView.setText("в 100 гр.");
        holder.proteinsView.setText(valueOf(foodItem.getProteins()));
        holder.carbohydratesView.setText(valueOf(foodItem.getCarbohydrates()));
        holder.fatsView.setText(valueOf(foodItem.getFats()));
        holder.kcalsView.setText(valueOf(foodItem.getKcals()));
    }
}
