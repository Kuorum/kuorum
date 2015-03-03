<%@ page import="springSecurity.KuorumRegisterCommand; kuorum.core.model.PostType" %>
<postUtil:userOption post="${post}">
    <postUtil:asNoLogged>
        <g:set var="registerCommand" value="${new KuorumRegisterCommand()}"/>
        <formUtil:validateForm bean="${registerCommand}" form="drive-noLogged"/>
        <g:form mapping="register" autocomplete="off" method="post" name="drive-noLogged" class="login" role="form" novalidate="novalidate">
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

            <div class="form-group">
                <a class="btn btn-blue btn-lg btn-block" href="#"><span class="fa fa-rocket fa-2x"></span> <g:message code="post.show.boxes.like.vote.button"/> </a>
            </div>
            <div class="form-group">
                <label class="checkbox-inline"><input type="checkbox" value="public" id="publico"> <g:message code="post.show.boxes.like.vote.anonymousCheckBoxLabel"/> </label>
            </div>
            <div class="form-group">
                <small><g:message code="register.conditions" args="[g.createLink(mapping: 'footerPrivacyPolicy')]" encodeAs="raw"/></small>
            </div>

        </g:form>
    </postUtil:asNoLogged>
    <postUtil:asPolitician>
        <form id="driveDefend">
            <div class="form-group">
                <a href="#"
                        class="${post.defender ? 'disabled' : ''} btn btn-blue btn-lg btn-block openModalDefender"
                        data-toggle="modal" data-target="#modalSponsor" params="${post.encodeAsLinkProperties()}"
                        data-postId="${post.id}">
                    <span class="fa fa-trophy fa-2x">
                    <g:message code="post.show.boxes.like.defend.${post.defender ? 'buttonDefended' : 'button'}" encodeAs="raw"/>
                </a>
            </div>
        </form>
        <g:set var="postType" value="${message(code: "${PostType.class.name}.${post.postType}")}"/>
        <script>
            $(function () {
                modalDefend.data.post_${post.id} = {
                    owner: {
                        name: "${post.owner.name}",
                        imageUrl: "${image.userImgSrc(user:post.owner)}"
                    },
                    defender: {
                        name: "${sec.loggedInUserInfo(field:'name')}",
                        imageUrl: "${image.loggedUserImgSrc()}"
                    },
                    post: {
                        sponsorLabel: '${message(code:'modalDefend.defender.sponsorLabel', args:[postType])}',
                        what: '${message(code:'modalDefend.owner.what', args:[postType])}',
                        title: "${post.title}",
                        options: [
                            %{-- Guarrerida para generar el js que se necesita para pintar la ventana modal --}%
                            <g:each in="${kuorum.core.model.CommitmentType.recoverCommitmentTypesByPostType(post.postType)}" var="commitmentType">
                            {
                                textButton: "${message(code:"modalDefend.option.${commitmentType}.button")}",
                                textDescription: "${message(code:"modalDefend.option.${commitmentType}.description")}",
                                defendLink: "${g.createLink(mapping: 'postAddDefender', params:post.encodeAsLinkProperties() + [commitmentType:commitmentType])}"
                            },
                            </g:each>
                            {
                                kk:'this js object is for not delete the las comma. Where it is used dont iterate over this array'
                            }
                        ],
                        description: "${message(code:'modalDefend.description', args:[post.owner.name, postType, post.numVotes])}",
                        numVotes:${post.numVotes}
                    }
                }
            });
        </script>
    </postUtil:asPolitician>
    <postUtil:asUser>
        <form id="drive">
            <div class="form-group">
                <g:link mapping="postVoteIt"
                        class="${userVote ? 'disabled' : ''} btn  btn-blue btn-lg btn-block"
                        params="${post.encodeAsLinkProperties()}" data-postId="${post.id}">
                    <span class="fa fa-rocket fa-2x"></span>
                    <g:message code="post.show.boxes.like.vote.${userVote ? 'buttonVoted' : 'button'}" encodeAs="raw"/>
                </g:link>
                <g:if test="${!userVote}">
                    <div class="form-group">
                        <label class="checkbox-inline">
                            <input type="checkbox" name="anonymous"
                                   value="private"/>
                            <g:message code="post.show.boxes.like.vote.anonymousCheckBoxLabel"/>
                        </label>
                    </div>
                </g:if>
        </form>
    </postUtil:asUser>
</postUtil:userOption>
