package libraries;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class DoublyLinkedList<T> implements Iterable<T> {

    private static class ListNode<T> {  //Элемент двусвязного списка
        T value;                        //Значение узла списка
        ListNode<T> next;               //Ссылка на следующий узел списка
        ListNode<T> prev;               //Ссылка на предыдущий узел списка

        ListNode(ListNode<T> prev, T value, ListNode<T> next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
    }

    private ListNode<T> head = null;  //Ссылка на первый узнл списка
    private ListNode<T> tail = null;  //Ссылка на последний узел списка
    private int size = 0;             //Кол-во узлов списка

    private void linkFirst(T value) {
        ListNode<T> first = head;
        ListNode<T> element = new ListNode<>(null, value, first);
        head = element;
        if (first == null) {
            tail = element;
        } else {
            first.prev = element;
        }
        size++;
    }

    private void linkLast(T value) {
        ListNode<T> last = tail;
        ListNode<T> element = new ListNode<>(last, value, null);
        tail = element;
        if (last == null) {
            head = element;
        } else {
            last.next = element;
        }
        size++;
    }

    private void linkBefore(T value, ListNode<T> target) {
        ListNode<T> previous = target.prev;
        ListNode<T> element = new ListNode<>(previous, value, target);
        target.prev = element;
        if (previous == null) {
            head = element;
        } else {
            previous.next = element;
        }
        size++;
    }

    private void unlink(ListNode<T> node) {
        ListNode<T> next = node.next;
        ListNode<T> prev = node.prev;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            node.prev = null;
        }
        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }

        node.value = null;
        size--;
    }

    private void unlinkFirst(ListNode<T> first) {
        ListNode<T> next = first.next;
        first.value = null;
        first.next = null;
        head = next;
        if (next == null) {
            tail = null;
        } else {
            next.prev = null;
        }
        size--;
    }

    private void unlinkLast(ListNode<T> last) {
        ListNode<T> prev = last.prev;
        last.value = null;
        last.prev = null;
        tail = prev;
        if (prev == null) {
            head = null;
        } else {
            prev.next = null;
        }
        size--;
    }

    public void add(T value) {
        linkLast(value);
    }

    public void add(int index, T value) {
        checkPositionIndex(index);
        if (index == size) {
            linkLast(value);
        } else {
            linkBefore(value, getNode(index));
        }
    }

    public void addFirst(T value) {
        linkFirst(value);
    }

    public void addLast(T value) {
        linkLast(value);
    }

    public T get(int index) {
        checkElementIndex(index);
        return getNode(index).value;
    }

    public T getFirst() {
        ListNode<T> first = head;
        if (first == null) {
            throw new NoSuchElementException();
        }
        return first.value;
    }

    public T getLast() {
        ListNode<T> last = tail;
        if (last == null) {
            throw new NoSuchElementException();
        }
        return last.value;
    }

    public void set(int index, T value) {
        checkElementIndex(index);
        ListNode<T> target = getNode(index);
        target.value = value;
    }

    public void remove(int index) {
        checkElementIndex(index);
        unlink(getNode(index));
    }

    public void remove(Object o) {
        if (o == null) {
            for (ListNode<T> node = head; node != null; node = node.next) {
                if (node.value == null) {
                    unlink(node);
                }
            }
        } else {
            for (ListNode<T> node = head; node != null; node = node.next) {
                if (o.equals(node.value)) {
                    unlink(node);
                }
            }
        }
    }

    public void removeFirst() {
        ListNode<T> first = head;
        if (first == null) {
            throw new NoSuchElementException();
        }
        unlinkFirst(first);
    }

    public void removeLast() {
        ListNode<T> last = tail;
        if (last == null) {
            throw new NoSuchElementException();
        }
        unlinkLast(last);
    }

    public void clear() {
        for (ListNode<T> current = head; current != null; ) {
            ListNode<T> next = current.next;
            current.value = null;
            current.next = null;
            current.prev = null;
            current = next;
        }
        head = tail = null;
        size = 0;
    }

    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private boolean isPositionIndex(int index) {
        return index >= 0 && index <= size;
    }

    private void checkPositionIndex(int index) {
        if (!isPositionIndex(index)) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    private boolean isElementIndex(int index) {
        return index >= 0 && index < size;
    }

    private void checkElementIndex(int index) {
        if (!isElementIndex(index)) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    public int indexOf(Object o) {
        int index = 0;
        if (o == null) {
            for (ListNode<T> target = head; target != null; target = target.next) {
                if (target.value == null) {
                    return index;
                }
                index++;
            }
        } else {
            for (ListNode<T> target = head; target != null; target = target.next) {
                if (o.equals(target.value)) {
                    return index;
                }
                index++;
            }
        }
        return -1;
    }

    public int lastIndexOf(Object o) {
        int index = size;
        if (o == null) {
            for (ListNode<T> target = tail; target != null; target = target.prev) {
                index--;
                if (target.value == null) {
                    return index;
                }
            }
        } else {
            for (ListNode<T> target = tail; target != null; target = target.prev) {
                index--;
                if (o.equals(target.value)) {
                    return index;
                }
            }
        }
        return -1;
    }

    private ListNode<T> getNode(int index) {
        if (index < (size >> 1)) {
            ListNode<T> target = head;
            for (int i = 0; i < index; i++) {
                target = target.next;
            }
            return target;
        } else {
            ListNode<T> target = tail;
            for (int i = size - 1; i > index; i--) {
                target = target.prev;
            }
            return target;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private ListNode<T> curr = head;

            @Override
            public boolean hasNext() {
                return curr != null;
            }

            @Override
            public T next() {
                T res = curr.value;
                curr = curr.next;
                return res;
            }
        };
    }

    public Iterator<T> descendingIterator() {
        return new Iterator<T>() {
            private ListNode<T> curr = tail;

            @Override
            public boolean hasNext() {
                return curr != null;
            }

            @Override
            public T next() {
                T res = curr.value;
                curr = curr.prev;
                return res;
            }
        };
    }

    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (ListNode<T> curr = head; curr != null; curr = curr.next) {
            result[i++] = curr.value;
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public <Y> Y[] toArray(Y[] dummy) {
        if (dummy.length < size) {
            dummy = (Y[]) Array.newInstance(dummy.getClass().getComponentType(), size);
        }
        int i = 0;
        Object[] result = dummy;
        for (ListNode<T> curr = head; curr != null; curr = curr.next) {
            result[i++] = curr.value;
        }
        if (dummy.length > size) {
            dummy[size] = null;
        }
        return dummy;
    }

    public void addAll(int index, Collection<? extends T> list) {
        checkPositionIndex(index);

        Object[] a = list.toArray();
        int numNew = a.length;

        ListNode<T> pred, succ;
        if (index == size) {
            succ = null;
            pred = tail;
        } else {
            succ = getNode(index);
            pred = succ.prev;
        }

        for (Object o : a) {
            @SuppressWarnings("unchecked") T value = (T) o;
            ListNode<T> newNode = new ListNode<>(pred, value, null);
            if (pred == null) {
                head = newNode;
            } else {
                pred.next = newNode;
            }
            pred = newNode;
        }

        if (succ == null) {
            tail = pred;
        } else {
            pred.next = succ;
            succ.prev = pred;
        }

        size += numNew;
    }

    public int size() {
        return size;
    }

}
