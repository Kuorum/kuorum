<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="domain.config.firstConfig.steps.step2.title"/></title>
    <meta name="layout" content="columnCLayout">
    <meta name="robots" content="noindex, nofollow">
    <parameter name="bodyCss" value="configDomainProcess"/>
    <r:require modules="forms"/>
</head>

<content tag="mainContent">
    <ol class="stepsSign stepsSignSecondaryColor">
        <li class="active">1<span class="signStepDescription"><g:message code="domain.config.firstConfig.steps.step1.title"/></span></li>
        <li class="">2<span class="signStepDescription"><g:message code="domain.config.firstConfig.steps.step2.title"/></span></li>
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
                <div class="input-group">
                    <label class="input-group-addon">
                        <span class="btn btn-blue inverted">
                            <g:message code="kuorum.web.admin.domain.DomainConfigStep1Command.logo.selectFile"/>
                            <input id="sign-in-step-5__input-file" name="logo" type="file" style="display: none;">
                        </span>
                    </label>
                    <input id="logo" type="text" class="form-control input-lg" readonly>
                </div>
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
                    <input type="text" required aria-required="true" id="color-picker-hex-code" name="colorHexCode" class="form-control input-lg" value="#ff9431" placeholder="">
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
                        <div id="macbook-pro-website-form-input-3" class="macbook-pro-website-form-input macbook-pro-website-form-input--primary-color"></div>
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
                    <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar"><span aria-hidden="true" class="fal fa-times-circle fa"></span><span class="sr-only">Cerrar</span></button>
                    <h4><g:message code="form.submit.slowLoading.modal.title"/></h4>
                    <h4 class="sr-only" id="modal-loading-signup-custom-site-title">Slow action</h4>
                </div>
                <div class="modal-body">
                    <p><g:message code="form.submit.slowLoading.modal.text"/></p>
                    <div class="modal-loading-signup-custom-site-dynamic-text fa-3x">
                        <span class="fa fa-spinner fa-pulse"></span>
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
            if ($form.valid()) {
                var numTexts = $("#modal-loading-signup-custom-site .modal-body .modal-loading-signup-custom-site-dynamic-text p").length;
                var count = 0;
                var changeText = function () {
                    $("#modal-loading-signup-custom-site .modal-body .modal-loading-signup-custom-site-dynamic-text p").hide();
                    $($("#modal-loading-signup-custom-site .modal-body .modal-loading-signup-custom-site-dynamic-text p")[count % numTexts]).show();
                    count = count + 1;
                };
                changeText();
                var changeTextModal = setInterval(changeText, 30 * 1000); // EACH 30 seconds
                $("#modal-loading-signup-custom-site").modal("show");
                $('#modal-loading-signup-custom-site').on('hidden.bs.modal', function (e) {
                    clearInterval(changeTextModal)
                })
            }
        }
    </r:script>
</content>