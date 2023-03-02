package ru.clevertec.json.exception;

public class ParsJsonException extends RuntimeException {
    public ParsJsonException(String message) {
        super(message);
    }
    public ParsJsonException(Exception e) {
        super(e);
    }
}
