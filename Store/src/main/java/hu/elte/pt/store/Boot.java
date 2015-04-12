package hu.elte.pt.store;

import hu.elte.pt.store.gui.StoreFrame;

/**
 * A program fő indító szálja. Amennyiben van adatbázis létező adatokkal, úgy ezen osztály segítségével
 * elérhetjük a GUI-t, amennyiben nincs először a DatebaseSetup osztályt célszerű lefuttatni.
 * @author Nagy Krisztián
 */
public class Boot {
    
    public static void main(String[] args){
        java.awt.EventQueue.invokeLater(new Runnable(){

            @Override
            public void run() {
                new StoreFrame().setVisible(true);
            }
            
        });
    }
    
}
