package hu.elte.pt.store.logic.controllers;

import hu.elte.pt.store.logic.DataSource;
import hu.elte.pt.store.logic.entities.Category;
import hu.elte.pt.store.logic.entities.Manufacturer;
import hu.elte.pt.store.logic.entities.Product;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * A Product táblán való műveletek végrehajtásáért felelős controller osztály.
 *
 * @author Bojtos Csaba
 */
public class ProductController implements EntityController<Product> {

    private static final Logger log = Logger.getLogger(ProductController.class);
    private final String TABLE_NAME = "PRODUCT";
    private final String FULL_SELECT_SQL = "SELECT * FROM " + TABLE_NAME;

    @Override
    public Product getEntityById(int entityId) throws SQLException {
        Product product = new Product();
        int manufacturerID;
        int categoryID;
        if (DataSource.getInstance().getManufacturerController().getEntityCount() < 1 || DataSource.getInstance().getCategoryController().getEntityCount() < 1) {
            throw new SQLException("Nem található idegen entitás");
        }
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(FULL_SELECT_SQL + " WHERE PRODUCT_ID = " + entityId);) {
            resultSet.next();
            product.setProductID(resultSet.getInt(1));
            product.setProductName(resultSet.getString(2));
            manufacturerID = resultSet.getInt(3);
            product.setDescription(resultSet.getString(4));
            categoryID = resultSet.getInt(5);
            product.setPrice(resultSet.getInt(6));
            product.setStock(resultSet.getInt(7));
            product.setIsActive(resultSet.getBoolean(8));

            log.debug("A táblában az azonosító alapján történő keresés sikeres volt. "
                    + "ResultSet: " + " "
                    + resultSet.getInt(1) + " "
                    + resultSet.getString(2) + " "
                    + resultSet.getInt(3) + " "
                    + resultSet.getString(4) + " "
                    + resultSet.getInt(5) + " "
                    + resultSet.getInt(6) + " "
                    + resultSet.getInt(7) + " "
                    + resultSet.getBoolean(8)
            );
        } catch (SQLException ex) {
            log.error("A táblában az [" + entityId + "] azonosító alapján történő keresés során kivétel keletkezett", ex);
            throw new SQLException("A táblában az [" + entityId + "] azonosító alapján történő keresés sikertelen volt");
        }

        Manufacturer manufacturer = DataSource.getInstance().getManufacturerController().getEntityById(manufacturerID);
        product.setManufacturer(manufacturer);
        Category category = DataSource.getInstance().getCategoryController().getEntityById(categoryID);
        product.setCategory(category);

        return product;
    }

    @Override
    public Product getEntityByRowIndex(int rowIndex) throws SQLException {
        Product product = new Product();
        int manufacturerID;
        int categoryID;
        if (DataSource.getInstance().getManufacturerController().getEntityCount() < 1 || DataSource.getInstance().getCategoryController().getEntityCount() < 1) {
            throw new SQLException("Nem található idegen entitás");
        }
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet resultSet = statement.executeQuery(FULL_SELECT_SQL);) {
            resultSet.absolute(rowIndex + 1);
            product.setProductID(resultSet.getInt(1));
            product.setProductName(resultSet.getString(2));
            manufacturerID = resultSet.getInt(3);
            product.setDescription(resultSet.getString(4));
            categoryID = resultSet.getInt(5);
            product.setPrice(resultSet.getInt(6));
            product.setStock(resultSet.getInt(7));
            product.setIsActive(resultSet.getBoolean(8));

            log.debug("A táblában a sorindex alapján történő keresés sikeres volt."
                    + "ResultSet: "
                    + resultSet.getInt(1) + " "
                    + resultSet.getString(2) + " "
                    + resultSet.getInt(3) + " "
                    + resultSet.getString(4) + " "
                    + resultSet.getInt(5) + " "
                    + resultSet.getInt(6) + " "
                    + resultSet.getInt(7) + " "
                    + resultSet.getBoolean(8)
            );
        } catch (SQLException ex) {
            log.error("A táblában a sorindex alapján történő keresés során kivétel keletkezett!", ex);
            throw new SQLException("A táblában a sorindex alapján történő keresés sikertelen volt!");
        }

        Manufacturer manufacturer = DataSource.getInstance().getManufacturerController().getEntityById(manufacturerID);
        product.setManufacturer(manufacturer);
        Category category = DataSource.getInstance().getCategoryController().getEntityById(categoryID);
        product.setCategory(category);

        return product;
    }

    @Override
    public int getEntityCount() throws SQLException {
        int productCount = 0;
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS CNT FROM PRODUCT");) {
            resultSet.next();
            productCount = resultSet.getInt("CNT");
            log.debug("A táblában fellelhető sorok számlálása sikeresen lezajlott " + productCount + " értékkel.");
        } catch (SQLException ex) {
            log.error("A táblában fellelhető sorok számlálásának lekérdezése során kivétel keletkezett!", ex);
            throw new SQLException("A táblában fellelhető sorok számlálásának lekérdezése során kivétel keletkezett!");
        }
        return productCount;
    }

    @Override
    public void updateEntity(Product entity, final int rowIndex) throws SQLException {
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet resultSet = statement.executeQuery(FULL_SELECT_SQL);) {
            resultSet.absolute(rowIndex + 1);
            resultSet.updateInt("PRODUCT_ID", entity.getProductID());
            resultSet.updateString("NAME", entity.getProductName());
            resultSet.updateInt("MANUFACTURER_ID", entity.getManufacturer().getManufacturerId());
            resultSet.updateString("DESCRIPTION", entity.getDescription());
            resultSet.updateInt("CATEGORY_ID", entity.getCategory().getCategoryId());
            resultSet.updateInt("PRICE", entity.getPrice());
            resultSet.updateInt("STOCK", entity.getStock());
            resultSet.updateBoolean("ACTIVE", entity.isActive());
            resultSet.updateRow();

            log.info("A(z) (" + entity.getProductID() + ") azonosítójú sor sikeresen módosult. "
                    + "A termék új adatai: " + entity.getProductName() + " " + entity.getCategory().getName()
                    + " (" + entity.getDescription() + "), Gyártó: " + entity.getManufacturer().getName()
                    + ", Ár: " + entity.getPrice() + " Forint, Raktárkészlet: " + entity.getStock());
        } catch (SQLException ex) {
            log.error("A táblában található" + entity.getProductID() + " azonosíítóval rendelkező sor módosítása során kivétel keletkezett!", ex);
            throw new SQLException("A táblában található" + entity.getProductID() + " azonosíítóval rendelkező sor módosítása sikertelen volt!");
        }
    }

    @Override
    public int addNewEntity() throws SQLException {
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet resultSet = statement.executeQuery(FULL_SELECT_SQL);) {
            resultSet.moveToInsertRow();
            resultSet.updateInt("PRODUCT_ID", DataSource.getInstance().obtainNewId());
            resultSet.updateString("NAME", "<új termék neve>");
            resultSet.updateInt("MANUFACTURER_ID", DataSource.getInstance().getManufacturers().get(0).getManufacturerId());
            resultSet.updateString("DESCRIPTION", "<leírás>");
            resultSet.updateInt("CATEGORY_ID", DataSource.getInstance().getCategories().get(0).getCategoryId());
            resultSet.updateInt("PRICE", 0);
            resultSet.updateInt("STOCK", 0);
            resultSet.updateBoolean("ACTIVE", false);
            resultSet.insertRow();

            log.info("Az új termék létrehozása sikeres volt.");
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
            log.info("A kiválasztott [" + rowIndex + "] sorindex-ű elem törlése sikeres volt");
        } catch (SQLException ex) {
            log.error("A kiválasztott [" + rowIndex + "] sorindex-ű elem törlése során kivétel keletkezett, így a törlés nem valósult meg! ", ex);
            throw new SQLException("A kiválasztott [" + rowIndex + "] sorindex-ű elem törlése során kivétel keletkezett, így a törlés nem valósult meg!");
        }
    }

    @Override
    public List<Product> getEntities() throws SQLException {
        List<Product> products = new ArrayList<>();
        int manufacturerID;
        int categoryID;
        if (DataSource.getInstance().getManufacturerController().getEntityCount() < 1 || DataSource.getInstance().getCategoryController().getEntityCount() < 1) {
            throw new SQLException("Nem található idegen entitás");
        }
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(FULL_SELECT_SQL)) {
            while (rs.next()) {
                Product product = new Product();
                product.setProductID(rs.getInt(1));
                product.setProductName(rs.getString(2));
                manufacturerID = rs.getInt(3);
                Manufacturer manufacturer = DataSource.getInstance().getManufacturerController().getEntityById(manufacturerID);
                product.setManufacturer(manufacturer);

                product.setDescription(rs.getString(4));
                categoryID = rs.getInt(5);
                Category category = DataSource.getInstance().getCategoryController().getEntityById(categoryID);

                product.setCategory(category);
                product.setPrice(rs.getInt(6));
                product.setStock(rs.getInt(7));
                product.setIsActive(rs.getBoolean(8));

                products.add(product);
            }
        }
        return products;
    }
}
