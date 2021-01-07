


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

function displayRoomVerification(){
    if(document.getElementById("enterRoomForm").style.display == "none"){
        document.getElementById("enterRoomForm").style.display = "";
    }else{
        document.getElementById("enterRoomForm").style.display = "none";
    }
}

// function validateRoomEntry(){
//     console.log(document.getElementById("requestRoomPassword"));
//     console.log(document.getElementById())
//     if(document.getElementById("requestRoomPassword")==document.getElementById("inputRoomPassword")){
//         return true;
//     }else{
//         alert("wrong password for this room");
//         return false;
//     }
//
// }