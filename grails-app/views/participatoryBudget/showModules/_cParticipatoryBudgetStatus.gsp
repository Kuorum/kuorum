<div class="comment-box call-to-action participatory-budget-status">
    <div class="comment-header">
        <span class="call-title">
            <g:message code="participatoryBudget.status.title"/>
        </span>
        <span class="call-subTitle">
            <g:message code="participatoryBudget.status.subtitle"/>
        </span>
    </div>
    <div class="comment-proposal ${participatoryBudget.status}">
        <div class="participatory-budget-steps-wrapper">
            <div class="campaign-progress-bar">
                <div class="pop-up">
                    ${g.message(code:"org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.${participatoryBudget.status}")}
                    <div class="arrow"></div>
                </div>
                <div class="progress-bar-custom">
                    <div class="progress-bar-custom-done"></div>
                </div>
            </div>
        </div>
        <ul class="participatory-budget-steps-info">
            <g:each in="${org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.values()}" var="status" status="i">
                <li class=" kuorum-tooltip ${status == participatoryBudget.status?'active':''}"
                    rel="tooltip"
                    data-toggle="tooltip"
                    data-placement="bottom"
                    title=""
                    data-original-title="${g.message(code:"org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.${status}.description")}">
                    <label><g:message code="org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.${status}"/></label>
                    <g:set var="statusDate">--</g:set>
                    <g:set var="statusDateMsgCode" value="participatoryBudget.status.until"/>
                    <g:if test="${status == org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.ADDING_PROPOSALS}"><g:set var="statusDate"><g:formatDate type="date" style="small" date="${participatoryBudget.deadLineProposals}"/></g:set></g:if>
                    <g:if test="${status == org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.TECHNICAL_REVIEW}"><g:set var="statusDate"><g:formatDate type="date" style="small" date="${participatoryBudget.deadLineTechnicalReview}"/></g:set></g:if>
                    <g:if test="${status == org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.BALLOT}"><g:set var="statusDate"><g:formatDate type="date" style="small" date="${participatoryBudget.deadLineVotes}"/></g:set></g:if>
                    <g:if test="${status == org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.CLOSED}"><g:set var="statusDate"><g:formatDate type="date" style="small" date="${participatoryBudget.deadLineResults}"/></g:set></g:if>
                    <g:if test="${status == org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.RESULTS}"><g:set var="statusDate"><g:formatDate type="date" style="small" date="${participatoryBudget.deadLineResults}"/></g:set><g:set var="statusDateMsgCode" value="participatoryBudget.status.from"/></g:if>
                    <span>(<g:message code="${statusDateMsgCode}" args="[statusDate]"/>)</span>
                    <userUtil:ifUserIsTheLoggedOne user="${campaignUser}">
                        <a data-status="${status}" data-status-text="${g.message(code:"org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.${status}")}"><span class="fal hand-point-left rigth"></span></a>
                    </userUtil:ifUserIsTheLoggedOne>
                </li>
            </g:each>
        </ul>
    </div>

    <userUtil:ifUserIsTheLoggedOne user="${campaignUser}">
        <!-- MODAL CHANGE CAMPAIGN STATUS-->
        <div class="modal fade in" id="modalEditParticipatoryBudgetStatus" tabindex="-1" role="dialog" aria-labelledby="modalEditParticipatoryBudgetStatusTitle" aria-hidden="true">
            <div class="modal-dialog ">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                            <span aria-hidden="true" class="fal fa-times-circle fa"></span><span class="sr-only"><g:message code="modalDefend.close"/></span>
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