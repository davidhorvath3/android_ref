package hu.bme.androidhf.davidhorvath.myshoppinglist.fragment.email;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;


import hu.bme.androidhf.davidhorvath.myshoppinglist.R;

/**
 * Created by david on 2017.11.14..
 */

public class SendMailFragment extends DialogFragment{

    /**TAG a Fragment azonositasahoz*/
    public static final String TAG = "SendEmail";

    /**Attributumok*/
    private EditText mail;
    private EditText subject;
    private SendMailInterface act;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**Activity lekerese es castolasa*/
        act = (SendMailInterface)getActivity();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        /**Dialogus letrehozasa, cim, nezet beallitasa*/
        return new AlertDialog.Builder(getContext())
                .setTitle(getResources().getString(R.string.email_title))
                .setView(getContentView())
                .setPositiveButton(getResources().getString(R.string.ok_title),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    DialogInterface dialogInterface, int i) {
                                /**Meghivja az Activity megfelelo fuggvenyet, ami majd elkuldi magat az E-mailt*/
                                        act.shareShoppingList(mail.getText().toString().trim(),subject.getText().toString().trim());

                            }
                        })
                .setNegativeButton(getResources().getString(R.string.cancel_text), null)
                .create();
    }

    private View getContentView() {
        /**View felfujasa a fragment_send_mail.xml fajlbol*/
        View view = LayoutInflater.from(getContext()).inflate(
                R.layout.fragment_send_mail, null);

        /**Referenciak elkerese a UI elemekre*/
        mail = view.findViewById(R.id.mailAddress);
        subject = view.findViewById(R.id.subject);


        return view;
    }
}
