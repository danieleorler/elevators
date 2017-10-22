package com.tingco.codechallenge.elevator.api;

import com.google.common.eventbus.EventBus;
import com.tingco.codechallenge.elevator.api.exceptions.ElevatorBusyException;
import com.tingco.codechallenge.elevator.api.messages.ArrivedToFloorMessage;
import com.tingco.codechallenge.elevator.api.messages.FinishedMovingMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

/**
 * @author daniele.orler
 */
public class ElevatorImpl implements Elevator {

    private static final Logger logger = LoggerFactory.getLogger(ElevatorImpl.class);

    private final int id;
    private final EventBus eventBus;
    private final Long timeToFloor;

    private int currentFloor = 0;
    private int addressedFloor = -1;
    private Direction direction = Direction.NONE;



    public ElevatorImpl(int id, EventBus eventBus) {
        this.id = id;
        this.eventBus = eventBus;
        this.currentFloor = id;
        this.timeToFloor = 1000L;
    }

    public ElevatorImpl(int id, EventBus eventBus, Long timeToFloor) {
        this.id = id;
        this.eventBus = eventBus;
        this.currentFloor = id;
        this.timeToFloor = timeToFloor;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public int getAddressedFloor() {
        return addressedFloor;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void moveElevator(int toFloor) {

        if(isBusy()) {
            throw new ElevatorBusyException("Elevator " + id + " is busy");
        }
        addressedFloor = toFloor;
        direction = movingDirection(toFloor);
        logger.debug("Elevator " + id + " going from floor " + currentFloor + " to floor " + addressedFloor);

        IntStream.range(0, Math.abs(currentFloor-toFloor))
            .forEach(value -> {
                try {
                    Thread.sleep(timeToFloor);
                    currentFloor += direction.change();
                    eventBus.post(new ArrivedToFloorMessage(this));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

        this.direction = Direction.NONE;
        eventBus.post(new FinishedMovingMessage(this));
    }

    @Override
    public boolean isBusy() {
        return this.direction != Direction.NONE;
    }

    @Override
    public int getCurrentFloor() {
        return currentFloor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ElevatorImpl elevator = (ElevatorImpl) o;

        return getId() == elevator.getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }

    private Elevator.Direction movingDirection(final int toFloor) {
        if(getCurrentFloor() > toFloor) {
            return Elevator.Direction.DOWN;
        } else if(getCurrentFloor() < toFloor) {
            return Elevator.Direction.UP;
        } else {
            return Elevator.Direction.NONE;
        }
    }

    @Override
    public int compareTo(Object o) {
        Elevator other = (Elevator) o;

        if(this.getId() > other.getId()) {
            return 1;
        }

        if(this.getId() < other.getId()) {
            return -1;
        }

        return 0;
    }
}
