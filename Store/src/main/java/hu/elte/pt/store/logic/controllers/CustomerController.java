package hu.elte.pt.store.logic.controllers;

import hu.elte.pt.store.logic.DataSource;
import hu.elte.pt.store.logic.entities.Customer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Customer controller osztálya
 *
 * @author Holló Eszter
 */
public class CustomerController implements EntityController<Customer> {

    private static final Logger log = Logger.getLogger(CustomerController.class);
    private final String TABLE_NAME = "CUSTOMER";
    private final String FULL_SELECT_SQL = "SELECT * FROM " + TABLE_NAME;

    @Override
    public Customer getEntityById(int entityId) throws SQLException {
        Customer customer = new Customer();
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(FULL_SELECT_SQL + " WHERE CUSTOMER_ID = " + entityId);) {
            resultSet.next();          
            customer.setCustomerID(resultSet.getInt(1));            
            customer.setName(resultSet.getString(3));
            customer.setAddress(resultSet.getString(4));
            customer.setTelephone(resultSet.getString(5));
            customer.setEmail(resultSet.getString(6));
            log.info("A táblában az azonosító alapján történő keresés sikeres volt. "
                    + "ResultSet: "
                    + resultSet.getInt(1) + " "
                    + resultSet.getInt(2) + " "
                    + resultSet.getString(3) + " "
                    + resultSet.getString(4) + " "
                    + resultSet.getString(5) + " "
                    + resultSet.getString(6)
            );
        } catch (SQLException ex) {
            log.error("A táblában az [" + entityId + "] azonosító alapján történő keresés során kivétel keletkezett", ex);
            throw new SQLException("A táblában az [" + entityId + "] azonosító alapján történő keresés sikertelen volt");
        }
        return customer;
    }

    @Override
    public Customer getEntityByRowIndex(int rowIndex) throws SQLException {
        Customer customer = new Customer();
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet resultSet = statement.executeQuery(FULL_SELECT_SQL);) {
            resultSet.absolute(rowIndex + 1);
            customer.setCustomerID(resultSet.getInt(1));            
            customer.setName(resultSet.getString(3));
            customer.setAddress(resultSet.getString(4));
            customer.setTelephone(resultSet.getString(5));
            customer.setEmail(resultSet.getString(6));

            log.info("A táblában a sorindex alapján történő keresés sikeres volt."
                    + "ResultSet: "
                    + resultSet.getInt(1) + " "
                    + resultSet.getInt(2) + " "
                    + resultSet.getString(3) + " "
                    + resultSet.getString(4) + " "
                    + resultSet.getString(5) + " "
                    + resultSet.getString(6)
            );
        } catch (SQLException ex) {
            log.error("A táblában a sorindex alapján történő keresés során kivétel keletkezett! ", ex);
            throw new SQLException("A táblában a sorindex alapján történő keresés sikertelen volt!");
        }
        return customer;
    }

    @Override
    public int getEntityCount() throws SQLException {
        int customerCount = 0;
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS CNT FROM CCUSTOMER");) {
            resultSet.next();
            customerCount = resultSet.getInt("CNT");
            log.debug("A táblában fellelhető sorok számlálása sikeresen lezajlott " + customerCount + " értékkel.");
        } catch (SQLException ex) {
            log.error("A táblában fellelhető sorok számlálásának lekérdezése során kivétel keletkezett! ", ex);
            throw new SQLException("A táblában fellelhető sorok számlálásának lekérdezése során kivétel keletkezett!");
        }
        return customerCount;
    }

    @Override
    public void updateEntity(Customer entity, final int rowIndex) throws SQLException {
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet resultSet = statement.executeQuery(FULL_SELECT_SQL);) {
            resultSet.absolute(rowIndex + 1);
            resultSet.updateString("NAME", entity.getName());
            resultSet.updateString("ADDRESS", entity.getAddress());
            resultSet.updateString("TELEPHONE", entity.getTelephone());
            resultSet.updateString("EMAIL", entity.getEmail());

            resultSet.updateRow();
            log.info("A(z) (" + entity.getCustomerID() + ") azonosítójú sor sikeresen módosult. Az új NAME: " + entity.getName() + " lett.");
        } catch (SQLException ex) {
            log.error("A táblában található" + entity.getCustomerID() + " azonosíítóval rendelkező sor módosítása során kivétel keletkezett!", ex);
            throw new SQLException("A táblában található" + entity.getCustomerID() + " azonosíítóval rendelkező sor módosítása sikertelen volt!");
        }
    }

    @Override
    public int addNewEntity() throws SQLException {
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet resultSet = statement.executeQuery(FULL_SELECT_SQL);) {
            resultSet.moveToInsertRow();
            resultSet.updateInt("CUSTOMER_ID", DataSource.getInstance().obtainNewId());
            resultSet.updateString("NAME", "NAME");
            resultSet.updateString("ADDRESS", "ADDRESS");
            resultSet.updateString("TELEPHONE", "TELEPHONE");
            resultSet.updateString("EMAIL", "EMAIL");
            resultSet.insertRow();
            log.info("Az új vásárló létrehozása sikeres volt.");
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

    @Override
    public List<Customer> getEntities() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(FULL_SELECT_SQL)) {
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerID(rs.getInt(1));               
                customer.setName(rs.getString(3));
                customer.setAddress(rs.getString(4));
                customer.setTelephone(rs.getString(5));
                customer.setEmail(rs.getString(6));
                customers.add(customer);
            }
        }
        return customers;
    }
}
