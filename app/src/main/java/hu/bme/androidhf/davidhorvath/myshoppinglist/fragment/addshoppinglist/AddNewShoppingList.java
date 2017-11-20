package hu.bme.androidhf.davidhorvath.myshoppinglist.fragment.addshoppinglist;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Date;

import hu.bme.androidhf.davidhorvath.myshoppinglist.R;
import hu.bme.androidhf.davidhorvath.myshoppinglist.model.ShoppingList;
import hu.bme.androidhf.davidhorvath.myshoppinglist.model.Thing;

/**
 * Created by david on 2017.11.15..
 */

public class AddNewShoppingList extends DialogFragment {


    /**TAG a Fragment azonositasahoz*/
    public static final String TAG = "AddNewShoppingList";

    /**Attributumok*/
    private AddNewListListener listener;
    private EditText editText;
    private DatePicker datePicker;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**Activity elkerese es castolasa*/
        listener = (AddNewListListener)getActivity();


    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        /**Dialogus letrehozasa, cim, nezet beallitasa*/
        return new AlertDialog.Builder(getContext())
                .setTitle(getResources().getString(R.string.new_item))
                .setView(getContentView())
                .setPositiveButton(getResources().getString(R.string.ok_title),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    DialogInterface dialogInterface, int i) {
                                /**Callback fuggveny hivasa az Activityben, Thing objektum osszeallitasa
                                 adatbazisba beszurashoz*/
                                ShoppingList ret = new ShoppingList();
                                if(!editText.getText().toString().trim().equals(" "))
                                    ret.setListName(editText.getText().toString().trim());
                                else
                                    Toast.makeText(getContext(),getResources().getString(R.string.shopping_list_error), Toast.LENGTH_LONG).show();
                                String date = datePicker.getYear() + "-" + (datePicker.getMonth()+1) + "-" + datePicker.getDayOfMonth();
                                ret.setDeadline(date);
                                listener.shoppingListAdded(ret);
                            }
                        })
                .setNegativeButton(getResources().getString(R.string.cancel_text), null)
                .create();
    }

    private View getContentView() {

        /**View felfujasa a fragment_add_new_item.xml fajlbol*/
        View view = LayoutInflater.from(getContext()).inflate(
                R.layout.fragment_add_new_shoppinglist, null);

        editText = view.findViewById(R.id.addShoppingList);
        datePicker = view.findViewById(R.id.shoppingListDatePicker);



        return view;
    }
}
