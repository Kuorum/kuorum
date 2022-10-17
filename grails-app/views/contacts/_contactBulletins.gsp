<div class="contact-activity">
    <table class="table list-tracking">
        <thead>
        <tr>
            <th><g:message code="tools.bulletins.name"/></th>
            <th><g:message code="tools.bulletins.title"/></th>
            <th></th>
            <th><g:message code="tools.massMailing.timestamp"/></th>
            <th><g:message code="tools.massMailing.actions"/></th>
        </tr>
        </thead>
        <tbody>
        <g:each in="${bulletins}" var="bulletin">
            <tr>
                <td>${bulletin.name}</td>
                <td>${bulletin.title}</td>
                <td></td>
                <td><kuorumDate:humanDate date="${bulletin.datePublished}"/></td>
                <td>
                    <g:link mapping="politicianMassMailingTrackEventsResend" params="[campaignId:bulletin.id,tackingMailId: 1]" class="btn btn-blue inverted resend-email">
                        <g:message code="tools.massMailing.actions.resend"/>
                        <span class="fal fa-angle-double-right"></span>
                    </g:link>
                </td>
            </tr>
        </g:each>
        </tbody>
    </table>
</div>