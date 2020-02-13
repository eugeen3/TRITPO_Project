package eugeen3.keepinfit.ui;

import android.content.Context;
import android.content.Intent;
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

import java.util.LinkedList;
import java.util.List;

import eugeen3.keepinfit.R;
import eugeen3.keepinfit.adapters.MealListAdapter;
import eugeen3.keepinfit.database.FileList;
import eugeen3.keepinfit.entities.FoodItem;
import eugeen3.keepinfit.entities.Meal;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {
    private TextView BMR;

    private int BMRvalue;
    private FileList<FoodItem> fileList;
    private List<Meal> mealList;
    private MealListAdapter adapter;
    private FloatingActionButton addMeal;
    private RecyclerView recyclerView;
    private Button btnProfile;

    public static final int REQUEST_CODE_PROFILE = 1;
    public static final int REQUEST_CODE_MEAL = 1;
    public static final String KEY_MEAL_NAME = "mealName";
    public static final String KEY_MEAL_AMOUNT = "mealAmount";
    public static final String KEY_MEAL_PROTS = "mealProts";
    public static final String KEY_MEAL_CARBS = "mealCarbs";
    public static final String KEY_MEAL_FATS = "mealFats";
    public static final String KEY_MEAL_KCALS = "mealKcals";
    public static final String KEY_NUMBER = "mealNumber";
    public static String FILE_PATH;
    public static final String FILE_NAME = "allMeals";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.today_stats);
        overridePendingTransition(0, 0);

        mealList = new LinkedList<>();
        List<String> str = loadFromFile();
        if (str != null) restoreList(str);

        Bundle arguments = getIntent().getExtras();
        if(arguments!= null){
            String name = arguments.getString(MealActivity.KEY_NAME);
            int amount = arguments.getInt(MealActivity.KEY_AMOUNT);
            float proteins = arguments.getFloat(MealActivity.KEY_PROTS);
            float fats = arguments.getFloat(MealActivity.KEY_FATS);
            float carbohydrates = arguments.getFloat(MealActivity.KEY_CARBS);
            int kcals = arguments.getInt(MealActivity.KEY_KCALS);
            //if(!mealList.isEmpty())
                mealList.set(arguments.getInt(KEY_NUMBER),
                        new Meal(name, amount, proteins, fats, carbohydrates, kcals));
            //else mealList.add(new Meal(name, amount, proteins, fats, carbohydrates, kcals));
        }

        //addMeals();
        BMR = findViewById(R.id.goalKcal);
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
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent, REQUEST_CODE_PROFILE);
            }
        });

        recyclerView = findViewById(R.id.mealsList);
        adapter = new MealListAdapter(this, mealList);
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {


        super.onResume();
    }

    @Override
    public void onDestroy() {
        saveToFile();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        finish();
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
                BMR.setText(valueOf(BMRvalue));
                break;
            }
//            case REQUEST_CODE_MEAL: {
//                if (data == null) {
//                    return;
//                }
//                BMRvalue = data.getIntExtra("BMR", 0);
//                BMR.setText(valueOf(BMRvalue));
//                break;
//            }
        }

    }

    private void addMeals() {
        mealList.add(new Meal("Завтрак", 2, 240f, 320f, 424f, 800));
        mealList.add(new Meal("Обед", 4, 23f, 123f, 42f, 142));
    }

    @Override
    public void onClick(View view, int position) {
        saveToFile();
        final Meal meal = mealList.get(position);
        Intent intent = new Intent(this, MealActivity.class);
        intent.putExtra(KEY_MEAL_NAME, meal.getName());
        intent.putExtra(KEY_NUMBER, position);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityForResult(,intent);
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
}