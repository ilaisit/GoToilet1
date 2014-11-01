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

var UID = 0;
var KIDID = 0;


var kidIsInitiator = false;
var comments = "";

var action = "action-pee";
var status = "status-success";
var kidInit = "kidInit-yes";
var formValues = [];
formValues["action-poo"] = false;
formValues["action-pee"] = true;
formValues["radio-status"] = 0;
formValues["radio-kid-init"] = 0;
formValues["radio-open-door"] = "fullHelp";
formValues["radio-close-door"] = "fullHelp";
formValues["radio-drop-pants"] = "fullHelp";
formValues["radio-drop-underware"] = "fullHelp";
formValues["radio-wipe"] = "fullHelp";
formValues["radio-lift-underware"] = "fullHelp";
formValues["radio-lift-pants"] = "fullHelp";
formValues["radio-flush"] = "fullHelp";
formValues["radio-wash-hands"] = "fullHelp";
formValues["radio-wipe-hands"] = "fullHelp";

function buildJSONObject() {
    var obj = {
        insertingUserId: UID,
        kidId: KIDID,
        dateTime: "2004-02-01 00:00:00",
        createdIndependenceStages: [
            {independenceStage: "pantsUp", assistantLevel: formValues["radio-lift-pants"]},
            {independenceStage: "pantiesDown", assistantLevel: formValues["radio-drop-underware"]},
            {independenceStage: "pantiesUp", assistantLevel: formValues["radio-lift-underware"]},
            {independenceStage: "doorOpen", assistantLevel: formValues["radio-open-door"]},
            {independenceStage: "doorClose", assistantLevel: formValues["radio-close-door"]},
            {independenceStage: "handsWashed", assistantLevel: formValues["radio-wash-hands"]},
            {independenceStage: "wipe", assistantLevel: formValues["radio-wipe"]},
            {independenceStage: "flushWater", assistantLevel: formValues["radio-flush"]},
            {independenceStage: "handsDry", assistantLevel: formValues["radio-wipe-hands"]},
            {independenceStage: "pantsDown", assistantLevel: formValues["radio-drop-pants"]},
        ],
        kidIsInitiator: formValues["radio-kid-init"],
        isKaki: formValues["action-poo"],
        isPipi: formValues["action-pee"]
    }
    alert(JSON.stringify(obj));
    return obj;
}


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
        $(".pee").width($(".burger_btn").height() * 1);
        $(".poo").width($(".burger_btn").height() * 1.30);
        $(".profile_picture").css("margin-top", "-" + ($(".profile_picture").height() / 2) + "px");
        $(".profile_name").css("margin-top", "-" + ($(".profile_name").height() / 2) + "px");
        $(".profile_name").css("right", (Math.ceil($(".profile_picture").width() * 1.7)) + "px");
        $("[name=checkbox]").width($("[name=checkbox]").height());
        $(".radio_btn").width($(".radio_btn").height());
        $(".radio_btn_small").width($(".radio_btn_small").height());
        $("textarea").click(function() {
            $("textarea").html("");
        });
    }

    function setRadios() {
        var allCB = document.getElementsByName("checkbox");
        alert(allCB.length)
        for (var i = 0; i < allCB.length; i++) {
            var id = allCB[i].id;
            //$("#"+id).bind('mousedown touchstart', getRadioDownFunction(id));
            //$("#"+id).bind('mouseup touchstart', getRadioDownFunction(id));
            //.on('click touchstart'
            allCB[i].onclick = getCBClickFunction(id);
        }
    }



    function setRadios() {
        var allRadios = document.getElementsByClassName("radio_btn");
        for (var i = 0; i < allRadios.length; i++) {
            var name = allRadios[i].getAttribute("name");
            var id = allRadios[i].id;
            //$("#"+id).bind('mousedown touchstart', getRadioDownFunction(id));
            //$("#"+id).bind('mouseup touchstart', getRadioDownFunction(id));
            //.on('click touchstart'
            allRadios[i].onclick = getRadioClickFunction(name, id);
        }

        var allRadios = document.getElementsByClassName("radio_btn_small");
        for (var i = 0; i < allRadios.length; i++) {
            var name = allRadios[i].getAttribute("name");
            var id = allRadios[i].id;
            //$("#"+id).bind('mousedown touchstart', getRadioDownFunction(id));
            //$("#"+id).bind('mouseup touchstart', getRadioDownFunction(id));
            //.on('click touchstart'
            allRadios[i].onclick = getSmallRadioClickFunction(name, id);
        }
    }

    function getRadioClickFunction(name, id) {
        return function() {
            $('[name="' + name + '"]').removeClass("radio_on").addClass("radio_off");
            $('#' + id).removeClass("radio_off").addClass("radio_on");
            formValues[name] = document.getElementById(id).getAttribute("value");
        };
    }

    function getSmallRadioClickFunction(name, id) {
        return function() {
            $('[name="' + name + '"]').removeClass("radio_small_on").addClass("radio_small_off");
            $('#' + id).removeClass("radio_small_off").addClass("radio_small_on");
            formValues[name] = document.getElementById(id).getAttribute("value");
        };
    }

    function getRadioDownFunction(id) {
        return function(event) {
            $('#' + id).removeClass("radio_off").addClass("radio_hover");
        };
    }

    $("#independentLine").click(function(){
      $("#bottom_table").slideToggle();
    });

    fixElementsApperance();
    setRadios();
    document.getElementById("childName").innerHTML = QueryString.name;
    document.getElementById("profilePicture").src = "images/" + QueryString.img;


});


function postToServer() {
    var data = buildJSONObject();
    $.ajax({
        url: "http://192.168.30.166:8080/diaryServer/newLogin",
        method: 'POST',
        data: JSON.stringify(data),
        success: function(resData) {
            document.getElementById("mainDiv").innerHTML = resData;
        },
        error: function() {
            alert("server error");
        }
    });
}


function cbClick(id) {
    var cb = document.getElementById("action-"+id);
    
    if (cb.className == "checkbox_on") {
        cb.className = "checkbox_off";
    } else {
        cb.className = "checkbox_on";
    }
    formValues[cb.id] = !formValues[cb.id];
}