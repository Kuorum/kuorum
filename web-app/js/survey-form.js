
$(function() {

    $("#questionsSurveyForm").on("click",".addQuestionOptionButton",function (e) {
        e.preventDefault();
        var $button = $(this);
        var $template = $button.parents(".row.question")
        var $container = $button.parents(".questionOption")
        var questionsId = parseInt($container.find(".question:last input").attr("name").split("]")[1].slice(-1))+1;
        $clone = $template.clone()
        $clone.find("input").each(function(idx, input){
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

    var _isValidSurveyQuestionsForm = function(){
        var valid = $("#questionsSurveyForm").valid();
        $(".questionOption input[type=text]")
            .filter(function() {return $(this).parents('#questionsSurveyForm-template').length < 1;})
            .each(function(idx, input){
            if ($(input).val() == "") {
                var errorMsg = i18n.kuorum.web.commands.payment.massMailing.DebateCommand.body.nullable;
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
    // Abrir modal confirmar envío campaña (SURVEY) programada
    $('body').on('click','.form-final-options #send-campaign-later', function(e) {
        e.preventDefault();
        if (_isValidSurveyQuestionsForm()) {
            $('#saveCampaignBtn').attr('data-callback', 'scheduleParams');
            //$("input[name='sendType']").val("SCHEDULED");
            prepareAndOpenCampaignConfirmModal();
        }
    });

    // Abrir modal confirmar envio de debate
    $('body').on('click','.form-final-options #send-draft', function(e) {
        e.preventDefault();
        if (_isValidSurveyQuestionsForm()) {
            $('#saveCampaignBtn').attr('data-callback', 'sendParams');
            prepareAndOpenCampaignConfirmModal();
        }
    });

});