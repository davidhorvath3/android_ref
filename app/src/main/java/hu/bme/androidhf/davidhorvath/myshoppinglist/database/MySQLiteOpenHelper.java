package hu.bme.androidhf.davidhorvath.myshoppinglist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by david on 2017.10.22..
 */

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    /**Elso adatbazistabla letrehozasahoz a create query
     * ket oszloppal, egyik egy ID masik pedig a lista neve
     */
    private static final String CREATE_SHOPPINGLIST = "CREATE TABLE " + Constants.TABLE_SHOPPINGLIST + "(" +
            Constants.SHOPPINGLIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Constants.SHOPPINGLIST_NAME + " TEXT, " +
            Constants.SHOPPINGLIST_DEADLINE + " TEXT" + ");";


    /**Masodik adatbazistabla letrehozasahoz a create query
     * ot oszloppal, egyik egy ID az adott elemhez, majd taroljuk rola, hogy melyik listahoz tartozik
     * amire egy FOREIGN KEY constraintet is letrehozunk kesobb, ami a masik table ID oszlopa,
     * ezutan taroljuk az elem kategoriajat, nevet es a vasarlas allapotat
     */
    private static final String CREATE_ITEMS = "CREATE TABLE " + Constants.TABLE_ITEMS + "(" +
            Constants.ITEMS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Constants.ITEMS_LISTNAME + " TEXT, " +
            Constants.ITEMS_CATEGORY + " TEXT, " +
            Constants.ITEMS_ITEMNAME + " TEXT, " +
            Constants.ITEMS_BOUGHT + " INTEGER, " +
            "FOREIGN KEY(" + Constants.ITEMS_LISTNAME + ")" + " REFERENCES " + Constants.TABLE_SHOPPINGLIST + "(" + Constants.SHOPPINGLIST_NAME + ")" + ");";


    /**Konstruktor*/
    public MySQLiteOpenHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        /**Create queryk futtatasa*/
        db.execSQL(CREATE_SHOPPINGLIST);
        db.execSQL(CREATE_ITEMS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        /**Tablak torlese ha leteznek, majd ujbol letrehozzuk oket*/
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_SHOPPINGLIST);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_ITEMS);

        onCreate(db);

    }
}
