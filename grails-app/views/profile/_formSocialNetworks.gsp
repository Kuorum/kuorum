<div class="box-ppal-section">
    <fieldset aria-live="polite" class="row">
        <div class="form-group col-md-6">
            <formUtil:url command="${command}" field="officialWebSite" showLabel="true"/>
        </div>
    </fieldset>
    <fieldset aria-live="polite" class="row">
        <div class="form-group col-md-6">
            <formUtil:socialInput command="${command}" field="facebook" cssIcon="fab fa-facebook"/>
        </div>
        <div class="form-group col-md-6">
            <formUtil:socialInput command="${command}" field="twitter" cssIcon="fab fa-twitter"/>
        </div>
    </fieldset>
    <fieldset aria-live="polite" class="row">
        <div class="form-group col-md-6">
            <formUtil:socialInput command="${command}" field="youtube" cssIcon="fab fa-youtube"/>
        </div>
        <div class="form-group col-md-6">
            <formUtil:socialInput command="${command}" field="blog" cssIcon="fal fa-rss-square"/>
        </div>
    </fieldset>
    <fieldset aria-live="polite" class="row">
        <div class="form-group col-md-6">
            <formUtil:socialInput command="${command}" field="linkedIn" cssIcon="fab fa-linkedin-in"/>
        </div>
        <div class="form-group col-md-6">
            <formUtil:socialInput command="${command}" field="instagram" cssIcon="fab fa-instagram"/>
        </div>
    </fieldset>
    <fieldset aria-live="polite" class="row">
        <div class="form-group col-md-6">
            %{--<formUtil:socialInput command="${command}" field="googlePlus" cssIcon="fab fa-google-plus"/>--}%
        </div>
        <div class="form-group col-md-6">
            %{--<formUtil:socialInput command="${command}" field="pinterest" cssIcon="fa-pinterest-square"/>--}%
        </div>
    </fieldset>
</div>

<div class="box-ppal-section">
    <fieldset aria-live="polite" class="form-group text-center">
        <input type="submit" value="${g.message(code:'default.save')}" class="btn btn-orange btn-lg">
    </fieldset>
</div>