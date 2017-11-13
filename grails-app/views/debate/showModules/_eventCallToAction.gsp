<r:require modules="recaptcha_modalRegister"/>
<g:set var="registerCommand" value="${new springSecurity.KuorumRegisterCommand()}"/>
<g:form mapping="register" action-ajax="${g.createLink(mapping: 'registerAjax')}" autocomplete="off" method="post" name="${formId}" class="login" role="form" novalidate="novalidate">
    <div class="comment-box call-to-action hidden-sm">
        <div class="comment-header">
            <span class="call-title">${message(code: "event.callToAction.title")}</span>
            <span class="call-subTitle">${message(code: "event.callToAction.subTitle", args: [debateUser.name])}</span>
        </div>
        <div class="comment-proposal clearfix">
            <div class="form-group">
                <formUtil:input
                        command="${registerCommand}"
                        field="name"
                        cssClass="form-control input-lg"
                        labelCssClass="sr-only"
                        showCharCounter="false"
                        required="true"/>
            </div>

            <div class="form-group">
                <formUtil:input
                        command="${registerCommand}"
                        field="email"
                        type="email"
                        id="email"
                        cssClass="form-control input-lg"
                        labelCssClass="sr-only"
                        required="true"/>
            </div>
        </div>

        <div class="actions clearfix">
            <button
                    type="button"
                    class="btn btn-blue btn-lg publish publish-proposal"
                    data-recaptcha=""
                    data-callback="registerModalCallback"
                    data-userLoggedAlias="${userUtil.loggedUserAlias()}"
                    data-postUrl="${g.createLink(mapping: 'debateProposalNew')}"
                    data-debateId="${debate.id}"
                    data-debateAlias="${debateUser.alias}"
            >
                ${message(code: "event.callToAction.button")}
            </button>
        </div>
    </div>
</g:form>