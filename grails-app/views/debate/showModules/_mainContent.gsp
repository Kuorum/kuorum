
<g:render template="/debate/showModules/mainContent/debateData" model="[debate: debate, debateUser: debateUser, user: user]" />

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

<ul class="nav nav-pills nav-underline">
    <li class="active">
        <a href="#">Best</a>
    </li>
    <li><a href="#">Newest</a></li>
    <li><a href="#">Oldest</a></li>
    <li><a href="#">Pinned</a></li>
</ul>

<g:each in="${proposalPage.data}" var="proposal">
    <g:render template="/debate/showModules/mainContent/proposalData" model="[proposal:proposal]"/>
</g:each>
<!-- propusal block !-->

<div class="see-more-content"> <!-- ^see-more-content !-->
    <button type="button" class="btn angle-down" data-anchor="conversation-box">See more</button>
</div> <!-- ^see-more-content !-->
