package BATTLES;

public class ListaEncadeada<T> {
    private Node<T> head;
    private int size;
    
    public ListaEncadeada() {
        head = null;
        size = 0;
    }
    
    public void adicionar(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
        } else {
            Node<T> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }
    
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }
    
    // Outros métodos: remover, tamanho, etc.
}