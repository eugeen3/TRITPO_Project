package eugeen3.keepinfit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import eugeen3.keepinfit.R;

public class MainActivity extends AppCompatActivity {
    private TextView BMR;

    private int BMRvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.today_stats);
        overridePendingTransition(0, 0);

        BMR = findViewById(R.id.goalKcal);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {return;}
        BMRvalue = data.getIntExtra("BMR", 0);
        BMR.setText("" + BMRvalue);
    }

    public void openProfile(View view) {
        Intent intent = new Intent(this, Profile.class);
        startActivityForResult(intent, 1);
    }

    public void openFoodList(View view) {
        Intent intent = new Intent(this, Eating.class);
        startActivity(intent);
        finish();
    }
}
