package dev.tk2575.fantasysports.details.sleeper;

import java.io.IOException;

enum Sport {
    NFL;

    String toValue() {
        switch (this) {
            case NFL: return "nfl";
        }
        return null;
    }

    static Sport forValue(String value) throws IOException {
        if (value.equals("nfl")) return NFL;
        throw new IOException("Cannot deserialize Sport");
    }
}