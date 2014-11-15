/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


   
$(document).ready(function() {
    deleteCookies();
    document.body.style = "height: " + $(document).height();
    +"px";
    $(window).on('resize', fixElementsApperance);
    $(window).on("orientationchange", fixElementsApperance);
    
    

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
        document.getElementById("loader").style.fontSize = "3.3vh";
        var data = {
            name: $('#loginName').val(),
            pass: $('#loginPass').val()
        }
        $.ajax({
            url: SERVER_URL + "/newLogin",
            method: 'POST',
            data: JSON.stringify(data),
            success: function(resData) {
                document.getElementById("loader").style.display = "none";
                onGetDataFromServer(resData);
            },
            error: function() {
                document.getElementById("loader").style.fontSize = "none";
                alert("server error");
            }
        });
    }
    

    function onGetDataFromServer(resData) {
        var data = JSON.parse(resData);
        if (data.type == 0 || data.type == "0") {
             document.getElementById("errorLabel").style.display = "";
        } else {
            setCookie("userid", data.userID, 365);
            setCookie("type", data.type, 365);

            if (data.type == 1 || data.type == "1" ) {
                window.location = SERVER_URL + "/main.html";
            } else {
                var id = data.data.arrayValues[0].kidId;
                var name = data.kidName;
                var image = data.imageLink;
                window.location = SERVER_URL + "/child.html?id=" + id + "&name=" + name + "&img=" + image;
            }
        }
    }