package hu.elte.pt.store.logic.controllers;

import hu.elte.pt.store.logic.DataSource;
import hu.elte.pt.store.logic.entities.Category;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import org.apache.log4j.Logger;

/**
 * A Category táblán való műveletek végrehajtásáért felelős controller osztály.
 * @author Nagy Krisztián
 */
public class CategoryController implements EntityController<Category>{
    private static final Logger log = Logger.getLogger(CategoryController.class);
    
    @Override
    public Category getEntityById(int entityId) {
        Category category = new Category();
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM CATEGORY WHERE CATEGORY_ID = " + entityId);) {
            resultSet.next();
            category.setCategoryId(resultSet.getInt(1));
            category.setName(resultSet.getString(2));
        } catch (SQLException ex) {
            log.error("A táblában az ["+ entityId +"] azonosító alapján történő keresés során kivétel keletkezett", ex);
        }
        return category;        
    }

    @Override
    public Category getEntityByRowIndex(int rowIndex) {
        Category category = new Category();
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet resultSet = statement.executeQuery("SELECT * FROM CATEGORY");) {
            resultSet.absolute(rowIndex + 1);
            category.setCategoryId(resultSet.getInt(1));
            category.setName(resultSet.getString(2));
        } catch (SQLException ex) {
            log.error("A táblában a sorindex alapján történő keresés során kivétel keletkezett! ", ex);
        }        
        return category;
    }

    @Override
    public int getEntityCount() {
        int categoryCount = 0;
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS CNT FROM CATEGORY");) {
            resultSet.next();
            categoryCount = resultSet.getInt("CNT");
        } catch (SQLException ex) {
            log.error("A táblában fellelhető sorok számlálásának lekérdezése során kivétel keletkezett! ", ex);
        }
        return categoryCount;        
    }

    @Override
    public void updateEntity(Category entity) {
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                ResultSet resultSet = statement.executeQuery("SELECT * FROM CATEGORY WHERE CATEGORY_ID = " + entity.getCategoryId());) {
            resultSet.next();
            resultSet.updateString("NAME", entity.getName());
            resultSet.updateRow();
        } catch (SQLException ex) {
            log.error("A táblában található" + entity.getCategoryId() + "-val rendelkező sor módosítása során kivétel keletkezett!",ex);
        }
    }

    @Override
    public int addNewEntity() {
        String title = "<new category>";
        int id = new Random().nextInt();
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet resultSet = statement.executeQuery("SELECT * FROM CATEGORY");) {
            resultSet.moveToInsertRow();
            resultSet.updateInt("CATEGORY_ID", id);
            resultSet.updateString("NAME", title);
            resultSet.insertRow();
        } catch (SQLException ex) {
            log.error("A táblán történő új sor létrehozása során kivétel keletkezett! ", ex);
        }
        return getEntityCount() - 1;        
    }

    @Override
    public void deleteEntity(int rowIndex) {
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet resultSet = statement.executeQuery("SELECT * FROM CATEGORY");) {
            resultSet.absolute(rowIndex + 1);
            resultSet.deleteRow();

        } catch (SQLException ex) {
            log.error("A kiválasztott [ "+ rowIndex+ " ] sorindex-ű elem törlése során kivétel keletkezett, így a törlés nem valósult meg! ", ex);
        }
    }
    
}
