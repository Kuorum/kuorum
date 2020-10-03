<table class="autologin-contact-data">
    <tr><td><g:message code="kuorum.web.commands.payment.contact.ContactCommand.name.label"/>:</td><td>${contact.name}</td></tr>
    <g:if test="${contact.surname}">
        <tr><td><g:message code="kuorum.web.commands.payment.contact.ContactCommand.surname.label"/>:</td><td>${contact.surname}</td></tr>
    </g:if>
    <g:if test="${contact.phone}">
        <tr><td><g:message code="kuorum.web.commands.profile.DomainUserPhoneValidationCommand.phoneNumber.placeholder"/>:</td><td>${contact.phone.encodeAsHiddenPhone()}</td></tr>
    </g:if>
    <tr><td>email:</td><td>${contact.email}</td></tr>
    <g:if test="${contact.surveyVoteWeight != 1}">
        <tr><td><g:message code="kuorum.web.commands.payment.contact.ContactCommand.surveyVoteWeight.label"/>:</td><td>${contact.surveyVoteWeight}</td></tr>
    </g:if>
    <g:if test="${campaign}">
        <tr><td><g:message code="tools.campaign.new.${campaign.campaignType}"/>:</td><td>${campaign.title}</td></tr>
    </g:if>
</table>
