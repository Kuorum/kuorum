<li class="conversation-box-comment" id="comment_${comment.id}">
    <div class="header clearfix">
        <userUtil:showUser user="${comment.user}" extraCss="pull-left"/>
        <span class="time-ago middle-point left">
            <kuorumDate:humanDate date="${comment.datePublished}"/>
        </span>
    </div>
    <div class="body">
        ${raw(comment.body)}
    </div>
    <div class="footer clearfix">
        <div class="pull-right">
            <userUtil:ifUserIsTheLoggedOne user="${comment.user}">
                <button type="button" class="delete" data-ajaxDelete='${g.createLink(mapping: 'debateProposalDeleteComment')}' data-commentId="${comment.id}">
                    <span class="middle-point right"><g:message code="post.show.comments.delete"/></span>
                </button>
            </userUtil:ifUserIsTheLoggedOne>

            <div class="footer-comment-votes ${comment.userVote>0?'vote-up':comment.userVote<0?'vote-down':''}">

                <button type="button" class="angle vote-up" data-ajaxVote='${g.createLink(mapping: 'debateProposalVoteComment')}' data-commentId="${comment.id}" data-userAlias="${sec.username()}">
                    <span class="fa fa-angle-up" aria-hidden="true"></span>
                </button>
                <button type="button" class="angle vote-down" data-ajaxVote='${g.createLink(mapping: 'debateProposalVoteComment')}' data-commentId="${comment.id}" data-userAlias="${sec.username()}">
                    <span class="fa fa-angle-down" aria-hidden="true"></span>
                </button>

                <span class="number">${comment.votes}</span>
            </div>
        </div>
        <g:render template="/debate/showModules/mainContent/commentDataInfoSocial" model="[debate:debate, comment:comment]"/>
    </div>
</li>