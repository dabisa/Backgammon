package com.dkelava.backgammon.websrv.exceptions;

public class ServerError extends RuntimeException {

    public ServerError(String message) {
        super(message);
    }
}
