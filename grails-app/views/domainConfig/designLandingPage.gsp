<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title> CHOOSE DOMAIN </title>
    <meta name="layout" content="columnCLayout">
    <r:require modules="forms"/>
</head>

<content tag="mainContent">
    <ol class="stepsSign stepsSignSecondaryColor">
        <li class="active">1<span class="signStepDescription">Design your landing page</span></li>
        <li class="">2<span class="signStepDescription">Set the user rights</span></li>
    </ol>
    <form role="form" class="signup-custom-site"  name="signup-custom-site" method="post" action="#">
        <fieldset class="row">
            <div class="form-group col-md-6 form-group-align-left">
                <label for="slogan">Slogan *</label>
                <input type="text" name="slogan" class="form-control input-lg" id="slogan" placeholder="slogan" aria-required="true" required>
                <span class="help-block">https://kuorum.org/matnso</span>
            </div>
            <div class="form-group col-md-6 form-group-align-left">
                <label for="subtitle">Subtitle *</label>
                <input type="subtitle" name="subtitle" class="form-control input-lg" id="subtitle" placeholder="subtitle" aria-required="true" required>
            </div>
        </fieldset>
        <fieldset class="row">
            <div class="form-group col-md-6 form-group-align-left">
                <label for="logo">Logo *</label>
                <div class="input-group">
                    <label class="input-group-addon">
                        <span class="btn btn-blue inverted">
                            Select file<input id="sign-in-step-5__input-file" type="file" style="display: none;" disabled>
                        </span>
                    </label>
                    <input name="logo" id="logo" type="text" class="form-control input-lg" readonly>
                </div>
            </div>
            <div class="form-group col-md-6 form-group-align-left">
                <label for="color-picker-hex-code">Color *</label>
                <div class="input-append input-group">
                    <span tabindex="100" class="add-on input-group-addon">
                        <label class="">
                            <input id="sign-in-step-5__color-picker" class="sign-in-step-5__color-selected jscolor {closable:true,closeText:'Close', valueElement:'color-picker-hex-code'}">
                        </label>
                    </span>
                    <input type="text" required aria-required="true" id="color-picker-hex-code" name="color-picker-hex-code" class="form-control input-lg" value="#ff9431" placeholder="" disabled>
                </div>
            </div>
        </fieldset>

        <fieldset class="row">
            <div class="form-group">
                <div class="col-md-4 col-sm-4">
                    <label class="input-group-btn">
                        <span class="btn btn-blue btn-lg">
                            <i class="fa fa-plus-square-o fa-4x" aria-hidden="true"></i><input id="sign-in-step-5__preview-image-1-input-file" data-pos="1" type="file" style="display: none;" multiple>
                        </span>
                        <div class="croppie-img">
                            <img src="" id="preview-image-1">
                        </div>
                    </label>
                </div>
                <div class="col-md-4 col-sm-4">
                    <label class="input-group-btn">
                        <span class="btn btn-blue btn-lg">
                            <i class="fa fa-plus-square-o fa-4x" aria-hidden="true"></i><input id="sign-in-step-5__preview-image-2-input-file" data-pos="2" type="file" style="display: none;" multiple>
                        </span>
                        <div class="croppie-img">
                            <img src="" id="preview-image-2">
                        </div>
                    </label>
                </div>
                <div class="col-md-4 col-sm-4">
                    <label class="input-group-btn">
                        <span class="btn btn-blue btn-lg">
                            <i class="fa fa-plus-square-o fa-4x" aria-hidden="true"></i><input id="sign-in-step-5__preview-image-3-input-file" data-pos="3" type="file" style="display: none;" multiple>
                        </span>
                        <div class="croppie-img">
                            <img src="" id="preview-image-3">
                        </div>
                    </label>
                </div>
            </div>
        </fieldset>

        <fieldset>
            <div class="form-group center">
                <input type="submit" value="Next Step" class="btn btn-lg btn-blue">
            </div>
        </fieldset>
    </form>
</content>



<content tag="cColumn">
    <div class="custom-url-info-box">
        <h3>Customise your site</h3>
        <p>
            Write a slogan and subtitle, choose a color that matches your logo and pick 3 pictures for your
            landing page. You can edit this later from your admin settings.
        </p>
        <h3>This is how it will look like</h3>
        <div class="macbook-pro d-flex column">
            <img class="macbook-pro-img" src="${g.resource(dir: "images", file: "macbook-pro.png")}" alt="macbook pro preview">
            <div class="macbook-pro-screen">
                <div class="macbook-pro-screen-header">
                    <img src="${g.resource(dir: "images", file: "logo@2x.png")}" id="macbook-pro-website-logo">
                </div>
                <div class="macbook-pro-screen-body">
                    <h1 id="macbook-pro-website-slogan">SLOGAN</h1>
                    <h3 id="macbook-pro-website-subtitle">Subtitle</h3>
                    <div class="macbook-pro-website-form d-flex">
                        <div id="macbook-pro-website-form-input-1" class="macbook-pro-website-form-input"></div>
                        <div id="macbook-pro-website-form-input-2" class="macbook-pro-website-form-input"></div>
                        <div id="macbook-pro-website-form-input-3" class="macbook-pro-website-form-input macbook-pro-website-form-input--primary-color"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</content>