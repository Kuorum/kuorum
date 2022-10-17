<div id="rankingListCampaigns">
    <g:render template="ranking/rankingFilter"/>
    <!-- LISTADO DE CAMPAÑAS -->
    <div class="box-ppal list-campaigns">
        <div class="pag-list-campaigns">
            <div class="pagination ranking-pagination">
                <span class="reloading"><g:message code="contest.ranking.list.reloading"/><span class="reloading-counter">5</span><g:message code="contest.ranking.list.reloading.seconds"/></span>
                <ul class="paginationTop"></ul>
                <span class="counterList"><g:message code="tools.massMailing.list.pagination.label"/> <span
                        class="totalList"></span></span>
            </div>
        </div>

        <div id="campaignsOrderOptions" class="box-order-options clearfix">
            %{--                    <span><g:message code="tools.massMailing.list.order.by"/> :</span>--}%
            <ul class="sort-options">
                <li class="ranking-pos-header "><a href="#" role="button" class="sort active asc"
                                                   data-sort="ranking-pos">#</a></li>
                <li class="ranking-title-author-header">
                    <a href="#" role="button" class="ranking-title-header sort"
                       data-sort="ranking-campaign-title"><g:message
                            code="contest.ranking.list.header.order.title"/></a>
                    <a href="#" role="button" class="ranking-author-header sort"
                       data-sort="ranking-campaign-author"><g:message
                            code="contest.ranking.list.header.order.author"/></a>
                </li>
            <li class="ranking-extra-info">
                <ul>
                    <li class="ranking-cause-header"><a href="#" role="button" class="sort"
                                                        data-sort="ranking-cause"><g:message
                                    code="contest.ranking.list.header.order.cause"/></a></li>
                    <li class="ranking-contest-focusType-header"><a href="#" role="button"
                                                                    class="sort"
                                                                    data-sort="ranking-contest-focusType"><g:message
                                    code="contest.ranking.list.header.order.focusType"/></a></li>
                    <li class="ranking-contest-activityType-header"><a href="#" role="button"
                                                                       class="sort"
                                                                       data-sort="ranking-contest-activityType"><g:message
                                    code="contest.ranking.list.header.order.activityType"/></a></li>
                    <li class="ranking-numVotes-header"><a href="#" role="button" class="sort"
                                                           data-sort="ranking-numVotes"><g:message
                                    code="contest.ranking.list.header.order.votes"/></a></li>
                    </ul>
                </li>
            </ul>
        </div>
        <ul id="rankingList" class="list"
            data-ajaxLoadContestApplications="${g.createLink(mapping: 'contestRankingApplications', params: contest.encodeAsLinkProperties())}">
        </ul>

        <div class="pag-list-campaigns clearfix">
            <div class="pagination">
                <ul class="paginationBottom"></ul>
                <span class="counterList"><g:message code="tools.massMailing.list.pagination.label"/> <span
                        class="totalList"></span></span>
            </div>
        </div>
    </div>
</div>