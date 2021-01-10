

function fileUpload(){
    document.getElementById("uploadFile").click();
}


function displayAddRoomForm(){

    if(document.getElementById("addRoomForm").style.display == "none"){
        document.getElementById("addRoomForm").style.display = "";
    }else{
        document.getElementById("addRoomForm").style.display = "none";
    }

}

function displayRoomVerification(rid){
    if(document.getElementById("enterRoomForm"+rid).style.display == "none"){
        document.getElementById("enterRoomForm"+rid).style.display = "";
    }else{
        document.getElementById("enterRoomForm"+rid).style.display = "none";
    }
}

var socket;
var stompClient;

//connect to the room
function connectRoom(rid) {

    //connect to socketJS
    socket=new SockJS("https://localhost:10086/websocket/");
    stompClient = Stomp.over(socket);
    var insertEl = document.getElementById("message_room");
    //build connection
    stompClient.connect(
        {},
        function connectCallback (frame) {
            //connect success,then execute
            console.log('on connect:' + frame );

            stompClient.subscribe('/room/' + rid, function (message) {
                // showResponse(response.body);
                console.log(message.body);

                //parse to JSON
                var obj = JSON.parse(message.body);

                //generate new html block
                if(obj.sender == document.getElementById("userName").innerText){
                    var insertText = '<div class="outgoing_msg">\n' +
                        '              <div class="sent_msg">\n' +
                        '                <p>'+obj.content+'</p>\n' +
                        '                <span class="time_date">'+new Date()+'</span> </div>\n' +
                        '            </div>';
                }else{
                    var insertText = '<div class="incoming_msg">\n' +
                        '              <div class="incoming_msg_sender">\n' +
                        '                <p>'+obj.sender+'</p>\n' +
                        '              </div>\n' +
                        '              <div class="received_msg">\n' +
                        '                <div class="received_withd_msg">\n' +
                        '                  <p>'+obj.content+'</p>\n' +
                        '                  <span class="time_date">'+new Date()+'</span>\n' +
                        '                </div>\n' +
                        '              </div>\n' +
                        '            </div>';
                }

                insertEl.innerHTML = insertEl.innerHTML + insertText;
            });



        });
}

function sendMessage(){

    var rid = document.getElementById("roomID").innerText;
    var input = document.getElementById("writeMsg").value;
    if(input !== ""){
        stompClient.send("/msg/chat", {}, "{\n" +
            "    \"room\" :"+rid+",\n" +
            "    \"sender\":\""+document.getElementById("userName").innerText+"\",\n" +
            "    \"content\":\""+input+"\",\n" +
            "    \"type\":\"text\"\n" +
            "}");
    }

    document.getElementById("writeMsg").value = "";

}