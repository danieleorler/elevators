package com.tingco.codechallenge.elevator.services;

import com.tingco.codechallenge.elevator.api.Elevator;
import com.tingco.codechallenge.elevator.api.ElevatorController;
import com.tingco.codechallenge.elevator.api.exceptions.ElevatorBusyException;
import com.tingco.codechallenge.elevator.api.exceptions.ElevatorNotFoundException;
import com.tingco.codechallenge.elevator.api.exceptions.NoElevatorAvailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ElevatorControllerService implements ElevatorController {

    private final List<Elevator> freeElevators;
    private final List<Elevator> busyElevators;
    private final Executor elevatorExecutor;

    @Autowired
    public ElevatorControllerService(
            @Qualifier("elevatorExecutor") final Executor elevatorExecutor,
            final List<Elevator> elevators) {
        this.elevatorExecutor = elevatorExecutor;
        this.freeElevators = elevators;
        this.busyElevators = new ArrayList<>();
    }

    @Override
    public Elevator requestElevator(int toFloor) {

        if(freeElevators.size() == 0) {
            throw new NoElevatorAvailableException("Can't find an elevator for you");
        }

        final Elevator closestElevator = findClosestElevator(toFloor);
        borrowElevator(closestElevator);
        elevatorExecutor.execute(() -> closestElevator.moveElevator(toFloor));
        return closestElevator;
    }

    @Override
    public Elevator getElevator(int id) {
        Elevator elevator = freeElevators.stream()
            .filter(e -> e.getId() == id)
            .findFirst()
            .orElseThrow(() -> new ElevatorNotFoundException("Elevator " + id + "not found"));
        borrowElevator(elevator);
        return elevator;
    }

    @Override
    public List<Elevator> getElevators() {
        return Stream.concat(freeElevators.stream(), busyElevators.stream())
            .sorted()
            .collect(Collectors.toList());
    }

    @Override
    public void releaseElevator(Elevator elevator) {
        if(busyElevators.contains(elevator)) {
            busyElevators.remove(elevator);
            freeElevators.add(elevator);
        }
    }

    private Elevator findClosestElevator(final int targetFloor) {
        Elevator closestElevator = freeElevators.get(0);

        for(Elevator elevator : freeElevators) {
            if (Math.abs(elevator.getCurrentFloor() - targetFloor) < Math.abs(closestElevator.getCurrentFloor() - targetFloor)) {
                closestElevator = elevator;
            }
        }
        return closestElevator;
    }

    private void borrowElevator(Elevator elevator) {
        if(freeElevators.contains(elevator)) {
            freeElevators.remove(elevator);
            busyElevators.add(elevator);
        } else {
            throw new ElevatorBusyException("Elevator " + elevator.getId() + " is busy");
        }
    }
}
