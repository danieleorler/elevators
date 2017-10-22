package com.tingco.codechallenge.elevator.api;

import com.google.common.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ElevatorImplTest {

    @Mock
    EventBus eventBus;

    private Elevator unitUnderTest;

    @Before
    public void setUp() {
        unitUnderTest = new ElevatorImpl(0, eventBus, 3, 5L);
    }

    @Test
    public void should_arrive_to_correct_floor() {
        unitUnderTest.moveElevator(2);
        assertEquals(2, unitUnderTest.getCurrentFloor());
        unitUnderTest.moveElevator(0);
        assertEquals(0, unitUnderTest.getCurrentFloor());
        unitUnderTest.moveElevator(0);
        assertEquals(0, unitUnderTest.getCurrentFloor());
        unitUnderTest.moveElevator(15);
        assertEquals(2, unitUnderTest.getCurrentFloor());
        unitUnderTest.moveElevator(-2);
        assertEquals(0, unitUnderTest.getCurrentFloor());
        unitUnderTest.moveElevator(3);
        assertEquals(2, unitUnderTest.getCurrentFloor());
    }

}