<!-- Modal survey show pdf -->
<div class="modal survey-pdf-modal" id="survey-pdf-modal" tabindex="-1" role="dialog"
     aria-labelledby="survey-pdf-modal-title" aria-hidden="true" data-backdrop="static" data-keyboard="false"
     data-survey-signed-votes="${survey.signVotes}"
     data-viewPdfUrl="${g.createLink(mapping: "surveySignedVotesPdfView", params: survey.encodeAsLinkProperties())}">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 id="survey-pdf-modal-title"><g:message code="survey.modal.report.title"/></h4>
            </div>

            <div class="modal-body">
                <p id="modal-pdf-message" data-message-unloaded="<g:message code="survey.modal.report.body" args="[survey.name]"/>"
                   data-message-loaded="<g:message code="survey.modal.report.body.loaded" args="[survey.name]"/>"
                   data-message-unsigned="<g:message code="survey.modal.report.body.unsigned" args="[survey.name]"/>">
                </p>

                <p class="loading"></p>
                <iframe src="#" width="100%"
                        height="100%"></iframe>
            </div>

            <div class="modal-actions">
                <a role="button" data-dismiss="modal" target="_blank" class="btn btn-orange inverted btn-lg modal-download-close">
                    <g:message code="survey.modal.close.button"/></a>
                <a href="" role="button" class="btn btn-grey inverted btn-lg modal-download"
                   target="_blank" rel="noopener noreferrer">
                    <g:message code="survey.modal.download.button"/></a>
            </div>
        </div>
    </div>
</div>