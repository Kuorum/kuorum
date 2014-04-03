$(document).ready(function() {

  	jQuery("time.timeago").timeago();

	$(function() {

		var $ui = $('#search-form');

		$ui.find('#filters li a').bind('focus click',function(){
			var filtro = $(this).html();
			$ui.find('#srch-term').attr("placeholder", filtro);
		});

	});

	$(function() {

		// $('.badge').each(function() {
		//     if ($(this).text() == "") {
		//     	$(this).addClass('hidden')
		//     }
		// });

		var triggers = $('.badge').closest('a');
	    triggers.click(function() {
	        $(this).find('.badge').empty();
	    });

	});




});


$(document).popover({
        selector: '.popover-user-tigger[rel=popover]',
        html: true,
        placement: 'bottom',
        content: function() {
            return $('.popover-user').html()
        ;}
    });

$(document).tooltip({
    selector: '[rel="tooltip"]'
});