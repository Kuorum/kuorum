var iFrameResizer = {
    messageCallback: function(message){
        //alert(message,window.parentIFrame.getId());
        $('#registro').css('top', message);
        $("#debateDeleteConfirm").css('top', message);
    }
}

$(function(){
    $('#registro').on('show.bs.modal', function (e) {
        //if (window.top.document.querySelector('iframe')) {
        //    $('#registro').css('top', window.top.scrollY); //set modal position
        //}
        window.parentIFrame.sendMessage('{action:getScrollPosition}');
        //var y = $('#registro').data('y'); // gets the mouseY position
        //$('#registro').css('top', y);
    });
    $('#debateDeleteConfirm').on('show.bs.modal', function (e) {
        window.parentIFrame.sendMessage('{action:getScrollPosition}');
    })

    $("body.widget .call-to-action-mobile").on("click",function(){
        $("body.widget .call-to-action-mobile").fadeOut("slow");
    })
})