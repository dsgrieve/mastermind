package com.kodewerk.math;

import java.math.BigInteger;

public class Index {

    public static final Index ZERO = new Index(0);
    public static final Index ONE = new Index(1);
    private BigInteger value;

    public static Index valueOf(int intValue) {
        return new Index(intValue);
    }

    public static Index valueOf(long longValue) {
        return new Index(longValue);
    }

    public Index(BigInteger newValue) {
        value = newValue;
    }

    public Index(String newValue) {
        value = new BigInteger(newValue);
    }

    public Index(byte[] bytes) {
        value = new BigInteger(bytes);
    }

    private Index(long newValue) {
        value = BigInteger.valueOf(newValue);
    }

    public Index add(Index index) {
        return new Index(value.add(index.value));
    }

    public Index remainder(Index foldValue) {
        return new Index(value.remainder(foldValue.value));
    }

    public Index divide(Index divisor) {
        return new Index(value.divide(divisor.value));
    }

    public int intValue() {
        return value.intValue();
    }

    public int compareTo(Index foldValue) {
        return value.compareTo(foldValue.value);
    }

    public byte[] toByteArray() {
        return value.toByteArray();
    }

    public Index abs() {
        return new Index(value.abs());
    }

    public Index multiply(Index index) {
        return new Index(value.multiply(index.value));
    }

    // Reuse BigInteger instances to reduce allocations
    public Index addInPlace(Index index) {
        this.value = this.value.add(index.value);
        return this;
    }

    public Index remainderInPlace(Index foldValue) {
        this.value = this.value.remainder(foldValue.value);
        return this;
    }

    public Index divideInPlace(Index divisor) {
        this.value = this.value.divide(divisor.value);
        return this;
    }

    public Index absInPlace() {
        this.value = this.value.abs();
        return this;
    }

    public Index multiplyInPlace(Index index) {
        this.value = this.value.multiply(index.value);
        return this;
    }
}
