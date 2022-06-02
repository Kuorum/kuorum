<div class="comment-box call-to-action campaign-steps-status">
    <div class="comment-header">
        <span class="call-title">
            <g:message code="participatoryBudget.status.title"/>
        </span>
        <span class="call-subTitle">
            <g:message code="participatoryBudget.status.subtitle"/>
        </span>
    </div>

    <div class="comment-proposal ${contest.status}">
        <div class="campaign-steps-wrapper steps4">
            <div class="campaign-progress-bar">
                <div class="pop-up">
                    ${g.message(code: "org.kuorum.rest.model.communication.contest.ContestStatusDTO.${contest.status}")}
                    <div class="arrow"></div>
                </div>

                <div class="progress-bar-custom">
                    <div class="progress-bar-custom-done"></div>
                </div>
            </div>
        </div>
        <ul class="campaign-steps-info">
            <g:each in="${org.kuorum.rest.model.communication.contest.ContestStatusDTO.values()}" var="status"
                    status="i">
                <li class=" kuorum-tooltip ${status == contest.status ? 'active' : ''}"
                    rel="tooltip"
                    data-toggle="tooltip"
                    data-placement="bottom"
                    title=""
                    data-original-title="${g.message(code: "org.kuorum.rest.model.communication.contest.ContestStatusDTO.${status}.description")}">
                    <label><g:message
                            code="org.kuorum.rest.model.communication.contest.ContestStatusDTO.${status}"/></label>
                    <g:set var="statusDate">--</g:set>
                    <g:set var="statusDateMsgCode" value="participatoryBudget.status.until"/>
                    <g:if test="${status == org.kuorum.rest.model.communication.contest.ContestStatusDTO.ADDING_APPLICATIONS}"><g:set
                            var="statusDate"><g:formatDate formatName="default.date.format.small"
                                                           date="${contest.deadLineApplications}"/></g:set></g:if>
                    <g:if test="${status == org.kuorum.rest.model.communication.contest.ContestStatusDTO.VALIDATING_APPLICATIONS}"><g:set
                            var="statusDate"><g:formatDate formatName="default.date.format.small"
                                                           date="${contest.deadLineReview}"/></g:set></g:if>
                    <g:if test="${status == org.kuorum.rest.model.communication.contest.ContestStatusDTO.VOTING}"><g:set
                            var="statusDate"><g:formatDate formatName="default.date.format.small"
                                                           date="${contest.deadLineVotes}"/></g:set></g:if>
                    <g:if test="${status == org.kuorum.rest.model.communication.contest.ContestStatusDTO.RESULTS}"><g:set
                            var="statusDate"><g:formatDate formatName="default.date.format.small"
                                                           date="${contest.deadLineResults}"/></g:set></g:if>
                    <g:if test="${status != org.kuorum.rest.model.communication.contest.ContestStatusDTO.RESULTS}">
                        <span>(<g:message code="${statusDateMsgCode}" args="[statusDate]"/>)</span>
                    </g:if>
                    <userUtil:ifUserIsTheLoggedOne user="${campaignUser}">
                        <a data-status="${status}"
                           data-status-text="${g.message(code: "org.kuorum.rest.model.communication.contest.ContestStatusDTO.${status}")}"><span
                                class="fal fa-hand-point-left rigth"></span></a>
                    </userUtil:ifUserIsTheLoggedOne>
                </li>
            </g:each>
        </ul>
    </div>

    <userUtil:ifUserIsTheLoggedOne user="${campaignUser}">
        <!-- MODAL CHANGE CAMPAIGN STATUS-->
        <div class="modal fade in" id="modalEditParticipatoryBudgetStatus" tabindex="-1" role="dialog"
             aria-labelledby="modalEditParticipatoryBudgetStatusTitle" aria-hidden="true">
            <div class="modal-dialog ">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                            <span aria-hidden="true" class="fal fa-times-circle fa"></span><span
                                class="sr-only"><g:message code="modalDefend.close"/></span>
                        </button>
                        <h4>
                            <g:message code="participatoryBudget.status.change.modal.title"/>
                        </h4>
                    </div>

                    <div class="modal-body">
                        <p><g:message code="participatoryBudget.status.change.modal.text"/></p>
                        <fieldset aria-live="polite" class="text-right">
                            <a href="#" class="btn btn-grey-light btn-lg" data-dismiss="modal"
                               id="modalEditParticipatoryBudgetStatusButtonClose">
                                <g:message code="participatoryBudget.status.change.modal.cancel"/>
                            </a>
                            <g:link mapping="participatoryBudgetEditStatus"
                                    params="${contest.encodeAsLinkProperties() + [status: "STATUS"]}"
                                    class="btn btn-blue inverted btn-lg"
                                    elementId="modalEditParticipatoryBudgetStatusButtonOk">
                                <g:message code="participatoryBudget.status.change.modal.submit"/>
                            </g:link>
                        </fieldset>

                    </div>
                </div>
            </div>
        </div>
    </userUtil:ifUserIsTheLoggedOne>
</div>