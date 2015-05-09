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
import hu.elte.pt.store.util.ImageReader;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellRenderer;
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
    
    private static ImageIcon aboutImage;
    private static ImageIcon aboutInfoImage;
    private static ImageIcon editorAddImage;
    private static ImageIcon editorDeleteImage;
    
    static{
        try {
            //System.out.println("images" + File.separator + "about.png");
            final BufferedImage aboutBufferedImage = ImageReader.readImage("images" + File.separator + "about.png");
            aboutImage = new ImageIcon(aboutBufferedImage);
            final BufferedImage aboutInfoBufferedImage = ImageReader.readImage("images" + File.separator + "about_i.png");
            aboutInfoImage = new ImageIcon(aboutInfoBufferedImage);
            final BufferedImage editorAddBufferedImage = ImageReader.readImage("images" + File.separator + "add.png");
            editorAddImage = new ImageIcon(editorAddBufferedImage);
            final BufferedImage editorDeleteBufferedImage = ImageReader.readImage("images" + File.separator + "delete.png");
            editorDeleteImage = new ImageIcon(editorDeleteBufferedImage);
        } catch (IOException ex) {
            log.error("A programban használt képek betöltése során probléma lépett fel.", ex);
        }
    }
    
    public StoreFrame(){
        try {
            DataSource.getInstance().getConnection().close();
        } catch (SQLException ex) {
            log.error("Az adatbáziskapcsolat létrehozása során kivétel lépett fel!", ex);
            displayError(ex.getMessage());
            System.exit(1);        
        }
        
        setTitle("Áruház");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setSize(600,400);
        setLocationRelativeTo(null);
        
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            log.warn("A Nimbus téma betöltése sikertelen volt!", e);
        }         
        
        StoreMenuBar menuBar = new StoreMenuBar();
        setJMenuBar(menuBar);
        
        jTabbedPane = new JTabbedPane();
        getContentPane().add(jTabbedPane, BorderLayout.CENTER);
        jTabbedPane.addChangeListener(menuBar);
  
        orderTableModel = new OrderTableModel();
        orderTable = new JTable(orderTableModel);
        orderTable.setAutoCreateRowSorter(true);
        orderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        orderTable.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JComboBox(new EntityComboBoxModel(DataSource.getInstance().getProductController()))));
        orderTable.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(new JComboBox(new EntityComboBoxModel(DataSource.getInstance().getCustomerController())))); 
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
        productTable = new JTable(productTableModel){

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                int modelRow = productTable.convertRowIndexToModel(row);
                boolean available = (Boolean) productTableModel.getValueAt(modelRow, 6);
                if (!available) {
                    component.setBackground(new Color(255,229,204));
                    component.setForeground(Color.GRAY);
                } else {
                    component.setBackground(Color.WHITE);
                    component.setForeground(Color.BLACK);
                }
                return component;
            }
        
        };
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
        
        jTabbedPane.addTab("Rendelés", new JScrollPane(orderTable));
        jTabbedPane.addTab("Raktár", new JScrollPane(storeTable));
        jTabbedPane.addTab("Vásárló", new JScrollPane(customerTable));
        jTabbedPane.addTab("Termék", new JScrollPane(productTable));
        jTabbedPane.addTab("Gyártó", new JScrollPane(manufacturerTable));
        jTabbedPane.addTab("Kategória", new JScrollPane(categoryTable));    
     
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
    
    private final Action aboutAction = new AbstractAction("Névjegy"){
        @Override
        public void actionPerformed(ActionEvent e) {          
            JOptionPane.showMessageDialog(null, "Áruház v1.0\n\nKészítették:\n\tNagy Krisztián\n\tHolló Eszter\n\tHonti Dóra Kármen\n\tDudás Orsolya\n\tCseh Zoltán\n\tBojtos Csaba\n", "Névjegy", JOptionPane.INFORMATION_MESSAGE, aboutImage);
        }
        
    };
    
    private class StoreMenuBar extends JMenuBar implements ChangeListener{
        
        private final JMenuItem aboutItem;
        private final JMenuItem orderAdd, orderDelete, storeAdd, storeDelete, customerAdd, customerDelete, productAdd, productDelete, manuAdd, manuDelete, catAdd, catDelete;
        
        public StoreMenuBar(){
            
            orderAdd = createMenuItem("Rendelés hozzáadása", editorAddImage, newOrderAction);
            orderDelete = createMenuItem("Rendelés törlése", editorDeleteImage, deleteOrderAction);
            storeAdd = createMenuItem("Bolt hozzáadása", editorAddImage, newStoreAction);
            storeDelete = createMenuItem("Bolt törlése", editorDeleteImage, deleteStoreAction);
            customerAdd = createMenuItem("Vásárló hozzáadása", editorAddImage, newCustomerAction);
            customerDelete = createMenuItem("Vásárló törlése", editorDeleteImage, deleteCustomerAction);
            productAdd = createMenuItem("Termék hozzáadása", editorAddImage, newProductAction);
            productDelete = createMenuItem("Termék törlése", editorDeleteImage, deleteProductAction);
            manuAdd = createMenuItem("Gyártó hozzáadása", editorAddImage, newManufacturerAction);
            manuDelete = createMenuItem("Gyártó törlése", editorDeleteImage, deleteManufacturerAction);
            catAdd = createMenuItem("Kategória hozzáadása", editorAddImage, newCategoryAction);
            catDelete = createMenuItem("Kategória törlése", editorDeleteImage, deleteCategoryAction);
            
            aboutItem = createMenuItem("Névjegy", aboutInfoImage, aboutAction);
        }
        
        @Override
        public void stateChanged(ChangeEvent e) {
            removeAll();
            repaint();
            switch (jTabbedPane.getTitleAt(jTabbedPane.getSelectedIndex())) {
                case "Rendelés":
                    add(orderAdd);
                    add(new JSeparator(JSeparator.VERTICAL));
                    add(orderDelete);
                    add(new JSeparator(JSeparator.VERTICAL));
                    break;
                case "Raktár":
                    add(storeAdd);
                    add(new JSeparator(JSeparator.VERTICAL));
                    add(storeDelete);
                    add(new JSeparator(JSeparator.VERTICAL));
                    break;
                case "Vásárló":
                    add(customerAdd);
                    add(new JSeparator(JSeparator.VERTICAL));
                    add(customerDelete);
                    add(new JSeparator(JSeparator.VERTICAL));
                    break;
                case "Termék":
                    add(productAdd);
                    add(new JSeparator(JSeparator.VERTICAL));
                    add(productDelete);
                    add(new JSeparator(JSeparator.VERTICAL));
                    break;
                case "Gyártó":
                    add(manuAdd);
                    add(new JSeparator(JSeparator.VERTICAL));
                    add(manuDelete);
                    add(new JSeparator(JSeparator.VERTICAL));
                    break;
                case "Kategória":
                    add(catAdd);
                    add(new JSeparator(JSeparator.VERTICAL));
                    add(catDelete);
                    add(new JSeparator(JSeparator.VERTICAL));
                    break;
            }
            
            add(aboutItem);
            add(new JSeparator(JSeparator.VERTICAL));
            
        }
        
        private JMenuItem createMenuItem(String menuName, final ImageIcon icon, final Action action){
            final JMenuItem item = new JMenuItem(menuName);
            item.setAction(action);
            if(icon != null) item.setIcon(icon);
            item.setCursor(new Cursor(Cursor.HAND_CURSOR));
            return item;
        }
    }
}
