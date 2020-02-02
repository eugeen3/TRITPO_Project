package eugeen3.keepinfit.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import eugeen3.keepinfit.R;
import eugeen3.keepinfit.adapters.SearchAdapter;

public class SearchFoodItem extends Eating {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal);
        setTitle("textView");
        overridePendingTransition(0, 0);

        //setInitialData();
        RecyclerView recyclerView2 = findViewById(R.id.foundedFoodItems);
        SearchAdapter adapter = new SearchAdapter(this, foodItems);
        recyclerView2.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Eating.class);
        startActivity(intent);
        finish();
    }

    /*
    private void setInitialData(){
        foodItems.add(new FoodItem ("Банан", 200, 5.2f, 62.7f, 2, 278));
        foodItems.add(new FoodItem ("Каша овсяная", 200, 10, 123, 14.88f, 600));
        //foodItems.add(new FoodItem ("Galaxy S8", "Samsung", R.drawable.galaxys6));
        //foodItems.add(new FoodItem ("LG G 5", "LG", R.drawable.nexus5x));
    }

     */
}