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
        $(this).css("height", width / 1.77777778);
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

    // isotope - plugin para apilar divs de diferente altura
    if ( $('.list-team').length > 0 ) {

        var $container = $('.list-team');
        // init
        $container.isotope({
          // options
          itemSelector: '.list-team > li'
        });

    }

    if ( $('.list-updates').length > 0 ) {

        var $container = $('.list-updates');
        // init
        $container.isotope({
          // options
          itemSelector: '.list-updates > li'
        });

    }



    // js que se ejecuta según width
    /*$(function() {

        var min_width;
        if (Modernizr.mq('(min-width: 0px)')) {
          // Browsers that support media queries
          min_width = function (width) {
            return Modernizr.mq('(min-width: ' + width + 'px)');
          };
        }
        else {
          // Fallback for browsers that does not support media queries
          min_width = function (width) {
            return $(window).width() >= width;
          };
        }

        var resize = function() {
          if (min_width(991)) {

            // si estamos en Propuesta, controlo la posición fixed del módulo impular/apadrinar
            $(window).scroll(function() {
                var heightBottom = $('#otras-propuestas').height();
                if($(window).scrollTop() + $(window).height() > $(document).height() - heightBottom) {
                   $('.boxes.vote.drive').css('position', 'relative');
                } else {
                    $('.boxes.vote.drive').css('position', 'fixed');
                }
            });

          }
        };

        $(window).resize(resize);
        resize();
    });*/



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

    // al hacer clic en el botón "Regístrate" de la Home cambio el orden de aparición natural de los formularios
    $('body').on('click','.open-sign-form', function(e) {
        $('form#login-modal').fadeOut('fast');
        $('form#sign-modal').fadeIn('fast');
    });


    // Funcionamiento de los radio button como nav-tabs
    $('input[name="cuenta"]').click(function () {
    //jQuery handles UI toggling correctly when we apply "data-target" attributes and call .tab('show')
    //on the <li> elements' immediate children, e.g the <label> elements:
        $(this).closest('label').tab('show');
    });


    // inicializamos la barra de progreso
    $('.progress-bar').progressbar();

    // desvanecer y eliminar la caja primera del Dashboard (.condition)
    $('body').on('click','aside.condition > .close', function(e) {

        $(this).parent('aside.condition').fadeOut('slow', function(){
          $(this).remove();
        });

    });


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


    // mostrar/ocultar pass en formulario de Entrar
    $('#show-pass-header').on('change', function () {
      $('#pass-header').hideShowPassword($(this).prop('checked'));
    });
    $('#show-pass-modal').on('change', function () {
      $('#pass-modal').hideShowPassword($(this).prop('checked'));
    });
    $('#show-pass-home').on('change', function () {
      $('#pass-home').hideShowPassword($(this).prop('checked'));
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

    })

    prepareArrowClucks();


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
    $('body').on('click', '.read-later a', function(e) {
        e.preventDefault();
        e.stopPropagation();
        readLater($(this))

    });
    $('body').on('click', '#postNav .read-later a', function(e) {
        readLater($(this));
        e.preventDefault();
        e.stopPropagation();
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


    // Activar/desactivar materia que me interesa en el proyecto -> lo dejo comentado porque sólo debe ocurrir cuando estás logado. Falta programar esto.

/*    $('body').on("click", ".icons.subject a", function(e) {
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


    // Deshabilitar enlaces números (descubre)
    $('.introDiscover .steps .active').find('a').addClass('disabled');
    $('body').on("click", ".introDiscover .steps .active a", function(e) {
        e.preventDefault();
    });


    // Si voto desaparecen los botones y aparece el enlace de cambio de opinión
    /*$('body').on("click", ".voting li a", function(e) {
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
    });*/

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
    /*$('body').on("click", ".changeOpinion", function(e) {
        e.preventDefault();
        var lawId = $(this).parents("section").attr("data-lawId")
        $('section[data-lawId='+lawId+'] .activity li').removeClass('active');
        $(this).css('display', 'none');
        $('section[data-lawId='+lawId+']  .voting ul').css('display', 'block');
    });
*/
    // Buscador: cambia el placeholder según el filtro elegido -> YA NO HAY
/*    $(function() {

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

    });*/


    //search filters -> YA NO HAY
    /*$('#searchFilters input:checked').css('display','block');
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

    }*/

    // el enlace callMobile (visible sólo en pantallas de hasta 767px, desaparece si le haces click o si llegas a la votación
    /*if ( $('#vote').length > 0 ) {

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

    }*/

    // si boxes lleva foto pongo padding superior
    /*if ( $('.boxes.noted.likes.important').children('img.actor').length > 0 ) {
        $('.boxes.noted.likes.important').css('padding-top', '275px');

    }*/


    // oculta los comentarios
//  $('.listComments > li:gt(2)').hide();
    $('#ver-mas a').click(function(e) {
        e.preventDefault();
//      $('.listComments > li:gt(2)').fadeIn('slow');
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


    // datepicker calendario
    if ( $('.input-group.date').length > 0 ) {

        $('.input-group.date').datepicker({
            language: "es",
            autoclose: true,
            todayHighlight: true
        });

    }


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

    // hacer visible la contraseña
    /*$('#show-pass').attr('checked', false);

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
    });*/

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


    // seleccionar todos los checkbox
    $(function () {

        // ESTO HAY QUE DESCOMENTARLO AL INTEGRAR **************************************************** //
        // changeDescriptionNumSelect()

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

            // ESTO HAY QUE DESCOMENTARLO AL INTEGRAR **************************************************** //
            // changeDescriptionNumSelect();
        });
    });


    // seleccionar todos los checkbox en configuración
    $(function () {
        $('.allActivityMails').change(function() {
            var formGroup = $(this).parents(".form-group")
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


    $(".multimedia .groupRadio input[type=radio]").on('click', function(e){
        var multimediaType = $(this).val()
        $('[data-multimedia-switch="on"]').hide()
        $('[data-multimedia-type="'+multimediaType+'"]').show()

    })


    // hacer clic en player falso del video (.front)
    $('.video').find('.front').click( function(e) {
        e.stopPropagation();
        e.preventDefault();
        var iframe = $(this).next('.youtube');
        iframe.css('display', 'block');
        iframe[0].src += "&autoplay=1";
        $(this).remove();
    });


    $('body').on("click", ".openModalVictory",function(e){
        var notificationId = $(this).attr("data-notification-id")
        modalVictory.openModal(notificationId)
    });

    $('body').on("click", ".openModalDefender",function(e){
        e.preventDefault()
        var postId = $(this).attr("data-postId")
        modalDeafend.openModal(postId)
    });

    $('.modalVictoryClose').on("click", function (e) {
        e.preventDefault()
        $('#modalVictory').modal('hide');
    });

    $('.modalVictoryAction').on("click", function (e) {
        e.preventDefault()
        var notificationId = $(this).attr("data-notificationId")
        var victoryOk = $(this).attr("data-victoryOk")
        var link = $(this).attr("href")
        $.ajax({
            url:link,
            data:{victoryOk:victoryOk}
        }).done(function(data){
            $('#modalVictory').modal('hide');
            modalVictory.hideNotificationActions(notificationId);
            display.success(data)
        })
    });

});



// ***** End jQuey Init *********** //

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
        $("#modalVictory .modal-footer a").attr('href',modalData.post.victoryLink)
        $("#modalVictory .modal-footer a").attr('data-notificationId',notificationId)
    },
    hideNotificationActions:function(notificationId){
        $("[data-notification-id="+notificationId+"]").hide();
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

$(document).ajaxStop(function () {

    // inicia el timeago
    $("time.timeago").timeago();

    prepareArrowClucks();
    preparePopover();

});