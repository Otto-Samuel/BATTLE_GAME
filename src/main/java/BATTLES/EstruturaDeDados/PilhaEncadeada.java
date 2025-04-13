package BATTLES.EstruturaDeDados;

import java.util.NoSuchElementException;

public class PilhaEncadeada<T> {
    private Node<T> top;
    private int size;
    
    public PilhaEncadeada() {
        top = null;
        size = 0;
    }

    public int size(){
        return size;
    }
    
    public void push(T data) {
        Node<T> newNode = new Node<>(data);
        newNode.next = top;
        top = newNode;
        size++;
    }
    
    public T pop() {
        if (top == null) {
            throw new NoSuchElementException();
        }
        T data = top.data;
        top = top.next;
        size--;
        return data;
    }
    
    public T peek() {
        if (top == null) {
            throw new NoSuchElementException();
        }
        return top.data;
    }
    
    public boolean isEmpty() {
        return top == null;
    }
}