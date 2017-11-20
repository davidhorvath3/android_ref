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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import hu.bme.androidhf.davidhorvath.myshoppinglist.R;
import hu.bme.androidhf.davidhorvath.myshoppinglist.model.Thing;

/**
 * Created by david on 2017.10.19..
 */

public class AddShoppingListItem extends DialogFragment {

    /**TAG a Fragment azonositasahoz*/
    public static final String TAG = "AddShoppingListItem";

    /**Attributumok*/
    private AddItemListener listener;
    private EditText editText;
    private Spinner spinner;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**Activity elkerese es castolasa*/
        listener = (AddItemListener)getActivity();


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
                                Thing ret = new Thing();
                                ret.setThingName(editText.getText().toString().trim());
                                ret.setCategory(spinner.getSelectedItem().toString());
                                ret.setBought(false);
                                listener.thingAdded(ret);

                            }
                        })
                .setNegativeButton(getResources().getString(R.string.cancel_text), null)
                .create();
    }

    private View getContentView() {

        /**View felfujasa a fragment_add_new_item.xml fajlbol*/
        View view = LayoutInflater.from(getContext()).inflate(
                R.layout.fragment_add_new_item, null);

        /**Referenciak elkerese a UI elemekre*/
        editText = (EditText) view.findViewById(R.id.shoppingListEditText);
        spinner = (Spinner)view.findViewById(R.id.categorySpinner);

        /**Adapter letrehozasa es tulajdonsagainak beallitasa*/
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        /**Adapter hozzaadasa a Spinnerhez*/
        spinner.setAdapter(adapter);


        return view;
    }
}
