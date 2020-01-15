
console.log("KUORUM WIDGET LOAD");


var KuorumWidgetHelper = {
    widgetContainer: null,
    saveHistory:true,
    domain: "",
    iframeResizerURL: null,

    startKuorum: function(widgetContainerId, urlHome, iframeResizerURL){
        var widgetContainer = document.getElementById(widgetContainerId);
        KuorumWidgetHelper.iframeResizerURL = iframeResizerURL;
        KuorumWidgetHelper.domain=KuorumWidgetHelper.extractHostName(urlHome);

        var initKuorumUrl = widgetContainer.getAttribute("data-kuorum-url");
        var ancho = widgetContainer.getAttribute("data-width");
        var alto = widgetContainer.getAttribute("data-height");

        if (initKuorumUrl == undefined || KuorumWidgetHelper.extractHostName(initKuorumUrl) != KuorumWidgetHelper.domain){
            initKuorumUrl = urlHome;
        }

        if (window.location.hash != "" && KuorumWidgetHelper.extractHostName(window.location.hash) == KuorumWidgetHelper.domain){
            initKuorumUrl = window.location.hash.substr(1);
        }

        KuorumWidgetHelper.widgetContainer = widgetContainer;
        KuorumWidgetHelper.buildIframe(initKuorumUrl, ancho, alto);
    },

    buildIframe:function(initKuorumUrl, ancho, alto){
        if (KuorumWidgetHelper.widgetContainer) {

            var widgetId = 'kuorum_'+(new Date().getTime());
            KuorumWidgetHelper.widgetContainer.style.cssText = 'width:' + ancho + '; height:' + alto + '; overflow:hidden;';
            KuorumWidgetHelper.widgetContainer.innerHTML     = '<iframe id="'+widgetId+'" src="'+initKuorumUrl+'" frameborder="0" scrolling="no" width="100%" height="100%" allowTransparency="true" style="overflow: hidden;">></iframe>';
            document.getElementById(widgetId).onload =  function(){
            // TODO: IE8
                kuorumLoadScript(KuorumWidgetHelper.iframeResizerURL, function(){
                    iFrameResize({
                        log:false,
                        messageCallback: function(messageData){ // Callback fn when message is received
                            var scrollPosition = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop || 0;
                            document.getElementById(widgetId).iFrameResizer.sendMessage(scrollPosition);
                        }
                    }, '#'+widgetId);
                });
            }

            // Create IE + others compatible event handler
            var eventMethod = window.addEventListener ? "addEventListener" : "attachEvent";
            var eventer = window[eventMethod];
            var messageEvent = eventMethod == "attachEvent" ? "onmessage" : "message";

            // Listen to message from child window
            eventer(messageEvent,function(e) {
                try{
                    var eventData = JSON.parse(e.data);
                    window["KuorumWidgetHelper"]["callbackMessages"][eventData.action](eventData);
                }catch(e){

                }
            },false);
        }
    },
    extractHostName:function (url) {
        var hostname;
        //find & remove protocol (http, ftp, etc.) and get hostname

        if (url.indexOf("//") > -1) {
            hostname = url.split('/')[2];
        }
        else {
            hostname = url.split('/')[0];
        }

        //find & remove port number
        hostname = hostname.split(':')[0];
        //find & remove "?"
        hostname = hostname.split('?')[0];

        return hostname;
    },
    callbackMessages:{
        loadPage: function(data){
            if (KuorumWidgetHelper.saveHistory){
                history.replaceState(data, data.title, "#"+data.url)
                document.getElementsByTagName("head")[0].getElementsByTagName("title")[0].innerHTML=data.title;
            }
            KuorumWidgetHelper.saveHistory=true;
        }
    }
};
