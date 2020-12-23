package libraries;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class CycleList<T> implements Iterable<T> {

    private static class ListNode<T> {
        T value;
        ListNode<T> next;
        ListNode<T> prev;

        ListNode(ListNode<T> next, ListNode<T> prev, T value) {
            this.next = next;
            this.prev = prev;
            this.value = value;
        }
    }

    private ListNode<T> head = null;
    private int size = 0;

    private ListNode<T> getNode(int index) {
        checkElementIndex(index);
        int half = size / 2;
        int count = 0;
        if (index < half) {
            count = index;
            ListNode<T> node = head;
            for (int i = 0; i < index; i++) {
                node = node.next;
            }
            return node;
        } else {
            count = size - index;
            ListNode<T> node = head;
            for (int i = size; i > index; i--) {
                node = node.prev;
            }
            return node;
        }
    }

    public int indexOf(Object o) {
        int index = 0;
        if (o == null) {
            ListNode<T> target = head;
            for (int i = 0; i < size; i++) {
                if (target.value == null) {
                    return index;
                }
                target = target.next;
                index++;
            }
        } else {
            ListNode<T> target = head;
            for (int i = 0; i < size; i++) {
                if (o.equals(target.value)) {
                    return index;
                }
                target = target.next;
                index++;
            }
        }
        return -1;
    }

    public int lastIndexOf(Object o) {
        int index = size - 1;
        if (o == null) {
            ListNode<T> target = head.prev;
            for (int i = size - 1; i >= 0; i--) {
                if (target.value == null) {
                    return index;
                }
                target = target.prev;
                index--;
            }
        } else {
            ListNode<T> target = head.prev;
            for (int i = size - 1; i >= 0; i--) {
                if (o.equals(target.value)) {
                    return index;
                }
                target = target.prev;
                index--;
            }
        }
        return -1;
    }

    public void clear() {
        ListNode<T> current = head;
        for (int i = 0; i < size; i++) {
            ListNode<T> next = current.next;
            current.value = null;
            current.next = null;
            current.prev = null;
            current = next;
        }
        head = null;
        size = 0;
    }

    public void add(T value) {
        addLast(value);
    }

    public void add(int index, T value) {
        if (index == 0) {
            addFirst(value);
        } else if (index == size) {
            addLast(value);
        } else {
            ListNode<T> node = getNode(index);
            if (node != null) {
                ListNode<T> newNode = new ListNode<>(node, node.prev, value);
                node.prev = node.prev.next = newNode;
                size++;
            }
        }
    }

    public void addFirst(T value) {
        if (size == 0) {
            head = new ListNode<>(null, null, value);
            head.next = head;
            head.prev = head;
        } else {
            ListNode<T> first = new ListNode<T>(head, head.prev, value);
            head.prev = head.prev.next = first;
            head = first;
        }
        size++;
    }

    public void addLast(T value) {
        if (size == 0) {
            head = new ListNode<>(null, null, value);
            head.next = head;
            head.prev = head;
        } else {
            ListNode<T> last = new ListNode<T>(head, head.prev, value);
            head.prev = head.prev.next = last;
        }
        size++;
    }

    public void addAll(Collection<? extends T> list) {
        if (list != null && !list.isEmpty()) {
            for (T value : list) {
                add(value);
            }
        }
    }

    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    public T get(int index) {
        return getNode(index).value;
    }

    public T getFirst() {
        ListNode<T> first = head;
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return first.value;
    }

    public T getLast() {
        ListNode<T> first = head;
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return first.prev.value;
    }

    public void set(int index, T value) {
        checkElementIndex(index);
        ListNode<T> target = getNode(index);
        target.value = value;
    }

    public void remove(int index) {
        checkElementIndex(index);
        ListNode<T> target = getNode(index);
        ListNode<T> prev = target.prev;
        ListNode<T> next = target.next;

        if (index == 0) {
            head = next;
        }
        prev.next = next;
        next.prev = prev;
        target.value = null;
        target.prev = null;
        target.next = null;
        size--;
    }

    public void remove(Object o) {
        ListNode<T> node = head;
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (node.value == null) {
                    remove(i);
                    break;
                }
                node = node.next;
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(node.value)) {
                    remove(i);
                    break;
                }
                node = node.next;
            }
        }
    }

    public void removeFirst() {
        ListNode<T> first = head;
        head = first.next;
        first.prev.next = first.next;
        first.next.prev = first.prev;
        first.value = null;
        first.prev = null;
        first.next = null;
        size--;
    }

    public void removeLast() {
        ListNode<T> last = head.prev;
        last.prev.next = last.next;
        last.next.prev = last.prev;
        last.value = null;
        last.prev = null;
        last.next = null;
        size--;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private ListNode<T> curr = head;
            private int count = size;

            @Override
            public boolean hasNext() {
                return count != 0;
            }

            @Override
            public T next() {
                T res = curr.value;
                curr = curr.next;
                count--;
                return res;
            }
        };
    }

    private class DescendingIteratorList implements Iterator<T> {
        private ListNode<T> curr = head.prev;
        private int count = size;

        @Override
        public boolean hasNext() {
            return count != 0;
        }

        @Override
        public T next() {
            T res = curr.value;
            curr = curr.prev;
            count--;
            return res;
        }
    }

    public Iterator<T> descendingIterator() {
        return new DescendingIteratorList();
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

    public boolean isEmpty() {
        return size == 0;
    }

    public Object[] toArray() {
        Object[] result = new Object[size];
        ListNode<T> curr = head;
        for (int i = 0; i < size; i++) {
            result[i] = curr.value;
            curr = curr.next;
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public <Y> Y[] toArray(Y[] dummy) {
        if (dummy.length < size) {
            dummy = (Y[]) Array.newInstance(dummy.getClass().getComponentType(), size);
        }
        Object[] result = dummy;
        ListNode<T> curr = head;
        for (int i = 0; i < size; i++) {
            result[i] = curr.value;
            curr = curr.next;
        }
        if (dummy.length > size) {
            dummy[size] = null;
        }
        return dummy;
    }

    public int size() {
        return size;
    }

}
