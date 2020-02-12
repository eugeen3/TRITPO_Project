package eugeen3.keepinfit.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import eugeen3.keepinfit.R;
import eugeen3.keepinfit.database.FoodDBHelper;
import eugeen3.keepinfit.entities.FoodItem;

public class SearchFoodItem extends AppCompatActivity {

    private FoodDBHelper sqlHelper;
    private SQLiteDatabase db;
    private Cursor foodItemCursor;
    private SimpleCursorAdapter foodItemAdapter;
    private ListView foodItemList;
    private EditText foodItemFilter;

    private String name;
    private int mass;
    private float prots;
    private float fats;
    private float carbs;
    private int kcals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        overridePendingTransition(0, 0);
        foodItemList = findViewById(R.id.foundedFoodItems);
        foodItemFilter = findViewById(R.id.searchFood);

        sqlHelper = new FoodDBHelper(getApplicationContext());
        sqlHelper.create_db();
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            db = sqlHelper.open();
            foodItemCursor = db.rawQuery("select * from " + FoodDBHelper.TABLE, null);
            String[] headers = new String[]{FoodDBHelper.COLUMN_NAME,
                    FoodDBHelper.COLUMN_PROTEINS, FoodDBHelper.COLUMN_FATS,
                    FoodDBHelper.COLUMN_CARBS, FoodDBHelper.COLUMN_KCALS, FoodDBHelper.COLUMN_ID};
            foodItemAdapter = new SimpleCursorAdapter(this, R.layout.founded_food_items,
                    foodItemCursor, headers, new int[]{R.id.foundedFoodName,
                    R.id.foundedFoodProtValue, R.id.foundedFoodFatValue,
                    R.id.foundedFoodCarbValue, R.id.foundedFoodKcalValue}, 0);

            if(!foodItemFilter.getText().toString().isEmpty())
                foodItemAdapter.getFilter().filter(foodItemFilter.getText().toString());

            foodItemFilter.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) { }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    foodItemAdapter.getFilter().filter(s.toString());
                }
            });

            foodItemAdapter.setFilterQueryProvider(new FilterQueryProvider() {
                @Override
                public Cursor runQuery(CharSequence constraint) {

                    if (constraint == null || constraint.length() == 0) {
                        return db.rawQuery("select * from " + FoodDBHelper.TABLE, null);
                    }
                    else {
                        return db.rawQuery("select * from " + FoodDBHelper.TABLE + " where " +
                                FoodDBHelper.COLUMN_NAME + " like ?", new String[]{"%" + constraint.toString() + "%"});
                    }
                }
            });

            foodItemList.setAdapter(foodItemAdapter);
            foodItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                    Cursor cursor = (Cursor) parent.getAdapter().getItem(position);
                    name = cursor.getString(1);
                    prots = cursor.getFloat(2);
                    carbs = cursor.getFloat(3);
                    fats = cursor.getFloat(4);
                    kcals = cursor.getInt(5);
                    showInputDialog();
                }
            });
        }
        catch (SQLException ex){}
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
        foodItemCursor.close();
    }

    private void finishActivity() {
        Intent intent = new Intent();
        intent.putExtra(MealActivity.KEY_NAME, name);
        intent.putExtra(MealActivity.KEY_MASS, mass);
        intent.putExtra(MealActivity.KEY_PROTS, prots);
        intent.putExtra(MealActivity.KEY_FATS, fats);
        intent.putExtra(MealActivity.KEY_CARBS, carbs);
        intent.putExtra(MealActivity.KEY_KCALS, kcals);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MealActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private void portionNV(int mass) {
        prots = FoodItem.portionNV(mass, prots);
        fats = FoodItem.portionNV(mass, fats);
        carbs = FoodItem.portionNV(mass, carbs);
        kcals = FoodItem.portionNV(mass, kcals);
    }

    public void showInputDialog() {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_food_mass, null);

        final EditText massText =  dialogView.findViewById(R.id.edt_comment);
        final Button input = dialogView.findViewById(R.id.btnInputMass);
        Button cancel = dialogView.findViewById(R.id.btnCancelInput);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.cancel();
            }
        });
        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mass = Integer.parseInt(massText.getText().toString());
                if (mass <= 0) {
                    Toast.makeText(getApplicationContext(),
                            "Масса должна быть > 0", Toast.LENGTH_LONG).show();
                }
                else {
                    portionNV(mass);
                    finishActivity();
                }
                dialogBuilder.dismiss();
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }
}