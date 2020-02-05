package eugeen3.keepinfit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import eugeen3.keepinfit.R;

public class AddFoodItem extends AppCompatActivity {

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

        initButtons();
        portionWeight = findViewById(R.id.portionWeight);
    }

    @Override
    public void onBackPressed() {
        backToSearch();
    }

    private void backToSearch() {
        Intent intent = new Intent(getApplicationContext(), SearchFoodItem.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
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
                   // Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                   // intent.putExtra(AGE_KEY, age);
                   // onActivityResult(intent, REQUEST_ACCESS_TYPE);
                }
            }
        });
    }
}
