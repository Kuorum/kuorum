var htmlLoading = '<div class="loading xs"><span class="sr-only">Cargando...</span></div>';

// PAGE LOADING
function pageLoadingOn (trackLog){
    if (trackLog!= undefined) console.log("LOADING ON :: "+trackLog)
    $('html').addClass('loading');
}
function pageLoadingOff (trackLog){
    if (trackLog!= undefined) console.log("LOADING OFF :: " +trackLog)
    $('html').removeClass('loading');
}

function isPageLoading(){
    return $('html').hasClass('loading');
}

function isUserLogged(){
    return $("header #open-user-options").length > 0;
}

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
                if ( !$(this).hasClass('rating') ) {
                    e.preventDefault();
                }
                $popover_togglers.not(this).popover('hide');
            });
            $('html').on('click', '[data-dismiss="popover"]', function (e) {
                $popover_togglers.popover('hide');
            });
        }
    });

    $(function () {
        //Gestionamos manualmente la aparicion y desaparición de los popover
        $('[data-toggle="popover"][data-trigger="manual"]')
            //En el mousenter sacamos el popover
            .on("mouseenter",function(e){
                if ($(this).siblings(".in").length ==0){
                    $(this).popover('show')
                }
            })
            //En el click ejecutamos el link normal. El framework the popover lo está bloqueando
            .on("click", function(e){
                if ( !($(this).hasClass('user-rating') || $(this).hasClass('rating'))) {
                    var href = $(this).attr("href");
                    var target = $(this).attr("target");
                    console.log("TARGET = "+target)
                    console.log(target && target == "_blank" )
                    if (target && target == "_blank" ){
                        window.open(href, '_blank');
                        e.preventDefault();
                    }else{
                        window.location=href;
                    }
                }
            });

        $('[data-toggle="popover"]').popoverClosable();
        $('[data-toggle="popover"][data-trigger="manual"]').parent()
            .on("mouseleave", function(e){
                $(this).children('[data-toggle="popover"]').popover('hide')
            });
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

function Animate(domQuery,options){
    this.animatedIcon = $(domQuery);
    //var origin = this.animatedIcon.position();
    var that = this;
    var running = false;
    var _options = options || {}

    if (_options.stopOnHover){
        this.animatedIcon.hover(function(){
            that.stop();
        });

        this.animatedIcon.mouseleave(function (){
            that.start();
        });
    }

    this.start = function(){
        if (!running) {
            that.animatedIcon.animate({"top": 0});
            goOn();
            running = true;
        }
    }

    this.stop = function(){
        that.animatedIcon.clearQueue();
        that.animatedIcon.stop();
        running = false;
    }

    var goOn = function(){
        that.animatedIcon.animate({ "top": "-=5px"}, 500, "linear",goBack);
    }

    var goBack=function(){
        that.animatedIcon.animate({ "top": "+=5px"}, 500, "linear", goOn);
    }
}
// Animación tooltip para importación de contactos
$(function(){
    var animatedIcon = new Animate(".animated-info-icon", {stopOnHover:true});
    animatedIcon.start();
});


// EVENTOS ON RESIZE
$(window).on('resize',function() {

    // controla el alto del iframe de Youtube cuando cambia de ancho en los diferentes tamaños de pantalla
    $('.youtube').each(function() {
        var width = $(this).width();
        $(this).css("height", width / 1.77777778);
    });

    // controla el alto del cuadro de subir imagen con formato 16:9
    $('.fondoperfil .qq-upload-drop-area').each(function() {
        var width = $(this).width();
        $(this).css("height", width * 328 /728);
    });

    // controla el alto del cuadro de subir imagen de header campaign
    $('.header-campaign .qq-upload-drop-area').each(function() {
        var width = $(this).width();
        $(this).css("height", width / 5);
    });

    if (window.matchMedia && window.matchMedia("(min-width: 768px)").matches) {
        $('.header-campaign').each(function() {
            var width = $(this).width();
            $(this).css("height", width / 8.5);
        });
    } else {
        $('.header-campaign').each(function() {
            var width = $(this).width();
            $(this).css("height", width / 4.2);
        });
    }

});

$(document).ready(function() {
    $(window).trigger('resize');
});


// aparece la info en la franja superior bajo el header al hacer scroll
$(document).ready(function() {

    [].forEach.call(document.querySelectorAll('img[data-src]'),    function(img) {
        img.setAttribute('src', img.getAttribute('data-src'));
        img.onload = function() {
            img.removeAttribute('data-src');
        };
    });

    $(".modal").on("click", "a.close-modal", function(e){
        e.preventDefault();
        $(this).parents(".modal").modal('hide')
    })

    // HEAD SEARCHER
    var a = $('#srch-term').autocomplete({
        paramName:"word",
        params:{type:getFileterType(), searchType:getSearchType()},
        serviceUrl:kuorumUrls.searchSuggest,
        minChars:1,
        width:330,
        noCache: true, //default is false, set to true to disable caching
        onSearchStart: function (query) {
            $('.loadingSearch').show()
            query.searchType = getSearchType()
            query.type = getFileterType()
        },
        onSearchComplete: function (query, suggestions) {
            $('.loadingSearch').hide()
            $('#srch-regionCode').val("");
        },
        formatResult:function (suggestion, currentValue) {
            var format = ""
            if (suggestion.type=="SUGGESTION" || suggestion.type=="REGION"){
                format =  suggestion.value
            }
            // else if(suggestion.type=="USER"){
            //     format = "<img class='user-img' alt='"+suggestion.data.name+"' src='"+suggestion.data.urlAvatar+"'>"
            //     format +="<span class='name'>"+suggestion.data.name+"</span>"
            //     format +="<span class='user-type'>"+suggestion.data.role.i18n+"</span>"
            // }
            return format
        },
        searchUserText:function(userText){
            window.location = kuorumUrls.search+"?word="+encodeURIComponent(userText)
        },
        onSelect: function(suggestion){
            var location = kuorumUrls.search
                +"?type="+getFileterType()
                +"&searchType="+getSearchType()
                +"&word="+encodeURIComponent(suggestion.value)
            if(suggestion.type=="REGION"){
                location +="&regionCode="+suggestion.data.iso3166_2
            }
            window.location = location
        },
        triggerSelectOnValidInput:false,
        deferRequestBy: 100 //miliseconds
    });

    // END HEAD SEARCHER


    if ($('#header').lenght>0) {
        var headerTop = $('#header').offset().top;
        var headerBottom = headerTop + 300; // Sub-menu should appear after this distance from top.
        $(window).scroll(function () {
            var scrollTop = $(window).scrollTop(); // Current vertical scroll position from the top
            if (scrollTop > headerBottom) { // Check to see if we have scrolled more than headerBottom
                if (($("#info-sup-scroll").is(":visible") === false)) {
                    $('#info-sup-scroll').fadeIn('fast');
                }
            } else {
                if ($("#info-sup-scroll").is(":visible")) {
                    $('#info-sup-scroll').fadeOut('fast');
                }
            }
        });
    }
});

$(document).ready(function() {

    // highlight search resultas with mark.js for list.js

    if ($('input#searchCampaign').length) {
        var mark = function() {
            var keyword = $('input#searchCampaign').val();
            $('#campaignsList').unmark().mark(keyword, options);
        };
        $('input#searchCampaign').on('input', mark);
    }


    // DISABLED NAV TABS
    $(".nav-tabs > li.disabled > a, .nav-tabs > li.disabled").on("click", function(e){e.stopPropagation();return false;})

    // abrir/cerrar Save filter as
    $('body').on('click','#saveFilterAsBtnOpenModal, #saveFilterAsBtnCancel', function(e) {
        //e.stopPropagation();
        e.preventDefault();
        if ($('#saveFilterAsPopUp').hasClass('on')) {
            $(this).closest('#saveFilterAsPopUp').removeClass('on');
        } else {
            $(this).next('#saveFilterAsPopUp').addClass('on');
        }
    });

    // comportamiento para seleccionar filas en la tabla de importar contactos
    $('table.csv input[type="checkbox"]').change(function (e) {
        if ($(this).is(":checked")) {
            $(this).closest('tr').addClass("highlight_row");
        } else {
            var clear = $(this).closest('tr').index();
            $(this).closest('tr').removeClass('highlight_row');
            $('table.csv tbody tr:gt('+clear+')').removeClass('highlight_row').find('input').prop( "checked", false );
        }
    });

    // Handle click on card campaigns list [List of post/debates on dashboard, searcher, userProfile]
    if ($('ul.search-list .link-wrapper .card-footer .post-like').length > 0){
        $('ul.search-list').on("click",'.link-wrapper .card-footer .post-like',postFunctions.bindLikeClick);
    }

    // Animar porcentaje perfil político
    if ($('#profileInfo').length) {
        var skillBar = $('#progressProfileFill');
        var skillVal = skillBar.attr("data-progress");
        $(skillBar).animate({
            height: skillVal
        }, 1500);
        var skillIndicator = $('#progressLineFill');
        $(skillIndicator).animate({
            height: skillVal
        }, 1500);
        var skillText = $('#profileInfo > span');
        $(skillText).animate({
            bottom: skillVal
        }, 1500);

    }

    // Carrusel noticias perfil político
    $('.carousel.news').carousel({
          interval: false,
          wrap: true
    });

    // 3 items a partir de 768px
    if (window.matchMedia && window.matchMedia("(min-width: 768px)").matches) {

        $('.carousel.news .item').each(function(){
          var next = $(this).next();
          if (!next.length) {
              next = $(this).siblings(':first');
          }
          next.children(':first-child').clone().appendTo($(this));

          if ($('.carousel.news .item').length >= 3) {
              // For more than or equal to 3 photos
              if (next.next().length > 0) {
                  next.next().children(':first-child').clone().appendTo($(this));
              } else {
                  $(this).siblings(':first').children(':first-child').clone().appendTo($(this));
              }
          } else {
              // Hide controls for less than 3 photos
              $('.left.carousel-control').hide();
              $('.right.carousel-control').hide();
          }
        });

    }

    // oscurecer el header de la Landing cuando se hace scroll
    if ($('#header.transp').length) {
        $(window).on("load scroll",function(){
            var top = $(document).scrollTop();
            if (top >= 50) {
                $('#header.transp').addClass('scrolled');
            } else {
                $('#header.transp').removeClass('scrolled');
            }
        });
    }


    // isotope - plugin para apilar divs de diferente altura
    if ( $('.list-team').length > 0 ) {
        var $container = $('.list-team');
        $container.isotope({
          itemSelector: '.list-team > li'
        });
    }

    if ( $('.list-updates').length > 0 ) {
        var $container = $('.list-updates');
        $container.isotope({
          itemSelector: '.list-updates > li'
        });
    }


    // controla el comportamiento del módulo de la columna derecha en Propuestas
    $(window).on("load resize",function(e){

        // elimina el vídeo de la landing por debajo de 1025px
        if (window.matchMedia && window.matchMedia('only screen and (max-width: 1024px)').matches) {
            $('.landing .full-video').find('video').remove();
        }
    });

    // Switch porcentaje/numero para ratios de apertura y clicks de cada campaña
    $('span.stat').hover(
        function(){
            var num = $(this).attr("data-openratenum");
            $(this).text(num);
        }
    );
    $('span.stat').mouseout(
        function(){
            var percentage = $(this).attr("data-openrateptg");
            $(this).text(percentage);
        }
    );


    // botón de cierre del aviso
    $('body').on('click','.warning .close', function(e) {
        e.stopPropagation();
        e.preventDefault();
        $('p.warning').fadeOut('fast');
        $('body').css('padding-top', '55px');
    });

    // Funcionamiento de los radio button como nav-tabs
    $('input[name="cuenta"]').click(function () {
    //jQuery handles UI toggling correctly when we apply "data-target" attributes and call .tab('show')
    //on the <li> elements' immediate children, e.g the <label> elements:
        $(this).closest('label').tab('show');
    });

    // desvanecer y eliminar la caja primera del Dashboard (.condition)
    $('body').on('click','aside.condition > .close', function(e) {

        $(this).parent('aside.condition').fadeOut('slow', function(){
          $(this).remove();
        });

    });

    // Timeago
    $.timeago.settings.allowFuture = true;
    // inicializa formato fechas
    $("time.timeago").timeago();


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

    });

    // prepareArrowClucks(); lo he pasado a custom.js


    // hacer un bloque clicable y que tome que es su primer elemento la url del enlace a.hidden
    $('body').on('click','.link-wrapper', function(e) {
        //ÑAAAPAAAAA para que no salte el evento del link-wrapper en los popover
        var target = $(e.target);
        if (!target.is("a")){
            // If target clicked is an a => link-wrapper should'd be trigger and probably it will have a special trigger
            var popover = target.parents(".popover");
            if (!popover.hasClass("popover")){
                window.location = $(this).find('a.hidden').attr('href');
            }
        }
    });

    // popover-trigger dentro del kakareo no lanza el enlace del bloque clicable
    // $('.link-wrapper .popover-trigger').click(function(e) {
    //     e.stopPropagation();
    //     e.preventDefault();
    // });

    $('#search-results .popover-box .follow').on('click', function() {
        // e.preventDefault();
        // e.stopPropagation();
        /*If not stopPropagation -> .link-wrapper is fired -> el bloque anterior ya evita esto para los button dentro de popover*/
        clickedButtonFollow($(this));
    });


    // scroll suave a hashtag
    $('.smooth').click(function (event) {
        event.preventDefault();
        event.stopPropagation();
        $(this).blur();
        moveToHash($(this).attr("href"))
    });
    moveToHash(window.location.hash);

    // SEARCH FILTERS
    $('.open-filter #filters a').on("click", function(e){
        e.preventDefault();
        var link = $(this).attr("href");
        $(this).parents("li").first().siblings().find("a").removeClass("active");
        $(this).addClass("active");
        var value = link.substr(1);
        var iconClasses = $(this).find("span").first().attr("class");
        var text = $(this).find(".search-filter-text").html();
        $("input[name=searchType]").val(value);
        $(".open-filter > a > span + span:first").html(text);
        $(".open-filter > a > span").first().attr("class",iconClasses );
        $(".input-group-btn .btn.search").click();
    });

    // setTimeout(prepareProgressBar, 500)
    // prepareProgressBar();  lo he pasado a custom.js


    // al hacer clic en los badges vacía el contenido para que desaparezca
    $(function() {
        //Eventos del menu de cabecera
        $('.nav .dropdown > a >.badge').closest('a').click(function(e) {
            e.preventDefault();
            var url = $(this).attr('href');
            console.log(url)
            var element = $(this);
            $.ajax(url).done(function(data){
                element.find('.badge').delay(1000).fadeOut("slow").queue(function() {
                    $(this).empty();
                });
                element.next('ul').find('li.new').delay(1000).queue(function() {
                    $(this).removeClass('new', 1000);
                });
            });
        });

        $('#see-more-notifications').on("click",function (event) {
            event.preventDefault();
            event.stopPropagation();
            var $link = $(this)
            var offset= parseInt($link.attr("data-pagination-offset"))+1;
            var max= parseInt($link.attr("data-pagination-max"));
            var total= parseInt($link.attr("data-pagination-total"));
            var url = $link.attr("href");
            var $seeMoreContainer =  $link.parents(".see-more");
            var $ul = $seeMoreContainer.parents("ul.notification-menu");
            console.log($ul)

            $.ajax({
                type: 'POST',
                url: url,
                data: {
                    offset: offset,
                    max: max
                },
                success: function(result) {
                    $link.attr("data-pagination-offset", offset);
                    $(result).insertBefore($ul.find("li").last())
                    if (total <= (offset+1)*max){
                        $seeMoreContainer.fadeOut("fast")
                    }

                },
                error: function() {
                    console.log('error');
                }
            });
        });


    });

    $(".saveDraft").on("click", function(e){
        e.preventDefault();
        $("input[name=isDraft]").val(true);
        $(this).parents("form").submit();
    });


    // controlando el video de Vimeo en la modal de la Home
    // $('.play a').click( function(e) {
    //
    //     var iframeHome = $('#vimeoplayer')[0];
    //     var playerHome = $f(iframeHome);
    //
    //     $("#videoHome").on('hidden.bs.modal', function (e) {
    //         playerHome.api('pause');
    //     });
    //     $("#videoHome").on('shown.bs.modal', function (e) {
    //         playerHome.api('play');
    //     })
    //
    // });

    // controlando el video de Vimeo en el Embudo1
    // $(function () {
    //
    //     $('.vimeo.uno .front').click( function(e) {
    //         e.stopPropagation();
    //         e.preventDefault();
    //         $(this).next('iframe').css('display', 'block');
    //         $(this).remove();
    //
    //         var iframe1 = $('#vimeoplayer1')[0];
    //         var player1 = $f(iframe1);
    //         player1.api('play');
    //
    //     });
    //
    //     $('.vimeo.dos .front').click( function(e) {
    //         e.stopPropagation();
    //         e.preventDefault();
    //         $(this).next('iframe').css('display', 'block');
    //         $(this).remove();
    //
    //         var iframe2 = $('#vimeoplayer2')[0];
    //         var player2 = $f(iframe2);
    //         player2.api('play');
    //
    //     });
    //
    //     $('.vimeo.tres .front').click( function(e) {
    //         e.stopPropagation();
    //         e.preventDefault();
    //         $(this).next('iframe').css('display', 'block');
    //         $(this).remove();
    //
    //         var iframe3 = $('#vimeoplayer3')[0];
    //         var player3 = $f(iframe3);
    //         player3.api('play');
    //
    //     });
    //
    // });

    prepareYoutubeVideosClick();

    // Language selector footer
    $('select#language-selector').on('change', function() {
        window.location.href = $(this).val();
    });

    // Request custom email sender
    $('.box-ppal .box-ppal-section #requestCustomSender').on('click', function (e){
        e.preventDefault();
        var $selector =$(this);
        requestCustomSender($selector);
    });


    // mostrar/ocultar pass en formulario de password
    $('.show-hide-pass').on('change', function () {
        var div_parent  = $(this).closest('div');
        var input_id    = div_parent.children('input:first').attr('id');

        $('#'+input_id).hideShowPassword($(this).prop('checked'));
    });
});

function prepareYoutubeVideosClick(){
    // hacer clic en player falso del video (.front)
    $('.video').find('.front').click( function(e) {
        e.stopPropagation();
        e.preventDefault();
        var iframe = $(this).next('.youtube');
        iframe.css('display', 'block');
//        iframe[0].src += "&autoplay=1";
        $(this).remove();
        var func = 'playVideo';
        iframe.get(0).contentWindow.postMessage('{"event":"command","func":"' + func + '","args":""}', '*');
    });
}

function requestCustomSender($selector) {
    pageLoadingOn();
    var map = $selector.attr('data-ajaxRequestSender');
    $.post(map)
        .done(function(data){
            $selector.attr('disabled', 'disabled');
            var checkIcon = $('.box-ppal .box-ppal-section').find('.valid');
            checkIcon.removeClass('hidden');
        })
        .fail(function(error){
            display.warn("There was an error doing the request");
        })
        .always(function() {
            pageLoadingOff();
        });
}


function stringStartsWith (string, prefix) {
    if (string == undefined || string.length < prefix.length){
        return false;
    }
    return string.slice(0, prefix.length) == prefix;
}

function moveToHash(hash){
    var dest = 0;
    hash = hash + "-tag";
    hash = hash.replace("=",""); // Facebook login adds #_=_ at the end of the URL. This makes to fail this logic
    if ($(hash).length){ //If the element exists
        if ($(hash).offset().top > $(document).height() - $(window).height()) {
            dest = $(document).height() - $(window).height();
        } else {
            dest = $(hash).offset().top;
        }
        var extraOffset = -70;
        if ($(hash)[0].hasAttribute("data-smoothOffset")){
            extraOffset = parseInt($(hash).attr("data-smoothOffset"))
        }
        //console.log("Moving to "+hash + " offset: "+extraOffset);
        dest = dest + extraOffset;
        //go to destination
        $('html,body').animate({
            scrollTop: dest
        }, 1000, 'swing');
    }
}

function guid() {
    function s4() {
        return Math.floor((1 + Math.random()) * 0x10000)
            .toString(16)
            .substring(1);
    }
    return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
        s4() + '-' + s4() + s4() + s4();
}

function formatTooltipDate(date) {
    var hours = date.getHours();
    var minutes = date.getMinutes();
    minutes = minutes < 10 ? '0'+minutes : minutes;
    hours = hours < 10 ? '0'+hours : hours;
    var strTime = hours + ':' + minutes;
    return date.getDate()+"-"+(date.getMonth()+1) + "-" + date.getFullYear() + "  " + strTime;
}

function getSearchType(){
    return $("#srch-type").val()
}
function getFileterType(){
    return $("#srch-userType").val()
}