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
 */
public class CategoryController implements EntityController<Category>{
    private static final Logger log = Logger.getLogger(CategoryController.class);
    private final String TABLE_NAME = "CATEGORY";
    private final String FULL_SELECT_SQL = "SELECT * FROM " + TABLE_NAME;
    
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
