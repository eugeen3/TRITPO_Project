package eugeen3.keepinfit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import eugeen3.keepinfit.R;
import eugeen3.keepinfit.entities.Meal;
import eugeen3.keepinfit.ui.OnItemClickListener;

import static java.lang.String.valueOf;

public class MealListAdapter extends RecyclerView.Adapter<MealListAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Meal> mealList;
    //private Context cont;
    private OnItemClickListener clickListener;

    public MealListAdapter(Context context, List<Meal> mealList) {
        this.mealList = mealList;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public MealListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.meal_list, parent, false);
        return new MealListAdapter.ViewHolder(view);
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
        return mealList == null ? 0 : mealList.size();
    }

    public void setClickListener(OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView nameView, countView, proteinsView, carbohydratesView, fatsView, kcalsView;
        ViewHolder(View view){
            super(view);
            nameView = view.findViewById(R.id.mealName);
            countView = view.findViewById(R.id.mealAmount);
            proteinsView = view.findViewById(R.id.mealProtValue);
            carbohydratesView = view.findViewById(R.id.mealCarbValue);
            fatsView = view.findViewById(R.id.mealFatValue);
            kcalsView = view.findViewById(R.id.mealKcalValue);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onClick(view, getAdapterPosition());
            }
        }
    }
}
