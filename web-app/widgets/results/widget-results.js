(function () {
	var widget = document.getElementById('results-widget');
	var ancho = "900px";
	var alto  = "900px";

	if (widget) {
		widget.style.cssText = 'width:' + ancho + '; height:' + alto + '; overflow:hidden;';
		widget.innerHTML     = '<iframe src="../../results-widget.html" frameborder="0" scrolling="yes" width="100%" height="100%" allowTransparency="true" style="overflow: hidden;"></iframe>';
	}
})();