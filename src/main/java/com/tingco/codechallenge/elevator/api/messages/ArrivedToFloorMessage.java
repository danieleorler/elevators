package com.tingco.codechallenge.elevator.api.messages;

import com.tingco.codechallenge.elevator.api.Elevator;

/**
 * @author daniele.orler
 */
public class ArrivedToFloorMessage {

    private final Elevator elevator;

    public ArrivedToFloorMessage(Elevator elevator) {
        this.elevator = elevator;
    }

    public Elevator getElevator() {
        return elevator;
    }
}
