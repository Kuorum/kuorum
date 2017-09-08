var iFrameResizer = {
    messageCallback: function(message){
        //alert(message,window.parentIFrame.getId());
        $('#registro').css('top', message);
    }
}

$(function(){
    console.log($('#registro'))
    $('#registro').on('show.bs.modal', function (e) {
        //if (window.top.document.querySelector('iframe')) {
        //    $('#registro').css('top', window.top.scrollY); //set modal position
        //}
        window.parentIFrame.sendMessage('Requesting height of scroll');
        //var y = $('#registro').data('y'); // gets the mouseY position
        //$('#registro').css('top', y);
    });
})