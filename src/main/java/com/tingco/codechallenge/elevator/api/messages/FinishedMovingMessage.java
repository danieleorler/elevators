package com.tingco.codechallenge.elevator.api.messages;

import com.tingco.codechallenge.elevator.api.Elevator;

/**
 * @author daniele.orler
 */
public class FinishedMovingMessage {

    private final Elevator elevator;

    public FinishedMovingMessage(Elevator elevator) {
        this.elevator = elevator;
    }

    public Elevator getElevator() {
        return elevator;
    }
}
