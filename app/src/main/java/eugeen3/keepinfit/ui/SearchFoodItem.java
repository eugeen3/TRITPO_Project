package eugeen3.keepinfit.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import eugeen3.keepinfit.R;
import eugeen3.keepinfit.adapters.DBAdapter;
import eugeen3.keepinfit.entities.FoodItem;

import static java.lang.String.valueOf;

public class SearchFoodItem extends AppCompatActivity {

    DBAdapter sqlHelper;
    SQLiteDatabase db;
    Cursor foodItemCursor;
    SimpleCursorAdapter foodItemAdapter;
    ListView foodItemList;
    EditText foodItemFilter;

    String name;
    int mass;
    float prots;
    float fats;
    float carbs;
    int kcals;
    FoodItem fItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        overridePendingTransition(0, 0);
        foodItemList = findViewById(R.id.foundedFoodItems);
        foodItemFilter = findViewById(R.id.searchFood);

        sqlHelper = new DBAdapter(getApplicationContext());
        sqlHelper.create_db();
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            db = sqlHelper.open();
            foodItemCursor = db.rawQuery("select * from " + DBAdapter.getTable(), null);
            String[] headers = new String[]{DBAdapter.getColumnName(),
                    DBAdapter.getColumnProteins(), DBAdapter.getColumnFats(),
                    DBAdapter.getColumnCarbs(), DBAdapter.getColumnKcals(), DBAdapter.getColumnId()};
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
                        return db.rawQuery("select * from " + DBAdapter.getTable(), null);
                    }
                    else {
                        return db.rawQuery("select * from " + DBAdapter.getTable() + " where " +
                                DBAdapter.getColumnName() + " like ?", new String[]{"%" + constraint.toString() + "%"});
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
                    Intent intent = new Intent(getApplicationContext(), AddFoodItem.class);
                    intent.putExtra(Meal.KEY_NAME, name);
                    intent.putExtra(Meal.KEY_PROTS, prots);
                    intent.putExtra(Meal.KEY_CARBS, carbs);
                    intent.putExtra(Meal.KEY_FATS, fats);
                    intent.putExtra(Meal.KEY_KCALS, kcals);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivityForResult(intent, 1);
                    finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {return;}
        mass = data.getIntExtra("mass", 0);
        portionNV(mass);
        Intent intent = new Intent();
        intent.putExtra(Meal.KEY_NAME, name);
        intent.putExtra(Meal.KEY_MASS, mass);
        intent.putExtra(Meal.KEY_PROTS, prots);
        intent.putExtra(Meal.KEY_CARBS, carbs);
        intent.putExtra(Meal.KEY_FATS, fats);
        intent.putExtra(Meal.KEY_KCALS, kcals);
        setResult(RESULT_OK, intent);
        //Toast.makeText(getApplicationContext(),
        //        name + " " + prots + " " + carbs + " " + fats + " " + kcals,
        //        Toast.LENGTH_LONG).show();
        finish();
    }

    public void cancelSearch(View view) {
        Intent intent = new Intent(this, Meal.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Meal.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private void portionNV(int mass) {
        prots = FoodItem.portionNV(mass, prots);
        carbs = FoodItem.portionNV(mass, carbs);
        fats = FoodItem.portionNV(mass, fats);
        kcals = FoodItem.portionNV(mass, kcals);
    }
}