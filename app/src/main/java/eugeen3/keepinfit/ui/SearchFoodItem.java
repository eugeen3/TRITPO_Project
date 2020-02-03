package eugeen3.keepinfit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import eugeen3.keepinfit.R;
import eugeen3.keepinfit.adapters.SearchAdapter;
import eugeen3.keepinfit.entities.FoodItem;

public class SearchFoodItem extends AppCompatActivity {

    private List<FoodItem> foodItems = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal);
        setTitle("textView");
        overridePendingTransition(0, 0);

        setInitialData();
        RecyclerView recyclerView = findViewById(R.id.foodItemsList);
        SearchAdapter adapter = new SearchAdapter(this, foodItems);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Eating.class);
        startActivity(intent);
        finish();
    }

    private void setInitialData() {
        foodItems.add(new FoodItem("Банан", 200, 5.2f, 62.7f, 2, 278));
        foodItems.add(new FoodItem("Каша овсяная", 200, 10, 123, 14.88f, 600));
    }
}