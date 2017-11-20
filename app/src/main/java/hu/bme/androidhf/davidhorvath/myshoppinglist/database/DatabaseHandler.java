package hu.bme.androidhf.davidhorvath.myshoppinglist.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hu.bme.androidhf.davidhorvath.myshoppinglist.model.ShoppingList;
import hu.bme.androidhf.davidhorvath.myshoppinglist.model.Thing;

/**
 * Created by david on 2017.10.22..
 */

/**Adatbazis kezeleset megvalosito osztaly*/
public class DatabaseHandler {

    /**Atrributumok*/
    SQLiteDatabase database;
    MySQLiteOpenHelper helper;

    /**Konstruktor*/
    public DatabaseHandler(Context context){
        helper = new MySQLiteOpenHelper(context);
    }


    /**A ShoppingListeket tarolo adatbazistablaba valo beszurast vegzi ez a fuggveny*/
    public long listInsert(ShoppingList list){

        /**ContentValues obejtumba kulcs-ertek parokban adjuk meg
         * a beszurando oszlop - ertek parokat*/
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.SHOPPINGLIST_NAME, list.getListName());
        contentValues.put(Constants.SHOPPINGLIST_DEADLINE, list.getDeadline());

        /**Adatbazis peldany elkerese irashoz, majd instert elvegzese es adatbazis bezarasa*/
        database = helper.getWritableDatabase();
        long id = database.insert(Constants.TABLE_SHOPPINGLIST,null,contentValues);
        database.close();

        /**Beszurt elem idval visszateres*/
        return id;

    }

    /**ShoppingListek lekerese az adatbazisbol*/
    public ArrayList<ShoppingList> getShoppingLists(){

        final ArrayList<ShoppingList> list = new ArrayList<>();
        database = helper.getReadableDatabase();

        final Cursor cursor = database.query(Constants.TABLE_SHOPPINGLIST, new String[]{Constants.SHOPPINGLIST_ID, Constants.SHOPPINGLIST_NAME, Constants.SHOPPINGLIST_DEADLINE}, null, null, null, null,null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            final ShoppingList lista = new ShoppingList(cursor.getInt(cursor.getColumnIndex(Constants.SHOPPINGLIST_ID)), cursor.getString(cursor.getColumnIndex(Constants.SHOPPINGLIST_NAME)), cursor.getString(cursor.getColumnIndex(Constants.SHOPPINGLIST_DEADLINE)));
            list.add(lista);
            cursor.moveToNext();
        }

        database.close();

        return list;
    }

    /**Itemek tablajaba valo beszurast vegzo fuggveny*/
    public long itemInsert(Thing thing){

        ContentValues contentValues = new ContentValues();

        contentValues.put(Constants.ITEMS_ITEMNAME, thing.getThingName());
        contentValues.put(Constants.ITEMS_LISTNAME, thing.getListName());
        contentValues.put(Constants.ITEMS_CATEGORY, thing.getCategory());
        if(thing.isBought())
            contentValues.put(Constants.ITEMS_BOUGHT, "true");
        else
            contentValues.put(Constants.ITEMS_BOUGHT, "false");

        database = helper.getWritableDatabase();
        long id = database.insert(Constants.TABLE_ITEMS, null, contentValues);
        database.close();

        return id;

    }

    /**Adott ShoppingListhez tartozo itemek lekerese az adatbazisbol*/
    public ArrayList<Thing> getItemList(String listName){

        final ArrayList<Thing> list = new ArrayList<>();

        database = helper.getReadableDatabase();

        final Cursor cursor = database.query(Constants.TABLE_ITEMS, new String[]{Constants.ITEMS_ID,Constants.ITEMS_LISTNAME,Constants.ITEMS_ITEMNAME,
                Constants.ITEMS_CATEGORY,Constants.ITEMS_BOUGHT}, Constants.ITEMS_LISTNAME + "=?", new String[]{listName},null,null,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            final Thing thing = new Thing();

            thing.setId(cursor.getInt(cursor.getColumnIndex(Constants.ITEMS_ID)));
            thing.setListName(cursor.getString(cursor.getColumnIndex(Constants.ITEMS_LISTNAME)));
            thing.setThingName(cursor.getString(cursor.getColumnIndex(Constants.ITEMS_ITEMNAME)));
            thing.setCategory(cursor.getString(cursor.getColumnIndex(Constants.ITEMS_CATEGORY)));
            thing.setBought(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(Constants.ITEMS_BOUGHT))));


            list.add(thing);

            cursor.moveToNext();
        }
        database.close();

        return list;

    }

    /**Thingek vasarlasi statuszat az adatbazisban frissito fuggveny*/
    public void updateItems(final Thing thing){

        @SuppressLint("StaticFieldLeak") AsyncTask<Void,Void,Void> async = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                ContentValues cv = new ContentValues();

                if(thing.isBought())
                    cv.put(Constants.ITEMS_BOUGHT, "true");
                else
                    cv.put(Constants.ITEMS_BOUGHT, "false");

                database = helper.getWritableDatabase();

                database.update(Constants.TABLE_ITEMS,cv,Constants.ITEMS_ID + "=?", new String[]{String.valueOf(thing.getId())});

                database.close();
                return null;
            }
        };

        async.execute();


    }

    /**Egy adott Thing torlese az Items tablabol*/
    public void deleteItem(final Thing thing){


        @SuppressLint("StaticFieldLeak") AsyncTask<Void,Void,Void> async = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                database = helper.getWritableDatabase();

                database.delete(Constants.TABLE_ITEMS, Constants.ITEMS_ID + "=?", new String[]{String.valueOf(thing.getId())});

                database.close();
                return null;
            }
        };

        async.execute();


    }

    /**Kitorli az osszes bejegyzest az Items tablabol, ami az adott ShoppingListhez tartozik*/
    public void deleteAllItem(String listName){

        database = helper.getWritableDatabase();

        if(listName != null)
            database.delete(Constants.TABLE_ITEMS,Constants.ITEMS_LISTNAME + "=?",new String[]{listName});
        else
            database.delete(Constants.TABLE_ITEMS,null,null);


        database.close();


    }

    /**Kitorli az adott ShoppingListet az adatbazisbol*/
    public void deleteShoppingList(final ShoppingList list){

        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void,Void,Void> async = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                database = helper.getWritableDatabase();

                database.delete(Constants.TABLE_SHOPPINGLIST, Constants.SHOPPINGLIST_ID + " = ?", new String[]{String.valueOf(list.getId())});
                deleteAllItem(list.getListName());

                database.close();

                return null;
            }
        };

        async.execute();

    }


    /**Kitorli az osszes ShoppingListet es a hozzajuk tartozo elemeket az Items tablabol*/
    public void deleteAllShoppingList(){

        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void,Void,Void> async = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                database = helper.getWritableDatabase();

                database.delete(Constants.TABLE_SHOPPINGLIST,null,null);
                deleteAllItem(null);

                database.close();
                return null;
            }
        };

        async.execute();



    }
}
