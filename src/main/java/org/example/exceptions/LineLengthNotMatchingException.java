package org.example.exceptions;

public class LineLengthNotMatchingException extends RuntimeException{
    public LineLengthNotMatchingException(String message) {
        super(message);
    }
}
