package hu.elte.pt.store.gui;

import hu.elte.pt.store.gui.comboboxmodels.EntityComboBoxModel;
import hu.elte.pt.store.gui.tablemodels.CategoryTableModel;
import hu.elte.pt.store.gui.tablemodels.CustomerTableModel;
import hu.elte.pt.store.gui.tablemodels.EntityHandlerTableModel;
import hu.elte.pt.store.gui.tablemodels.ManufacturerTableModel;
import hu.elte.pt.store.gui.tablemodels.OrderTableModel;
import hu.elte.pt.store.gui.tablemodels.ProductTableModel;
import hu.elte.pt.store.gui.tablemodels.StoreTableModel;
import hu.elte.pt.store.logic.DataSource;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableColumn;
import org.apache.log4j.Logger;

/**
 * GUI Frame
 * @author Nagy Krisztián
 */
public class StoreFrame extends JFrame{
    
    private static final Logger log = Logger.getLogger(StoreFrame.class);
    
    public static void displayError(final String message){
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private final JTabbedPane jTabbedPane;
    private final JTable categoryTable, storeTable, customerTable, manufacturerTable, productTable, orderTable;
    private final CategoryTableModel categoryTableModel;
    private final StoreTableModel storeTableModel;
    private final CustomerTableModel customerTableModel;
    private final ManufacturerTableModel manufacturerTableModel;
    private final ProductTableModel productTableModel;
    private final OrderTableModel orderTableModel;
    
    public StoreFrame(){
        try {
            DataSource.getInstance().getConnection().close();
        } catch (SQLException ex) {
            log.error("Az adatbáziskapcsolat létrehozása során kivétel lépett fel!", ex);
            displayError(ex.getMessage());
            System.exit(1);        
        }
        
        setTitle("Store");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setSize(600,400);
        setLocationRelativeTo(null);
        
        StoreMenuBar menuBar = new StoreMenuBar();
        setJMenuBar(menuBar);
        
        jTabbedPane = new JTabbedPane();
        getContentPane().add(jTabbedPane, BorderLayout.CENTER);
        jTabbedPane.addChangeListener(menuBar);
  
        orderTableModel = new OrderTableModel();
        orderTable = new JTable(orderTableModel);
        orderTable.setAutoCreateRowSorter(true);
        orderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setCellEditorListener(orderTable, orderTableModel);
        
        storeTableModel = new StoreTableModel();
        storeTable = new JTable(storeTableModel);
        storeTable.setAutoCreateRowSorter(true);
        storeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setCellEditorListener(storeTable, storeTableModel);        
              
        customerTableModel = new CustomerTableModel();
        customerTable = new JTable(customerTableModel);
        customerTable.setAutoCreateRowSorter(true);
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setCellEditorListener(customerTable, customerTableModel);

        productTableModel = new ProductTableModel();
        productTable = new JTable(productTableModel);
        productTable.setAutoCreateRowSorter(true);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productTable.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(new JComboBox(new EntityComboBoxModel(DataSource.getInstance().getManufacturerController()))));
        productTable.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(new JComboBox(new EntityComboBoxModel(DataSource.getInstance().getCategoryController()))));        
        setCellEditorListener(productTable, productTableModel);           
        
        manufacturerTableModel = new ManufacturerTableModel();
        manufacturerTable = new JTable(manufacturerTableModel);
        manufacturerTable.setAutoCreateRowSorter(true);
        manufacturerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        manufacturerTable.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(new JComboBox(new EntityComboBoxModel(DataSource.getInstance().getStoreController()))));
        setCellEditorListener(manufacturerTable, manufacturerTableModel);
        
        categoryTableModel = new CategoryTableModel();
        categoryTable = new JTable(categoryTableModel);
        categoryTable.setAutoCreateRowSorter(true);
        categoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setCellEditorListener(categoryTable, categoryTableModel);        
        
        jTabbedPane.add("Rendelés", new JScrollPane(orderTable));
        jTabbedPane.add("Bolt", new JScrollPane(storeTable));
        jTabbedPane.add("Vásárló", new JScrollPane(customerTable));
        jTabbedPane.add("Termék", new JScrollPane(productTable));
        jTabbedPane.add("Gyártó", new JScrollPane(manufacturerTable));
        jTabbedPane.add("Kategória", new JScrollPane(categoryTable));     
        
    }
    
    private void setCellEditorListener(final JTable table, final EntityHandlerTableModel tableModel){
        Enumeration<TableColumn> tableColumns = table.getColumnModel().getColumns();
        while(tableColumns.hasMoreElements()){
            TableColumn tableColumn = tableColumns.nextElement();
            if(tableColumn.getCellEditor() != null){
                tableColumn.getCellEditor().addCellEditorListener(tableModel.getCellEditorListener());
            }
        }
    }
  
    private final Action newOrderAction = new AbstractAction("Rendelés hozzáadása"){

        @Override
        public void actionPerformed(ActionEvent e) {
            orderTableModel.addNewEntity();
        }
        
    };    
    
    private final Action deleteOrderAction = new AbstractAction("Rendelés törlése"){

        @Override
        public void actionPerformed(ActionEvent e) {
            deleteRowsFromTable(orderTable, orderTableModel);
        }
        
    };    
    
    private final Action newCategoryAction = new AbstractAction("Kategória hozzáadása"){

        @Override
        public void actionPerformed(ActionEvent e) {
            categoryTableModel.addNewEntity();
        }
        
    };    
    
    private final Action deleteCategoryAction = new AbstractAction("Kategória törlése"){

        @Override
        public void actionPerformed(ActionEvent e) {
            deleteRowsFromTable(categoryTable, categoryTableModel);
        }
        
    };
    
    private final Action newStoreAction = new AbstractAction("Raktár hozzáadása"){

        @Override
        public void actionPerformed(ActionEvent e) {
            storeTableModel.addNewEntity();
        }
        
    };    
    
    private final Action deleteStoreAction = new AbstractAction("Raktár törlése"){

        @Override
        public void actionPerformed(ActionEvent e) {
            deleteRowsFromTable(storeTable, storeTableModel);
        }
        
    };    
 
    private final Action newCustomerAction = new AbstractAction("Vásárló hozzáadása"){

        @Override
        public void actionPerformed(ActionEvent e) {
            customerTableModel.addNewEntity();
        }
        
    };    
    
    private final Action deleteCustomerAction = new AbstractAction("Vásárló törlése"){

        @Override
        public void actionPerformed(ActionEvent e) {
            deleteRowsFromTable(customerTable, customerTableModel);
        }
        
    };    
    
    private final Action newManufacturerAction = new AbstractAction("Gyártó hozzáadása"){

        @Override
        public void actionPerformed(ActionEvent e) {
            manufacturerTableModel.addNewEntity();
        }
        
    };    
    
    private final Action deleteManufacturerAction = new AbstractAction("Gyártó törlése"){

        @Override
        public void actionPerformed(ActionEvent e) {
            deleteRowsFromTable(manufacturerTable, manufacturerTableModel);
        }
        
    };      
    
    private final Action newProductAction = new AbstractAction("Termék hozzáadása"){

        @Override
        public void actionPerformed(ActionEvent e) {
            productTableModel.addNewEntity();
        }
        
    };    
    
    private final Action deleteProductAction = new AbstractAction("Termék törlése"){

        @Override
        public void actionPerformed(ActionEvent e) {
            deleteRowsFromTable(productTable, productTableModel);
        }
        
    };    
    
    private void deleteRowsFromTable(JTable table, EntityHandlerTableModel tableModel) {
        int[] selectedRows = table.getSelectedRows();
        ArrayList<Integer> rowIndicesList = new ArrayList<Integer>(selectedRows.length);
        for (int i = 0; i < selectedRows.length; i++) {
            int selectedRowIdx = selectedRows[i];
            rowIndicesList.add(table.convertRowIndexToModel(selectedRowIdx));
        }
        Collections.sort(rowIndicesList);
        Collections.reverse(rowIndicesList);
        for (int i = 0; i < rowIndicesList.size(); i++) {
            Integer rowIndex = rowIndicesList.get(i);
            tableModel.deleteEntity(rowIndex);
        }
    }    
    
    private class StoreMenuBar extends JMenuBar implements ChangeListener{
        
        private final JMenu orderMenu, storeMenu, customerMenu, productMenu, manufacturerMenu, categoryMenu;
        
        public StoreMenuBar(){
            
            orderMenu = new JMenu("Rendelés");
            orderMenu.add(newOrderAction);
            orderMenu.add(deleteOrderAction);
            
            storeMenu = new JMenu("Bolt");
            storeMenu.add(newStoreAction);
            storeMenu.add(deleteStoreAction);
            
            customerMenu = new JMenu("Vásárló");
            customerMenu.add(newCustomerAction);
            customerMenu.add(deleteCustomerAction);
            
            productMenu = new JMenu("Termék");
            productMenu.add(newProductAction);
            productMenu.add(deleteProductAction);
            
            manufacturerMenu = new JMenu("Gyártó");
            manufacturerMenu.add(newManufacturerAction);
            manufacturerMenu.add(deleteManufacturerAction);
            
            categoryMenu = new JMenu("Kategória");
            categoryMenu.add(newCategoryAction);
            categoryMenu.add(deleteCategoryAction);
            

            
        }
        
        @Override
        public void stateChanged(ChangeEvent e) {
            removeAll();
            repaint();
            switch (jTabbedPane.getTitleAt(jTabbedPane.getSelectedIndex())) {
                case "Rendelés":
                    add(orderMenu);
                    break;
                case "Bolt":
                    add(storeMenu);
                    break; 
                case "Vásárló":
                    add(customerMenu);
                    break;
                case "Termék":
                    add(productMenu);
                    break;
                case "Gyártó":
                    add(manufacturerMenu);
                    break;
                case "Kategória":
                    add(categoryMenu);
                    break;

            }

        }
    
    }
}
