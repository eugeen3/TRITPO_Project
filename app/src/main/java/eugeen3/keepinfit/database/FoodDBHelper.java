package eugeen3.keepinfit.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FoodDBHelper extends SQLiteOpenHelper {
    private static String DB_PATH;
    private static String DB_NAME = "foodItems.db";
    private static final int SCHEMA = 1;
    public static final String TABLE = "foodItems";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PROTEINS = "proteins";
    public static final String COLUMN_CARBS = "carbs";
    public static final String COLUMN_FATS = "fats";
    public static final String COLUMN_KCALS = "kcals";
    private Context myContext;

    public FoodDBHelper(Context context) {
        super(context, DB_NAME, null, SCHEMA);
        this.myContext = context;
        DB_PATH = "/data/data/"+context.getPackageName()+"/databases/" + DB_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
    }

    public void create_db(){
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            File file = new File(DB_PATH);
            if (!file.exists()) {
                this.getReadableDatabase();
                inputStream = myContext.getAssets().open(DB_NAME);
                String outFileName = DB_PATH;
                outputStream = new FileOutputStream(outFileName);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                outputStream.flush();
                outputStream.close();
                inputStream.close();
            }
        }
        catch(IOException ex){
            Log.d(this.getClass().getSimpleName(), ex.getMessage());
        }
    }
    public SQLiteDatabase open()throws SQLException {
        return SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }
}