package com.saidashevar.ptgame.exception;

@SuppressWarnings("serial")
public class InvalidGameException extends Exception {
    private String message;

    public InvalidGameException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
