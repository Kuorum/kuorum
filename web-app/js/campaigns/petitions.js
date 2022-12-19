
$(function () {

    var signButton = $('.petition-sign');
    signButton.on("click", petitionFunctions.bindSignClick);
    noLoggedCallbacks['signPetitionNoLogged'] = function () {
        var buttonId = $('#registro').find("form").attr("data-buttonId");
        var $button = $("#" + buttonId);
        pageLoadingOff();
        petitionFunctions.onClickSignPetition($button, noLoggedCallbacks.reloadPage);
    };

    $(".petition-sign-call-to-action").on("click", function (e) {
        e.preventDefault();
        signButton.click();
    })
});


var petitionFunctions = {
    bindSignClick: function(event){
        event.preventDefault();
        event.stopPropagation();
        var $button = $(this);
        var loggedUser = $button.attr("data-loggedUser");
        if (loggedUser == undefined || loggedUser == ""){
            // NO LOGGED
            var buttonId = guid();
            $button.attr("id", buttonId);
            $('#registro').find("form").attr("callback", "signPetitionNoLogged");
            $('#registro').find("form").attr("data-buttonId", buttonId);
            $('#registro').modal('show');
        }else{
            petitionFunctions.onClickSignPetition($button);
        }
    },
    onClickSignPetition:function($button, callback) {
        if (isPageLoading()) {
            return;
        }
        pageLoadingOn();
        var params = {
            callback: callback,
            $button: $button
        };
        var sign = $button.hasClass('on');
        console.log($button)
        console.log("Already signed: " + sign)
        if (sign) {
            // ALREADY SIGNED
            pageLoadingOff();
            var petitionId = $button.attr('data-petitionId');
            console.log("Already signed: " + petitionId)
            var $modalAlreadySigned = $("#petition-already-signed-modal-" + petitionId);
            $modalAlreadySigned.modal("show");
        } else {
            var executableFunction = new userValidatedByDomain.ExcutableFunctionCallback(petitionFunctions.__requestSignReport, params);
            userValidatedByDomain.executeClickButtonHandlingValidations($button, executableFunction);
        }
    },
    __requestSignReport: function (params) {
        pageLoadingOff();
        var callback = params.callback;
        var $button = params.$button;
        var signUrl = $button.attr('href');
        var petitionId = $button.attr('data-petitionId');
        var requestPdfUrl = $button.attr('data-requestPdfUrl');
        var viewPdfUrl = $button.attr('data-viewPdfUrl');
        var $modal = $("#petition-sign-pdf-modal-" + petitionId);
        var $modalSubmitButton = $modal.find(".modal-sign");
        var $modalMessage = $modal.find("#modal-pdf-message")
        var $downloadButton = $modal.find(".modal-download")

        $modalMessage.html($modalMessage.attr("data-message-noEmail-loading"))
        $downloadButton.addClass("disabled")

        $modalSubmitButton.attr("href", signUrl);
        if ($modalSubmitButton.attr("href") !== '') {
            // BIND CLICK
            $modalSubmitButton.on("click", function (e) {
                e.preventDefault();
                petitionFunctions.__executableAsyncPetitionSign(params)
            });
        }

        var data = {};

        $.ajax({
            type: 'POST',
            url: requestPdfUrl,
            data: data,
            success: function (petitionSign) {

                $modal.modal("show");
                petitionFunctions._loadPDFIframe($modal, viewPdfUrl);
            },
            error: function () {

            },
            complete: function () {
                pageLoadingOff();
            }
        });

    },
    __executableAsyncPetitionSign: function (params) {
        var callback = params.callback;
        var $button = params.$button;
        var url = $button.attr('href');

        var sign = $button.find('.fa-microphone').hasClass('fal');
        var petitionUserId = $button.attr('data-petitionUserId');
        var data = {
            sign: sign,
            petitionUserId:petitionUserId
        };

        $.ajax({
            type: 'POST',
            url: url,
            data: data,
            success: function(petitionSign){
                var petitionRSDTO = petitionSign.petition;
                var $buttons = $(".petition-sign-"+petitionRSDTO.id);
                var petitionId = $button.attr('data-petitionId');
                var $modal = $("#petition-sign-pdf-modal-" + petitionId);
                $buttons.toggleClass('active');
                $buttons.toggleClass('on');
                $buttons.find('.fa-microphone').toggleClass("fas fal");
                $buttons.find('.number').text(petitionRSDTO.signs);
                $button.blur();
                $("#module-petition-user-signs").html(petitionSign.signsHtml);
                if (callback != undefined){
                    callback();
                }
                $(".petition-sign-call-to-action").fadeOut();
                $modal.modal("hide");
            },
            error: function () {

            },
            complete: function () {
                pageLoadingOff();
            }
        });
    },
    _checkPDFReadyHelper: {
        NUM_REQUEST_REPORT: 0,
        WAIT_BETWEEN_REQUESTS_MS: 1000,
        REPORT_URL: undefined,
        SUCCESS_FUNCTION_LOADED: undefined,
        ERROR_FUNCTION_LOADED: undefined,
        MAX_RELOAD_ATTEMPTS: 20,
        PDF_READY: false,
        INITIALIZED: false,
        init: function (url, successFunction, errorFunction) {
            if (!petitionFunctions._checkPDFReadyHelper.INITIALIZED) {
                console.log("PDF REPORT :: Pdf loading system initialized")
                petitionFunctions._checkPDFReadyHelper.INITIALIZED = true;
                petitionFunctions._checkPDFReadyHelper.NUM_REQUEST_REPORT = 0;
                petitionFunctions._checkPDFReadyHelper.REPORT_URL = url;
                petitionFunctions._checkPDFReadyHelper.SUCCESS_FUNCTION_LOADED = successFunction;
                petitionFunctions._checkPDFReadyHelper.ERROR_FUNCTION_LOADED = errorFunction;
                petitionFunctions._checkPDFReadyHelper._checkPDFSuccessViaAjaxHandlingAttempts();
            }
        },
        restart: function () {
            petitionFunctions._checkPDFReadyHelper.NUM_REQUEST_REPORT = 0;
            petitionFunctions._checkPDFReadyHelper._checkPDFSuccessViaAjaxHandlingAttempts();
        },
        _checkPDFSuccessViaAjaxHandlingAttempts: function () {
            console.log("PDF REPORT :: Checking PDF via ajax")
            if (petitionFunctions._checkPDFReadyHelper.NUM_REQUEST_REPORT < petitionFunctions._checkPDFReadyHelper.MAX_RELOAD_ATTEMPTS) {
                petitionFunctions._checkPDFReadyHelper._checkPDFSuccessViaAjax();
            } else {
                console.log("PDF REPORT :: Error :: Max attempts reloading Iframe")
                petitionFunctions._checkPDFReadyHelper.ERROR_FUNCTION_LOADED();
            }
            petitionFunctions._checkPDFReadyHelper.NUM_REQUEST_REPORT++;
        },
        _checkPDFSuccessViaAjax: function (e) {
            console.log("PDF REPORT :: Checking PDF via ajax. Times= " + petitionFunctions._checkPDFReadyHelper.NUM_REQUEST_REPORT)
            $.ajax({
                type: "HEAD",
                async: false,
                url: petitionFunctions._checkPDFReadyHelper.REPORT_URL,
            }).done(function () {
                petitionFunctions._checkPDFReadyHelper.PDF_READY = true;
                petitionFunctions._checkPDFReadyHelper.SUCCESS_FUNCTION_LOADED();
            }).fail(function () {
                setTimeout(function () {
                    petitionFunctions._checkPDFReadyHelper._checkPDFSuccessViaAjaxHandlingAttempts()
                }, petitionFunctions._checkPDFReadyHelper.WAIT_BETWEEN_REQUESTS_MS);
            }).always(function () {
            });
        }
    },
    _loadPDFIframe: function ($modal, viewPdfUrl) {
        const iframe = $modal.find("iframe");
        const $loading = $modal.find(".loading");
        $loading.show()
        iframe.hide();

        const successFunction = function () {
            iframe.attr("src", viewPdfUrl);
            console.log("PDF Loaded")
            petitionFunctions._showIframeWithPdf($modal);
        }

        const errorFunction = function () {
            console.log("MAX ATTEMPTS ACHIEVE")
            petitionFunctions._checkPDFReadyHelper.restart();
        }
        petitionFunctions._checkPDFReadyHelper.init(viewPdfUrl, successFunction, errorFunction)
    },
    _checkPdfReady($modal) {

    },
    _showIframeWithPdf: function ($modal) {
        var $iframe = $modal.find("iframe");
        var $loading = $modal.find(".loading");
        var $downloadButton = $modal.find(".modal-download")
        var $modalMessage = $modal.find("#modal-pdf-message")
        $modalMessage.html($modalMessage.attr("data-message-noEmail-loaded"))
        $downloadButton.removeClass("disabled")
        $downloadButton.attr("href", $iframe.attr("src"))
        $loading.hide();
    }
};