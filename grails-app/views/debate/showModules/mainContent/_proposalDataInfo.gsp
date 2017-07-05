<div id="proposal_${proposal.id}" class="conversation-box" data-debateId="${proposal.debateId}" data-debateAlias="${proposal.debateAlias}">
    <div class="header clearfix">
        <userUtil:showUserByAlias alias="${proposal.userAlias}" extraCss="pull-left"/>
        <span class="time-ago middle-point left"> <kuorumDate:humanDate date="${proposal.datePublished}"/> </span>
        <ul class="icons pull-right">
            <userUtil:ifUserIsTheLoggedOne user="${debate.userAlias}">
                <li>
                    <button
                            class="pin-proposal ${proposal.pinned?'active':''}"
                            rel="tooltip"
                            type="button"
                            data-urlAction="${g.createLink(mapping: 'debateProposalPin')}"
                            data-debateId="${debate.id}"
                            data-debateAlias="${debate.userAlias}"
                            data-proposalId="${proposal.id}"
                            data-userLogged="${userUtil.loggedUserAlias()}"
                            data-toggle="tooltip"
                            data-placement="bottom"
                            title=""
                            data-original-title="${g.message(code:'debate.show.proposal.pin.tooltip')}">
                        <span class="fa-stack fa-lg" aria-hidden="true">
                            <span class="fa fa-circle dark fa-stack-2x"></span>
                            <span class="fa fa-flag-o fa-stack-1x fa-inverse"></span>
                        </span>
                    </button>
                </li>
            </userUtil:ifUserIsTheLoggedOne>
            <userUtil:elseIfUserNotIsTheLoggedOne user="${debate.userAlias}">
                <g:if test="${proposal.pinned}">
                    <li >
                        <span class="fa-stack fa-lg pin-proposal active"
                              aria-hidden="true"
                              rel="tooltip"
                              data-toggle="tooltip"
                              data-placement="bottom"
                              title=""
                              data-original-title="${g.message(code:'debate.show.proposal.pinned.tooltip', args: [debateUser.name])}">
                            <span class="fa fa-circle dark fa-stack-2x"></span>
                            <span class="fa fa-flag-o fa-stack-1x fa-inverse"></span>
                        </span>
                    </li>
                </g:if>
            </userUtil:elseIfUserNotIsTheLoggedOne>
        </ul>
    </div>
    <div class="body">
        ${raw(proposal.body)}
    </div>
    <div class="actions">
        <button type="button" class="btn-see-more stack" data-anchor="conversation-box">
            <span class="pull-left fa-stack fa-lg" aria-hidden="true">
                <span class="fa fa-circle dark fa-stack-2x"></span>
                <span class="angle fa fa-stack-1x fa-inverse fa-angle-down"></span>
            </span>
        </button>
    </div>

    <div class="footer">
        <div class="clearfix">
            <g:render template="/debate/showModules/mainContent/proposalDataInfoSocial" model="[debate:debate, proposal:proposal]"/>

            <div class="comment-counter pull-right">
                <userUtil:ifUserIsTheLoggedOne user="${proposal.userAlias}">
                    <button type="button" class="delete" data-ajaxDelete='${g.createLink(mapping: 'debateProposalDelete')}'>
                        <span class="middle-point right"><g:message code="post.show.comments.delete"/></span>
                    </button>
                </userUtil:ifUserIsTheLoggedOne>
                <button type="button" class="comment">
                    <span class="fa fa-comment-o" aria-hidden="true"></span>
                    <span class="number">${proposal.comments?.size()?:0}</span>
                </button>
                <sec:ifLoggedIn><g:set var="isLogged" value="${true}"/></sec:ifLoggedIn>
                <sec:ifNotLoggedIn><g:set var="isLogged" value="${false}"/></sec:ifNotLoggedIn>
                <g:set var="activeButton" value="${proposal.liked && isLogged}"/>
                <button type="button"
                        class="proposal-like ${activeButton?'active':''}"
                        data-urlAction="${g.createLink(mapping: 'debateProposalLike')}"
                        data-debateId="${debate.id}"
                        data-debateAlias="${debate.userAlias}"
                        data-proposalId="${proposal.id}"
                        data-userLogged="${userUtil.loggedUserAlias()}"
                    >
                    <g:if test="${activeButton}">
                        <span class="fa fa-heart" aria-hidden="true"></span>
                    </g:if>
                    <g:else>
                        <span class="fa fa-heart-o" aria-hidden="true"></span>
                    </g:else>
                    <span class="number">${proposal.likes}</span>
                </button>
            </div>
        </div>
    </div>
</div>