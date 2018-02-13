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

    var _setProgressBarsPercent = function(progressBars) {
        var arr = [].slice.call(progressBars);
        arr.forEach(function(element) {
            var progressBar = element.children[0];
            var progressBarCounter = element.previousElementSibling;
            progressBarCounter.textContent = progressBar.getAttribute('data-answer-percent') + '%';
            progressBar.style.width = progressBar.getAttribute('data-answer-percent') + '%';
        });
    }

    var _setProgressBarsPercentMultiOptions = function(progressBars, question) {
        var arr = [].slice.call(progressBars);
        arr.forEach(function(element) {
            var answer = element.parentElement.parentElement;
            var question = answer.parentElement;
            var selectedAnswers = (question.getAttribute('data-answer-selected') !== "") ? JSON.parse(question.getAttribute('data-answer-selected')) : "";
            var answerPosition = selectedAnswers.indexOf(answer.getAttribute('data-answer-id'));
            var progressBar = element.children[0];
            var progressBarCounter = element.previousElementSibling;

            if (answerPosition === -1) {
                progressBar.style.width = progressBar.getAttribute('data-answer-percent') + '%';
            } else {
                progressBar.style.width = progressBar.getAttribute('data-answer-percent-selected') + '%';
                progressBarCounter.textContent = parseInt(progressBarCounter.textContent, 10) + 1;
            }
        });
    }

    // Checkbox
    var _selectSingleAnswer = function (event) {
        var answer = event.currentTarget.parentElement;
        var question = answer.parentElement;
        var progressBarCounters = question.getElementsByClassName('progress-bar-counter');
        var progressBars = question.getElementsByClassName('progress');
        var selectedAnswer = question.querySelector('[data-answer-id="' + question.getAttribute('data-answer-selected') + '"] .option.checked');
        var progressBar = answer.getElementsByClassName('progress-bar')[0];
        var progressBarCounter = answer.getElementsByClassName('progress-bar-counter')[0];
        var nextButton = question.nextElementSibling.querySelector('.next-section button');

        if (!!selectedAnswer === true) {    // unselect current selected option
            _hideElement(selectedAnswer);
            _showElement(selectedAnswer.previousElementSibling);
            nextButton.classList.add('disabled');
            question.setAttribute('data-answer-selected', '');
        }

        if (event.currentTarget === selectedAnswer) {   // hide progress bars and percents
            _hideAllElements(progressBarCounters);
            _hideAllElements(progressBars);
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
        var question = answer.parentElement;
        var progressBarCounters = question.getElementsByClassName('progress-bar-counter');
        var progressBars = question.getElementsByClassName('progress');
        var selectedAnswers = (question.getAttribute('data-answer-selected') !== "") ? JSON.parse(question.getAttribute('data-answer-selected')) : "";
        var progressBar = answer.getElementsByClassName('progress-bar')[0];
        var nextButton = question.nextElementSibling.querySelector('.next-section button');


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

    var _updateSurveyProgress = function(questionPos) {
        var surveyPos = document.getElementById('survey-pos');
        var surveyProgress = document.getElementById('survey-progress');
        var progressBarSurveyCounter = document.getElementById('progress-bar-survey-counter');
        var amountPercentPerStep = parseInt(progressBarSurveyCounter.getAttribute('data-amount-percent-per-step'), 10);
        var nextQuestionPos = questionPos + 1;
        surveyProgress.setAttribute('data-question-pos', questionPos);
        var currentQuestion = document.querySelector('.survey-question[data-question-pos="' + questionPos + '"]');
        currentQuestion.classList.add('answered');
        var nextQuestion = $(currentQuestion).next();
        console.log(nextQuestion)
        console.log(!!nextQuestion)
        if (!!nextQuestion === true) {
            // $(nextQuestion).css("display","none");
            $(nextQuestion).slideDown("slow");
        }


        progressBarSurveyCounter.style.width = (questionPos * amountPercentPerStep) + '%';
        surveyPos.textContent = questionPos.toString();
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
            var selectedAnswer = answers.getAttribute('data-answer-selected');

            var answer = answers.querySelector('[data-answer-id="' + selectedAnswer + '"]');
            var progressBar = answer.getElementsByClassName('progress-bar')[0];
            var progressBarCounter = answer.getElementsByClassName('progress-bar-counter')[0];
            var options = question.querySelectorAll('.option'); // Html collection to array

            if (selectedAnswer && button.getAttribute('data-clicked') === 'false') {
                button.setAttribute('data-clicked', 'true');
                options.forEach(function(option) {
                    option.removeEventListener('click', _selectSingleAnswer);
                });

                _showAllElements(progressBarCounters);
                _showAllElements(progressBars);
                _setProgressBarsPercent(progressBars);

                setTimeout(function() {
                    progressBar.style.width = progressBar.getAttribute('data-answer-percent-selected') + '%';
                    progressBarCounter.textContent = progressBar.getAttribute('data-answer-percent-selected') + '%';
                }, 100);

                _updateSurveyProgress(parseInt(question.getAttribute('data-question-pos'), 10));
                nextButton.classList.add('hidden');
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
            var selectedAnswers = answers.getAttribute('data-answer-selected');
            var multiOptions = question.querySelectorAll('.multi-option'); // Html collection to array

            selectedAnswers = JSON.parse(selectedAnswers);

            if (!!selectedAnswers && button.getAttribute('data-clicked') === 'false') {
                button.setAttribute('data-clicked', 'true');
                multiOptions.forEach(function(option) {
                    option.removeEventListener('click', _selectMultiAnswer);
                });

                _showAllElements(progressBarCounters);
                _showAllElements(progressBars);
                _setProgressBarsPercentMultiOptions(progressBars);
                _updateSurveyProgress(parseInt(question.getAttribute('data-question-pos'), 10));
                nextButton.classList.add('hidden');
            }
        });
    });
})