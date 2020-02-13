package eugeen3.keepinfit.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;

import eugeen3.keepinfit.R;
import eugeen3.keepinfit.adapters.MealListAdapter;
import eugeen3.keepinfit.database.FileList;
import eugeen3.keepinfit.entities.FoodItem;
import eugeen3.keepinfit.entities.Meal;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {

    private int BMRvalue;
    private FileList<FoodItem> fileList;
    private List<Meal> mealList;
    private MealListAdapter adapter;
    private FloatingActionButton addMeal;
    private RecyclerView recyclerView;
    private Button btnProfile;
    private Button btnReset;
    private TextView curProts;
    private TextView curFats;
    private TextView curCarbs;
    private TextView curKcals;
    private TextView goalProts;
    private TextView goalFats;
    private TextView goalCarbs;
    private TextView goalKcals;

    public static final int REQUEST_CODE_PROFILE = 1;
    public static final int REQUEST_CODE_MEAL = 2;
    public static final String KEY_MEAL_NAME = "mealName";
    public static final String KEY_MEAL_AMOUNT = "mealAmount";
    public static final String KEY_MEAL_PROTS = "mealProts";
    public static final String KEY_MEAL_CARBS = "mealCarbs";
    public static final String KEY_MEAL_FATS = "mealFats";
    public static final String KEY_MEAL_KCALS = "mealKcals";
    public static final String KEY_NUMBER = "mealNumber";
    public static String FILE_PATH;
    public static final String FILE_NAME = "all_meals";
    public static final String FILE_STATS = "stats";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.today_stats);
        overridePendingTransition(0, 0);

        mealList = new LinkedList<>();
        List<String> str = loadFromFile();
        if (str != null) restoreList(str);

        addMeal = findViewById(R.id.btnAddMeal);
        addMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTitleDialog();
            }
        });

        btnProfile = findViewById(R.id.openProfile);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivityForResult(intent, REQUEST_CODE_PROFILE);
            }
        });

        curProts = findViewById(R.id.currentProts);
        curFats = findViewById(R.id.currentFats);
        curCarbs = findViewById(R.id.currentCarbs);
        curKcals = findViewById(R.id.currentKcals);

        goalProts = findViewById(R.id.goalProts);
        goalFats = findViewById(R.id.goalFats);
        goalCarbs = findViewById(R.id.goalCarbs);
        goalKcals = findViewById(R.id.goalKcals);

        //FileList stats = new FileList(FILE_STATS);
        //String stats = stats.loadList();
        updateStats();

        recyclerView = findViewById(R.id.mealsList);
        adapter = new MealListAdapter(this, mealList);
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        saveToFile();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        saveToFile();
        super.onPause();
    }

    @Override
    public void onResume() {
        updateStats();
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_PROFILE: {
                if (data == null) {
                    return;
                }
                BMRvalue = data.getIntExtra("BMR", 0);
                goalKcals.setText("/" + valueOf(BMRvalue));
                break;
            }
            case REQUEST_CODE_MEAL: {
                if (data == null) {
                    return;
                }
                String name = data.getStringExtra(MealActivity.KEY_NAME);
                int amount = data.getIntExtra(MealActivity.KEY_AMOUNT, 0);
                float proteins = data.getFloatExtra(MealActivity.KEY_PROTS, 0f);
                float fats = data.getFloatExtra(MealActivity.KEY_FATS, 0f);
                float carbohydrates = data.getFloatExtra(MealActivity.KEY_CARBS, 0f);
                int kcals = data.getIntExtra(MealActivity.KEY_KCALS, 0);
                mealList.set(data.getIntExtra(KEY_NUMBER, 0),
                        new Meal(name, amount, proteins, fats, carbohydrates, kcals));
                adapter.notifyDataSetChanged();
                break;
            }
        }

    }

    @Override
    public void onClick(View view, int position) {
        saveToFile();
        final Meal meal = mealList.get(position);
        Intent intent = new Intent(this, MealActivity.class);
        intent.putExtra(KEY_MEAL_NAME, meal.getName());
        intent.putExtra(KEY_NUMBER, position);
        startActivityForResult(intent, REQUEST_CODE_MEAL);
    }

    private void updateStats() {

        if (!mealList.isEmpty()) {
            sumAllMeals();
        }
        else {
            curProts.setText(valueOf(0));
            curFats.setText(valueOf(0));
            curCarbs.setText(valueOf(0));
            curKcals.setText(valueOf(0));
        }
    }

    public void showTitleDialog() {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_food_mass, null);

        final TextView name = dialogView.findViewById(R.id.inputDialogText);
        final EditText nameText =  dialogView.findViewById(R.id.edt_comment);
        final Button input = dialogView.findViewById(R.id.btnInputMass);
        Button cancel = dialogView.findViewById(R.id.btnCancelInput);
        nameText.setHint("");
        nameText.setInputType(InputType.TYPE_CLASS_TEXT);
        name.setText("Введите название приёма пищи");

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.cancel();
            }
        });
        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = nameText.getText().toString();
                while (true) {
                    if (newName.equals("")) {
                        Toast.makeText(getApplicationContext(),
                                "Введите название", Toast.LENGTH_LONG).show();
                        break;
                    }
//                    if (newName.contains(" ")) {
//                        Toast.makeText(getApplicationContext(),
//                                "В названии не должно быть пробелов", Toast.LENGTH_LONG).show();
//                        break;
//                    }

                    if(mealList != null) {
                        int cnt = 0;
                        for (Meal i : mealList) {
                            cnt++;
                            if (newName.equals(i.getName())) {
                                Toast.makeText(getApplicationContext(),
                                        "Приём пищи с таким названием уже существует",
                                        Toast.LENGTH_LONG).show();
                                nameText.setText("");
                                break;
                            }
                            if (cnt == mealList.size() - 1) {
                                addNewMeal(newName);
                                break;
                            }
                        }
                    }
                    addNewMeal(newName);
                    break;
                }
                dialogBuilder.dismiss();
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    private void addNewMeal(String name) {
        mealList.add(new Meal(name));
        adapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(),
                valueOf(mealList.size()), Toast.LENGTH_LONG).show();
        saveToFile();
    }

    private void saveToFile() {
        Context context = getApplicationContext();
        FILE_PATH = context.getFilesDir().toString() + FileList.CHAR_SLASH + FILE_NAME;
        FileList fileList = new FileList(FILE_PATH, mealList);
        fileList.saveList();
    }

    private List<String> loadFromFile() {
        Context context = getApplicationContext();
        FILE_PATH = context.getFilesDir().toString() + FileList.CHAR_SLASH + FILE_NAME;
        List<String> str = null;
        try {
            FileList fileList = new FileList(FILE_PATH, mealList);
            str = fileList.loadList();
        } catch (Exception e) { }
        return str;
    }

    private void restoreList(List<String> str) {
        fileList = new FileList<>();
        for (int i = 0; i < str.size();i++) {
            String[] info = fileList.stringParser(str.get(i));
            Meal meal = new Meal(
                    info[0] = info[0].replace(FileList.CHAR_UNDERSCORE, FileList.CHAR_SPACE),
                    Integer.parseInt(info[1]),
                    Float.parseFloat(info[2]),
                    Float.parseFloat(info[3]),
                    Float.parseFloat(info[4]),
                    Integer.parseInt(info[5]));
            mealList.add(meal);
        }
    }

    private void sumAllMeals() {
        if (mealList.size() != 0) {
            float proteins = 0;
            float carbohydrates = 0;
            float fats = 0;
            int kcals = 0;
            for (Meal meal : mealList) {
                proteins += meal.getProteins();
                carbohydrates += meal.getCarbohydrates();
                fats += meal.getFats();
                kcals += meal.getKcals();
            }
            curProts.setText(valueOf(new BigDecimal(proteins).
                    setScale(2, RoundingMode.UP).floatValue()));
            curFats.setText(valueOf(new BigDecimal(fats).
                    setScale(2, RoundingMode.UP).floatValue()));
            curCarbs.setText(valueOf(new BigDecimal(carbohydrates).
                    setScale(2, RoundingMode.UP).floatValue()));
            curKcals.setText(valueOf(kcals));
        }
    }
}