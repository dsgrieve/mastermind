package com.kodewerk.math;

import java.util.Random;

public class ArrangementGroup {

    private int[] symbols;
    private int arrangementLength;
    private Index length;
    private int numberOfSymbols;

    public ArrangementGroup(int arrangementLength, int numberOfSymbols) {
        symbols = new int[numberOfSymbols];
        for (int i = 0; i < numberOfSymbols; i++) {
            symbols[i] = i;
        }
        this.arrangementLength = arrangementLength;
        this.numberOfSymbols = numberOfSymbols;
    }

    public Index getLength() {
        if (length == null)
            length = com.kodewerk.math.Math.permutationAsIndex(numberOfSymbols, arrangementLength);
        return length;
    }

    /**
     * Since the cyclic group is ordered, ever arrangement in the group has an index. This method calculates the
     * arrangement for a given index.
     *
     * @param i
     * @return Arrangement at index
     */
    public Arrangement getMember(Index i) {
        int[] elements = new int[arrangementLength];
        this.calculateElementIndex(elements, 0, i.remainderInPlace(getLength()),
                this.arrangementLength, this.symbols, numberOfSymbols);
        return new Arrangement(elements);
    }

    private void calculateElementIndex(int[] results, int position,
                                       Index index, int elementLength,
                                       int[] symbols, int remainingSymbols) {

        // end of recursion.
        // formulation is a bit ugly because Index constructor doesn't accept int as parameter
        if (elementLength == 1) {
            int symbolIndex = index.remainder(new Index(Integer.toString(remainingSymbols))).intValue();
            if (symbolIndex >= symbols.length) {
                throw new ArrayIndexOutOfBoundsException("Index " + symbolIndex + " out of bounds for length " + symbols.length);
            }
            results[position] = symbols[symbolIndex];
            return;
        }

        Index foldValue = Math.permutationAsIndex(remainingSymbols - 1,
                elementLength - 1);
        int symbolPosition = index.divide(foldValue).intValue();
        if (symbolPosition >= symbols.length) {
            throw new ArrayIndexOutOfBoundsException("Index " + symbolPosition + " out of bounds for length " + symbols.length);
        }
        results[position] = symbols[symbolPosition];

        // we've calculated the nth position, now we can reduce the problem by one and repeat for nth-1.
        Index newIndex = (index.compareTo(foldValue) >= 0) ? index.remainder(
                foldValue) : index;
        int[] newSymbols = removeSymbol(symbols, symbolPosition);
        this.calculateElementIndex(results, position + 1, newIndex,
                elementLength - 1, newSymbols, remainingSymbols - 1);
    }

    // need to remove the used symbol from the list of allowed.
    private int[] removeSymbol(int[] symbols, int symbolPosition) {
        int[] newSymbols = new int[symbols.length - 1];
        int position = 0;
        for (; position < symbolPosition; position++) {
            newSymbols[position] = symbols[position];
        }
        for (int i = symbolPosition + 1; i < symbols.length; i++) {
            newSymbols[position++] = symbols[i];
        }

        return newSymbols;
    }

    /**
     * Controls indexing in the cyclic group.
     *
     * @param current
     * @param increment
     * @return Index index
     */
    public Index calculateIndex(Index current, Index increment) {
        return current.addInPlace(increment).remainderInPlace(this.getLength());
    }

    public Arrangement getRandomMember() {
        Random generator = new Random();
        int numberOfBytes = this.getLength().toByteArray().length;
        byte[] random = new byte[numberOfBytes + (generator.nextInt() % numberOfBytes)];
        generator.nextBytes(random);
        Index index = new Index(random);
        index = index.absInPlace().remainderInPlace(this.getLength());
        return this.getMember(index);
    }
}
