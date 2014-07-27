
var htmlLoading = '<div class="loading xs"><span class="sr-only">Cargando...</span></div>'

// inicializa los popover
function preparePopover(){
    $.fn.extend({
        popoverClosable: function (options) {
            var defaults = {
                html: true,
                placement: 'bottom',
                content: function() {
                    return $(this).next('.popover').html();
                }
            };
            options = $.extend({}, defaults, options);
            var $popover_togglers = this;
            $popover_togglers.popover(options);
            $popover_togglers.on('click', function (e) {
                e.preventDefault();
                $popover_togglers.not(this).popover('hide');
            });
            $('html').on('click', '[data-dismiss="popover"]', function (e) {
                $popover_togglers.popover('hide');
            });
        }
    });

    $(function () {
        $('[data-toggle="popover"]').popoverClosable();
    });
}
preparePopover();
// cierra los popover al hacer click fuera
$('body').on('click', function (e) {
    $('[data-toggle="popover"]').each(function () {
        //the 'is' for buttons that trigger popups
        //the 'has' for icons within a button that triggers a popup
        if (!$(this).is(e.target) && $(this).has(e.target).length === 0 && $('.popover').has(e.target).length === 0) {
            $(this).popover('hide');
        }
    });
});


// inicializa los tooltip
$(document).tooltip({
	selector: '[rel="tooltip"]'
});


$(document).ready(function() {

    // inicializa formato fechas
    $("time.timeago").timeago();

    prepareArrowClucks();

	// slider de las imágenes de fondo en la Home
	$(".home .images").bgswitcher({
		duration: 2000,
	  	images: ['images/home1.jpg', 'images/home2.jpg', "images/home3.jpg"]
	});


	// hacer un bloque clicable y que tome que es su primer elemento la url del enlace a.hidden
	$(function() {

        $('body').on('click','.link-wrapper', function(e) {
            //ÑAAAPAAAAA para que no salte el evento del link-wrapper en los popover
            var target = $(e.target)
            var popover = target.parents(".popover")
            if (!popover.hasClass("popover")){
                window.location = $(this).find('a.hidden').attr('href');
            }
        });

	});

    // popover-trigger dentro del kakareo no lanza el enlace del bloque clicable
    $('.link-wrapper .popover-trigger').click(function(e) {
        e.stopPropagation();
        e.preventDefault();
    });

    // button dentro del popover del kakareo no lanzan el enlace del bloque clicable
//    $('body').on('click', '.link-wrapper .popover button', function(e) {
//        e.preventDefault();
//        e.stopPropagation();
//    });

    //
    $("#search-results .popover-box #follow").on('click', function() {
        // e.preventDefault();
        // e.stopPropagation();
        /*If not stopPropagation -> .link-wrapper is fired -> el bloque anterior ya evita esto para los button dentro de popover*/
        clickedButtonFollow($(this));
    });

	// scroll suave a hashtag
    $(".smooth").click(function (event) {
    	event.preventDefault();
    	// calcular el destino
    	var dest = 0;
    	if ($(this.hash).offset().top > $(document).height() - $(window).height()) {
    		dest = $(document).height() - $(window).height();
    	} else {
    		dest = $(this.hash).offset().top - 68;
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
    setTimeout(prepareProgressBar, 500)
    prepareProgressBar();

	//tooltip visible sobre la progress bar
	$('.progress-bar').tooltip({trigger: 'manual', placement: 'top'}).tooltip('show');


    // cierre de la ventana del Karma
    $('body').on('click', '#karma .close', function() {
		karma.close()
 	});


	// al hacer clic en los badges vacía el contenido para que desaparezca
	$(function() {
        //Eventos del menu de cabecera
        $('.nav .dropdown > a >.badge').closest('a').click(function(e) {
            e.preventDefault()
            var url = $(this).attr('href')
            var element = $(this)
            $.ajax(url).done(function(data){
                element.find('.badge').delay(1000).fadeOut("slow").queue(function() {
                    $(this).empty();
                });
                element.next('ul').find('li.new').delay(1000).queue(function() {
                    $(this).removeClass('new', 1000);
                });
            });
        });

        //Eventos de los numeros del discover
		$('.introDiscover .badge').closest('a').click(function(e) {
            e.preventDefault()
			$(this).find('.badge').delay(2000).fadeOut("slow").queue(function() {
				$(this).empty();
			});
			$(this).next('ul').find('li.new').delay(2000).queue(function() {
				$(this).removeClass('new');
			});

            var activeId = $('.introDiscover li.active .badge').closest('a').html() -1;
            $('.introDiscover li.active .badge').removeClass("disabled");
            $('.introDiscover li.active').removeClass("active");
			var nextId = $(this).html() -1;
            $(this).addClass("disabled");
            $(this).parent("li").addClass("active");
            $("#relevantLaw_"+activeId).fadeOut(1000);
            $("#relevantLaw_"+nextId).fadeIn(3000);
		});
	});

    $("#brand.disabled").on('click', function(e){e.preventDefault();})
    // links kakareo, impulsar
    $('body').on('click', '.action.cluck', function(e) {
        e.preventDefault();
        e.stopPropagation();
        if (!$(this).hasClass('disabled')){
            var url = $(this).attr("href");
            var postId = $(this).parents("article").first().attr("data-cluck-postId");
            var html = $("article[data-cluck-postId='"+postId+"'] li.kakareo-number").html()
            $.ajax({
                url:url,
                beforeSend:function(xhr){
                    $("article[data-cluck-postId='"+postId+"'] li.kakareo-number").html('<div class="loading xs"><span class="sr-only">Cargando...</span></div>')
                }
            }).done(function(data){
                $("article[data-cluck-postId='"+postId+"'] li.kakareo-number").html(html)
                $("article[data-cluck-postId='"+postId+"'] li.kakareo-number .action").addClass('disabled');
                $("article[data-cluck-postId='"+postId+"'] li.kakareo-number .counter").each(function(idx, element){
                    var numKakareos = parseInt($(element).text()) +1;
                    $(element).text(numKakareos);
                });
            });
        }
    });

	// links kakareo, impulsar
	$('body').on('click', '.action.drive', function(e) {
		e.preventDefault();
        e.stopPropagation();
        if (!$(this).hasClass('disabled')){
            var url = $(this).attr("href");
            var postId = $(this).parents("article").first().attr("data-cluck-postId");
            votePost(url, postId, false)

        }
	});

    function votePost(url, postId, anonymous){
        var html = $("article[data-cluck-postId='"+postId+"'] li.like-number").html()
        var htmlDriveButton = $('#drive > a').html()
        var loadingHtml = '<div class="loading xs"><span class="sr-only">Cargando...</span></div>'
        $.ajax({
            url:url,
            data:{postId:postId, anonymous:anonymous},
            beforeSend:function(xhr){
                $("article[data-cluck-postId='"+postId+"'] li.like-number").html(loadingHtml)
                $('#drive > a').html(loadingHtml)
            }
        }).done(function(data){
            var numLikes = data.numLikes
            var limitTo = data.limitTo
            $("article[data-cluck-postId='"+postId+"'] li.like-number").html(html)
            $("article[data-cluck-postId='"+postId+"'] li.like-number .action").addClass('disabled');
            $("article[data-cluck-postId='"+postId+"'] li.like-number .counter").each(function(idx, element){
                numLikes = parseInt($(element).text()) +1;
                $(element).text(numLikes);
            });

            //If Page == Post
            var progressBarNumLikes = $("section.boxes.noted.likes > .likesContainer > div > .likesCounter")
            $('#m-callback-done').css('opacity', '0');
            $('.likes .progress-bar').attr("aria-valuetransitiongoal",numLikes)
            $('.likes .progress-bar').attr("aria-valuenow",numLikes)
            $('.likes .progress-bar').attr("aria-valuemax",limitTo)
            $('#drive > a').html(i18n.post.show.boxes.like.vote.buttonVoted).addClass('disabled');
            $("#drive :input").attr("disabled", true);
            $("#drive div.form-group").remove()
            prepareProgressBar()
            setTimeout(prepareProgressBar, 500)
//                setTimeout(prepareProgressBar, 1000)

//            console.log(data.gamification)
            karma.open(data.gamification)

        });
    }


	// leer después
	$('body').on('click', '.read-later a', function(e) {
        e.preventDefault();
        readLater($(this))

	});
    $('body').on('click', '#postNav .read-later a', function(e) {
        readLater($(this));
        e.preventDefault();
        e.stopPropagation();
    });

    function readLater(readLaterElement){
        var url = $(readLaterElement).attr("href");
        var postId = $(readLaterElement).parents("article").first().attr("data-cluck-postId");
        var html = $("article[data-cluck-postId='"+postId+"'] li.read-later").html()
        var loadingHtml = '<div class="loading xs"><span class="sr-only">Cargando...</span></div>'
        console.log("Reading later"+postId);
        $.ajax({
            url:url,
            beforeSend:function(xhr){
                $("article[data-cluck-postId='"+postId+"'] li.read-later").html(loadingHtml)
            }
        }).done(function(data, status, xhr){
            var isFavorite = xhr.getResponseHeader('isFavorite');
            var numFavorites = xhr.getResponseHeader('numList');
            $("article[data-cluck-postId='"+postId+"'] li.read-later").html(html)
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
    }


	// Cambio de flechita en el botón desplegar texto de la ley
	$('body').on('click', '.readMore a', function() {
		if ( $(this).hasClass('collapsed') ) {
			$(this).html('Ocultar texto');
		} else {
			$(this).html('Leer más');
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


	// añade la flechita al span de los mensajes de error de los formularios
	if ( $('.error').length > 0 ) {
		$('span.error').prepend('<span class="tooltip-arrow"></span>');
	}


	// botón Seguir de las cajas popover y página ley-participantes
	$(document).on({
	    mouseenter: function () {
            $(this).html($(this).attr('data-message-follow_hover'));
	    },
	    mouseleave: function () {
            $(this).html($(this).attr('data-message-follow'));
	    }
	}, "#follow.enabled"); //pass the element as an argument to .on

	$(document).on({
	    mouseenter: function () {
	        $(this).html($(this).attr('data-message-unfollow_hover'));
	    },
	    mouseleave: function () {
	        $(this).html($(this).attr('data-message-unfollow'));
	    }
	}, "#follow.disabled"); //pass the element as an argument to .on

	$('body').on("click", "#follow", function(e) {
        clickedButtonFollow($(this))
	});

    function clickedButtonFollow(button){
        var buttonFollow= $(button)
        if (buttonFollow.hasClass('noLogged')){
            var url = buttonFollow.attr("data-noLoggedUrl");
            location.href =url
        }
        else if ( buttonFollow.hasClass('disabled') ){
            var url = buttonFollow.attr("data-ajaxunfollowurl")
            ajaxFollow(url, buttonFollow, function(data, status, xhr) {
                var message = buttonFollow.attr('data-message-follow');
                var userId = buttonFollow.attr('data-userId');
                $("button[data-userId="+userId+"]").html(message).removeClass('disabled').addClass('enabled');
            });
        } else {
            var url = buttonFollow.attr("data-ajaxfollowurl")
            ajaxFollow(url, buttonFollow, function(data, status, xhr) {
                var message = buttonFollow.attr('data-message-unfollow');
                var userId = buttonFollow.attr('data-userId');
                $("button[data-userId="+userId+"]").html(message).removeClass('enabled').addClass('disabled');
            });
        }
    }

    function ajaxFollow(url, buttonFollow, doneFunction){
        $.ajax( {
            url:url,
            statusCode: {
                401: function() {
                    display.info("Estás deslogado")
                    setTimeout('location.reload()',5000);
                }
            },
            beforeSend: function(){
                buttonFollow.html('<div class="loading xs"><span class="sr-only">Cargando...</span></div>')
            },
            complete:function(){
//                console.log("end")
            }
        }).done(function(data, status, xhr) {
            doneFunction(data,status,xhr)
        })
    }


	// Deshabilitar enlaces números (descubre)
	$('.introDiscover .steps .active').find('a').addClass('disabled');
	$('body').on("click", ".introDiscover .steps .active a", function(e) {
		e.preventDefault();
	});

    // Deshabilitar botón defender (Post)
    $('body').on("click", "#driveDefend .btn", function(e) {
        e.preventDefault();
        var anonymous = $("#drive :input").is(":checked")
        var url = $(this).attr("href")
        var postId = $(this).attr("data-postId")
//        votePost(url, postId, anonymous)
    });

	// Deshabilitar botón Impulsar (Post)
	$('body').on("click", "#drive .btn", function(e) {
		e.preventDefault();
		var anonymous = $("#drive :input").is(":checked")
		var url = $(this).attr("href")
		var postId = $(this).attr("data-postId")
		votePost(url, postId, anonymous)
	});


	// Si voto desaparecen los botones y aparece el enlace de cambio de opinión
	$('body').on("click", ".voting li a", function(e) {
		e.preventDefault();
        var lawId = $(this).attr("data-lawId")
		$('section[data-lawId='+lawId+'] .voting ul').css('display', 'none');
		$('section[data-lawId='+lawId+'] .changeOpinion').css('display', 'block');
        $.ajax( {
            url:$(this).attr("href"),
            statusCode: {
                401: function() {
                    display.info("Estás deslogado")
                    setTimeout('location.reload()',5000);
                }
            }
        }).done(function(data, status, xhr) {
            $('section[data-lawId='+lawId+']  ul.activity li').removeClass("active")
            $('section[data-lawId='+lawId+']  ul.activity li.'+data.voteType).addClass("active")
            $('section[data-lawId='+lawId+']  ul.activity li.POSITIVE span').html(data.votes.yes)
            $('section[data-lawId='+lawId+']  ul.activity li.NEGATIVE span').html(data.votes.no)
            $('section[data-lawId='+lawId+']  ul.activity li.ABSTENTION span').html(data.votes.abs)
            $('section[data-lawId='+lawId+']  .kuorum span.counter').html(data.necessaryVotesForKuorum)
            if (data.newVote){
                karma.open(data.gamification)
            }
        })
	});

	$('body').on("click", ".voting li .yes", function(e) {
        var lawId = $(this).parents("section").attr("data-lawId")
		$('section[data-lawId='+lawId+'] .activity .favor').addClass('active');
	});
	$('body').on("click", ".voting li .no", function(e) {
        var lawId = $(this).parents("section").attr("data-lawId")
        $('section[data-lawId='+lawId+'] .activity .contra').addClass('active');
	});
	$('body').on("click", ".voting li .neutral", function(e) {
        var lawId = $(this).parents("section").attr("data-lawId")
        $('section[data-lawId='+lawId+'] .activity .abstencion').addClass('active');
	});


	// Si hago click en cambio de opinión vuelven los botones
	$('body').on("click", ".changeOpinion", function(e) {
		e.preventDefault();
        var lawId = $(this).parents("section").attr("data-lawId")
		$('section[data-lawId='+lawId+'] .activity li').removeClass('active');
		$(this).css('display', 'none');
		$('section[data-lawId='+lawId+']  .voting ul').css('display', 'block');
	});

	// Buscador: cambia el placeholder según el filtro elegido
	$(function() {

		var $ui = $('#search-form');
		$ui.find('#filters li a').bind('focus click',function(){
			var filtro = $(this).text();
			$ui.find('#srch-term').attr('placeholder', filtro);

			if ( $(this).hasClass('enleyes') ) {
				$('#filterSign').html('<span class="fa fa-briefcase"></span>');
			} else if ( $(this).hasClass('enpersonas') ) {
				$('#filterSign').html('<span class="fa fa-user"></span>');
			} else {
				$('#filterSign').html('');
			}

		});

	});


	//search filters
	$('#searchFilters input:checked').css('display','block');
	$('#searchFilters input:checked').closest('label').css('color','#545454');

	$('#searchFilters #todo').change(function() {
	    var checkboxes = $(this).closest('form').find(':checkbox');
	    if($(this).is(':checked')) {
	        checkboxes.prop('checked', true);
	        checkboxes.css('display','block');
	        $('#searchFilters label').css('color','#545454');
	    } else {
	        checkboxes.prop('checked', false);
	        checkboxes.css('display','none');
	        $('#searchFilters label').css('color','#a8a8a8');
	    }
	});

	$('#searchFilters ol > li > ul > li > .checkbox input').change(function() {
	    var checkboxes = $(this).closest('li').find(':checkbox');
	    $('#searchFilters #todo').prop('checked', false).css('display','none');
	    $('#searchFilters #todo').closest('label').css('color','#a8a8a8');
	    if($(this).is(':checked')) {
	        checkboxes.prop('checked', true);
	        checkboxes.css('display','block');
	        $(this).closest('label').css('color','#545454');
	        $(this).closest('li').find('ul > li label').css('color','#545454');

	    } else {
	        checkboxes.prop('checked', false);
	        checkboxes.css('display','none');
	        $(this).closest('label').css('color','#a8a8a8');
	        $(this).closest('li').find('ul > li label').css('color','#a8a8a8');
	    }
	});

	$('#searchFilters .only').change(function() {
		var inputSup = $(this).closest('.checkbox').closest('ul').prev('.checkbox');
		inputSup.find('input').prop('checked', false).css('display','none');
		inputSup.find('label').css('color','#a8a8a8');
		$('#searchFilters #todo').prop('checked', false).css('display','none');
	    $('#searchFilters #todo').closest('label').css('color','#a8a8a8');

	    if($(this).is(':checked')) {
	        $(this).css('display','block');
	        $(this).closest('label').css('color','#545454');
	    } else {
	        $(this).css('display','none');
	        $(this).closest('label').css('color','#a8a8a8');
	    }
        reloadSearchNewFilters()
	});

    $('#searchFilters input[type=checkbox]').change(function(){
        if($(this).is(':checked')) {
            $("#search-form input[value="+$(this).val()+"]").prop('checked', true);
        } else {
            $("#search-form input[value="+$(this).val()+"]").prop('checked', false);
        }
        reloadSearchNewFilters()
    });

    function reloadSearchNewFilters(){
        var form = $("#searchFilters")
        var elementToUpdate=$("#"+form.attr('data-updateElementId'))
        $.ajax( {
            url:form.attr("action"),
            data:form.serialize()
        }).done(function(data, status, xhr) {
            $(elementToUpdate).html(data)
        })

    }

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


	// oculta los comentarios
//	$('.listComments > li:gt(2)').hide();
	$('#ver-mas a').click(function(e) {
		e.preventDefault();
//		$('.listComments > li:gt(2)').fadeIn('slow');
		$('.listComments > li').fadeIn('slow');
		$('#ver-mas').hide();
	});
    $("#addComment").on('submit', function(e){
        e.preventDefault()
        var form = $(this)
        if (form.valid()){
            var parentId = form.attr('data-parent-id')
            var parent = $("#"+parentId)
            var url = form.attr('action')
            $.ajax( {
                url:url,
                data:$(this).serialize(),
                statusCode: {
                    401: function() {
                        display.warn("Tienes que registrarte para poder añadir comentarios")
                    }
                }
            })
                .done(function(data, status, xhr) {
                    parent.append(data)
                    $('#ver-mas a').click()
                    form[0].reset()
                    if (parent.children().last().prev().length >0){
                        //NO es el primer elemento
                        $("html, body").animate({ scrollTop: parent.children().last().prev().offset().top }, 1000);
                    }
                })
                .fail(function(data) {
                    console.log(data)
                })
                .always(function(data) {

                });
        }else{
            //console.log("pete")
        }
    })

    $("#addDebate").on('submit', function(e){
        e.preventDefault()
        var form = $(this)
        if (form.valid()){
            var url = form.attr('action')
            var ulList = $("ul.chat")
            $.ajax( {
                url:url,
                data:$(this).serialize(),
                statusCode: {
                    401: function() {
                        display.warn("Tienes que registrarte para poder añadir comentarios")
                    }
                }
            })
                .done(function(data, status, xhr) {
                    ulList.append(data)
                    form.each (function(){  this.reset();});
                    $("section.boxes.noted.likes").addClass("important")
                    $("#main > div.author").addClass("important")
                    $("#drive a").removeClass("btn-blue")
//                    $("html, body").animate({ scrollTop: parent.children().last().prev().offset().top }, 1000);
                })
                .fail(function(data) {
                    display.warn("Hubo algun error. Intent otra vez o contacte con info@kuorum.org")
                    console.log(data)
                })
                .always(function(data) {

                });
        }else{
            //console.log("pete")
        }
    })


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

	if ( $('.jqte_editor').text() == "" ) {
        $('.jqte_placeholder_text').css('display', 'block');
    } else {
    	$('.jqte_placeholder_text').css('display', 'none');
    }


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
        var formId = link.attr('data-form-id')
        url += '?'+$('#'+formId).serialize()
		var parentId = link.attr('data-parent-id')
		var offset = $.parseJSON(link.attr('data-offset') || 10 ) //Para que sea un integer
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
			var moreResults = $.parseJSON(xhr.getResponseHeader('moreResults')) //Para que sea un bool
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

    function prepareFormUsingGender(gender){
        if (gender == "ORGANIZATION"){
            $(".userData").hide()
            $(".organizationData").show()
        }else{
            $(".userData").show()
            $(".organizationData").hide()
        }

    }
    $("input[name=gender]").on("change", function(e){
        prepareFormUsingGender($(this).val())
    })

    if ($("input[name=gender]:checked").val() != undefined){
        prepareFormUsingGender($("input[name=gender]:checked").val())
    }else{
        prepareFormUsingGender("MALE")
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
		        $('#others').prop('checked', true);
		    } else {
		        checkboxes.iCheck('uncheck');
		        $('#others').prop('checked', false);
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


	// seleccionar todos los checkbox en configuración
	$(function () {
        $('.allActivityMails').change(function() {
            var formGroup = $(this).parents(".form-group")
            console.log(formGroup.length)
            if($(this).is(':checked')) {
                formGroup.find('.checkbox input').prop('checked', true);
            } else {
                formGroup.find('.checkbox input').prop('checked', false);
            }
        });
	});


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
    $('.ajax.popover-trigger.more-users').on('shown.bs.popover', function () {
        var that = $(this)
        var content = $(this).next().children(".popover-content")
        var ajaxUrl = content.children("a").attr("href")
        var ulUserLists = content.find("ul")
        $.ajax( {
            url:ajaxUrl,
            statusCode: {
                500: function() {
                    display.error("Error en el sistema")
                }
            },
            beforeSend: function(){
                ulUserLists.html('<li class="loading xs"><span class="sr-only">Cargando...</span></li>')
            },
            complete:function(){
//                console.log("end")
            }
        }).done(function(data, status, xhr) {
            ulUserLists.html(data)
        })
    })
    $('.roleButton').on("click", function(e){
        e.preventDefault()
        var link = $(this)
        var url = link.attr('href')
        ajaxFollow(url,link, function(data){
            $(".roleButton.active").removeClass("active").html("Activar")
            link.addClass("btn-green active")
            link.html(i18n.profile.kuorumStore.roleButton.active)
            $("#numEggs").html(data.numEggs)
            $("#numCorns").html(data.numCorns)
            $("#numPlumes").html(data.numPlumes)
        })
    })
    $('.skillButton').on("click", function(e){
        e.preventDefault()
        var link = $(this)
        var url = link.attr('href')
        ajaxFollow(url,link, function(data){
            link.addClass("active")
            console.log()
            link.html(i18n.profile.kuorumStore.skillButton.active)
            $("#numEggs").html(data.numEggs)
            $("#numCorns").html(data.numCorns)
            $("#numPlumes").html(data.numPlumes)
        })
    })

    $("#deleteAccountForm a").on("click", function(e){
        e.preventDefault()
        $("#deleteAccountForm input[name=forever]").val("true")
        $("#deleteAccountForm").submit()
    })
    $("#deleteAccountForm button").on("click", function(e){
        e.preventDefault()
        $("#deleteAccountForm input[name=forever]").val("false")
        $("#deleteAccountForm").submit()
    })
    $("#videoHome").on('hidden.bs.modal', function (e) {
        var iframe = $("#iframeVideo")[0].contentWindow;
        func = 'pauseVideo';
        //this is posible for this option on youtube video : enablejsapi=1
        iframe.postMessage('{"event":"command","func":"' + func + '","args":""}', '*');
    })

    $("#videoHome").on('shown.bs.modal', function (e) {
        var iframe = document.getElementById("iframeVideo").contentWindow;
        func = 'playVideo';
        //this is posible for this option on youtube video : enablejsapi=1
        iframe.postMessage('{"event":"command","func":"' + func + '","args":""}', '*');
//        $('#iframeVideo')[0].src += '&autoplay=1';
    })

    $(".multimedia .groupRadio input[type=radio]").on('click', function(e){
        var multimediaType = $(this).val()
        $('[data-multimedia-switch="on"]').hide()
        $('[data-multimedia-type="'+multimediaType+'"]').show()

    })

    // hacer clic en player falso del video (.front)
    $('.kakareo .front').click( function(e) {
        e.stopPropagation();
        e.preventDefault()
        var iframe = $(this).next('.youtube')
        iframe.css('display', 'block');
        $(this).remove();
        var func = 'playVideo';
        iframe.get(0).contentWindow.postMessage('{"event":"command","func":"' + func + '","args":""}', '*');
    });

    $('body').on("click", ".openModalVictory",function(e){
        var notificationId = $(this).attr("data-notification-id")
        modalVictory.openModal(notificationId)
    });

    $('body').on("click", ".openModalDefender",function(e){
        e.preventDefault()
        var postId = $(this).attr("data-postId")
        modalDefend.openModal(postId)
    });

    $('.modalVictoryClose').on("click", function (e) {
        e.preventDefault()
        $('#modalVictory').modal('hide');
    });

    $('body').on("click",".votePostCommentLink", function(e){
        e.preventDefault()
        var element = $(this)
        if (!element.hasClass("disabled")){
            var url = $(this).attr('href')
            $.ajax(url).done(function(data){
                element.parent().siblings().children().addClass("disabled");
                element.addClass("disabled");
                var q = parseInt(element.siblings('span').html(),10)
                element.siblings('span').html(q+1)
            });
        }
    })

});

var modalDefend = {
    data:{},
    openModal:function(postId){
        var modalData = this.data['post_'+postId]
        $("#modalDefenderPolitician img").attr('src',modalData.defender.imageUrl)
        $("#modalDefenderPolitician img").attr('alt',modalData.defender.name)
        $("#modalDefenderPolitician #sponsorLabel").html(modalData.post.sponsorLabel)
        $("#modalDefenderPolitician h1").html(modalData.defender.name)
        $("#modalDefenderOwner img").attr('src',modalData.owner.imageUrl)
        $("#modalDefenderOwner img").attr('alt',modalData.owner.name)
        $("#modalDefenderOwner .name").html(modalData.owner.name)
        $("#modalDefenderOwner .what").html(modalData.post.what)
        $("#modalDefenderOwner .action span").html(modalData.post.numVotes)
        $("#modalSponsor .modal-body").children("p").html(modalData.post.description)
        $("#modalSponsor .modal-body").children("div").each(function(i,buttonElement){
            var dataButton = modalData.post.options[i]
            $(buttonElement).children("a").html(dataButton.textButton)
            $(buttonElement).children("a").attr('href',dataButton.defendLink)
            $(buttonElement).children("p").html(dataButton.textDescription)
        })
    }
}

var modalVictory = {
    data:{},
    openModal:function(notificationId){
        var modalData = this.data['notification_'+notificationId]
        $("#modalVictoryUser img").attr('src',modalData.user.imageUrl)
        $("#modalVictoryUser img").attr('alt',modalData.user.name)
        $("#modalVictoryDefender img").attr('src',modalData.defender.imageUrl)
        $("#modalVictoryDefender img").attr('alt',modalData.defender.name)
        $("#modalVictoryDefender .name").html(modalData.defender.name)
        $("#modalVictoryDefender .action").html(modalData.post.action)
        $("#modalVictory .modal-body p").first().html(modalData.post.description)
        $("#modalVictory .modal-body p").last().html(modalData.post.lawLink)
        $("#modalVictory .modal-footer a").first().attr('href',modalData.post.victoryLink)
        $("#modalVictory .modal-footer a").last().attr('href',modalData.post.victoryLink)
    }
}

var karma = {
    title:"",
    text:"",

    numEggs:0,
    numPlumes:0,
    numCorns:0,
    open:function(options){
        if (options != undefined){
            this.numEggs = options.eggs || 0
            this.numPlumes = options.plumes || 0
            this.numCorns = options.corns || 0
            this.text = options.text || ""
            this.title = options.title || ""
        }
        this._open()
    },

    close:function(){
        $('#karma').css('display','none').removeClass('in');
    },

    _idLiEggs:"karmaEggs",
    _idLiPlumes:"karmaPlumes",
    _idLiCorn:"karmaCorn",
    _open:function(){
        this._prepareKarma()
        $('#karma').fadeIn().addClass('in');
    },

    _prepareKarma:function(){
        console.log(this.title)
        $("#karma h2").html(this.title)
        var motivation = $("#karma p span")
        var motivationText = "<span class='"+motivation.attr('class')+"'>"+motivation.html()+"</span>"
        $("#karma p").html(this.text +"<br>"+motivationText)

        $("ul.karma li").removeClass("active");
        this._prepareIcon(this._idLiEggs, this.numEggs)
        this._prepareIcon(this._idLiPlumes, this.numPlumes)
        this._prepareIcon(this._idLiCorn, this.numCorns)
    },

    _prepareIcon:function(liId, quantity){
        $("#"+liId+" .counter").html("+"+quantity)
        if (quantity > 0) $("#"+liId).addClass("active")
    }
}


function prepareProgressBar(){
    // animo la progress-bar de boxes.likes
    $('.likes .progress-bar').progressbar({
        done: function() {
            var posTooltip = $('.progress-bar').width();
            $('#m-callback-done').css('left', posTooltip).css('opacity', '1');
            $('#m-callback-done > .likesCounter').html($('.likes .progress-bar').attr("aria-valuenow"))
        }
    });
}

function prepareArrowClucks(){
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

    cookiesHelper.displayCookiesPolitics();

}
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

var cookiesHelper = {
    cookieNamePoliticsAccepted: "kuorumCookiesAccepted",
     setCookie:function(cname, cvalue, exdays) {
         var d = new Date();
         d.setTime(d.getTime() + (exdays*24*60*60*1000));
         var expires = "expires="+d.toGMTString();
         document.cookie = cname + "=" + cvalue + "; " + expires;
     },
    getCookie:function (cname) {
        var name = cname + "=";
        var ca = document.cookie.split(';');
        for(var i=0; i<ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0)==' ') c = c.substring(1);
            if (c.indexOf(name) != -1) return c.substring(name.length,c.length);
        }
        return "";
    },
    checkCookie: function(cname, onCookieFound, onNotCookieFound) {
        var cvalue=this.getCookie(cname);
        if (cvalue!="") {
            onCookieFound(cvalue);
        }else{
            onNotCookieFound(cname);
        }
    },
    displayCookiesPolitics: function (){
        this.checkCookie(
            this.cookieNamePoliticsAccepted,
            function(){},
            function(cName){
                var button = "<button id='acceptCookies' class='btn btn-xs' onclick='cookiesHelper.acceptedCookiesPolitics()'>"+i18n.cookies.accept+"</button>";
                var message = i18n.cookies.message + button;
                display.warn(message);
            }
        )
    },
    acceptedCookiesPolitics: function(){
        this.setCookie(this.cookieNamePoliticsAccepted, "true", 99999);
    }
}


$(document).ajaxStop(function () {

	// inicia el timeago
	$("time.timeago").timeago();

	prepareArrowClucks();
    preparePopover();

});