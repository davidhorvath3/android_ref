package hu.bme.androidhf.davidhorvath.myshoppinglist.fragment.email;

/**
 * Created by david on 2017.11.14..
 */


/**Interfesz, amin keresztul E-mail kuldeset kerheti az alkalmazas az interfacet implementalo osztalytol*/
public interface SendMailInterface {

    public void shareShoppingList(String mail, String subject);
}

