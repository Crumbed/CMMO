import java.util.Arrays;
import java.util.HashMap;

public class TownMapArray {
    // Declare an array of your class type
    private TownMap[] array;
    private int size = 1;

    // Write a constructor
    public TownMapArray () {
        array = new TownMap[size];
    }
    public TownMapArray (int size) {
        array = new TownMap[size];
    }

    // Write an add() method that adds an object to your array at a certain index
    public void add(int i, TownMap map) {
        array[i] = map;
    }

    // Write a print() method using an enhanced for loop
    public void print() {
        for (TownMap map : array) {
            map.print("array");
        }
    }

    // Write a findAndPrint(attribute) method using an enhanced for loop
    public void findAndPrint(char[][] map) {
        for (TownMap tm : array) {
            if (tm.getMap() == map) tm.print("array");
        }
    }

    public static void main(String[] args)
    {

        char[][] map = new char[][] {
                new char[] {' ','#','@',' ',' '},
                new char[] {' ','#','@','@',' '},
                new char[] {'#','#','#','#','#'},
                new char[] {'@','#',' ',' ','#'},
                new char[] {' ','#','@','@','#'}
        };
        // Declare an object of ClassNameArray with your class name
        TownMapArray tma = new TownMapArray(3);
        // Call its add method to add enough new objects to fill the array
        tma.add(0, new TownMap());
        tma.add(1, new TownMap());
        tma.add(2, new TownMap());
        // Call its print method
        tma.print();
        // Call its findAndPrint method
        tma.findAndPrint(map);
    }
}

class TownMap {
    // 1. Copy your class from the last lesson.
    private char[][] map;
    private HashMap<Character, String> key;
    private int x;
    private int y;


    public TownMap() {
        x = 5;
        y = 5;
        key = new HashMap<>();
        key.put(' ', "Empty");
        key.put('#', "Road");
        key.put('@', "Building");
        map = new char[][] {
                new char[] {' ','#','@',' ',' '},
                new char[] {' ','#','@','@',' '},
                new char[] {'#','#','#','#','#'},
                new char[] {'@','#',' ',' ','#'},
                new char[] {' ','#','@','@','#'}
        };

    }
    private TownMap(int width, int height, HashMap<Character, String> key, char[][] map) {
        this.x = width;
        this.y = height;
        this.key = key;
        this.map = map;
    }


    public char getCharAt(int x, int y) {
        return map[y][x];
    }
    public String getLocationAt(int x, int y) {
        return key.get(map[y][x]);
    }



    public char[][] getMap() {
        return map;
    }
    public HashMap<Character, String> getKey() {
        return key;
    }
    public int getWidth() {
        return x;
    }
    public int getHeight() {
        return y;
    }

    public void setMap(char[][] map) {
        this.map = map;
    }
    public void setKey(HashMap<Character, String> key) {
        this.key = key;
    }
    public void setWidth(int width) {
        x = width;
    }
    public void setHeight(int height) {
        y = height;
    }

    public void print(String style) {
        String strMap = "";
        if (style.equalsIgnoreCase("square")) {
            for(char[] sharArr : map) {
                for(char shar : sharArr) {
                    strMap += shar+" ";
                }
                strMap += "\n";
            }
        } else if (style.equalsIgnoreCase("true")) {
            for(char[] sharArr : map) {
                for(char shar : sharArr) {
                    strMap += shar;
                }
                strMap += "\n";
            }
        } else if (style.equalsIgnoreCase("array")) {
            for (char[] sharArr : map) {
                strMap += Arrays.toString(sharArr)+"\n";
            }
        }

        System.out.println(strMap);

    }

    public String toString() {
        String strMap = "";
        for(char shar : key.keySet()) {
            strMap += shar + ": "+key.get(shar)+", ";
        }
        strMap += "\nWidth: "+x+", Height: "+y+"\n";

        for(char[] sharArr : map) {
            for(char shar : sharArr) {
                strMap += shar+" ";
            }
            strMap += "\n";
        }

        return strMap;
    }

    // 2.  Add a method for your class that takes a parameter.
    // For example, there could be a print method with arguments that indicate
    // how you want to print out the information, print(format) where format is "plain" or "table".

    // 3. Test the method in the main method.
    public static void main(String[] args) {
        // Construct an object of your class
        TownMap[] maps = {new TownMap(), new TownMap(), new TownMap()};

        for (TownMap map : maps) {
            map.print("array");
        }
        // call the object's method

    }
}
