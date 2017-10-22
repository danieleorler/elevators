package com.tingco.codechallenge.elevator.api;

import java.util.List;

public class Shaft {

    private final int numberOfFloors;
    private final List<Elevator> elevators;

    public Shaft(int numberOfFloors, List<Elevator> elevators) {
        this.numberOfFloors = numberOfFloors;
        this.elevators = elevators;
    }

    public int getNumberOfFloors() {
        return numberOfFloors;
    }

    public List<Elevator> getElevators() {
        return elevators;
    }
}
