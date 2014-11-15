/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function fixElementsApperance() {
    $(".burger_btn").width($(".burger_btn").height() * 1.91);
    $(".back_btn").css("margin-top", "-" + ($(".back_btn").height() / 2) + "px");
    $(".burger_btn").css("margin-top", "-" + ($(".burger_btn").height() / 2) + "px");
}


$(document).ready(function() {
    $("#my-menu").mmenu();
    document.body.style = "height: " + $(document).height();
    $(window).on('resize', fixElementsApperance);
    $(window).on("orientationchange", fixElementsApperance);

    fixElementsApperance();
});







