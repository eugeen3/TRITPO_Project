package eugeen3.keepinfit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import eugeen3.keepinfit.R;
import eugeen3.keepinfit.entities.FoodItem;
import eugeen3.keepinfit.entities.Meal;

import static java.lang.String.valueOf;

public class MealListAdapter extends RecyclerView.Adapter<MealListAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Meal> mealList;

    public MealListAdapter(Context context, List<Meal> mealList) {
        this.mealList = mealList;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public MealListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.food_items_list, parent, false);
        return new MealListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MealListAdapter.ViewHolder holder, int position) {
        Meal meal = mealList.get(position);
        holder.nameView.setText(meal.getName());
        holder.countView.setText("Продуктов: " + meal.getAmountOfProducts());
        holder.proteinsView.setText(valueOf(meal.getProteins()));
        holder.carbohydratesView.setText(valueOf(meal.getCarbohydrates()));
        holder.fatsView.setText(valueOf(meal.getFats()));
        holder.kcalsView.setText(valueOf(meal.getKcals()));
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView nameView, countView, proteinsView, carbohydratesView, fatsView, kcalsView;
        ViewHolder(View view){
            super(view);
            nameView = view.findViewById(R.id.foodName);
            countView = view.findViewById(R.id.foodMass);
            proteinsView = view.findViewById(R.id.foodProtValue);
            carbohydratesView = view.findViewById(R.id.foodCarbValue);
            fatsView = view.findViewById(R.id.foodFatValue);
            kcalsView = view.findViewById(R.id.foodKcalValue);
        }
    }
}
