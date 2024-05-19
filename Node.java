

public class Node {
	private String fileName;
	private int frequency;
	private Node next;
	public Node(String fileName) {
		this.fileName = fileName;
		next = null;
	}
	public Node(String fileName, int frequency) {
		this.fileName = fileName;
		this.frequency = frequency;
	}
	public String getFileName() {
		return fileName;
	}
	public void setWord(String fileName) {
		this.fileName = fileName;
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	public Node getNext() {
		return next;
	}
	public void setNext(Node next) {
		this.next = next;
	}
}
