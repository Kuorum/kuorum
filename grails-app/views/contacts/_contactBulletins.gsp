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
                <g:link mapping="politicianMassMailingBulletinCopyAndSend" params="[campaignId:bulletin.id, contactId:contactId]" class="btn btn-blue inverted resend-bulletin">
                    <g:message code="tools.massMailing.actions.resend"/>
                    <span class="fal fa-angle-double-right"></span>
                </g:link>
            </td>
        </tr>
        </g:each>
        </tbody>
    </table>
</div>

<!-- Modal registro/validation -->
<div class="modal fade modal-activity-resend" id="activity-resend-bulletin-confirm" tabindex="-1" role="dialog" aria-labelledby="modal-activity-resend" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><g:message code="tools.massMailing.actions.resend.modal.title"/></h4>
            </div>
            <div class="modal-body">
                <p><g:message code="tools.massMailing.actions.resend.modal.text" /></p>
            </div>
            <div class="modal-footer">
                <a href="#" class="cancel" data-dismiss="modal"><g:message code="profile.modal.cropImage.cancel"/> </a>
                <button type="button" class="btn">
                    <g:message code="tools.massMailing.actions.resend.modal.submit"/>
                    <span class="fal fa-angle-double-right"></span>
                </button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>