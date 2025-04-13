package BATTLES.EstruturaDeDados;

import java.util.NoSuchElementException;

public class FilaEncadeada<T> {
    private Node<T> front;
    private Node<T> rear;
    public int size;
    
    public FilaEncadeada() {
        front = null;
        rear = null;
        size = 0;
    }
    
    public void enqueue(T data) {
        Node<T> newNode = new Node<>(data);
        if (rear == null) {
            front = newNode;
            rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }
    
    public T dequeue() {
        if (front == null) {
            throw new NoSuchElementException();
        }
        T data = front.data;
        front = front.next;
        if (front == null) {
            rear = null;
        }
        size--;
        return data;
    }
    
    public boolean isEmpty() {
        return front == null;
    }

    public int size(){
        return size;
    }
}
