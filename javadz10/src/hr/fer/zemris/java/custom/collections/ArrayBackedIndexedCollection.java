package hr.fer.zemris.java.custom.collections;

public class ArrayBackedIndexedCollection {

	/**
	 * If capacity is not set, <code>DEFAULT_CAPACITY</code> is used
	 * for the size of the initial collection.
	 */
	private static final int DEFAULT_CAPACITY = 16;
	
	private int size = 0;
	private int capacity;
	private Object[] elements;
	
	/**
	 * Creates new collection with the <code>DEFAULT_CAPACITY</code> size.
	 */
	public ArrayBackedIndexedCollection() {
		this.elements = new Object[DEFAULT_CAPACITY];
		this.capacity = DEFAULT_CAPACITY;
	}
	
	/**
	 * Creates new collection with the <code>initialCapacity</code> size.
	 * 
	 * @param initialCapacity Size of the initial collection.
	 * @throws IllegalArgumentException Capacity must be larger than 0.
	 */
	public ArrayBackedIndexedCollection(int initialCapacity) {
		if(initialCapacity < 1)
			throw new IllegalArgumentException("Capacity must be larger than 0.");
		
		this.elements = new Object[initialCapacity];
		this.capacity = initialCapacity;
	}
	
	/**
	 * Doubles the collection capacity and moves elements from old collection to new one.
	 */
	private void reallocate() {
		Object[] newArray = new Object[this.capacity*2];
		for(int i = 0; i < this.capacity; i++) {
			newArray[i] = this.elements[i];
		}
		this.capacity *= 2;
		this.elements = newArray;
	}
	
	/**
	 * Adds given <code>value</code> into collection at the first empty place.
	 * 
	 * @param value Object you want to add.
	 * @throws IllegalArgumentException Value cannot be <code>null</code>.
	 */
	public void add(Object value) {
		if(value == null)
			throw new IllegalArgumentException();
		
		// Reallocate, double the size
		if(this.size == this.capacity) {
			this.reallocate();
		}
		
		this.elements[this.size] = value;
		this.size++;
	}
	
	/**
	 * Inserts given <code>value</code> at given <code>position</code> in array.
	 * 
	 * @param value Object value you want to add.
	 * @param position Position where you want to insert your value.
	 * @throws IndexOutOfBoundsException Valid <code>position</code> is <code>0</code> to <code>size</code>.
	 */
	public void insert(Object value, int position) {
		if(position < 0 || position > (this.size))
			throw new IndexOutOfBoundsException();
		
		// Reallocate, double the size
		if(this.size == this.capacity) {
			this.reallocate();
		}
		
		if(this.elements[position] == null) { // Last position
			this.elements[position] = value;
		} else {
			for(int i = size; i > position; i--) {
				this.elements[i] = this.elements[i-1];
			}
			this.elements[position] = value;
		}
		
		this.size++;
	}
	
	/**
	 * Removes the object that is stored at position <code>index</code>.
	 * 
	 * @param index Position of the object which will be removed.
	 * @throws IndexOutOfBoundsException Valid <code>index</code> is <code>0</code> to <code>size-1</code>.
	 */
	public void remove(int index) {
		if(index < 0 || index > (this.size-1))
			throw new IndexOutOfBoundsException();
		
		for(int i = index; i < size-1; i++) {
			this.elements[i] = this.elements[i+1];
		}
		this.elements[size-1] = null;
		
		this.size--;
	}
	
	/**
	 * Returns the index of the first occurrence of given <code>value</code>.
	 * 
	 * @param value Object you want to search for.
	 * @return Index of the first occurrence if found, else <code>-1</code>.
	 */
	public int indexOf(Object value) {
		for(int i = 0; i < this.size; i++) {
			if(this.elements[i].equals(value))
				return i;
		}
		
		return -1;
	}
	
	/**
	 * Searches the collection for the requested <code>value</code.
	 * 
	 * @param value Object you want to search for.
	 * @return <code>true</code> if the collection contains given <code>value</code>, else <code>false</code>
	 */
	public boolean contains(Object value) {
		for(int i = 0; i < this.size; i++) {
			if(this.elements[i].equals(value))
				return true;
		}
		
		return false;
	}
	
	/**
	 * Returns the object at given <code>index</code>.
	 * 
	 * @param index Position of the requested object.
	 * @return Object at given index.
	 * @throws IndexOutOfBoundsException Valid <code>index</code> is <code>0</code> to <code>size-1</code>.
	 */
	public Object get(int index) {
		if(index < 0 || index > (this.size-1))
			throw new IndexOutOfBoundsException();
		
		return this.elements[index];
	}
	
	/**
	 * Removes all elements from collection.
	 */
	public void clear() {
		this.size = 0;
		
		for(int i = 0; i < size; i++) {
			this.elements[i] = null;
		}
	}
	
	/**
	 * Checks if collection is empty.
	 * 
	 * @return <code>true</code> if collection is empty, else <code>false</code>.
	 */
	public boolean isEmpty() {
		return (this.size == 0);
	}
	
	/**
	 * Returns number of currently stored objects in collection.
	 * 
	 * @return Collection size.
	 */
	public int size() {
		return this.size;
	}
}