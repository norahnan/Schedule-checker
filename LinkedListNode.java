
public class LinkedListNode<T> {

	
	private T start;
	private T end;
	private LinkedListNode<T> next = null;
	private LinkedListNode<T> previous;

	
	public void setPre(LinkedListNode<T> myNude)
	{
		previous = myNude;
	}
	
	public LinkedListNode<T> getPre()
	{
		return previous;
	}
	
	
	/**
	 * Set the data stored at this node.
	 **/
	public void setData( T start, T end )
	{
		this.start = start;
		this.end = end;
	}
	 
	/**
	 * Get the data stored at this node.
	 **/
	public T getStart()
	{
		return start;
	}
	
	/**
	 * Get the data stored at this node.
	 **/
	public T getEnd()
	{
		return end;
	}
	
	
	 
	/**
	 * Set the next pointer to passed node.
	 **/
	public void setNext( LinkedListNode<T> node )
	{
		next = node;
	}
	 
	/**
	 * Get (pointer to) next node.
	 **/
	public LinkedListNode<T> getNext()
	{
		return next;
	}
	/**
	 * Returns a String representation of this node.
	 **/
	public String toString()
	{
		if (start != null && end != null)
		{
			//return  (String)start + (String)end ;
			return  start.toString() + end.toString() ;

		}
		else return "";
	}
	
	
}
