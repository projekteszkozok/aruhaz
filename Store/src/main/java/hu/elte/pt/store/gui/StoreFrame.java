package hu.elte.pt.store.gui;

import hu.elte.pt.store.gui.tablemodels.CategoryTableModel;
import hu.elte.pt.store.gui.tablemodels.EntityHandlerTableModel;
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
    private final JTable categoryTable, storeTable;
    private final CategoryTableModel categoryTableModel;
    private final StoreTableModel storeTableModel;
    
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
        
        categoryTableModel = new CategoryTableModel();
        categoryTable = new JTable(categoryTableModel);
        categoryTable.setAutoCreateRowSorter(true);
        categoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setCellEditorListener(categoryTable, categoryTableModel);
         
        storeTableModel = new StoreTableModel();
        storeTable = new JTable(storeTableModel);
        storeTable.setAutoCreateRowSorter(true);
        storeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setCellEditorListener(storeTable, storeTableModel);
               
        jTabbedPane.add("Kategória", new JScrollPane(categoryTable));
        jTabbedPane.add("Raktár", new JScrollPane(storeTable));
        
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
        
        private final JMenu categoryMenu, storeMenu;
        
        public StoreMenuBar(){
            categoryMenu = new JMenu("Kategória");
            categoryMenu.add(newCategoryAction);
            categoryMenu.add(deleteCategoryAction);
            
            storeMenu = new JMenu("Raktár");
            storeMenu.add(newStoreAction);
            storeMenu.add(deleteStoreAction);
            
        }
        
        @Override
        public void stateChanged(ChangeEvent e) {
            removeAll();
            repaint();
            switch (jTabbedPane.getTitleAt(jTabbedPane.getSelectedIndex())) {
                case "Kategória":
                    add(categoryMenu);
                    break;
                case "Raktár":
                    add(storeMenu);
                    break;
            }

        }
    
    }
}
