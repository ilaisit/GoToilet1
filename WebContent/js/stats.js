/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

google.load("visualization", "1", {packages: ["corechart"]});

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
    $(".back_btn").css("margin-top", "-" + ($(".back_btn").height() / 2) + "px");
    $(".burger_btn").css("margin-top", "-" + ($(".burger_btn").height() / 2) + "px");
    $(".profile_picture").css("margin-top", "-" + ($(".profile_picture").height() / 2) + "px");
    $(".profile_name").css("margin-top", "-" + ($(".profile_name").height() / 2) + "px");
    $(".profile_name").css("right", (Math.ceil($(".profile_picture").width() * 1.7)) + "px");

}

function setTables() {
    
    google.setOnLoadCallback(drawChart);
    function drawChart() {
        alert("4");
        var data = google.visualization.arrayToDataTable([
            ['הצלחות', 'ימים'],
            ['2013', 1000],
            ['2014', 1170],
            ['2015', 660],
            ['2016', 1030]
        ]);

        var options = {
            title: 'הצלחות',
            hAxis: {title: 'הצלחות', titleTextStyle: {color: '#333'}},
            vAxis: {title: 'ימים', titleTextStyle: {color: '#333'}, minValue: 0},
            'width': $("#mainContent").width() * 0.75,
            'height': $("#mainContent").height() * 0.65
        };

        alert("3");
        var chart1 = new google.visualization.AreaChart(document.getElementById('chart_div1'));
        var chart2 = new google.visualization.AreaChart(document.getElementById('chart_div2'));
        chart1.draw(data, options);
        chart2.draw(data, options);
    }
}


$(document).ready(function() {
    $("#my-menu").mmenu();
    document.body.style = "height: " + $(document).height();
    $(window).on('resize', fixElementsApperance);
    $(window).on("orientationchange", fixElementsApperance);

    fixElementsApperance();
    setTables();


    //alert(document.getElementById("childName") + ":::" + QueryString.name);
    document.getElementById("childName").innerHTML = decodeURI(QueryString.name);
    document.getElementById("profilePicture").src = "images/" + QueryString.img;

    $("#success").click(function() {
        if ($("#success_arrow").hasClass("up")) {
            $("#success_arrow").removeClass("up").addClass("down");
        } else {
            $("#success_arrow").removeClass("down").addClass("up");
        }
        $("#chart_div1").slideToggle();
    });

    $("#times").click(function() {
        if ($("#times_arrow").hasClass("up")) {
            $("#times_arrow").removeClass("up").addClass("down");
        } else {
            $("#times_arrow").removeClass("down").addClass("up");
        }
        $("#chart_div2").slideToggle();
    });
    setTimeout("fixTables();", 1000);





});

function fixTables() {
    $("#chart_div2").hide();
}






