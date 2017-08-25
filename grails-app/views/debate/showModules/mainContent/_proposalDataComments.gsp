<div class="conversation-box-comments">

    <g:if test="${(proposal.comments?.size()?:0) < 3}">
        <g:set var="display" value="hidden"/>
    </g:if>
    <div class="show-more-comments ${display}">
        <button class="go-up stack" data-anchor="conversation-box">
            <span class="message-builder"
                    data-status-on="${g.message(code: 'debate.show.proposal.collapsedComments.message.status.show')}"
                    data-status-off="${g.message(code: 'debate.show.proposal.collapsedComments.message.status.hide')}"
                    data-syntax-singular="${g.message(code: 'debate.show.proposal.collapsedComments.message.syntax.singular')}"
                    data-syntax-singularHide="${g.message(code: 'debate.show.proposal.collapsedComments.message.syntax.singularHide')}"
                    data-syntax-plural="${g.message(code: 'debate.show.proposal.collapsedComments.message.syntax.plural')}"
                    data-syntax-pluralHide="${g.message(code: 'debate.show.proposal.collapsedComments.message.syntax.pluralHide')}">
                <span class="status"></span>
                <span class="comments"></span>
                <span class="syntax"></span>
            </span>
        </button>
    </div>
    %{--<div class="show-more-comments ${display}">--}%
        %{--<button type="button" class="go-up stack" data-anchor="conversation-box">--}%
            %{--<span class="pull-left fa-stack fa-lg" aria-hidden="true">--}%
                %{--<span class="fa fa-circle dark fa-stack-2x"></span>--}%
                %{--<span class="angle fa fa-angle-down fa-stack-1x fa-inverse"></span>--}%
            %{--</span>--}%
        %{--</button>--}%
    %{--</div>--}%

    <ul class="conversation-box-comments-list collapsed">
        <g:each in="${proposal.comments}" var="comment">
            <g:render template="/debate/showModules/mainContent/proposalDataComment" model="[debate:debate, proposal:proposal, comment:comment]"/>
        </g:each>
    </ul>
    <!-- ^comment-box !-->
    <div class="comment-box">
        <div class="comment-proposal clearfix">
            <div class="user-box col-md-1 col-xs-12">
                <img class="img-circle" alt="${userUtil.loggedUserName()}" src="${image.loggedUserImgSrc()}">
            </div>
            <div class="comment editable-comment col-md-11 col-xs-12" data-placeholder="&#xf0e5;  Write your comment here..." style="min-height: 100px; padding-top: 20px"></div>
        </div>

        <div class="actions clearfix">
            %{--<div class="pull-left">--}%
                %{--<button type="button" class="go-up stack" data-anchor="conversation-box">--}%
                    %{--<span class="pull-left fa-stack fa-lg" aria-hidden="true">--}%
                        %{--<span class="fa fa-circle dark fa-stack-2x"></span>--}%
                        %{--<span class="angle fa fa-angle-down fa-stack-1x fa-inverse"></span>--}%
                    %{--</span>--}%
                %{--</button>--}%
            %{--</div>--}%
            <button
                type="button"
                class="pull-right btn btn-grey publish save-comment"
                data-postUrl="${g.createLink(mapping: 'debateProposalComment')}"
                data-debateId="${debate.id}"
                data-proposalId="${proposal.id}"
                data-debateAlias="${debate.userAlias}"
                data-userLogged="${userUtil.loggedUserAlias()}">
                <g:message code="debate.show.proposal.comments.save"/>
            </button>
        </div>
    </div><!-- ^comment-box !-->
</div>