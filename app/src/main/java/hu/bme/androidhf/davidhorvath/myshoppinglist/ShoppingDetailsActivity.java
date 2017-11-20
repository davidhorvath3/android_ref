package hu.bme.androidhf.davidhorvath.myshoppinglist;


import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import hu.bme.androidhf.davidhorvath.myshoppinglist.adapter.ShoppingDetailsAdapter;
import hu.bme.androidhf.davidhorvath.myshoppinglist.adapter.ShoppingListAdapter;
import hu.bme.androidhf.davidhorvath.myshoppinglist.database.DatabaseHandler;
import hu.bme.androidhf.davidhorvath.myshoppinglist.fragment.addshoppinglist.AddItemListener;
import hu.bme.androidhf.davidhorvath.myshoppinglist.fragment.addshoppinglist.AddShoppingListItem;
import hu.bme.androidhf.davidhorvath.myshoppinglist.fragment.email.SendMailFragment;
import hu.bme.androidhf.davidhorvath.myshoppinglist.fragment.email.SendMailInterface;
import hu.bme.androidhf.davidhorvath.myshoppinglist.model.Thing;

public class ShoppingDetailsActivity extends AppCompatActivity implements AddItemListener, SendMailInterface {

    /**Attributumok*/
    private RecyclerView recyclerView;
    private ShoppingDetailsAdapter adapter;
    private ArrayList<Thing> list;
    private DatabaseHandler handler;

    /**String amiben taroljuk az adott Activityt elindito Activity altal atadott informaciot arrol,
     * hogy melyik lista elemeit kell megjelenitenunk*/
    private String listName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_details);

        /**Referencia a toolbarra es beallitasa supportactionbarkent*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**Az extrakat tartalmazo Bundle elkerese az Activityt
         * elindito Intenttol
         */
        Bundle b = getIntent().getExtras();

        /**Ha a Bundle nem ures, akkor kiszedjuk belole a lista nevet*/
        if(b != null)
            listName = b.getString(ShoppingListAdapter.KEY);

        /**FloatingActionButton referenciajanak elkerese es OnClickListener
         * beallitasa
         */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**A gomb megnyomasakor elinditjuk az E-mail kuldesere szolgalo
                 dialogfragmentet*/
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                SendMailFragment fragment = new SendMailFragment();
                fragment.show(ft,"send mail");
            }
        });

        /**RecyclerView referenciajanak elkerese es LayoutManager beallitasa*/
        recyclerView = (RecyclerView) findViewById(R.id.detailsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /**Lista letrehozasa*/
        list = new ArrayList<>();

        /**Adatbaziskezelo letrehozasa*/
        handler = new DatabaseHandler(getApplicationContext());

        /**AsyncTask az adatbazismuvelethez, itt nyerjuk ki az adatbazisbol a lista tartalmat,
         * amit meg kell jelenitenunk
         */
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, ArrayList<Thing>> asyncTask = new AsyncTask<Void, Void, ArrayList<Thing>>() {

            @Override
            protected ArrayList<Thing> doInBackground(Void... voids) {
                return handler.getItemList(listName);
            }

            @Override
           protected void onPostExecute(ArrayList<Thing> things) {
                /**Miutan megkaptuk egy listaban az elemeket
                 * utana letrehozunk egy adaptert a RecyclerViewhoz es beallitjuk
                 * adapterenek a letrehozott adaptert, aki megkapja a megjelenitendo listat.
                 */
                list = things;
                adapter = new ShoppingDetailsAdapter(getApplicationContext(),list,handler);
                recyclerView.setAdapter(adapter);
            }
       };

        /**AsyncTask elinditasa*/
        asyncTask.execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        /**Menu felfujasa a menu_main.xml fajlbol*/
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_details,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            /**Ha a hozzaadas opciot valasztotta*/
            case R.id.addNewItem:
                /**Fragment letrehozasa, amiben megkapjuk az adatokat az uj item letrehozasahoz*/
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                AddShoppingListItem fragment = new AddShoppingListItem();
                fragment.show(ft,"add item");
                break;
        }

        return true;
    }

    /**Callback fuggveny, amit a Fragment hiv meg, aki megkapja az uj elem
     * hozzaadasahoz szukseges adatokat es azt atadja egy Thing objectkent a fuggvenyunknek
     */
    @Override
    public void thingAdded(final Thing thing) {

        /**Az item listajanak beallitasa*/
        thing.setListName(listName);

        /**Item beszurasa az adatbazisba es a megkapodd id beallitasa*/
        long id = handler.itemInsert(thing);
        thing.setId(Integer.valueOf(Long.toString(id)));

        /**Thing hozzaadasa a tarolohoz, adapter ertesitese, hogy beszuras tortent*/
        list.add(thing);
        adapter.notifyItemInserted(list.size()-1);

    }

    /**Callback fuggveny, amit a Fragment hiv meg, aki megkapja az E-mail kuldesehez
     * szukseges adatokat es azokat atadja Stringekkent a fuggvenyunknek
     */
    @Override
    public void shareShoppingList(String mail, String subject){

        StringBuilder data = new StringBuilder();

        /**Ciklus, amiben az elkuldendo listaelemeket hozzaadjuk
         * a StringBuildeunkhoz, aki vegul majd letrehozza az elkuldendo uzenetet
         */
        for(int i = 0; i < list.size(); i++){
            String name = list.get(i).getThingName();
            String category = list.get(i).getCategory();

            data.append(i+1 + getResources().getString(R.string.thing_text) + " \n");
            data.append(getResources().getString(R.string.item_text) + name + "\n");
            data.append(getResources().getString(R.string.category_text) + category + "\n");
        }

        /**Implicit Intent beallitasai a megfelelo atvett parameterek szerint*/
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_SUBJECT,subject);
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{mail});
        i.putExtra(Intent.EXTRA_TEXT,data.toString());

        try{
            /**Implicit Intent kikuldese*/
            startActivity(Intent.createChooser(i,getResources().getString(R.string.email_title)));
        }catch(ActivityNotFoundException e){
            /**Hibauzenet*/
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.mail_error),Toast.LENGTH_LONG).show();
        }
    }
}

