$(document).ready(function() {

	// plugin timeago.es para el formato de las fechas
  	$("time.timeago").timeago();


  	// al hacer clic en los badges vacía el contenido para que desaparezca
	$(function() {

		var triggers = $('.badge').closest('a');

	    triggers.click(function() {
	        $(this).find('.badge').empty();
	        $(this).next('ul').find('li.new').removeClass('new');

	        // llamada a Ajax
			$.ajax({
				type: 'POST',
				url: 'loquesea.url',
				data: 'variable=Valor&variable2=Valor2'
			});


	    });

	});


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
	$('.kakareo-number a, .like-number a').click( function(e) {
		e.preventDefault();
		$(this).addClass('disabled').attr('href', '');
	});
	$('.read-later a').click( function(e) {
        e.preventDefault();
        $(this).toggleClass('enabled disabled');
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
		    window.location = $(this).find('a.hidden').attr('href');
		});

	});

    $("a.loadMore").on("click", function(e){loadMore(e, this)})
});


function loadMore(e, that){
    e.preventDefault()
    var link = $(that)
    var url = link.attr('href')
    var parentId = link.attr('parent')
    var loadingId = parentId+"-loading"
    var parent = $("#"+parentId)
    parent.append('<span id="'+loadingId+'">LOADING</span>')
    $.ajax( {
        url:url,
        statusCode: {
            401: function() {
                location.reload();
            }
        }
    })
        .done(function(data) {
            parent.append(data)
        })
        .fail(function(data) {
            console.log(data)
        })

        .always(function(data) {
            $("#"+loadingId).remove()
        });

}