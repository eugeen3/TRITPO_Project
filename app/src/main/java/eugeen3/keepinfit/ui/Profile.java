package eugeen3.keepinfit.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import eugeen3.keepinfit.R;

public class Profile extends AppCompatActivity implements TextWatcher{

    /*
    private TextInputEditText age;
    private TextInputEditText height;
    private TextInputEditText weight;
     */
    private int ageInt;
    private int heightInt;
    private int weightInt;
    private int BMR;

    Spinner genderSpinner;
    Spinner ageSpinner;
    Spinner heightSpinner;
    Spinner weightSpinner;
    Spinner goalSpinner;
    Spinner activitySpinner;

    SharedPreferences sPref;
    public static final String USERS_DATA = "profile";
    public static final String USERS_GENDER = "gender";
    public static final String USERS_AGE = "age";
    public static final String USERS_HEIGHT = "height";
    public static final String USERS_WEIGHT = "weight";
    public static final String USERS_GOAL = "goal";
    public static final String USERS_ACTIVITY = "activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        overridePendingTransition(0, 0);

        initializeSpinners();

        sPref = getSharedPreferences(USERS_DATA, Context.MODE_PRIVATE);
        loadSettings();
        //createTextListeners();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveSettings();
    }

    private void createTextListeners() {
        /*
        age.addTextChangedListener(this);
        height.addTextChangedListener(this);
        weight.addTextChangedListener(this);

         */
    }

    private void initializeSpinners() {
        genderSpinner = findViewById(R.id.genderSpinner);
        ageSpinner = findViewById(R.id.ageSpinner);
        heightSpinner = findViewById(R.id.heightSpinner);
        weightSpinner = findViewById(R.id.weightSpinner);
        goalSpinner = findViewById(R.id.goalSpinner);
        activitySpinner = findViewById(R.id.activitySpinner);
    }

    public void goBack(View view) {
        saveSettings();
        Intent intent = new Intent();
        BMR = calculateBMR();
        intent.putExtra("BMR", BMR);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void saveSettings() {
        Editor ed = sPref.edit();
        ed.putInt(USERS_GENDER, genderSpinner.getSelectedItemPosition());
        ed.putInt(USERS_AGE, ageSpinner.getSelectedItemPosition());
        ed.putInt(USERS_HEIGHT, heightSpinner.getSelectedItemPosition());
        ed.putInt(USERS_WEIGHT, weightSpinner.getSelectedItemPosition());
        ed.putInt(USERS_GOAL, goalSpinner.getSelectedItemPosition());
        ed.putInt(USERS_ACTIVITY, activitySpinner.getSelectedItemPosition());
        ed.apply();
    }

    private void loadSettings() {
        genderSpinner.setSelection(sPref.getInt(USERS_GENDER, 0));
        ageSpinner.setSelection(sPref.getInt(USERS_AGE, 0));
        heightSpinner.setSelection(sPref.getInt(USERS_HEIGHT, 0));
        weightSpinner.setSelection(sPref.getInt(USERS_WEIGHT, 0));
        goalSpinner.setSelection(sPref.getInt(USERS_GOAL, 0));
        activitySpinner.setSelection(sPref.getInt(USERS_ACTIVITY, 0));
    }


    private int calculateBMR() {
        /*
        try {
            int ageInt = Integer.parseInt(age.getText().toString());
        }
        catch (NumberFormatException nfe)
        {
            System.out.println("age cast NumberFormatException: " + nfe.getMessage());
        }

        try {
            int heightInt = Integer.parseInt(age.getText().toString());
        }
        catch (NumberFormatException nfe)
        {
            System.out.println("height cast NumberFormatException: " + nfe.getMessage());
        }

        try {
            int weightInt = Integer.parseInt(age.getText().toString());
        }
        catch (NumberFormatException nfe)
        {
            System.out.println("weight cast NumberFormatException: " + nfe.getMessage());
        }

         */

        try {
            ageInt = Integer.parseInt(ageSpinner.getSelectedItem().toString());
        }
        catch (NumberFormatException nfe)
        {
            System.out.println("age cast NumberFormatException: " + nfe.getMessage());
        }
        try {
            heightInt = Integer.parseInt(heightSpinner.getSelectedItem().toString());
        }
        catch (NumberFormatException nfe)
        {
            System.out.println("height cast NumberFormatException: " + nfe.getMessage());
        }
        try {
            weightInt = Integer.parseInt(weightSpinner.getSelectedItem().toString());
        }
        catch (NumberFormatException nfe)
        {
            System.out.println("weight cast NumberFormatException: " + nfe.getMessage());
        }

        if (genderSpinner.getSelectedItemPosition() == 0) {
            BMR = (int) Math.round((88.36 + (13.4 * weightInt) + (4.8 * heightInt) - (5.7 * ageInt)));
        }
        else {
            BMR = (int) Math.round((447.6 + (9.2 * weightInt) + (3.1 * heightInt) - (4.3 * ageInt)));
        }

        Spinner activitySpinner = findViewById(R.id.activitySpinner);
        switch (activitySpinner.getSelectedItemPosition()) {
            case 0: {
                BMR *= 1.2;
                break;
            }
            case 1: {
                BMR *= 1.375;
                break;
            }
            case 2: {
                BMR *= 1.55;
                break;
            }
            case 3: {
                BMR *= 1.725;
                break;
            }
            case 4: {
                BMR *= 1.9;
                break;
            }
        }

        Spinner goalSpinner = findViewById(R.id.goalSpinner);
        if (goalSpinner.getSelectedItemPosition() == 0) {
            BMR *= 1.2;
        }
        else if (genderSpinner.getSelectedItemPosition() == 1) {
            BMR *= 0.8;
        }

        return BMR;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        /*
        if (age.getText().toString() != null) {
            int ageInt = Integer.parseInt(age.getText().toString());
        }
        if (height.getText().toString() != null) {
            int heightInt = Integer.parseInt(height.getText().toString());
        }
        if (weight.getText().toString() != null) {
            int weightInt = Integer.parseInt(weight.getText().toString());
        }

        if (ageInt < 10 || ageInt > 120) {
            Toast.makeText(this, "Некорректный возраст", Toast.LENGTH_LONG).show();
        }

        if (heightInt < 140 || heightInt > 230) {
            Toast.makeText(this, "Некорректный рост", Toast.LENGTH_LONG).show();
        }

        if (weightInt < 40 || weightInt > 300) {
            Toast.makeText(this, "Некорректный вес", Toast.LENGTH_LONG).show();
        }
         */
    }
}
