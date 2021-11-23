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
        // surveyFunctions._setProgressBarsPercent(questionId);
        surveyFunctions._nextQuestion(questionId);
    });

    $(".survey-question .next-section a.skip-survey").on("click", function (e) {
        e.preventDefault();
        var survey = e.currentTarget.parentElement.parentElement.parentElement.parentElement;
        for (questionIdx = 0; questionIdx < survey.children.length; questionIdx++) {
            var question = survey.children[questionIdx];
            var questionId = parseInt(question.getAttribute('data-question-id'));
            // surveyFunctions._setProgressBarsPercent(questionId);
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
            // surveyFunctions._setProgressBarsPercent(questionId)
            var nextQuestionId = surveyFunctions._getNextQuestionId(question);
            surveyFunctions._nextQuestion(questionId,nextQuestionId);
        };
        surveyFunctions._updateSurveyProgressBar();
        // Block bubbling click on inputs when has text
        $(".survey-question-answer input, .survey-question-answer textara, .survey-question-answer select").on("click", function (e) {
            if ($(this).parents(".multi-answer").length!=0){
                console.log("Stop bubbling because there are text on the input. Only on multi answers because they can be unselected. ")
                e.stopPropagation();
            }
        });

        // CONTACT FILES -> Option is selected by default
        $(".survey-question[data-questiontype='CONTACT_UPLOAD_FILES'] .survey-question-answer.ANSWER_FILES").click()
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
        var questionType = question.getAttribute("data-questionType");
        var answersValid = true;

        var questionOptionsSelectedIdx; // IE10 not supports forEach
        for (questionOptionsSelectedIdx = 0; questionOptionsSelectedIdx < questionOptionsSelected.length; questionOptionsSelectedIdx++) {
            var questionOption = questionOptionsSelected[questionOptionsSelectedIdx];
            var questionOptionType = questionOption.getAttribute(surveyFunctions.QUESTION_OPTION_ATTR_TYPE)
            answersValid = surveyFunctions._checkValidAnswerType[questionOptionType](questionOption,questionType,question) && answersValid;
        }
        answersValid = answersValid && surveyFunctions._checkValidQuestion[questionType](question);
        return answersValid;
    },

    _checkValidQuestion:{
        ONE_OPTION: function(question){return true;},
        MULTIPLE_OPTION : function(question){return true;},
        MULTIPLE_OPTION_WEIGHTED : function(question){
            var questionPoints = parseFloat(question.getAttribute("data-points"));
            var maxAnswers = parseFloat(question.getAttribute("data-maxanswers"));
            if (maxAnswers == 0){
                // The question has no limit choosing answers. So the max is the number of the available options.
                maxAnswers = $(question).find(".survey-question-answer").length;
            }
            var minAnswers = parseFloat(question.getAttribute("data-minanswers"));
            var limitMaxPoints = questionPoints * maxAnswers;
            var limitMinPoints = questionPoints * minAnswers;
            var questionValidationData ={
                valid:false,
                msg: "No valid sum",
                question: question
            }
            if (questionPoints <= 0){
                // USER NO LOGGED // CHAPU
                questionValidationData.msg = i18n.survey.questions.header.extrainfo.multi.points.noLoggedError+" <a href='#modal' onclick=\"$('#registro').modal('show');\">"+i18n.register.head.login+"</a>"
                $(questionValidationData.question).find(".survey-question-extra-info-points").html(questionValidationData.msg)
                this._handlePrintingQuestionError(questionValidationData);
                return questionValidationData.valid;
            }
            var questionOptionsSelected = question.querySelectorAll(".survey-question-answer.checked");
            var answersValid = true;

            var questionOptionsSelectedIdx; // IE10 not supports forEach
            var summedPoints = 0;
            var validInputsMax = true;
            for (questionOptionsSelectedIdx = 0; questionOptionsSelectedIdx < questionOptionsSelected.length; questionOptionsSelectedIdx++) {
                var questionOption = questionOptionsSelected[questionOptionsSelectedIdx];
                var textNumberInputNodes = questionOption.querySelectorAll(".option-extra-content input");
                var textNumberInput = textNumberInputNodes[0];
                var rawData = textNumberInput.value
                var floatNumber = parseFloat(rawData);
                summedPoints = summedPoints + floatNumber;
                if (floatNumber > questionPoints){
                    validInputsMax = false;
                    var rawMsg = i18n.kuorum.web.commands.payment.survey.QuestionOptionCommand.text.overflow;
                    var validData = {
                        valid: false,
                        msg:rawMsg.replace(new RegExp("\\{0\\}", 'g'), questionPoints),
                        input: textNumberInput
                    };
                    surveyFunctions._checkValidAnswerType._handlePrintingError(validData);
                }
            }
            if (!validInputsMax){
                questionValidationData.valid = false;
                questionValidationData.msg = "Overflow of points in one option"
            }else if (limitMaxPoints < summedPoints) {
                questionValidationData.valid = false;
                questionValidationData.msg = "Overflow of points"
            }else if (limitMinPoints > summedPoints){
                questionValidationData.valid = false;
                questionValidationData.msg = "it do not reach the minimum votes"
            }else{
                questionValidationData.valid = true;
                questionValidationData.msg = ""
            }
            this._handlePrintingQuestionError(questionValidationData);
            return questionValidationData.valid;
        },
        ONE_OPTION_WEIGHTED: function(question){
            var questionPoints = parseFloat(question.getAttribute("data-points"));
            var questionValidationData ={
                valid:false,
                msg: "No valid sum",
                question: question
            }
            if (questionPoints <= 0){
                // USER NO LOGGED // CHAPU
                questionValidationData.msg = i18n.survey.questions.header.extrainfo.multi.points.noLoggedError+" <a href='#modal' onclick=\"$('#registro').modal('show');\">"+i18n.register.head.login+"</a>"
                $(questionValidationData.question).find(".survey-question-extra-info-points").html(questionValidationData.msg)
                this._handlePrintingQuestionError(questionValidationData);
                return questionValidationData.valid;
            }
            var questionOptionsSelected = question.querySelectorAll(".survey-question-answer.checked");
            var answersValid = true;

            var questionOptionsSelectedIdx; // IE10 not supports forEach
            var summedPoints = 0;
            for (questionOptionsSelectedIdx = 0; questionOptionsSelectedIdx < questionOptionsSelected.length; questionOptionsSelectedIdx++) {
                var questionOption = questionOptionsSelected[questionOptionsSelectedIdx];
                var textNumberInputNodes = questionOption.querySelectorAll(".option-extra-content input");
                var textNumberInput = textNumberInputNodes[0];
                var rawData = textNumberInput.value
                var floatNumber = parseFloat(rawData);
                summedPoints = summedPoints + floatNumber;
            }
            if (summedPoints != questionPoints){
                questionValidationData.valid = false;
                questionValidationData.msg = "No coinciden las sumas"
            }else{
                questionValidationData.valid = true;
                questionValidationData.msg = ""
            }
            this._handlePrintingQuestionError(questionValidationData);
            return questionValidationData.valid;
            },
        MULTIPLE_OPTION_POINTS: function(question){return this.ONE_OPTION_WEIGHTED(question)},
        TEXT_OPTION: function(question){return true;},
        RATING_OPTION: function(question){return true;},
        CONTACT_UPLOAD_FILES: function(question) {return true;},
        CONTACT_GENDER: function(question){return true;},
        CONTACT_PHONE: function(question){return true;},
        CONTACT_EXTERNAL_ID: function(question){return true;},
        CONTACT_WEIGHT: function(question){return true;},
        CONTACT_BIRTHDATE: function(question){return true;},
        _handlePrintingQuestionError: function(questionValidationData){
            if (questionValidationData.valid){
                $(questionValidationData.question).find(".survey-question-extra-info-points").removeClass("error");
            }else{
                $(questionValidationData.question).find(".survey-question-extra-info-points").addClass("error");
                var questionDOMId = questionValidationData.question.getAttribute("id")
                moveSmooth("#"+questionDOMId)
            }
        },
    },

    _checkValidAnswerType:{
        ANSWER_PREDEFINED: function(questionAnswerOption,questionType){return true;},
        ANSWER_TEXT: function(questionAnswerOption,questionType){
            var textAreaNodes = questionAnswerOption.querySelectorAll(".option-extra-content textarea");
            var textArea = textAreaNodes[0];
            var validationData = surveyFunctions._checkValidAnswerType._checkInputData(textArea);
            if (validationData.valid){
                validationData = surveyFunctions._checkValidAnswerType._checkValidInputAnswerByQuestionType[questionType](textArea);
            }
            surveyFunctions._checkValidAnswerType._handlePrintingError(validationData);
            return validationData.valid;
        },
        ANSWER_SMALL_TEXT: function(questionAnswerOption,questionType){
            var inputTextNodes = questionAnswerOption.querySelectorAll(".option-extra-content input");
            var inputTextNode = inputTextNodes[0];
            var validationData = surveyFunctions._checkValidAnswerType._checkInputData(inputTextNode);
            if (validationData.valid){
                validationData = surveyFunctions._checkValidAnswerType._checkValidInputAnswerByQuestionType[questionType](inputTextNode);
            }
            surveyFunctions._checkValidAnswerType._handlePrintingError(validationData);
            return validationData.valid;
        },
        ANSWER_DATE: function(questionAnswerOption,questionType){
            var textDateInputNodes = questionAnswerOption.querySelectorAll(".option-extra-content input");
            var textDateInput = textDateInputNodes[0];
            var validationData = surveyFunctions._checkValidAnswerType._checkInputData(textDateInput);
            if (validationData.valid){
                validationData = surveyFunctions._checkValidAnswerType._checkValidInputAnswerByQuestionType[questionType](textDateInput);
            }
            surveyFunctions._checkValidAnswerType._handlePrintingError(validationData);
            return validationData.valid;
        },
        ANSWER_PHONE: function(questionAnswerOption,questionType){
            var phoneInputNodes = questionAnswerOption.querySelectorAll(".option-extra-content input");
            var phoneInput = phoneInputNodes[0];
            var phoneValidationData = surveyFunctions._checkValidAnswerType._checkInputData(phoneInput);
            // var prefixInputNodes = questionAnswerOption.querySelectorAll(".option-extra-content select");
            // var prefixInput = prefixInputNodes[0];
            // var prefixPhoneValidationData = surveyFunctions._checkValidAnswerType._handlePrintingError(phoneValidationData);
            // return phoneValidationData.valid && prefixPhoneValidationData.valid;
            if (phoneValidationData.valid){
                phoneValidationData = surveyFunctions._checkValidAnswerType._checkValidInputAnswerByQuestionType[questionType](phoneInput);
            }
            surveyFunctions._checkValidAnswerType._handlePrintingError(phoneValidationData);
            return phoneValidationData.valid;
        },
        ANSWER_NUMBER: function(questionAnswerOption,questionType, question){
            var textNumberInputNodes = questionAnswerOption.querySelectorAll(".option-extra-content input");
            var textNumberInput = textNumberInputNodes[0];
            var validationData = "";
            var questionPoints = parseFloat(question.getAttribute("data-points"));

            validationData = (questionType === "ONE_OPTION_WEIGHTED" || (questionPoints <= 1 && questionType === 'MULTIPLE_OPTION_WEIGHTED')) ?
                surveyFunctions._checkValidAnswerType.ANSWER_NUMBER_SPECIAL_CASE(questionAnswerOption, questionType, question, textNumberInput, questionPoints) :
                surveyFunctions._checkValidAnswerType.ANSWER_NUMBER_REGULAR_CASE(validationData, questionType, textNumberInput)


            return validationData.valid
        },
        ANSWER_NUMBER_REGULAR_CASE(validationData, questionType, textNumberInput){
            validationData = surveyFunctions._checkValidAnswerType._checkValidInputAnswerByQuestionType[questionType](textNumberInput);
            if (validationData.valid){
                validationData = surveyFunctions._checkValidAnswerType._checkInputData(textNumberInput);
            }
            surveyFunctions._checkValidAnswerType._handlePrintingError(validationData);
            return validationData;
        },
        ANSWER_NUMBER_SPECIAL_CASE(questionAnswerOption, questionType, question, textNumberInput, questionPoints) {
            var validationData;

            if(questionPoints <= 1){
                validationData = surveyFunctions._checkValidAnswerType._checkWeightedOptionOnePoint(questionAnswerOption ,textNumberInput);
            }
            if(questionType === "ONE_OPTION_WEIGHTED" && questionPoints > 1){
                // Check basic data inputs -> No empty and greater than 0
                var validationData = surveyFunctions._checkValidAnswerType.ANSWER_NUMBER_REGULAR_CASE(undefined, questionType, textNumberInput);
                if (!validationData.valid){
                    // Basic validation fails
                    surveyFunctions._checkValidAnswerType._handlePrintingError(validationData);
                    return validationData.valid;
                }
                validationData = surveyFunctions._checkValidAnswerType._checkOneOptionWeightedPointsSpecialCase(question, questionPoints);
            }
            surveyFunctions._checkValidAnswerType._handleSpecialCase(validationData, question);
            return  validationData;
        },
        ANSWER_FILES: function(questionAnswerOption,questionType){
            var textFileListNodes = questionAnswerOption.querySelectorAll("li.qq-upload-success");
            var validationData = {
                valid: textFileListNodes.length > 0,
                msg:"UPLOAD AT LEAST ONE FILE",
                input: textFileListNodes[0]
            }
            if (validationData.valid){
                validationData = surveyFunctions._checkValidAnswerType._checkValidInputAnswerByQuestionType[questionType](textFileListNodes[0]);
            }
            if (!validationData.valid){
                $(questionAnswerOption).find(".survey-question-option-extra-info").addClass("error");
            }
            // surveyFunctions._checkValidAnswerType._handlePrintingError(validationData);
            return validationData.valid
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
        _checkWeightedOptionOnePoint:function(questionAnswerOption, textNumberInput) {
            if(questionAnswerOption.getAttribute("class", "checked")){
                textNumberInput.value = 1;
            } else {
                textNumberInput.value = 0;
            }
            return validationData = {
                valid: true,
                msg:"",
                input: textNumberInput
            };
        },
        _checkOneOptionWeightedPointsSpecialCase:function(question, questionPoints) {
            var textSpecialNumberInputNodes = question.querySelectorAll(".option-extra-content input");
            var summedPoints = 0;
            var rawData;
            var floatNumber;
            var answerId;
            for(var i= 0; i < textSpecialNumberInputNodes.length; i++) {
                floatNumber = 0;
                rawData = "";
                answerId = textSpecialNumberInputNodes[i].id.split("_")[0];
                if ($("#question-option-"+answerId).hasClass("checked")) {
                    rawData = textSpecialNumberInputNodes[i].value
                }
                floatNumber = !isNaN(parseFloat(rawData))?parseFloat(rawData):0;
                summedPoints += floatNumber;
            }
            return surveyFunctions._checkValidAnswerType._checkInputOneOptionWeightedData(textSpecialNumberInputNodes, questionPoints ,summedPoints);
        },
        _checkInputOneOptionWeightedData:function (inputNode, expectedPoints, actualPoints) {
            if (actualPoints != expectedPoints){
                return {
                    valid: false,
                    msg:i18n.kuorum.web.commands.payment.survey.QuestionOptionCommand.number.points + " " + expectedPoints,
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
            }else {
                $(validationData.input).siblings(".error").remove();
                validationData.input.classList.add("error");
                var errorNode = document.createElement("span");
                errorNode.classList = "error";
                errorNode.innerHTML = validationData.msg
                validationData.input.parentNode.insertBefore(errorNode, validationData.input.nextSibling);
            }
        },_handleSpecialCase(validationData, question) {
            $(".survey-question-extra-info."+question.id+" span.error").remove();
            if(!validationData.valid) {
                var errorDivNode = $(".survey-question-extra-info." + question.id);
                errorDivNode.append("<span class='error'>"+validationData.msg+"</span>");
            }
        },
        _checkValidInputAnswerByQuestionType:{
            ONE_OPTION: function(inputNode){return surveyFunctions._checkValidAnswerType._checkValidInputAnswerByQuestionType.defaultValidation(inputNode);},
            MULTIPLE_OPTION : function(inputNode){return surveyFunctions._checkValidAnswerType._checkValidInputAnswerByQuestionType.defaultValidation(inputNode);},
            MULTIPLE_OPTION_WEIGHTED : function(inputNode){return surveyFunctions._checkValidAnswerType._checkValidInputAnswerByQuestionType.ONE_OPTION_WEIGHTED(inputNode);},
            ONE_OPTION_WEIGHTED: function(inputNode, weigth, summedPoints){
                var valid = parseFloat(inputNode.value) >0;
                return {
                    valid: valid,
                    msg:i18n.kuorum.web.commands.payment.survey.QuestionOptionCommand.number.negative,
                    input: inputNode
                };
            },
            MULTIPLE_OPTION_POINTS: function(inputNode){return surveyFunctions._checkValidAnswerType._checkValidInputAnswerByQuestionType.ONE_OPTION_WEIGHTED(inputNode)},
            TEXT_OPTION:            function(inputNode){return surveyFunctions._checkValidAnswerType._checkValidInputAnswerByQuestionType.defaultValidation(inputNode);},
            RATING_OPTION:          function(inputNode){return surveyFunctions._checkValidAnswerType._checkValidInputAnswerByQuestionType.defaultValidation(inputNode);},
            CONTACT_UPLOAD_FILES:   function(inputNode){return surveyFunctions._checkValidAnswerType._checkValidInputAnswerByQuestionType.defaultValidation(inputNode);},
            CONTACT_GENDER:         function(inputNode){return surveyFunctions._checkValidAnswerType._checkValidInputAnswerByQuestionType.defaultValidation(inputNode);},
            CONTACT_PHONE:          function(inputNode){return surveyFunctions._checkValidAnswerType._checkValidInputAnswerByQuestionType.defaultValidation(inputNode);},
            CONTACT_EXTERNAL_ID:    function(inputNode){return surveyFunctions._checkValidAnswerType._checkValidInputAnswerByQuestionType.defaultValidation(inputNode);},
            CONTACT_WEIGHT:         function(inputNode){return surveyFunctions._checkValidAnswerType._checkValidInputAnswerByQuestionType.defaultValidation(inputNode);},
            CONTACT_BIRTHDATE:      function(inputNode){return surveyFunctions._checkValidAnswerType._checkValidInputAnswerByQuestionType.defaultValidation(inputNode);},
            defaultValidation:      function(inputNode){
                return {
                    valid: true,
                    msg:"",
                    input: inputNode
                };
            }
        },

    },

    _singleOptionNextButtonClick : function(question){
        // event.preventDefault();
        // var question = event.currentTarget.parentElement.parentElement.parentElement;
        var answers = question.getElementsByClassName('survey-question-answers')[0];
        var selectedAnswer = question.getAttribute('data-answer-selected');
        //var answer = answers.querySelector('[data-answer-id="' + selectedAnswer + '"]');
        if (selectedAnswer) {
            // UPDATE NUM ANSWERS
            //answer.setAttribute("data-numanswers", parseInt(answer.getAttribute("data-numanswers"))+1);
            //question.setAttribute("data-numanswers", parseInt(question.getAttribute("data-numanswers"))+1);

            var questionId = parseInt(question.getAttribute('data-question-id'), 10);
            var nextQuestionId = surveyFunctions._getNextQuestionId(question);
            
            var successFunction = function(questionRSDTO){
                surveyFunctions._transformExtraDataNoEditable(questionId, true)
                surveyFunctions._nextQuestion(questionId,nextQuestionId);
                // surveyFunctions._setProgressBarsPercent(questionId);
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
        var nextQuestionIdAttributeName = 'data-nextQuestionId';
        var nextQuestionId = NaN;
        if (answer!= undefined && answer.hasAttribute(nextQuestionIdAttributeName)){
            nextQuestionId = parseInt(answer.getAttribute('data-nextQuestionId'), 10);
        }
        return nextQuestionId;
    },
    _ratingOptionNextButtonClick : function(question){
        // event.preventDefault();
        // var question = event.currentTarget.parentElement.parentElement.parentElement;
        var answers = question.getElementsByClassName('survey-question-answers')[0];
        var selectedAnswer = question.getAttribute('data-answer-selected');
        //var answer = answers.querySelector('[data-answer-id="' + selectedAnswer + '"]');
        //var options = question.querySelectorAll('.survey-question-answer'); // Html collection to array
        if (selectedAnswer) {
            // UPDATE NUM ANSWERS
            //answer.setAttribute("data-numanswers", parseInt(answer.getAttribute("data-numanswers"))+1);
            //question.setAttribute("data-numanswers", parseInt(question.getAttribute("data-numanswers"))+1);

            var questionId = parseInt(question.getAttribute('data-question-id'), 10);

            var successFunction = function(questionRSDTO){
                surveyFunctions._transformExtraDataNoEditable(questionId, true)
                // surveyFunctions._setProgressBarsPercent(questionId);
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
            //var numQuestionAnswers = parseInt(question.getAttribute("data-numAnswers"));
            //numQuestionAnswers= numQuestionAnswers+1;
            //question.setAttribute("data-numAnswers",numQuestionAnswers);

            var questionId = parseInt(question.getAttribute('data-question-id'), 10);

            var successFunction = function(questionRSDTO){
                surveyFunctions._transformExtraDataNoEditable(questionId, true)
                surveyFunctions._nextQuestion(questionId);
                // surveyFunctions._setProgressBarsPercent(questionId);
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
        if (currentQuestion.parentElement.classList.contains("survey-vote-secret")){
            surveyFunctions._clearAnswer(currentQuestion);
        }
        surveyFunctions._updateQuestionStats(currentQuestionId);
    },

    _clearAnswer: function(question){
        $(question).find(".survey-question-answer.checked .option-extra-content").remove();
        $(question).find(".survey-question-answer.checked").removeClass("checked");
        $(question).attr("data-answer-selected","")
        $(question).find(".survey-question-answer").attr("data-question-extra-content","")
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
        var answerOptions = Array.from(question.getElementsByClassName('survey-question-answer'))
        var answerOptionIdx; // IE10 not supports forEach
        for (answerOptionIdx = 0; answerOptionIdx < answerOptions.length; answerOptionIdx++) {
            var answerOption = answerOptions[answerOptionIdx];
            var percentageOptionProgressBar = Math.round(parseFloat(answerOption.getAttribute("data-optionStats-percentage"))*100);
            answerOption.getElementsByClassName("progress-bar-counter")[0].textContent=percentageOptionProgressBar +"%";
            var optionProgressBar = answerOption.getElementsByClassName("progress-bar")[0]
            optionProgressBar.style.width=percentageOptionProgressBar+"%"
            optionProgressBar.setAttribute("aria-valuenow",percentageOptionProgressBar)
            optionProgressBar.setAttribute("data-answer-percent",percentageOptionProgressBar)
            optionProgressBar.setAttribute("data-answer-percent-selected",percentageOptionProgressBar)
        }
    },

    _setProgressBarsPercentRatio:function(questionId) {
        var question = document.querySelector('[data-question-id="' + questionId + '"]');
        //var numAnswers = parseInt(question.getAttribute("data-numAnswers"))
        var answerOptions = Array.from(question.getElementsByClassName('survey-question-answer'))
        var answerOptionIdx; // IE10 not supports forEach
        // var totalScore = 0;
        // var totalScoreBase = 0;
        var rating = 0;
        for (answerOptionIdx = 0; answerOptionIdx < answerOptions.length; answerOptionIdx++) {
            var answerOption = answerOptions[answerOptionIdx];
            var numOptionAnswers = parseInt(answerOption.getAttribute("data-optionStats-votes"))
            var percentageOption = parseFloat(answerOption.getAttribute("data-optionStats-percentage"));
            var percentageOptionProgressBar = Math.round(percentageOption*100);
            answerOption.getElementsByClassName("progress-bar-counter")[0].textContent=(percentageOptionProgressBar +"%");
            var optionProgressBar = answerOption.getElementsByClassName("progress-bar")[0]
            optionProgressBar.style.width=percentageOptionProgressBar+"%"
            optionProgressBar.setAttribute("aria-valuenow",percentageOptionProgressBar)
            optionProgressBar.setAttribute("data-answer-percent",percentageOptionProgressBar)
            optionProgressBar.setAttribute("data-answer-percent-selected",percentageOptionProgressBar)
            var optionValue = answerOptionIdx+1;
            // totalScore += (answerOptionIdx+1) * numOptionAnswers;
            // totalScoreBase += (answerOptionIdx) * numOptionAnswers;
            rating += (optionValue * percentageOption)
        }
        // var rating = totalScore / numAnswers;
        var numOptions = answerOptions.length;
        var ratingPercentage = (rating-1)/(numOptions-1); // BASE OF PERCENTAGE is -1
        var optionProgressBar = question.querySelector(".survey-question-progress-info .progress-bar")
        optionProgressBar.style.width=(ratingPercentage*100)+"%";
        optionProgressBar.setAttribute("aria-valuenow",rating.toFixed(1))
        optionProgressBar.setAttribute("data-answer-percent",rating.toFixed(1))
        optionProgressBar.setAttribute("data-answer-percent-selected",rating.toFixed(1))
        var optionProgressBarCounter = question.querySelector(".survey-question-progress-info .progress-bar-counter");
        optionProgressBarCounter.innerHTML = rating.toFixed(1) +"<span class='progress-bar-counter-total'> / "+ numOptions+"</span>";
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
                    var uploaderContainerId = $answerOption.find(".qq-uploader-multiple-files").parent().attr("id")
                    multipleFileUploaders[uploaderContainerId].disable();
                    $answerOption.find(".survey-question-option-extra-info").removeClass("error");
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
        }else if ($(answer).find(".only-predefined-option").length >0){
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
        var questionType = question.getAttribute('data-questiontype');
        var minAnswers = parseInt(question.getAttribute('data-minAnswers'));
        var maxAnswers = parseInt(question.getAttribute('data-maxAnswers')); // 0 means no limit
        var selectedAnswers = (question.getAttribute('data-answer-selected') !== "") ? JSON.parse(question.getAttribute('data-answer-selected')) : "";
        var nextButton = question.querySelector('.footer .next-section button');
        var numOptionAnswers = parseInt(answer.getAttribute("data-optionStats-votes"))

        // MULTIPLE_OPTION_WEIGHTED -> Has no limit. The unique limit is the max points that the voter can distribute.
        var maxAnswersReached = false;

        maxAnswersReached = function (operator){
            // Instead of a var, this logic should be reevaluated. That's why is not using a var.
            var overflowMax
            switch(operator) {
                case ">=": overflowMax = selectedAnswers.length >= maxAnswers ; break;
                case ">": overflowMax = selectedAnswers.length > maxAnswers ; break;
                case "==":
                default:overflowMax = selectedAnswers.length == maxAnswers ; break;
            }
            return maxAnswers>0 && (overflowMax && questionType != "MULTIPLE_OPTION_WEIGHTED");
        }
        if (!!selectedAnswers === true && Array.isArray(selectedAnswers)) {
            var answerPosition = selectedAnswers.indexOf(answer.getAttribute('data-answer-id'));
            if (answerPosition === -1 && maxAnswersReached(">=") ) {
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
        answer.setAttribute("data-optionStats-votes",numOptionAnswers)
        question.setAttribute('data-answer-selected',  (selectedAnswers.length > 0) ? JSON.stringify(selectedAnswers) : '');
        if (maxAnswersReached("==")){
            $(question).find(".survey-question-answer:not(.checked)").addClass("disabled")
        }else{
            $(question).find(".survey-question-answer:not(.checked)").removeClass("disabled")
        }
        if (selectedAnswers.length === 0 || selectedAnswers.length < minAnswers || maxAnswersReached(">")){
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

        if(question.classList.contains("rating-answer")) {
            surveyFunctions._setProgressBarsPercentRatio(questionId)
        }else{
            surveyFunctions._setProgressBarsPercentOneOption(questionId)
        }
    },

    _sendQuestionAnswers:function(questionId, successFunction, failFunction){
        var question = document.querySelector('.survey-question[data-question-id="' + questionId + '"]');
        var button = question.querySelector('.actions button');
        var url = button.getAttribute("data-postUrl");
        var answerIds = JSON.parse(question.getAttribute("data-answer-selected"));
        var questionType = question.getAttribute("data-questionType");
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
                successFunction(data.question);
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

    _updateQuestionStats: function(questionId){
        updateQustionOptionWithStats = function(questionOptionStats){
            var $questionOption = $("#question-option-"+questionOptionStats.questionOptionId);
            $questionOption.attr("data-optionstats-total", questionOptionStats.total);
            $questionOption.attr("data-optionstats-votes", questionOptionStats.optionVotes);
            $questionOption.attr("data-optionstats-percentage", questionOptionStats.percentage);
        }
        updateQuestionWithStats = function($question, questionStats) {
            $question.attr("data-numAnswers", questionStats.answerAmount);
            // questionStats.questionOptionStats.forEach(updateQustionOptionWithStats);
            var answerOptionIdx; // IE10 not supports forEach
            for (answerOptionIdx = 0; answerOptionIdx < questionStats.questionOptionStats.length; answerOptionIdx++) {
                var questionOptionStats =  questionStats.questionOptionStats[answerOptionIdx];
                updateQustionOptionWithStats(questionOptionStats);
            }
            surveyFunctions._setProgressBarsPercent(questionStats.questionId);
        }
        var $question = $("#question-"+questionId);
        var urlQuestionStats = $question.attr("data-ajaxQuestionStats");
        var data = {};
        $.ajax({
            type: "GET",
            url: urlQuestionStats,
            data: $.param(data),
            success: function(data){
                if (data.status = "SUCCESS"){
                    updateQuestionWithStats($question, data.questionStats);
                }else{
                    console.log("Error recovering stats")
                }
            },
            dataType: 'json'
        }).fail(function(xhr, status, error) {
            console.log(error);
        }).always(function() {
            // pageLoadingOff("Survey :: save answer. Question Id: "+questionId)
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
            if ($(extraDataSelector).length > 0){
                data = surveyFunctions._getExtraInfoAnswer($(answerOptionSelector))
            }
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