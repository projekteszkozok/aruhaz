package hu.elte.pt.store.gui.comboboxmodels;

import hu.elte.pt.store.gui.StoreFrame;
import hu.elte.pt.store.logic.entities.Entity;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Timer;
import hu.elte.pt.store.logic.controllers.EntityController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
/**
 *
 * @author Nagy Krisztián
 * @param <E> Entitás, mely implementálja az Entity interfész-t
 */
public class EntityComboBoxModel<E extends Entity> extends DefaultComboBoxModel<E> {
    private List<E> entities;
    private final int refreshInterval;
    private final Timer refreshTimer;
    private final EntityController<E> controller;
    
    public EntityComboBoxModel(EntityController<E> controller){
        this.controller = controller;
        entities = new ArrayList<>();
        this.refreshInterval = 60 * 1000;
        this.refreshTimer = new Timer(refreshInterval, new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                refreshEntities();      
            }
            
        });
        refreshTimer.start();
        refreshEntities();        
    }
    
    @Override
    public int getSize() {
        return entities.size();
    }

    @Override
    public E getElementAt(int index) {
        return entities.get(index);
    }

    private void refreshEntities() {
        new SwingWorker<List<E>, Void>() {

            @Override
            protected List<E> doInBackground() throws Exception {
                List<E> entities = new ArrayList<>();
                int entityCount = controller.getEntityCount();
                for (int rowIndex = 0; rowIndex < entityCount; rowIndex++) {
                    entities.add(controller.getEntityByRowIndex(rowIndex));
                }
                return entities;
            }

            @Override
            protected void done() {
                try {
                    entities = get();
                    fireContentsChanged(this, 0, getSize());
                } catch (InterruptedException | ExecutionException ex) {
                    StoreFrame.displayError("Kivétel keletkezett a kiválasztás során!");
                }
            }
        }.execute();
    }    
}
