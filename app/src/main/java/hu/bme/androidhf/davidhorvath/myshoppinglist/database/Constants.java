package hu.bme.androidhf.davidhorvath.myshoppinglist.database;

/**
 * Created by david on 2017.10.22..
 */

public class Constants {

    /**Konstansok az adatbazis kezelesehez
     * Tobbek kozt az adatbazis neve, verzio szama, tablak es oszlopaik neve
     * igy innen lehet rajuk hivatkozni barhonnan az alkalmazasbol.
     */
    public static final String  DATABASE_NAME = "myshoppinglist.db";
    public static final int  DATABASE_VERSION = 1;
    public static final String TABLE_SHOPPINGLIST = "SHOPPINGLIST";
    public static final String SHOPPINGLIST_ID = "_id";
    public static final String SHOPPINGLIST_NAME = "listname";
    public static final String SHOPPINGLIST_DEADLINE = "deadline";
    public static final String TABLE_ITEMS = "ITEMS";
    public static final String ITEMS_ID = "itemid";
    public static final String ITEMS_LISTNAME = "listname";
    public static final String ITEMS_ITEMNAME = "itemname";
    public static final String ITEMS_BOUGHT = "bought";
    public static final String ITEMS_CATEGORY = "category";
}
