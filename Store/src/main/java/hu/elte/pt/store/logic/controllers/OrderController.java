package hu.elte.pt.store.logic.controllers;

import hu.elte.pt.store.logic.DataSource;
import hu.elte.pt.store.logic.entities.Order;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.apache.log4j.Logger;
//befejezetlen
/**
 *
 * @author Cseh Zoltán
 */

public class OrderController implements EntityController<Order> {

    private static final Logger log = Logger.getLogger(OrderController.class);
    private final String TABLE_NAME = "ORDER";
    private final String FULL_SELECT_SQL = "SELECT * FROM " + TABLE_NAME;      //select all data from a database

    @Override
    public Order getEntityById(int entityId) throws SQLException {
        Order order = new Order();
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

            //When iterating the ResultSet you want to access the column values of each record.
            //We use column index instead of a column name.
            log.info("A táblában az azonosító alapján történő keresés sikeres volt. "
                    + "ResultSet: "
                    + resultSet.getInt(1) + " " //first column, you can add colname
                    + resultSet.getInt(2) + " "  //second column
                    + resultSet.getInt(3) + " "
            );
        } catch (SQLException ex) {
            log.error("A táblában az [" + entityId + "] azonosító alapján történő keresés során kivétel keletkezett", ex);
            throw new SQLException("A táblában az [" + entityId + "] azonosító alapján történő keresés sikertelen volt");
        }
        return order;
    }

    @Override
    public Order getEntityByRowIndex(int rowIndex) throws SQLException {
        Order order = new Order();
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet resultSet = statement.executeQuery(FULL_SELECT_SQL);) {
            
            //Moves the cursor to the given row number in this ResultSet object.
            
            resultSet.absolute(rowIndex + 1);
            
            order.setOrderID(resultSet.getInt(1));
            

            
            log.info("A táblában a sorindex alapján történő keresés sikeres volt."
                    + "ResultSet: "
                    + resultSet.getInt(1) + " "
                    
            );
        } catch (SQLException ex) {
            log.error("A táblában a sorindex alapján történő keresés során kivétel keletkezett! ", ex);
            throw new SQLException("A táblában a sorindex alapján történő keresés sikertelen volt!");
        }
        return order;
    }

    @Override
    public int getEntityCount() throws SQLException {
        int orderCount = 0;
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS CNT FROM ORDER");) {
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
        
            resultSet.updateInt(1, entity.getOrderID());    //or you can use colname for the first parameter
            
           
            resultSet.updateRow();                                                                  //Módosítani--------------------
            //log.info("A(z) (" + entity.getOrderID() + ") azonosítójú sor sikeresen módosult. Az új NAME: " + entity.getName() + " lett.");
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
            resultSet.updateInt("MANUFACTURER_ID", DataSource.getInstance().obtainNewId());
            
            
            resultSet.insertRow();
            log.info("Az új rendelés létrehozása sikeres volt.");
        } catch (SQLException ex) {
            log.error("A táblán történő új sor létrehozása során kivétel keletkezett! ", ex);
            throw new SQLException("Nem sikerült új sort létrehozni a táblában!");
        }

        try {
            return getEntityCount() - 1;
        } catch (SQLException ex) {
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
            log.info("A kiválasztott [ " + rowIndex + " ] sorindex-ű elem törlése sikeres volt");
        } catch (SQLException ex) {
            log.error("A kiválasztott [ " + rowIndex + " ] sorindex-ű elem törlése során kivétel keletkezett, így a törlés nem valósult meg! ", ex);
            throw new SQLException("A kiválasztott [ " + rowIndex + " ] sorindex-ű elem törlése során kivétel keletkezett, így a törlés nem valósult meg!");
        }
    }

    @Override   //megírni
    public List<Order> getEntities() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
}