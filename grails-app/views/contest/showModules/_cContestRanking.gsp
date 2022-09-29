<g:if test="${[org.kuorum.rest.model.communication.contest.ContestStatusDTO.VOTING, org.kuorum.rest.model.communication.contest.ContestStatusDTO.RESULTS].contains(contest.status)}">
    <div class="comment-box call-to-action">
        <div class="comment-header">
            <span class="call-title">
                <g:message code="contest.columnC.ranking.title"/>
            </span>
            <span class="call-subTitle">
                <g:message code="contest.columnC.ranking.subtitle"/>
            </span>
        </div>

        <div class="actions clearfix">
            <g:link class="btn btn-lg btn-orange" mapping="contestRanking"
                    params="${contest.encodeAsLinkProperties()}"><g:message
                    code="contest.columnC.ranking.button"/></g:link>
        </div>
    </div>
</g:if>