package eugeen3.keepinfit.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import eugeen3.keepinfit.R;
import eugeen3.keepinfit.entities.Meal;
import eugeen3.keepinfit.ui.MealActivity;

import static java.lang.String.valueOf;

public class MealListAdapter extends RecyclerView.Adapter<MealListAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Meal> mealList;
    private Context cont;

    public MealListAdapter(Context context, List<Meal> mealList) {
        this.mealList = mealList;
        this.inflater = LayoutInflater.from(context);
        this.cont = context;
    }
    @Override
    public MealListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.meal_list, parent, false);
        return new MealListAdapter.ViewHolder(view, cont);
    }

    @Override
    public void onBindViewHolder(MealListAdapter.ViewHolder holder, int position) {
        Meal meal = mealList.get(position);
        String count = ("Продуктов: " + meal.getAmountOfProducts());
        holder.nameView.setText(meal.getName());
        holder.countView.setText(count);
        holder.proteinsView.setText(valueOf(meal.getProteins()));
        holder.carbohydratesView.setText(valueOf(meal.getCarbohydrates()));
        holder.fatsView.setText(valueOf(meal.getFats()));
        holder.kcalsView.setText(valueOf(meal.getKcals()));
    }

    @Override
    public int getItemCount() {
        if (mealList == null) {
            return 0;
        }
        return mealList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView nameView, countView, proteinsView, carbohydratesView, fatsView, kcalsView;
        final CardView cv;
        ViewHolder(View view, final Context context){
            super(view);
            cv = view.findViewById(R.id.mealView);
            nameView = view.findViewById(R.id.mealName);
            countView = view.findViewById(R.id.mealAmount);
            proteinsView = view.findViewById(R.id.mealProtValue);
            carbohydratesView = view.findViewById(R.id.mealCarbValue);
            fatsView = view.findViewById(R.id.mealFatValue);
            kcalsView = view.findViewById(R.id.mealKcalValue);
            //cv.setOnClickListener();
        }
    }
}
