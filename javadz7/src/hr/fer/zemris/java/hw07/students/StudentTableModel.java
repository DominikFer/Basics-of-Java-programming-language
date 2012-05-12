package hr.fer.zemris.java.hw07.students;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 * StudentListModel which models the data for
 * using on the {@link JTable} components.
 */
public abstract class StudentTableModel extends AbstractTableModel implements TableModel, StudentDBModificationListener {
	
	private static final long serialVersionUID = 1L;

	private List<TableModelListener> listeners = new ArrayList<>();
	
	private StudentDatabase db;
	
	/**
	 * Constructor.
	 * 
	 * @param db	{@link StudentDatabase} which will be used as a data resource.
	 */
	public StudentTableModel(StudentDatabase db) {
		this.db = db;
		
		db.addListener(this);
	}
	
    @Override
    public void studentsAdded(StudentDatabase db, int fromIndex, int toIndex) {
		TableModelEvent event = new TableModelEvent(this, fromIndex, toIndex);
        TableModelListener[] array = listeners.toArray(new TableModelListener[0]);
        for (TableModelListener l : array) {
            l.tableChanged(event);
        }
    }
    
    @Override
    public void studentsRemoved(StudentDatabase db, int fromIndex, int toIndex) {
    	TableModelEvent event = new TableModelEvent(this, fromIndex, this.getDb().size());
        TableModelListener[] array = listeners.toArray(new TableModelListener[0]);
        for (TableModelListener l : array) {
            l.tableChanged(event);
        }
    }
    
    @Override
    public void databaseChanged(StudentDatabase db) {
    	TableModelEvent event = new TableModelEvent(this, 0, this.getDb().size());
        TableModelListener[] array = listeners.toArray(new TableModelListener[0]);
        for (TableModelListener l : array) {
            l.tableChanged(event);
        }
    }
    
	@Override
	public void addTableModelListener(TableModelListener l) {
		if(!listeners.contains(l)) {
			listeners.add(l);
		}
	}
	
	@Override
	public void removeTableModelListener(TableModelListener l) {
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