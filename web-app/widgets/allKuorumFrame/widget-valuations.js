(function () {
	var widget = document.getElementById('valuations-widget');
	var ancho = "300px";
	var alto  = "300px";

	if (widget) {
		widget.style.cssText = 'width:' + ancho + '; height:' + alto + '; overflow:hidden;';
		widget.innerHTML     = '<iframe src="../../valuations-widget.html" frameborder="0" scrolling="yes" width="100%" height="100%" allowTransparency="true" style="overflow: hidden;"></iframe>';
	}
})();