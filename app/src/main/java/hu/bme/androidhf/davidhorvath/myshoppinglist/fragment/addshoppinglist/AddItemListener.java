package hu.bme.androidhf.davidhorvath.myshoppinglist.fragment.addshoppinglist;

import hu.bme.androidhf.davidhorvath.myshoppinglist.model.Thing;

/**
 * Created by david on 2017.10.19..
 */

/**Interfesz, amin keresztul uj Thing adatbazishoz adasat kerheti az alkalmazas az interfacet implementalo osztalytol*/
public interface AddItemListener {

    void thingAdded(final Thing thing);
}
