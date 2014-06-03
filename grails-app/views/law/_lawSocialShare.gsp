<r:require modules="social"/>

<g:set var="twitterShareText"><g:message
        code="law.social.twitter.shareMessage"
        args="[
                law.hashtag.encodeAsURL()
        ]"/></g:set>
<g:set var="twitterLink">https://twitter.com/share?url=${law.shortUrl}&text=${twitterShareText}&via=kuorumorg&hashtags=${law.hashtag}</g:set>


<div class="social">
    <p><g:message code="law.social.title"/></p>
    <ul class="social">
        <li>
            <a href="${twitterLink}" class="social-share twitter">
                <span class="sr-only"><g:message code="law.social.twitter"/> </span><span class="fa-stack fa-lg"><span class="fa fa-circle fa-stack-2x"></span><span class="fa fa-twitter fa-stack-1x"></span></span>
            </a>
        </li>
        <li>
            <script>

                function shareLaw_${law.id}(){
                    FB.ui(
                            {
                                method: 'feed',
                                name: '${law.shortName.replaceAll('\'', '\\\'')}',
                                caption: '${law.realName.replaceAll('\'', '\\\'')}',
                                description: '${law.introduction.trim().replaceAll('\'', '\\\'')}',
                                link: '${createLink(mapping: 'lawShow', params:law.encodeAsLinkProperties(), absolute:true)}',
                                picture: '${law.image.url}'
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
                    $("#socialFacebook_${law.id}").on('click',function(e){e.preventDefault(); shareLaw_${law.id}()})
                })
            </script>
            <a href="#" id="socialFacebook_${law.id}" class="social-share facebook">
                <span class="sr-only"><g:message code="law.social.facebook"/></span><span class="fa-stack fa-lg"><span class="fa fa-circle fa-stack-2x"></span><span class="fa fa-facebook fa-stack-1x"></span></span>
            </a>
        </li>
        %{--<li><a href="#"><span class="sr-only">LinkedIn</span><span class="fa-stack fa-lg"><span class="fa fa-circle fa-stack-2x"></span><span class="fa fa-linkedin fa-stack-1x"></span></span></a></li>--}%
        <li>
            <g:set var="shortUrl" value="${law.shortUrl?:createLink(mapping:'lawShow', params:law.encodeAsLinkProperties(), absolute: true)}"/>
            <a href="https://plus.google.com/share?url=${shortUrl}" class="social-share google">
                <span class="sr-only"><g:message code="law.social.googlePlus"/></span><span class="fa-stack fa-lg"><span class="fa fa-circle fa-stack-2x"></span><span class="fa fa-google-plus fa-stack-1x"></span></span>
            </a>
        </li>
    </ul>
</div>