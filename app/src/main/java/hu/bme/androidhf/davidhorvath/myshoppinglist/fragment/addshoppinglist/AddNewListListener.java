package hu.bme.androidhf.davidhorvath.myshoppinglist.fragment.addshoppinglist;

/**
 * Created by david on 2017.11.15..
 */

import hu.bme.androidhf.davidhorvath.myshoppinglist.model.ShoppingList;

/**Interfesz, amin keresztul uj ShoppingList adatbazishoz adasat kerheti az alkalmazas az interfacet implementalo osztalytol*/
public interface AddNewListListener {

    void shoppingListAdded(ShoppingList list);
}
