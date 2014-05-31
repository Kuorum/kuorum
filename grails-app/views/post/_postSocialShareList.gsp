<r:require modules="social"/>
<g:set var="textClass" value="${showText?'':'sr-only'}"/>
<li>
    <g:set var="twitterShareText"><g:message
            code="post.social.twitter.shareMessage"
            args="[
                    post.title.encodeAsURL(),
                    post.law.hashtag.encodeAsURL()
            ]"
            encodeAs="HTML"/></g:set>
    <g:set var="twitterLink">https://twitter.com/share?url=${post.shortUrl}&text=${twitterShareText}&via=kuorumorg&hashtags=${post.law.hashtag}</g:set>

    <a href="${twitterLink}" class="social-share twitter">
        <span class="${textClass}"><g:message code="law.social.twitter"/></span>
        <g:if test="${showIcon}">
            <span class="fa-stack fa-lg">
                <span class="fa fa-circle fa-stack-2x"></span>
                <span class="fa fa-twitter fa-stack-1x"></span>
            </span>
        </g:if>
    </a>
</li>
<li>
    <script>

        function sharePost_${post.id}(){
            FB.ui(
                    {
                        method: 'feed',
                        name: '${post.title.trim().encodeAsURL()}',
                        caption: '${post.law.hashtag}',
                        description: '${post.text.trim().encodeAsURL()}',
                        link: '${createLink(mapping: 'postShow', params:post.encodeAsLinkProperties(), absolute:true)}',
                        picture: '${post.multimedia?.url}'
                    },
                    function(response) {
                        if (response && response.post_id) {
                            display.success('Se ha publicado correctamente en tu muro');
                        } else {
                            display.warn('Hubo algun problema, vuelva a intentarlo');
                        }
                    }
            );
        }
        $(function(){
            $("a.social-share.facebook").on('click',function(e){e.preventDefault(); sharePost_${post.id}()})
        })
    </script>
    <a href="#" class="social-share facebook">
        <span class="${textClass}"><g:message code="law.social.facebook"/></span>
        <g:if test="${showIcon}">
            <span class="fa-stack fa-lg">
                <span class="fa fa-circle fa-stack-2x"></span>
                <span class="fa fa-facebook fa-stack-1x"></span>
            </span>
        </g:if>
    </a>
</li>
%{--<li><a href="#"><span class="sr-only">LinkedIn</span><span class="fa-stack fa-lg"><span class="fa fa-circle fa-stack-2x"></span><span class="fa fa-linkedin fa-stack-1x"></span></span></a></li>--}%
<li>
    <g:set var="shortUrl" value="${post.shortUrl?:createLink(mapping:'postShow', params:post.encodeAsLinkProperties(), absolute: true)}"/>
    <a href="https://plus.google.com/share?url=${shortUrl}" class="social-share google">
        <span class="${textClass}"><g:message code="law.social.googlePlus"/></span>
        <g:if test="${showIcon}">
            <span class="fa-stack fa-lg">
                <span class="fa fa-circle fa-stack-2x"></span>
                <span class="fa fa-google-plus fa-stack-1x"></span>
            </span>
        </g:if>
    </a>
</li>