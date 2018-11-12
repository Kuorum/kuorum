<%@ page import="kuorum.web.widget.AverageWidgetType; org.kuorum.rest.model.kuorumUser.reputation.UserReputationEvolutionRSDTO" %>
(function () {
	var urlTypes={
		'ratePolitician':'${raw(createLink(mapping: 'widgetRatePolitician', absolute: true))}',
		'debate':'${raw(createLink(mapping: 'debateShow', absolute: true, params:[userAlias:'-userAlias-',urlTitle:'debate', campaignId:'-campaignId-']))}'
	}
	var widget = document.getElementById('${divId}');
	var type = widget.getAttribute("data-type")
	var ancho = widget.getAttribute("data-width")
	var alto = widget.getAttribute("data-height")
	var lang = widget.getAttribute("data-lang")
	var url = urlTypes[type]
	var widgetId = 'kuorum_'+type+"_"+(new Date().getTime());
	console.log(url)
	if (url.indexOf("?")>=0){
		url += "&"
	}else{
		url += "?"
	}
	// Style
	var style = {
	    bodyBackground:widget.getAttribute("data-style-body-background"),
        headBackground:widget.getAttribute("data-style-head-background"),
	    titleFontFamily:widget.getAttribute("data-style-title-fontFamily"),
	    titleColor:widget.getAttribute("data-style-title-color"),
	    borderColor:widget.getAttribute("data-style-border-color"),
	    valuationHover:widget.getAttribute("data-style-valuationHover"),
    }
    var customCss = "";
    if (style.bodyBackground !=undefined) customCss += "body.widget{background:"+style.bodyBackground+"; }\n";
    if (style.headBackground !=undefined) customCss += "section.widget.dark header, section.widget header{background:"+style.headBackground+"; }\n";
    if (style.titleFontFamily !=undefined) customCss += "section.widget header > h1 {font-family:"+style.titleFontFamily+"; }\n";
    if (style.titleColor !=undefined) customCss += "section.widget header > h1 {color:"+style.titleColor+"; }\n";
    if (style.borderColor !=undefined){customCss += "#results-widget-content section, body.widget > header, section.widget .user-list-followers > li {border-color:"+style.borderColor+"; }\n";}
    if (style.valuationHover !=undefined){customCss += "section.widget .user-list-followers > li:hover, section.widget .user-list-followers > li:focus {background-color:"+style.valuationHover+"; }\n";}

	if (lang != undefined){
		url += "lang="+lang+"&"
	}
	if (customCss != ""){
	    url += "customCss="+encodeURI(customCss).replace(/#/g, '%23')+"&";
	}

	var aliases = widget.getAttribute("data-userAlias").split(",")
	if(aliases.length > 1){
        for (i = 0; i < aliases.length; i++) {
            url = url + 'userAlias='+aliases[i] +'&'
        }
	}else if (aliases.length == 1){
		url = url.replace('-userAlias-',aliases);
	}
	console.log(url)

	var debateId = widget.getAttribute("data-debateId");
	url = url.replace("-campaignId-",debateId)
	url = url +'&printAsWidget=true'

    var borderColor = "#ddd"
    if (style.borderColor !=undefined) {borderColor=style.borderColor}
	var colorBorde = "1px solid "+borderColor;

	if (widget) {
		widget.style.cssText = 'border:' + colorBorde + '; width:' + ancho + '; height:' + alto + '; overflow:hidden; top: 20px';
		widget.innerHTML     = '<iframe id="'+widgetId+'" src="'+url+'" frameborder="0" scrolling="no" width="100%" height="100%" allowTransparency="true" style="overflow: hidden;"></iframe>';
		document.getElementById(widgetId).onload =  function(){
			%{--// TODO: IE8--}%
			var iframeResizerURL = "${g.resource(dir: '/js/widget/iframe-resizer', file: 'iframeResizer.min.js', absolute: true)}"
			loadScript(iframeResizerURL, function(){
				iFrameResize({
					log:false,
					messageCallback: function(messageData){ // Callback fn when message is received
						var scrollPosition = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop || 0
						document.getElementById(widgetId).iFrameResizer.sendMessage(scrollPosition);
					}
				}, '#'+widgetId);
			})
		}
	}
})();


function loadScript(src, callback)
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