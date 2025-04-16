package BATTLES.EstruturaDeDados;

import java.util.function.Predicate;
// DARIO
public class ListaEncadeada<T> {
    private Node<T> head;
    private int size;
    
    private static class Node<T> {
        T data;
        Node<T> next;
        
        public Node(T data) {
            this.data = data;
            this.next = null;
        }
    }
    
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

    public T remover(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Índice inválido: " + index);
        }

        if (index == 0) {
            T dadoRemovido = head.data;
            head = head.next;
            size--;
            return dadoRemovido;
        }
        
        Node<T> anterior = head;
        for (int i = 0; i < index - 1; i++) {
            anterior = anterior.next;
        }
        
        T dadoRemovido = anterior.next.data;
        anterior.next = anterior.next.next;
        size--;
        
        return dadoRemovido;
    }
    
    public boolean remover(T data) {
        if (head == null) return false;

        if (head.data.equals(data)) {
            head = head.next;
            size--;
            return true;
        }
        
        Node<T> current = head;
        while (current.next != null && !current.next.data.equals(data)) {
            current = current.next;
        }
        
        if (current.next != null) {
            current.next = current.next.next;
            size--;
            return true;
        }
        
        return false;
    }

    public T buscar(Predicate<T> criterio) {
        Node<T> current = head;
        while (current != null) {
            if (criterio.test(current.data)) {
                return current.data;
            }
            current = current.next;
        }
        return null;
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

    public int tamanho() {
        return size;
    }

    public boolean estaVazia() {
        return size == 0;
    }

    public boolean contem(T data) {
        Node<T> current = head;
        while (current != null) {
            if (current.data.equals(data)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }
}