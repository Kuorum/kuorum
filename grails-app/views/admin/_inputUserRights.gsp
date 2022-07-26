<%@ page import="grails.plugin.springsecurity.SpringSecurityUtils" %>
<fieldset aria-live="polite" class="row">
    <g:each in="${campaignRoles}" var="campaignRole">
        <div class="multiple-range-selector">
            <label><g:message code="org.kuorum.rest.model.kuorumUser.UserRoleRSDTO.${campaignRole}"/></label>

            <div class="input-range-selector">
                <input type="text"
                       name="${campaignRole}"
                       data-provide="slider"
                       data-slider-ticks="[0, 1, 2, 3]"
                       data-slider-ticks-labels='["${message(code: "org.kuorum.rest.model.kuorumUser.UserRoleRSDTO.none")}","${userRoles.collect { message(code: "org.kuorum.rest.model.kuorumUser.UserRoleRSDTO." + it) }.join("\",\"")}"]'
                       data-slider-min="0"
                       data-slider-max="3"
                       data-slider-step="1"
                       data-slider-value="${globalAuthoritiesCommand.get(campaignRole)}"
                       data-slider-enabled="${grails.plugin.springsecurity.SpringSecurityUtils.ifAnyGranted('ROLE_SUPER_ADMIN')}"
                       data-slider-tooltip="hide"/>
            </div>
        </div>
    </g:each>
</fieldset>