package eugeen3.keepinfit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import eugeen3.keepinfit.R;

public class AddFoodItem extends AppCompatActivity {

    private TextView foodName;
    private TextView foodProt;
    private TextView foodCarb;
    private TextView foodFat;
    private TextView foodKcal;
    private Button cancelAdding;
    private Button addToMeal;
    private EditText portionWeight;
    private Integer iWeight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_food_item);
        setTitle("Добавить продукт");
        overridePendingTransition(0, 0);
/*
        foodName = findViewById(R.id.addFoodName);
        foodProt = findViewById(R.id.addFoodProtValue);
        foodCarb = findViewById(R.id.addFoodCarbValue);
        foodFat = findViewById(R.id.addFoodFatValue);
        foodKcal = findViewById(R.id.addFoodKcalValue);

        Bundle arguments = getIntent().getExtras();

        if(arguments!=null){
            String name = arguments.getString(Meal.KEY_NAME);
            String prots = arguments.get(Meal.KEY_PROTS).toString();
            String carbs = arguments.get(Meal.KEY_CARBS).toString();
            String fats = arguments.get(Meal.KEY_FATS).toString();
            String kcals = arguments.get(Meal.KEY_KCALS).toString();

            foodName.setText(name);
            foodProt.setText(prots);
            foodCarb.setText(carbs);
            foodFat.setText(fats);
            foodKcal.setText(kcals);
        }
*/
        initButtons();
        portionWeight = findViewById(R.id.portionWeight);
    }

    @Override
    public void onBackPressed() {
        backToSearch();
    }

    private void backToSearch() {
        Intent intent = new Intent(getApplicationContext(), SearchFoodItem.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private void initButtons() {
        cancelAdding = findViewById(R.id.btnCancelAdding);
        cancelAdding.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                backToSearch();
            }
        });
        addToMeal = findViewById(R.id.btnAddFoodItemToMeal);
        addToMeal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                iWeight = Integer.parseInt(portionWeight.getText().toString());
                if (iWeight <= 0) {
                    Toast.makeText(getApplicationContext(),
                            "Масса должна быть > 0", Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent = new Intent();
                    intent.putExtra(Meal.KEY_MASS, iWeight);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
}
