<div class="box-ppal-section">
    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:url command="${command}" field="officialWebSite" showLabel="true"/>
        </div>
        %{--<g:if test="${showPoliticianFields}">--}%
            %{--<div class="form-group col-md-6">--}%
                %{--<formUtil:url command="${command}" field="institutionalWebSite" showLabel="true"/>--}%
            %{--</div>--}%
        %{--</g:if>--}%
    </fieldset>
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
            <formUtil:socialInput command="${command}" field="blog" cssIcon="fa-rss-square"/>
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
    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:socialInput command="${command}" field="youtube" cssIcon="fa-youtube-square"/>
        </div>
        <div class="form-group col-md-6">
            %{--<formUtil:socialInput command="${command}" field="pinterest" cssIcon="fa-pinterest-square"/>--}%
        </div>
    </fieldset>
</div>
<div class="box-ppal-section">
    <fieldset class="form-group text-center">
        <input type="submit" value="${g.message(code:'profile.emailNotifications.save')}" class="btn btn-orange btn-lg">
    </fieldset>
</div>