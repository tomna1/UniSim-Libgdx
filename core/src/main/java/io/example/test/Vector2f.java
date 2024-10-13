package io.example.test;

public class Vector2f {
    public float x;
    public float y;

    public Vector2f() {
        x = 0.0f;
        y = 0.0f;
    }
    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public Vector2f(Vector2f vec) {
        this.x = vec.x;
        this.y = vec.y;
    }

    @Override
    public int hashCode() {
        int prime1 = 7;
        int prime2 = 31;
        int result = prime1 * ((int)x * 1000);
        result = (result * prime1) + (prime2 * (int)y);
        return result;
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
