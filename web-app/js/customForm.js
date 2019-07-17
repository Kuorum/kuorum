/**
 * Kuorum scripts for FORMS
 */

$(function(){
    formHelper.prepareForms();



    // abrir/cerrar calendario
    $('body').on('click','#openCalendar', function(e) {
        console.log("OPEN CALENDAR")
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
            change: function(){ var $form = $(val).parents("form");if ($form.length>0){formHelper.dirtyFormControl.dirty($form);} }
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
            $("#"+realInputId).val(suggestion.data.iso3166);
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

    /* PRETTY CHECKBOX */
    // $(".pretty-check-box input").on("change", function(e){
    //     if (this.checked){
    //         $(this).attr("checked","checked")
    //     }else{
    //         $(this).attr("checked","")
    //     }
    // })


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

            $('.input-group.date').each(function(){
                if ($(this).attr("data-datePicker-type")=="birthDate"){
                    var birthDay = $(this).find("input").val()
                    var divId = guid();
                    var inputName = $(this).find("input").attr("name")
                    $(this).html("");
                    $(this).attr("id", divId)
                    $(this).birthdayPicker({
                            maxAge: 100,
                            minAge: 0,
                        dateFormat: 'littleEndian',
                        monthFormat: "long",
                        placeholder: true,
                        defaultDate: birthDay,
                        sizeClass: "birthday-select",
                        inputName:inputName,
                        language:i18n.lang
                    });
                }else{
                    var startView = "days"; // "decades"
                    $(this).datepicker({
                        language: lang,
                        autoclose: true,
                        todayHighlight: true,
                        startView: startView
                    });
                }

            })
        }

        // Datetime piker for all input datestimes
        $('.input-group.datetime').datetimepicker({
            locale: lang,
            format:"DD/MM/YYYY HH:mm",
            //allowInputToggle:true,
            //collapse: false,
            allowInputToggle:true,
            stepping:15,
            showTimeZone:true,
            keepOpen:true
        });

        $(".counted").each(function(input){
            formHelper.counterCharacters($(this).attr("name"))
        })

        prepareAutocompleteTags();
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


var tagsnames
function prepareAutocompleteTags(){
    // input tags
    if ($('.tagsField').length) {

        $.each($('.tagsField'),function(i, input){
            var tagsUrl = 'mock/tags.json';
            if ($(input).attr("data-urlTags") != undefined){
                tagsUrl=$(input).attr("data-urlTags");
            }
            if (tagsnames == undefined){
                tagsnames = new Bloodhound({
                    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('name'),
                    queryTokenizer: Bloodhound.tokenizers.whitespace,
                    prefetch: {
                        url: tagsUrl,
                        cache:false, //Prevents local storage
                        filter: function(list) {
                            return $.map(list, function(tagsname) {
                                return { name: tagsname }; });
                        }
                    }
                });
                tagsnames.initialize();
            }

            $(input).tagsinput({
                allowDuplicates: false,
                freeInput: true,
                addOnBlur: true,
                typeaheadjs: {
                    minLength: 2,
                    hint: true,
                    highlight: true,
                    name: 'tagsnames',
                    displayKey: 'name',
                    valueKey: 'name',
                    source: tagsnames.ttAdapter()
                }
            });
            $(input).siblings("#inputAddTags").on("click", function(e){
                console.log("Click")
            })
        });

        // Add tags when focusout
        $(".bootstrap-tagsinput input").on('focusout', function() {
            var elem = $(this).closest(".bootstrap-tagsinput").parent().children("input.tagsField");
            elem.tagsinput('add', $(this).val());
            $(this).typeahead('val', '');
        });
    }
}