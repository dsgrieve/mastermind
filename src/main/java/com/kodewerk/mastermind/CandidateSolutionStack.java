package com.kodewerk.mastermind;

import com.kodewerk.math.Arrangement;
import com.kodewerk.math.ArrangementGroup;
import com.kodewerk.math.Index;

import java.util.Iterator;

/**
 * CandidateSolutionStack holds onto all of the possible solutions. It will passout either
 * a single possible solution or a group of possible solutions.
 *
 * Note: This is not a traditional stack in the sense that it contains actual values. Doing so
 * could create a stack of an enourmous size and take a long time to fill. Instead values are
 * calculated upon demand. To do so the stack uses the helper class ArrangementGroup.
 *
 * @author kirk
 * @since Jun 30, 2005
 * @version 1.0
 * @see com.kodewerk.math.ArrangementGroup
 * @see com.kodewerk.math.Arrangement
 *
 * Copyright 2005 KodeWerk. All rights reservered
 */

public class CandidateSolutionStack {

    private final ArrangementGroup group;
    private Index currentArrangementIndex;
    final private Object lock = new Object();

    public CandidateSolutionStack(ArrangementGroup group) {
        this.currentArrangementIndex = Index.ZERO;
        this.group = group;
    }

    private synchronized Index nextArrangementIndex() {
        Index index;
        index = currentArrangementIndex;
        currentArrangementIndex = group.calculateIndex( currentArrangementIndex, Index.ONE);
        return index;
    }

    /**
     * returns the next single element of the ArrangementGroup
     *
     * @return Arrangement
     */
    public synchronized Arrangement pop() {
        Index index = this.nextArrangementIndex();
        return group.getMember(index);
    }

    private Index getNextNArragementIndcies( Index numberOfElements) {
        Index index;
        synchronized( lock) {
            index = currentArrangementIndex;
        }
        currentArrangementIndex = group.calculateIndex( currentArrangementIndex, numberOfElements);
        return index;
    }

    /**
     * Returns a group of
     * @param length
     * @return Iterator
     */
    public Iterator rangeIterator( int length) {
        return new ArrangementGroupRangeIterator( Index.valueOf(length));
    }

    /**
     * Support for range iterator.
     */
    class ArrangementGroupRangeIterator implements Iterator {

        Index index;
        Index finalIndex;
        ArrangementGroupRangeIterator( Index length) {
            index = getNextNArragementIndcies( length);
            finalIndex = group.calculateIndex( index, length);
        }

        public boolean hasNext() {
            return index.compareTo( finalIndex) != 0;
        }

        public Object next() {
            Arrangement arrangement = group.getMember(index);
            index = group.calculateIndex( index, Index.ONE);
            return arrangement;
        }

        public void remove() {}
    }
}
