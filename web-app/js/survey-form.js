
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

});