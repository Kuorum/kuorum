
<g:render template="/debate/showModules/mainContent/debateData" model="[debate: debate, debateUser: debateUser, user: user]" />

<!-- ^comment-box !-->
<div class="comment-box clearfix">
    <div class="user-box col-lg-1 col-xs-12">
        <img class="img-circle" alt="Pepito perz" src="${image.userImgSrc(user: user)}">
    </div>
    <div class="comment editable col-lg-11 col-xs-12" data-placeholder="${message(code: "debate.proposal.placeholder")}" style="min-height: 100px; padding-top: 10px"></div>
    <div class="actions pull-right">
        <button type="button" class="btn btn-blue inverted publish"><g:message code="debate.publish" /></button>
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

<g:render template="/debate/showModules/mainContent/proposalsData"/>
<!-- propusal block !-->

<div class="see-more-content"> <!-- ^see-more-content !-->
    <button type="button" class="btn angle-down" data-anchor="conversation-box">See more</button>
</div> <!-- ^see-more-content !-->
