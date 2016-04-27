<g:set var="widgetUrl" value="${createLink(mapping: 'widget', absolute: true, params: params)}"/>

(function () {
	var widget = document.getElementById('valuation_widget');	
	var ancho = "300px";
	var alto  = "400px";	
	var colorBorde = "1px solid #ddd";

	if (widget) {
		widget.style.cssText = 'border:' + colorBorde + '; width:' + ancho + '; height:' + alto + '; overflow:hidden;';
		widget.innerHTML     = '<iframe src="${widgetUrl}" frameborder="0" scrolling="yes" width="100%" height="100%" allowTransparency="true" style="overflow: hidden;"></iframe>';
	}
})();