$(function () {

    $("#survey-call-to-action, .leader-post .footer .comment-counter button").on("click", function(e){
        e.preventDefault()
        moveToHash($(this).attr("data-goto"))
    })


    var optionsNextButton = document.querySelectorAll('.survey-question.single-answer .next-section button');
    var multiOptionsNextButton = document.querySelectorAll('.survey-question.multi-answer .next-section button');

    // Add click listener on answers that aren't answered
    $('.survey-question-answer')
        .filter(function() {return $(this).parents('.answered').length === 0})
        .on("click",surveyFunctions._selectAnswer)

    optionsNextButton.forEach(function(nextButton) {
        nextButton.addEventListener('click', surveyFunctions._nextButtonClick);
    });

    multiOptionsNextButton.forEach(function(nextButton) {
        nextButton.addEventListener('click', surveyFunctions._nextButtonClick);
    });

    $(".survey-question.single-answer .actions a").on("click",function (e) {
        e.preventDefault();
        var question = e.currentTarget.parentElement.parentElement.parentElement;
        var questionId= parseInt(question.getAttribute('data-question-id'), 10);
        surveyFunctions._setProgressBarsPercentOneOption(questionId);
        surveyFunctions._nextQuestion(questionId);
    });
    $(".survey-question.multi-answer .actions a").on("click", function (e) {
        e.preventDefault();
        var question = e.currentTarget.parentElement.parentElement.parentElement;
        var questionId= parseInt(question.getAttribute('data-question-id'), 10);
        surveyFunctions._setProgressBarsPercentMultiOptions(question);
        surveyFunctions._nextQuestion(questionId);
    });

    surveyFunctions._updateSurveyProgressBar();
    document.querySelectorAll(".survey-question.single-answer.answered").forEach(function(question){
        var questionId = parseInt(question.getAttribute('data-question-id'))
        surveyFunctions._setProgressBarsPercentOneOption(questionId)
    });
    document.querySelectorAll(".survey-question.multi-answer.answered").forEach(function(question){
        surveyFunctions._setProgressBarsPercentMultiOptions(question)
    });
})

var surveyFunctions = {
    NO_LOGGED_CALLBACK:"addAnwerNoLogged",
    NO_LOGGED_CALLBACK_QUESTION_VAR_NAME:"data-questionId",
    relodAfeterSubmit: false,
    _nextButtonClick : function(e) {
        e.preventDefault();
        var $buttonNext = $(e.target);
        var alias = $buttonNext.attr("data-userLoggedAlias");
        var question = e.currentTarget.parentElement.parentElement.parentElement;
        if (alias == "") {
            // USER NO LOGGED
            var buttonId = guid();
            $buttonNext.attr("id", buttonId);
            $form = $buttonNext.parents("form")
            $form.removeClass("dirty");
            var questionId = question.getAttribute("data-question-id")
            // $form.attr(surveyFunctions.NO_LOGGED_CALLBACK_QUESTION_VAR_NAME, questionId)
            // noLoggedRememberPasswordCallbacks.publishProposal.saveState($buttonPublish);
            $('#registro').find("form").attr("callback", surveyFunctions.NO_LOGGED_CALLBACK);
            $('#registro').find("form").attr(surveyFunctions.NO_LOGGED_CALLBACK_QUESTION_VAR_NAME, questionId);
            $('#registro').modal('show');
        } else {
            surveyFunctions._nextButtonClickSelector(question)
        }
    },
    _nextButtonClickSelector:function (question) {
        if (question.classList.contains("multi-answer")){
            surveyFunctions._multiOptionNextButtonClick(question)
        }else{
            surveyFunctions._singleOptionNextButtonClick(question)
        }
    },
    _singleOptionNextButtonClick : function(question){
        // event.preventDefault();
        // var question = event.currentTarget.parentElement.parentElement.parentElement;
        var answers = question.getElementsByClassName('survey-question-answers')[0];

        var selectedAnswer = question.getAttribute('data-answer-selected');

        var answer = answers.querySelector('[data-answer-id="' + selectedAnswer + '"]');
        var options = question.querySelectorAll('.survey-question-answer'); // Html collection to array
        if (selectedAnswer) {
            options.forEach(function(option) {
                $(option).off('click',surveyFunctions._selectAnswer);
            });
            // UPDATE NUM ANSWERS
            answer.setAttribute("data-numanswers", parseInt(answer.getAttribute("data-numanswers"))+1);
            question.setAttribute("data-numanswers", parseInt(question.getAttribute("data-numanswers"))+1);

            surveyFunctions._setProgressBarsPercentOneOption(parseInt(question.getAttribute('data-question-id')));
            var questionId = parseInt(question.getAttribute('data-question-id'), 10);
            surveyFunctions._sendQuestionAnswers(questionId)
            surveyFunctions._nextQuestion(questionId);
        }
    },

    _multiOptionNextButtonClick : function(question){
        // var question = event.currentTarget.parentElement.parentElement.parentElement;
        var options = question.querySelectorAll('.survey-question-answer'); // Html collection to array
        var selectedAnswers = question.getAttribute('data-answer-selected');

        selectedAnswers = JSON.parse(selectedAnswers);
        if (!!selectedAnswers) {
            options.forEach(function(option) {
                $(option).off('click',surveyFunctions._selectAnswer);
            });

            // Updating num answers
            var numQuestionAnswers = parseInt(question.getAttribute("data-numAnswers"));
            numQuestionAnswers= numQuestionAnswers+1;
            question.setAttribute("data-numAnswers",numQuestionAnswers);

            surveyFunctions._setProgressBarsPercentMultiOptions(question);
            var questionId = parseInt(question.getAttribute('data-question-id'), 10);
            surveyFunctions._sendQuestionAnswers(questionId)
            surveyFunctions._nextQuestion(questionId);
        }
    },

    _nextQuestion : function(questionId) {
        var currentQuestion = document.querySelector('.survey-question[data-question-id="' + questionId + '"]');
        currentQuestion.classList.add('answered');
        // var nextQuestion = $(currentQuestion).next();
        // SLIDE DOWN DONE WITH CSS
        // if (!!nextQuestion === true) {
        //     // $(nextQuestion).css("display","none");
        //     $(nextQuestion).slideDown("slow");
        // }
        surveyFunctions._updateSurveyProgressBar();
    },

    _updateSurveyProgressBar : function(){
        // GLOBAL PROGRESS
        var numberQuestions = $(".survey-question").length;
        var numberQuestionsAnswered = $(".survey-question.answered").length;
        var surveyPos = document.getElementById('survey-pos');
        var surveyTotal = document.getElementById('survey-total');
        var progressBarSurveyCounter = document.getElementById('progress-bar-survey-counter');
        progressBarSurveyCounter.style.width = (numberQuestionsAnswered*100 / numberQuestions) + '%';
        surveyPos.textContent = numberQuestionsAnswered.toString();
        surveyTotal.textContent = numberQuestions.toString();
    },

    _setProgressBarsPercentOneOption:function(questionId){
        var question = document.querySelector('[data-question-id="' + questionId + '"]');
        var numAnswers = parseInt(question.getAttribute("data-numAnswers"))
        var answerOptions = question.getElementsByClassName('survey-question-answer')
        Array.from(answerOptions).forEach(function(answerOption) {
            var numOptionAnswers = parseInt(answerOption.getAttribute("data-numAnswers"))
            var percentageOptionProgressBar = 0;
            if (numAnswers > 0){
                percentageOptionProgressBar = (numOptionAnswers / numAnswers * 100)
                percentageOptionProgressBar = Math.round(percentageOptionProgressBar)
            }
            answerOption.getElementsByClassName("progress-bar-counter")[0].textContent=percentageOptionProgressBar +"%";
            var optionProgressBar = answerOption.getElementsByClassName("progress-bar")[0]
            optionProgressBar.style.width=percentageOptionProgressBar+"%"
            optionProgressBar.setAttribute("aria-valuenow",percentageOptionProgressBar)
            optionProgressBar.setAttribute("data-answer-percent",percentageOptionProgressBar)
            optionProgressBar.setAttribute("data-answer-percent-selected",percentageOptionProgressBar)
        })
    },

    _selectAnswer:function(e){
        var answer = e.currentTarget
        if ($(answer).find(".option").length >0){
            surveyFunctions._selectSingleAnswer(e)
        }else if ($(answer).find(".multi-option").length >0){
            surveyFunctions._selectMultiAnswer(e)
        }
    },

    _selectSingleAnswer : function (event) {
        var answer = event.currentTarget;
        var answerList = answer.parentElement;
        var question = answer.parentElement.parentElement;
        var nextButton = answerList.nextElementSibling.querySelector('.next-section button');

        $(answerList).find(".survey-question-answer").removeClass('checked');

        $(nextButton).removeClass('disabled');
        $(answer).addClass('checked');
        question.setAttribute('data-answer-selected', answer.getAttribute('data-answer-id'));
    },

    _selectMultiAnswer : function (event) {
        var answer = event.currentTarget;
        // var answerVotes = answer.querySelector('.progress-bar-counter');
        var answersList = answer.parentElement;
        var question = answersList.parentElement;

        var selectedAnswers = (question.getAttribute('data-answer-selected') !== "") ? JSON.parse(question.getAttribute('data-answer-selected')) : "";
        var nextButton = answersList.nextElementSibling.querySelector('.next-section button');

        var numOptionAnswers = parseInt(answer.getAttribute("data-numAnswers"))
        if (!!selectedAnswers === true && Array.isArray(selectedAnswers)) {
            var answerPosition = selectedAnswers.indexOf(answer.getAttribute('data-answer-id'));
            if (answerPosition === -1) {
                selectedAnswers.push(answer.getAttribute('data-answer-id'));
                $(nextButton).removeClass('disabled');
                $(answer).addClass('checked');
                numOptionAnswers = numOptionAnswers+1;
            } else {
                selectedAnswers.splice(answerPosition, 1);
                $(answer).removeClass('checked');
                numOptionAnswers = numOptionAnswers -1;
            }
        } else {
            selectedAnswers = [answer.getAttribute('data-answer-id')];
            $(nextButton).removeClass('disabled');
            numOptionAnswers = numOptionAnswers +1;
            $(answer).addClass('checked');
        }
        answer.setAttribute("data-numAnswers",numOptionAnswers)
        question.setAttribute('data-answer-selected',  (selectedAnswers.length > 0) ? JSON.stringify(selectedAnswers) : '');

        if (selectedAnswers.length === 0) {
            $(nextButton).addClass('disabled');
        }
    },

    _setProgressBarsPercentMultiOptions : function(question) {
        var answers = question.getElementsByClassName('survey-question-answers')[0];
        var progressBars = answers.getElementsByClassName('progress');

        var numQuestionAnswers = parseInt(question.getAttribute("data-numAnswers"));
        var arr = [].slice.call(progressBars);
        arr.forEach(function(progress) {
            var answer = progress.parentElement.parentElement;
            var numOptionAnswers = parseInt(answer.getAttribute("data-numAnswers"))
            // var selectedAnswers = (question.getAttribute('data-answer-selected') !== "") ? JSON.parse(question.getAttribute('data-answer-selected')) : "";
            // var answerPosition = selectedAnswers.indexOf(answer.getAttribute('data-answer-id'));
            var progressBar = progress.children[0];
            var progressBarCounter = answer.querySelector('.progress-bar-counter');


            if (numQuestionAnswers > 0 ){
                progressBar.style.width = Math.round(numOptionAnswers/numQuestionAnswers*100)+ '%';
                progressBarCounter.textContent = Math.round(numOptionAnswers/numQuestionAnswers * 100) + '%';
                // progressBarCounter.textContent = numOptionAnswers +"/"+numQuestionAnswers;
            }else{
                progressBar.style.width = '0%';
                // progressBarCounter.textContent = "0/0";
                progressBarCounter.textContent = "0%";
            }
        });
    },

    _sendQuestionAnswers:function(questionId){
        var question = document.querySelector('.survey-question[data-question-id="' + questionId + '"]');
        var button = question.querySelector('.actions button')
        var url = button.getAttribute("data-postUrl")
        var answerIds = JSON.parse(question.getAttribute("data-answer-selected"))
        if (!Array.isArray(answerIds)){
            answerIds = [answerIds]
        }
        var data = {
            // surveyId:10,
            questionId:questionId,
            answersIds:answerIds
        }
        moveToHash("#survey-progress")
        $.ajax({
            type: "POST",
            url: url,
            data: $.param(data, true),
            success: function(data){
                // console.log("Succes"+data)
                if (surveyFunctions.relodAfeterSubmit){
                    // Chapu para recargar la pagina sin pasar el callback desde mordor
                    noLoggedCallbacks.reloadPage();
                }
            },
            dataType: 'json'
        }).fail(function(error) {
            // console.log(error)
            // display.warn( "error" );
        }).always(function() {

        });
    }
}

noLoggedCallbacks[surveyFunctions.NO_LOGGED_CALLBACK] = function(){
    pageLoadingOn();
    var questionId = $('#registro').find("form").attr(surveyFunctions.NO_LOGGED_CALLBACK_QUESTION_VAR_NAME);
    var question = document.querySelector('.survey-question[data-question-id="' + questionId + '"]');
    surveyFunctions.relodAfeterSubmit = true;
    surveyFunctions._nextButtonClickSelector(question)
};