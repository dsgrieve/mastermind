package com.kodewerk.mastermind;

import com.kodewerk.math.Arrangement;

/**
 *
 * Interface to be implemented by any process interesting in watching a game. Primaly used as a call-back
 * mechanisum so that observer is notified of significant gaming events.
 *
 * @author kirk
 * @since Auguest 11, 2005
 * @version 1.0
 *
 * Copyright 2005 KodeWerk, All rights reserved.
 */
public interface MasterMindGameObserver {

    void guessMade( Arrangement arrangement, Score score);
    void jackpotted( Arrangement arrangement);

}
