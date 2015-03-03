<%@ page import="kuorum.core.FileType" %>
<r:require modules="social"/>
<g:set var="textClass" value="${showText?'':'sr-only'}"/>
<g:set var="socialPostLink">${post.shortUrl?:createLink(mapping: 'postShow', params: post.encodeAsLinkProperties(), absolute:true)}</g:set>
<li>
    <g:set var="twitterShareText"><g:message
            code="post.social.twitter.shareMessage"
            args="[
                    post.title.encodeAsURL(),
                    post.project.hashtag.encodeAsURL()
            ]"
            encodeAs="HTML"/></g:set>
    <g:set var="twitterLink">https://twitter.com/share?url=${socialPostLink}&text=${twitterShareText}&hashtags=${post.project.hashtag}</g:set>

    <a href="${twitterLink}" class="social-share twitter">
        <span class="${textClass}"><g:message code="project.social.twitter"/></span>
        <g:if test="${showIcon}">
            <span class="fa fa-twitter fa-2x"></span>
        </g:if>
    </a>
</li>
<li>
    %{--<script>--}%
        %{--$(function(){--}%
            %{--facebookData["post_${post.id}"]={--}%
                %{--name: '${post.title.trim().replaceAll('\'', '\\\'')}',--}%
                %{--caption: '${post.project.hashtag}',--}%
                %{--description: '${post.text.trim().replaceAll('\'', '\\\'').replaceAll('\n', ' ')}',--}%
                %{--link: '${socialPostLink?:createLink(mapping: 'postShow', params:post.encodeAsLinkProperties(), absolute:true)}',--}%
                %{--picture: '${post.multimedia?.url}'--}%
            %{--}--}%
        %{--});--}%
    %{--</script>--}%
    <g:set var="facebookLink">https://www.facebook.com/sharer/sharer.php?u=${socialPostLink}</g:set>
    <a href="${facebookLink}" class="social-share facebook" title="Share on Facebook">
        <span class="${textClass}"><g:message code="project.social.facebook"/></span>
        <g:if test="${showIcon}">
            <span class="fa fa-facebook fa-2x"></span>
        </g:if>
    </a>
</li>
<li>
    <g:set var="shortUrl" value="${socialPostLink}"/>
    <a href="https://plus.google.com/share?url=${shortUrl}" class="social-share google">
        <span class="${textClass}"><g:message code="project.social.googlePlus"/></span>
        <g:if test="${showIcon}">
            <span class="fa fa-google-plus fa-2x"></span>
        </g:if>
    </a>
</li>