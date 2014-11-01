/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var UID = -1;

function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ')
            c = c.substring(1);
        if (c.indexOf(name) != -1)
            return c.substring(name.length, c.length);
    }
    return "";
}

function getChildren() {
    var data = {
        userId: UID
    }
    $.ajax({
        url: SERVER_URL + "/viewGarden",
        method: 'POST',
        data: JSON.stringify(data),
        success: function(resData) {
            addChildrenToPage()
        },
        error: function() {
            alert("server error");
        }
    });
}

function addChildrenToPage(resData) {
    var data = JSON.parse(resData);
    var gardenName = data.name;
    var allChildern = JSON.parse(data.data);
    
}

//UID = getCookie("userid");
UID = "4";
if (UID < 0 || UID == "" || !UID) {
    alert("ERROR no UID");
} else {
   alert("UID FOUND!!   UID=" + UID)
   getChildren();
    
    $(document).ready(function() {
        $("#my-menu").mmenu();
        document.body.style = "height: " + $(document).height();
        +"px";
        $(window).on('resize', fixElementsApperance);



        function fixElementsApperance() {
            $("#profilePicture").width($("#profilePicture").height());
            $(".time_box").css("margin-top", "-" + ($(".time_box").height() / 2) + "px");
            $(".burger_btn").width($(".burger_btn").height() * 1.91);
            $(".burger_btn").css("margin-top", "-" + ($(".burger_btn").height() / 2) + "px");
            $(".profile_picture").css("margin-top", "-" + ($(".profile_picture").height() / 2) + "px");
            $(".profile_name").css("margin-top", "-" + ($(".profile_name").height() / 2) + "px");
            $(".profile_name").css("right", (Math.ceil($(".profile_picture").width() * 1.7)) + "px");

        }

        fixElementsApperance();


    });

}


