package Object;

public class LinkedList implements java.io.Serializable{
	
	private static final long serialVersionUID = 7326384737417185121L;
	
	public Node head = null;
	public Node end = null;
	private int length = 0;
	
	public void insert(Object o) {

		Node temp = new Node(o);
		if (head == null) head = temp;
		if (end == null) end = temp;
		else end.next = temp;
		temp.prev = end;
		end = temp;
		length++;
	}
	
	public int getLength() {return this.length;}
	
	public void delete(int index) {
		Node temp = this.head;
		for (int i = 0; i <= index; i++) {
			if (i == index) {
				if (temp.next == null) end = temp.prev;
				temp.prev.next = temp.next;
				if (temp.prev == null) head = temp.next;
				temp.next.prev = temp.prev;
				this.length--;
			}
			temp = temp.next;
		}
	}
}