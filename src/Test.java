public class Test {
    public static void main(String[] args) {
        MyMap map = new MyHashMap();

        // put
        for (int i = 0; i < 100; i++) {
            map.put(i, "value" + i);
        }

        // get
        for (int i = 0; i < 100; i++) {
            System.out.println(map.get(i));
        }
    }
}
