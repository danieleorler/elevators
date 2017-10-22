package com.tingco.codechallenge.elevator.services;

import com.google.common.eventbus.EventBus;
import com.tingco.codechallenge.elevator.api.Elevator;
import com.tingco.codechallenge.elevator.api.ElevatorImpl;
import com.tingco.codechallenge.elevator.api.exceptions.ElevatorNotFoundException;
import com.tingco.codechallenge.elevator.api.exceptions.NoElevatorAvailableException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

/**
 * @author daniele.orler
 */
@RunWith(MockitoJUnitRunner.class)
public class ElevatorControllerServiceTest {

    @Mock
    EventBus eventBus;

    @Spy
    Executor executor = Executors.newScheduledThreadPool(2);

    private List<Elevator> elevators;
    private ElevatorControllerService unitUnderTest;
    private int numberOfFloors = 5;

    @Before
    public void setUp() {
        elevators = new ArrayList<>();
        elevators.add(new ElevatorImpl(0, eventBus, numberOfFloors));
        elevators.add(new ElevatorImpl(5, eventBus, numberOfFloors));

        unitUnderTest = new ElevatorControllerService(executor, elevators);
    }


    @Test(expected = NoElevatorAvailableException.class)
    public void should_throw_exception_when_elevator_not_available() throws Exception {
        unitUnderTest = new ElevatorControllerService(executor, new ArrayList<>());
        unitUnderTest.requestElevator(2);
    }

    @Test(expected = NoElevatorAvailableException.class)
    public void should_throw_exception_when_all_elevators_are_busy() throws Exception {
        unitUnderTest.requestElevator(2);
        unitUnderTest.requestElevator(2);
        unitUnderTest.requestElevator(2);
    }

    @Test
    public void should_provide_the_closest_elevator() {
        assertEquals(5, unitUnderTest.requestElevator(4).getId());
        assertEquals(0, unitUnderTest.requestElevator(1).getId());
    }

    @Test
    public void should_return_the_requested_elevator() {
        assertEquals(5, unitUnderTest.getElevator(5).getId());
    }

    @Test(expected = ElevatorNotFoundException.class)
    public void should_throw_exception_when_trying_to_get_a_busy_elevator() {
        Elevator elevator = unitUnderTest.requestElevator(2);
        unitUnderTest.getElevator(elevator.getId()).getId();
    }

    @Test
    public void should_release_the_elevator() {
        List<Elevator> elevators = new ArrayList<>();
        elevators.add(new ElevatorImpl(0, eventBus, numberOfFloors));
        unitUnderTest = new ElevatorControllerService(executor, elevators);

        Elevator elevator = unitUnderTest.requestElevator(2);
        assertEquals(0, elevator.getId());
        unitUnderTest.releaseElevator(elevator);
        elevator = unitUnderTest.requestElevator(2);
        assertEquals(0, elevator.getId());
    }

}