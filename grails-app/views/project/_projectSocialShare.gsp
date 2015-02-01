<r:require modules="social"/>
<g:set var="shortUrl" value="${project.shortUrl?:createLink(mapping:'projectShow', params:project.encodeAsLinkProperties(), absolute: true)}"/>
<g:set var="twitterShareText"><g:message
        code="project.social.twitter.shareMessage"
        args="[
                project.hashtag.encodeAsURL()
        ]"/></g:set>
<g:set var="twitterLink">https://twitter.com/share?url=${shortUrl}&text=${twitterShareText}&via=kuorumorg&hashtags=${project.hashtag}</g:set>


<div class="social">
    <p><g:message code="project.social.title"/></p>
    <ul class="social">
        <li>
            <a href="${twitterLink}" class="social-share twitter">
                <span class="sr-only"><g:message code="project.social.twitter"/> </span>
                <span class="fa-stack fa-lg"><span class="fa fa-twitter fa-stack-1x"></span></span>
            </a>
        </li>
        <li>
            %{--<script>--}%
                %{--$(function(){--}%
                    %{--facebookData["project{project.id}"]={--}%
                        %{--name: '${project.shortName.replaceAll('\'', '\\\'')}',--}%
                        %{--caption: '${project.realName.replaceAll('\'', '\\\'')}',--}%
                        %{--description: '${project.introduction.trim().replaceAll('\'', '\\\'')}',--}%
                        %{--link: '${project.shortUrl?:createLink(mapping: 'projectShow', params:project.encodeAsLinkProperties(), absolute:true)}',--}%
                        %{--picture: '${project.image.url}'--}%
                    %{--}--}%
                %{--});--}%
            %{--</script>--}%
            %{--<a href="#" class="social-share facebook" data-facebookDataId="project_${project.id}">--}%
                %{--<span class="sr-only"><g:message code="project.social.facebook"/></span><span class="fa-stack fa-lg"><span class="fa fa-circle fa-stack-2x"></span><span class="fa fa-facebook fa-stack-1x"></span></span>--}%
            %{--</a>--}%
            %{--<a href="https://www.facebook.com/sharer/sharer.php?u=http://test.kuorum.org/leyes/parlamento-espanol/empleo-y-seguridad-social/recualificacionprofesional" class="social-share facebook" data-facebookDataId="project_${project.id}">--}%
                %{--<span class="sr-only"><g:message code="project.social.facebook"/></span><span class="fa-stack fa-lg"><span class="fa fa-circle fa-stack-2x"></span><span class="fa fa-facebook fa-stack-1x"></span></span>--}%
            %{--</a>--}%
            <g:set var="facebookLink">https://www.facebook.com/sharer/sharer.php?u=${shortUrl}</g:set>
            <a href="${facebookLink}" class="social-share facebook" data-facebookDataId="project_${project.id}">
                <span class="sr-only"><g:message code="project.social.facebook"/></span><span class="fa-stack fa-lg"><span class="fa fa-facebook fa-stack-1x"></span></span>
            </a>

        </li>
        %{--<li><a href="#"><span class="sr-only">LinkedIn</span><span class="fa-stack fa-lg"><span class="fa fa-circle fa-stack-2x"></span><span class="fa fa-linkedin fa-stack-1x"></span></span></a></li>--}%
        <li>
            <a href="https://plus.google.com/share?url=${shortUrl}" class="social-share google">
                <span class="sr-only"><g:message code="project.social.googlePlus"/></span><span class="fa-stack fa-lg"><span class="fa fa-google-plus fa-stack-1x"></span></span>
            </a>
        </li>
    </ul>
</div>