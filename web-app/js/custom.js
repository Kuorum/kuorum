$(document).ajaxStop(function () {
    $("time.timeago").timeago();
    $('.kakareo > .link-wrapper').on({
        mouseenter: function () {
            $(this).prev('.from').find('.inside').css('border-bottom', '8px solid #efefef');
        },
        mouseleave: function () {
            $(this).prev('.from').find('.inside').css('border-bottom', '8px solid #fafafa');
        }
    });
})

$(document).ready(function() {

	$("time.timeago").timeago();

	// load more
	$(function(){
            $("a.loadMore").on("click", function(e){loadMore(e, this)})
        })

        function loadMore(e, that){
		    e.preventDefault()
		    var link = $(that)
		    var url = link.attr('href')
		    var parentId = link.attr('data-parent-id')
            var offset = link.attr('data-offset') || 10
		    var loadingId = parentId+"-loading"
		    var parent = $("#"+parentId)
		    parent.append('<span id="'+loadingId+'">LOADING</span>')
		    $.ajax( {
		        url:url,
                data:"offset="+offset,
		        statusCode: {
		            401: function() {
		                location.reload();
		            }
		        }
		    })
		        .done(function(data, status, xhr) {
		            parent.append(data)
                    var moreResults = xhr.getResponseHeader('moreResults')
                    link.attr('data-offset', offset +10)
                    if (moreResults){
                        link.remove()
                    }
		        })
		        .fail(function(data) {
		            console.log(data)
		        })

		        .always(function(data) {
		            $("#"+loadingId).remove()
                    $("time.timeago").timeago();
		        });

		}

    // el hover sobre el kakareo que afecte al triángulo superior

	$('.kakareo > .link-wrapper').on({
	    mouseenter: function () {
	        $(this).prev('.from').find('.inside').css('border-bottom', '8px solid #efefef');
	    },
	    mouseleave: function () {
	        $(this).prev('.from').find('.inside').css('border-bottom', '8px solid #fafafa');
	    }
	});

	$('.important .kakareo > .link-wrapper').on({
	    mouseenter: function () {
	        $(this).prev('.from').find('.inside').css('border-bottom', '8px solid #feedce');
	    },
	    mouseleave: function () {
	        $(this).prev('.from').find('.inside').css('border-bottom', '8px solid #fff8ed');
	    }
	});

  	// al hacer clic en los badges vacía el contenido para que desaparezca
	$(function() {

		var triggers = $('.badge').closest('a');

	    triggers.click(function() {
	        $(this).find('.badge').empty();
	        $(this).next('ul').find('li.new').removeClass('new');
            var url = $(this).attr("href")
			$.ajax(url);
	    });

	});


	// deshabilitar links kakareo, impulsar, leer después
    $('body').on('click','.kakareo-number a, .like-number a', function(e) {
		e.preventDefault();
        e.stopPropagation();
        if (!$(this).hasClass('disabled')){
            var url = $(this).attr("href")
            var postId = $(this).parents("article").first().attr("data-cluck-postId")
            var cssClass = $(this).parent().hasClass("kakareo-number")?"kakareo-number":"like-number"
            $.ajax(url).done(function(data){
                console.log("article[data-cluck-postId='"+postId+"'] li."+cssClass+" a")
                $("article[data-cluck-postId='"+postId+"'] li."+cssClass+" a").addClass('disabled')
                $("article[data-cluck-postId='"+postId+"'] li."+cssClass+" .counter").each(function(idx, element){
                    var numKakareos = parseInt($(element).text()) +1
                    $(element).text(numKakareos)
                })
            })
        }
	});

    $('body').on('click', '.read-later a', function(e) {
        e.preventDefault();
        var url = $(this).attr("href")
        var postId = $(this).parents("article").first().attr("data-cluck-postId")
        $.ajax(url).done(function(data, status, xhr){
            var isFavorite = xhr.getResponseHeader('isFavorite')
            var numFavorites = xhr.getResponseHeader('numList')
            $(".pending h1 .badge").text(numFavorites)
            if (isFavorite == "true"){
                $("article[data-cluck-postId='"+postId+"'] li.read-later a").addClass("disabled")
                $("article[data-cluck-postId='"+postId+"'] li.read-later a").removeClass("enabled")
                $("section.boxes.guay.pending ul.kakareo-list").prepend(data)
            }else{
                $("article[data-cluck-postId='"+postId+"'] li.read-later a").removeClass("disabled")
                $("article[data-cluck-postId='"+postId+"'] li.read-later a").addClass("enabled")
                $("section.boxes.guay.pending article[data-cluck-postId='"+postId+"']").parent().remove()
            }
        })

	});

    $('a.postpone-alert').click(function(e){
        e.preventDefault()
        var url = $(this).attr("href")
        var alertToRemove = $(this).parents("li.profile-alert")
        $.ajax(url).done(function(data, status, xhr){
            var numAlerts = parseInt($(".alerts h1 .badge").text())-1
            alertToRemove.remove()
            $(".alerts h1 .badge").text(numAlerts>0?numAlerts:'')

        })
    })

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

});
