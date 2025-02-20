package com.kodewerk.math;

/**
 *
 * An arrangement is an ordering of N symbols. No symbol is repeated.
 * @author kirk
 * @since Jun 26, 2005
 * @version 1.0
 */

public class Arrangement {

    private final int[] symbols;

    public Arrangement(int[] symbols) {
        this.symbols = symbols;
    }

    public int length() {
        return symbols.length;
    }

    public int symbolAt(int index) {
        return symbols[index];
    }

    public boolean contains(int symbol) {
        for (int index = 0; index < symbols.length; index++) {
            if (this.symbols[index] == symbol)
                return true;
        }
        return false;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Arrangement)) return false;
        Arrangement arrangement = (Arrangement) o;
        for (int i = 0; i < symbols.length; i++) {
            if (symbols[i] != arrangement.symbols[i]) return false;
        }
        return true;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < symbols.length - 1; i++) {
            buffer.append(symbols[i]);
            buffer.append(", ");
        }
        buffer.append(symbols[ symbols.length - 1]);
        return buffer.toString();
    }
}