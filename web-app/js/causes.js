
$(document).ready(function() {
    // SUPPORT CAUSES SMALL
    $("body").on("click", ".causes-tags .cause-support", function(e){
        e.preventDefault();
        e.stopPropagation();
        var $parent = $(this).parents(".cause");
        if ( $parent.hasClass("noLogged")){
            $('#registro').modal('show');
        }else{
            $a = $(this).find("a");
            causesFunctions.clickSupportCause($a, function(){})
        }
    });

    var heightClose = $(".panel.panel-default.causes .panel-body").outerHeight();
    $(".panel.panel-default.causes .panel-body").css('height', 'auto');
    var heightOpen = $(".panel.panel-default.causes .panel-body").outerHeight();
    $(".panel.panel-default.causes .panel-body").css('height', heightClose);
    if (heightClose > heightOpen){
        $(".panel.panel-default.causes .panel-body").css('height', 'auto');
        $(".panel.panel-default.causes .panel-footer").hide()
    }

    $("body").on("click",".panel.panel-default.causes .panel-footer .open-causes button",function(e){
        e.preventDefault();
        var $panelBody = $(this).parents(".panel.causes").find(".panel-body");
        var currentHeight  =$panelBody.outerHeight();
        console.log("$panelBody heigh="+currentHeight)
        var nextHeigh =0;
        if (currentHeight == heightClose){
            nextHeigh = heightOpen;
            $(this).removeClass("angle-down").addClass("angle-up");
        }else{
            nextHeigh = heightClose;
            $(this).removeClass("angle-up").addClass("angle-down");
        }
        $panelBody.outerHeight(currentHeight).animate({height: nextHeigh}, 1000);
    });


})

var causesFunctions={
    clickSupportCause:function($a, actionAfterSupport){
        causesFunctions.hearBeat(2,  $a.find(".fa"));
        $.get(  $a.attr('href'), function( data ) {
            var citizenSupports = data.cause.citizenSupports;
            var $liCause = $a.parents(".cause");
            $liCause.toggleClass("active");
            $liCause.find(".cause-counter").html(citizenSupports);
            var $pannel = $liCause.parents(".panel.panel-default.causes")
            console.log("Support cause"+!$liCause.hasClass("active") +" -- " + $pannel.hasClass("owner-causes"))
            if (!$liCause.hasClass("active") && $pannel.hasClass("owner-causes")){
                $liCause.animate({width:'toggle'},350);
            }

            relaodAllDynamicDivs();
            actionAfterSupport()
        });
    },

    hearBeat:function(numHeartBeats, $element){
    if (numHeartBeats <0){
        return;
    }
    var back = numHeartBeats % 2 == 0;
    $element.animate(
        {
            'font-size': (back) ? '14px' : '20px',
            'opacity': (back) ? 1 : 0.5
        }, 100, function(){causesFunctions.hearBeat(numHeartBeats -1, $element)});
}
}