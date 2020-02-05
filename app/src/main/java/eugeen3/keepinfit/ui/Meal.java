package eugeen3.keepinfit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import eugeen3.keepinfit.R;
import eugeen3.keepinfit.adapters.MealAdapter;
import eugeen3.keepinfit.entities.FoodItem;

public class Meal extends AppCompatActivity {

    private List<FoodItem> foodItems = new ArrayList<>();
    private FloatingActionButton addFoodItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal);
        setTitle("textView");
        overridePendingTransition(0, 0);

        addFoodItem = findViewById(R.id.btnAddFoodItem);
        addFoodItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchFoodItem.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent, 1);
                finish();
            }
        });
        setInitialData();
        RecyclerView recyclerView = findViewById(R.id.foodItemsList);
        MealAdapter adapter = new MealAdapter(this, foodItems);
        
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {return;}
        String name = data.getStringExtra("foodName");
        int mass = data.getIntExtra("foodMass", 100);
        float prots = data.getFloatExtra("foodProts", 1.0f);
        float fats = data.getFloatExtra("foodFats", 1.0f);
        float carbs = data.getFloatExtra("foodCarbs", 1.0f);
        int kcals = data.getIntExtra("foodKcals", 1);
        FoodItem fItem = new FoodItem(name, mass, prots, carbs, fats, kcals);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private void setInitialData(){
        foodItems.add(new FoodItem ("Банан", 200, 5.2f, 62.7f, 2, 278));
        foodItems.add(new FoodItem ("Каша овсяная", 200, 10, 123, 14.88f, 600));
        foodItems.add(new FoodItem ("Каша овсяная", 200, 10, 123, 14.88f, 600));
        foodItems.add(new FoodItem ("Каша овсяная", 200, 10, 123, 14.88f, 600));
        foodItems.add(new FoodItem ("Каша овсяная", 200, 10, 123, 14.88f, 600));
    }
}
