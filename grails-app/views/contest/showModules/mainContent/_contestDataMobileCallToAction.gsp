<g:if test="${contest.published && [org.kuorum.rest.model.communication.contest.ContestStatusDTO.VOTING,
                                    org.kuorum.rest.model.communication.contest.ContestStatusDTO.ADDING_APPLICATIONS,
                                    org.kuorum.rest.model.communication.contest.ContestStatusDTO.RESULTS].contains(contest.status)}">
    <g:set var="callButtonMsg"
           value="${g.message(code: 'contest.callToAction.' + contest.status + '.button')}"/>


    <div class="actions call-to-action-mobile go-to-action">
        <button type="button" class="btn btn-blue btn-lg call-message" name="${callButtonMsg}">
            ${callButtonMsg}
        </button>
        <span class="fas fa-caret-down arrow"></span>
        <button type="button" class="btn btn-blue btn-xl btn-circle call-button" name="${callButtonMsg}">
            <span class="fal fa-rocket fa-2x"></span>
        </button>
    </div>
</g:if>