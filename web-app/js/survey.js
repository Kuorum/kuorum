$(function () {

    $("#survey-call-to-action").on("click", function(e){
        e.preventDefault()
        moveToHash($(this).attr("data-goto"))
    })


    var options = document.querySelectorAll('.option');
    var optionsNextButton = document.querySelectorAll('.survey-question.single-answer .next-section button');
    var multiOptionsNextButton = document.querySelectorAll('.survey-question.multi-answer .next-section button');
    var multiOptions = document.querySelectorAll('.multi-option');

    var _sendQuestionAnswers=function(questionId){
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
        $.ajax({
            type: "POST",
            url: url,
            data: $.param(data, true),
            success: function(data){
                // console.log("Succes"+data)
            },
            dataType: 'json'
        }).fail(function(error) {
            // console.log(error)
            // display.warn( "error" );
        }).always(function() {

        });
    }

    var _setProgressBarsPercentMultiOptions = function(question) {
        var answers = question.getElementsByClassName('survey-question-answers')[0];
        var progressBars = answers.getElementsByClassName('progress');

        var numQuestionAnswers = parseInt(question.getAttribute("data-numAnswers"));
        numQuestionAnswers= numQuestionAnswers+1;
        question.setAttribute("data-numAnswers",numQuestionAnswers);

        var arr = [].slice.call(progressBars);
        arr.forEach(function(progress) {
            var answer = progress.parentElement.parentElement;
            var numOptionAnswers = parseInt(answer.getAttribute("data-numAnswers"))
            // var selectedAnswers = (question.getAttribute('data-answer-selected') !== "") ? JSON.parse(question.getAttribute('data-answer-selected')) : "";
            // var answerPosition = selectedAnswers.indexOf(answer.getAttribute('data-answer-id'));
            var progressBar = progress.children[0];
            var progressBarCounter = answer.querySelector('.progress-bar-counter');

            // if (answerPosition >= 0) {
            //     numOptionAnswers = numOptionAnswers +1;
            // }
            // answer.setAttribute("data-numAnswers", numOptionAnswers);
            progressBar.style.width = Math.round(numOptionAnswers/numQuestionAnswers*100*100)/100 + '%';
            progressBarCounter.textContent = numOptionAnswers;
        });
    }

    // Checkbox
    var _selectSingleAnswer = function (event) {
        var answer = event.currentTarget.parentElement;
        var answerList = answer.parentElement;
        var question = answer.parentElement.parentElement;
        var nextButton = answerList.nextElementSibling.querySelector('.next-section button');

        answerList.querySelectorAll(".survey-question-answer").forEach(function(answerListed){
            answerListed.classList.remove('checked');
        })

        nextButton.classList.remove('disabled');
        answer.classList.add('checked');
        question.setAttribute('data-answer-selected', answer.getAttribute('data-answer-id'));
    };

    // Radio
    var _selectMultiAnswer = function (event) {
        var answer = event.currentTarget.parentElement;
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
                nextButton.classList.remove('disabled');
                answer.classList.add('checked');
                numOptionAnswers = numOptionAnswers+1;
            } else {
                selectedAnswers.splice(answerPosition, 1);
                answer.classList.remove('checked');
                numOptionAnswers = numOptionAnswers -1;
            }
        } else {
            selectedAnswers = [answer.getAttribute('data-answer-id')];
            nextButton.classList.remove('disabled');
            numOptionAnswers = numOptionAnswers +1;
            answer.classList.add('checked');
        }
        answer.setAttribute("data-numAnswers",numOptionAnswers)
        question.setAttribute('data-answer-selected',  (selectedAnswers.length > 0) ? JSON.stringify(selectedAnswers) : '');

        if (selectedAnswers.length === 0) {
            nextButton.classList.add('disabled');
        }
    };

    var _updateSurveyProgressBar = function(){
        // GLOBAL PROGRESS
        var numberQuestions = $(".survey-question").length;
        var numberQuestionsAnswered = $(".survey-question.answered").length;
        var surveyPos = document.getElementById('survey-pos');
        var surveyTotal = document.getElementById('survey-total');
        var progressBarSurveyCounter = document.getElementById('progress-bar-survey-counter');
        progressBarSurveyCounter.style.width = (numberQuestionsAnswered*100 / numberQuestions) + '%';
        surveyPos.textContent = numberQuestionsAnswered.toString();
        surveyTotal.textContent = numberQuestions.toString();
    }

    var _nextQuestion = function(questionId) {
        var currentQuestion = document.querySelector('.survey-question[data-question-id="' + questionId + '"]');
        currentQuestion.classList.add('answered');
        var nextQuestion = $(currentQuestion).next();
        // SLIDE DOWN DONE WITH CSS
        // if (!!nextQuestion === true) {
        //     // $(nextQuestion).css("display","none");
        //     $(nextQuestion).slideDown("slow");
        // }
        _sendQuestionAnswers(questionId)
        _updateSurveyProgressBar();
    };

    var _setProgressBarsPercentOneOption=function(questionId){
        var question = document.querySelector('[data-question-id="' + questionId + '"]');
        var numAnswers = parseInt(question.getAttribute("data-numAnswers"))
        var answerOptions = question.getElementsByClassName('survey-question-answer')
        Array.from(answerOptions).forEach(function(answerOption) {
            var numOptionAnswers = answerOption.getAttribute("data-numAnswers")
            var percentageOptionProgressBar = (numOptionAnswers / numAnswers * 100)
            percentageOptionProgressBar = Math.round(percentageOptionProgressBar * 100) / 100
            answerOption.getElementsByClassName("progress-bar-counter")[0].textContent=percentageOptionProgressBar +"%";
            var optionProgressBar = answerOption.getElementsByClassName("progress-bar")[0]
            optionProgressBar.style.width=percentageOptionProgressBar+"%"
            optionProgressBar.setAttribute("aria-valuenow",percentageOptionProgressBar)
            optionProgressBar.setAttribute("data-answer-percent",percentageOptionProgressBar)
            optionProgressBar.setAttribute("data-answer-percent-selected",percentageOptionProgressBar)
        })
    };

    options.forEach(function(option) {
        option.addEventListener('click', _selectSingleAnswer);
    });

    multiOptions.forEach(function(option) {
        option.addEventListener('click', _selectMultiAnswer);
    });


    var _nextButtonClick = specificNextButtonClick => e => {
        var $buttonPublish = $(e.target);
        var alias = $buttonPublish.attr("data-userLoggedAlias");
        if (alias == "") {
            // USER NO LOGGED
            var buttonId = guid();
            $buttonPublish.attr("id", buttonId);
            $form = $buttonPublish.parents("form")
            $form.removeClass("dirty");
            // noLoggedRememberPasswordCallbacks.publishProposal.saveState($buttonPublish);
            // $('#registro').find("form").attr("callback", "publishProposalNoLogged");
            // $('#registro').find("form").attr("data-buttonId", buttonId);
            $('#registro').modal('show');
        } else {
            specificNextButtonClick(e)
        }
    }

    optionsNextButton.forEach(function(nextButton) {
        nextButton.addEventListener('click', _nextButtonClick(_singleOptionNextButtonClick));
    });

    multiOptionsNextButton.forEach(function(nextButton) {
        nextButton.addEventListener('click', _nextButtonClick(_multiOptionNextButtonClick));
    });

    var _singleOptionNextButtonClick = function(event){
        var question = event.currentTarget.parentElement.parentElement.parentElement;
        var answers = question.getElementsByClassName('survey-question-answers')[0];
        var button = question.querySelector('.actions button');

        var selectedAnswer = question.getAttribute('data-answer-selected');

        var answer = answers.querySelector('[data-answer-id="' + selectedAnswer + '"]');
        var options = question.querySelectorAll('.option'); // Html collection to array

        if (selectedAnswer) {
            options.forEach(function(option) {
                option.removeEventListener('click', _selectSingleAnswer);
            });
            // UPDATE NUM ANSWERS
            answer.setAttribute("data-numanswers", parseInt(answer.getAttribute("data-numanswers"))+1);
            question.setAttribute("data-numanswers", parseInt(question.getAttribute("data-numanswers"))+1);

            _setProgressBarsPercentOneOption(parseInt(question.getAttribute('data-question-id')));

            _nextQuestion(parseInt(question.getAttribute('data-question-id'), 10));
        }
    }

    var _multiOptionNextButtonClick = function(event){
        var question = event.currentTarget.parentElement.parentElement.parentElement;
        var answers = question.getElementsByClassName('survey-question-answers')[0];
        var button = question.querySelector('[data-clicked]');
        var selectedAnswers = question.getAttribute('data-answer-selected');
        var multiOptions = question.querySelectorAll('.multi-option'); // Html collection to array

        selectedAnswers = JSON.parse(selectedAnswers);
        if (!!selectedAnswers && button.getAttribute('data-clicked') === 'false') {
            button.setAttribute('data-clicked', 'true');
            multiOptions.forEach(function(option) {
                option.removeEventListener('click', _selectMultiAnswer);
            });

            _setProgressBarsPercentMultiOptions(question);
            _nextQuestion(parseInt(question.getAttribute('data-question-id'), 10));
            nextButton.parentNode.classList.add('hidden');
        }
    }
    _updateSurveyProgressBar();
    document.querySelectorAll(".survey-question.single-answer.answered").forEach(function(question){
        var questionId = parseInt(question.getAttribute('data-question-id'))
        _setProgressBarsPercentOneOption(questionId)
    });
    document.querySelectorAll(".survey-question.multi-answer.answered").forEach(function(question){
        _setProgressBarsPercentMultiOptions(question)
    });
})