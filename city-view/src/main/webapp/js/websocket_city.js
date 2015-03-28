var wsUri = "ws://" + document.location.host + "/cityboardendpoint";
var websocket = new WebSocket(wsUri);

websocket.onmessage = function(evt) { onMessage(evt) };

websocket.onopen = function(evt) { onOpen(evt) };

function sendText(json) {
    console.log("sending text: " + json);
    websocket.send(json);
}
                
function onMessage(evt) {
    console.log("received: " + evt.data);
    controler(evt.data);
}


function onOpen(evt) {
    console.log("open: " + evt.data);
    askForSun();
}


