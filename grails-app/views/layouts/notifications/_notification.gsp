
<li class='${newNotification?'new':''}'>
    <g:if test="${user}">
        <span itemscope itemtype="http://schema.org/Person">
            <g:link mapping="userShow" params="${user.encodeAsLinkProperties()}" itemprop="url">
                <img src="${image.userImgSrc(user:user)}" alt="${user.name}" class="user-img" itemprop="image">
                <span itemprop="name">${user.name}</span>
            </g:link>
        </span>
    </g:if>
    <span class="text-notification">
        ${text}
    </span>
    <g:if test="${answerLink}">
        <span class="actions clearfix">
            <span class="pull-right">
                <a href="${answerLink}" class="btn btn-sm btn-custom-primary">Responder</a>
            </span>
        </span>
    </g:if>
</li>