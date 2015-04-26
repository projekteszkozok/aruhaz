package hu.elte.pt.store.gui.tablemodels;

import javax.swing.event.CellEditorListener;

/**
 * Interfész melyet minden tábla modellnek, melyek entitásokkal foglalkoznak implementálnia kell.
 * @author Nagy Krisztián
 */
public interface EntityHandlerTableModel {
    /**
     * Eljárás, mely segítségével új entitást adhatunk hozzá a táblához
     */
    void addNewEntity();
    
    /**
     * Eljárás, mely segítségével törölhetünk egy entitásnak megfelelő sort az adatbázis táblából.
     * @param rowIndex törölni kívánt entitás sor indexe.
     */
    void deleteEntity(int rowIndex);
    
    CellEditorListener getCellEditorListener();
}
