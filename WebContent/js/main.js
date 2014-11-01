/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var kidTemplate = '<img src="[IMG_SRC]" class="profile_picture v_align background_cover"/>'+
                  '<div class="profile_name v_align">[NAME_OF_CHILD]</div>';
         
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
    };
    $.ajax({
        url: SERVER_URL + "/viewGarden",
        method: 'POST',
        data: JSON.stringify(data),
        success: function(resData) {
            addChildrenToPage(resData);
        },
        error: function() {
            alert("server error");
        }
    });
}

function addChildrenToPage(resData) {
    alert("add children \n" + resData);
    var data = JSON.parse(resData);
    var gardenName = data.name;
    document.getElementById("header").innerHTML = gardenName ;
    var allChildern = data.data.arrayValues;
    var mainDiv = document.getElementById("mainContent");
    
    for(var i = 0; i < allChildern.length; i++) {
        var newChildDiv = document.createElement("div");
        newChildDiv.className = "kid_header";
        newChildDiv.onclick = getKidFunction(allChildern[i].kidId, allChildern[i].kidName, allChildern[i].imageLink);
        var innerContant = kidTemplate.replace('[NAME_OF_CHILD]', allChildern[i].kidName);
        innerContant = innerContant.replace('[IMG_SRC]', SERVER_URL + '/images/' + allChildern[i].imageLink);
        newChildDiv.innerHTML = innerContant;
        mainDiv.appendChild(newChildDiv);
        
        var spacer = document.createElement("div");
        spacer.className = "spacer";
        spacer.innerHTML = "&nbsp;";
        mainDiv.appendChild(spacer);
    }
    
    fixElementsApperance();
}

function getKidFunction(id,name,img) {
    return function() {
        window.location = SERVER_URL + "/child.html?id=" + id +"&name=" + name + "&img=" + img;
    };
}

UID = getCookie("userid");
if (UID < 0 || UID == "" || !UID) {
    alert("ERROR no UID");
} else {
   getChildren();
    
    $(document).ready(function() {
        $("#my-menu").mmenu();
        document.body.style = "height: " + $(document).height();
        +"px";
        $(window).on('resize', fixElementsApperance);



        function fixElementsApperance() {
            $(".profile_picture").width($(".profile_picture").height());
            $(".burger_btn").width($(".burger_btn").height() * 1.91);
            $(".burger_btn").css("margin-top", "-" + ($(".burger_btn").height() / 2) + "px");
            $(".profile_picture").css("margin-top", "-" + ($(".profile_picture").height() / 2) + "px");
            $(".profile_name").css("margin-top", "-" + ($(".profile_name").height() / 2) + "px");
            $(".profile_name").css("right", (Math.ceil($(".profile_picture").width() * 1.7)) + "px");

        }

        fixElementsApperance();


    });

}


