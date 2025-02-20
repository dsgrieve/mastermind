package com.kodewerk.mastermind;

import com.kodewerk.math.Arrangement;
import com.kodewerk.math.Index;

/**
 * Text based user interface for MasterMind
 * Accepts up to five parameters
 * Number of Symbols in the Arrangement
 * The Length of the Arrangement (must be less then the number of symbols)
 * number of threads (or Players)
 * Chunk sise to pass out from the CandidateSolutionStack
 * Optional index for solution. If not provided will be choosen randomly.
 *
 * Once spawning off the game, this interface becomes an observer and reports on progress
 * as reported by MasterMind.
 *
 */

public class MasterMindText implements MasterMindGameObserver {
    private final Object lock = new Object();
    private boolean running = true;

    private long waitForSolution() {
        synchronized (this.lock) {
            while ( this.running) {
                try {
                    this.lock.wait();
                } catch (InterruptedException e) {
                }
            }
        }
        return System.currentTimeMillis();
    }

    public void play(int numberOfSymbols, int arrangementLength, Index index, int numberOfPlayers, int unitOfWork) {

        System.out.println("numberOfSymbols = " + numberOfSymbols);
        System.out.println("arrangementLength = " + arrangementLength);
        System.out.println("numberOfPlayers = " + numberOfPlayers);
        System.out.println("Size of Unit Of Work = " + unitOfWork);
        System.out.println("index = " + index);

        MasterMind game = new MasterMind();
        game.addMasterMindGameObserver( this);
        long startTime = game.play( numberOfSymbols, arrangementLength, index, numberOfPlayers, unitOfWork);
        long stopTime = this.waitForSolution();
        game.tearDown();
        System.out.println("Solution was found in " + (stopTime - startTime) + "ms");
    }

    synchronized public void guessMade(Arrangement arrangement, Score score) {
        System.out.println( "Guess -> " + arrangement.toString() + " : " + score.toString());
    }

    public void jackpotted(Arrangement solution) {
        System.out.println("Solution : " + solution.toString());
        synchronized ( this.lock) {
            this.running = false;
            this.lock.notify();
        }
    }

    /**
     * Arguments
     * - number of symbols
     * - number of choices
     * - number of threads
     *
     * @param args
     */
    public static void main(String[] args) {

        MasterMindText textGame = new MasterMindText();
        if ((args.length < 4) || (args.length > 5))
            return;
        int numberOfSymbols = Integer.parseInt(args[0]);
        Index index = null;
        boolean indexSet = args.length == 5;
        if (indexSet) {
            index = new Index(args[4]);
        }
        int arrangementLength = Integer.parseInt(args[1]);
        int numberOfThreads = Integer.parseInt(args[2]);
        int batchSize = Integer.parseInt( args[ 3]);
        textGame.play(numberOfSymbols, arrangementLength, index, numberOfThreads, batchSize);
    }
}