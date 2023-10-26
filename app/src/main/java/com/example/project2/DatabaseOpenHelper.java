package com.example.project2;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
public class DatabaseOpenHelper extends SQLiteOpenHelper{
    final public static String TABLE_NAME = "parts";
    final public static String PART_NAME = "partName";
    final public static String CAR_INFO = "carInfo";
    final public static String QTY = "quantity";

    public static String _ID = "_id";
    final public static String NAME = "inventory_db";
    final private static Integer VERSION = 1;
    final private Context context;
    final public static String[] allColumns = { _ID,
            PART_NAME,QTY, CAR_INFO };
    public DatabaseOpenHelper(Context context) {
        super(context, NAME, null,VERSION);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
//        String CREATE_CMD = "CREATE TABLE " + TABLE_NAME + " (" + _ID +
//                " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + PART_NAME + " TEXT NOT NULL,"+ QTY + "TEXT NOT NULL )";
//        String CREATE_CMD = "CREATE TABLE "+TABLE_NAME+ "("
//                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + "partName TEXT NOT NULL, "
//                + "quantity INTEGER);";
        String CREATE_CMD = "CREATE TABLE " + TABLE_NAME + " ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "partName TEXT NOT NULL, "
                + "quantity INTEGER, "
                + "carInfo TEXT);";

        db.execSQL(CREATE_CMD);
        // these inserts only run when the database is first created
        ContentValues values = new ContentValues();

        values.put(PART_NAME,"Fender");
        values.put(QTY,2);
        values.put(CAR_INFO,"Honda Accord 2015");
        db.insert(TABLE_NAME,null,values);
        values.clear();
        values.put(PART_NAME,"Hood");
        values.put(QTY,3);
        db.insert(TABLE_NAME,null,values);
        values.clear();
//        values.put(PART_NAME,"engine");
//        values.put(QTY,"5");
//        db.insert(TABLE_NAME,null,values);
//        values.clear();
//        values.put(PART_NAME,"tire");
//        values.put(QTY,"0");
//        db.insert(TABLE_NAME,null,values);

    }
    public void insert(String partName, int quantity,String carInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PART_NAME, partName);
        values.put(QTY, quantity);
        values.put(CAR_INFO, carInfo);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void delete(String key) {
        SQLiteDatabase db = this.getWritableDatabase();
        int status =  db.delete(TABLE_NAME, _ID + "=?",
                new String[] { key });
        db.close();
    }
    public void deleteEntries(){
        SQLiteDatabase db = this.getWritableDatabase();
        int status = db.delete(TABLE_NAME,null,null);
        db.close();
    }
    public Cursor readAll() {
        Cursor c = null;
        SQLiteDatabase db = this.getWritableDatabase();
        c =  db.query(TABLE_NAME, allColumns, null, new String[] {}, null, null,
                null);
        //       db.close();
        return c;
    }

    public void update(String oldName, String newName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PART_NAME, newName);
        int status = db.update(TABLE_NAME, values, PART_NAME + "=?",
                new String[] { oldName });
        db.close();
    }

    private void clearAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);


    }

    void deleteDatabase ( ) {
        context.deleteDatabase(NAME);
    }
}
