package hu.elte.pt.store.logic;

import hu.elte.pt.store.logic.controllers.CategoryController;
import hu.elte.pt.store.logic.controllers.CustomerController;
import hu.elte.pt.store.logic.controllers.ManufacturerController;
import hu.elte.pt.store.logic.controllers.StoreController;
import hu.elte.pt.store.logic.entities.Category;
import hu.elte.pt.store.logic.entities.Customer;
import hu.elte.pt.store.logic.entities.Manufacturer;
import hu.elte.pt.store.logic.entities.Store;
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
    private final ManufacturerController manufacturerController;
    private final CustomerController customerController;    
    private final StoreController storeController;
    
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
        manufacturerController = new ManufacturerController();
        customerController = new CustomerController();
        storeController = new StoreController();
    }
    
    public Connection getConnection() throws SQLException{
        return DriverManager.getConnection(config.getConnectionString(), config.getUserName(), config.getPassword());
    }

    public CategoryController getCategoryController() {
        return categoryController;
    }
    
    public ManufacturerController getManufacturerController() {
        return manufacturerController;
    }
    
    public CustomerController getCustomerController() {
        return customerController;
    }
    
    public StoreController getStoreController() {
        return storeController;
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
    
    //Category
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
    
    //Manufacturer
    public List<Manufacturer> getManufacturers() throws SQLException {
        return manufacturerController.getEntities();
    }

    public int addManufacturer() throws SQLException {
        manufacturerController.addNewEntity();
        return getManufacturers().size() - 1;
    }

    public void deleteManufacturer(int index) throws SQLException {
        manufacturerController.deleteEntity(index);
    }

    public void updateManufacturer(final Manufacturer manufacturer, final int rowIndex) throws SQLException {
        manufacturerController.updateEntity(manufacturer, rowIndex);
    }
    
    //Customer
    public List<Customer> getCustomers() throws SQLException {
        return customerController.getEntities();
    }

    public int addCustomer() throws SQLException {
        customerController.addNewEntity();
        return getCustomers().size() - 1;
    }

    public void deleteCustomer(int index) throws SQLException {
        customerController.deleteEntity(index);
    }

    public void updateCustomer(final Customer customer, final int rowIndex) throws SQLException {
        customerController.updateEntity(customer, rowIndex);
    }
    
    //Store
    public List<Store> getStores() throws SQLException {
        return storeController.getEntities();
    }

    public int addStore() throws SQLException {
        storeController.addNewEntity();
        return getStores().size() - 1;
    }

    public void deleteStore(int index) throws SQLException {
        storeController.deleteEntity(index);
    }

    public void updateStore(final Store store, final int rowIndex) throws SQLException {
        storeController.updateEntity(store, rowIndex);
    }
}
