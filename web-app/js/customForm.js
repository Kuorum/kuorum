/**
 * Kuorum scripts for FORMS
 */

$(function(){
    formHelper.prepareForms();



    // abrir/cerrar calendario
    $('body').on('click','#openCalendar', function(e) {
        e.stopPropagation();
        e.preventDefault();
        if ($('#selectDate').hasClass('on')) {
            $(this).next('#selectDate').removeClass('on');
        } else {
            $(this).next('#selectDate').addClass('on');
        }
    });
    $('body').mouseup(function(e) {
        var subject = $("#selectDate");
        var subject2 = $(".datepicker");
        if(e.target.id != subject.attr('id') && !subject.has(e.target).length && !subject2.has(e.target).length) {
            $('#selectDate').removeClass('on');
        }
    });

    // mostrar/ocultar pass en formulario de password
    $('.show-hide-pass').on('change', function () {
        var div_parent  = $(this).closest('div');
        var input_id    = div_parent.children('input:first').attr('id');

        $('#'+input_id).hideShowPassword($(this).prop('checked'));
    });

    // añade la flechita al span de los mensajes de error de los formularios
    if ( $('.error').length > 0 ) {
        $('span.error').prepend('<span class="tooltip-arrow"></span>');
    }

    //Tipo de imagen o youtube seleccionado
    $("form [data-fileType]").on("click", function(e){
        $(this).closest("form").find("input[name=fileType]").val($(this).attr("data-fileType"));
    });

    // textarea editor
    $.each($(".texteditor"), function(idx, val) {
        var placeholder = $(val).attr("placeholder");
        if (placeholder == undefined){
            placeholder = i18n.form.textEditor.textAreaPlaceHolder
        }
        $(val).jqte({
            br: true,
            center: false,
            color: false,
            format: false,
            indent: false,
            left: false,
            ol: false,
            outdent: false,
            p: true,
            placeholder: $(val).attr("placeholder"),
            linktypes: ["URL", "Email"],
            remove: true,
            right: false,
            rule: false,
            source: false,
            sub: false,
            strike: true,
            sup: false,
            ul: true,
            unlink: true,
            fsize: false,
            title: false,
            // change: function(){ var $form = $(val).parents("form"); console.log($form);if ($form.length>0){$form.addClass("dirty")} }
        });
    });

    $("body").on("click", ".jqte_editor a", function(e){
        e.preventDefault();
        var link = $(this).attr("href");
        window.open(link);
    });

    if ( $('.jqte_editor').text() == "" ) {
        $('.jqte_placeholder_text').css('display', 'block');
    } else {
        $('.jqte_placeholder_text').css('display', 'none');
    }

    $("input[name=userType], input[name=gender]").on("change", function(e){
        formHelper.prepareFormUsingGender($(this).val())
    });
    if ($("input[name=userType]:checked").val() != undefined){
        formHelper.prepareFormUsingGender($("input[name=userType]:checked").val())
    }else{
        formHelper.prepareFormUsingGender("PERSON")
    }

    if ($("input[name=gender]:checked").val() != undefined){
        formHelper.prepareFormUsingGender($("input[name=gender]:checked").val())
    }else{
        formHelper.prepareFormUsingGender("FEMALE")
    }

    $(".dynamicList").dynamiclist();


    $(".input-region").autocomplete({
        paramName:"word",
        params:{country:""},
        serviceUrl:kuorumUrls.suggestRegion,
        minChars:3,
        width:330,
        noCache: false, //default is false, set to true to disable caching
        onSearchStart: function (query) {
//            var realInputId = $(this).attr('data-real-input-id');
//            realInputId = realInputId.replace(".", "\\.")
//            $("#"+realInputId).val("");
        },
        onSearchComplete: function (query, suggestions) {
            $('.loadingSearch').hide()
        },
        formatResult:function (suggestion, currentValue) {
            var format = "";
            if (suggestion.type=="NATION"){
                //AD ICONS DEPENDING ONT REGION TYPE || EXAMPLE COMMENTED
//                format = "<img class='user-img' alt='"+suggestion.data.name+"' src='"+suggestion.data.urlAvatar+"'>"
//                format +="<span class='name'>"+suggestion.data.name+"</span>"
//                format +="<span class='user-type'>"+suggestion.data.role.i18n+"</span>"
            }else{
                format =  suggestion.value
            }
            return format
        },
        searchUserText:function(userText){
            $(this).val(userText)
        },
        onSelect: function(suggestion){
            if(suggestion.type=="NATION"){
                console.log("NATION")
            }
            $(this).val(suggestion.data.name);
            var realInputId = $(this).attr('data-real-input-id');
            realInputId = realInputId.replace(".", "\\.");
            $("#"+realInputId).val(suggestion.data.iso3166_2);
            $("#"+realInputId).valid()
        },
        triggerSelectOnValidInput:false,
        deferRequestBy: 100 //miliseconds
    }).focusout(function(e){
        var realInputId = $(this).attr('data-real-input-id');
        realInputId = realInputId.replace(".", "\\.");
        if ($(this).val().length ===0){
            $("#"+realInputId).val("")
        }
        $("#"+realInputId).valid()
    }).keypress(function(e){
        var realInputId = $(this).attr('data-real-input-id');
        realInputId = realInputId.replace(".", "\\.");
        $("#"+realInputId).val("")
    });


    /* CHECKBOX COMMISSIONS */
    // seleccionar todos los checkbox [COMMISIONS] => _commissions.gsp
    // $(function () {
    //     var checkAll = $('#selectAll');
    //     var checkboxes = $('input.check');
    //
    //     $('input.check').each(function(){
    //         var self = $(this),
    //             label = self.next(),
    //             label_text = label.html();
    //         label.remove();
    //         self.iCheck({
    //             checkboxClass: 'icheckbox_line-orange',
    //             radioClass: 'iradio_line-orange',
    //             inheritID: true,
    //             aria: true,
    //             insert:  label_text
    //         });
    //     });
    //
    //     $('#selectAll').change(function() {
    //         if($(this).is(':checked')) {
    //             checkboxes.iCheck('check');
    //             $('#others').prop('checked', true);
    //         } else {
    //             checkboxes.iCheck('uncheck');
    //             $('#others').prop('checked', false);
    //         }
    //     });
    //
    //     checkAll.on('ifChecked ifUnchecked', function(event) {
    //         if (event.type == 'ifChecked') {
    //             checkboxes.iCheck('check');
    //         } else {
    //             checkboxes.iCheck('uncheck');
    //         }
    //     });
    //
    //     checkboxes.on('ifChanged', function(event){
    //         if(checkboxes.filter(':checked').length == checkboxes.length) {
    //             checkAll.prop('checked', 'checked');
    //         } else {
    //             checkAll.removeProp('checked');
    //         }
    //         checkAll.iCheck('update');
    //     });
    // });


})

var formHelper = {
    dirtyFormControl:{
        prepare:function($form){
            $form.find('[type="submit"]').attr('disabled', 'disabled');
            $form.areYouSure({
                message:i18n.form.warn.leavingEditedForm,
                addRemoveFieldsMarksDirty:true,
                change: function() {
                    // Enable save button only if the form is dirty. i.e. something to save.
                    if ($(this).hasClass('dirty')) {
                        $(this).find('[type="submit"]').removeAttr('disabled');
                    } else {
                        $(this).find('[type="submit"]').attr('disabled', 'disabled');
                    }
                }
            })
        },
        restart:function($form){
            $form.trigger('rescan.areYouSure'); //From plugin areYouSure
        },
        dirty:function($form){
            $form.addClass("dirty")
            $form.trigger('checkform.areYouSure');
            $form.find('[type="submit"]').removeAttr('disabled');
        }
    },
    prepareForms: function(){

        var lang = "en"
        if (typeof i18n !== 'undefined'){
            lang = i18n.lang;
        }
        $.fn.datepicker.defaults.language = lang;

        // datepicker calendario
        if ( $('.input-group.date').length > 0 ) {

            $('.input-group.date').datepicker({
                language: lang,
                autoclose: true,
                todayHighlight: true
            });

        }

        // Datetime piker for all input datestimes
        $('.input-group.datetime').datetimepicker({
            locale: lang,
            format:"DD/MM/YYYY HH:mm",
            //allowInputToggle:true,
            //collapse: false,
            stepping:15,
            showTimeZone:true
        });

        $(".counted").each(function(input){
            formHelper.counterCharacters($(this).attr("name"))
        })
    },

    counterCharacters: function (idField) {
        // idField puede ser ID o name
        var idFieldEscaped = idField.replace('[','\\[').replace(']','\\]').replace('\.','\\.');
        var input = $("[name='"+idFieldEscaped+"']");
        if (input == undefined){
            var input = $("[id='"+idFieldEscaped+"']");
        }
        var totalCharsText = input.parents(".form-group").find("div[id*='charInit']").find("span").text();
        var totalChars      = parseInt(totalCharsText);
        var countTextBox    = input;
        var charsCountEl    = input.parents(".form-group").find("div[id*='charNum']").find("span");
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
    },
    prepareFormUsingGender: function(userType){
    if (userType == "ORGANIZATION"){
        $(".userData").hide();
        $(".politicianData").hide();
        $(".organizationData").show()
    }else if (userType == "POLITICIAN"){
        $(".userData").show();
        $(".politicianData").show();
        $(".organizationData").hide()
    }else{
        $(".userData").show();
        $(".politicianData").hide();
        $(".organizationData").hide()
    }
}

}