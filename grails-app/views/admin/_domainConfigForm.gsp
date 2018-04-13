<r:require modules="forms"/>
<div class="box-ppal-section">
    <h4 class="box-ppal-section-title">Colors</h4>
    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:input command="${command}" field="name" showLabel="true" />
        </div>
    </fieldset>

    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:input command="${command}" field="slogan" showLabel="true" />
        </div>
        <div class="form-group col-md-6">
            <formUtil:input command="${command}" field="subtitle" showLabel="true" />
        </div>
    </fieldset>
    <h4 class="box-ppal-section-title">Colors</h4>
    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:input command="${command}" field="mainColor" showLabel="true" />
        </div>
        <div class="form-group col-md-6">
            <formUtil:input command="${command}" field="mainColorShadowed" showLabel="true" />
        </div>
        <div class="form-group col-md-6">
            <formUtil:input command="${command}" field="secondaryColor" showLabel="true" />
        </div>
        <div class="form-group col-md-6">
            <formUtil:input command="${command}" field="secondaryColorShadowed" showLabel="true" />
        </div>
    </fieldset>
    <h4 class="box-ppal-section-title">Social</h4>
    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:socialInput command="${command}" field="facebook" cssIcon="fa-facebook"/>
        </div>
        <div class="form-group col-md-6">
            <formUtil:socialInput command="${command}" field="twitter" cssIcon="fa-twitter"/>
        </div>
    </fieldset>
    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:socialInput command="${command}" field="googlePlus" cssIcon="fa-google-plus"/>
        </div>
        <div class="form-group col-md-6">
            <formUtil:socialInput command="${command}" field="youtube" cssIcon="fa-youtube-square"/>
        </div>
    </fieldset>
    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:socialInput command="${command}" field="linkedIn" cssIcon="fa-linkedin-square"/>
        </div>
        <div class="form-group col-md-6">
            <formUtil:socialInput command="${command}" field="instagram" cssIcon="fa-instagram"/>
        </div>
    </fieldset>
</div>
<div class="box-ppal-section">
    <fieldset>
        <div class="form-group text-center">
            <button type="submit" class="btn btn-default btn-lg"><g:message code="admin.createUser.submit"/></button>
        </div>
    </fieldset>
</div>