
<r:require modules="social"/>

<div class="modal fade" id="rating-social-share-modal" tabindex="-1" role="dialog" aria-labelledby="dynamicInputOverflow_${formId}" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar"><span aria-hidden="true" class="fa fa-times-circle-o fa"></span><span class="sr-only">Cerrar</span></button>
                <h4 class="modal-title"><g:message code="politician.valuation.modal.share.title" args="[user.name]"/> </h4>
            </div>
            <div class="modal-body clearfix">
                <p><g:message code="politician.valuation.modal.share.description" args="[user.name]" /></p>
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
                                <i class="social-share fa fa-facebook"></i>
                            </a>
                        </li>
                        <li>
                            <g:set var="twitterName" value="${user?.socialLinks?.twitter?.encodeAsTwitter()?:user.name}"/>
                            <g:set var="twitterShareText"><g:message
                                    code="politician.valuation.modal.share.twitter.text"
                                    args="[twitterName]"
                                    encodeAs="HTML"/></g:set>
                            <g:set var="twitterLink">https://twitter.com/share?url=${userLink}&text=${twitterShareText}&hashtags=${user.alias?:""}</g:set>

                            <a href="${twitterLink}" target="_blank" title="${g.message(code: 'project.social.twitter')}">
                                <i class="social-share fa fa-twitter"></i>
                            </a>
                        </li>
                        <li>
                            <a href="https://plus.google.com/share?url=${userLink}" target="_blank" title="${g.message(code:'project.social.googlePlus')}">
                                <i class="social-share fa fa-google-plus"></i>
                            </a>
                        </li>
                        <li>
                            <a href="http://www.linkedin.com/shareArticle?mini=true&url=${userLink}&title=${user.name}&summary=${user.bio}&source=kuorum.org" target="_blank" title="${g.message(code:'project.social.linkedin')}">
                                <i class="social-share fa fa-linkedin"></i>
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>