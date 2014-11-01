/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var rowTemplate = 
                    <td>8:00</td> 
                    <td class='details'>
                        <div class='[INIT]'>הליכה יזומה</div>
                        <div class='spacer'>&nbsp;</div>
                        <div class='[MISS]'>פספוס</div>
                    </td>
                    <td class='sign'>&nbsp;-</td>
                    <td class='sign'>&nbsp;-</td>
                </tr>"
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

getChildren();

function addChildInfo(resData) {
    alert("add child info \n" + resData);
    var data = JSON.parse(resData);
    var rows = data.data.arrayValues;
    var mainTable = document.getElementById("mainTable");
    for (var i = 0; i < rows.length; i++) {
        var tr = document.createElement("tr");
        var inner = rowTemplate;
        inner.replace("[DATE_TIME]",rows[i].dateTime);
        if (rows[i].isKaki){
            inner.replace("[KAKI]","&nbsp;+");
        } else {
            inner.replace("[KAKI]","&nbsp;-");
        }
        
        if (rows[i].isPipi) {
            inner.replace("[PIPI]","&nbsp;+");
        } else {
            inner.replace("[PIPI]","&nbsp;+");
        }
             
        if (rows[i].kidIsInitiator) {
            inner.replace("[INIT]","cb_v");
        } else {
            inner.replace("[INIT]","cb");
        }
   
        if (rows[i].successResult) {
            inner.replace("[MISS]","cb_v");
        } else {
            inner.replace("[MISS]","cb");
        }
        
        tr.innerHTML = inner;
        mainTable.appendChild(tr);
    }
    
    
    fixElementsApperance();
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

    fixElementsApperance();
});




