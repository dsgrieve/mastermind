package com.kodewerk.mastermind;

import com.kodewerk.math.Arrangement;
import com.kodewerk.math.Index;

import java.util.Iterator;
import java.util.logging.Logger;

/**
 * The Player is the driving element of the game. There can be more then 1 players making guesses if need be.
 *
 * @author kirk
 * @since Jun 30, 2005
 * @version 1.0
 *
 * Copyright 2005 KodeWerk, All rights reserved.
 */

public class Player implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(Player.class.getName());
    private final MasterMind game;
    private final Board board;
    private final CandidateSolutionStack stack;
    private int batchSize;

    public Player(MasterMind game, Board board, CandidateSolutionStack stack, int batchSize) {
        this.game = game;
        this.board = board;
        this.stack = stack;
        this.batchSize = batchSize;
    }

    /**
     * This is the primary execution loop in the application. It takes a candiate solution off of the stack and
     * tests it against the board. If the test passes, then the guess is submitted to the game as a candidate
     * solution. This tread runs until the game is marked as finished.
     */
    public void run() {

        if ( batchSize == 1) {
            while ( this.game.isInPlay()) {
                testCandidate(this.stack.pop());
            }
        } else {
            while (this.game.isInPlay()) {
                Iterator iter = this.stack.rangeIterator(batchSize);
                while (iter.hasNext() && this.game.isInPlay()) {
                    Arrangement candidate = (Arrangement) iter.next();
                    testCandidate(candidate);
                }
            }
        }
    }

    private void testCandidate(Arrangement candidate) {
        if (board.isPossibleSolution(candidate)) {
            //LOGGER.fine("testing:submitting" + candidate.toString());
            game.submitCandidateSolution(candidate);
        }
    }
}
