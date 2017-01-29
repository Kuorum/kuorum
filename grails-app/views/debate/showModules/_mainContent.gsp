
<g:render template="/debate/showModules/mainContent/debateData" model="[debate: debate, debateUser: debateUser, proposalPage: proposalPage]" />

<!-- ^comment-box !-->
<div class="comment-box clearfix">
    <div class="user-box col-lg-1 col-xs-12">
        <img class="img-circle" alt="${userUtil.loggedUserName()}" src="${image.loggedUserImgSrc()}">
    </div>
    <div class="comment editable col-lg-11 col-xs-12" data-placeholder="${message(code: "debate.proposal.placeholder")}" style="min-height: 100px; padding-top: 10px"></div>
    <div class="actions pull-right">
        <button
                type="button"
                class="btn btn-blue inverted publish publish-proposal"
                data-userLoggedAlias="${userUtil.loggedUserAlias()}"
                data-postUrl="${g.createLink(mapping: 'debateProposalNew')}"
                data-debateId="${debate.id}"
                data-debateAlias="${debateUser.alias}">
            <g:message code="debate.publish" />
        </button>
    </div>
</div> <!-- ^comment-box !-->

<ul id="proposal-option" class="nav nav-pills nav-underline" style="display: none">
    <li><a href="#latest"><g:message code="debate.proposals.nav.latest"/> </a></li>
    <li><a href="#oldest"><g:message code="debate.proposals.nav.oldest"/></a></li>
    <li><a href="#best"><g:message code="debate.proposals.nav.best"/></a></li>
    <li><a href="#pinned"><g:message code="debate.proposals.nav.pinned"/></a></li>
</ul>

<ul class="proposal-list">
<g:each in="${proposalPage.data}" var="proposal">
    <g:render template="/debate/showModules/mainContent/proposalData" model="[debate:debate, proposal:proposal]"/>
</g:each>
</ul>
<!-- propusal block !-->

%{-- SEE MORE HIDDEN

<div class="see-more-content"> <!-- ^see-more-content !-->
    <button type="button" class="btn angle-down" data-anchor="conversation-box">See more</button>
</div> <!-- ^see-more-content !-->

--}%