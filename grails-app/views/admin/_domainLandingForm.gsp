<%@ page import="org.kuorum.rest.model.kuorumUser.UserRoleRSDTO" %>
<r:require modules="forms"/>
<div class="box-ppal-section">
    <h4 class="box-ppal-section-title"><g:message code="kuorum.web.admin.domain.landingSettings.label"/></h4>

    <fieldset aria-live="polite" class="row">
        <div class="form-group col-md-6">
            <formUtil:input command="${command}" field="slogan" showLabel="true" />
        </div>
        <div class="form-group col-md-6">
            <formUtil:input command="${command}" field="subtitle" showLabel="true" />
        </div>
    </fieldset>
    <fieldset aria-live="polite" class="row">
        <div class="form-group col-md-12">
            <formUtil:textArea command="${command}" field="domainDescription" showLabel="true" />
        </div>
    </fieldset>

    <h4 class="box-ppal-section-title"><g:message code="kuorum.web.admin.domain.landingVisibleRoles.label"/></h4>
    <fieldset aria-live="polite" class="row pretty-check-box-row">
        <ul class="pretty-check-boxes">
            <g:each in="${[
                    [role:org.kuorum.rest.model.kuorumUser.UserRoleRSDTO.ROLE_CAMPAIGN_POST, label:'POST', icon:'fa-newspaper'],
                    [role:org.kuorum.rest.model.kuorumUser.UserRoleRSDTO.ROLE_CAMPAIGN_DEBATE, label:'DEBATE', icon:'fa-comments'],
                    [role:org.kuorum.rest.model.kuorumUser.UserRoleRSDTO.ROLE_CAMPAIGN_EVENT, label:'EVENT', icon:'fa-calendar-star'],
                    [role:org.kuorum.rest.model.kuorumUser.UserRoleRSDTO.ROLE_CAMPAIGN_SURVEY, label:'SURVEY', icon:'fa-chart-bar'],
                    [role:org.kuorum.rest.model.kuorumUser.UserRoleRSDTO.ROLE_CAMPAIGN_PETITION, label:'PETITION', icon:'fa-microphone'],
                    [role:org.kuorum.rest.model.kuorumUser.UserRoleRSDTO.ROLE_CAMPAIGN_PARTICIPATORY_BUDGET, label:'PARTICIPATORY_BUDGET', icon:'fa-money-bill-alt']
            ]}" var="roleData">
                <li class="pretty-check-box">
                    <label  for="landingVisibleRoles_${roleData.role}" rel="tooltip" data-toggle="tooltip" data-placement="bottom" title="" data-original-title="${g.message(code:"dashboard.payment.chooseCampaign.tooltip.${roleData.label}")}">
                        <input id="landingVisibleRoles_${roleData.role}" name="landingVisibleRoles" type="checkbox" value="${roleData.role}" ${command.landingVisibleRoles.contains(roleData.role)?'checked=checked':''}>
                        <div class="pretty-check-box-icon">
                            <i class="fal ${roleData.icon} fa-2x"></i>
                            <i class="fa fa-check-circle check"></i>
                            <span class="pretty-check-box-text"><g:message code="tools.campaign.new.${roleData.label}"></g:message></span>
                        </div>
                    </label>
                </li>
            </g:each>
        </ul>

    </fieldset>

    <h4 class="box-ppal-section-title"><g:message code="kuorum.web.admin.domain.footerLinks.label"/></h4>
    <fieldset aria-live="polite">
        <formUtil:dynamicComplexInputs
                command="${command}"
                field="footerLinks"
                listClassName="kuorum.web.commands.LinkCommand"
                cssParentContainer="profile-dynamic-fields"
                customRemoveButton="true"
                appendLast="true"
                formId="adminDomainConfigLandingForm">
            <fieldset aria-live="polite" class="row">
                <div class="form-group col-md-5">
                    <formUtil:input field="title" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="true"/>
                </div>
                <div class="form-group col-md-5">
                    <formUtil:url field="url" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="true"/>
                </div>
                <div class="form-group col-md-1 form-group-remove">
                    <button type="button" class="btn btn-transparent removeButton"><i class="fa fa-trash"></i></button>
                </div>
            </fieldset>
        </formUtil:dynamicComplexInputs>
    </fieldset>
</div>

<div class="box-ppal-section">
    <fieldset aria-live="polite">
        <div class="form-group text-center">
            <button type="submit" class="btn btn-default btn-lg"><g:message code="default.save"/></button>
        </div>
    </fieldset>
</div>