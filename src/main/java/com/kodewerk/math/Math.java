package com.kodewerk.math;

import java.math.BigInteger;

/**
 *
 * @author kirk
 * @since Jun 30, 2005
 * @version 1.0
 */
public final class Math {

    /**
     * P(n,m) = n! / (n-m)! = n * (n-1) *... (n-m)
     *
     * @param n
     * @param m
     * @return Index
     */
    public static Index permutationAsIndex(int n, int m) {
        Index value = Index.ONE;

        for (int i = n; i > n - m; i--) {
            value = value.multiply(Index.valueOf(i));
        }
        return value;
    }

    public static BigInteger permutationAsBigInteger(int n, int m) {
        BigInteger value = BigInteger.ONE;

        for (int i = n; i > n - m; i--) {
            value = value.multiply(BigInteger.valueOf((long)i));
        }
        return value;
    }

    public static long permutationAsLong(int n, int m) {
        long value = 1L;

        for (int i = n; i > n - m; i--) {
            value *= i;
        }
        return value;
    }
}
