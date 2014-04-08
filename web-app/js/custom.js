$(document).ready(function() {

	// plugin timeago.es para el formato de las fechas
  	$("time.timeago").timeago();

	// el hover sobre el kakareo que afecte al triángulo superior
	$('.kakareo > .link-wrapper').hover(function() {
		$(this).prev('.from').find('.inside').css('border-bottom', '8px solid #efefef');
	}, function() {
		$(this).prev('.from').find('.inside').css('border-bottom', '8px solid #fafafa');
	});

	$('.important .kakareo > .link-wrapper').hover(function() {
		$(this).prev('.from').find('.inside').css('border-bottom', '8px solid #feedce');
	}, function() {
		$(this).prev('.from').find('.inside').css('border-bottom', '8px solid #fff8ed');
	});

	// deshabilitar links kakareo, impulsar, leer después
	$('.kakareo-number a, .like-number a, .read-later a').click( function() {
		$(this).addClass('disabled').attr('href', '');
		return false;
	});

	// hacer clic en player falso del video (.front)
	$('.front').click( function() {
		$(this).next('.youtube').css('display', 'block');
		$(this).remove();
		return false;
	});

  	// cambia el placeholder según el filtro elegido
	$(function() {

		var $ui = $('#search-form');
		$ui.find('#filters li a').bind('focus click',function(){
			var filtro = $(this).html();
			$ui.find('#srch-term').attr('placeholder', filtro);
		});

	});

	// hacer un bloque clicable y que tome
	// que es su primer elemento la url del enlace a.hidden
	$(function() {

		$('.link-wrapper').click( function() {
			$(this).removeClass('new');
		    window.location = $(this).find('a.hidden').attr('href');
		});

	});

});