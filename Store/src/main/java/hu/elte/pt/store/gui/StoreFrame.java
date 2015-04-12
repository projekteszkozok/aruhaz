package hu.elte.pt.store.gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * GUI Frame
 * @author Nagy Krisztián
 */
public class StoreFrame extends JFrame{
    
    public static void showError(final String message){
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
}
