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

public class Eating extends AppCompatActivity {

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
                startActivity(intent);
                finish();
            }
        });
        setInitialData();
        RecyclerView recyclerView = findViewById(R.id.foodItemsList);
        MealAdapter adapter = new MealAdapter(this, foodItems);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setInitialData(){
        foodItems.add(new FoodItem ("Банан", 200, 5.2f, 62.7f, 2, 278));
        foodItems.add(new FoodItem ("Каша овсяная", 200, 10, 123, 14.88f, 600));
        foodItems.add(new FoodItem ("Каша овсяная", 200, 10, 123, 14.88f, 600));
        foodItems.add(new FoodItem ("Каша овсяная", 200, 10, 123, 14.88f, 600));
        foodItems.add(new FoodItem ("Каша овсяная", 200, 10, 123, 14.88f, 600));
        //foodItems.add(new FoodItem ("Galaxy S8", "Samsung", R.drawable.galaxys6));
        //foodItems.add(new FoodItem ("LG G 5", "LG", R.drawable.nexus5x));
    }
}
