package code;

public class Cell {
    private int size;
    private int value;

    public Cell(int size, int value) {
        this.size = size;
        this.value = value;
    }
    public Cell(){};

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
