<div class="comment-box call-to-action participatory-budget-status">
    <div class="comment-header">
        <span class="call-title">
            <g:message code="participatoryBudget.status.title"/>
        </span>
        <span class="call-subTitle">
            <g:message code="participatoryBudget.status.subtitle"/>
        </span>
    </div>
    <div class="comment-proposal">
        <ul class="timeline zipped">
            <g:each in="${org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.values()}" var="status" status="i">
                <li class="${i%2 == 0?'':'timeline-inverted'}">
                    <div class="timeline-badge warning ${status == participatoryBudget.status?'':'small'}">
                        <g:if test="${status == participatoryBudget.status}">
                            <span class="fa fa-check"></span>
                        </g:if>
                    </div>
                    <div class="timeline-panel">
                        <div class="timeline-heading">
                            <h4 class="timeline-title">
                                <g:message code="org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.${status}"/>
                            </h4>
                        </div>
                        <div class="timeline-body">
                            <p>
                                <g:message code="org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.${status}.description"/>
                            </p>
                            <userUtil:ifUserIsTheLoggedOne user="${campaignUser}">
                            <p>
                                <a data-status="${status}" data-status-text="${g.message(code:"org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.${status}")}">
                                    <g:message code="participatoryBudget.status.openModal"/>
                                </a>
                            </p>
                            </userUtil:ifUserIsTheLoggedOne>
                        </div>
                    </div>
                    <div class="date text-brand-dark">
                        <g:set var="statusDate">--</g:set>
                        <g:if test="${status == org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.ADDING_PROPOSALS}"><g:set var="statusDate"><g:formatDate type="date" style="small" date="${participatoryBudget.deadLineProposals}"/></g:set></g:if>
                        <g:if test="${status == org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.TECHNICAL_REVIEW}"><g:set var="statusDate"><g:formatDate type="date" style="small" date="${participatoryBudget.deadLineTechnicalReview}"/></g:set></g:if>
                        <g:if test="${status == org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.BALLOT}"><g:set var="statusDate"><g:formatDate type="date" style="small" date="${participatoryBudget.deadLineVotes}"/></g:set></g:if>
                        <g:if test="${status == org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.CLOSED}"><g:set var="statusDate"></g:set></g:if>
                        ${statusDate}
                    </div>
                </li>
            </g:each>
        </ul>
    </div>

    <userUtil:ifUserIsTheLoggedOne user="${campaignUser}">
        <div class="modal fade in" id="modalEditParticipatoryBudgetStatus" tabindex="-1" role="dialog" aria-labelledby="modalEditParticipatoryBudgetStatusTitle" aria-hidden="true">
            <div class="modal-dialog ">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                            <span aria-hidden="true" class="fa fa-times-circle-o fa"></span><span class="sr-only"><g:message code="modalDefend.close"/></span>
                        </button>
                        <h4>
                            <g:message code="participatoryBudget.status.change.modal.title"/>
                        </h4>
                    </div>
                    <div class="modal-body">
                        <p><g:message code="participatoryBudget.status.change.modal.text"/> </p>
                        <fieldset class="text-right">
                            <a href="#" class="btn btn-grey-light btn-lg" data-dismiss="modal" id="modalEditParticipatoryBudgetStatusButtonClose">
                                <g:message code="participatoryBudget.status.change.modal.cancel"/>
                            </a>
                            <g:link mapping="participatoryBudgetEditStatus" params="${participatoryBudget.encodeAsLinkProperties()+[status:"STATUS"]}" class="btn btn-blue inverted btn-lg" elementId="modalEditParticipatoryBudgetStatusButtonOk">
                                <g:message code="participatoryBudget.status.change.modal.submit"/>
                            </g:link>
                        </fieldset>

                    </div>
                </div>
            </div>
        </div>
    </userUtil:ifUserIsTheLoggedOne>
</div>