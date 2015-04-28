package hu.elte.pt.store.logic.controllers;

import hu.elte.pt.store.logic.DataSource;
import hu.elte.pt.store.logic.entities.Customer;
import hu.elte.pt.store.logic.entities.Order;
import hu.elte.pt.store.logic.entities.Product;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
//befejezetlen
/**
 *
 * @author Cseh Zoltán
 */

public class OrderController implements EntityController<Order> {

    private static final Logger log = Logger.getLogger(OrderController.class);
    private final String TABLE_NAME = "\"ORDER\"";
    private final String FULL_SELECT_SQL = "SELECT * FROM " + TABLE_NAME;      //select all data from a database

    
    @Override
    public Order getEntityById(int entityId) throws SQLException {
        Order order = new Order();
        int customerID;
        int productID;
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement();
                /*
                A ResultSet consists of records. Each records contains a set of columns.
                Each record contains the same amount of columns,
                although not all columns may have a value. 
                */
                ResultSet resultSet = statement.executeQuery(FULL_SELECT_SQL + " WHERE ORDER_ID = " + entityId);) {
            resultSet.next();
            
            order.setOrderID(resultSet.getInt(1));
            productID = resultSet.getInt(2);
            //productID = resultSet.getInt(2); 
            customerID = resultSet.getInt(3);
              
            //order.setCustomerID((Customer) resultSet.getObject(3));
            

            //When iterating the ResultSet you want to access the column values of each record.
            //We use column index instead of a column name.
            log.debug("A táblában az azonosító alapján történő keresés sikeres volt. "
                    + "ResultSet: "
                    + resultSet.getInt(1) + " " //first column, you can add colname
                    + resultSet.getInt(2) + " "  //second column
                    + resultSet.getInt(3) + " " //ide mit adjak meg????????????????,
            );
        } catch (SQLException ex) {
            log.error("A táblában az [" + entityId + "] azonosító alapján történő keresés során kivétel keletkezett", ex);
            throw new SQLException("A táblában az [" + entityId + "] azonosító alapján történő keresés sikertelen volt");
        }
        
        Customer customer = DataSource.getInstance().getCustomerController().getEntityById(customerID);
        order.setCustomer(customer);
        
        Product product = DataSource.getInstance().getProductController().getEntityById(productID);
        order.setProduct(product);
        return order;
    }

    @Override
    public Order getEntityByRowIndex(int rowIndex) throws SQLException {
        Order order = new Order();
        int customerID;
        int productID;
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet resultSet = statement.executeQuery(FULL_SELECT_SQL);) {
            
            //Moves the cursor to the given row number in this ResultSet object.
            resultSet.absolute(rowIndex + 1);
            
            order.setOrderID(resultSet.getInt(1));
            customerID = resultSet.getInt(2);
            productID = resultSet.getInt(3);     

            
            log.debug("A táblában a sorindex alapján történő keresés sikeres volt."
                    + "ResultSet: "
                    + resultSet.getInt(1) + " "
                    
            );
        } catch (SQLException ex) {
            log.error("A táblában a sorindex alapján történő keresés során kivétel keletkezett! ", ex);
            throw new SQLException("A táblában a sorindex alapján történő keresés sikertelen volt!");
        }

        Customer customer = DataSource.getInstance().getCustomerController().getEntityById(customerID);
        order.setCustomer(customer);
        
        Product product = DataSource.getInstance().getProductController().getEntityById(productID);
        order.setProduct(product);
        return order;
    }
    //OK
    @Override
    public int getEntityCount() throws SQLException {
        int orderCount = 0;
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS CNT FROM \"ORDER\"");) {
            resultSet.next();
            orderCount = resultSet.getInt("CNT");
            log.debug("A táblában fellelhető sorok számlálása sikeresen lezajlott " + orderCount + " értékkel.");
        } catch (SQLException ex) {
            log.error("A táblában fellelhető sorok számlálásának lekérdezése során kivétel keletkezett! ", ex);
            throw new SQLException("A táblában fellelhető sorok számlálásának lekérdezése során kivétel keletkezett!");
        }
        return orderCount;
    }

    @Override
    public void updateEntity(Order entity, final int rowIndex) throws SQLException {
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet resultSet = statement.executeQuery(FULL_SELECT_SQL);) {
            resultSet.absolute(rowIndex + 1);
        
            resultSet.updateInt(1, entity.getOrderID());
            resultSet.updateInt(2, entity.getProduct().getProductID());
            resultSet.updateInt(3, entity.getCustomer().getCustomerID());

           
            
            resultSet.updateRow();                                                           
            log.info("A(z) (" + entity.getOrderID() + ") azonosítójú sor sikeresen módosult."
                    + "Hozzáadott termék: " + entity.getProduct().getProductName() +
                    " a hozzá tartozó vásárló neve: " + entity.getCustomer().getName() + " lett.");
            
            
        } catch (SQLException ex) {
            log.error("A táblában található" + entity.getOrderID()+ " azonosíítóval rendelkező sor módosítása során kivétel keletkezett!", ex);
            throw new SQLException("A táblában található" + entity.getOrderID() + " azonosíítóval rendelkező sor módosítása sikertelen volt!");
            
        }
    }

    @Override
    public int addNewEntity() throws SQLException {
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet resultSet = statement.executeQuery(FULL_SELECT_SQL);) {
            resultSet.moveToInsertRow();
            resultSet.updateInt("ORDER_ID", DataSource.getInstance().obtainNewId());
            resultSet.updateInt("PRODUCT_ID", DataSource.getInstance().getProducts().get(0).getProductID());
            resultSet.updateInt("CUSTOMER_ID", DataSource.getInstance().getCustomers().get(0).getCustomerID());
            
            resultSet.insertRow();
            log.info("Az új rendelés létrehozása sikeres volt.");
        } catch (SQLException ex) {
            log.error("A táblán történő új sor létrehozása során kivétel keletkezett! ", ex);
            throw new SQLException("Nem sikerült új sort létrehozni a rendelés táblában!");
        }

        try {
            return getEntityCount() - 1;
        } catch (SQLException ex) {
            log.error("A rendelés táblán történő új sor hozzáadása folyamán, az új sor indexének megállapítása közben kivétel váltódott ki!");
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
            log.info("A kiválasztott [ " + rowIndex + " ] sorindex-ű elem törlése sikeres volt");
        } catch (SQLException ex) {
            log.error("A kiválasztott [ " + rowIndex + " ] sorindex-ű elem törlése során kivétel keletkezett, így a törlés nem valósult meg! ", ex);
            throw new SQLException("A kiválasztott [ " + rowIndex + " ] sorindex-ű elem törlése során kivétel keletkezett, így a törlés nem valósult meg!");
        }
    }

    @Override
    public List<Order> getEntities() throws SQLException {
        List<Order> orders = new ArrayList<>();
        int productID;
        int customerID;
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(FULL_SELECT_SQL)) {
            while (rs.next()) {
                Order order = new Order();
                
                order.setOrderID(rs.getInt(1));
          
                productID = rs.getInt(2);
                Product product = DataSource.getInstance().getProductController().getEntityById(productID);
                order.setProduct(product);
                
                customerID = rs.getInt(3);
                Customer customer = DataSource.getInstance().getCustomerController().getEntityById(customerID);
                order.setCustomer(customer);

                orders.add(order);
            }
        }
        return orders;
    }

    
}
