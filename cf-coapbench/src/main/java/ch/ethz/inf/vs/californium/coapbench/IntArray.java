package ch.ethz.inf.vs.californium.coapbench;

import java.util.Arrays;

public class IntArray {

	private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
	
	private int[] array;
	
	private int size;
	
	public IntArray() {
		this(10);
	}
	
	public IntArray(int cap) {
		this.array = new int[cap];
	}

	public void ensureCapacity(int minCapacity) {
        if (minCapacity - array.length > 0)
            grow(minCapacity);
	}
	
	private void grow(int minCapacity) {
        int oldCapacity = array.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        // minCapacity is usually close to size, so this is a win:
        array = Arrays.copyOf(array, newCapacity);
	}
	
    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        return (minCapacity > MAX_ARRAY_SIZE) ?
            Integer.MAX_VALUE :
            MAX_ARRAY_SIZE;
    }
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public void add(int e) {
    	ensureCapacity(size + 1);
        array[size++] = e;
    }
    
    public void add(int[] es) {
    	int numNew = es.length;
    	ensureCapacity(size + numNew);
    	System.arraycopy(es, 0, array, size, numNew);
        size += numNew;
    }
    
    public void add(IntArray es) {
    	int numNew = es.size();
    	if (numNew > 0) {
	    	ensureCapacity(size + numNew);
	    	System.arraycopy(es.array, 0, array, size, numNew);
	    	size += numNew;
    	}
    }
    
    public void clear() {
    	array = new int[0];
    }
    
    public int[] getArray() {
        int oldCapacity = array.length;
        if (size < oldCapacity) {
        	return Arrays.copyOf(array, size);
        } else {
        	return array;
        }
    }
}
