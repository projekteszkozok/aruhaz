package hu.elte.pt.store.logic.controllers;

import hu.elte.pt.store.logic.entities.Entity;
import java.sql.SQLException;

/**
 * Az entitásokhoz tartozó controller-ek megvalósítják ezt az interfészt.
 * Az interfész leírja a controllerek megvalósítandó metódusait.
 * @author Nagy Krisztián
 */
interface EntityController <E extends Entity>{
    
    public E getEntityById(int entityId) throws SQLException;
    
    public E getEntityByRowIndex(int rowIndex) throws SQLException;
    
    public int getEntityCount() throws SQLException;
    
    public void updateEntity(E entity) throws SQLException;
    
    public int addNewEntity() throws SQLException;
    
    public void deleteEntity(int rowIndex) throws SQLException;    
}
