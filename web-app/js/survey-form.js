
$(function() {

    $("#questionsSurveyForm").on("click",".addQuestionOptionButton",function (e) {
        e.preventDefault();
        console.log("ADD NEW OPTION")
        var $button = $(this);
        var $container_btn = $button.parents(".questionOptionActions")
        var $container = $container_btn.siblings(".questionOption")
        var $template = $container.find("fieldset.question:first-child")
        var questionsId = parseInt($container.find(".question:last input").attr("name").split("]")[1].split("[")[1])+1;
        $clone = $template.clone()
        $clone.addClass("new-question-option")
        $clone.find("input, select").each(function(idx, input){
            $(input).val("")
            var name = $(input).attr("name")
            if (name != undefined){
                var nameParts = name.split("]")
                var optionPart = nameParts[1].substring(0, nameParts[1].length - 1)
                optionPart = optionPart +questionsId
                var name = nameParts[0]+"]"+optionPart+"]"+nameParts[2]
                $(input).attr("name", name)
            }else{
                console.debug("Cloning inputs without name")
            }
        })
        // Cloning select value -> By Defualt .clone() not clones the input values.
        var $originalSelects = $template.find('select');
        $clone.find('select').each(function(index, item) {
            //set new select to value of old select
            $(item).val( $originalSelects.eq(index).val() );

        });
        $clone.appendTo($container)
        SurveyFormHelper.prepareSortableQuestionOptions();
    });

    $("#questionsSurveyForm").on("click",".reorderQuestionsButton, .endReorderQuestionsButton",function (e) {
        e.preventDefault();
        var $form = $("#questionsSurveyForm")
        var $button = $(this)
        var cssClass="sort-questions"
        if ($form.hasClass(cssClass)){
            // $(document).find(".quesiton-dynamic-fields:not('.hide') .question-options").slideDown()
            $form.removeClass(cssClass)
            $('#questionsSurveyForm').sortable({ disabled: true })
            $button.siblings(".addButton").removeClass("hide")
        }else{
            // $(document).find(".quesiton-dynamic-fields:not('.hide') .question-options").slideUp()
            $form.addClass(cssClass)
            $('#questionsSurveyForm').sortable({ disabled: false })
            $button.siblings(".addButton").addClass("hide")
        }
    });

    $("#questionsSurveyForm").on("click",".removeQuestionButton",function (e) {
        e.preventDefault();
        var $button = $(this);
        var $template = $button.parents(".row.question")
        $template.remove()
    })

    $("#questionsSurveyForm").on("change",".question-type select", function(e){
        SurveyFormHelper.prepareQuestionType($(this))
    });

    $("#questionsSurveyForm").on("change",".question-data-extra .question-data-extra-multi-limit-type select", function(e){
        var $extraDataParent = $(this).parents(".question-data-extra")
        SurveyFormHelper.prepareQuestionExtraDataMultiLimit($extraDataParent)
    });

    SurveyFormHelper.initForm();

    var _isValidSurveyQuestionsForm = function(){
        var valid = $("#questionsSurveyForm").valid();
        $(".questionOption input[type=text]")
            .filter(function() {return $(this).parents('#questionsSurveyForm-template').length < 1;})
            // .filter(function() {return $(this).parents('.form-group').parent().siblings().find(".question-type select").val() != "TEXT_OPTION";})
            // .filter(function() {return $(this).parents('.form-group').parent().siblings().find(".question-type select").val() != "RATING_OPTION";})
            .each(function(idx, input){
            if ($(input).val() == "") {
                var errorMsg = i18n.kuorum.web.commands.payment.survey.QuestionOptionCommand.text.nullable;
                if (!$(input).next().hasClass("error")){
                    $(input).parent().append('<span for="text" class="error"><span class="tooltip-arrow"></span>'+errorMsg+'</span>');
                    $(input).addClass("error");
                }else{
                    $(input).next().css("display","inline-block") // form.validate() hide this
                }
                valid = false;
            } else {
                $(input).next(".error").fadeOut("slow", function(){$(this).remove();});
            }
        })
        return valid;
    }

    // OVERWRITE CUSTOM FORM VALIDATION
    campaignForm.validateCampaignForm = _isValidSurveyQuestionsForm



    SurveyFormHelper.prepareSortableQuestions();
    SurveyFormHelper.prepareSortableQuestionOptions();
});

var SurveyFormHelper ={

    _questionTypeConfigHelper: {
        forceOptionToNumber: function($questionOptionRow){
            var select = $questionOptionRow.find("select");
            select.val("ANSWER_NUMBER");
            select.attr("disabled", "disabled");
        },
        reset:function($questionOptionRow){
            var select = $questionOptionRow.find("select");
            select.removeAttr("disabled", "disabled");
        }
    },
    questionTypeConfig:{
        'ONE_OPTION':               {cssClass:'ONE_OPTION',             showOptions: true,  extraDataVisibleClasses: []},
        'MULTIPLE_OPTION':          {cssClass:'MULTIPLE_OPTION',        showOptions: true,  extraDataVisibleClasses: ["question-data-extra-multi-limit"]},
        'ONE_OPTION_WEIGHTED':      {cssClass:'ONE_OPTION',             showOptions: true,  extraDataVisibleClasses: []},
        'MULTIPLE_OPTION_WEIGHTED': {cssClass:'MULTIPLE_OPTION',        showOptions: true,  extraDataVisibleClasses: ["question-data-extra-multi-limit"],                                       prepareOption:'forceOptionToNumber'},
        'MULTIPLE_OPTION_POINTS':   {cssClass:'MULTIPLE_OPTION',        showOptions: true,  extraDataVisibleClasses: ["question-data-extra-multi-limit", "question-data-extra-multi-points"],   prepareOption:'forceOptionToNumber'},
        'TEXT_OPTION':              {cssClass:'TEXT_OPTION',            showOptions: false, extraDataVisibleClasses: []},
        'RATING_OPTION':            {cssClass:'RATING_OPTION',          showOptions: false, extraDataVisibleClasses: []},
        'CONTACT_UPLOAD_FILES':     {cssClass:'CONTACT_UPLOAD_FILES',   showOptions: false, extraDataVisibleClasses: []},
        'CONTACT_GENDER':           {cssClass:'CONTACT_GENDER',         showOptions: false, extraDataVisibleClasses: []},
        'CONTACT_PHONE':            {cssClass:'CONTACT_PHONE',          showOptions: false, extraDataVisibleClasses: []},
        'CONTACT_EXTERNAL_ID':      {cssClass:'CONTACT_EXTERNAL_ID',    showOptions: false, extraDataVisibleClasses: []},
        'CONTACT_WEIGHT':           {cssClass:'CONTACT_WEIGHT',         showOptions: false, extraDataVisibleClasses: []},
        'CONTACT_BIRTHDATE':        {cssClass:'CONTACT_BIRTHDATE',      showOptions: false, extraDataVisibleClasses: []}
    },

    prepareSortableQuestionOptions: function(){
        $('.dynamic-fieldset:not(.hide) .questionOption').sortable({

            // default options
            accept: '*',
            activeClass: 'sorting-questionOptions',
            cancel: 'input, textarea, button, select, option, .popover, .modal',
            connectWith: false,
            disabled: false,
            forcePlaceholderSize: false,
            initialized: false,
            items: 'fieldset',
            placeholder: 'sortable-placeholder',
            placeholderTag: null,
        }).on('sortable:update', function(e, ui){
            // do somethong
            var $item = $(ui.item);
            var $itemContainer = $item.parent();
            var $form = $item.parents("form");
            $itemContainer.find("fieldset.question").each(function(idx, element){
                var questionOptionPos = idx;
                var re = /(\w+)\[(\d+)\]\.(\w+)\[(\d+)\](.*)/;
                $(element).find("input, select").each(function(idx, input){
                    var $input = $(input);
                    var inputName = $input.attr("name")
                    if (updatedInputName != undefined){
                        var updatedInputName = inputName.replace(re, '$1[$2].$3['+questionOptionPos+']$5');
                        // console.log("Updating name: "+inputName +" to -> "+updatedInputName);
                        $input.attr("name", updatedInputName);
                    }else{
                        console.debug("Updating field without name")
                    }
                })
                formHelper.dirtyFormControl.dirty($form)
            });
        }).on('sortable:start', function(e, ui){
            // do somethong
            $(ui.item).addClass("item-sorting")
        }).on('sortable:stop', function(e, ui){
            // do somethong
            $(ui.item).removeClass("item-sorting")

        });
    },
    prepareSortableQuestions: function(){

        $('#questionsSurveyForm').sortable({

            // default options
            accept: '*',
            activeClass: 'sorting-questions',
            cancel: 'input, textarea, button, select, option',
            connectWith: false,
            disabled: true,
            forcePlaceholderSize: false,
            handle: false,
            initialized: false,
            items: '.quesiton-dynamic-fields:not(.hide)',
            placeholder: 'sortable-placeholder',
            placeholderTag: null,
            Handler: null,
            receiveHandler: null
        }).on('sortable:update', function(e, ui){

            var $item = $(ui.item);
            var $form = $item.parent();
            $form.find(".quesiton-dynamic-fields:not(.hide)").each(function(idx, questionContainer){
                var questionPos = idx;
                var re = /(\w+)\[(\d+)\](.*)/;
                $(questionContainer).find("input, select").each(function(idx, input){
                    var $input = $(input);
                    var inputName = $input.attr("name")
                    var updatedInputName = inputName.replace(re, '$1['+questionPos+']$3');
                    // console.log("Updating name: "+inputName +" to -> "+updatedInputName);
                    $input.attr("name", updatedInputName);
                })
            })
            formHelper.dirtyFormControl.dirty($form);
        }).on('sortable:start', function(e, ui){
            console.log($(ui.item).parents("form").find(".quesiton-dynamic-fields:not('.hide') .question-options"))
            // $(ui.item).parents("form").find(".quesiton-dynamic-fields:not('.hide') .question-options").slideUp();
        }).on('sortable:stop', function(e, ui){
            // do somethong
            // $(ui.item).parents("form").find(".quesiton-dynamic-fields:not('.hide') .question-options").slideDown();

        });
    },

    // HIDE OPTIONS OF TEXT QUESTIONS
    _hideOptions: function($selectQuestionType){
        var $fieldsetOptions = $selectQuestionType.closest("fieldset.row").siblings();
        var newVal = $selectQuestionType.val();
        var configType = SurveyFormHelper.questionTypeConfig[newVal]
        if (configType.showOptions){
            $fieldsetOptions.slideDown()
        }else{
            $fieldsetOptions.slideUp( "slow", function(){
                // CHAPU RELLENA INPUTS despues de ocultar
                $fieldsetOptions.find("input[type=text]").each(function(idx){if ($(this).val()==""){$(this).val(idx)}});
            })
        }
    },
    initQuestionOptions:function(){
        // HIDE OPTIONS OF TEXT QUESTIONS on init page
        var questionTypeSelectors = $("#questionsSurveyForm .question-type select");
        var questionTypeSelectorsIdx;
        for (questionTypeSelectorsIdx = 0; questionTypeSelectorsIdx< questionTypeSelectors.length; questionTypeSelectorsIdx++){
            SurveyFormHelper.prepareQuestionType($(questionTypeSelectors[questionTypeSelectorsIdx]));
        }
        // END HIDE OPTION OF TEXT QUESTIONS
    },

    _addClassTypeToQuestion:function($selectQuestionType){
        var questionType = $selectQuestionType.val();
        var $questionContainer = $selectQuestionType.parents(".quesiton-dynamic-fields");
        // console.log($questionContainer)
        Object.keys(SurveyFormHelper.questionTypeConfig).forEach(function (typeId) {
            $questionContainer.removeClass(SurveyFormHelper.questionTypeConfig[typeId].cssClass);
        });
        $questionContainer.addClass(SurveyFormHelper.questionTypeConfig[questionType].cssClass);
    },

    prepareQuestionType:function($selectQuestionType){
        SurveyFormHelper._hideOptions($selectQuestionType);
        SurveyFormHelper._toggleQuestionExtraData($selectQuestionType);
        SurveyFormHelper._addClassTypeToQuestion($selectQuestionType);
        SurveyFormHelper._prepareQuestionOptionsDependingOnType($selectQuestionType);
        SurveyFormHelper._prepareQuestionTypeInfo($selectQuestionType);
    },
    _prepareQuestionOptionsDependingOnType:function($selectQuestionType){
        var questionType = $selectQuestionType.val();
        var questionTypeConf = SurveyFormHelper.questionTypeConfig[questionType];
        var $questionOptionsContainer = $selectQuestionType.parents("fieldset.question-data").siblings(".question-options")
        console.log($questionOptionsContainer)
        $questionOptionsContainer.find("fieldset.row.question").each(function(i, questionOption){
            if (questionTypeConf.prepareOption != undefined){
                SurveyFormHelper._questionTypeConfigHelper[questionTypeConf.prepareOption]($(questionOption));
            }else{
                SurveyFormHelper._questionTypeConfigHelper.reset($(questionOption))
            }
        })
    },
    _prepareQuestionTypeInfo:function($selectQuestionType){
        var questionType = $selectQuestionType.val();
        var $questionTypeInfoContainer = $selectQuestionType.parents(".question-type").siblings(".question-type-info");
        var msg = i18n.org.kuorum.rest.model.communication.survey.QuestionTypeRSDTO[questionType].info;
        $questionTypeInfoContainer.children("span").attr("data-original-title", msg)

    },
    _toggleQuestionExtraData:function($selectQuestionType){
        var questionType = $selectQuestionType.val();
        var $extraDataMulti = $selectQuestionType.parents("fieldset.question-data").find(".question-data-extra")
        var questionTypeConf = SurveyFormHelper.questionTypeConfig[questionType];
        if (questionTypeConf.extraDataVisibleClasses.length>0){
            SurveyFormHelper.prepareQuestionExtraData($extraDataMulti, questionTypeConf);
            $extraDataMulti.show();
        }else{
            $extraDataMulti.hide();
        }
    },

    prepareQuestionExtraData:function($extraDataMulti, questionTypeConf){
        if (questionTypeConf != undefined){
            // Defined question type -> Changing visibility of associated inputs
            $extraDataMulti.children("div").hide();
            for (var i = 0; i < questionTypeConf.extraDataVisibleClasses.length; i++) {
                var visibleClass = questionTypeConf.extraDataVisibleClasses[i];
                console.log(visibleClass);
                $extraDataMulti.children("div."+visibleClass).show();
            }
        }
        SurveyFormHelper.prepareQuestionExtraDataMultiLimit($extraDataMulti);
    },
    prepareQuestionExtraDataMultiLimit:function($extraDataMulti){
        var questionLimitTypeVal= $extraDataMulti.find("#questionLimitAnswersType").val()
        if (questionLimitTypeVal == "MIN"){
            $extraDataMulti.find(".question-data-exta-multi-limit-min").show();
            $extraDataMulti.find(".question-data-exta-multi-limit-max").hide();
        }else if(questionLimitTypeVal == "MAX"){
            $extraDataMulti.find(".question-data-exta-multi-limit-min").hide();
            $extraDataMulti.find(".question-data-exta-multi-limit-max").show();
        }else if(questionLimitTypeVal == "RANGE"){
            $extraDataMulti.find(".question-data-exta-multi-limit-min").show();
            $extraDataMulti.find(".question-data-exta-multi-limit-max").show();
        }else if(questionLimitTypeVal == "FORCE"){
            $extraDataMulti.find(".question-data-exta-multi-limit-min").hide();
            $extraDataMulti.find(".question-data-exta-multi-limit-max").show();
        }
    },

    initForm:function(){
        SurveyFormHelper.initQuestionOptions();
        $("form .dynamic-fieldset-addbutton button.addButton").on("kuorum.dynamicInput.add",function( event, formId ){
            prepareTooltips();
        });
    }
}