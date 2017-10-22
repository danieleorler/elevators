package com.tingco.codechallenge.elevator.api.exceptions;

/**
 * @author daniele.orler
 */
public class ElevatorBusyException extends RuntimeException {
    public ElevatorBusyException(String message) {
        super(message);
    }
}
