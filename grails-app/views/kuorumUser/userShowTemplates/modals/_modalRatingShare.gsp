<%@ page import="kuorum.core.customDomain.CustomDomainResolver" %>

<r:require modules="social"/>

<g:set var="titleModal" value="${titleModal?:message(code:'politician.valuation.modal.share.title', args: [user.name])}"/>
<g:set var="paragraphModal" value="${paragraphModal?:message(code:'politician.valuation.modal.share.description', args: [user.name])}"/>

<div class="modal fade rating-social-share-modal" id="rating-social-share-modal-${user.id}" tabindex="-1" role="dialog" aria-labelledby="dynamicInputOverflow_${formId}" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar"><span aria-hidden="true" class="fal fa-times-circle fa"></span><span class="sr-only">Cerrar</span></button>
                <h4 class="modal-title">${titleModal}</h4>
            </div>
            <div class="modal-body clearfix">
                <p>${paragraphModal}</p>
                <div class="form-group">
                    <g:set var="userLink"><g:createLink
                            mapping="userShow"
                            params="${user.encodeAsLinkProperties()}"
                            absolute="true"/></g:set>

                    <ul class="share-buttons">
                    <li></li>
                        <li>
                            <g:set var="facebookLink">https://www.facebook.com/sharer/sharer.php?u=${userLink}</g:set>
                            <a href="${facebookLink}" target="_blank" title="${g.message(code:"project.social.facebook")}">
                                <i class="social-share fab fa-facebook-f"></i>
                            </a>
                        </li>
                        <li>
                            <g:set var="twitterName" value="${user?.socialLinks?.twitter?.encodeAsTwitter()?:user.name}"/>
                            <g:set var="twitterShareText"><g:message
                                    code="politician.valuation.modal.share.twitter.text"
                                    args="[twitterName, kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name]"
                                    encodeAs="HTML"/></g:set>
                            <g:set var="twitterLink">https://twitter.com/share?url=${userLink}&text=${twitterShareText}&hashtags=${user.alias?:""}</g:set>

                            <a href="${twitterLink}" target="_blank" title="${g.message(code: 'project.social.twitter')}">
                                <i class="social-share fab fa-twitter"></i>
                            </a>
                        </li>
                        <li>
                            <a href="https://plus.google.com/share?url=${userLink}" target="_blank" title="${g.message(code:'project.social.googlePlus')}">
                                <i class="social-share fab fa-google-plus-g"></i>
                            </a>
                        </li>
                        <li>
                            <a href="http://www.linkedin.com/shareArticle?mini=true&url=${userLink}&title=${user.name}&summary=${user.bio}&source=kuorum.org" target="_blank" title="${g.message(code:'project.social.linkedin')}">
                                <i class="social-share fab fa-linkedin"></i>
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>