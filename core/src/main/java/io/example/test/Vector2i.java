package io.example.test;

public class Vector2i {
    public int x;
    public int y;

    public Vector2i() {
        x = 0;
        y = 0;
    }
    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Vector2i(Vector2i vec) {
        this.x = vec.x;
        this.y = vec.y;
    }

    @Override
    public int hashCode() {
        int prime1 = 7;
        int prime2 = 31;
        int result = prime1 * (x * 1000);
        result = (result * prime1) + (prime2 * y);
        return result;
    }

    @Override
    public boolean equals(Object rhs) {
        if (this == rhs) return true;

        if (!(rhs instanceof Vector2i)) return false;
        
        Vector2i r = (Vector2i) rhs;
        if (this.x == r.x && this.y == r.y) return true;
        return false;
    }

    @Override
    public String toString() {
        String output = "(" + x + "," + y + ")";
        return output;
    }
}
