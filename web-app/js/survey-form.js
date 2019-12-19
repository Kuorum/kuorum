
$(function() {

    $("#questionsSurveyForm").on("click",".addQuestionOptionButton",function (e) {
        e.preventDefault();
        var $button = $(this);
        var $template = $button.parents(".row.question")
        var $container = $button.parents(".questionOption")
        var questionsId = parseInt($container.find(".question:last input").attr("name").split("]")[1].slice(-1))+1;
        $clone = $template.clone()
        $clone.find("input, select").each(function(idx, input){
            $(input).val("")
            var name = $(input).attr("name")
            var nameParts = name.split("]")
            var optionPart = nameParts[1].substring(0, nameParts[1].length - 1)
            optionPart = optionPart +questionsId
            var name = nameParts[0]+"]"+optionPart+"]"+nameParts[2]
            $(input).attr("name", name)
        })
        // $template.after($clone)
        $clone.appendTo($container)
    })
    $("#questionsSurveyForm").on("click",".removeQuestionButton",function (e) {
        e.preventDefault();
        var $button = $(this);
        var $template = $button.parents(".row.question")
        $template.remove()
    })


    // HIDE OPTIONS OF TEXT QUESTIONS
    var _hideOptions = function($selectQuestionType){
        var $fieldsetOptions = $selectQuestionType.closest("fieldset.row").siblings();
        var newVal = $selectQuestionType.val();
        if (newVal == "TEXT_OPTION" || newVal == "RATING_OPTION"){
            $fieldsetOptions.slideUp()
        }else{
            $fieldsetOptions.slideDown()
        }
    }

    $("#questionsSurveyForm").on("change",".question-type select", function(e){
        _hideOptions($(this))
    });

    // HIDE OPTIONS OF TEXT QUESTIONS on init page
    var questionTypeSelectors = $("#questionsSurveyForm .question-type select");
    var questionTypeSelectorsIdx;
    for (questionTypeSelectorsIdx = 0; questionTypeSelectorsIdx< questionTypeSelectors.length; questionTypeSelectorsIdx++){
        _hideOptions($(questionTypeSelectors[questionTypeSelectorsIdx]));
    }
    // END HIDE OPTION OF TEXT QUESTIONS

    var _isValidSurveyQuestionsForm = function(){
        var valid = $("#questionsSurveyForm").valid();
        $(".questionOption input[type=text]")
            .filter(function() {return $(this).parents('#questionsSurveyForm-template').length < 1;})
            .filter(function() {return $(this).parents('.form-group').parent().siblings().find(".question-type select").val() != "TEXT_OPTION";})
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
});