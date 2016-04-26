
public class LinkedList<T> {
	protected LinkedListNode<T> head;
	protected LinkedListNode<T> tail;

	/**
	 * constructor, create the list and set head to null
	 */
	public LinkedList() {
		LinkedListNode<T> head = new LinkedListNode<T>();
		head.setData(null,null);
		head.setNext(null);
		tail = head;

	}

	/**
	 * Get data stored in head node of list.
	 **/
	public T getFirstStart() {
		return head.getStart();
	}
	
	/**
	 * Get data stored in head node of list.
	 **/
	public T getFirstEnd() {
		return head.getEnd();
	}

	/**
	 * Get the head node of the list.
	 **/
	public LinkedListNode<T> getFirstNode() {
		return head;
	}

	/**
	 * Get data stored in tail node of list.
	 **/
	public T getLastStart() {
		return tail.getStart();
	}
	
	/**
	 * Get data stored in tail node of list.
	 **/
	public T getLastEnd() {
		return tail.getEnd();
	}

	
	
	/**
	 * Get the tail node of the list.
	 **/
	public LinkedListNode<T> getLastNode() {
		return tail;
	}

	/**
	 * Insert a new node with data at the head of the list.
	 **/
	
	public void insertFirst(T start, T end) {

		// create a new object called newNode to hold data and next
		LinkedListNode<T> newNode = new LinkedListNode<T>();
		// set its data to the new data, and node point to the original head
		newNode.setNext(head);
		newNode.setPre(null);
		newNode.setData(start,end);
		
		// now the head is a new one
		head = newNode;
		// if this is inserting the first element to the list, the also let tail
		// point to this node
		if (tail.getEnd() == null)
			tail = head;

	}

	/**
	 * Insert a new node with data after currentNode
	 **/
	public void insertAfter(LinkedListNode<T> currentNode, T start, T end) {
		// create a new nude
		LinkedListNode<T> newNude = new LinkedListNode<T>();
		// set the data
		newNude.setData(start, end);

		if (currentNode.getNext() == null) {
			newNude.setPre(currentNode);
			newNude.setNext(null);
			currentNode.setNext(newNude);
			tail.setNext(newNude);
			tail = newNude;
		}

		else {
			// set the next of the original and the new nude
			newNude.setNext(currentNode.getNext());
			currentNode.setNext(newNude);
			// set previous of the new nude
			newNude.setPre(currentNode);
			// set previous of the one after the current node
			currentNode.getNext().setPre(newNude);

			// update the tail
			LinkedListNode<T> lastNude = new LinkedListNode<T>();
			lastNude = (LinkedListNode<T>) head;
			while (lastNude.getNext() != null) {
				lastNude = lastNude.getNext();
			}

			if (lastNude.getPre() != null) {
				lastNude.getPre().setNext(lastNude);
			}
			tail = lastNude;
		}

	}

	/**
	 * Insert a new node with data at the tail of the list.
	 **/
	public void insertLast(T start, T end) {

		// create and set the data for the new node
		LinkedListNode<T> newNude = new LinkedListNode<T>();
		newNude.setData(start, end);
		// put the node into the list
		if (isEmpty())
		{
			insertFirst(start, end);
		}
		/*else if (tail == null) {
			newNude.setPre(null);
			newNude.setNext(null);
			head = tail = newNude;
			return;

		}*/ else {
			tail.setNext(newNude);
			newNude.setPre(tail);
			newNude.setNext(null);
			tail = newNude;

		}

	}

	/**
	 * Remove the first node
	 **/
	public void deleteFirst() {

		// update the previous point of the second element
		if (head.getNext() != null) {
			head = head.getNext();

			// make the previous pointer of the new head point to null
			((LinkedListNode<T>) head).setPre(null);
			// ((LinkedListNode<T>) head.getNext()).setPre((LinkedListNode<T>)
			// head);
		} else {
			head = null;
			tail = null;
		}

	}

	/**
	 * Remove the last node
	 **/
	public void deleteLast() {
		if (size() == 1)
			deleteFirst();
		else {
			// create a newNode to keep on track of where the last node is
			LinkedListNode<T> newNode = new LinkedListNode<T>();
			// set it to the head
			newNode = head;

			// find the node before the last node
			while (newNode.getNext() != tail) {
				// set it to the one after itself
				newNode = newNode.getNext();
			}
			// set its pointer to null, which is deleting the last node
			newNode.setNext(null);
			// set this new node to be the tail;
			tail = newNode;
		}

		// update the tail
		tail = tail.getPre();
		// update the next pointer
		tail.setNext(null);
	}

	/**
	 * Remove node following currentNode If no node exists (i.e., currentNode is
	 * the tail), do nothing
	 **/
	public void deleteNext(LinkedListNode<T> currentNode) {
		// check if the node is tail
		if (currentNode.getNext() != null) {

			if (currentNode.getNext().getNext() != null) {
				// update the next pointer
				currentNode.setNext(currentNode.getNext().getNext());
				// update the previous pointer
				(currentNode.getNext().getNext()).setPre(currentNode);
			} else {
				currentNode.setNext(null);
				tail = currentNode;
			}

		}

		// update the tail
		LinkedListNode<T> lastNude = new LinkedListNode<T>();
		lastNude = (LinkedListNode<T>) head;
		while (lastNude.getNext() != null) {
			lastNude = lastNude.getNext();
		}
		if (lastNude.getPre() != null) {
			lastNude.getPre().setNext(lastNude);
		}
		tail = lastNude;

	}

	/**
	 * Return the number of nodes in this list.
	 **/
	public int size() {
		// initialize the int size
		int size = 0;

		// create a newNode to keep on track of where the last node is
		LinkedListNode<T> newNode = new LinkedListNode<T>();
		// set it to the head
		newNode = head;

		// as the node is not, indicating that it still holds a value
		while (newNode != null) {
			// size increase by one
			size++;
			// set the newNode to the one after itself
			newNode = newNode.getNext();
		}

		return size;
	}

	/**
	 * Check if list is empty. return true if list contains no items.
	 **/
	public boolean isEmpty() {
		// if the head of the list does not hold anything, then the list is
		// empty
		if (head == null)
			return true;

		return false;
	}

	/**
	 * Return a String representation of the list.
	 **/
	public String toString() {
		// result holds the final string, initialize it to null
		String result = "";
		// create a newNode to keep on track of where the last node is
		LinkedListNode<T> newNode = new LinkedListNode<T>();
		// set it to the head
		newNode = head;

		// as the node is not, indicating that it still holds a value
		while (newNode != null) {
			// if it is the last one of the list, which the one after it is
			// null, only adds its own value to the strinf
			if (newNode.getNext() == null)
				result = result + newNode.getStart().toString() + newNode.getEnd().toString();
			// if it is not the end of the list, adds its own value as well as a
			// pointer sign
			else
				result = result + newNode.getStart().toString() + newNode.getEnd().toString() + "->";

			// set the newNode to the one after itself
			newNode = newNode.getNext();
		}
		return result;
	}

	public LinkedListNode<T> search(T start, T end) {
		//a temp node to help go through the list
		LinkedListNode<T> target = new LinkedListNode<T>();
		//assign it to the head
		target = (LinkedListNode<T>) head;
		//while data does not match, check the next
		while (!(target.getStart().equals(start)&&target.getEnd().equals(end)) && target.getNext() != null) {
			target = target.getNext();
		}
		return target;
	}

}
