var iFrameResizer = {
    messageCallback: function(message){
        //alert(message,window.parentIFrame.getId());
        $('#registro').css('top', message);
        $("#debateDeleteConfirm").css('top', message);
    }
}

var iframeHelper = {
    inIframe: function () {
        try {
            return window.self !== window.top;
        } catch (e) {
            return true;
        }
    },

    loadIframeMode: function(){
        iframeHelper.prepareIframeView();
        iframeHelper.prepareLoadPageEvent();
        iframeHelper.prepareUnloadPageEvetn();
        iframeHelper.prepareEventsModalLogin();
    },

    prepareIframeView: function(){
        $("body").addClass("widget");
    },

    prepareLoadPageEvent: function(){
        data = {
            action: 'loadPage',
            title: document.getElementsByTagName("title")[0].innerHTML,
            url:document.location.href
        }
        parent.postMessage(JSON.stringify(data),'*');
    },

    prepareUnloadPageEvetn: function(){
        window.addEventListener('beforeunload', function(event) {
            pageLoadingOn("Unload")
        });
    },
    prepareEventsModalLogin:function(){
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
    }
}

$(function(){
    if (iframeHelper.inIframe()) {
        iframeHelper.loadIframeMode();
    }
})