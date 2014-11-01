/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


   
$(document).ready(function() {
    document.body.style = "height: " + $(document).height();
    +"px";
    $(window).on('resize', fixElementsApperance);
    
    

    function fixElementsApperance() {
        $(".burger_btn").width($(".burger_btn").height() * 1.91);
        $(".burger_btn").css("margin-top", "-" + ($(".burger_btn").height() / 2) + "px");
        $(".login_box").css("margin-top", "-" + ($(".login_box").height() / 1.5) + "px");
        $(".login_box").css("margin-right", "-" + ($(".login_box").width() / 2) + "px");
    }
    
    fixElementsApperance();
    
    

    

    
    
    
});
    function postToServer() {
        document.getElementById("loader").style.display = "";
        var data = {
            name: $('#loginName').val(),
            pass: $('#loginPass').val()
        }
        $.ajax({
            url: "http://localhost:8080/diaryServer/newLogin",
            method: 'POST',
            data: JSON.stringify(data),
            success: function(resData) {
                document.getElementById("loader").style.display = "none";
                onGetDataFromServer(resData);
            },
            error: function() {
                document.getElementById("loader").style.display = "none";
                alert("server error");
            }
        });
    }
    

    function onGetDataFromServer(resData) {
        alert(resData);
        var data = JSON.parse(resData);
        if (data.type == 0 || data.type == "0") {
             document.getElementById("errorLabel").style.display = "";
        } else {
            document.cookie="userid=" + data.userId;
            if (data.type == 1 || data.type == "1" ) {
                window.location = "http://localhost:8080/diaryServer/main.html";
            } else {
                alert("data.data  " + data.data)
                var children = JSON.parse(data.data);
                var parentChild = JSON.parse(children.arrayValues[0]);
                window.location = "http://localhost:8080/diaryServer/child.html?id=" + parentChild.kidId;
            }
        }
    }