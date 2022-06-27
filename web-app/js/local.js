
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
                    $(this).popover('show');
                }
            })
            //En el click ejecutamos el link normal. El framework the popover lo está bloqueando
            .on("click", function(e){
                if ( !($(this).hasClass('user-rating') || $(this).hasClass('rating'))) {
                    var href = $(this).attr("href");
                    var target = $(this).attr("target");
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

function findDuplicates(arr) {
    let sorted_arr = arr.slice().sort(); // You can define the comparing function here.
    // JS by default uses a crappy string compare.
    // (we use slice to clone the array so the
    // original array won't be modified)
    let results = [];
    for (let i = 0; i < sorted_arr.length - 1; i++) {
        if (sorted_arr[i + 1] == sorted_arr[i]) {
            results.push(sorted_arr[i]);
        }
    }
    return results;
}

function Animate(domQuery,options){
    this.animatedIcon = $(domQuery);
    //var origin = this.animatedIcon.position();
    var that = this;
    var running = false;
    var _options = options || {};

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
    };

    this.stop = function(){
        that.animatedIcon.clearQueue();
        that.animatedIcon.stop();
        running = false;
    };

    var goOn = function(){
        that.animatedIcon.animate({ "top": "-=5px"}, 500, "linear",goBack);
    };

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
    });

    // HEAD SEARCHER
    var a = $('#srch-term').autocomplete({
        paramName:"word",
        params:{type:getFileterType(), searchType:getSearchType()},
        serviceUrl:kuorumUrls.searchSuggest,
        minChars:1,
        width:330,
        noCache: true, //default is false, set to true to disable caching
        onSearchStart: function (query) {
            $('.loadingSearch').show();
            query.searchType = getSearchType();
            query.type = getFileterType()
        },
        onSearchComplete: function (query, suggestions) {
            $('.loadingSearch').hide();
            $('#srch-regionCode').val("");
        },
        formatResult:function (suggestion, currentValue) {
            var format = "";
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
                +"&word="+encodeURIComponent(suggestion.value);
            if(suggestion.type=="REGION"){
                location +="&regionCode="+suggestion.data.iso3166_2
            }
            window.location = location
        },
        triggerSelectOnValidInput:false,
        deferRequestBy: 100 //miliseconds
    });

    // END HEAD SEARCHER
});

$(document).ready(function() {

    prepareTooltips();
    // highlight search resultas with mark.js for list.js

    if ($('input#searchCampaign').length) {
        var mark = function() {
            var keyword = $('input#searchCampaign').val();
            $('#campaignsList').unmark().mark(keyword, options);
        };
        $('input#searchCampaign').on('input', mark);
    }


    // DISABLED NAV TABS
    $(".nav-tabs > li.disabled > a, .nav-tabs > li.disabled").on("click", function(e){e.stopPropagation();return false;});

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

    prepareCampaignClickEvents();

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

    // hacer un bloque clicable y que tome que es su primer elemento la url del enlace a.hidden
    $('body').on('click','.link-wrapper', function(e) {
        //ÑAAAPAAAAA para que no salte el evento del link-wrapper en los popover
        var target = $(e.target);
        if (target.parent("a").length==0 && !target.is("a")){
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
    function loadMoreNotifications(){
        var $link = $("#see-more-notifications");
        var offset= parseInt($link.attr("data-pagination-offset"))+1;
        var max= parseInt($link.attr("data-pagination-max"));
        var total= parseInt($link.attr("data-pagination-total"));
        var url = $link.attr("href");
        var $seeMoreContainer =  $link.parents(".see-more");
        var $loadingLi = $("<li class='loading'></li>");
        var $ul = $seeMoreContainer.parents("ul.notification-menu");
        $ul.prepend($loadingLi)


        $.ajax({
            type: 'POST',
            url: url,
            data: {
                offset: offset,
                max: max
            },
            success: function(result) {
                $link.attr("data-pagination-offset", offset);
                $(result).insertBefore($ul.find("li").last());
                if (total <= (offset+1)*max){
                    $seeMoreContainer.fadeOut("fast")
                }
                $loadingLi.remove()

            },
            error: function() {
                console.log('error');
                $loadingLi.remove()
            }
        });
    }
    $(function() {
        //Eventos del menu de cabecera
        $('.nav .dropdown > a >.badge').closest('a').click(function(e) {
            e.preventDefault();
            var url = $(this).attr('href');
            loadMoreNotifications();
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
            loadMoreNotifications();
        });


    });

    $(".saveDraft").on("click", function(e){
        e.preventDefault();
        $("input[name=isDraft]").val(true);
        $(this).parents("form").submit();
    });

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
    /*****************
     * Create list of participative budgets
     *****************/
    $(".get-participatory-budgets").on('click', function (e) {
        e.preventDefault();
        var ajaxLink = $(this).attr('href');
        getParticipatoryBudgetList(ajaxLink);
    });

});

function getParticipatoryBudgetList(ajaxLink) {
    pageLoadingOn();
    $.ajax({
        type: "GET",
        url: ajaxLink,
        success: function(jsonData){
            var $modal = $('.modal-pb');
            var $modalContent = $('.modal-pb .modal-content .pb-list');
            $modalContent.html(jsonData);
            $modal.modal("show")
        },
        error:function(){
            display.error("Sorry: Error doing operation");
        },
        complete: function () {
            pageLoadingOff();
        }
    });
}

function prepareYoutubeVideosClick(){
    // hacer clic en player falso del video (.front)
    $('.video.click-handler-no-processed').find('.front').click( function(e) {
        e.stopPropagation();
        e.preventDefault();
        var iframe = $(this).next('.youtube');
        iframe.css('display', 'block');
        youtubeHelper.playVideo(iframe[0]); // $(this).remove should be after play because it takes the height from the link
        $(this).remove();
    });
    $(".video.click-handler-no-processed").removeClass("click-handler-no-processed")

}

function prepareCampaignClickEvents(){
    // Handle click on card campaigns list [List of post/debates on dashboard, searcher, userProfile]
    if ($('ul.search-list .link-wrapper .card-footer .post-like').length > 0){
        $('ul.search-list').on("click",'.link-wrapper .card-footer .post-like',postFunctions.bindLikeClick);
    }
}

function prepareTooltips(){
    $('[data-toggle="tooltip"]').tooltip();
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

function getHash(){

}

function normalizeHash(hash){
    hash = hash.replace("=",""); // Facebook login adds #_=_ at the end of the URL. This makes to fail this logic
    hash = hash.replace("'",""); // Facebook login adds #_=_ at the end of the URL. This makes to fail this logic
    hash = decodeURIComponent(hash);
    hash = removeDiacritics(hash);
    hash = hash.replace(/ /g,"-");
    hash = hash.toLowerCase();
    return hash;
}

function moveToHash(hash){
    var dest = 0;
    hash = normalizeHash(hash);
    hash = hash + "-tag";
    moveSmooth(hash)
}

function moveSmooth(hash){
    if ($(hash).length==1){ //If the element exists
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
    }else{
        // console.log("No exists element with id: "+hash)
    }
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


var defaultDiacriticsRemovalMap = [
    {'base':'A', 'letters':/[\u0041\u24B6\uFF21\u00C0\u00C1\u00C2\u1EA6\u1EA4\u1EAA\u1EA8\u00C3\u0100\u0102\u1EB0\u1EAE\u1EB4\u1EB2\u0226\u01E0\u00C4\u01DE\u1EA2\u00C5\u01FA\u01CD\u0200\u0202\u1EA0\u1EAC\u1EB6\u1E00\u0104\u023A\u2C6F]/g},
    {'base':'AA','letters':/[\uA732]/g},
    {'base':'AE','letters':/[\u00C6\u01FC\u01E2]/g},
    {'base':'AO','letters':/[\uA734]/g},
    {'base':'AU','letters':/[\uA736]/g},
    {'base':'AV','letters':/[\uA738\uA73A]/g},
    {'base':'AY','letters':/[\uA73C]/g},
    {'base':'B', 'letters':/[\u0042\u24B7\uFF22\u1E02\u1E04\u1E06\u0243\u0182\u0181]/g},
    {'base':'C', 'letters':/[\u0043\u24B8\uFF23\u0106\u0108\u010A\u010C\u00C7\u1E08\u0187\u023B\uA73E]/g},
    {'base':'D', 'letters':/[\u0044\u24B9\uFF24\u1E0A\u010E\u1E0C\u1E10\u1E12\u1E0E\u0110\u018B\u018A\u0189\uA779]/g},
    {'base':'DZ','letters':/[\u01F1\u01C4]/g},
    {'base':'Dz','letters':/[\u01F2\u01C5]/g},
    {'base':'E', 'letters':/[\u0045\u24BA\uFF25\u00C8\u00C9\u00CA\u1EC0\u1EBE\u1EC4\u1EC2\u1EBC\u0112\u1E14\u1E16\u0114\u0116\u00CB\u1EBA\u011A\u0204\u0206\u1EB8\u1EC6\u0228\u1E1C\u0118\u1E18\u1E1A\u0190\u018E]/g},
    {'base':'F', 'letters':/[\u0046\u24BB\uFF26\u1E1E\u0191\uA77B]/g},
    {'base':'G', 'letters':/[\u0047\u24BC\uFF27\u01F4\u011C\u1E20\u011E\u0120\u01E6\u0122\u01E4\u0193\uA7A0\uA77D\uA77E]/g},
    {'base':'H', 'letters':/[\u0048\u24BD\uFF28\u0124\u1E22\u1E26\u021E\u1E24\u1E28\u1E2A\u0126\u2C67\u2C75\uA78D]/g},
    {'base':'I', 'letters':/[\u0049\u24BE\uFF29\u00CC\u00CD\u00CE\u0128\u012A\u012C\u0130\u00CF\u1E2E\u1EC8\u01CF\u0208\u020A\u1ECA\u012E\u1E2C\u0197]/g},
    {'base':'J', 'letters':/[\u004A\u24BF\uFF2A\u0134\u0248]/g},
    {'base':'K', 'letters':/[\u004B\u24C0\uFF2B\u1E30\u01E8\u1E32\u0136\u1E34\u0198\u2C69\uA740\uA742\uA744\uA7A2]/g},
    {'base':'L', 'letters':/[\u004C\u24C1\uFF2C\u013F\u0139\u013D\u1E36\u1E38\u013B\u1E3C\u1E3A\u0141\u023D\u2C62\u2C60\uA748\uA746\uA780]/g},
    {'base':'LJ','letters':/[\u01C7]/g},
    {'base':'Lj','letters':/[\u01C8]/g},
    {'base':'M', 'letters':/[\u004D\u24C2\uFF2D\u1E3E\u1E40\u1E42\u2C6E\u019C]/g},
    {'base':'N', 'letters':/[\u004E\u24C3\uFF2E\u01F8\u0143\u00D1\u1E44\u0147\u1E46\u0145\u1E4A\u1E48\u0220\u019D\uA790\uA7A4]/g},
    {'base':'NJ','letters':/[\u01CA]/g},
    {'base':'Nj','letters':/[\u01CB]/g},
    {'base':'O', 'letters':/[\u004F\u24C4\uFF2F\u00D2\u00D3\u00D4\u1ED2\u1ED0\u1ED6\u1ED4\u00D5\u1E4C\u022C\u1E4E\u014C\u1E50\u1E52\u014E\u022E\u0230\u00D6\u022A\u1ECE\u0150\u01D1\u020C\u020E\u01A0\u1EDC\u1EDA\u1EE0\u1EDE\u1EE2\u1ECC\u1ED8\u01EA\u01EC\u00D8\u01FE\u0186\u019F\uA74A\uA74C]/g},
    {'base':'OI','letters':/[\u01A2]/g},
    {'base':'OO','letters':/[\uA74E]/g},
    {'base':'OU','letters':/[\u0222]/g},
    {'base':'P', 'letters':/[\u0050\u24C5\uFF30\u1E54\u1E56\u01A4\u2C63\uA750\uA752\uA754]/g},
    {'base':'Q', 'letters':/[\u0051\u24C6\uFF31\uA756\uA758\u024A]/g},
    {'base':'R', 'letters':/[\u0052\u24C7\uFF32\u0154\u1E58\u0158\u0210\u0212\u1E5A\u1E5C\u0156\u1E5E\u024C\u2C64\uA75A\uA7A6\uA782]/g},
    {'base':'S', 'letters':/[\u0053\u24C8\uFF33\u1E9E\u015A\u1E64\u015C\u1E60\u0160\u1E66\u1E62\u1E68\u0218\u015E\u2C7E\uA7A8\uA784]/g},
    {'base':'T', 'letters':/[\u0054\u24C9\uFF34\u1E6A\u0164\u1E6C\u021A\u0162\u1E70\u1E6E\u0166\u01AC\u01AE\u023E\uA786]/g},
    {'base':'TZ','letters':/[\uA728]/g},
    {'base':'U', 'letters':/[\u0055\u24CA\uFF35\u00D9\u00DA\u00DB\u0168\u1E78\u016A\u1E7A\u016C\u00DC\u01DB\u01D7\u01D5\u01D9\u1EE6\u016E\u0170\u01D3\u0214\u0216\u01AF\u1EEA\u1EE8\u1EEE\u1EEC\u1EF0\u1EE4\u1E72\u0172\u1E76\u1E74\u0244]/g},
    {'base':'V', 'letters':/[\u0056\u24CB\uFF36\u1E7C\u1E7E\u01B2\uA75E\u0245]/g},
    {'base':'VY','letters':/[\uA760]/g},
    {'base':'W', 'letters':/[\u0057\u24CC\uFF37\u1E80\u1E82\u0174\u1E86\u1E84\u1E88\u2C72]/g},
    {'base':'X', 'letters':/[\u0058\u24CD\uFF38\u1E8A\u1E8C]/g},
    {'base':'Y', 'letters':/[\u0059\u24CE\uFF39\u1EF2\u00DD\u0176\u1EF8\u0232\u1E8E\u0178\u1EF6\u1EF4\u01B3\u024E\u1EFE]/g},
    {'base':'Z', 'letters':/[\u005A\u24CF\uFF3A\u0179\u1E90\u017B\u017D\u1E92\u1E94\u01B5\u0224\u2C7F\u2C6B\uA762]/g},
    {'base':'a', 'letters':/[\u0061\u24D0\uFF41\u1E9A\u00E0\u00E1\u00E2\u1EA7\u1EA5\u1EAB\u1EA9\u00E3\u0101\u0103\u1EB1\u1EAF\u1EB5\u1EB3\u0227\u01E1\u00E4\u01DF\u1EA3\u00E5\u01FB\u01CE\u0201\u0203\u1EA1\u1EAD\u1EB7\u1E01\u0105\u2C65\u0250]/g},
    {'base':'aa','letters':/[\uA733]/g},
    {'base':'ae','letters':/[\u00E6\u01FD\u01E3]/g},
    {'base':'ao','letters':/[\uA735]/g},
    {'base':'au','letters':/[\uA737]/g},
    {'base':'av','letters':/[\uA739\uA73B]/g},
    {'base':'ay','letters':/[\uA73D]/g},
    {'base':'b', 'letters':/[\u0062\u24D1\uFF42\u1E03\u1E05\u1E07\u0180\u0183\u0253]/g},
    {'base':'c', 'letters':/[\u0063\u24D2\uFF43\u0107\u0109\u010B\u010D\u00E7\u1E09\u0188\u023C\uA73F\u2184]/g},
    {'base':'d', 'letters':/[\u0064\u24D3\uFF44\u1E0B\u010F\u1E0D\u1E11\u1E13\u1E0F\u0111\u018C\u0256\u0257\uA77A]/g},
    {'base':'dz','letters':/[\u01F3\u01C6]/g},
    {'base':'e', 'letters':/[\u0065\u24D4\uFF45\u00E8\u00E9\u00EA\u1EC1\u1EBF\u1EC5\u1EC3\u1EBD\u0113\u1E15\u1E17\u0115\u0117\u00EB\u1EBB\u011B\u0205\u0207\u1EB9\u1EC7\u0229\u1E1D\u0119\u1E19\u1E1B\u0247\u025B\u01DD]/g},
    {'base':'f', 'letters':/[\u0066\u24D5\uFF46\u1E1F\u0192\uA77C]/g},
    {'base':'g', 'letters':/[\u0067\u24D6\uFF47\u01F5\u011D\u1E21\u011F\u0121\u01E7\u0123\u01E5\u0260\uA7A1\u1D79\uA77F]/g},
    {'base':'h', 'letters':/[\u0068\u24D7\uFF48\u0125\u1E23\u1E27\u021F\u1E25\u1E29\u1E2B\u1E96\u0127\u2C68\u2C76\u0265]/g},
    {'base':'hv','letters':/[\u0195]/g},
    {'base':'i', 'letters':/[\u0069\u24D8\uFF49\u00EC\u00ED\u00EE\u0129\u012B\u012D\u00EF\u1E2F\u1EC9\u01D0\u0209\u020B\u1ECB\u012F\u1E2D\u0268\u0131]/g},
    {'base':'j', 'letters':/[\u006A\u24D9\uFF4A\u0135\u01F0\u0249]/g},
    {'base':'k', 'letters':/[\u006B\u24DA\uFF4B\u1E31\u01E9\u1E33\u0137\u1E35\u0199\u2C6A\uA741\uA743\uA745\uA7A3]/g},
    {'base':'l', 'letters':/[\u006C\u24DB\uFF4C\u0140\u013A\u013E\u1E37\u1E39\u013C\u1E3D\u1E3B\u017F\u0142\u019A\u026B\u2C61\uA749\uA781\uA747]/g},
    {'base':'lj','letters':/[\u01C9]/g},
    {'base':'m', 'letters':/[\u006D\u24DC\uFF4D\u1E3F\u1E41\u1E43\u0271\u026F]/g},
    {'base':'n', 'letters':/[\u006E\u24DD\uFF4E\u01F9\u0144\u00F1\u1E45\u0148\u1E47\u0146\u1E4B\u1E49\u019E\u0272\u0149\uA791\uA7A5]/g},
    {'base':'nj','letters':/[\u01CC]/g},
    {'base':'o', 'letters':/[\u006F\u24DE\uFF4F\u00F2\u00F3\u00F4\u1ED3\u1ED1\u1ED7\u1ED5\u00F5\u1E4D\u022D\u1E4F\u014D\u1E51\u1E53\u014F\u022F\u0231\u00F6\u022B\u1ECF\u0151\u01D2\u020D\u020F\u01A1\u1EDD\u1EDB\u1EE1\u1EDF\u1EE3\u1ECD\u1ED9\u01EB\u01ED\u00F8\u01FF\u0254\uA74B\uA74D\u0275]/g},
    {'base':'oi','letters':/[\u01A3]/g},
    {'base':'ou','letters':/[\u0223]/g},
    {'base':'oo','letters':/[\uA74F]/g},
    {'base':'p','letters':/[\u0070\u24DF\uFF50\u1E55\u1E57\u01A5\u1D7D\uA751\uA753\uA755]/g},
    {'base':'q','letters':/[\u0071\u24E0\uFF51\u024B\uA757\uA759]/g},
    {'base':'r','letters':/[\u0072\u24E1\uFF52\u0155\u1E59\u0159\u0211\u0213\u1E5B\u1E5D\u0157\u1E5F\u024D\u027D\uA75B\uA7A7\uA783]/g},
    {'base':'s','letters':/[\u0073\u24E2\uFF53\u00DF\u015B\u1E65\u015D\u1E61\u0161\u1E67\u1E63\u1E69\u0219\u015F\u023F\uA7A9\uA785\u1E9B]/g},
    {'base':'t','letters':/[\u0074\u24E3\uFF54\u1E6B\u1E97\u0165\u1E6D\u021B\u0163\u1E71\u1E6F\u0167\u01AD\u0288\u2C66\uA787]/g},
    {'base':'tz','letters':/[\uA729]/g},
    {'base':'u','letters':/[\u0075\u24E4\uFF55\u00F9\u00FA\u00FB\u0169\u1E79\u016B\u1E7B\u016D\u00FC\u01DC\u01D8\u01D6\u01DA\u1EE7\u016F\u0171\u01D4\u0215\u0217\u01B0\u1EEB\u1EE9\u1EEF\u1EED\u1EF1\u1EE5\u1E73\u0173\u1E77\u1E75\u0289]/g},
    {'base':'v','letters':/[\u0076\u24E5\uFF56\u1E7D\u1E7F\u028B\uA75F\u028C]/g},
    {'base':'vy','letters':/[\uA761]/g},
    {'base':'w','letters':/[\u0077\u24E6\uFF57\u1E81\u1E83\u0175\u1E87\u1E85\u1E98\u1E89\u2C73]/g},
    {'base':'x','letters':/[\u0078\u24E7\uFF58\u1E8B\u1E8D]/g},
    {'base':'y','letters':/[\u0079\u24E8\uFF59\u1EF3\u00FD\u0177\u1EF9\u0233\u1E8F\u00FF\u1EF7\u1E99\u1EF5\u01B4\u024F\u1EFF]/g},
    {'base':'z','letters':/[\u007A\u24E9\uFF5A\u017A\u1E91\u017C\u017E\u1E93\u1E95\u01B6\u0225\u0240\u2C6C\uA763]/g}
];

function removeDiacritics (str) {

    for(var i=0; i<defaultDiacriticsRemovalMap.length; i++) {
        str = str.replace(defaultDiacriticsRemovalMap[i].letters, defaultDiacriticsRemovalMap[i].base);
    }
    return str;
}

KEYBOARD_EVENT_CODES={
    SPACE:32,
    ENTER:13,
    TAB:9,
    LEFT:39,
    RIGHT:37,
    UP:38,
    DOWN:40
}