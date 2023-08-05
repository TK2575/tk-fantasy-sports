package dev.tk2575.fantasysports.details.sleeper;

import java.io.IOException;

enum Position {
    K, P, QB, RB, WR, TE, DEF;

    String toValue() {
        return switch (this) {
            case K -> "K";
            case P -> "P";
            case QB -> "QB";
            case RB -> "RB";
            case WR -> "WR";
            case TE -> "TE";
            case DEF -> "DEF";
        };
    }

    static Position forValue(String value) throws IOException {
        if (value.equals("K")) return K;
        if (value.equals("P")) return P;
        if (value.equals("QB")) return QB;
        if (value.equals("RB")) return RB;
        if (value.equals("WR")) return WR;
        if (value.equals("TE")) return TE;
        if (value.equals("DEF")) return DEF;
        if (value.equals("DST")) return DEF;
        throw new IOException("Cannot deserialize Position");
    }
}