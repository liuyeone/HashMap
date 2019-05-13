import java.util.ArrayList;
import java.util.List;

public class MyHashMap<K, V> implements MyMap<K, V> {

    // 数组的初始化长度
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;

    // 阀值比例
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private int defaultInitSize;

    private float defaultLoadFactor;

    // MAP当中entry的数量
    private int entryUseSize;

    // 数组
    private Entry<K, V>[] table = null;

    // 门面模式
    public MyHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int defaultInitialCapacity, float defaultLoadFactor) {
        if (defaultInitialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + defaultInitialCapacity);
        }
        if (defaultLoadFactor <= 0 || Float.isNaN(defaultLoadFactor)) {
            throw new IllegalArgumentException("Illegal load factor: " + defaultLoadFactor);
        }
        this.defaultInitSize = defaultInitialCapacity;
        this.defaultLoadFactor = defaultLoadFactor;

        table = new Entry[this.defaultInitSize];
    }

    private void resize(int i) {
        Entry[] newTable = new Entry[i];
        // 改变了数组的大小
        defaultInitSize = i;
        entryUseSize = 0;
        rehash(newTable);
    }

    private void rehash(Entry<K, V>[] newTable) {
        // 得到原来老的entry集合，注意遍历单链表
        List<Entry<K, V>> entryList = new ArrayList<>();
        for (Entry<K, V> entry : table) {
            if (null != entry) {
                do {
                    entryList.add(entry);
                    entry = entry.getNext();
                } while (null != entry);
            }
        }

        // 覆盖旧的引用
        if (newTable.length > 0) {
            table = newTable;
        }

        // 所谓重新HASH，就是重新PUT ENTRY到HASHMAP
        for (Entry<K, V> entry : entryList) {
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 要想散列均匀，就得进行二进制的位运算
     */
    private int hash(K k) {
        int hashCode = k.hashCode();
        hashCode ^= (hashCode >>> 20) ^ (hashCode >>> 12);
        return hashCode ^ (hashCode >>> 7) ^ (hashCode >>> 4);
    }

    @Override
    public void put(K k, V v) {
        V oldValue = null;
        // 是否需要扩容?
        // 扩容完毕 肯定需要重新散列
        if (entryUseSize >= defaultInitSize * defaultLoadFactor) {
            resize(2 * defaultInitSize);
        }
        // 得到HASH值，计算出数组中的位置
        int index = hash(k) & (defaultInitSize - 1);
        if (null == table[index]) {
            table[index] = new Entry<K, V>(k, v, null);
            ++entryUseSize;
        } else {
            // 需要遍历单链表
            Entry<K, V> entry = table[index];
            Entry<K, V> e = entry;
            while (e != null) {
                if (k == e.getKey() || k.equals(e.getKey())) {
                    e.setValue(v);
                    return;
                }
                e = e.getNext();
            }
            table[index] = new Entry<K, V>(k, v, entry);
            ++entryUseSize;
        }
    }

    @Override
    public V get(K k) {

        int index = hash(k) & (defaultInitSize - 1);

        if (null == table[index]) {
            return null;
        } else {
            Entry<K, V> entry = table[index];

            do {
                if (k == entry.getKey() || k.equals(entry.getKey())) {
                    return entry.getValue();
                }
                entry = entry.getNext();
            } while (null != entry);
        }
        return null;
    }
}
