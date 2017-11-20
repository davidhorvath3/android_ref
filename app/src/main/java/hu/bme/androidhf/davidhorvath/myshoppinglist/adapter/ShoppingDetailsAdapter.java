package hu.bme.androidhf.davidhorvath.myshoppinglist.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import hu.bme.androidhf.davidhorvath.myshoppinglist.R;
import hu.bme.androidhf.davidhorvath.myshoppinglist.database.DatabaseHandler;
import hu.bme.androidhf.davidhorvath.myshoppinglist.model.ShoppingList;
import hu.bme.androidhf.davidhorvath.myshoppinglist.model.Thing;

/**
 * Created by david on 2017.10.22..
 */

public class ShoppingDetailsAdapter extends RecyclerView.Adapter<ShoppingDetailsAdapter.ViewHolder>{

    /**Attributumok, amikre az adapterben szuksegunk lesz*/
    private Context context;
    private ArrayList<Thing> list;
    private DatabaseHandler handler;

    /**Konstruktor*/
    public ShoppingDetailsAdapter(Context context, ArrayList<Thing> list, DatabaseHandler handler) {
        this.context = context;
        this.list = list;
        this.handler = handler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        /**Nezet felfujasa a shopping_list_details_row.xml fajlbol*/
        View v = LayoutInflater.from(context).inflate(R.layout.shopping_list_details_row,parent,false);

        /**A letrehozott viewt atadjuk a ViewHolder konstruktoranak*/
        return new ViewHolder(v);
    }

    /**ViewHolder osszekotese a megjelenitendo adatokkal*/
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        /**Jelenlegi thing elkerese a listatol*/
        final Thing t = list.get(position);

        /**ViewHolder UI elemeinek beallitasa*/
        holder.image.setImageDrawable(getIcon(t));
        holder.thingName.setText(t.getThingName());
        holder.categoryName.setText(t.getCategory());
        holder.isBought.setChecked(t.isBought());

        /**Ha a bought CheckBox valtozik, updateljuk az adatbazist is az uj checked erteknek megfeleloen*/
        holder.isBought.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                t.setBought(isChecked);
                handler.updateItems(t);
            }
        });

        /**Ha hosszan nyom a felhasznalo egy listaelemre, akkor azt toroljuk az adatbazisbol es a listabol is*/
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                handler.deleteItem(t);
                list.remove(t);
                notifyItemRemoved(position);

                return true;
            }
        });

    }

    /**Listaban tarolt elemek szamat adja vissza*/
    @Override
    public int getItemCount() {
        return list.size();
    }

    /**ViewHolder osztaly*/
    public class ViewHolder extends RecyclerView.ViewHolder {

        /**ViewHolder altal tarolt UI elemek*/
        ImageView image;
        TextView thingName;
        TextView categoryName;
        CheckBox isBought;

        public ViewHolder(View itemView) {
            super(itemView);

            /**Referenciak elkeresek*/
            image = (ImageView) itemView.findViewById(R.id.thingImageView);
            thingName = (TextView) itemView.findViewById(R.id.itemName);
            categoryName = (TextView) itemView.findViewById(R.id.itemType);
            isBought = (CheckBox) itemView.findViewById(R.id.itemBought);
        }
    }

    /**Visszaadja az adott Thing kategoriajanak megfelelo icont*/
    public Drawable getIcon(Thing t){

        switch(t.getCategory()){
            case "Food":
                return context.getResources().getDrawable(R.drawable.food);

            case "Electronic":
                return context.getResources().getDrawable(R.drawable.electrical);

            case "Book":
                return context.getResources().getDrawable(R.drawable.book);

            case "Drink":
                return context.getResources().getDrawable(R.drawable.drink);

            case "Game":
                return context.getResources().getDrawable(R.drawable.game);

            case "House":
                return context.getResources().getDrawable(R.drawable.house);

            default:
                return context.getResources().getDrawable(R.drawable.food);

        }


    }
}
