package hu.bme.androidhf.davidhorvath.myshoppinglist.fragment;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import hu.bme.androidhf.davidhorvath.myshoppinglist.R;
import hu.bme.androidhf.davidhorvath.myshoppinglist.ShoppingDetailsActivity;
import hu.bme.androidhf.davidhorvath.myshoppinglist.adapter.ShoppingDetailsAdapter;
import hu.bme.androidhf.davidhorvath.myshoppinglist.adapter.ShoppingListAdapter;
import hu.bme.androidhf.davidhorvath.myshoppinglist.database.DatabaseHandler;
import hu.bme.androidhf.davidhorvath.myshoppinglist.model.ShoppingList;
import hu.bme.androidhf.davidhorvath.myshoppinglist.model.Thing;

/**
 * Created by david on 2017.10.19..
 */

public class ShoppingListFragment extends Fragment{

    /**TAG a fragment azonositasahoz*/
    public static final String TAG = "ShoppingListFragment";

    /**Valtozok, amik taroljak a hasznalni kivant UI elemek es egyeb osztalyok referenciajat*/
    private RecyclerView recyclerView;
    private ShoppingListAdapter adapter;
    private ArrayList<ShoppingList> list;
    private DatabaseHandler handler;

    /**Id a Notificationok kuldesehez*/
    private int id = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /**Nezet felfujasa a fragment_shopping_list.xml fajlbol*/
        View v = inflater.inflate(R.layout.fragment_shopping_list,container,false);

        /**RecyclerView referenciajanak elkerese, LayoutManagerenek beallitasa*/
        recyclerView = (RecyclerView)v.findViewById(R.id.shoppingRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



        /**Adatbaziskezelo letrehozasa*/
        handler = new DatabaseHandler(getContext());


        /**AsyncTask az adatbazismuvelethez, itt nyerjuk ki az adatbazisbol az alkalmazasunk altal
         * tarolt osszes bevasarlolistat
         */
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, ArrayList<ShoppingList>> asyncTask = new AsyncTask<Void, Void, ArrayList<ShoppingList>>() {
            @Override
            protected ArrayList<ShoppingList> doInBackground(Void... voids) {
                return handler.getShoppingLists();
            }

            @Override
            protected void onPostExecute(ArrayList<ShoppingList> lista) {
                /**Miutan megkaptuk egy listaban a bevasarlolistakat
                 * utana letrehozunk egy adaptert a RecyclerViewhoz es beallitjuk
                 * adapterenek a letrehozott adaptert, aki megkapja a megjelenitendo listat.
                 */
                list = lista;
                adapter = new ShoppingListAdapter(getActivity(),list,handler);
                recyclerView.setAdapter(adapter);
            }
        };

        /**AsyncTask futtatasa*/
        asyncTask.execute();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        /**BroadcastReceiver feliratkozasa*/
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("my-event"));
    }

    /**BroadcastReceiver a broadcast Intent kezelesehez*/
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            /**Elkeri a lista nevet at Intenttol*/
            String listn = intent.getStringExtra("message");

            /**Notification dobasa*/
            showNotification(listn);

        }
    };

    @Override
    public void onPause() {

        /**Leiratkozas*/
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }




    /**Fuggveny uj bevasarlolista felvetelehez*/
    public void addShoppingList(ShoppingList lista){

        /**A beszurando lista nevet megkapjuk a fuggveny parameterekent
         * majd ezzel a nevvel letrehozunk egy uj ShoppingList objectet
         * amit aztan atadunk az adatbaziskezelonek az adatbazisba valo beszurashoz
         * aki visszaadja nekunk a beszurt lista idjat
         */

        long id = handler.listInsert(lista);

        /**A megkapott id beallitasa az objectnek*/
        lista.setId(Integer.valueOf(Long.toString(id)));

        /**Az uj ShoppingList hozzaadasa a tarolohoz es
         * adapter ertesitese, hogy beszurad tortent az adathalmazba
         */
        list.add(lista);
        adapter.notifyItemInserted(list.size()-1);

    }

    /**Callback fuggveny, amit a Fragment Activityje fog meghivni
     * miutan kitorolte az osszes elemet a listabol
     */
    public void allDeleted() {

        /**Ertesiti a fuggveny az adaptert, hogy kitoroltek az osszes elemet
         * a listabol, illetve kiuriti a tarolot, amiben a ShoppingListeket taroltuk
         */
        adapter.notifyItemRangeRemoved(0,list.size());
        list.clear();

    }



    public void showNotification(String listName){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext());
        builder.setSmallIcon(R.drawable.food);
        builder.setContentTitle(listName);
        builder.setContentText(getResources().getString(R.string.notification_message));

        Intent i = new Intent(getContext(), ShoppingDetailsActivity.class);
        i.putExtra(ShoppingListAdapter.KEY, listName);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getContext());
        stackBuilder.addParentStack(ShoppingDetailsActivity.class);
        stackBuilder.addNextIntent(i);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        @SuppressLint("ServiceCast")
        NotificationManager manager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(id,builder.build());
        id++;
    }
}
