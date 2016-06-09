<%@ page import="org.kuorum.rest.model.kuorumUser.reputation.UserReputationEvolutionRSDTO" %>
(function () {
	var urlTypes={
		'ratePolitician':'${createLink(mapping: 'widgetRatePolitician', absolute: true)}',
		'comparative':'${createLink(mapping: 'widgetComparative', absolute: true, params:[interval:org.kuorum.rest.model.kuorumUser.reputation.UserReputationEvolutionRSDTO.Interval.HOUR])}',
		'comparative_15MINS':'${createLink(mapping: 'widgetComparative', absolute: true, params:[interval:org.kuorum.rest.model.kuorumUser.reputation.UserReputationEvolutionRSDTO.Interval.MINUTE_15])}',
		'comparative_5MINS':'${createLink(mapping: 'widgetComparative', absolute: true, params:[interval:org.kuorum.rest.model.kuorumUser.reputation.UserReputationEvolutionRSDTO.Interval.MINUTE_5])}'
	}
	var widget = document.getElementById('${divId}');
	var type = widget.getAttribute("data-type")
	var ancho = widget.getAttribute("data-width")
	var alto = widget.getAttribute("data-height")
	var lang = widget.getAttribute("data-lang")
	var url = urlTypes[type]
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
	for (i = 0; i < aliases.length; i++) {
		url = url + 'userAlias='+aliases[i] +'&'
	}

    var borderColor = "#ddd"
    if (style.borderColor !=undefined) {borderColor=style.borderColor}
	var colorBorde = "1px solid "+borderColor;

	if (widget) {
		widget.style.cssText = 'border:' + colorBorde + '; width:' + ancho + '; height:' + alto + '; overflow:hidden;';
		widget.innerHTML     = '<iframe src="'+url+'" frameborder="0" scrolling="no" width="100%" height="100%" allowTransparency="true" style="overflow: hidden;"></iframe>';
	}
})();