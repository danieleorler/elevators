package com.tingco.codechallenge.elevator.resources;

import com.tingco.codechallenge.elevator.api.Elevator;
import com.tingco.codechallenge.elevator.api.ElevatorController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * Rest Resource.
 *
 * @author Sven Wesley
 *
 */
@RestController
@RequestMapping("/rest/v1")
@CrossOrigin(origins = "*")
public final class ElevatorControllerEndPoints {

    private final ElevatorController elevatorController;
    private final Executor elevatorExecutor;

    @Autowired
    public ElevatorControllerEndPoints(
            ElevatorController elevatorController,
            @Qualifier("elevatorExecutor") Executor elevatorExecutor) {
        this.elevatorController = elevatorController;
        this.elevatorExecutor = elevatorExecutor;
    }

    /**
     * Ping service to test if we are alive.
     *
     * @return String pong
     */
    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public String ping() {
        return "pong";
    }

    @RequestMapping(value = "/shaft", method = RequestMethod.GET)
    public List<Elevator> getElevators() {
        return elevatorController.getElevators();
    }

    @RequestMapping(value="/request/{floor}")
    public String requestElevator(@PathVariable int floor) {
        Elevator elevator = elevatorController.requestElevator(floor);
        return "Elevator " + elevator.getId() + " is coming";
    }

    @RequestMapping(value="/elevator/{id}/move/{floor}")
    public String requestElevator(@PathVariable int id, @PathVariable int floor) {
        Elevator elevator = elevatorController.getElevator(id);
        elevatorExecutor.execute(() -> elevator.moveElevator(floor));
        return "Elevator " + elevator.getId() + " is moving to floor " + floor;
    }
}
