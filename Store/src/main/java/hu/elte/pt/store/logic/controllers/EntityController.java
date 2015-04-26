package hu.elte.pt.store.logic.controllers;

import hu.elte.pt.store.logic.entities.Entity;
import java.sql.SQLException;
import java.util.List;

/**
 * Az entitásokhoz tartozó controller-ek megvalósítják ezt az interfészt.
 * Az interfész leírja a controllerek megvalósítandó metódusait.
 * @author Nagy Krisztián
 * @param <E> Entitás, mely implementálja az Entity interfészt.
 */
public interface EntityController <E extends Entity>{
    /**
     * Egy olyan metódus, amely a paraméterben megadott azonosító alapján visszaadja a megfelelő entitást az adatbázisból.
     * @param entityId az entitás azonosítója, ami alapján keresni szeretnénk. Az entitáshoz tartozó tábla elsődleges kulcsú attribútumára vonatkozó keresés. 
     * @return a keresett entitás, amennyiben volt ilyen. 
     * @throws SQLException az entitás keresése során kiváltódó kivétel.
     */
    public E getEntityById(int entityId) throws SQLException;
    
    /**
     * Egy olyan metódus, amely a paraméterben kapott sor index alapján visszaadja a megfelelő entitást az adatbázisból.
     * @param rowIndex annak a sor indexe, amely a kiválasztott adatot tartalmazza.
     * @return a keresett entitás, amennyiben volt ilyen.
     * @throws SQLException az entitás keresése során kiváltódó kivétel.
     */
    public E getEntityByRowIndex(int rowIndex) throws SQLException;
    
    /**
     * Metódus, mely visszaadja, hogy az adott típusú entitásból hány sor található az adatbázisban.
     * @return sorok száma az adatbázisban
     * @throws SQLException az entitás kezelése során kiváltódó kivétel.
     */
    public int getEntityCount() throws SQLException;
    
    /**
     * Eljárás, melynek segítségével módosíthatunk az adatbázisban valamilyen adatot, a paraméterként megadott entitást felhasználva.
     * @param entity módosított entitás
     * @param rowIndex a módosítani kívánt sor indexe a táblázatban
     * @throws SQLException sikertelen módosítás esetén kiváltodó kivétel.
     */
    public void updateEntity(E entity, int rowIndex) throws SQLException;
    
    /**
     * Metódus, mely létrehoz egy új entitásnak megfelelő sort az adatbázisban és visszaadja, hogy melyik index-re lett beszúrva az adott adat.
     * @return a beszúrás helye.
     * @throws SQLException az entitás kezelése során kiváltódó kivétel.
     */
    public int addNewEntity() throws SQLException;
    
    /**
     * Eljárás, melynek segítségével törölhetünk egy sort az adatbázisból.
     * @param rowIndex sor index mely megmondja, hogy melyik adatot szeretnénk törölni.
     * @throws SQLException sikertelen törlés esetén kiváltódó kivétel.
     */
    public void deleteEntity(int rowIndex) throws SQLException;    
    
    /**
     * Metódus, mely visszaadja az összes sort egy entitásokból álló listában az adatbázis-táblából.
     * @return lista a tábla összes entitásáról
     * @throws SQLException sikertelen adatbázisművelet esetén kiváltódó kivétel
     */
    public List<E> getEntities() throws SQLException;
}
