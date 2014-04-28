// inicializa los popover
$(document).popover({
	selector: '[data-toggle="popover"]',
	html: true,
	placement: 'bottom',
	content: function() {
		return $(this).next('.popover').html();
	}
});



// inicializa los tooltip
$(document).tooltip({
	selector: '[rel="tooltip"]'
});


$(document).ready(function() {


	// scroll suave a hashtag
    $(".smooth").click(function (event) {
    	event.preventDefault();
    	// calcular el destino
    	var dest = 0;
    	if ($(this.hash).offset().top > $(document).height() - $(window).height()) {
    		dest = $(document).height() - $(window).height();
    	} else {
    		dest = $(this.hash).offset().top - 64;
    	}
    	// ir al destino
    	$('html,body').animate({
    		scrollTop: dest
    	}, 600, 'swing');
    });


	// slider y su contador
	$(function() {
		$('.carousel').carousel('pause');
		$('.carousel-inner .item').first().addClass('active');

		var myCarousel = $(".carousel");
		var indicators = $(".carousel-index");

		var total = myCarousel.find(".carousel-inner").children(".item").length;
		var actual = $('.carousel .active').index('.carousel .item') + 1;
		$('.actual').html(actual);
		$('.total').html(total);

		$(".carousel").on('slid', function(e){
			var actual = $(".active", e.target).index() + 1;
			$('.actual').html(actual);
		});
	});


	// touch swipe para el slider
	$(".carousel-inner").swipe( {
		//Generic swipe handler for all directions
		swipeLeft:function(event, direction, distance, duration, fingerCount) {
			$(this).parent().carousel('next');
		},
		swipeRight: function() {
			$(this).parent().carousel('prev');
		},
		//Default is 75px, set to 0 for demo so any distance triggers swipe
		threshold:0
	});


	// inicia el timeago
	$('time.timeago').timeago();


	// animo la progress-bar de boxes.likes
	$('.likes .progress-bar').progressbar({
		done: function() {
	    		var posTooltip = $('.progress-bar').width();
	    		console.log(posTooltip);
                $('#m-callback-done').css('left', posTooltip).css('opacity', '1');
            }
	});


	//tooltip visible sobre la progress bar
	$('.progress-bar').tooltip({trigger: 'manual', placement: 'top'}).tooltip('show');


	// apertura de karma
	function openKarma () {
		$('#karma').fadeIn().addClass('in');
	}
	$('body').on('click', '#karma .close', function() {
		$('#karma').css('display','none').removeClass('in');
 	});


	// al hacer clic en los badges vacía el contenido para que desaparezca
	$(function() {
		$('.badge').closest('a').click(function() {

			$(this).find('.badge').delay(2000).fadeOut("slow").queue(function() {
				$(this).empty();
			});
			$(this).next('ul').find('li.new').delay(2000).queue(function() {
				$(this).removeClass('new');
			});

			var url = $(this).attr("href")
			$.ajax(url);
		});
	});


	// links kakareo, impulsar
	$('.action.cluck, .action.drive').click( function(e) {
		e.preventDefault();
        e.stopPropagation();
        if (!$(this).hasClass('disabled')){
            var url = $(this).attr("href");
            var postId = $(this).parents("article").first().attr("data-cluck-postId");
            var cssClass = $(this).parent().hasClass("kakareo-number")?"kakareo-number":"like-number";
            $.ajax(url).done(function(data){
                console.log("article[data-cluck-postId='"+postId+"'] li."+cssClass+" .action");
                $("article[data-cluck-postId='"+postId+"'] li."+cssClass+" .action").addClass('disabled');
                $("article[data-cluck-postId='"+postId+"'] li."+cssClass+" .counter").each(function(idx, element){
                    var numKakareos = parseInt($(element).text()) +1;
                    $(element).text(numKakareos);
                });
            });
        }
	});


	// leer después
	$('body').on('click', '.read-later a', function(e) {
        e.preventDefault();
        var url = $(this).attr("href");
        var postId = $(this).parents("article").first().attr("data-cluck-postId");
        $.ajax(url).done(function(data, status, xhr){
            var isFavorite = xhr.getResponseHeader('isFavorite');
             var numFavorites = xhr.getResponseHeader('numList');
            $(".pending h1 .badge").text(numFavorites);
            if (isFavorite == "true"){
                $("article[data-cluck-postId='"+postId+"'] li.read-later a").addClass("disabled");
                $("article[data-cluck-postId='"+postId+"'] li.read-later a").removeClass("enabled");
                $("section.boxes.guay.pending ul.kakareo-list").prepend(data);
            }else{
                $("article[data-cluck-postId='"+postId+"'] li.read-later a").removeClass("disabled");
                $("article[data-cluck-postId='"+postId+"'] li.read-later a").addClass("enabled");
                $("section.boxes.guay.pending article[data-cluck-postId='"+postId+"']").parent().remove();
            }
        });

	});


	// Cambio de flechita en el botón desplegar texto de la ley
	$('body').on("click", ".readMore a", function(e) {

		if ( $(this).hasClass('collapsed') ) {
			$(this).html('Ocultar texto<span class="fa fa-chevron-circle-up fa-lg"></span>');
		} else {
			$(this).html('Leer más<span class="fa fa-chevron-circle-down fa-lg"></span>');
		}
	});


	// Habilitar/deshabilitar link "Marcar como inapropiado"
	$('body').on("click", ".mark a", function(e) {
		e.preventDefault();
		if ( $(this).hasClass('disabled') ){
			$(this).removeClass('disabled');
		} else {
			$(this).addClass('disabled');
		}
	});

	// botón Seguir de las cajas popover y página ley-participantes
	$(document).on({
	    mouseenter: function () {
	        $(this).html('Seguir <span class="fa fa-check-circle"></span>');
	    },
	    mouseleave: function () {
	        $(this).html('Seguir');
	    }
	}, "#follow.enabled"); //pass the element as an argument to .on

	$(document).on({
	    mouseenter: function () {
	        $(this).html('No seguir');
	    },
	    mouseleave: function () {
	        $(this).html('Siguiendo <span class="fa fa-check-circle"></span>');
	    }
	}, "#follow.disabled"); //pass the element as an argument to .on

	$('body').on("click", "#follow", function() {
		if ( $(this).hasClass('disabled') ){
			$(this).html('Seguir <span class="fa fa-check-circle"></span>').removeClass('disabled').addClass('enabled');
		} else {
			$(this).html('Siguiendo <span class="fa fa-check-circle"></span>').removeClass('enabled').addClass('disabled');
		}
	});


	// Deshabilitar botón Votar (ley no logado)
	$('body').on("click", ".voting .vote", function(e) {
		e.preventDefault();
		$(this).text('Votación cerrada').addClass('disabled');
	});


	// Deshabilitar botón Impulsar (Post)
	$('body').on("click", "#drive .btn", function(e) {
		e.preventDefault();
		$(this).html('Ya la has impulsado <br><small>es tu momento de hablar</small>').addClass('disabled');
	});


	// Si voto desaparecen los botones y aparece el enlace de cambio de opinión
	$('body').on("click", ".voting li a", function(e) {
		e.preventDefault();
		$('.voting ul').css('display', 'none');
		$('.changeOpinion').css('display', 'block');
        $.ajax( {
            url:$(this).attr("href"),
            statusCode: {
                401: function() {
                    display.info("Estás deslogado")
                    setTimeout('location.reload()',5000);
                }
            }
        }).done(function(data, status, xhr) {
            $('ul.activity li').removeClass("active")
            $('ul.activity li.'+data.voteType).addClass("active")
            $('ul.activity li.POSITIVE span').html(data.votes.yes)
            $('ul.activity li.NEGATIVE span').html(data.votes.no)
            $('ul.activity li.ABSTENTION span').html(data.votes.abs)
            $('.kuorum span.counter').html(data.necessaryVotesForKuorum)
        })
	});

	$('body').on("click", ".voting li .yes", function(e) {
		$('.activity .POSITIVE').addClass('active');
	});
	$('body').on("click", ".voting li .no", function(e) {
		$('.activity .NEGATIVE').addClass('active');
	});
	$('body').on("click", ".voting li .neutral", function(e) {
		$('.activity .ABSTENTION').addClass('active');
	});


	// Si hago click en cambio de opinión vuelven los botones
	$('body').on("click", ".changeOpinion", function(e) {
		e.preventDefault();
		$('.activity li').removeClass('active');
		$(this).css('display', 'none');
		$('.voting ul').css('display', 'block');
	});


	// hacer clic en player falso del video (.front)
	$('.front').click( function() {
		$(this).next('.youtube').css('display', 'block');
		$(this).remove();
		return false;
	});


	// Buscador: cambia el placeholder según el filtro elegido
	$(function() {

		var $ui = $('#search-form');
		$ui.find('#filters li a').bind('focus click',function(){
			var filtro = $(this).html();
			$ui.find('#srch-term').attr('placeholder', filtro);
		});

	});


	// el enlace callMobile (visible sólo en pantallas de hasta 767px, desaparece si le haces click o si llegas a la votación
	if ( $('#vote').length > 0 ) {

		$(function() {
			var callMobile = $('.callMobile');
			var eTop = $('#vote').offset().top;
			var realPos = eTop - 80;
			$(window).scroll(function() {
				var scroll = $(window).scrollTop();
				if (scroll >= realPos) {
					callMobile.fadeOut('slow');
				} else {
					callMobile.fadeIn('slow');
				}
			});
		});

	}

	// si boxes lleva foto pongo padding superior
	if ( $('.boxes.noted.likes.important').children('img.actor').length > 0 ) {
		$('.boxes.noted.likes.important').css('padding-top', '275px');

	}

	// hacer un bloque clickable y que tome
	// que es su primer elemento la url del enlace a.hidden
	$(function() {

		$('.link-wrapper').click( function() {
			window.location = $(this).find('a.hidden').attr('href');
		});

	});

	// oculta los comentarios
	$('.listComments > li:gt(2)').hide();
	$('#ver-mas a').click(function(e) {
		e.preventDefault();
		$('.listComments > li:gt(2)').fadeIn('slow');
		$('#ver-mas').remove();
	});



	// change text when select option in the edit post form
	$('#updateText').text($('#typePubli li.active').text());
	$('#selectType').change(function(){
        	$('#updateText').text($('#typePubli li').eq(this.selectedIndex).text());
    });


	// countdown textarea
	$(function() {
		var totalChars      = parseInt($('#charInit span').text());
		var countTextBox    = $('.counted'); // Textarea input box
		var charsCountEl    = $('#charNum span'); // Remaining chars count will be displayed here

		if (countTextBox.length> 0){
			charsCountEl.text(totalChars - countTextBox.val().length); //initial value of countchars element
        }
		countTextBox.keyup(function() { //user releases a key on the keyboard

			var thisChars = this.value.replace(/{.*}/g, '').length; //get chars count in textarea

			if (thisChars > totalChars) //if we have more chars than it should be
			{
				var CharsToDel = (thisChars-totalChars); // total extra chars to delete
				this.value = this.value.substring(0,this.value.length-CharsToDel); //remove excess chars from textarea
			} else {
				charsCountEl.text( totalChars - thisChars ); //count remaining chars
			}
		});
	});

	// textarea editor
	$(".texteditor").jqte({
		br: true,
		center: false,
		color: false,
		format: false,
		indent: false,
		left: false,
		ol: false,
		outdent: false,
		p: false,
		placeholder: "Escribe un texto que describa tu publicación",
		linktypes: ["URL", "Email"],
		remove: false,
		right: false,
		rule: false,
		source: false,
		sub: false,
		strike: false,
		sup: false,
		ul: false,
		unlink: false,
		fsize: false,
		title: false
	});


	// hacer visible la contraseña
	$('#show-pass').attr('checked', false);

	$('#show-pass').click(function(){

		if ($(this).hasClass('checked')) {
			$(this).removeClass('checked');
		} else {
			$(this).addClass('checked');
		}

	    name = $('#password').attr('name');
	    value = $('#password').val();

	    if($(this).hasClass('checked')) {
	    	html = '<input type="text" name="'+ name + '" value="' + value + '" id="password" class="form-control input-lg">';
	        $('#password').after(html).remove();
	    } else {
	    	html = '<input type="password" name="'+ name + '" value="' + value + '" id="password" class="form-control input-lg">';
	    	$('#password').after(html).remove();
	    }
	});


	// load more
	$("a.loadMore").on("click", function(e){loadMore(e, this)})
	function loadMore(e, that) {

		e.preventDefault()
		var link = $(that)
		var url = link.attr('href')
		var parentId = link.attr('data-parent-id')
		var offset = link.attr('data-offset') || 10
		var loadingId = parentId+"-loading"
		var parent = $("#"+parentId)
		parent.append('<div class="loading" id="'+loadingId+'"><span class="sr-only">Cargando...</span></div>')
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


	// para los checkbox del formulario de registro
	var texts= {
        0: i18n.customRegister.step4.form.submit.description0,
        1: i18n.customRegister.step4.form.submit.description1,
        2: i18n.customRegister.step4.form.submit.description2,
        ok:i18n.customRegister.step4.form.submit.descriptionOk
    }

    function changeDescriptionNumSelect(){
        var numChecked = $("#sign4 input[type=checkbox]:checked").length
        if (numChecked < 3){
            $("#descNumSelect").html(texts[numChecked])
            $("#sign4 input[type=submit]").addClass('disabled')
        }else{
            $("#descNumSelect").html(texts['ok'])
            $("#sign4 input[type=submit]").removeClass('disabled')
        }
    }
	// seleccionar todos los checkbox
	$(function () {
        changeDescriptionNumSelect()
	    var checkAll = $('#selectAll');
	    var checkboxes = $('input.check');

	    $('input.check').each(function(){
		    var self = $(this),
		    label = self.next(),
		    label_text = label.html();
		    label.remove();
		    self.iCheck({
		      checkboxClass: 'icheckbox_line-orange',
		      radioClass: 'iradio_line-orange',
		      inheritID: true,
		      aria: true,
		      insert:  label_text
		    });
		});

	    $('#selectAll').change(function() {
		    if($(this).is(':checked')) {
		        checkboxes.iCheck('check');
		        $('#others').attr('checked', true);
		    } else {
		        checkboxes.iCheck('uncheck');
		        $('#others').attr('checked', false);
		    }
		});


	    checkAll.on('ifChecked ifUnchecked', function(event) {
	        if (event.type == 'ifChecked') {
	            checkboxes.iCheck('check');
	        } else {
	            checkboxes.iCheck('uncheck');
	        }
	    });

	    checkboxes.on('ifChanged', function(event){
	        if(checkboxes.filter(':checked').length == checkboxes.length) {
	            checkAll.prop('checked', 'checked');
	        } else {
	            checkAll.removeProp('checked');
	        }
	        checkAll.iCheck('update');
            changeDescriptionNumSelect();
	    });
	});

	// añade la flechita al span de los mensajes de error de los formularios
	$('span.error').prepend('<span class="tooltip-arrow"></span>');

	// le da la clase error al falso textarea
	$(function () {
		if ( $('#textPost').hasClass('error') ) {
				$('#textPost').closest('.jqte').addClass('error');
			}
	});

	// inicializa el scroll dentro del popover
	$('.popover-trigger.more-users').on('shown.bs.popover', function () {

		$(this).next('.popover').find($('.scroll')).slimScroll({
			size: '10px',
			height: '145px',
			distance: '0',
			railVisible: true,
			alwaysVisible: true,
			disableFadeOut: true
		});

	})

});


// funciones que llaman a las diferentes notificacones (salen en la parte superior de la pantalla)
	var display = {
    error:function(text){this._notyGeneric(text, "error")},
    success:function(text){this._notyGeneric(text, "success")},
    info:function(text){this._notyGeneric(text, "information")},
    warn:function(text){this._notyGeneric(text, "warning")},

    _notyGeneric:function(text, type) {
        var nW = noty({
            layout: 'top',
            dismissQueue: true,
            animation: {
                open: {height: 'toggle'},
                close: {height: 'toggle'},
                easing: 'swing',
                speed: 500 // opening & closing animation speed
            },
            template: '<div class="noty_message" role="alert"><span class="noty_text"></span><div class="noty_close"></div></div>',
            type: type,
            text: text
        });
    }
}


// el hover sobre el kakareo que afecte al triángulo superior
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

	$('.important .kakareo > .link-wrapper').on({
	    mouseenter: function () {
	        $(this).prev('.from').find('.inside').css('border-bottom', '8px solid #feedce');
	    },
	    mouseleave: function () {
	        $(this).prev('.from').find('.inside').css('border-bottom', '8px solid #fff8ed');
	    }
	});

});