package com.dkelava.backgammon.websrv.exceptions;

/**
 * Created by Dabisa on 20/08/2017.
 */
public class ForbiddenActionException extends RuntimeException {

    public ForbiddenActionException(String message) {
        super(message);
    }
}
