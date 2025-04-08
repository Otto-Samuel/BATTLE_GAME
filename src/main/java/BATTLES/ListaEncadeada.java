package BATTLES;

public class ListaEncadeada<T> {
    private Node<T> head;
    private int size;
    
    // Classe Node interna
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
    
    // Método remover por índice
    public T remover(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Índice inválido: " + index);
        }
        
        // Caso especial: remover o primeiro elemento
        if (index == 0) {
            T dadoRemovido = head.data;
            head = head.next;
            size--;
            return dadoRemovido;
        }
        
        // Para outros índices
        Node<T> anterior = head;
        for (int i = 0; i < index - 1; i++) {
            anterior = anterior.next;
        }
        
        T dadoRemovido = anterior.next.data;
        anterior.next = anterior.next.next;
        size--;
        
        return dadoRemovido;
    }
    
    // Método remover por objeto (remove a primeira ocorrência)
    public boolean remover(T dado) {
        if (head == null) return false;
        
        // Caso especial: remover o primeiro elemento
        if (head.data.equals(dado)) {
            head = head.next;
            size--;
            return true;
        }
        
        Node<T> atual = head;
        while (atual.next != null && !atual.next.data.equals(dado)) {
            atual = atual.next;
        }
        
        if (atual.next != null) {
            atual.next = atual.next.next;
            size--;
            return true;
        }
        
        return false;
    }
    
    // Outros métodos da lista...
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
    
    public int tamanho() {
        return size;
    }
    
    public boolean estaVazia() {
        return size == 0;
    }
}