package hu.bme.androidhf.davidhorvath.myshoppinglist;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import hu.bme.androidhf.davidhorvath.myshoppinglist.database.DatabaseHandler;
import hu.bme.androidhf.davidhorvath.myshoppinglist.fragment.ShoppingListFragment;
import hu.bme.androidhf.davidhorvath.myshoppinglist.fragment.addshoppinglist.AddNewListListener;
import hu.bme.androidhf.davidhorvath.myshoppinglist.fragment.addshoppinglist.AddNewShoppingList;
import hu.bme.androidhf.davidhorvath.myshoppinglist.fragment.addshoppinglist.AddShoppingListItem;
import hu.bme.androidhf.davidhorvath.myshoppinglist.model.ShoppingList;

public class MainActivity extends AppCompatActivity implements AddNewListListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**Bevasarlolista fragment letrehozas, tranzakcio vegrehajtasa, fragment megjelenik
         az onCreate vegen */
        ShoppingListFragment first = new ShoppingListFragment();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.mainFrameLayout, first, ShoppingListFragment.TAG);

        transaction.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        /**Menu felfujasa a menu_main.xml fajlbol*/
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            /**Ha a torles opciot valasztotta*/
            case R.id.deleteAllOption:

                /**Adatbaziskezelo letrehozasa es az osszes elem torlesere hasznalatos fgv hivasa*/
                DatabaseHandler handler = new DatabaseHandler(getApplicationContext());
                handler.deleteAllShoppingList();

                /**A mar megjelenitett ShoppingListFragment elkerese a Managertol
                  es ertesitese az elemek torleserol */
                FragmentManager manager = getSupportFragmentManager();
                ShoppingListFragment fragment = (ShoppingListFragment)manager.findFragmentByTag(ShoppingListFragment.TAG);
                fragment.allDeleted();
                break;
            case R.id.addNewShoppingList:

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                AddNewShoppingList frag = new AddNewShoppingList();
                frag.show(ft,AddNewShoppingList.TAG);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void shoppingListAdded(ShoppingList list) {
        ShoppingListFragment frag = (ShoppingListFragment)getSupportFragmentManager().findFragmentByTag(ShoppingListFragment.TAG);
        frag.addShoppingList(list);
    }
}

