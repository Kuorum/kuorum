<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="domain.config.firstConfig.steps.step2.title"/></title>
    <meta name="layout" content="columnCLayout">
    <meta name="robots" content="noindex, nofollow">
    <r:require modules="forms"/>
</head>

<content tag="mainContent">
    <ol class="stepsSign stepsSignSecondaryColor">
        <li class="active">1<span class="signStepDescription"><g:message code="domain.config.firstConfig.steps.step1.title"/></span></li>
        <li class="">2<span class="signStepDescription"><g:message code="domain.config.firstConfig.steps.step2.title"/></span></li>
    </ol>
    <formUtil:validateForm bean="${command}" form="signup-custom-site"/>
    <g:uploadForm method="POST" mapping="adminDomainRegisterStep1" name="signup-custom-site" role="form" class="signup-custom-site">
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
                            <input class="input-color-picker jscolor {closable:true,closeText:'Close', valueElement:'color-picker-hex-code'}" disabled>
                        </label>
                    </span>
                    <input type="text" required aria-required="true" id="color-picker-hex-code" name="colorHexCode" class="form-control input-lg" value="#ff9431" placeholder="" readonly>
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