<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="domain.config.firstConfig.steps.step1.title"/></title>
    <meta name="layout" content="columnCLayout">
    <meta name="robots" content="noindex, nofollow">
    <parameter name="bodyCss" value="configDomainProcess"/>
    <r:require modules="forms"/>
</head>

<content tag="mainContent">

    <ol class="stepsSign stepsSignSecondaryColor">
        <li class="active"><div class="step-label"><g:message code="domain.config.firstConfig.steps.step1.title"/></div></li>
        <li class=""><div class="step-label"><g:message code="domain.config.firstConfig.steps.step2.title"/></div></li>
    </ol>

    <formUtil:validateForm bean="${command}" form="signup-custom-site"/>
    <g:uploadForm method="POST" mapping="adminDomainRegisterStep1" name="signup-custom-site" role="form" class="signup-custom-site slowForm" data-slowLoading-texts='["${g.message(code:'form.submit.slowLoading.modal.text1')}","${g.message(code:'form.submit.slowLoading.modal.text2')}"]'>
        <fieldset class="row">
            <div class="form-group col-md-6">
                <formUtil:input command="${command}" field="slogan" showLabel="true"/>
            </div>
            <div class="form-group col-md-6">
                <formUtil:input command="${command}" field="subtitle" showLabel="true"/>
            </div>
        </fieldset>
        <fieldset class="row">
            <div class="form-group col-md-6">
                <label for="logo"><g:message code="kuorum.web.admin.domain.DomainConfigStep1Command.logo.label"/> *</label>

                <div class="input-group input-group-lg">
                    <input id="sign-in-step-5__input-file"
                           name="logo"
                           type="file"
                           class="filestyle"
                           data-icon="false"
                           data-buttonText="${message(code: 'kuorum.web.admin.domain.DomainConfigStep1Command.logo.selectFile')}"
                           data-buttonName="btn-blue btn-lg inverted"
                           data-buttonBefore="true"
                           data-placeholder="${message(code: 'tools.contact.import.csv.selectFile.noSelection')}">
                </div>
                %{--<div class="input-group">--}%
                    %{--<label class="input-group-addon">--}%
                        %{--<span class="btn btn-blue inverted">--}%
                            %{--<g:message code="kuorum.web.admin.domain.DomainConfigStep1Command.logo.selectFile"/>--}%
                            %{--<input id="sign-in-step-5__input-file" name="logo" type="file" style="display: none;">--}%
                        %{--</span>--}%
                    %{--</label>--}%
                    %{--<input id="logo" type="text" class="form-control input-lg" readonly>--}%
                %{--</div>--}%
                <div class="errors">
                    <g:renderErrors bean="${command}" field="logoName"/>
                </div>
            </div>
            <div class="form-group col-md-6">
                <label for="color-picker-hex-code"><g:message code="kuorum.web.admin.domain.DomainConfigStep1Command.colorHexCode.label"/> *</label>
                <div class="input-append input-group">
                    <span tabindex="100" class="add-on input-group-addon">
                        <label class="">
                            <input class="input-color-picker jscolor {closable:true,closeText:'Close', valueElement:'color-picker-hex-code'}" readonly>
                        </label>
                    </span>
                    <input type="text" required aria-required="true" id="color-picker-hex-code" name="colorHexCode" class="form-control input-lg" value="${command.colorHexCode}" placeholder="">
                </div>
            </div>
        </fieldset>

        <fieldset>
            <label for="color-picker-hex-code"><g:message code="kuorum.web.admin.domain.DomainConfigStep1Command.carousel.label"/>*</label>

            <div class="form-group">
                <div class="col-sm-4">
                    <formUtil:editImage command="${command}" field="slideId1" fileGroup="${kuorum.core.FileGroup.DOMAIN_SLIDE_IMAGE}"/>
                </div>
                <div class="col-sm-4">
                    <formUtil:editImage command="${command}" field="slideId2" fileGroup="${kuorum.core.FileGroup.DOMAIN_SLIDE_IMAGE}"/>
                </div>
                <div class="col-sm-4">
                    <formUtil:editImage command="${command}" field="slideId3" fileGroup="${kuorum.core.FileGroup.DOMAIN_SLIDE_IMAGE}"/>
                </div>
            </div>
        </fieldset>

        <fieldset>
            <div class="form-group center">
                <input type="submit" value="${message(code:'tour.dashboard.next')}" class="btn btn-lg btn-blue">
            </div>
        </fieldset>
    </g:uploadForm>
</content>



<content tag="cColumn">
    <div class="custom-url-info-box">
        <h3><g:message code="domain.config.firstConfig.steps.step1.columnc.title"/></h3>
        <p><g:message code="domain.config.firstConfig.steps.step1.columnc.explanation"/></p>
        <h3><g:message code="domain.config.firstConfig.steps.step1.columnc.lookslike.title"/></h3>
        <div class="macbook-pro column">
            <img class="macbook-pro-img" src="${g.resource(dir: "images", file: "macbook-pro.png")}" alt="macbook pro preview">
            <div class="macbook-pro-screen">
                <div class="macbook-pro-screen-header">
                    <img src="${g.resource(dir: "images", file: "logo@2x.png")}" id="macbook-pro-website-logo">
                </div>
                <div class="macbook-pro-screen-body">
                    <h1 id="macbook-pro-website-slogan"><g:message code="kuorum.web.admin.domain.DomainConfigStep1Command.slogan.label"/></h1>
                    <h3 id="macbook-pro-website-subtitle"><g:message code="kuorum.web.admin.domain.DomainConfigStep1Command.subtitle.label"/></h3>
                    <div class="macbook-pro-website-form">
                        <div id="macbook-pro-website-form-input-1" class="macbook-pro-website-form-input"></div>
                        <div id="macbook-pro-website-form-input-2" class="macbook-pro-website-form-input"></div>
                        <div id="macbook-pro-website-form-input-3" class="macbook-pro-website-form-input macbook-pro-website-form-input--primary-color" style="background-color: ${command.colorHexCode}"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</content>

<content tag="modals">
    <div class="modal fade modal-slow-loading" id="modal-loading-signup-custom-site" tabindex="-1" role="dialog" aria-labelledby="modal-loading-signup-custom-site-title" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h4><g:message code="form.submit.slowLoading.modal.title"/></h4>
                    <h4 class="sr-only" id="modal-loading-signup-custom-site-title">Slow action</h4>
                </div>
                <div class="modal-body">
                    <p><g:message code="form.submit.slowLoading.modal.text"/></p>
                    <img src="${g.resource(dir:'images', file: 'logo-waiting.gif')}"/>
                    <div class="modal-loading-signup-custom-site-dynamic-text fa-3x">
                        <p><g:message code="form.submit.slowLoading.modal.text1"/></p>
                        <p><g:message code="form.submit.slowLoading.modal.text2"/></p>
                        <p><g:message code="form.submit.slowLoading.modal.text3"/></p>
                        <p><g:message code="form.submit.slowLoading.modal.text4"/></p>
                        <p><g:message code="form.submit.slowLoading.modal.text5"/></p>
                        <p><g:message code="form.submit.slowLoading.modal.text6"/></p>
                        <p><g:message code="form.submit.slowLoading.modal.text7"/></p>
                    </div>
                </div>
            </div>
        </div>
    </div>


    <r:script>
        $(function () {
            $("#signup-custom-site").on("submit", showModalLoadingSignUpCustomSite)
        });
        function showModalLoadingSignUpCustomSite() {
            var $form = $("#signup-custom-site");
            if (customLandingFormValidation($form)) {
                var numTexts = $("#modal-loading-signup-custom-site .modal-body .modal-loading-signup-custom-site-dynamic-text p").length;
                var count = 0;
                var changeText = function () {
                    $("#modal-loading-signup-custom-site .modal-body .modal-loading-signup-custom-site-dynamic-text p").hide();
                    $($("#modal-loading-signup-custom-site .modal-body .modal-loading-signup-custom-site-dynamic-text p")[count % numTexts]).show();
                    count = count + 1;slogan
                };
                changeText();
                var changeTextModal = setInterval(changeText, 30 * 1000); // EACH 30 seconds
                $("#modal-loading-signup-custom-site").modal({backdrop: 'static', keyboard: false});
                $('#modal-loading-signup-custom-site').on('hidden.bs.modal', function (e) {
                    clearInterval(changeTextModal)
                })
            }else{
                console.log("Error validating form");
                return false;
            }
        }
        function customLandingFormValidation($form){
            return !!(
                $form.valid()
                & landigUploadedFilesValidation($form)
            );
        }

        function landigUploadedFilesValidation($form){
            return !!(
                validateSlideImage($form, 1)
                & validateSlideImage($form, 2)
                & validateSlideImage($form, 3)
                & validateLogo($form)
            );

        }
        function validateSlideImage($form, slidePos){
            var $inputSlide = $form.find("[name=slideId"+slidePos+"]");
            $container = $inputSlide.parent().find(".uploaderImageContainer");
            if ($inputSlide.val()==""){
                if ($container.find(".error").length==0){
                    var msg = [
                        '${g.message(code:'kuorum.web.admin.domain.DomainConfigStep1Command.slideId1.nullable')}',
                        '${g.message(code:'kuorum.web.admin.domain.DomainConfigStep1Command.slideId2.nullable')}',
                        '${g.message(code:'kuorum.web.admin.domain.DomainConfigStep1Command.slideId3.nullable')}'
                    ];
                    var errorSpan = '<span for="input__slideId'+slidePos+'_NEW_" class="error"><span class="tooltip-arrow"></span>'+msg[slidePos-1]+'</span>';
                    $container.append(errorSpan)
                }
                return false;
            }else{
                $container.find(".error").remove();
                return true;
            }
        }
        function validateLogo($form){
            var $inputLogo = $form.find("[name=logo]");
            $errors = $inputLogo.parent().parent().find(".errors");
            if ($inputLogo.val()==""){
                if ($errors.find("ul").length == 0){
                    $error = '<ul><li>${g.message(code:'kuorum.web.admin.domain.DomainConfigStep1Command.logoName.nullable')}</li></ul>';
                    $errors.append($error);
                }
                return false;
            }else{
                $errors.children().remove();
                return true;
            }
        }
    </r:script>
</content>