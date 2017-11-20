package hu.bme.androidhf.davidhorvath.myshoppinglist.adapter;

import android.annotation.SuppressLint;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.IntentFilter;

import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;

import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;



import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;


import hu.bme.androidhf.davidhorvath.myshoppinglist.R;
import hu.bme.androidhf.davidhorvath.myshoppinglist.ShoppingDetailsActivity;
import hu.bme.androidhf.davidhorvath.myshoppinglist.database.DatabaseHandler;
import hu.bme.androidhf.davidhorvath.myshoppinglist.model.ShoppingList;


/**
 * Created by david on 2017.10.19..
 */

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ListViewHolder>  {

    /**Kulcs, amit az Intent extrajakent kulcs erteknek adunk at*/
    public static final String KEY = "listname";

    /**Attributumok, amikre az adapterben szuksegunk lesz*/
    private Context context;
    private ArrayList<ShoppingList> list;
    private DatabaseHandler handler;
    private IntentFilter myFilter;

    /**Konstruktor*/
    public ShoppingListAdapter(Context context, ArrayList<ShoppingList> list, DatabaseHandler handler) {
        this.context = context;
        this.list = list;
        this.handler = handler;
        myFilter = new IntentFilter("MY_ACTION");
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /**Nezet felfujasa a shopping_list_row.xml fajlbol*/
        View v = LayoutInflater.from(context).inflate(R.layout.shopping_list_row,parent,false);

        /**A letrehozott viewt atadjuk a ViewHolder konstruktoranak*/
        return new ListViewHolder(v);
    }

    /**ViewHolder osszekotese a megjelenitendo adatokkal*/
    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(ListViewHolder holder, final int position) {

        /**A position szerinti ShoppingList elkerese a listatol*/
        final ShoppingList item = list.get(position);

        /**A ShoppingList nevenek kiiratasa a TextViewra*/
        holder.shoppingList.setText(item.getListName());

        /**Aktualis datum lekerese*/
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        /**Datum ellenorzese, ha megegyezik az elem Deadlineja a mai datummal
         * Broadcast Itentet kuld ki a rendszer, amit egy Receiver fogad
         */
        if(item.getDeadline().equals(date)) {
            Intent intent = new Intent("my-event");
            intent.putExtra("message", item.getListName());
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }


        /**Deadline kiirasa a TextViewra*/
        holder.date.setText(item.getDeadline());

        /**OnClickListener a teljes viewn*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**Ha rányom a felhasznalo a listaelemre, elindul egy uj Activity,
                 * ahol a lista elemei jelennek meg
                 */
                Intent i = startDetailsActivity(item);
                context.startActivity(i);
            }
        });

        /**OnLongClickListener a viewra*/
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                /**Ha a felhasznalo hosszan nyom ra a listaelemre,
                 * feldobodik egy AlertDialog, aminek a pozitiv gombja kitorli
                 * a kivalasztott listaelemet az adatbazisbol es a listabol is
                 */
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(context.getResources().getString(R.string.sure_dialog));
                builder.setCancelable(false);
                builder.setPositiveButton(context.getResources().getString(R.string.delete_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.deleteShoppingList(item);
                        list.remove(item);
                        notifyItemRemoved(position);
                    }
                });
                builder.setNegativeButton(context.getResources().getString(R.string.cancel_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return true;
            }
        });
    }

    /**Listaban tarolt elemek szamat adja vissza*/
    @Override
    public int getItemCount() {
        return list.size();
    }







    /**Fuggveny az DetailsActivity inditasanak elokeszitesehez*/
    public Intent startDetailsActivity(ShoppingList item){
        Intent i = new Intent(context, ShoppingDetailsActivity.class);
        i.putExtra(ShoppingListAdapter.KEY, item.getListName());

        return i;
    }

    /**ViewHolder osztaly egy TextViewval*/
    public class ListViewHolder extends RecyclerView.ViewHolder {

        TextView shoppingList;
        TextView date;

        public ListViewHolder(View itemView) {
            super(itemView);

            shoppingList = (TextView) itemView.findViewById(R.id.shoppingListName);
            date = (TextView) itemView.findViewById(R.id.shoppingListDeadline);

        }
    }
}
