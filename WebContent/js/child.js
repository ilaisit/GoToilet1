    /* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var rowTemplate =   "<td>[DATE_TIME]</td>"+
                    "<td class='details'>"+
                        "<div class='[INIT]'>הליכה יזומה</div>"+
                        "<div class='spacer'>&nbsp;</div>"+
                        "<div class='[MISS]'>פספוס</div>"+
                    "</td>"+
                    "<td class='sign'>[PIPI]</td>"+
                    "<td class='sign'>[KAKI]</td>";

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


var userType = getCookie("type");

getChildren();

function addChildInfo(resData) {
    var data = JSON.parse(resData);
    var rows = data.data.arrayValues;
    var mainTable = document.getElementById("mainTable");
    for (var i = 0; i < rows.length; i++) {
        var tr = document.createElement("tr");
        var inner = rowTemplate;
        inner = inner.replace("[DATE_TIME]", rows[i].dateTime);
        if (rows[i].isKaki){
            inner = inner.replace("[KAKI]","&nbsp;+");
        } else {
            inner = inner.replace("[KAKI]","&nbsp;-");
        }
        
        if (rows[i].isPipi) {
            inner= inner.replace("[PIPI]","&nbsp;+");
        } else {
            inner = inner.replace("[PIPI]","&nbsp;+");
        }
             
        if (rows[i].kidIsInitiator) {
            inner = inner.replace("[INIT]","cb_v");
        } else {
            inner = inner.replace("[INIT]","cb");
        }
   
        if (rows[i].successResult) {
            inner = inner.replace("[MISS]","cb_v");
        } else {
            inner = inner.replace("[MISS]","cb");
        }
        
        tr.innerHTML = inner;
        mainTable.appendChild(tr);
    }
    
    
    fixElementsApperance();
}

function fixElementsApperance() {
    $(".profile_picture").width($(".profile_picture").height());
    $(".burger_btn").width($(".burger_btn").height() * 1.91);
    $(".back_btn").css("margin-top", "-" + ($(".back_btn").height() / 2) + "px");
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
    
    
    if (userType == "2" || userType == 2) {
        $(".back_btn").hide();
    }

    fixElementsApperance();
    
    document.getElementById("childName").innerHTML = decodeURI(QueryString.name);
    document.getElementById("profilePicture").src = "images/" + QueryString.img;
    document.getElementById("newEventLink").href = "add-event.html?id=" + QueryString.id + "&name=" +  QueryString.name + "&img=" +  QueryString.img;
    document.getElementById("statsLink").href = "stats.html?id=" + QueryString.id + "&name=" +  QueryString.name + "&img=" +  QueryString.img;
    
    
});




