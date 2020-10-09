$(function () {

    $("#survey-call-to-action, .leader-post .footer .comment-counter button, .call-to-action-mobile.go-to-survey button").on("click", function(e){
        e.preventDefault()
        moveToHash($(this).attr("data-goto"))
    })

    var singleOptionNextButton = document.querySelectorAll('.survey-question.single-answer .next-section button');
    var multiOptionsNextButton = document.querySelectorAll('.survey-question.multi-answer .next-section button');
    var textOptionsNextButton = document.querySelectorAll('.survey-question.text-answer .next-section button');
    var ratioOptionsNextButton = document.querySelectorAll('.survey-question.rating-answer .next-section button');

    // Add click listener on answers that aren't answered
    $('.survey-question-answer')
        .filter(function() {return $(this).parents('.'+surveyFunctions.ANSWERED_CLASS).length === 0})
        .on("click",surveyFunctions._selectAnswer)

    var nextButtonIdx;
    for (nextButtonIdx = 0; nextButtonIdx < singleOptionNextButton.length; nextButtonIdx++) {
        // Fore each not works on IE10
        var nextButton = singleOptionNextButton[nextButtonIdx]
        nextButton.addEventListener('click', surveyFunctions._nextButtonClick);
    }
    for (nextButtonIdx = 0; nextButtonIdx < multiOptionsNextButton.length; nextButtonIdx++) {
        // Fore each not works on IE10
        var nextButton = multiOptionsNextButton[nextButtonIdx]
        nextButton.addEventListener('click', surveyFunctions._nextButtonClick);
    }
    for (nextButtonIdx = 0; nextButtonIdx < textOptionsNextButton.length; nextButtonIdx++) {
        // Fore each not works on IE10
        var nextButton = textOptionsNextButton[nextButtonIdx]
        nextButton.addEventListener('click', surveyFunctions._nextButtonClick);
    }
    for (nextButtonIdx = 0; nextButtonIdx < ratioOptionsNextButton.length; nextButtonIdx++) {
        // Fore each not works on IE10
        var nextButton = ratioOptionsNextButton[nextButtonIdx]
        nextButton.addEventListener('click', surveyFunctions._nextButtonClick);
    }

    $(".multi-answer .survey-question-answer .option-extra-content textarea").on("click", function (e) {
        // Blocking uncheck multi-answer when click on text area
        e.stopPropagation();
        e.preventDefault();
    })

    $(".survey-question .next-section a.skip-question").on("click",function (e) {
        e.preventDefault();
        var question = e.currentTarget.parentElement.parentElement.parentElement;
        var questionId= parseInt(question.getAttribute('data-question-id'), 10);
        surveyFunctions._setProgressBarsPercentOneOption(questionId);
        surveyFunctions._nextQuestion(questionId);
    });

    $(".survey-question .next-section a.skip-survey").on("click", function (e) {
        e.preventDefault();
        var survey = e.currentTarget.parentElement.parentElement.parentElement.parentElement;
        for (questionIdx = 0; questionIdx < survey.children.length; questionIdx++) {
            var question = survey.children[questionIdx];
            var questionId = parseInt(question.getAttribute('data-question-id'));
            surveyFunctions._setProgressBarsPercent(questionId);
            surveyFunctions._nextQuestion(questionId);
        };
    });

   surveyFunctions.initSurvey();
});

var surveyFunctions = {
    NO_LOGGED_CALLBACK:"addAnwerNoLogged",
    NO_LOGGED_CALLBACK_QUESTION_VAR_NAME:"data-questionId",
    SKIPPED_CLASS:"skipped",
    ANSWERED_CLASS:"answered",
    relodAfeterSubmit: false,

    QUESTION_OPTION_ATTR_TYPE:"data-questionoptiontype",

    initSurvey:function(){
        // IE10 not supports foreach
        var questionAnswerIdx
        var singleAnswers =document.querySelectorAll(".survey-question."+surveyFunctions.ANSWERED_CLASS);
        for (questionAnswerIdx = 0; questionAnswerIdx < singleAnswers.length; questionAnswerIdx++) {
            var question = singleAnswers[questionAnswerIdx];
            var questionId = parseInt(question.getAttribute('data-question-id'))
            surveyFunctions._setProgressBarsPercent(questionId)
            if (questionAnswerIdx == singleAnswers.length-1){
                // LAST QUESTION ANSWERED
                var nextQuestionId = surveyFunctions._getNextQuestionId(question);
                surveyFunctions._nextQuestion(questionId,nextQuestionId);
            }
        };
        surveyFunctions._updateSurveyProgressBar();
        // Block bubbling click on inputs when has text
        $(".survey-question-answer input, .survey-question-answer textara, .survey-question-answer select").on("click", function (e) {
            if ($(this).parents(".multi-answer").length!=0){
                console.log("Stop bubbling because there are text on the input. Only on multi answers because them can be unselected. ")
                e.stopPropagation();
            }
        });
    },
    _nextButtonClick : function(e) {
        e.preventDefault();
        var $buttonNext = $(e.target);
        var alias = $buttonNext.attr("data-userLoggedAlias");
        var question = e.currentTarget.parentElement.parentElement.parentElement;
        if (surveyFunctions._checkValidAnswers(question)){
            if (alias == "") {
                surveyFunctions._openRegisterModal(question)
            } else {
                surveyFunctions._nextButtonClickSelector(question)
            }
        }
    },
    _openRegisterModal: function(question){
        // USER NO LOGGED
        var buttonId = guid();
        var $buttonNext = $(question).find("button[data-campaignId]");
        $buttonNext.attr("id", buttonId);
        $form = $buttonNext.parents("form")
        $form.removeClass("dirty");
        var questionId = question.getAttribute("data-question-id")
        // $form.attr(surveyFunctions.NO_LOGGED_CALLBACK_QUESTION_VAR_NAME, questionId)
        // noLoggedRememberPasswordCallbacks.publishProposal.saveState($buttonPublish);
        $('#registro').find("form").attr("callback", surveyFunctions.NO_LOGGED_CALLBACK);
        $('#registro').find("form").attr(surveyFunctions.NO_LOGGED_CALLBACK_QUESTION_VAR_NAME, questionId);
        $('#registro').modal('show');
    },
    _nextButtonClickSelector:function (question) {
        var params ={
            question: question
        }
        var executableFunction = new userValidatedByDomain.ExcutableFunctionCallback(surveyFunctions._nextButtonClickSelectorValidationChecked, params)
        var button = question.querySelector('.next-section button');
        var $button = $(button);
        userValidatedByDomain.executeClickButtonHandlingValidations($button, executableFunction);

    },
    _nextButtonClickSelectorValidationChecked:function (params) {
        var question = params.question
        if (surveyFunctions._checkValidAnswers(question)){
            if (question.classList.contains("multi-answer")){
                surveyFunctions._multiOptionNextButtonClick(question)
            }else if (question.classList.contains("rating-answer")){
                surveyFunctions._ratingOptionNextButtonClick(question)
            }else {
                surveyFunctions._singleOptionNextButtonClick(question)
            }
        }
    },
    _checkValidAnswers: function(question){
        var questionOptionsSelected = question.querySelectorAll(".survey-question-answer.checked");
        var answersValid = true;

        var questionOPtionsSelectedIdx; // IE10 not supports forEach
        for (questionOPtionsSelectedIdx = 0; questionOPtionsSelectedIdx < questionOptionsSelected.length; questionOPtionsSelectedIdx++) {
            var questionOption = questionOptionsSelected[questionOPtionsSelectedIdx];
            var questionOptionType = questionOption.getAttribute(surveyFunctions.QUESTION_OPTION_ATTR_TYPE)
            answersValid = surveyFunctions._checkValidAnswerType[questionOptionType](questionOption) && answersValid;
        }
        return answersValid;
    },

    _checkValidAnswerType:{
        ANSWER_PREDEFINED: function(questionAnswerOption){return true;},
        ANSWER_TEXT: function(questionAnswerOption){
            var textAreaNodes = questionAnswerOption.querySelectorAll(".option-extra-content textarea");
            var textArea = textAreaNodes[0];
            var validationData = surveyFunctions._checkValidAnswerType._checkInputData(textArea);
            surveyFunctions._checkValidAnswerType._handlePrintingError(validationData);
            return validationData.valid;
        },
        ANSWER_SMALL_TEXT: function(questionAnswerOption){
            var textAreaNodes = questionAnswerOption.querySelectorAll(".option-extra-content input");
            var textArea = textAreaNodes[0];
            var validationData = surveyFunctions._checkValidAnswerType._checkInputData(textArea);
            surveyFunctions._checkValidAnswerType._handlePrintingError(validationData);
            return validationData.valid;
        },
        ANSWER_DATE: function(questionAnswerOption){
            var textDateInputNodes = questionAnswerOption.querySelectorAll(".option-extra-content input");
            var textDateInput = textDateInputNodes[0];
            var validationData = surveyFunctions._checkValidAnswerType._checkInputData(textDateInput);
            surveyFunctions._checkValidAnswerType._handlePrintingError(validationData);
            return validationData.valid;
        },
        ANSWER_PHONE: function(questionAnswerOption){
            var phoneInputNodes = questionAnswerOption.querySelectorAll(".option-extra-content input");
            var phoneInput = phoneInputNodes[0];
            var phoneValidationData = surveyFunctions._checkValidAnswerType._checkInputData(phoneInput);
            // var prefixInputNodes = questionAnswerOption.querySelectorAll(".option-extra-content select");
            // var prefixInput = prefixInputNodes[0];
            // var prefixPhoneValidationData = surveyFunctions._checkValidAnswerType._handlePrintingError(phoneValidationData);
            // return phoneValidationData.valid && prefixPhoneValidationData.valid;
            return phoneValidationData.valid;
        },
        ANSWER_NUMBER: function(questionAnswerOption){
            var textNumberInputNodes = questionAnswerOption.querySelectorAll(".option-extra-content input");
            var textNumberInput = textNumberInputNodes[0];
            var numberValidationData = surveyFunctions._checkValidAnswerType._checkInputData(textNumberInput);
            surveyFunctions._checkValidAnswerType._handlePrintingError(numberValidationData);
            return numberValidationData.valid
        },
        _checkInputData:function(inputNode){
            if (inputNode.value === ""){
                return {
                    valid: false,
                    msg:i18n.kuorum.web.commands.payment.survey.QuestionOptionCommand.text.nullable,
                    input: inputNode
                };
            }else{
                return {
                    valid: true,
                    msg:"",
                    input: inputNode
                };
            }
        },
        _handlePrintingError: function(validationData){
            if (validationData.valid) {
                $(validationData.input).siblings(".error").remove();
                validationData.input.classList.remove("error")
                return true;
            }else{
                $(validationData.input).siblings(".error").remove();
                validationData.input.classList.add("error");
                var errorNode = document.createElement("span");
                errorNode.classList = "error";
                errorNode.innerHTML = validationData.msg
                validationData.input.parentNode.insertBefore(errorNode, validationData.input.nextSibling);
            }
        }
    },

    _singleOptionNextButtonClick : function(question){
        // event.preventDefault();
        // var question = event.currentTarget.parentElement.parentElement.parentElement;
        var answers = question.getElementsByClassName('survey-question-answers')[0];
        var selectedAnswer = question.getAttribute('data-answer-selected');
        var answer = answers.querySelector('[data-answer-id="' + selectedAnswer + '"]');
        if (selectedAnswer) {
            // UPDATE NUM ANSWERS
            answer.setAttribute("data-numanswers", parseInt(answer.getAttribute("data-numanswers"))+1);
            question.setAttribute("data-numanswers", parseInt(question.getAttribute("data-numanswers"))+1);

            var questionId = parseInt(question.getAttribute('data-question-id'), 10);
            var nextQuestionId = surveyFunctions._getNextQuestionId(question);
            
            var successFunction = function(){
                surveyFunctions._transformExtraDataNoEditable(questionId, true)
                surveyFunctions._nextQuestion(questionId,nextQuestionId);
                // surveyFunctions._setProgressBarsPercentOneOption(parseInt(question.getAttribute('data-question-id')));
                surveyFunctions._setProgressBarsPercent(questionId);
            }
            var failFunction = function(){
                display.warn("ERROR SAVING ANSWER");
            }
            surveyFunctions._transformExtraDataNoEditable(questionId, false)
            surveyFunctions._sendQuestionAnswers(questionId, successFunction, failFunction)
        }
    },

    _getNextQuestionId : function(question){
        var answer = $(question).find(".survey-question-answer.checked")[0];
        var nextQuestionId = parseInt(answer.getAttribute('data-nextQuestionId'), 10);
        return nextQuestionId;
    },
    _ratingOptionNextButtonClick : function(question){
        // event.preventDefault();
        // var question = event.currentTarget.parentElement.parentElement.parentElement;
        var answers = question.getElementsByClassName('survey-question-answers')[0];
        var selectedAnswer = question.getAttribute('data-answer-selected');
        var answer = answers.querySelector('[data-answer-id="' + selectedAnswer + '"]');
        var options = question.querySelectorAll('.survey-question-answer'); // Html collection to array
        if (selectedAnswer) {
            // UPDATE NUM ANSWERS
            answer.setAttribute("data-numanswers", parseInt(answer.getAttribute("data-numanswers"))+1);
            question.setAttribute("data-numanswers", parseInt(question.getAttribute("data-numanswers"))+1);

            var questionId = parseInt(question.getAttribute('data-question-id'), 10);

            var successFunction = function(){
                // surveyFunctions._setProgressBarsPercentOneOption(parseInt(question.getAttribute('data-question-id')));
                surveyFunctions._transformExtraDataNoEditable(questionId, true)
                surveyFunctions._setProgressBarsPercent(questionId);
                surveyFunctions._nextQuestion(questionId);
            }
            var failFunction = function(){
                display.warn("ERROR SAVING ANSWER");
            }
            surveyFunctions._transformExtraDataNoEditable(questionId, false)
            surveyFunctions._sendQuestionAnswers(questionId, successFunction, failFunction)
        }
    },

    _multiOptionNextButtonClick : function(question){
        // var question = event.currentTarget.parentElement.parentElement.parentElement;
        var options = question.querySelectorAll('.survey-question-answer'); // Html collection to array
        var selectedAnswers = question.getAttribute('data-answer-selected');

        selectedAnswers = JSON.parse(selectedAnswers);
        if (!!selectedAnswers) {
            // Updating num answers
            var numQuestionAnswers = parseInt(question.getAttribute("data-numAnswers"));
            numQuestionAnswers= numQuestionAnswers+1;
            question.setAttribute("data-numAnswers",numQuestionAnswers);

            var questionId = parseInt(question.getAttribute('data-question-id'), 10);

            var successFunction = function(){
                surveyFunctions._transformExtraDataNoEditable(questionId, true)
                surveyFunctions._nextQuestion(questionId);
                surveyFunctions._setProgressBarsPercentMultiOptions(question);
            }
            var failFunction = function(){
                display.warn("ERROR SAVING ANSWER");
            }
            surveyFunctions._transformExtraDataNoEditable(questionId, false)
            surveyFunctions._sendQuestionAnswers(questionId, successFunction, failFunction)
        }
    },

    _nextQuestion : function(currentQuestionId, nextQuestionId ) {
        var currentQuestion = document.querySelector('.survey-question[data-question-id="' + currentQuestionId + '"]');
        if (currentQuestion == undefined){
            console.log("Question with id ["+currentQuestionId+"]no defined");
            return;
        }
        currentQuestion.classList.add(surveyFunctions.ANSWERED_CLASS);
        surveyFunctions._switchOffOptionClickEventsOfQuestion(currentQuestionId);
        if (isNaN(nextQuestionId)) {
            $nextQuestion = $(currentQuestion).next();
        }else if (nextQuestionId == 0){
            $nextQuestion = $(".survey-end");
        }else{
            $nextQuestion = $('.survey-question[data-question-id="' + nextQuestionId + '"]')
        }
        $nextQuestion.addClass("active-question")
        $nextQuestion.prevAll(":not(."+surveyFunctions.ANSWERED_CLASS+")").addClass(surveyFunctions.SKIPPED_CLASS)
        var nextQuestionCounter = $nextQuestion.prevAll(".survey-question."+surveyFunctions.ANSWERED_CLASS).length +1;
        $nextQuestion.find(".survery-question-number .survey-quiestion-number-idx").html(nextQuestionCounter)
        $(currentQuestion).removeClass("active-question")
        surveyFunctions._updateSurveyProgressBar();
    },

    _switchOffOptionClickEventsOfQuestion : function(questionId){
        var question = document.querySelector('.survey-question[data-question-id="' + questionId + '"]');
        var options = question.querySelectorAll('.survey-question-answer');
        var optionIdx; // IE10 not supports forEach
        for (optionIdx = 0; optionIdx < options.length; optionIdx++) {
            var option = options[optionIdx];
            $(option).off('click',surveyFunctions._selectAnswer);
        };
    },

    _updateSurveyProgressBar : function(){
        // GLOBAL PROGRESS
        var numberQuestions = $(".survey-question:not(."+surveyFunctions.SKIPPED_CLASS+")").length;
        var numberQuestionsAnswered = $(".survey-question."+surveyFunctions.ANSWERED_CLASS).length;
        // var surveyPos = document.getElementById('survey-pos');
        // var surveyTotal = document.getElementById('survey-total');
        var surveyPercentage = document.getElementById('survey-percentage');
        var progressBarSurveyCounter = document.getElementById('progress-bar-survey-counter');
        var percentage = Math.round(numberQuestionsAnswered*100 / numberQuestions) + '%';
        progressBarSurveyCounter.style.width = percentage;
        // surveyPos.textContent = numberQuestionsAnswered.toString();
        // surveyTotal.textContent = numberQuestions.toString();
        surveyPercentage.textContent = percentage.toString();
    },

    _setProgressBarsPercentOneOption:function(questionId){
        var question = document.querySelector('[data-question-id="' + questionId + '"]');
        var numAnswers = parseInt(question.getAttribute("data-numAnswers"))
        var answerOptions = Array.from(question.getElementsByClassName('survey-question-answer'))
        var answerOptionIdx; // IE10 not supports forEach
        for (answerOptionIdx = 0; answerOptionIdx < answerOptions.length; answerOptionIdx++) {
            var answerOption = answerOptions[answerOptionIdx];
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
        }
    },

    _setProgressBarsPercentText:function(questionId) {
        var question = document.querySelector('[data-question-id="' + questionId + '"]');
        var numAnswers = parseInt(question.getAttribute("data-numAnswers"))
        var answerOptions = Array.from(question.getElementsByClassName('survey-question-answer'))
        // There is only one option on text questions
        var answerOption = answerOptions[0];
        answerOption.getElementsByClassName("progress-bar-counter")[0].textContent=numAnswers;
        var optionProgressBar = answerOption.getElementsByClassName("progress-bar")[0]
        var percentageOptionProgressBar=100;
        optionProgressBar.style.width=percentageOptionProgressBar+"%"
        optionProgressBar.setAttribute("aria-valuenow",percentageOptionProgressBar)
        optionProgressBar.setAttribute("data-answer-percent",percentageOptionProgressBar)
        optionProgressBar.setAttribute("data-answer-percent-selected",percentageOptionProgressBar)
    },

    _setProgressBarsPercentRatio:function(questionId) {
        var question = document.querySelector('[data-question-id="' + questionId + '"]');
        var numAnswers = parseInt(question.getAttribute("data-numAnswers"))
        var answerOptions = Array.from(question.getElementsByClassName('survey-question-answer'))
        var answerOptionIdx; // IE10 not supports forEach
        var totalScore = 0;
        var totalScoreBase = 0;
        for (answerOptionIdx = 0; answerOptionIdx < answerOptions.length; answerOptionIdx++) {
            var answerOption = answerOptions[answerOptionIdx];
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

            totalScore += (answerOptionIdx+1) * numOptionAnswers;
            totalScoreBase += (answerOptionIdx) * numOptionAnswers;
        }
        var rating = totalScore / numAnswers;
        var ratingPercentage = totalScoreBase / numAnswers;
        var optionProgressBar = question.querySelector(".survey-question-progress-info .progress-bar")
        var numQuestions = answerOptions.length;
        var globalRatingPercent = ratingPercentage * 100 / (numQuestions -1); // -1 because idx goes from 0 to 4
        optionProgressBar.style.width=globalRatingPercent+"%";
        optionProgressBar.setAttribute("aria-valuenow",rating.toFixed(1))
        optionProgressBar.setAttribute("data-answer-percent",rating.toFixed(1))
        optionProgressBar.setAttribute("data-answer-percent-selected",rating.toFixed(1))
        var optionProgressBarCounter = question.querySelector(".survey-question-progress-info .progress-bar-counter");
        optionProgressBarCounter.innerHTML = rating.toFixed(1) +"<span class='progress-bar-counter-total'> / "+ numQuestions+"</span>";
    },

    _transformExtraDataNoEditable: function(questionId, switchToNoEditable){
        var question = document.querySelector('[data-question-id="' + questionId + '"]');
        var answerOptions = Array.from(question.getElementsByClassName('survey-question-answer'))
        var answerOptionIdx; // IE10 not supports forEach
        for (answerOptionIdx = 0; answerOptionIdx < answerOptions.length; answerOptionIdx++) {
            var answerOption = answerOptions[answerOptionIdx];
            var $answerOption = $(answerOption)
            var answerOptionType = $answerOption.attr(surveyFunctions.QUESTION_OPTION_ATTR_TYPE);
            if ($answerOption.find(".option-extra-content").length >0){
                var data = {}
                var text
                var elementsToRemove = []
                if (answerOptionType == "ANSWER_TEXT"){
                    var $textArea = $answerOption.find("textarea")
                    data = {text:$textArea.val()}
                    text = data.text;
                    elementsToRemove.push($textArea);
                }else if (answerOptionType == "ANSWER_SMALL_TEXT"){
                    var $input = $answerOption.find("input")
                    data = {text:$input.val()}
                    text = data.text
                    elementsToRemove.push($input);
                }else if (answerOptionType == "ANSWER_NUMBER"){
                    var $input = $answerOption.find("input")
                    data = {number:$input.val()}
                    text = data.number
                    elementsToRemove.push($input);
                }else if (answerOptionType == "ANSWER_DATE"){
                    var $input = $answerOption.find("input")
                    data = {date:$input.val()}
                    text = data.date
                    elementsToRemove.push($input.parents(".input-group.date"));
                }else if (answerOptionType == "ANSWER_PHONE"){
                    var $input = $answerOption.find("input[name=text]")
                    var $prefix = $answerOption.find("select[name=text2]")
                    data = {text:$input.val(), text2:$prefix.val()}
                    text = data.text2+data.text
                    elementsToRemove.push($input);
                    elementsToRemove.push($prefix);
                }else if (answerOptionType == "ANSWER_FILES"){
                   //TODO
                }
                if (switchToNoEditable){
                    $answerOption.find(".text-answer").html(text)
                    elementsToRemove.forEach(function(item, idx, array){
                        item.remove();
                    });
                }
                surveyFunctions._addExtraInfoAnswer($answerOption, data)
            }
        }
    },

    _addExtraInfoAnswer: function ($answerOption, data){
        $answerOption.attr("data-question-extra-content",JSON.stringify(data))
    },

    _getExtraInfoAnswer: function ($answerOption){
        return JSON.parse($answerOption.attr("data-question-extra-content"));
    },

    _selectAnswer:function(e){
        var answer = e.currentTarget
        if ($(answer).find(".single-option").length >0){
            surveyFunctions._selectSingleAnswer(e)
        }else if ($(answer).find(".text-option").length >0){
            surveyFunctions._selectSingleAnswer(e)
        }else if ($(answer).find(".multi-option").length >0){
            surveyFunctions._selectMultiAnswer(e)
        }else if ($(answer).find(".rating-option").length >0){
            surveyFunctions._selectSingleAnswer(e)
        }
    },

    _selectSingleAnswer : function (event) {
        var answer = event.currentTarget;
        var answerList = answer.parentElement;
        var question = answer.parentElement.parentElement;
        var nextButton = question.querySelector('.footer .next-section button');

        $(answerList).find(".survey-question-answer").removeClass('checked');

        $(answer).addClass('checked');
        $(nextButton).removeClass('disabled');
        question.setAttribute('data-answer-selected', answer.getAttribute('data-answer-id'));
    },

    _selectMultiAnswer : function (event) {
        var answer = event.currentTarget;
        // var answerVotes = answer.querySelector('.progress-bar-counter');
        var answersList = answer.parentElement;
        var question = answersList.parentElement;
        var minAnswers = parseInt(question.getAttribute('data-minAnswers'));
        var maxAnswers = parseInt(question.getAttribute('data-maxAnswers')); // 0 means no limit
        var selectedAnswers = (question.getAttribute('data-answer-selected') !== "") ? JSON.parse(question.getAttribute('data-answer-selected')) : "";
        var nextButton = question.querySelector('.footer .next-section button');
        var numOptionAnswers = parseInt(answer.getAttribute("data-numAnswers"))

        if (!!selectedAnswers === true && Array.isArray(selectedAnswers)) {
            var answerPosition = selectedAnswers.indexOf(answer.getAttribute('data-answer-id'));
            if (answerPosition === -1 && maxAnswers>0 && selectedAnswers.length >= maxAnswers ) {
                console.log("Skip add new answer because the limit is reached")
                return;
            }else if (answerPosition === -1) {
                selectedAnswers.push(answer.getAttribute('data-answer-id'));
                $(answer).addClass('checked');
                numOptionAnswers = numOptionAnswers+1;
            } else {
                selectedAnswers.splice(answerPosition, 1);
                $(answer).removeClass('checked');
                numOptionAnswers = numOptionAnswers -1;
            }
        } else {
            selectedAnswers = [answer.getAttribute('data-answer-id')];
            numOptionAnswers = numOptionAnswers +1;
            $(answer).addClass('checked');
        }
        answer.setAttribute("data-numAnswers",numOptionAnswers)
        question.setAttribute('data-answer-selected',  (selectedAnswers.length > 0) ? JSON.stringify(selectedAnswers) : '');
        if (maxAnswers != 0 && selectedAnswers.length == maxAnswers){
            $(question).find(".survey-question-answer:not(.checked)").addClass("disabled")
        }else{
            $(question).find(".survey-question-answer:not(.checked)").removeClass("disabled")
        }
        if (selectedAnswers.length === 0 || selectedAnswers.length < minAnswers || selectedAnswers.length > maxAnswers && maxAnswers >0){
            $(nextButton).addClass('disabled');
        }else{
            $(nextButton).removeClass('disabled');
        }
    },

    _setProgressBarsPercent: function(questionId){
        var question = document.querySelector('[data-question-id="' + questionId + '"]');
        if (question == undefined){
            console.log("Question with id ["+questionId+"]no defined")
            return;
        }
        if (question.classList.contains("multi-answer")){
            surveyFunctions._setProgressBarsPercentMultiOptions(question)
        }else if(question.classList.contains("rating-answer")) {
            surveyFunctions._setProgressBarsPercentRatio(questionId)
        }else if(question.classList.contains("text-answer")) {
            surveyFunctions._setProgressBarsPercentText(questionId)
        }else{
            surveyFunctions._setProgressBarsPercentOneOption(questionId)
        }
    },

    _setProgressBarsPercentMultiOptions : function(question) {
        var answers = question.getElementsByClassName('survey-question-answers')[0];
        var progressBars = answers.getElementsByClassName('progress');

        var numQuestionAnswers = parseInt(question.getAttribute("data-numAnswers"));
        var arr = [].slice.call(progressBars);

        var progressBarIdx; // IE10 not supports forEach
        for (progressBarIdx = 0; progressBarIdx < arr.length; progressBarIdx++) {
            var progress = arr[progressBarIdx];
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
        }
    },

    _sendQuestionAnswers:function(questionId, successFunction, failFunction){
        var question = document.querySelector('.survey-question[data-question-id="' + questionId + '"]');
        var button = question.querySelector('.actions button');
        var url = button.getAttribute("data-postUrl");
        var answerIds = JSON.parse(question.getAttribute("data-answer-selected"));
        var questionType = question.getAttribute("data-questionType");
        console.log("Question type : "+questionType)
        if (!Array.isArray(answerIds)){
            answerIds = [answerIds];
        }

        var answers = surveyFunctions._recoverExtraDataForEachAnswer(answerIds);

        var data = {
            // surveyId:10,
            questionId:questionId,
            questionType:questionType,
            answersJson:JSON.stringify(answers)
        }
        moveToHash("#survey-progress")
        pageLoadingOn("Survey :: save answer. QuestionId: "+questionId)
        $.ajax({
            type: "POST",
            url: url,
            data: $.param(data),
            success: function(data){
                // console.log("Succes"+data)
                successFunction();
                if (surveyFunctions.relodAfeterSubmit){
                    // Chapu para recargar la pagina sin pasar el callback desde mordor
                    noLoggedCallbacks.reloadPage("Survey :: _sendQuestionAnswers");
                }
            },
            dataType: 'json'
        }).fail(function(xhr, status, error) {
            console.log(error);
            if (xhr.status==401){
                surveyFunctions._openRegisterModal(question)
            }else if (xhr.status=403){
                display.warn(xhr.responseJSON.msg)
                noLoggedCallbacks.reloadPage("Survey :: _sendQuestionAnswers :: Survey validation changed");
            }else if (xhr.status=405){
                display.warn(xhr.responseJSON.msg)
                noLoggedCallbacks.reloadPage("Survey :: _sendQuestionAnswers :: Survey closed");
            }else{
                failFunction();
            }
        }).always(function() {
            pageLoadingOff("Survey :: save answer. Question Id: "+questionId)
        });
    },

    _recoverExtraDataForEachAnswer: function (answerIds){
        // Recover extra data of each option chosen
        var answers = [];
        var answerIdsIdx;
        for (answerIdsIdx = 0; answerIdsIdx < answerIds.length; answerIdsIdx++) {
            var answerId = answerIds[answerIdsIdx];
            var data = {};
            var answerOptionSelector = "div[data-answer-id="+answerId+"]";
            var questionOptionType = $(answerOptionSelector).attr("data-questionOptionType")
            var extraDataSelector = answerOptionSelector+" .option-extra-content"
            console.log("Extra data:"+$(extraDataSelector).length);
            if ($(extraDataSelector).length > 0){
                data = surveyFunctions._getExtraInfoAnswer($(answerOptionSelector))
            }
            console.log("Question option type: "+questionOptionType)
            var answer = {
                questionOptionType:questionOptionType,
                answerId: answerId,
                data: data
            }
            answers.push(answer)
        }
        return answers;
    }
}

noLoggedCallbacks[surveyFunctions.NO_LOGGED_CALLBACK] = function(){
    pageLoadingOn();
    var questionId = $('#registro').find("form").attr(surveyFunctions.NO_LOGGED_CALLBACK_QUESTION_VAR_NAME);
    var question = document.querySelector('.survey-question[data-question-id="' + questionId + '"]');
    surveyFunctions.relodAfeterSubmit = true;
    surveyFunctions._nextButtonClickSelector(question)
};