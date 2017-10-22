package com.tingco.codechallenge.elevator.messanging;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.tingco.codechallenge.elevator.api.Elevator;
import com.tingco.codechallenge.elevator.api.messages.ArrivedToFloorMessage;
import com.tingco.codechallenge.elevator.api.messages.FinishedMovingMessage;
import com.tingco.codechallenge.elevator.services.ElevatorControllerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * @author daniele.orler
 */
@Controller
public class ElevatorChangesSubscriber {

    private final ElevatorControllerService elevatorControllerService;
    private final SimpMessagingTemplate broker;

    private static final Logger logger = LoggerFactory.getLogger(ElevatorChangesSubscriber.class);

    @Autowired
    public ElevatorChangesSubscriber(ElevatorControllerService elevatorControllerService, EventBus eventBus, SimpMessagingTemplate broker) {
        this.elevatorControllerService = elevatorControllerService;
        this.broker = broker;
        eventBus.register(this);
    }

    @Subscribe
    void handleArrivedToFloor(ArrivedToFloorMessage message) {
        Elevator e = message.getElevator();
        logger.debug("Elevator {} arrived to floor {}", e.getId(), e.getCurrentFloor());
        broker.convertAndSend("/topic/arrivedToFloor", message);
    }

    @Subscribe
    void handleFinishedMoving(FinishedMovingMessage message) {
        Elevator e = message.getElevator();
        elevatorControllerService.releaseElevator(e);
        logger.debug("Elevator {} has finished moving", e.getId());
        broker.convertAndSend("/topic/finishedMoving", message);
    }
}
