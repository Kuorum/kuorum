<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title> CHOOSE DOMAIN </title>
    <meta name="layout" content="columnCLayout">
    <r:require modules="forms"/>
</head>

<content tag="mainContent">
    <ol class="stepsSign stepsSignSecondaryColor">
        <li class="active">1<span class="signStepDescription">Design your landing page</span></li>
        <li class="active">2<span class="signStepDescription">Set the user rights</span></li>
    </ol>
    <form role="form" class="signup-custom-site"  name="signup-custom-site" method="post" action="#">
        <p>Choose what kind of content will <b>your users</b> produce: </p>
        <fieldset>

            <div class="multiple-range-selector">
                <label>Post</label>
                <div  class="input-range-selector">
                    <input type="text"
                           data-provide="slider"
                           data-slider-ticks="[0, 1, 2, 3]"
                           data-slider-ticks-labels='["none", "short", "medium", "long"]'
                           data-slider-min="1"
                           data-slider-max="3"
                           data-slider-step="1"
                           data-slider-value="3"
                           data-slider-tooltip="hide" />
                </div>
            </div>
            <div class="multiple-range-selector">
                <label>Survey</label>
                <div  class="input-range-selector">
                    <input type="text"
                           data-provide="slider"
                           data-slider-ticks="[0, 1, 2, 3]"
                           data-slider-ticks-labels='["none", "short", "medium", "long"]'
                           data-slider-min="1"
                           data-slider-max="3"
                           data-slider-step="1"
                           data-slider-value="2"
                           data-slider-tooltip="hide" />
                </div>
            </div>
            <div class="multiple-range-selector">
                <label>Petition</label>
                <div  class="input-range-selector">
                    <input type="text"
                           data-provide="slider"
                           data-slider-ticks="[0, 1, 2, 3]"
                           data-slider-ticks-labels='["none","short", "medium", "long"]'
                           data-slider-min="1"
                           data-slider-max="3"
                           data-slider-step="1"
                           data-slider-value="2"
                           data-slider-tooltip="hide" />
                </div>
            </div>
            <div class="multiple-range-selector">
                <label>Participatory budget</label>
                <div class="input-range-selector">
                    <input type="text"
                           data-provide="slider"
                           data-slider-ticks="[0, 1, 2, 3]"
                           data-slider-ticks-labels='["none", "short", "medium", "long"]'
                           data-slider-min="1"
                           data-slider-max="3"
                           data-slider-step="1"
                           data-slider-value="2"
                           data-slider-tooltip="hide" />
                </div>
            </div>
        </fieldset>
        <fieldset>
            <div class="form-group center">
                <input type="submit" value="Complete set up process" class="btn btn-lg btn-blue">
            </div>
        </fieldset>
    </form>

</content>



<content tag="cColumn">
    <div class="custom-url-info-box">
        <h3>What can do what?</h3>
        <p>
            As admin, you will be able to produce any kind of content. But you choose what your users can
            and cannot do. User generated content will sustain the growth of your community but it will
            also make it more difficult to manage.
        </p>
    </div>
</content>