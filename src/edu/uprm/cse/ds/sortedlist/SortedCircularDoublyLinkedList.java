package edu.uprm.cse.ds.sortedlist;


import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SortedCircularDoublyLinkedList<E extends Comparable<E>> implements SortedList<E> {

    private class CircularDoublyLinkedListIterator<E> implements Iterator<E>{

        private Node<E> nextNode;

        public CircularDoublyLinkedListIterator(int index) {
            this.nextNode = (Node<E>)findPosition(index);
        }

        public CircularDoublyLinkedListIterator() {
            this.nextNode = (Node<E>) header.getNext();
        }

        @Override
        public boolean hasNext() {
            return this.nextNode != header;
        }

        @Override
        public E next() {
            if (this.hasNext()){
                E result = this.nextNode.getElement();
                this.nextNode = this.nextNode.getNext();
                return result;
            }
            else {
                throw new NoSuchElementException();
            }
        }
    }

    private class ReverseCircularDoublyLinkedListIterator<E> implements ReverseIterator<E>{

        private Node<E> prevNode;

        public ReverseCircularDoublyLinkedListIterator(int index) {
            this.prevNode = (Node<E>)findPosition(index);
        }

        public ReverseCircularDoublyLinkedListIterator() {
            this.prevNode = (Node<E>) header.getPrev();
        }

        @Override
        public boolean hasPrevious() {
            return this.prevNode != header;
        }

        @Override
        public E previous() {
            if (this.hasPrevious()){
                E result = this.prevNode.getElement();
                this.prevNode = this.prevNode.getPrev();
                return result;
            }
            else {
                throw new NoSuchElementException();
            }
        }
    }

    private static class Node<E> {
        private E element; // reference to value stored in the Node
        private Node<E> next;  // reference to next Node in the chain
        private Node<E> prev; // reference to previous Node in the chain

        public Node<E> getPrev() {
            return prev;
        }

        public void setPrev(Node<E> prev) {
            this.prev = prev;
        }

        public Node(E element, Node<E> next, Node<E> prev) {
            this.element = element;
            this.next = next;
            this.prev = prev;

        }

        public Node(){
            this.element = null;
            this.next = null;
            this.prev = null;

        }

        public E getElement() {
            return element;
        }

        public void setElement(E element) {
            this.element = element;
        }

        public Node<E> getNext() {
            return next;
        }

        public void setNext(Node<E> next) {
            this.next = next;
        }
    }

    private Node<E> header;
    private int currentSize;

    public SortedCircularDoublyLinkedList() {
        this.currentSize = 0;
        this.header = new Node<>();
        this.header.setNext(header);
        this.header.setPrev(header);
        this.currentSize = 0;
    }

    @Override
    /*
    Receive an object, adds it to the list and returns true if the object was added and false if it wasn't.
     */
    public boolean add(E obj) {
        if(obj==null){//if the object is null, it doesn't add it
            return false;
        }
        else{
            Node<E> newNode = new Node<>();
            newNode.setElement(obj);
            if(this.isEmpty()){
                newNode.setNext(this.header);
                newNode.setPrev(this.header);
                this.header.setNext(newNode);
                this.header.setPrev(newNode);
                this.currentSize++;
                return true;
            }
            else{
                for (Node temp = this.header.getNext(); temp != this.header; temp = temp.getNext()) {
                    if(newNode.getElement().compareTo((E)temp.getElement())<0){
                        newNode.setNext(temp);
                        newNode.setPrev(temp.getPrev());
                        temp.getPrev().setNext(newNode);
                        temp.setPrev(newNode);
                        this.currentSize++;
                        return true;
                    }
                    else {
                        if (temp.getNext() == this.header) {
                            newNode.setNext(this.header);
                            newNode.setPrev(temp);
                            this.header.setPrev(newNode);
                            temp.setNext(newNode);
                            this.currentSize++;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    // This method returns the size of the list.
    public int size() {
        return this.currentSize;
    }

    @Override
    /*
    This method receives and object and it removes the object given from the list.
    It returns true if the object was removed and false if it wasn't.
     */
    public boolean remove(E obj) {
        if (this.isEmpty()){
            return false;
        }

        int targetPosition = this.firstIndex(obj);
        if (targetPosition < 0){
            return false;
        }
        else {
            Node<E> temp = null; // node just before target
            Node<E> target = null; // node to be deleted
            if (targetPosition == 0){
                temp = this.header;
                target = temp.getNext();
                temp.setNext(target.getNext());
                target.setNext(null);
                target.setElement(null);
                this.currentSize--;
                return true;
            }
            else {
                temp = this.findPosition(targetPosition-1); // node just before
                target = temp.getNext();
                temp.setNext(target.getNext());
                target.getNext().setPrev(temp);
                target.setNext(null);
                target.setPrev(null);
                target.setElement(null);
                this.currentSize--;
                return true;
            }
        }
    }

    @Override
    /*
    This method receives an index and it removes the object located in that index from the list.
    It returns true if the object was removed and false if it wasn't.
    */
    public boolean remove(int index) {
        if ((index < 0) || (index >= this.size()) ){
            throw new IndexOutOfBoundsException("index is out of bounds");
        }
        else {
            Node<E> temp = null;
            Node<E> target = null;
            if (index == 0){
                temp = this.header;

            }
            else { // case 2 - index >= 1 (in the middle)
                temp = this.findPosition(index -1 );
            }
            target = temp.getNext();
            temp.setNext(target.getNext());
            target.getNext().setPrev(temp);
            target.setNext(null);
            target.setPrev(null);
            target.setElement(null);
            this.currentSize--;
            return true;
        }
    }

    @Override
    /*
    This method receives and object and it removes every saojaod given off the list.
    It returns the number of the amount of objects that were removed.
    */
    public int removeAll(E obj) {
        int counter = 0;
        while(this.remove(obj)){
            counter++;
        }
        return counter;
    }

    @Override
    /*
    This method locates the first object on the list.
    It returns the object located in the first position.
    */
    public E first() {
        if(this.isEmpty()){
            return null;
        }
        else{
            return this.header.getNext().getElement();
        }
    }

    @Override
    /*
    This method locates the last object on the list and returns it.
    */
    public E last() {
        if(this.isEmpty()){
            return null;
        }
        else{
            return this.header.getPrev().getElement();
        }
    }

    /*
    This method can be used by other methods to locate the node on the given position and return it.
    */
    private Node<E> findPosition(int index){
        //It assumes
        int currentPosition = 0;
        Node<E> temp = null;

        temp = header.getNext();
        while (currentPosition < index){
            temp = temp.getNext();
            currentPosition++;
        }
        // temp points to the node at position index
        return temp; // return reference to the Node
    }

    @Override
    /*
    This method returns the element that is located in the node of the position given.
    */
    public E get(int index) {
        if ((index < 0) || (index >= this.size())){
            throw new IndexOutOfBoundsException("index is out range.");
        }
        Node<E> result = findPosition(index);
        return result.getElement();
    }

    @Override
    /*
    This method returns true if the list is empty or false if is not.
    */
    public void clear() {
        while(!this.isEmpty()){
            this.remove(0);
        }
    }

    @Override
    /*
    This method returns true if the list contains the given object or false if the list false if the list doesn't contain it.
    */
    public boolean contains(E e) {
        return this.firstIndex(e)>=0;
    }

    @Override
    /*
    This method returns true if the list is empty or false if is not.
    */
    public boolean isEmpty() {
        return this.size()==0;
    }

    @Override
    /*
    This method that iterates from the beginning of the list to the last.
    */
    public Iterator<E> iterator(int index) {
        return new CircularDoublyLinkedListIterator<>(index);
    }

    @Override
    /*
    This method returns the first index of the object given.
    */
    public int firstIndex(E e) {
        int counter  = 0;
        for (Node<E> temp = this.header.getNext(); temp != this.header; temp = temp.getNext(), ++counter){
            if (temp.getElement().equals(e)){
                return counter;
            }
        }
        return -1;
    }

    @Override
    /*
    This method returns the last index of the object given.
    */
    public int lastIndex(E e) {
        int counter  = this.size()-1;
        for (Node<E> temp = this.header.getPrev(); temp != this.header; temp = temp.getPrev(), --counter){
            if (temp.getElement().equals(e)){
                return counter;
            }
        }
        return -1;
    }

    @Override
    public ReverseIterator<E> reverseIterator() {
        return new ReverseCircularDoublyLinkedListIterator<>();
    }

    @Override
    public ReverseIterator<E> reverseIterator(int index) {
        return new ReverseCircularDoublyLinkedListIterator<>(index);
    }

    @Override
    public Iterator<E> iterator() {
        return new CircularDoublyLinkedListIterator<>();
    }
}
