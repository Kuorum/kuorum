<!-- Modal petition show pdf for signing -->
<div class="modal petition-sign-pdf-modal" id="petition-sign-pdf-modal-${petition.id}" tabindex="-1" role="dialog"
     aria-labelledby="petition-sign-pdf-modal-title" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar"><span aria-hidden="true"
                                                                                                   class="fal fa-times-circle fa"></span><span
                        class="sr-only">Cerrar</span></button>
                <h4 id="petition-sign-pdf-modal-title"><g:message code="petition.modal.sign.title"/></h4>
            </div>

            <div class="modal-body">
                <p id="modal-pdf-message"
                   data-message-noEmail-loading="<g:message code="petition.modal.sign.body" args="[petition.name]"/>"
                   data-message-noEmail-loaded="<g:message code="petition.modal.sign.body.loaded"
                                                           args="[petition.name]"/>">
                </p>

                <p class="loading"></p>
                <iframe src="" width="100%" height="100%"></iframe>
                %{--                <object data="your_url_to_pdf" type="application/pdf" width="100%" height="100%">--}%
                %{--                    <div>Loading PDF </div>--}%
                %{--                </object>--}%
            </div>

            <div class="modal-actions">
                <a href="" role="button" class="btn btn-grey-light btn-lg modal-download"
                   target="_blank" rel="noopener noreferrer">
                    <g:message code="petition.modal.download.button"/></a>
                <a href="" role="button" class="btn btn-blue inverted btn-lg modal-sign" ><g:message
                        code="petition.modal.sign.button"/></a>
            </div>
        </div>
    </div>
</div>

<!-- Modal petition show pdf for signing -->
<div class="modal petition-already-signed-modal" id="petition-already-signed-modal-${petition.id}" tabindex="-1"
     role="dialog" aria-labelledby="petition-already-signed-modal-title" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar"><span aria-hidden="true"
                                                                                                   class="fal fa-times-circle fa"></span><span
                        class="sr-only">Cerrar</span></button>
                <h4 id="petition-already-signed-modal-title"><g:message code="petition.modal.signed.title"/></h4>
            </div>

            <div class="modal-body">
                <p><g:message code="petition.modal.signed.body"/></p>
            </div>

            <div class="modal-actions">
                <a href="" role="button" class="btn btn-blue inverted btn-lg modal-sign"><g:message
                        code="default.close"/></a>
            </div>
        </div>
    </div>
</div>

