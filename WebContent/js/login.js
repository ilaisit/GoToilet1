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
});