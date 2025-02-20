package com.kodewerk.mastermind;

/**
 * A score has two element, symbols that are correct and symbols that are correct and in the collect position
 * in relationship to the true answer. A symbol can only fall into one of these two categories. The score is
 * said to represent a jackpot situation when the number of elements in the correct position equals then length
 * of the Arrangement.
 *
 * @author kirk
 * @since Jun 30, 2005
 * @version 1.0
 *
 * Copyright 2005 KodeWerk, All rights reserved.
 */

public class Score {
    private boolean jackpot = false;
    private int positions = 0;
    private int presence = 0;

    public void addToPosition() { positions++; }
    public void addToPresence() { presence++; }

    public int getPositions() { return positions; }
    private int getPresence() { return presence; }

    public void jackpot() {
        this.jackpot = true;
    }

    /**
     * does this score represent a jackpot
     * @return boolean true for score that has jackpotted.
     */
    public boolean getJackpot() {
        return this.jackpot;
    }

    public boolean equals(Object obj) {
        Score score = (Score) obj;
        return (score.getPositions() == positions) && (score.getPresence() == presence);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(positions);
        buffer.append(", ");
        buffer.append(presence);
        return buffer.toString();
    }

    public void reset() {
        jackpot = false;
        positions = 0;
        presence = 0;
    }
}