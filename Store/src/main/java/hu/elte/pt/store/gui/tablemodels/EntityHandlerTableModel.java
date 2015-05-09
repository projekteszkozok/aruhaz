package hu.elte.pt.store.gui.tablemodels;

import javax.swing.event.CellEditorListener;

/**
 * Interfész melyet minden tábla modellnek, melyek entitásokkal foglalkoznak
 * implementálnia kell.
 *
 * @author Nagy Krisztián
 * @version 1.%I%
 */
public interface EntityHandlerTableModel {

    /**
     * Eljárás, mely segítségével új entitást adhatunk hozzá a táblához
     */
    void addNewEntity();

    /**
     * Eljárás, mely segítségével törölhetünk egy entitásnak megfelelő sort az
     * adatbázis táblából.
     *
     * @param rowIndex törölni kívánt entitás sor indexe.
     */
    void deleteEntity(int rowIndex);

    /**
     * Metódus, mellyel le tudjuk kérdezni az aktuális táblamodel-hez tartozó
     * CellEditorListener-t.
     *
     * @return a táblamodel-hez tartozó CellEditorListener
     */
    CellEditorListener getCellEditorListener();
}
