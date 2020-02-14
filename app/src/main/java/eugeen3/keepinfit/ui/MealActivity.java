package eugeen3.keepinfit.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;

import eugeen3.keepinfit.R;
import eugeen3.keepinfit.adapters.MealAdapter;
import eugeen3.keepinfit.database.FileList;
import eugeen3.keepinfit.entities.FoodItem;
import eugeen3.keepinfit.entities.Meal;

public class MealActivity extends AppCompatActivity {

    private FileList<FoodItem> fileList;
    private List<FoodItem> foodItems;
    private MealAdapter adapter;
    private RecyclerView recyclerView;
    private FloatingActionButton addFoodItem;
    private TextView mealName;
    private int number;

    public static final String KEY_NAME = "name";
    public static final String KEY_MASS = "mass";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_PROTS = "prots";
    public static final String KEY_CARBS = "carbs";
    public static final String KEY_FATS = "fats";
    public static final String KEY_KCALS = "kcals";
    public static String FILE_PATH;
    public static String FILE_NAME;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal);
        overridePendingTransition(0, 0);

        mealName = findViewById(R.id.mealTitle);
        Bundle arguments = getIntent().getExtras();
        if(arguments!= null){
            String name = arguments.getString(MainActivity.KEY_MEAL_NAME);
            number = arguments.getInt(MainActivity.KEY_NUMBER);
            mealName.setText(name);
            FILE_NAME = name;
        }

        foodItems = new LinkedList<>();
        List<String> str = loadFromFile();
        if (str != null) restoreList(str);

        addFoodItem = findViewById(R.id.btnAddFoodItem);
        addFoodItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchFoodItem.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent, 1);
                saveToFile();
            }
        });

        recyclerView = findViewById(R.id.foodItemsList);
        adapter = new MealAdapter(this, foodItems);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {return;}
        String name = data.getStringExtra(KEY_NAME);
        int mass = data.getIntExtra(KEY_MASS, 100);
        float prots = data.getFloatExtra(KEY_PROTS, 1.0f);
        float fats = data.getFloatExtra(KEY_FATS, 1.0f);
        float carbs = data.getFloatExtra(KEY_CARBS, 1.0f);
        int kcals = data.getIntExtra(KEY_KCALS, 1);
        FoodItem fItem = new FoodItem(name, mass, prots, fats, carbs, kcals);
        foodItems.add(fItem);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        if (foodItems.size() != 0) {
            Meal meal = sumAllFood();
            intent.putExtra(KEY_NAME, meal.getName());
            intent.putExtra(KEY_AMOUNT, meal.getAmountOfProducts());
            intent.putExtra(KEY_PROTS, meal.getProteins());
            intent.putExtra(KEY_CARBS, meal.getCarbohydrates());
            intent.putExtra(KEY_FATS, meal.getFats());
            intent.putExtra(KEY_KCALS, meal.getKcals());
            intent.putExtra(MainActivity.KEY_NUMBER, number);
        }
        else {
            intent.putExtra(KEY_NAME, FILE_NAME);
            intent.putExtra(MainActivity.KEY_NUMBER, number);
        }
        setResult(RESULT_OK ,intent);
        finish();
    }

    @Override
    public void onDestroy() {
        saveToFile();
        super.onDestroy();
    }

    private void saveToFile() {
        Context context = getApplicationContext();
        FILE_PATH = context.getFilesDir().toString() + FileList.CHAR_SLASH + FILE_NAME;
        FileList fileList = new FileList(FILE_PATH, foodItems);
        fileList.saveList();
    }

    private List<String> loadFromFile() {
        Context context = getApplicationContext();
        FILE_PATH = context.getFilesDir().toString() + FileList.CHAR_SLASH + FILE_NAME;
        List<String> str = null;
        try {
            FileList fileList = new FileList(FILE_PATH, foodItems);
            str = fileList.loadList();
        } catch (Exception e) { }
        return str;
    }

    private void restoreList(List<String> str) {
        fileList = new FileList<>();
        for (int i = 0; i < str.size();i++) {

            String[] info = fileList.stringParser(str.get(i));
            FoodItem foodItem = new FoodItem(
                    info[0] = info[0].replace(FileList.CHAR_UNDERSCORE, FileList.CHAR_SPACE),
                    Integer.parseInt(info[1]),
                    Float.parseFloat(info[2]),
                    Float.parseFloat(info[3]),
                    Float.parseFloat(info[4]),
                    Integer.parseInt(info[5]));
            foodItems.add(foodItem);
        }
    }

    private Meal sumAllFood () {
        if (foodItems.size() != 0) {
            Meal allFoodItems = new Meal(FILE_NAME);
            int amountOfProducts = foodItems.size();
            float proteins = 0;
            float carbohydrates = 0;
            float fats = 0;
            int kcals = 0;
            for (FoodItem foodItem : foodItems) {
                proteins += foodItem.getProteins();
                carbohydrates += foodItem.getCarbohydrates();
                fats += foodItem.getFats();
                kcals += foodItem.getKcals();
            }
            allFoodItems.setAmountOfProducts(amountOfProducts);
            allFoodItems.setProteins(new BigDecimal(proteins).
                    setScale(2, RoundingMode.UP).floatValue());
            allFoodItems.setFats(new BigDecimal(fats).
                    setScale(2, RoundingMode.UP).floatValue());
            allFoodItems.setCarbohydrates(new BigDecimal(carbohydrates).
                    setScale(2, RoundingMode.UP).floatValue());
            allFoodItems.setKcals(kcals);
            return allFoodItems;
        }
        else {
            return new Meal(FILE_NAME);
        }
    }
}