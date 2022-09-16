<!-- Modal warn before vote -->
<div class="modal confirm-vote-contest-application" id="confirm-vote-contest-application-${contestApplication.id}"
     tabindex="-1" role="dialog" aria-labelledby="confirm-vote-contest-application-${contestApplication.id}-title"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar"><span aria-hidden="true"
                                                                                                   class="fal fa-times-circle fa"></span><span
                        class="sr-only">Cerrar</span></button>
                <h4 id="confirm-vote-contest-application-${contestApplication.id}-title"><g:message
                        code="contestApplication.modal.confirmVote.title"
                        args="[contestApplication.contest.title]"/></h4>
            </div>

            <div class="modal-body">
                <p><g:message code="contestApplication.modal.confirmVote.body.numVotes"
                              args="[contestApplication.contest.title]"/></p>

                <p><g:message code="contestApplication.modal.confirmVote.body.phoneWarn"
                              args="[contestApplication.contest.title]"/></p>
            </div>

            <div class="modal-actions">
                <a href="" role="button" class="btn btn-blue btn-lg confirm-vote-contest-application"><g:message
                        code="contestApplication.modal.confirmVote.buttonVote"/></a>
                <button data-dismiss="modal" role="button" class="btn btn-blue inverted btn-lg close-modal"><g:message
                        code="default.close"/></button>
            </div>
        </div>
    </div>
</div>
