package hu.elte.pt.store.logic.controllers;

import hu.elte.pt.store.logic.DataSource;
import hu.elte.pt.store.logic.entities.Category;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * A Category táblán való műveletek végrehajtásáért felelős controller osztály.
 * @author Nagy Krisztián
 * @version 0.%I%
 */
public class CategoryController implements EntityController<Category>{
    /**
     * Naplózás elősegítésére szolgáló mező
     */
    private static final Logger log = Logger.getLogger(CategoryController.class);
    /**
     * Az adatbázis tábla nevét tároló mező
     */
    private final String TABLE_NAME = "CATEGORY";
    /**
     * Az aktuális adattáblára vonatkozó kilistázási lekérdezést tartalmazó mező
     */
    private final String FULL_SELECT_SQL = "SELECT * FROM " + TABLE_NAME;
    
    /**
     * Azonosító alapján történő lekérdezés megvalósítása a Category táblára nézve.
     * @param entityId az entitás azonosítója
     * @return az adott azonosítóval rendelkező kategória.
     * @throws java.sql.SQLException a keresési eredmények lekérdezése során fellépő hiba.
     * @see EntityController#getEntityById(int)
     * @see java.sql.SQLException
     */
    @Override
    public Category getEntityById(int entityId) throws SQLException {
        Category category = new Category();
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(FULL_SELECT_SQL + " WHERE CATEGORY_ID = " + entityId);) {
            resultSet.next();
            category.setCategoryId(resultSet.getInt(1));
            category.setName(resultSet.getString(2));
            log.debug("A táblában az azonosító alapján történő keresés sikeres volt. ResultSet: "+ resultSet.getInt(1) + " " + resultSet.getString(2));
        } catch (SQLException ex) {
            log.error("A táblában az ["+ entityId +"] azonosító alapján történő keresés során kivétel keletkezett", ex);
            throw new SQLException("A táblában az ["+ entityId +"] azonosító alapján történő keresés sikertelen volt");
        }
        return category;        
    }

    /**
     * Sor index alapján történő lekérdezés megvalósítása a Category táblára nézve.
     * @param rowIndex sor index
     * @return az adott sorban található kategória.
     * @throws SQLException a keresési eredmények lekérdezése során fellépő hiba.
     * @see EntityController#getEntityByRowIndex(int)
     * @see java.sql.SQLException
     */
    @Override
    public Category getEntityByRowIndex(int rowIndex) throws SQLException {
        Category category = new Category();
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet resultSet = statement.executeQuery(FULL_SELECT_SQL);) {
            resultSet.absolute(rowIndex + 1);
            category.setCategoryId(resultSet.getInt(1));
            category.setName(resultSet.getString(2));
            log.debug("A táblában a sorindex alapján történő keresés sikeres volt. ResultSet: "+ resultSet.getInt(1) + " " + resultSet.getString(2));
        } catch (SQLException ex) {
            log.error("A táblában a sorindex alapján történő keresés során kivétel keletkezett! ", ex);
            throw new SQLException("A táblában a sorindex alapján történő keresés sikertelen volt!");
        }        
        return category;
    }

    /**
     * A kategória táblában található sorok számát lekérdező metódus.
     * @return sorok száma
     * @throws SQLException a lekérdezés során keletkező kivétel. 
     * @see EntityController#getEntityCount() 
     * @see java.sql.SQLException
     */
    @Override
    public int getEntityCount() throws SQLException {
        int categoryCount = 0;
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS CNT FROM CATEGORY");) {
            resultSet.next();
            categoryCount = resultSet.getInt("CNT");
            log.debug("A táblában fellelhető sorok számlálása sikeresen lezajlott " + categoryCount + " értékkel.");
        } catch (SQLException ex) {
            log.error("A táblában fellelhető sorok számlálásának lekérdezése során kivétel keletkezett! ", ex);
            throw new SQLException("A táblában fellelhető sorok számlálásának lekérdezése során kivétel keletkezett!");
        }
        return categoryCount;        
    }

    /**
     * Eljárás amely módosítja a táblában található kategóriát a paraméterben kapott értékek alapján.
     * @param entity a módosult kategóriát tároló objektum
     * @param rowIndex a sorindex, ahol megtalálható
     * @throws SQLException a módosító utasítás futtatása során keletkezett kivétel
     * @see EntityController#updateEntity(hu.elte.pt.store.logic.entities.Entity, int) 
     * @see java.sql.SQLException
     */
    @Override
    public void updateEntity(Category entity, final int rowIndex) throws SQLException {
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet resultSet = statement.executeQuery(FULL_SELECT_SQL);) {
            resultSet.absolute(rowIndex + 1);
            resultSet.updateInt("CATEGORY_ID", entity.getCategoryId());
            resultSet.updateString("NAME", entity.getName());
            resultSet.updateRow();
            log.info("A(z) ("+ entity.getCategoryId() + ") azonosítójú sor sikeresen módosult. Az új NAME: " + entity.getName() + " lett.");
        } catch (SQLException ex) {
            log.error("A táblában található" + entity.getCategoryId() + " azonosíítóval rendelkező sor módosítása során kivétel keletkezett!",ex);
            throw new SQLException("A táblában található" + entity.getCategoryId() + " azonosíítóval rendelkező sor módosítása sikertelen volt!");
        }
    }

    /**
     * Új kategória hozzáadását elősegítő metódus, mely visszaadja annak a sornak az indexét, ahova az új kategória be lett szúrva.
     * @return beszúrt kategória sor indexe
     * @throws SQLException a beszúrás során keletkező kivétel
     * @see EntityController#addNewEntity()
     * @see java.sql.SQLException
     */
    @Override
    public int addNewEntity() throws SQLException {
        String title = "<új kategória neve>";
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet resultSet = statement.executeQuery(FULL_SELECT_SQL);) {
            resultSet.moveToInsertRow();
            resultSet.updateInt("CATEGORY_ID", DataSource.getInstance().obtainNewId());
            resultSet.updateString("NAME", title);
            resultSet.insertRow();
            log.info("Az új kategória létrehozása sikeres volt.");
        } catch (SQLException ex) {
            log.error("A táblán történő új sor létrehozása során kivétel keletkezett! ", ex);
            throw new SQLException("Nem sikerült új sort létrehozni a táblában!");
        }
        
        try{
            return getEntityCount() - 1;
        } catch(SQLException ex){
            log.error("A táblán történő új sor hozzáadása folyamán, az új sor indexének megállapítása közben kivétel váltódott ki!");
            throw new SQLException("A táblán történő új sor hozzáadása folyamán, az új sor indexének megállapítása közben kivétel váltódott ki!");
        }
                
    }
   
    /**
     * Egy meglévő kategória törlését elősegítő eljárás, mely a kapott sorindex alapján kitörli az adott sort az adatbázisból.
     * @param rowIndex sor index
     * @throws SQLException Amennyiben nem található adott sorindexű elem, vagy a törlés sikertelen volt.
     * @see EntityController#deleteEntity(int)
     * @see java.sql.SQLException
     */
    @Override
    public void deleteEntity(int rowIndex) throws SQLException {
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet resultSet = statement.executeQuery(FULL_SELECT_SQL);) {
            resultSet.absolute(rowIndex + 1);
            resultSet.deleteRow();
            log.info("A kiválasztott [ "+ rowIndex+ " ] sorindex-ű elem törlése sikeres volt");
        } catch (SQLException ex) {
            log.error("A kiválasztott [ "+ rowIndex+ " ] sorindex-ű elem törlése során kivétel keletkezett, így a törlés nem valósult meg! ", ex);
            throw new SQLException("A kiválasztott [ "+ rowIndex+ " ] sorindex-ű elem törlése során kivétel keletkezett, így a törlés nem valósult meg!");
        }
    }

    /**
     * Metódus, amely visszaadja az adatbázisban található kategóriákat egy listában.
     * @return kategóriák egy listában
     * @throws SQLException sikertelen lekérdezés esetén
     * @see EntityController#getEntities() 
     * @see java.sql.SQLException
     */
    @Override
    public List<Category> getEntities() throws SQLException {
        List<Category> categories = new ArrayList<>();
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(FULL_SELECT_SQL)) {
            while (rs.next()) {
                Category category = new Category();
                category.setCategoryId(rs.getInt(1));
                category.setName(rs.getString(2));
                categories.add(category);
            }
        }
        return categories;        
    }
    
}
