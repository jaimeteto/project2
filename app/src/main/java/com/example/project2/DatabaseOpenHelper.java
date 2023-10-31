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
    final public static String PRICE = "price";

    public static String _ID = "_id";
    final public static String NAME = "inventory_db";
    final private static Integer VERSION = 1;
    final private Context context;
    final public static String[] allColumns = { _ID,
            PART_NAME,QTY, CAR_INFO ,PRICE};
    public DatabaseOpenHelper(Context context) {
        super(context, NAME, null,VERSION);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CMD = "CREATE TABLE " + TABLE_NAME + " ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "partName TEXT NOT NULL, "
                + "quantity INTEGER, "
                + "carInfo TEXT, "
                + "price TEXT NOT NULL)";

        db.execSQL(CREATE_CMD);
        // these inserts only run when the database is first created
        ContentValues values = new ContentValues();

        values.put(PART_NAME,"Fender");
        values.put(QTY,2);
        values.put(CAR_INFO,"Honda Accord 2015");
        values.put(PRICE,"100.50");
        db.insert(TABLE_NAME,null,values);
        values.clear();
        values.put(PART_NAME,"Hood");
        values.put(PRICE,"50.00");
        values.put(QTY,3);
        db.insert(TABLE_NAME,null,values);
        values.clear();


    }
    public void insert(String partName, int quantity,String carInfo,String price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PART_NAME, partName);
        values.put(QTY, quantity);
        values.put(CAR_INFO, carInfo);
        values.put(PRICE,price);
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
    public Cursor getPrice(String key){
        Cursor c = null;
        SQLiteDatabase db = this.getWritableDatabase();
        c = db.query(TABLE_NAME, new String[] {PRICE}, "_id = ?", new String[] {key}, null, null, null);

        //       db.close();
        return c;

    }
    public Cursor getQuantity(String key){
        Cursor c = null;
        SQLiteDatabase db = this.getWritableDatabase();
        c = db.query(TABLE_NAME, new String[] {QTY}, "_id = ?", new String[] {key}, null, null, null);

        //       db.close();
        return c;

    }
    public Cursor readAll() {
        Cursor c = null;
        SQLiteDatabase db = this.getWritableDatabase();
        c =  db.query(TABLE_NAME, allColumns, null, new String[] {}, null, null,
                null);
        //       db.close();
        return c;
    }

    public void update(String _id, String carInfo, String qty,String price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if(!price.equals(""))
            values.put(PRICE, price);
        if(!carInfo.equals(""))
            values.put(CAR_INFO, carInfo);

        if(!qty.equals(""))
            values.put(QTY,qty);
        int status = db.update(TABLE_NAME, values, _ID + "=?",
                new String[] { _id });
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
