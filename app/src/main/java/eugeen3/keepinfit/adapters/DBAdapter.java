package eugeen3.keepinfit.adapters;

import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBAdapter extends SQLiteOpenHelper {
    private static String DB_PATH;
    private static String DB_NAME = "foodItems.db";
    private static final int SCHEMA = 1;
    private static final String TABLE = "foodItems";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PROTEINS = "proteins";
    private static final String COLUMN_CARBS = "carbs";
    private static final String COLUMN_FATS = "fats";
    private static final String COLUMN_KCALS = "kcals";
    private Context myContext;

    public DBAdapter(Context context) {
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
            Log.d("DBAdapter", ex.getMessage());
        }
    }
    public SQLiteDatabase open()throws SQLException {
        return SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public static String getTable() {
        return TABLE;
    }

    public static String getColumnId() {
        return COLUMN_ID;
    }

    public static String getColumnName() {
        return COLUMN_NAME;
    }

    public static String getColumnProteins() {
        return COLUMN_PROTEINS;
    }

    public static String getColumnCarbs() {
        return COLUMN_CARBS;
    }

    public static String getColumnFats() {
        return COLUMN_FATS;
    }

    public static String getColumnKcals() {
        return COLUMN_KCALS;
    }
}