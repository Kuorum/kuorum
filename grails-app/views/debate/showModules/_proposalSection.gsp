
<!-- ^comment-box !-->
<div class="comment-box proposal-comment-box ${debate.event && !debate.event.registered?'hide':''} clearfix">
    <div class="user-box col-md-1 col-xs-12">
        <img class="img-circle" alt="${userUtil.loggedUserName()}" src="${image.loggedUserImgSrc()}">
    </div>

    <div class="comment editable col-md-11 col-xs-12" data-placeholder="${message(code: "debate.proposal.placeholder")}" style="min-height: 100px; padding-top: 10px"></div>
    <div class="col-md-11 col-xs-12 pull-right">
        <span for="editable" class="error" style="display: none;"><span class="tooltip-arrow"></span>${message(code: "debate.proposals.error")}</span>
    </div>

    <div class="actions pull-right">
        <button
                type="button"
                class="btn btn-blue btn-lg publish publish-proposal"
                data-userLoggedAlias="${userUtil.loggedUserAlias()}"
                data-postUrl="${g.createLink(mapping: 'debateProposalNew')}"
                data-debateId="${debate.id}"
                data-debateAlias="${debateUser.alias}">
            <g:message code="debate.publish" />
        </button>
    </div>
</div> <!-- ^comment-box !-->

<ul id="proposal-option" class="nav nav-pills nav-underline" style="display: none">
    <li><a href="#latest"><g:message code="debate.proposals.nav.latest"/> </a></li>
    <li><a href="#oldest"><g:message code="debate.proposals.nav.oldest"/></a></li>
    <li><a href="#best"><g:message code="debate.proposals.nav.best"/></a></li>
    <li><a href="#pinned"><g:message code="debate.proposals.nav.pinned"/></a></li>
</ul>

<ul class="proposal-list">
    <g:each in="${proposalPage.data}" var="proposal">
        <g:render template="/debate/showModules/mainContent/proposalData" model="[debate:debate, debateUser:debateUser, proposal:proposal]"/>
    </g:each>
</ul>
<!-- propusal block !-->

%{-- SEE MORE HIDDEN

<div class="see-more-content"> <!-- ^see-more-content !-->
    <button type="button" class="btn angle-down" data-anchor="conversation-box">See more</button>
</div> <!-- ^see-more-content !-->

--}%

%{--Confirm modal for delete proposals and comments --}%
<div class="modal fade in" id="debateDeleteConfirm" tabindex="-1" role="dialog" aria-labelledby="debateDeleteTitle" aria-hidden="true">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                    <span aria-hidden="true" class="fa fa-times-circle-o fa"></span><span class="sr-only"><g:message code="modalDefend.close"/></span>
                </button>
                <h4 id="debateDeleteTitle">
                    <g:message code="debate.modalDelete.title"/>
                </h4>
            </div>
            <fieldset class="modal-body">
                <a href="#UrlUpdatedByAjax" role="button" class="btn btn-blue inverted btn-lg pull-right" id="modalDeleteDebateButton">
                    <g:message code="tools.massMailing.deleteContactModal.button"/>
                </a>
            </fieldset>
        </div>
    </div>
</div>