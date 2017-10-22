stompClient = undefined;
BASE_URL = "http://192.168.0.2:8080";
BASE_PATH = BASE_URL+"/rest/v1";

function httpGet(theUrl, callback)
{
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() { 
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200)
            callback(xmlHttp.responseText);
    }
    xmlHttp.open("GET", theUrl, true);
    xmlHttp.send(null);
}

function duplicate(elevator) {
    var original = document.getElementById('base-elevator');
    var clone = original.cloneNode(true);
    clone.id = "elevator-" + elevator.id;
    clone.style = "float: left;";
    moveElevator(clone.children, elevator.currentFloor, elevator.busy?"busy":"available");
    original.parentNode.appendChild(clone);
}

function moveElevator(floors, targetFloor, className) {
    let numberOfFloors = floors.length-1;
    Array.from(floors).forEach((item, key) => {
        if(numberOfFloors - key === targetFloor) {
            item.className = "floor " + className;
        } else {
            item.className = "floor";
        }
    })
}

function selectFloor(element) {
    if(element.classList.contains("available")) {
        element.getElementsByTagName("input")[0].classList.remove("hidden");
    }
}

function requestElevator(floor) {
    httpGet(BASE_PATH + "/request/"+floor, (data) => {
        console.log(data);
    });
}

function connect() {
    var socket = new SockJS(BASE_URL+'/connect');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/arrivedToFloor', (message) => {
            let elevator = JSON.parse(message.body).elevator;
            let element = document.getElementById("elevator-"+elevator.id);
            moveElevator(element.children, elevator.currentFloor, elevator.busy?"busy":"available");
        });
        stompClient.subscribe('/topic/finishedMoving', (message) => {
            let elevator = JSON.parse(message.body).elevator;
            let element = document.getElementById("elevator-"+elevator.id);
            moveElevator(element.children, elevator.currentFloor, "available");
        });
    });
}

function goToFloor(event) {
    if (event.which == 13 || event.keyCode == 13) {
        let targetFloor = event.srcElement.value;
        let elevator = event.srcElement.parentNode.parentNode.id.split("-")[1];
        httpGet(BASE_PATH + "/elevator/"+elevator+"/move/"+targetFloor, (data) => {
            console.log(data);
            event.srcElement.classList.add("hidden");
        })
    }
    
}

httpGet(BASE_PATH + "/shaft", (data) => {
    elevators = JSON.parse(data);
    console.log(elevators);
    elevators.forEach((elevator) => {
        duplicate(elevator);
    });
});
connect();