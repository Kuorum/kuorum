(function () {
	var urlTypes={
		'ratePolitician':'${createLink(mapping: 'widgetRatePolitician', absolute: true)}',
		'comparative':'${createLink(mapping: 'widgetComparative', absolute: true)}'
	}
	var widget = document.getElementById('${divId}');
	var type = widget.getAttribute("data-type")
	var ancho = widget.getAttribute("data-width")
	var alto = widget.getAttribute("data-height")
	var url = urlTypes[type]+ "?"

	switch (type) {
	  case "ratePolitician":
		url = url + 'userAlias='+widget.getAttribute("data-userAlias")
		break;
	  case "comparative":
	    var aliases = widget.getAttribute("data-userAliases").split(",")
	    for (i = 0; i < aliases.length; i++) {
	    	url = url + 'userAlias='+aliases[i] +'&'
		}
	  	break;
	  default:
    }

	var colorBorde = "1px solid #ddd";

	if (widget) {
		widget.style.cssText = 'border:' + colorBorde + '; width:' + ancho + '; height:' + alto + '; overflow:hidden;';
		widget.innerHTML     = '<iframe src="'+url+'" frameborder="0" scrolling="yes" width="100%" height="100%" allowTransparency="true" style="overflow: hidden;"></iframe>';
	}
})();