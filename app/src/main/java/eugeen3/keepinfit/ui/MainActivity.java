package eugeen3.keepinfit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.LinkedList;
import java.util.List;

import eugeen3.keepinfit.R;
import eugeen3.keepinfit.adapters.MealListAdapter;
import eugeen3.keepinfit.entities.Meal;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {
    private TextView BMR;

    private int BMRvalue;
    private List<Meal> mealList;
    private MealListAdapter adapter;
    private FloatingActionButton addMeal;
    private RecyclerView recyclerView;
    public static final String KEY_MEAL_NAME = "mealName";
    public static final String KEY_MEAL_AMOUNT = "mealAmount";
    public static final String KEY_MEAL_PROTS = "mealProts";
    public static final String KEY_MEAL_CARBS = "mealCarbs";
    public static final String KEY_MEAL_FATS = "mealFats";
    public static final String KEY_MEAL_KCALS = "mealKcals";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.today_stats);
        overridePendingTransition(0, 0);

        BMR = findViewById(R.id.goalKcal);
        addMeal = findViewById(R.id.addMeal);
        addMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MealActivity.class);
                startActivity(intent);
            }
        });

        addMeals();
        recyclerView = findViewById(R.id.mealsList);
        adapter = new MealListAdapter(this, mealList);
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {return;}
        BMRvalue = data.getIntExtra("BMR", 0);
        BMR.setText(valueOf(BMRvalue));
    }

    public void openProfile(View view) {
        Intent intent = new Intent(this, Profile.class);
        startActivityForResult(intent, 1);
    }

    private void addMeals() {
        mealList = new LinkedList<>();
        mealList.add(new Meal("Завтрак", 2, 240f, 320f, 424f, 800));
        mealList.add(new Meal("Обед", 4, 23f, 123f, 42f, 142));
    }

    @Override
    public void onClick(View view, int position) {
        final Meal meal = mealList.get(position);
        Intent intent = new Intent(this, MealActivity.class);
        intent.putExtra(KEY_MEAL_NAME, meal.getName());
        startActivity(intent);
    }
}
