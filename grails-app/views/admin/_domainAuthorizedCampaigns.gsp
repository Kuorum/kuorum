<r:require modules="forms"/>
<div class="box-ppal-section">
    <h4 class="box-ppal-section-title"><g:message code="kuorum.web.admin.domain.authorizedCampaigns.label"/></h4>
    <fieldset class="row">
        <g:each in="${campaignRoles}" var="campaignRole">
            <div class="multiple-range-selector">
                <label><g:message code="org.kuorum.rest.model.kuorumUser.UserRoleRSDTO.${campaignRole}"/></label>
                <div class="input-range-selector">
                    <input type="text"
                            name="${campaignRole}"
                           data-provide="slider"
                           data-slider-ticks="[0, 1, 2, 3]"
                           data-slider-ticks-labels='["${message(code:"org.kuorum.rest.model.kuorumUser.UserRoleRSDTO.none")}","${userRoles.collect{message(code:"org.kuorum.rest.model.kuorumUser.UserRoleRSDTO."+it)}.join("\",\"")}"]'
                           data-slider-min="1"
                           data-slider-max="3"
                           data-slider-step="1"
                           data-slider-value="${globalAuthoritiesCommand.get(campaignRole)}"
                           data-slider-tooltip="hide" />
                </div>
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