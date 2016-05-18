(function () {
	var urlTypes={
		'ratePolitician':'${createLink(mapping: 'widgetRatePolitician', absolute: true)}',
		'comparative':'${createLink(mapping: 'widgetComparative', absolute: true)}'
	}
	var widget = document.getElementById('${divId}');
	var type = widget.getAttribute("data-type")
	var ancho = widget.getAttribute("data-width")
	var alto = widget.getAttribute("data-height")
	var lang = widget.getAttribute("data-lang")
	var url = urlTypes[type]+ "?"

	if (lang != undefined){
		url += "lang="+lang+"&"
	}
	switch (type) {
	  case "ratePolitician":
	  case "comparative":
	    var aliases = widget.getAttribute("data-userAlias").split(",")
	    for (i = 0; i < aliases.length; i++) {
	    	url = url + 'userAlias='+aliases[i] +'&'
		}
	  	break;
	  default:
    }

	var colorBorde = "1px solid #ddd";

	if (widget) {
		widget.style.cssText = 'border:' + colorBorde + '; width:' + ancho + '; height:' + alto + '; overflow:hidden;';
		widget.innerHTML     = '<iframe src="'+url+'" frameborder="0" scrolling="no" width="100%" height="100%" allowTransparency="true" style="overflow: hidden;"></iframe>';
	}
})();