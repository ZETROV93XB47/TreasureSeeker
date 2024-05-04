package org.example.exceptions;

public class SimulationDataNotInitializedException extends RuntimeException {
    public SimulationDataNotInitializedException(String message) {
        super(message);
    }
}