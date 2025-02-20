package com.kodewerk.mastermind;

import com.kodewerk.math.Arrangement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * Board is the focal point for the game. It holds onto previous guesses. It uses these guesses to provide testing
 * and scoring facilities.
 *
 * @author kirk
 * @since Jun 30, 2005
 * @version 1.0
 *
 * Copyright 2005 KodeWerk, All rights reserved.
 *
 */

public class Board {

    private static final Logger LOGGER = Logger.getLogger(Board.class.getName());

    private final Arrangement code;
    private final CopyOnWriteArrayList boardEntries;

    /* todo cleanup after testing */
    public Board(Arrangement code) {
        this.code = code;
        this.boardEntries = new CopyOnWriteArrayList();
    }

    /**
     * Tests an Arrangement to see is it could be a possible solution. A possible solution
     * should not score differently then any of the previous guesses.
     * @param guess
     * @return boolean true if this is a possible solution
     */
    public boolean isPossibleSolution(Arrangement guess) {

        Iterator attempts = boardEntries.iterator();
        while (attempts.hasNext()) {
            Score score = new Score();
            BoardEntry entry = (BoardEntry) attempts.next();
            for (int i = 0; i < guess.length(); i++) {
                if (guess.symbolAt(i) == entry.getArrangement().symbolAt(i))
                    score.addToPosition();
                else if (entry.getArrangement().contains(guess.symbolAt(i)))
                    score.addToPresence();
            }
            //LOGGER.fine("testing:" + guess.toString() + ", scored " + score.toString());
            if (!score.equals(entry.getScore()))
                return false;
        }
        return true;
    }

    /**
     * testForJackpot is taking a score as an offical guess. An offical guess is scored against the
     * answer and is entered into the board to be used in future testing assuming that this test fails.
     * @param guess
     * @return Score
     */
    public Score testForJackpot(Arrangement guess) {

        Score score = new Score();
        for (int i = 0; i < guess.length(); i++) {
            if (guess.symbolAt(i) == code.symbolAt(i))
                score.addToPosition();
            else if (code.contains(guess.symbolAt(i)))
                score.addToPresence();
        }

        if (score.getPositions() == code.length())
            score.jackpot();
        //LOGGER.fine("testing: added ->" + guess.toString() + ", scored " + score.toString());
        this.addGuess( new BoardEntry(guess, score));
        return score;
    }

    synchronized public void addGuess( BoardEntry entry) {
        this.boardEntries.add( entry);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        Iterator iter = boardEntries.iterator();
        while (iter.hasNext()) {
            buffer.append(iter.next().toString());
            buffer.append("\n");
        }
        return buffer.toString();
    }
}
