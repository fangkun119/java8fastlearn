import java.util.Arrays;
import java.util.concurrent.locks.StampedLock;

class Vector {
    private int size;
    private Object[] elements = new Object[10];
    private StampedLock lock  = new StampedLock();

    public Object get(int n) {
        // 先尝试乐观读
        long stamp = lock.tryOptimisticRead();
        Object[] currentElements = elements;
        int      currentSize     = size;
        // 如果乐观读失败（此时有个线程获得了写锁并更新了数据），则只能拿一把读锁并重新读取数据
        if (!lock.validate(stamp)) { // Someone else had a write lock
            // 获取读锁
            stamp = lock.readLock(); // Get a pessimistic lock
            // 读数据
            currentElements = elements;
            currentSize = size;
            // 解锁
            lock.unlockRead(stamp);
        }
        return n < currentSize ? currentElements[n] : null;
    }


    public int size() {
        // 先尝试乐观读
        long stamp = lock.tryOptimisticRead();
        int currentSize = size;
        // 如果乐观读失败（此时有个线程获得了写锁并更新了数据），则只能拿一把读锁并重新读取数据
        if (!lock.validate(stamp)) { // Someone else had a write lock
            // 获取读锁
            stamp = lock.readLock(); // Get a pessimistic lock
            // 读数据
            currentSize = size;
            // 解锁
            lock.unlockRead(stamp);
        }
        return currentSize;
    }


    public void add(Object obj) {
        // 获取写锁并更新数据
        long stamp = lock.writeLock();
        try {
            if (size == elements.length)
                elements = Arrays.copyOf(elements, 2 * size);
            elements[size] = obj;
            size++;
        } finally {
            // 解锁
            lock.unlockWrite(stamp);
        }
    }
}
