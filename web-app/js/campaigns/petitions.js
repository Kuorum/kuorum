
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

    _loadPDFIframe: function ($modal, viewPdfUrl) {
        var iframe = $modal.find("iframe");
        var $loading = $modal.find(".loading");
        $loading.show()
        iframe.attr("src", viewPdfUrl);
        iframe.hide();
        var reloadUntilCorrectPdf = function (e) {
            var title = iframe.contents().find("title").html();
            console.log("Reloading until pdf generated. Title = " + title)
            if (title == "File not found") { // Title checking
                console.log("Reloading IFRAME")
                iframe.attr('src', function (i, val) {
                    return val;
                }); // resets iframe
                iframe.attr('data', function (i, val) {
                    return val;
                }); // resets iframe
            } else {
                console.log("PDF Loaded")
                petitionFunctions._showIframeWithPdf($modal);
            }
        }
        iframe.load(reloadUntilCorrectPdf);
    },
    _showIframeWithPdf: function ($modal) {
        var $iframe = $modal.find("iframe");
        var $loading = $modal.find(".loading");
        var $downloadButton = $modal.find(".modal-download")
        var $modalMessage = $modal.find("p")
        $modalMessage.html($modalMessage.attr("data-message-loaded"))
        $downloadButton.removeClass("disabled")
        $downloadButton.attr("href", $iframe.attr("src"))
        //$iframe.show();
        $loading.hide();
    }
};