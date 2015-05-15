package hu.elte.pt.store.logic;

import hu.elte.pt.store.logic.controllers.CategoryController;
import hu.elte.pt.store.logic.controllers.CustomerController;
import hu.elte.pt.store.logic.controllers.ManufacturerController;
import hu.elte.pt.store.logic.controllers.OrderController;
import hu.elte.pt.store.logic.controllers.ProductController;
import hu.elte.pt.store.logic.controllers.StoreController;
import hu.elte.pt.store.logic.entities.Category;
import hu.elte.pt.store.logic.entities.Customer;
import hu.elte.pt.store.logic.entities.Manufacturer;
import hu.elte.pt.store.logic.entities.Order;
import hu.elte.pt.store.logic.entities.Product;
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
 * Singleton pattern szerint elkészített osztály, mely az adatbázis kapcsolat és
 * az entitásokhoz tartozó controllerek lekérésére szolgál.
 *
 * @author Nagy Krisztián
 * @version 1.0
 */
public class DataSource {

    /**
     * A spring segítségével injektált adatbázis kapcsolati adatokat tároló
     * konténer referenciáját tartalmazó mező.
     */
    private final StoreConfiguration config;
    /**
     * Naplózást elősegítő mező
     */
    private static final Logger log = Logger.getLogger(DataSource.class);

    /**
     * A kategória kontrollerének referenciáját tároló mező
     */
    private final CategoryController categoryController;
    /**
     * A gyártó kontrollerének referenciáját tároló mező
     */
    private final ManufacturerController manufacturerController;
    /**
     * A vásárló kontrollerének referenciáját tároló mező
     */
    private final CustomerController customerController;
    /**
     * A bolt kontrollerének referenciáját tároló mező
     */
    private final StoreController storeController;
    /**
     * A termék kontrollerének referenciáját tároló mező
     */
    private final ProductController productController;
    /**
     * A rendelés kontrollerének referenciáját tároló mező
     */
    private final OrderController orderController;

    /**
     * A DataSource konstruktora. Mivel egyszerre csak egy példány létezhet
     * belőle, így kívülről nem látható.
     */
    private DataSource() {
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
        productController = new ProductController();
        orderController = new OrderController();
    }

    /**
     * Az adatbázis kapcsolat elérését bisztosító metódus
     *
     * @return az adatbázis kapcsolat objektuma.
     * @throws SQLException kapcsolat létrehozása során keletkező kivétel.
     * @see java.sql.Connection
     * @see java.sql.SQLException
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(config.getConnectionString(), config.getUserName(), config.getPassword());
    }

    /**
     * A kategória kontrollerének elkérését elősegítő metódus
     *
     * @return a kategória kontrollere
     * @see hu.elte.pt.store.logic.controllers.CategoryController
     */
    public CategoryController getCategoryController() {
        return categoryController;
    }

    /**
     * A gyártó kontrollerének elkérését elősegítő metódus
     *
     * @return a gyártó kontrollere
     * @see hu.elte.pt.store.logic.controllers.ManufacturerController
     */
    public ManufacturerController getManufacturerController() {
        return manufacturerController;
    }

    /**
     * A vásárló kontrollerének elkérését elősegítő metódus
     *
     * @return a vásárló kontrollere
     * @see hu.elte.pt.store.logic.controllers.CustomerController
     */
    public CustomerController getCustomerController() {
        return customerController;
    }

    /**
     * A bolt kontrollerének elkérését elősegítő metódus
     *
     * @return a bolt kontrollere
     * @see hu.elte.pt.store.logic.controllers.StoreController
     */
    public StoreController getStoreController() {
        return storeController;
    }

    /**
     * A termék kontrollerének elkérését elősegítő metódus
     *
     * @return a termék kontrollere
     * @see hu.elte.pt.store.logic.controllers.ProductController
     */
    public ProductController getProductController() {
        return productController;
    }

    /**
     * A rendelés kontrollerének elkérését elősegítő metódus
     *
     * @return a rendelés kontrollere
     * @see hu.elte.pt.store.logic.controllers.OrderController
     */
    public OrderController getOrderController() {
        return orderController;
    }

    /**
     * A DataSource egyetlen példányának elkérését elősegítő metódus
     *
     * @return DataSource példány
     */
    public static DataSource getInstance() {
        return DataSourceHolder.INSTANCE;
    }

    /**
     * Segédosztály, amely elősegíti, hogy egyetlen példány létezzen egy időben
     * a DataSource-ból.
     */
    private static class DataSourceHolder {

        private static final DataSource INSTANCE = new DataSource();
    }

    /**
     * Azonosító generálását lehetővétevő metódus, mely egy soron következő
     * azonosítót ad az adatbázisban felhasznált adatok alapján, ezzel
     * biztosítva az azonosító egyediségét.
     *
     * @return egyedi azonosító
     * @throws SQLException sikertelen adatbázis lekérdezés során kiváltódó
     * kivétel
     */
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

    /**
     * Kategória kilistázásának metódusa
     *
     * @return kategória entitásokat tartalmazó lista
     * @throws SQLException lekérdezés során kiváltodó kivétel
     */
    public List<Category> getCategories() throws SQLException {
        return categoryController.getEntities();
    }

    /**
     * Új kategória hozzáadását elősegítő metódus
     *
     * @return az újonnan beszúrt kategória sorának indexe
     * @throws SQLException a művelet végrehajtása során keletkező kivétel
     */
    public int addCategory() throws SQLException {
        categoryController.addNewEntity();
        return getCategories().size() - 1;
    }

    /**
     * Egy kategória törlését elősegítő metódus
     *
     * @param index a kategória sor indexe
     * @throws SQLException a törlés során kiváltódó kivétel
     */
    public void deleteCategory(int index) throws SQLException {
        categoryController.deleteEntity(index);
    }

    /**
     * Egy kategória módosítását elősegítő metódus
     *
     * @param category módosított kategória
     * @param rowIndex a módosított kategória sorindexe
     * @throws SQLException módosítás során kiváltódó kivétel
     */
    public void updateCategory(final Category category, final int rowIndex) throws SQLException {
        categoryController.updateEntity(category, rowIndex);
    }

    /**
     * A gyártó kilistázásának metódusa
     *
     * @return gyártó entitásokat tartalmazó lista
     * @throws SQLException lekérdezés során kiváltodó kivétel
     */
    public List<Manufacturer> getManufacturers() throws SQLException {
        return manufacturerController.getEntities();
    }

    /**
     * Új gyártó hozzáadását elősegítő metódus
     *
     * @return az újonnan beszúrt gyártó sorának indexe
     * @throws SQLException a művelet végrehajtása során keletkező kivétel
     */
    public int addManufacturer() throws SQLException {
        manufacturerController.addNewEntity();
        return getManufacturers().size() - 1;
    }

    /**
     * Egy gyártó törlését elősegítő metódus
     *
     * @param index a gyártó sor indexe
     * @throws SQLException a törlés során kiváltódó kivétel
     */
    public void deleteManufacturer(int index) throws SQLException {
        manufacturerController.deleteEntity(index);
    }

    /**
     * Egy gyártó módosítását elősegítő metódus
     *
     * @param manufacturer módosított gyártó
     * @param rowIndex a módosított gyártó sorindexe
     * @throws SQLException módosítás során kiváltódó kivétel
     */
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

    //Product
    public List<Product> getProducts() throws SQLException {
        return productController.getEntities();
    }

    public int addProduct() throws SQLException {
        productController.addNewEntity();
        return getProducts().size() - 1;
    }

    public void deleteProduct(int index) throws SQLException {
        productController.deleteEntity(index);
    }

    public void updateProduct(final Product product, final int rowIndex) throws SQLException {
        productController.updateEntity(product, rowIndex);
    }

    //Order
    public List<Order> getOrders() throws SQLException {
        return orderController.getEntities();
    }

    public int addOrder() throws SQLException {
        orderController.addNewEntity();
        return getOrders().size() - 1;
    }

    public void deleteOrder(int index) throws SQLException {
        orderController.deleteEntity(index);
    }

    public void updateOrder(final Order order, final int rowIndex) throws SQLException {
        orderController.updateEntity(order, rowIndex);
    }
}
