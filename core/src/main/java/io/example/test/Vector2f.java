package io.example.test;

public class Vector2f {
    public float x;
    public float y;

    Vector2f() {
        x = 0.0f;
        y = 0.0f;
    }
    Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object rhs) {
        if (this == rhs) return true;

        if (!(rhs instanceof Vector2f)) return false;
        
        Vector2f r = (Vector2f) rhs;
        if (this.x == r.x && this.y == r.y) return true;
        return false;
    }
}
