/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var QueryString = function() {
    // This function is anonymous, is executed immediately and 
    // the return value is assigned to QueryString!
    var query_string = {};
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split("=");
        // If first entry with this name
        if (typeof query_string[pair[0]] === "undefined") {
            query_string[pair[0]] = pair[1];
            // If second entry with this name
        } else if (typeof query_string[pair[0]] === "string") {
            var arr = [query_string[pair[0]], pair[1]];
            query_string[pair[0]] = arr;
            // If third or later entry with this name
        } else {
            query_string[pair[0]].push(pair[1]);
        }
    }
    return query_string;
}();

function getChildren() {
    var data = {
        kidId: QueryString.id
    };
    $.ajax({
        url: SERVER_URL + "/viewKid",
        method: 'POST',
        data: JSON.stringify(data),
        success: function(resData) {
            addChildInfo(resData);
        },
        error: function() {
            alert("server error");
        }
    });
}

function fixElementsApperance() {
    $(".profile_picture").width($(".profile_picture").height());
    $(".burger_btn").width($(".burger_btn").height() * 1.91);
    $(".burger_btn").css("margin-top", "-" + ($(".burger_btn").height() / 2) + "px");
    $(".profile_picture").css("margin-top", "-" + ($(".profile_picture").height() / 2) + "px");
    $(".profile_name").css("margin-top", "-" + ($(".profile_name").height() / 2) + "px");
    $(".profile_name").css("right", (Math.ceil($(".profile_picture").width() * 1.7)) + "px");

}


$(document).ready(function() {
    $("#my-menu").mmenu();
    document.body.style = "height: " + $(document).height();
    +"px";
    $(window).on('resize', fixElementsApperance);
    $(window).on("orientationchange", fixElementsApperance);

    fixElementsApperance();
    
    document.getElementById("childName").innerHTML = decodeURI(QueryString.name);
    document.getElementById("profilePicture").src = "images/" + QueryString.img;

    $("#success").click(function(){
        if ($("#success_arrow").hasClass("up")) {
            $("#success_arrow").removeClass("up").addClass("down");
        } else {
            $("#success_arrow").removeClass("down").addClass("up");
        }
      $("#chart_div1").slideToggle();
    });
    
    $("#times").click(function(){
        if ($("#times_arrow").hasClass("up")) {
            $("#times_arrow").removeClass("up").addClass("down");
        } else {
            $("#times_arrow").removeClass("down").addClass("up");
        }
      $("#chart_div2").slideToggle();
    });
    setTimeout("fixTables();",1000);

    
});

    function fixTables() {
        $("#chart_div2").hide();
    }




