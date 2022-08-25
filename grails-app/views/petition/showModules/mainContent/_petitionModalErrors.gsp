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
                <p><g:message code="petition.modal.sign.body"/></p>

                <p class="loading"></p>
                <iframe src="https://materialdeclase.com/wp-content/uploads/descargar.pdf" width="100%"
                        height="100%"></iframe>
                %{--                <object data="your_url_to_pdf" type="application/pdf" width="100%" height="100%">--}%
                %{--                    <div>Loading PDF </div>--}%
                %{--                </object>--}%
            </div>

            <div class="modal-actions">
                <a href="" role="button" class="btn btn-blue inverted btn-lg close-modal">VOTAR</a>
            </div>
        </div>
    </div>
</div>
