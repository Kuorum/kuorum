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
                var href = $(this).attr("href")
                window.location=href;
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


// controla el alto del iframe de Youtube cuando cambia de ancho en los diferentes tamaños de pantalla
$(window).on('resize',function() {
    $('.youtube').each(function() {
        var width = $(this).width();
        $(this).css("height", width / 1.77777778);
    });
});
$(document).ready(function() {
    $(window).trigger('resize');
});


// controla el alto del cuadro de subir imagen con formato 16:9
$(window).on('resize',function() {
    $('.fondoperfil .qq-upload-drop-area').each(function() {
        var width = $(this).width();
        $(this).css("height", width * 328 /728);
    });
});
$(document).ready(function() {
    $(window).trigger('resize');
});


// aparece la info en la franja superior bajo el header al hacer scroll
$(document).ready(function() {
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
});


$(document).ready(function() {

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
    
    // custom radio option en formulario Subscribe2
    if ($('.subscribeForm').length) {
        var inputOption = $('.subscribeForm input[type=radio]');
        $(inputOption).click(function(){
            $(this).closest('.radioOptions').find('label').removeClass();
            $(this).closest('label').addClass('selected');
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

    $("#partialUserTryingToVote a").on("click", function(e){
        e.preventDefault();
        var voteType = $(this).attr("data-voteType");
        $("#basicUserDataForm input[name=voteType]").val(voteType);
        $("#basicUserDataForm").submit()
    })


    // controla el comportamiento del módulo de la columna derecha en Propuestas
    $(window).on("load resize",function(e){

        // elimina el vídeo de la landing por debajo de 1025px
        if (window.matchMedia('only screen and (max-width: 1024px)').matches) {
            $('.landing .full-video').find('video').remove();
        }
        
        // controla el comportamiento del módulo de la columna derecha en Propuestas
        if ($(window).width() > 991) {

            $(window).scroll(function() {
                var heightBottom = $('#otras-propuestas').height();
                if ($(window).scrollTop() + $(window).height() > $(document).height() - heightBottom) {
                       $('.boxes.vote.drive').removeClass('fixed');
                } else {
                        $('.boxes.vote.drive').addClass('fixed');
                }
            });

        } else {
            $('.boxes.vote.drive').removeClass('fixed');
        }

    });


    ////////////////////////////////////////////////  EDITAR ////////////////////////////////////////
    // Abre el aviso superior para usuarios semilogados en la Propuesta.
    // Esto hace que se abra cuando le das al botón "Impulsa esta propuesta" dentro del formulario con clase "semilogado" pero hay que programar el caso real en que queremos que se abra.
    $('body').on('click','#drive.semilogado .btn', function(e) {
        e.stopPropagation();
        e.preventDefault();
        $('body').css('padding-top', '134px');
        $('p.warning').fadeIn('slow');
    });
    // botón de cierre del aviso
    $('body').on('click','.warning .close', function(e) {
        e.stopPropagation();
        e.preventDefault();
        $('p.warning').fadeOut('fast');
        $('body').css('padding-top', '55px');
    });

    // cambio de formulario Entrar/Registro
    $('body').on('click','.change-home-register', function(e) {
        e.stopPropagation();
        e.preventDefault();
        $(this).closest('form').fadeOut('fast');
        $(this).closest('form').next('form').fadeIn('slow');
    });

    $('body').on('click','.change-home-login', function(e) {
        e.stopPropagation();
        e.preventDefault();
        $(this).closest('form').fadeOut('fast');
        $(this).closest('form').prev('form').fadeIn('slow');
    });

    // al hacer clic en el botón "Regístrate" de la Home cambio el orden de aparición
    // natural de los formularios
    $('body').on('click','.open-sign-form', function() {
        $('form#login-modal').fadeOut('fast');
        $('form#sign-modal').fadeIn('fast');
    });
    $('body').on('click','.homeMore.two .btn-blue', function(e) {
        $('form#login-modal').fadeIn('fast');
        $('form#sign-modal').fadeOut('fast');
    });

    // Funcionamiento de los radio button como nav-tabs
    $('input[name="cuenta"]').click(function () {
    //jQuery handles UI toggling correctly when we apply "data-target" attributes and call .tab('show')
    //on the <li> elements' immediate children, e.g the <label> elements:
        $(this).closest('label').tab('show');
    });

    // inicializamos la barra de progreso
    $('.progress-bar').progressbar();

    $("#module-card-ipdb-recruitment-hideWarnButton").on("click", function(e){
        e.preventDefault()
        $("#module-card-ipdb-recruitment").fadeOut('fast')
    })

    // desvanecer y eliminar la caja primera del Dashboard (.condition)
    $('body').on('click','aside.condition > .close', function(e) {

        $(this).parent('aside.condition').fadeOut('slow', function(){
          $(this).remove();
        });

    });

    // si el box de Usuarios de la columna C no lleva la X de cierre quito el hueco de la derecha del botón Follow
    if ( !$('.user-list-followers .actions .close').length ) {
        $('.user-list-followers > .user > .actions').css('width', 'auto');
    }

    // desvanecer y eliminar los usuario de la lista "A quién seguir"
    $('body').on('click','ul.user-list-followers > li.user .actions .close', function(e) {

        $(this).closest('li.user').fadeOut('slow', function(){
          $(this).remove();
        });

    });
    $('body').on('click','ul.user-list-followers > li.user:only-child .actions .close', function(e) {

        $(this).closest('.boxes.follow').fadeOut('slow', function(){
          $(this).remove();
        });

    });

    // desvanecer y eliminar la caja que informa de "subida completada" del .pdf en EDICIÓN DE PROYECTO
    $('body').on('click','.progress-complete .close', function(e) {

        $(this).closest('.progress-complete').fadeOut('slow', function(){
          $(this).remove();
        });

    });


    // mostrar/ocultar pass en formulario de password
    $('.show-hide-pass').on('change', function () {
        var div_parent  = $(this).closest('div');
        var input_id    = div_parent.children('input:first').attr('id');

        $('#'+input_id).hideShowPassword($(this).prop('checked'));
    });


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
        $(this).blur()
        moveToHash($(this).attr("href"))
    });
    moveToHash(window.location.hash)

    // setTimeout(prepareProgressBar, 500)
    // prepareProgressBar();  lo he pasado a custom.js

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


    // leer después
    $('body').on('click', '.read-later.logged a', function(e) {
        e.preventDefault();
        e.stopPropagation();
        readLater($(this))
    });
    $('body').on('click', '#postNav .read-later a', function(e) {
        readLater($(this));
        e.preventDefault();
        e.stopPropagation();
    });

    // modal registro
    // $('body').on('click', "[data-toggle='modal'][data-target='#registro']", function(e) {
    //     e.preventDefault();
    //     e.stopPropagation();
    //     $("#registro").modal("show")
    // });

    // Habilitar/deshabilitar link "Marcar como inapropiado"
    $('body').on("click", ".mark a", function(e) {
        e.preventDefault();
        if ( $(this).hasClass('disabled') ){
            $(this).removeClass('disabled');
        } else {
            $(this).addClass('disabled');
        }
    });


    // Activar/desactivar materia que me interesa en el proyecto -> lo dejo comentado porque sólo debe ocurrir cuando estás logado. Falta programar esto.

    /* $('body').on("click", ".icons.subject a", function(e) {
        e.preventDefault();
        e.stopPropagation();
        if ( $(this).hasClass('active') ){
            $(this).removeClass('active');
        } else {
            $(this).addClass('active');
        }
    });*/


    // Activar/desactivar filtros propuestas ciudadanas
    $('body').on("click", ".filters .btn", function(e) {
        e.preventDefault();
        e.stopPropagation();
        if ( $(this).hasClass('active') ){
            $(this).removeClass('active');
        } else {
            $(this).addClass('active');
        }
    });


    // añade la flechita al span de los mensajes de error de los formularios
    if ( $('.error').length > 0 ) {
        $('span.error').prepend('<span class="tooltip-arrow"></span>');
    }

    // votaciones
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


    $('#ver-mas a').click(function(e) {
        e.preventDefault();
        $('.listComments > li').fadeIn('slow');
        $('#ver-mas').hide();
    });


    ///////////////////// EDICIÓN PROPUESTA //////////////////////////

    // countdown textarea bio Editar perfil
    $(function() {
        var totalChars      = parseInt($('#charInitBio span').text());
        var countTextBox    = $('.counted'); // Textarea input box
        var charsCountEl    = $('#charNumBio span'); // Remaining chars count will be displayed here

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


    ///////////////////// EDICIÓN PROPUESTA //////////////////////////

    // change text when select option in the edit post form
    $('#updateText').text($('#typePubli li.active').text());
    $('#selectType').change(function(){
        $('#updateText').text($('#typePubli li').eq(this.selectedIndex).text());
    });


    // countdown textarea edición propuesta
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


    ///////////////////// EDICIÓN PROYECTO //////////////////////////

    // countdown textarea edición proyecto TÍTULO
    $(function() {
        var totalChars      = parseInt($('#charInitTit span').text());
        var countTextBox    = $('#title-project.counted');
        var charsCountEl    = $('#charNumTit span');

        if (countTextBox.length> 0){
            charsCountEl.text(totalChars - countTextBox.val().length);
        }
        countTextBox.keyup(function() {

            var thisChars = this.value.replace(/{.*}/g, '').length;

            if (thisChars > totalChars)
            {
                var CharsToDel = (thisChars-totalChars);
                this.value = this.value.substring(0,this.value.length-CharsToDel);
            } else {
                charsCountEl.text( totalChars - thisChars );
            }
        });
    });

    // countdown textarea edición proyecto HASHTAG
    $(function() {
        var totalChars      = parseInt($('#charInitHash span').text());
        var countTextBox    = $('#hashtag.counted');
        var charsCountEl    = $('#charNumHash span');

        if (countTextBox.length> 0){
            charsCountEl.text(totalChars - countTextBox.val().length);
        }
        countTextBox.keyup(function() {

            var thisChars = this.value.replace(/{.*}/g, '').length;

            if (thisChars > totalChars)
            {
                var CharsToDel = (thisChars-totalChars);
                this.value = this.value.substring(0,this.value.length-CharsToDel);
            } else {
                charsCountEl.text( totalChars - thisChars );
            }
        });
    });

    // countdown textarea edición proyecto DESCRIPCIÓN
    $(function() {
        var totalChars      = parseInt($('#charInitTextProj span').text());
        var countTextBox    = $('#textProject.counted');
        var charsCountEl    = $('#charNumTextProj span');

        if (countTextBox.length> 0){
            charsCountEl.text(totalChars - countTextBox.val().length);
        }
        countTextBox.keyup(function() {

            var thisChars = this.value.replace(/{.*}/g, '').length;

            if (thisChars > totalChars)
            {
                var CharsToDel = (thisChars-totalChars);
                this.value = this.value.substring(0,this.value.length-CharsToDel);
            } else {
                charsCountEl.text( totalChars - thisChars );
            }
        });
    });

    //Tipo de imagen o youtube seleccionado
    $("form [data-fileType]").on("click", function(e){
        $(this).closest("form").find("input[name=fileType]").val($(this).attr("data-fileType"));
    })
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
        p: true,
        placeholder: "Escribe un texto que lo describa",
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

    $(".saveDraft").on("click", function(e){
        e.preventDefault();
        $("input[name=isDraft]").val(true);
        $(this).parents("form").submit();
    })


    // controlando el video de Vimeo en la modal de la Home
    $('.play a').click( function(e) {

        var iframeHome = $('#vimeoplayer')[0];
        var playerHome = $f(iframeHome);

        $("#videoHome").on('hidden.bs.modal', function (e) {
            playerHome.api('pause');
        })
        $("#videoHome").on('shown.bs.modal', function (e) {
            playerHome.api('play');
        })

    });

    // controlando el video de Vimeo en el Embudo1
    $(function () {

        $('.vimeo.uno .front').click( function(e) {
            e.stopPropagation();
            e.preventDefault();
            $(this).next('iframe').css('display', 'block');
            $(this).remove();

            var iframe1 = $('#vimeoplayer1')[0];
            var player1 = $f(iframe1);
            player1.api('play');

        });

        $('.vimeo.dos .front').click( function(e) {
            e.stopPropagation();
            e.preventDefault();
            $(this).next('iframe').css('display', 'block');
            $(this).remove();

            var iframe2 = $('#vimeoplayer2')[0];
            var player2 = $f(iframe2);
            player2.api('play');

        });

        $('.vimeo.tres .front').click( function(e) {
            e.stopPropagation();
            e.preventDefault();
            $(this).next('iframe').css('display', 'block');
            $(this).remove();

            var iframe3 = $('#vimeoplayer3')[0];
            var player3 = $f(iframe3);
            player3.api('play');

        });

    });

    // hacer clic en player falso del video (.front)
    $('.video').find('.front').click( function(e) {
        e.stopPropagation();
        e.preventDefault()
        var iframe = $(this).next('.youtube')
        iframe.css('display', 'block');
//        iframe[0].src += "&autoplay=1";
        $(this).remove();
        var func = 'playVideo';
        iframe.get(0).contentWindow.postMessage('{"event":"command","func":"' + func + '","args":""}', '*');
    });

    prepareForms()
});

function prepareForms(){
    // datepicker calendario
    if ( $('.input-group.date').length > 0 ) {

        $('.input-group.date').datepicker({
            language: "es",
            autoclose: true,
            todayHighlight: true
        });

    }
    $(".counted").each(function(input){
        counterCharacters($(this).attr("name"))
    })
}

function counterCharacters(idField) {
    // idField puede ser ID o name
    var idFieldEscaped = idField.replace('[','\\[').replace(']','\\]').replace('\.','\\.')
    var input = $("[name='"+idFieldEscaped+"']");
    if (input == undefined){
        var input = $("[id='"+idFieldEscaped+"']");
    }
    var totalCharsText = input.parents(".form-group").find("div[id*='charInit']").find("span").text()
    var totalChars      = parseInt(totalCharsText);
    var countTextBox    = input
    var charsCountEl    = input.parents(".form-group").find("div[id*='charNum']").find("span")
    if (countTextBox.length> 0){
        charsCountEl.text(totalChars - countTextBox.val().length);
    }
    countTextBox.keyup(function() {

        var thisChars = this.value.replace(/{.*}/g, '').length;

        if (thisChars > totalChars)
        {
            var CharsToDel = (thisChars-totalChars);
            this.value = this.value.substring(0,this.value.length-CharsToDel);
        } else {
            charsCountEl.text( totalChars - thisChars );
        }
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
    hash = hash + "-tag"
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
        console.log("Moving to "+hash + " offset: "+extraOffset)
        dest = dest + extraOffset
        //go to destination
        $('html,body').animate({
            scrollTop: dest
        }, 1000, 'swing');
    }
}
