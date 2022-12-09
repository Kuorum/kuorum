<!-- Modal survey show pdf -->
<div class="modal survey-pdf-modal" id="survey-pdf-modal" tabindex="-1" role="dialog"
     aria-labelledby="survey-pdf-modal-title" aria-hidden="true" data-backdrop="static" data-keyboard="false"
     data-survey-signed-votes="${survey.signVotes}"
     data-survey-email-active="${sec.loggedInUserInfo(field: 'email')}"
     data-viewPdfUrl="${g.createLink(mapping: "surveySignedVotesPdfView", params: survey.encodeAsLinkProperties())}">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 id="survey-pdf-modal-title"><g:message code="survey.modal.report.title"/></h4>
            </div>

            <div class="modal-body center">
                <p data-message-noEmail-loading="<g:message code="survey.modal.report.body.noemail.loading"
                                                            args="[survey.name]"/>"
                   data-message-noEmail-loaded="<g:message code="survey.modal.report.body.noemail.loaded"
                                                           args="[survey.name]"/>"
                   data-message-unsigned="<g:message code="survey.modal.report.body.unsigned" args="[survey.name]"/>"
                   data-message-sentByEmail="<g:message code="survey.modal.report.body.sentByEmail"
                                                        args="[survey.name]"/>"
                   data-message-maxAttempts="<g:message code="survey.modal.report.body.maxAttempts"
                                                        args="[survey.name]"/>">
                </p>

                <div class="loading-container">
                    <div class="loading"></div>

                    <p><g:message code="survey.modal.report.body.loading" args="[survey.name]"/></p>
                </div>
            </div>

            <div class="modal-actions">
                <a role="button" data-dismiss="modal" target="_blank"
                   class="btn btn-blue inverted btn-lg modal-download-close hide">
                    <g:message code="survey.modal.close.button"/></a>
                <a href="" role="button" class="btn btn-blue btn-lg modal-download"
                   target="_blank" rel="noopener noreferrer">
                    <g:message code="survey.modal.download.button"/>
                </a>
            </div>
        </div>
    </div>
</div>