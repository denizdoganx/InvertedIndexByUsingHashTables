

import java.util.Arrays;
import java.util.Iterator;

public class HashedDictionary<K, V> implements DictionaryInterface<K, V> {
	private static final int DEFAULT_CAPACITY = 19;
	private TableEntry<K, V>[] hashTable;
	private int numOfEntries;
	public static int SSF_PAF;
	public static int LinearProbe_DoubleHashing;
	public static double LOAD_FACTOR;
	private final int primeNumber = 7;
	private int tableSize;
	private long totalCollisionCount;

	public HashedDictionary() {
		this(DEFAULT_CAPACITY);
	}
	public HashedDictionary(int initialCapacity) {
		totalCollisionCount = 0;
		tableSize = getNextPrime(initialCapacity);
		@SuppressWarnings("unchecked")
		TableEntry<K, V>[] temp = (TableEntry<K, V>[]) new TableEntry[tableSize];
		hashTable = temp;
		numOfEntries = 0;
	}

	@Override
	public V put(K key, V value) {
		V oldValue = null;
		if(key == null || value == null)
			throw new IllegalArgumentException("Cannot add null to a dictionary.");
		else {
			
			int index = findIndex(key);
			if(hashTable[index] == null) {
				hashTable[index] = new TableEntry<>(key, value);
				numOfEntries++;
			}
			else {
				hashTable[index].add(value);
			}
			if(numOfEntries / hashTable.length > LOAD_FACTOR) {
				resize();
			}
		}
		return oldValue;
	}
	//linear probing or double hashing is called in find index
	private int findIndex(K key) {
		int index;
		index = getHashIndex(key);
		if(hashTable[index] != null && !hashTable[index].getKey().equals(key))
			totalCollisionCount++;
		if(LinearProbe_DoubleHashing == 1) {
			index = linearProbing(index, key);	
		}
		else {
			index = doubleHashing(index, key);
		}
		return index;
	}
	@Override
	public V remove(K key) {
		V removedValue = null;
		int index = getHashIndex(key);
		if(LinearProbe_DoubleHashing == 1) {
			index = locateForLinearProbing(index, key);
		}
		else {
			index = locateForLinearProbing(index, key);
		}
		if(index != - 1) {
			hashTable[index] = null;
			numOfEntries--;
			System.out.println("Successfully removed.");
		}
		else {
			System.out.println("Uninstall failed");
		}
		return removedValue;
	}

	@Override
	public V getValue(K key) {
		int index;
		index = getHashIndex(key);
		if(LinearProbe_DoubleHashing == 1) {
			index = locateForLinearProbing(index, key);
		}
		else {
			index = locateForDoubleHashing(index, key);
		}
		if(index != - 1)
			hashTable[index].showValues();
		else
			System.out.println("Not found !!!");
		return null;
	}

	@Override
	public boolean contains(K key) {
		return false;
	}

	@Override
	public Iterator<K> getKeyIterator() {
		return new KeyIterator();
	}

	@Override
	public Iterator<V> getValueIterator() {
		return new ValueIterator();
	}

	@Override
	public boolean isEmpty() {
		return numOfEntries == 0;
	}

	@Override
	public int getSize() {
		return numOfEntries;
	}
	//here is a prime number for table size
	private int getNextPrime(int primeNumber) {
		boolean flag;
		for(int i = primeNumber + 1;i < primeNumber * 2; i++) {
			flag = true;
			for(int j = 2; j < Math.sqrt(primeNumber) + 1; j++) {
				if(i % j == 0) {
					flag = false;
				}
			}
			if(flag) {
				tableSize = i;
				return i;
			}
		}
		return 0;
	}
	
	@SuppressWarnings("unchecked")
	private void resize() {
		TableEntry<K, V>[] oldTable = hashTable;
		int oldSize = hashTable.length;
		int newSize = getNextPrime(oldSize * 2);
		TableEntry<K, V>[] temp = (TableEntry<K, V>[]) new TableEntry[newSize];
		hashTable = temp;
		for(int i = 0;i < oldSize; i++) {
			if(oldTable[i] != null) {
				int newIndex = findIndex(oldTable[i].getKey());
				hashTable[newIndex] = oldTable[i];
			}
		}
	}
 	private int linearProbing(int index, K key) {
		boolean found = false;
		int removedStateIndex = - 1;
		while(!found && hashTable[index] != null) {
			if(hashTable[index].isIn()) {
				if(key.equals(hashTable[index].getKey()))
					found = true;
				else {
					index = (index + 1) % hashTable.length;
				}
			}
			else {
				// Save index of first location in removed state
				if(removedStateIndex == - 1)
					removedStateIndex = index;
				index = (index + 1) % hashTable.length;
			}
		}
		if (found || (removedStateIndex == -1) )
			return index; // index of either key or null
		else
			return removedStateIndex; // index of an available location
	}
	private int doubleHashing(int index, K key) {
		boolean found = false;
		int removedStateIndex = - 1;
		int firstIndex = index;
		int hashNumber, dKey, coefficent = 1;
		if(SSF_PAF == 1)
			hashNumber = SSF((String)key);
		else
			hashNumber = PAF((String)key);
		dKey = primeNumber - hashNumber % primeNumber;
		while(!found && hashTable[index] != null) {
			if(hashTable[index].isIn()) {
				if(key.equals(hashTable[index].getKey()))
					found = true;
				else {
					index = (firstIndex + (coefficent * dKey)) % hashTable.length;
				}
			}
			else {
				// Save index of first location in removed state
				if(removedStateIndex == - 1)
					removedStateIndex = index;
				index = (firstIndex + (coefficent * dKey)) % hashTable.length;
			}
			coefficent++;
		}
		if (found || (removedStateIndex == -1) )
			return index; // index of either key or null
		else
			return removedStateIndex; // index of an available location
		
	}
	public double getCollisionCount() {
		return totalCollisionCount;
	}
	private int getHashIndex(K key) {
		int index;
		if(SSF_PAF == 1)
			index = SSF((String)key);
		else
			index = PAF((String)key);
		return index % hashTable.length;
	}
	private int locateForLinearProbing(int index, K key) {
		boolean found = false;
		int result = - 1;
		while(!found && hashTable[index] != null) {
			if(hashTable[index].isIn() && key.equals(hashTable[index].getKey()))
				found = true;
			else 
				index = (index + 1) % hashTable.length;//Linear probing
		}
		if(found)
			result = index;
		return result;
	}
	private int locateForDoubleHashing(int index, K key) {
		boolean found = false;
		int result = - 1;
		int firstIndex = index;
		int hashNumber, dKey, coefficent = 1;
		if(SSF_PAF == 1)
			hashNumber = SSF((String)key);
		else
			hashNumber = PAF((String)key);
		dKey = primeNumber - hashNumber % primeNumber;
		while(!found && hashTable[index] != null) {
			if(hashTable[index].isIn() && key.equals(hashTable[index].getKey()))
				found = true;
			else 
				index = (firstIndex + (coefficent * dKey)) % hashTable.length;//double hashing
			coefficent++;
		}
		if(found)
			result = index;
		return result;
	}
	@Override
	public void clear() {
		int index = 0;
		while(index != hashTable.length - 1) {
			if(hashTable[index] != null)
				hashTable[index] = null;
			index++;
		}
	}
	private int SSF(String word) {
		int ssf = 0;
		int chrNumber = 96;
		word = word.toLowerCase();
		for(int i = 0;i < word.length(); i++) {
			ssf += word.charAt(i) - chrNumber;
		}
		return ssf;
	}
	private int PAF(String word) {
		int paf = 0;
		int chrNumber = 96;
		int primeNumber = 33;
		int pow = word.length() - 1;
		word = word.toLowerCase();
		for(int i = 0;i < word.length(); i++) {
			paf += Math.pow(primeNumber, pow) * (word.charAt(i) - chrNumber);
			pow--;
		}
		return paf;
	}
	private class TableEntry<S, T> {
		private SingleLinkedList sllOfSameWords;
		private S key;
		private T value;
		private TableEntry(S key, T value) {
			this.key = key;
			this.value = value;
			sllOfSameWords = new SingleLinkedList();
			add(value);
		}
		private S getKey() {
			return key;
		}
		private void showValues() {
			sllOfSameWords.display();
		}
		
		private boolean isIn() {
			return value != null;
		}
		private void add(T fileName) {
			Node tempFileName = (Node) fileName;
			sllOfSameWords.add(tempFileName);
		}
		
	}
	private class KeyIterator implements Iterator<K> {
		private Iterator<TableEntry<K, V>> traverser;
		private KeyIterator() {
			@SuppressWarnings("unchecked")
			TableEntry<K, V>[] tempDictionary = (TableEntry<K, V>[]) new TableEntry[numOfEntries];
			int index = 0;
			for(int i = 0; i < hashTable.length; i++) {
				if(hashTable[i] != null && hashTable[i].getKey() != null) {
					tempDictionary[index] = hashTable[i];
					index++;
				}	
			}
			traverser = Arrays.asList(tempDictionary).iterator();
		}
		@Override
		public boolean hasNext() {
			return traverser.hasNext();
		}
		@Override
		public K next() {
			TableEntry<K, V> nextEntry = traverser.next();
			return (K)nextEntry.getKey();
		}
		public void remove() {
			throw new UnsupportedOperationException();
		} 
	}
	private class ValueIterator implements Iterator<V> {

		@Override
		public boolean hasNext() {
			return false;
		}

		@Override
		public V next() {
			return null;
		}
		
	}
}
