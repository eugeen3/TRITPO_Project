package eugeen3.keepinfit.ui;

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
    public static final String KEY_NUMBER = "mealNumber";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.today_stats);
        overridePendingTransition(0, 0);

        mealList = new LinkedList<>();
        addMeals();
        BMR = findViewById(R.id.goalKcal);
        addMeal = findViewById(R.id.btnAddMeal);
        addMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTitleDialog();
            }
        });

        Bundle arguments = getIntent().getExtras();
        if(arguments!= null){
            String name = arguments.getString(MealActivity.KEY_NAME);
            int amount = arguments.getInt(MealActivity.KEY_AMOUNT);
            float proteins = arguments.getFloat(MealActivity.KEY_PROTS);
            float fats = arguments.getFloat(MealActivity.KEY_FATS);
            float carbohydrates = arguments.getFloat(MealActivity.KEY_CARBS);
            int kcals = arguments.getInt(MealActivity.KEY_KCALS);
            mealList.set(arguments.getInt(KEY_NUMBER),
                    new Meal(name, amount, proteins, fats, carbohydrates, kcals));
        }

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
        mealList.add(new Meal("Завтрак", 2, 240f, 320f, 424f, 800));
        mealList.add(new Meal("Обед", 4, 23f, 123f, 42f, 142));
    }

    @Override
    public void onClick(View view, int position) {
        final Meal meal = mealList.get(position);
        Intent intent = new Intent(this, MealActivity.class);
        intent.putExtra(KEY_MEAL_NAME, meal.getName());
        intent.putExtra(KEY_NUMBER, position);
        startActivity(intent);
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
                    if (newName.contains(" ")) {
                        Toast.makeText(getApplicationContext(),
                                "В названии не должно быть пробелов", Toast.LENGTH_LONG).show();
                        break;
                    }
                    if(mealList != null) {
                        int cnt = 0;
                        for (Meal i : mealList) {
                            cnt++;
                            if (newName.equals(i.getName())) {
                                Toast.makeText(getApplicationContext(),
                                        "Приём пищи с таким названием уже существует", Toast.LENGTH_LONG).show();
                                nameText.setText("");
                                break;
                            }
                            if (cnt == mealList.size() - 1) {
                                addNewMeal(newName);
                                break;
                            }
                        }
                    }
                    else {
                      //  if (newName.equals("Завтрак")) {
                      //      Toast.makeText(getApplicationContext(),
                      //             "asdsa", Toast.LENGTH_LONG).show();
                      //  }
                        addNewMeal(newName);
                    }
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
    }
}