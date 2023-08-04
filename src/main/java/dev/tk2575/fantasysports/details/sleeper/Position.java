package dev.tk2575.fantasysports.details.sleeper;

import java.io.IOException;

enum Position {
    K, P;

    String toValue() {
        switch (this) {
            case K: return "K";
            case P: return "P";
        }
        return null;
    }

    static Position forValue(String value) throws IOException {
        if (value.equals("K")) return K;
        if (value.equals("P")) return P;
        throw new IOException("Cannot deserialize Position");
    }
}