package hu.elte.pt.store.logic;

import hu.elte.pt.store.logic.controllers.CategoryController;
import hu.elte.pt.store.logic.entities.Category;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Singleton pattern szerint elkészített osztály, mely az adatbázis kapcsolat és az entitásokhoz tartozó controllerek lekérésére szolgál.
 * @author Nagy Krisztián
 */
public class DataSource {
    
    private final StoreConfiguration config;
    private static final Logger log = Logger.getLogger(DataSource.class);
    
    private final CategoryController categoryController;
    
    private DataSource(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("store-config.xml");
        config = (StoreConfiguration) context.getBean("StoreConfiguration");
        final StringBuilder logMessageBuilder = new StringBuilder()
                .append("Az adatbázis konfiguráció az alábbi értékekkel betöltődött: ")
                .append("[CONNECTION : ").append(config.getConnectionString())
                .append(" , USER: ").append(config.getUserName())
                .append(" , PASSWORD: ").append(config.getPassword()).append("]");
        log.info(logMessageBuilder.toString());
        
        categoryController = new CategoryController(); 
    }
    
    public Connection getConnection() throws SQLException{
        return DriverManager.getConnection(config.getConnectionString(), config.getUserName(), config.getPassword());
    }

    public CategoryController getCategoryController() {
        return categoryController;
    }
    
    public static DataSource getInstance(){
        return DataSourceHolder.INSTANCE;
    }
    
    private static class DataSourceHolder{
        private static final DataSource INSTANCE = new DataSource();
    }
    
    public int obtainNewId() throws SQLException {
        int id;
        try (
                Connection connection = getConnection();
                Statement stmt = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = stmt.executeQuery("SELECT VALUUE FROM SEQ")) {
            rs.next();
            id = rs.getInt("VALUUE") + 1;
            rs.updateInt("VALUUE", id);
            rs.updateRow();
        }
        return id;
    }    
    
    public List<Category> getCategories() throws SQLException {
        return categoryController.getEntities();
    }    
    
    public int addCategory() throws SQLException{
        categoryController.addNewEntity();
        return getCategories().size() - 1;
    }
    
    public void deleteCategory(int index) throws SQLException{
        categoryController.deleteEntity(index);
    }
    
    public void updateCategory(final Category category, final int rowIndex) throws SQLException{
        categoryController.updateEntity(category, rowIndex);
    }
}
