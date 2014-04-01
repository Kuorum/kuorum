$(document).ready(function() {

	$(function() {

		var $ui = $('#search-form');

		$ui.find('#filters li a').bind('focus click',function(){
			var filtro = $(this).html();
			$ui.find('#srch-term').attr("placeholder", filtro);
		});



	});
});