stompClient = undefined;
BASE_URL = "http://192.168.0.2:8080";
BASE_PATH = BASE_URL+"/rest/v1";

function httpGet(theUrl, callback)
{
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = () => { 
        if (xmlHttp.readyState == 4) {
            if(xmlHttp.status == 200) {
                callback(xmlHttp.responseText);
            } else {
                console.error(xmlHttp.responseText);
            }
            
        }
    }
    xmlHttp.open("GET", theUrl, true);
    xmlHttp.send(null);
}

function renderElevator(elevator, numberOfFloors) {
    var original = document.getElementById('base-elevator');
    var clone = original.cloneNode(true);
    clone.id = "elevator-" + elevator.id;
    clone.style = "float: left;";
    clone.children[0].getElementsByTagName("input")[0].setAttribute("max", numberOfFloors-1);
    for(let i=0; i<numberOfFloors-1; i++) {
        let floor = clone.children[0].cloneNode(true);
        floor.getElementsByTagName("input")[0].setAttribute("max", numberOfFloors-1);
        clone.appendChild(floor);
    }
    moveElevator(clone.children, elevator.currentFloor, elevator.busy?"busy":"available");
    original.parentNode.appendChild(clone);
}

function renderControls(numberOfFloors) {
    for(let i=numberOfFloors-1; i>=0; i--) {
        let control = document.createElement("div");
        control.appendChild(document.createTextNode("request"));
        control.classList.add("button");
        control.addEventListener("click", requestElevator, false);
        control.setAttribute("data-floor", i);
        document.getElementById('controls').appendChild(control);
    }
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

function requestElevator(event) {
    let floor = event.srcElement.getAttribute('data-floor');
    httpGet(BASE_PATH + "/request/"+floor, (data) => {});
}

function connect() {
    var socket = new SockJS(BASE_URL+'/connect');
    stompClient = Stomp.over(socket);
    stompClient.debug = null;
    stompClient.connect({}, function (frame) {
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
            event.srcElement.classList.add("hidden");
        })
    }
    
}

httpGet(BASE_PATH + "/shaft", (data) => {
    shaft = JSON.parse(data);
    shaft.elevators.forEach((elevator) => {
        renderElevator(elevator, shaft.numberOfFloors);
    });
    renderControls(shaft.numberOfFloors);
});
connect();