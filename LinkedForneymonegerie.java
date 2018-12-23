package linked_forneymonegerie;

import java.util.NoSuchElementException;

public class LinkedForneymonegerie implements LinkedForneymonegerieInterface {

	// Fields
	// -----------------------------------------------------------
	private ForneymonType head;
	private int size, typeSize, modCount;

	// Constructor
	// -----------------------------------------------------------
	LinkedForneymonegerie() {
		size = 0;
		typeSize = 0;
		modCount = 0;
		head = null;
	}

	// Methods
	// -----------------------------------------------------------
	public boolean empty() {
		return size == 0;
	}

	public int size() {
		return size;
	}

	public int typeSize() {
		return typeSize;
	}

	public boolean collect(String toAdd) {
		boolean thereSheIs = true;
		if (empty()) {
			head = new ForneymonType(toAdd, 1);
			head.next = null;
			head.prev = null;
			typeSize++;
		} else {
			ForneymonType typeToAdd = findType(toAdd);
			if (typeToAdd != null) {
				typeToAdd.count++;
				thereSheIs = false;
			} else if (typeToAdd == null) {
				ForneymonType lastOne = findLast();
				lastOne.next = new ForneymonType(toAdd, 1);
				lastOne.next.prev = lastOne;
				typeSize++;
			}
		}
		modCount++;
		size++;
		return thereSheIs;
	}

	public boolean release(String toRemove) {
		if (empty()) {
			return false;

		}
		ForneymonType typeToRemove = findType(toRemove);
		if (typeToRemove != null) {
			if (typeToRemove.count > 1) {
				typeToRemove.count--;
			} else if (typeToRemove.count == 1) {
				removeNodeReferences(typeToRemove);
			}
		} else {
			return false;
		}
		modCount++;
		size--;
		return true;

	}

	public void releaseType(String toNuke) {
		ForneymonType typeToRelease = findType(toNuke);
		if (typeToRelease == null) {
		} else {
			removeNodeReferences(typeToRelease);
			modCount++;
			size -= typeToRelease.count;
		}
	}

	public int countType(String toCount) {
		ForneymonType typeToCount = findType(toCount);
		if (typeToCount == null) {
			return 0;
		}
		return typeToCount.count;
	}

	public boolean contains(String toCheck) {
		return findType(toCheck) != null;
	}

	public String rarestType() {
		if (empty()) {
			return null;
		}
		int rarestForneymonCount = head.count;
		String rarestForneymonType = head.type;
		ForneymonType current = head;
		do {
			if (current.count <= rarestForneymonCount) {
				rarestForneymonType = current.type;
				rarestForneymonCount = current.count;
			}
			current = current.next;

		} while (current != null);
		return rarestForneymonType;
	}

	public LinkedForneymonegerie clone() {
		LinkedForneymonegerie deepCopy = new LinkedForneymonegerie();
		deepCopy.size = size;
		deepCopy.typeSize = typeSize;
		deepCopy.modCount = modCount;
		if (empty()) {
			return deepCopy;
		}
		ForneymonType current = head;
		do {
			if (current == head) {
				deepCopy.head = new ForneymonType(current.type, current.count);
			} else {
				ForneymonType lastForneymonType = deepCopy.findLast();
				lastForneymonType.next = new ForneymonType(current.type, current.count);
				lastForneymonType.next.prev = lastForneymonType;
			}
			current = current.next;
		} while (current != null);
		return deepCopy;
	}

	public void trade(LinkedForneymonegerie other) {
		ForneymonType tempHead = head;
		int tempSize = size;
		int tempTypeSize = typeSize;
		int tempModCount = modCount;
		head = other.head;
		size = other.size;
		typeSize = other.typeSize;
		modCount = other.modCount;
		other.head = tempHead;
		other.size = tempSize;
		other.typeSize = tempTypeSize;
		other.modCount = tempModCount;
	}

	public LinkedForneymonegerie.Iterator getIterator() {
		if (empty()) {
			throw new IllegalStateException();
		}
		return new Iterator(this);
	}

	public String toString() {
		String stringVersion = "[ ";
		if (!empty()) {
			ForneymonType current = head;
			while (current.next != null) {
				stringVersion += current + ", ";
				current = current.next;
			}
			stringVersion += current;

		}
		stringVersion += " ]";
		return stringVersion;
	}

	// -----------------------------------------------------------
	// Static methods
	// -----------------------------------------------------------

	public static LinkedForneymonegerie diffMon(LinkedForneymonegerie y1, LinkedForneymonegerie y2) {
		LinkedForneymonegerie diffForneymonegerie = y1.clone();
		if (diffForneymonegerie.empty()) {
			return null;
		}
		ForneymonType current = diffForneymonegerie.head;
		do {
			ForneymonType weFoundIt = y2.findType(current.type);
			if (weFoundIt != null) {
				if (current.count == weFoundIt.count) {
					diffForneymonegerie.releaseType(current.type);
				}
				if (current.count > weFoundIt.count) {
					current.count = current.count - weFoundIt.count;
					diffForneymonegerie.size -= weFoundIt.count;
				}

			}
			current = current.next;
		} while (current != null);
		return diffForneymonegerie;
	}

	public static boolean sameCollection(LinkedForneymonegerie y1, LinkedForneymonegerie y2) {
		return diffMon(y1, y2).empty() && (y1.typeSize == y2.typeSize) && (y1.size == y2.size);
	}

	// Private helper methods
	// -----------------------------------------------------------
	private ForneymonType findLast() {
		if (empty()) {
			return null;
		}
		ForneymonType current = head;
		while (current.next != null) {
			current = current.next;
		}
		return current;
	}

	private ForneymonType findType(String toFind) {
		if (empty()) {
			return null;
		}
		ForneymonType current = head;
		do {
			if (toFind.equals(current.type)) {
				return current;
			}
			current = current.next;

		} while (current != null);
		return null;
	}

	private void removeNodeReferences(ForneymonType typeToRemove) {
		if (typeToRemove == head) {
			head = head.next;
		} else if (typeToRemove.next == null) {
			typeToRemove.prev.next = null;
		} else {
			typeToRemove.prev.next = typeToRemove.next;
			typeToRemove.next.prev = typeToRemove.prev;
		}
		typeSize--;
	}

	// Inner Classes
	// -----------------------------------------------------------

	public class Iterator implements LinkedForneymonegerieIteratorInterface {
		LinkedForneymonegerie owner;
		ForneymonType current;
		int itModCount;
		int currentIndexInType;

		Iterator(LinkedForneymonegerie y) {
			owner = y;
			itModCount = modCount;
			current = head;
			currentIndexInType = 0;

		}

		public boolean hasNext() {
			if (current.count == 1) {
				return current.next != null;
			}
			if (currentIndexInType == current.count - 1) {
				return current.next != null;
			}
			return true;
		}

		public boolean hasPrev() {
			if (current.count == 1) {
				return current.prev != null;
			}
			if (currentIndexInType == 0) {
				return current.prev != null;
			}
			return true;
		}

		public boolean isValid() {
			return itModCount == modCount;
		}

		public String getType() {
			return current.type;
		}

		public void next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			throwIfInvalid();
			if (currentIndexInType == current.count - 1) {
				current = current.next;
				currentIndexInType = 0;
			} else {
				currentIndexInType++;
			}
		}

		public void prev() {
			if (!hasPrev()) {
				throw new NoSuchElementException();
			}
			throwIfInvalid();
			if (currentIndexInType == 0) {
				current = current.prev;
				currentIndexInType = (current.count - 1);
			} else {
				currentIndexInType--;
			}
		}

		public void replaceAll(String toReplaceWith) {
			throwIfInvalid();
			ForneymonType typeToReplaceWith = owner.findType(toReplaceWith);
			if(typeToReplaceWith == null) {
				int storedAmount = current.count;
				owner.removeNodeReferences(current);
				ForneymonType lastOne = owner.findLast();
				lastOne.next = new ForneymonType(toReplaceWith, storedAmount);
				current = lastOne.next;
				typeSize++;
			}else {
				if(typeToReplaceWith != current) {
					typeToReplaceWith.count+= current.count;
					owner.removeNodeReferences(current);
					current = typeToReplaceWith;
				}
			}
			itModCount++;
			owner.modCount++;
		}
		
		private void throwIfInvalid() {
			if (!isValid()) {
				throw new IllegalStateException();
			}
		}

	}

	private class ForneymonType {
		ForneymonType next, prev;
		String type;
		int count;

		ForneymonType(String t, int c) {
			type = t;
			count = c;
		}

		public String toString() {
			return "\"" + this.type + "\": " + this.count;
		}
	}

}
