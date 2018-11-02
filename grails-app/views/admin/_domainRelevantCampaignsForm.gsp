<r:require modules="forms"/>
<div class="box-ppal-section">
    <h4 class="box-ppal-section-title"><g:message code="kuorum.web.admin.domain.editRelevantCampaigns"/></h4>
    <fieldset class="row">
        <g:each in="${["first","second","third"]}" var="relevance">
            <div class="form-group">
                <label for="campaignIds" class=""><g:message code="kuorum.web.admin.domain.editRelevantCampaigns.label.${relevance}"/></label>
                <select name="campaignIds" class="form-control valid">
                    <option value="">-- DEFAULT --</option>
                    <g:each in="${adminCampaigns}" var="adminCampaign">
                        <option value="${adminCampaign.id}" ${domainCampaignsId[relevance]==adminCampaign.id?'selected':''}>${adminCampaign.name}</option>
                    </g:each>
                </select>
            </div>
        </g:each>
    </fieldset>
</div>
<div class="box-ppal-section">
    <fieldset>
        <div class="form-group text-center">
            <button type="submit" class="btn btn-default btn-lg"><g:message code="default.save"/></button>
        </div>
    </fieldset>
</div>