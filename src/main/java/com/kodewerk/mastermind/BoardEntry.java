package com.kodewerk.mastermind;

import com.kodewerk.math.Arrangement;

/**
 *
 * BoardEntry is primaraly a data holder. It represents a single guess which
 * is placed on the game board. The guess is 'scored'.
 *
 * @author kirk
 * @since Jun 30, 2005
 * @version 1.0
 *
 * Copyright 2005 KodeWerk, All rights reserved.
 * 
 */

public class BoardEntry {
    private final Arrangement arrangement;
    private final Score score;

    BoardEntry(Arrangement arrangement, Score score) {
        this.arrangement = arrangement;
        this.score = score;
    }

    public Score getScore() { return this.score; }
    public Arrangement getArrangement() { return this.arrangement; }
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(arrangement.toString());
        buffer.append(" : ");
        buffer.append(score.toString());
        return buffer.toString();
    }
}
