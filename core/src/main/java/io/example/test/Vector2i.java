package io.example.test;

public class Vector2i {
    public int x;
    public int y;

    Vector2i() {
        x = 0;
        y = 0;
    }
    Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object rhs) {
        if (this == rhs) return true;

        if (!(rhs instanceof Vector2i)) return false;
        
        Vector2i r = (Vector2i) rhs;
        if (this.x == r.x && this.y == r.y) return true;
        return false;
    }
}
