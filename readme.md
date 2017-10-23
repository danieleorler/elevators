# Elevator Coding Challenge

## Implementation

The project is composed by:
* the backend which is a spring boot application
* the frontend which is a simple (and pretty crappy) javascript application

The operations on elevators are async, for example if you ask for an elevator you'll get back which elevator is coming to you.   
But since the elevators don't move instantaneously, it will take a bit of time for the to get to you.
Don't worry, you'll have a way to track the elevator progress.

### Backend
The `ElevatorControllerService` is responsible for dispatching the closest elevator to the user which required one.
It also allows to get an elevator by id, in order to simulate when the user actually enters an elevator.

The `ElevatorImpl` class represents the elevator itself. When it steps to the next floor it sleeps for some time (configurable) to simulate the movement.   
This class fires events when the elevator stops moving or when it reaches a floor.

The events are handled by `ElevatorChangesSubscriber` which, among other things, forwards the event to a WebSocket topic.

There are 3 REST endpoints to interact with the elevators:

`GET /shaft`   
returns the current status of the elevators' shaft

`GET /request/{floor}`   
requests an elevator to the floor {floor}

`GET /elevator/{id}/move/{floor}`   
moves the elevator {id} to the floor {floor}


### FrontEnd
This is just a super fast implementation of a javascript client and it is served by the same spring boot app running the backend.   
It shows (in real time) the status of the shaft and allows to request and move elevators.   
The elevators are controlled via HTTP requests to the backend, while their position is updated by listening to a WebSocket topic.   
You can find instruction on how to use the UI on the homepage http://localhost:8080

N.B. only tested on Chrome on a Mac.

## Configuration
The number of elevators and number of floors is configurable from the file `application.properties`

## Build And Run

As the project is, the Spring app can be started as seen below.

build and run the code with Maven

    mvn package
    mvn spring-boot:run

Now point you browser (please use chrome) to http://localhost:8080 you should see the initial status of the shaft and you can interact with it.

### Go Social
If you want to see how the app works with multiple users you can change the backend address on the javascript app (line 2 in `resouces/static/app.js`) to the ip of you computer (i.e. 192.168.0.2).   
Share the new address (i.e. http://192.168.0.2:8080) with somebody on the same network and start moving elevators.
You can even try from a smartphone (the experience is total crap though)

## Limitations / Future Improvements

### elevators don't go below floor 0
This could be fixed with a set of properties to configure lower and upper bound

### elevators serve a user to completion before being available again
this means that it won't stop picking up somebody on his way to the target.   
The solution could be adding a queue of destinations to each elevator.


