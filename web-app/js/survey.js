$(function () {
    var options = document.querySelectorAll('.option');
    var optionsNextButton = document.querySelectorAll('.survey-question.single-answer .next-section button');
    var multiOptionsNextButton = document.querySelectorAll('.survey-question.multi-answer .next-section button');
    var multiOptions = document.querySelectorAll('.multi-option');

    var _showElement = function(element) {
        element.style.display = 'flex';
    };
    var _hideElement = function(element) {
        element.style.display = 'none';
    };
    var _showAllElements = function(elements) {
        var arr = [].slice.call(elements);
        arr.forEach(function(element) {
            element.style.display = 'flex';
        });
    };
    var _hideAllElements = function(elements) {
        var arr = [].slice.call(elements);
        arr.forEach(function(element) {
            element.style.display = 'none';
        });
    };

    var _setProgressBarsPercentMultiOptions = function(progressBars, question) {
        var numQuestionAnswers = parseInt(question.getAttribute("data-numAnswers"))
        question.setAttribute("data-numAnswers",numQuestionAnswers+1);
        var arr = [].slice.call(progressBars);
        arr.forEach(function(progress) {
            var answer = progress.parentElement.parentElement;
            var numOptionAnswers = parseInt(answer.getAttribute("data-numAnswers"))
            var selectedAnswers = (question.getAttribute('data-answer-selected') !== "") ? JSON.parse(question.getAttribute('data-answer-selected')) : "";
            var answerPosition = selectedAnswers.indexOf(answer.getAttribute('data-answer-id'));
            var progressBar = progress.children[0];
            var progressBarCounter = progress.previousElementSibling;

            if (answerPosition >= 0) {
                numOptionAnswers = numOptionAnswers +1;

            }
            answer.setAttribute("data-numAnswers", numOptionAnswers);
            progressBar.style.width = Math.round(numOptionAnswers/numQuestionAnswers*100*100)/100 + '%';
            progressBarCounter.textContent = numOptionAnswers;
        });
    }

    // Checkbox
    var _selectSingleAnswer = function (event) {
        var answer = event.currentTarget.parentElement;
        var answerList = answer.parentElement;
        var question = answer.parentElement.parentElement;
        var selectedAnswer = answerList.querySelector('[data-answer-id="' + question.getAttribute('data-answer-selected') + '"] .option.checked');
        var nextButton = answerList.nextElementSibling.querySelector('.next-section button');

        if (!!selectedAnswer === true) {    // unselect current selected option
            _hideElement(selectedAnswer);
            _showElement(selectedAnswer.previousElementSibling);
            nextButton.classList.add('disabled');
            question.setAttribute('data-answer-selected', '');
        }

        if (event.currentTarget === selectedAnswer) {   // hide progress bars and percents
            // var progressBarCounters = question.getElementsByClassName('progress-bar-counter');
            // var progressBars = question.getElementsByClassName('progress');
            // _hideAllElements(progressBarCounters);
            // _hideAllElements(progressBars);
            return;
        }

        nextButton.classList.remove('disabled');
        question.setAttribute('data-answer-selected', answer.getAttribute('data-answer-id'));
        _hideElement(event.currentTarget);
        _showElement(event.currentTarget.nextElementSibling);
    };

    // Radio
    var _selectMultiAnswer = function (event) {
        var answer = event.currentTarget.parentElement;
        var answerVotes = answer.querySelector('.progress-bar-counter');
        var answersList = answer.parentElement;
        var question = answersList.parentElement;

        var selectedAnswers = (question.getAttribute('data-answer-selected') !== "") ? JSON.parse(question.getAttribute('data-answer-selected')) : "";
        var nextButton = answersList.nextElementSibling.querySelector('.next-section button');


        if (!!selectedAnswers === true && Array.isArray(selectedAnswers)) {
            var answerPosition = selectedAnswers.indexOf(answer.getAttribute('data-answer-id'));
            if (answerPosition === -1) {
                selectedAnswers.push(answer.getAttribute('data-answer-id'));
                answerVotes.textContent = parseInt(answerVotes.textContent, 10) + 1;
                nextButton.classList.remove('disabled');
            } else {
                selectedAnswers.splice(answerPosition, 1);
                answerVotes.textContent = parseInt(answerVotes.textContent, 10) - 1;
            }
        } else {
            selectedAnswers = [answer.getAttribute('data-answer-id')];
            answerVotes.textContent = parseInt(answerVotes.textContent, 10) + 1;
            nextButton.classList.remove('disabled');
        }

        question.setAttribute('data-answer-selected',  (selectedAnswers.length > 0) ? JSON.stringify(selectedAnswers) : '');
        _hideElement(event.currentTarget);
        _showElement((event.currentTarget.className.split('checked').length !== 2) ? event.currentTarget.nextElementSibling : event.currentTarget.previousElementSibling);

        if (selectedAnswers.length === 0) {
            nextButton.classList.add('disabled');
        }

        /*_showAllElements(progressBarCounters);
        _showAllElements(progressBars);
        setTimeout(function() {
            _setProgressBarsPercentMultiOptions(progressBars, question);
        }, 100);  */
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

    var _nextQuestion = function(questionPos) {
        var currentQuestion = document.querySelector('.survey-question[data-question-pos="' + questionPos + '"]');
        currentQuestion.classList.add('answered');
        var nextQuestion = $(currentQuestion).next();
        if (!!nextQuestion === true) {
            // $(nextQuestion).css("display","none");
            $(nextQuestion).slideDown("slow");
        }
        _updateSurveyProgressBar();
    };

    var _setProgressBarsPercentOneOption=function(questionId){
        var question = document.querySelector('[data-question-pos="' + questionId + '"]');
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

    optionsNextButton.forEach(function(nextButton) {
        nextButton.addEventListener('click', function(event) {
            var question = event.currentTarget.parentElement.parentElement.parentElement;
            var answers = question.getElementsByClassName('survey-question-answers')[0];
            var button = question.querySelector('[data-clicked]');

            var progressBarCounters = answers.getElementsByClassName('progress-bar-counter');
            var progressBars = answers.getElementsByClassName('progress');
            var selectedAnswer = question.getAttribute('data-answer-selected');

            var answer = answers.querySelector('[data-answer-id="' + selectedAnswer + '"]');
            var options = question.querySelectorAll('.option'); // Html collection to array

            if (selectedAnswer && button.getAttribute('data-clicked') === 'false') {
                button.setAttribute('data-clicked', 'true');
                options.forEach(function(option) {
                    option.removeEventListener('click', _selectSingleAnswer);
                });
                // UPDATE NUM ANSWERS
                answer.setAttribute("data-numanswers", parseInt(answer.getAttribute("data-numanswers"))+1);
                question.setAttribute("data-numanswers", parseInt(question.getAttribute("data-numanswers"))+1);

                _showAllElements(progressBarCounters);
                _showAllElements(progressBars);
                _setProgressBarsPercentOneOption(parseInt(question.getAttribute('data-question-pos')));

                _nextQuestion(parseInt(question.getAttribute('data-question-pos'), 10));
                nextButton.parentNode.classList.add('hidden');
            }
        });
    });

    multiOptionsNextButton.forEach(function(nextButton) {
        nextButton.addEventListener('click', function(event) {
            var question = event.currentTarget.parentElement.parentElement.parentElement;
            var answers = question.getElementsByClassName('survey-question-answers')[0];
            var button = question.querySelector('[data-clicked]');

            var progressBarCounters = answers.getElementsByClassName('progress-bar-counter');
            var progressBars = answers.getElementsByClassName('progress');
            var selectedAnswers = question.getAttribute('data-answer-selected');
            var multiOptions = question.querySelectorAll('.multi-option'); // Html collection to array

            selectedAnswers = JSON.parse(selectedAnswers);
            console.log(selectedAnswers)
            if (!!selectedAnswers && button.getAttribute('data-clicked') === 'false') {
                button.setAttribute('data-clicked', 'true');
                multiOptions.forEach(function(option) {
                    option.removeEventListener('click', _selectMultiAnswer);
                });

                _showAllElements(progressBarCounters);
                _showAllElements(progressBars);
                _setProgressBarsPercentMultiOptions(progressBars,question);
                _nextQuestion(parseInt(question.getAttribute('data-question-pos'), 10));
                nextButton.parentNode.classList.add('hidden');
            }
        });
    });

    _updateSurveyProgressBar();
})