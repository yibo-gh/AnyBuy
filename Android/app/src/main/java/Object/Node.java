package Object;

public class Node implements java.io.Serializable {
	
	// Object Hash for validation use.
	private static final long serialVersionUID = -5305311271055485376L;
	
	Object o;
	Node next;
	Node prev;
	
	Node (Object obj) {
		this.o = obj;
		next = null;
		prev = null;
	}
	
	public Object getObject() { return this.o;}
	public Node getNext() { return this.next;}
	public Node getPrev() {return this.prev;}
}
