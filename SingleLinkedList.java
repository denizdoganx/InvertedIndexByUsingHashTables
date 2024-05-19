

public class SingleLinkedList {
	//I used my own linked list class to store file references.
	private Node head;
	private int numberOfDocument;
	public SingleLinkedList() {
		head = null;
	}
	public void add(Node newNode) {
		Node temp = head;
		if(temp == null) {
			head = newNode;
			newNode.setFrequency(1);
			numberOfDocument++;
		}
		else {
			Node prev = temp;
			while(temp != null && !temp.getFileName().equals(newNode.getFileName())) {
				prev = temp;
				temp = temp.getNext();
			}
			if(temp != null) {
				temp.setFrequency(temp.getFrequency() + 1);
			}
			else {
				prev.setNext(newNode);
				newNode.setFrequency(1);
				numberOfDocument++;
			}
		}
	}
	public void display() {
		Node temp = head;
		String space;
		if(numberOfDocument == 0) {
			System.out.println("Not found !!!");
		}
		else {
			System.out.println(numberOfDocument + " documents found.");
			while(temp != null) {
				System.out.print(temp.getFrequency());
				space = String.valueOf(temp.getFrequency());
				for(int i = 0;i < 8 - space.length(); i++)
					System.out.print("-");
				System.out.println(temp.getFileName());
				temp = temp.getNext();
			}
		}
		System.out.println();
	}
	public Node getHead() {
		return head;
	}
}
