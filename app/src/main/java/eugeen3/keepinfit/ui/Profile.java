package eugeen3.keepinfit.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import eugeen3.keepinfit.R;

import static java.lang.String.valueOf;

public class Profile extends AppCompatActivity
{
    private int ageInt;
    private int heightInt;
    private int weightInt;
    private int BMR;
    private int prots;
    private int fats;
    private int carbs;

    private Spinner genderSpinner;
    private Spinner goalSpinner;
    private Spinner activitySpinner;
    private EditText ageInput;
    private EditText heightInput;
    private EditText weightInput;
    private Button btnDone;

    private SharedPreferences sPref;
    public static final String USERS_DATA = "profile";
    public static final String USERS_GENDER = "gender";
    public static final String USERS_AGE = "age";
    public static final String USERS_HEIGHT = "height";
    public static final String USERS_WEIGHT = "weight";
    public static final String USERS_GOAL = "goal";
    public static final String USERS_ACTIVITY = "activity";
    public static final String GOAL_PROTS = "prots";
    public static final String GOAL_FATS = "fats";
    public static final String GOAL_CARBS = "carbs";
    public static final String GOAL_BMR = "bmr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        overridePendingTransition(0, 0);

        btnDone = findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        goalSpinner = findViewById(R.id.goalSpinner);
        activitySpinner = findViewById(R.id.activitySpinner);
        initializeSpinners();
        initializeInputs();

        sPref = getSharedPreferences(USERS_DATA, Context.MODE_PRIVATE);
        loadSettings();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveSettings();
    }

    private void initializeSpinners() {
        genderSpinner = findViewById(R.id.genderSpinner);
        goalSpinner = findViewById(R.id.goalSpinner);
        activitySpinner = findViewById(R.id.activitySpinner);
    }

    private void initializeInputs() {
        ageInput = findViewById(R.id.ageInput);
        heightInput = findViewById(R.id.heightInput);
        weightInput = findViewById(R.id.weightInput);
    }

    public void goBack() {
        saveSettings();
        Intent intent = new Intent();
        calculateBMR();
        calculatePFC();
        intent.putExtra(GOAL_BMR, BMR);
        intent.putExtra(GOAL_PROTS, prots);
        intent.putExtra(GOAL_FATS, fats);
        intent.putExtra(GOAL_CARBS, carbs);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void saveSettings() {
        Editor ed = sPref.edit();
        ed.putInt(USERS_GENDER, genderSpinner.getSelectedItemPosition());
        ed.putInt(USERS_AGE, Integer.parseInt(ageInput.getText().toString()));
        ed.putInt(USERS_HEIGHT, Integer.parseInt(heightInput.getText().toString()));
        ed.putInt(USERS_WEIGHT, Integer.parseInt(weightInput.getText().toString()));
        ed.putInt(USERS_GOAL, goalSpinner.getSelectedItemPosition());
        ed.putInt(USERS_ACTIVITY, activitySpinner.getSelectedItemPosition());
        ed.apply();
    }

    private void loadSettings() {
        genderSpinner.setSelection(sPref.getInt(USERS_GENDER, 0));
        ageInput.setText(valueOf(sPref.getInt(USERS_AGE, 0)));
        heightInput.setText(valueOf(sPref.getInt(USERS_HEIGHT, 0)));
        weightInput.setText(valueOf(sPref.getInt(USERS_WEIGHT, 0)));
        goalSpinner.setSelection(sPref.getInt(USERS_GOAL, 0));
        activitySpinner.setSelection(sPref.getInt(USERS_ACTIVITY, 0));
    }


    private void calculateBMR() {
        try {
            ageInt = Integer.parseInt(ageInput.getText().toString());
        }
        catch (NumberFormatException nfe)
        {
            System.out.println("age cast NumberFormatException: " + nfe.getMessage());
        }

        try {
            heightInt = Integer.parseInt(heightInput.getText().toString());
        }
        catch (NumberFormatException nfe)
        {
            System.out.println("height cast NumberFormatException: " + nfe.getMessage());
        }

        try {
            weightInt = Integer.parseInt(weightInput.getText().toString());
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

        if (goalSpinner.getSelectedItemPosition() == 0) {
            BMR *= 1.2;
        }
        else if (goalSpinner.getSelectedItemPosition() == 1) {
            BMR *= 0.8;
        }
    }

    private void calculatePFC(){
        switch (goalSpinner.getSelectedItemPosition()) {
            case 0: {
                prots = weightInt * 2;
                fats = (int) Math.round(weightInt * 0.9);
                carbs = (int) Math.round(weightInt * 4.5);
            }
            case 1: {
                prots = (int) Math.round(weightInt * 1.5);
                fats = (int) Math.round(weightInt * 0.8);
                carbs = (int) Math.round(weightInt * 2);
            }
            case 2: {
                prots = weightInt;
                fats = (int) Math.round(weightInt * 0.9);
                carbs = (int) Math.round(weightInt * 3);
            }
        }
    }

    @Override
    public void onBackPressed() {
        goBack();
    }
}