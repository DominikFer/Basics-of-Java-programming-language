package hr.fer.zemris.java.hw07.students;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * StudentListModel which models the data for
 * using on the {@link JList} components.
 */
public abstract class StudentListModel extends AbstractListModel<Student> implements ListModel<Student>, StudentDBModificationListener {
	
	private static final long serialVersionUID = 1L;

	private List<ListDataListener> listeners = new ArrayList<>();
	
	private StudentDatabase db;
	
	/**
	 * Constructor.
	 * 
	 * @param db	{@link StudentDatabase} which will be used as a data resource.
	 */
	public StudentListModel(StudentDatabase db) {
		this.db = db;
		
		db.addListener(this);
	}
	
    @Override
    public void studentsAdded(StudentDatabase db, int fromIndex, int toIndex) {
    	ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, fromIndex, toIndex);
		ListDataListener[] arrayListeners = listeners.toArray(new ListDataListener[0]);
		for(ListDataListener listener : arrayListeners) {
			listener.intervalAdded(event);
		}
    }
    
    @Override
    public void studentsRemoved(StudentDatabase db, int fromIndex, int toIndex) {
    	ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, fromIndex, toIndex);
		ListDataListener[] arrayListeners = listeners.toArray(new ListDataListener[0]);
		for(ListDataListener listener : arrayListeners) {
			listener.intervalAdded(event);
		}
    }
    
    @Override
    public void databaseChanged(StudentDatabase db) {
    	ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, 0, db.size());
		ListDataListener[] arrayListeners = listeners.toArray(new ListDataListener[0]);
		for(ListDataListener listener : arrayListeners) {
			listener.intervalAdded(event);
		}
    }
    
	@Override
    public void addListDataListener(ListDataListener l) {
		if(!listeners.contains(l)) {
			listeners.add(l);
		}
    }

	@Override
    public void removeListDataListener(ListDataListener l) {
		if(listeners.contains(l)) {
			listeners.remove(l);
		}
    }
	
	/**
	 * @return Returns {@link StudentDatabase} of this model.
	 */
	public StudentDatabase getDb() {
		return db;
	}
}