package com.kodewerk.mastermind;

import com.kodewerk.math.ArrangementGroup;
import com.kodewerk.math.Arrangement;
import com.kodewerk.math.Index;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * MasterMind is the entry point into the game. It starts and manages all of the elements of a game
 * including the game observers.
 * The public methods of interest are init() and play(). There are two
 * versions of init(). The first will randomly select an Arrangement for the final answer. The second
 * will accept an index into the ArrangementGroup as the final answer. This second init is intended to
 * be used when one needs to control the run time of the game.
 *
 * After init() called, the game is ready to be play()ed.
 *
 * @author kirk
 * @since Jun 30, 2005
 * @version 1.0
 *
 * Copyright 2005 KodeWerk, All rights reserved.
 */


public class MasterMind {

    private Board board;
    private final ArrayList players = new ArrayList();
    private final ArrayList threads = new ArrayList();
    private Arrangement answer = null;
    private boolean abandoned = false;

    public MasterMind() {}

    // KCP this logic replaces the boolean setRunning and isRunning that controlled the end of the game condition
    public boolean isInPlay() {
        return (this.answer == null) && ( ! abandoned);
    }

    // logic replaces the boolean setRunning and isRunning that controlled the end of the game condition
    private void setAnswer( Arrangement answer) {
        this.answer = answer;
    }

    public void abandon() {
        this.abandoned = true;
    }

    // KCP this logic replaces the boolean setRunning and isRunning that controlled the end of the game condition
    public Arrangement getAnswer() {
        return this.answer;
    }

    /**
     * Setup the game with this ArrangementGroup
     *
     * @param group
     */
    private void init(ArrangementGroup group) {
        this.board = new Board(group.getRandomMember());
    }

    /**
     * Setup the game with this ArrangementGroup and use the Arrangement found at index as the answer
     * @param group
     * @param index
     */
    private void init(ArrangementGroup group, Index index) {
        board = new Board(group.getMember(index));
    }

    /**
     * Start the game using numberOfPlayers (threads), the specified ArrangementGroup, and have the
     * players work with batchSize members of the ArrangementGroup in a single call.
     * @param numberOfPlayers
     * @param group
     * @param batchSize
     * @return long milliseconds runtime.
     */
    private long start(int numberOfPlayers, ArrangementGroup group, int batchSize) {
        CandidateSolutionStack stack = new CandidateSolutionStack(group);

        for (int i = 0; i < numberOfPlayers; i++) {
            Player player = new Player( this, board, stack, batchSize);
            this.players.add(player);
        }

        Iterator iter = this.players.iterator();
        while (iter.hasNext()) {
            Player player = (Player) iter.next();
            Thread thread = new Thread( player ,"Player - " + player.toString());
            this.threads.add( thread);
        }

        iter = this.threads.iterator();
        long time = System.currentTimeMillis();
        while ( iter.hasNext())
            ((Thread)iter.next()).start();
        return time;
    }

    /**
     * Start the game using numberOfPlayers (threads), the specified (by length and number of symbols) ArrangementGroup, and have the
     * players work with batchSize members of the ArrangementGroup in a single call.
     * @param numberOfSymbols
     * @param arrangementLength
     * @param index
     * @param numberOfPlayers
     * @param batchSize
     * @return long milliseconds runtime.
     */
    public long play(int numberOfSymbols, int arrangementLength, Index index, int numberOfPlayers, int batchSize) {
        ArrangementGroup group = new ArrangementGroup(arrangementLength, numberOfSymbols);
        if (index == null)
            this.init(group);
        else
            this.init(group, index);

        return this.start(numberOfPlayers, group, batchSize);
    }

    public void tearDown() {
        Iterator iter = this.threads.iterator();
        while (iter.hasNext())
            try {
                ((Thread) iter.next()).join();
            } catch (InterruptedException e) { // eat the exception
            }
    }

    /**
     * Players call this method to submit a guess for final scoring.
     * 
     * @param candidate
     */
    public void submitCandidateSolution( Arrangement candidate) {
        Score score = board.testForJackpot( candidate);
        this.guessMade( candidate, score);

        if ( score.getJackpot()) {
            this.setAnswer( candidate);
            this.jackpotted( candidate);
        }
    }

    // MasterMindGameObserver methods

    private MasterMindGameObserver observer;

    public void addMasterMindGameObserver( MasterMindGameObserver observer) {
        this.observer = observer;
    }

    private void guessMade( Arrangement arrangement, Score score) {
        if ( this.observer != null)
            this.observer.guessMade( arrangement, score);
    }

    private void jackpotted( Arrangement arrangement) {
        if ( this.observer != null)
            this.observer.jackpotted( arrangement);

    }
}
