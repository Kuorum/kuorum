function kuorumLoadScript (src, callback = function(){}){
	{
        var script, r, t;

        r = false;
        script = document.createElement('script');
        script.type = 'text/javascript';
        script.src = src;
        script.onload = script.onreadystatechange = function() {
            console.log( this.readyState );
            if ( !r && (!this.readyState || this.readyState == 'complete') )
            {
              r = true;
            callback();
            }
        };
        t = document.getElementsByTagName('script')[0];
        t.parentNode.insertBefore(script, t);
	}

}


(function () {

	var loadKuorumScript = "${g.resource(dir: '/js/widget', file: 'kuorum-widget-build-iframe.js', absolute: true)}";
	var iframeResizerURL = "${g.resource(dir: '/js/widget/iframe-resizer', file: 'iframeResizer.min.js', absolute: true)}"
	var urlHome='${raw(createLink(mapping: 'home', absolute: true, params:[]))}';

	kuorumLoadScript(loadKuorumScript, function(){
		KuorumWidgetHelper.startKuorum('${divId}', urlHome, iframeResizerURL);
	});
})();
