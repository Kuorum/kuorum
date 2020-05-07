
<sec:ifLoggedIn><g:set var="isSigned" value="${true}"/></sec:ifLoggedIn>
<sec:ifNotLoggedIn><g:set var="isSigned" value="${false}"/></sec:ifNotLoggedIn>
<g:set var="activeButton" value="${petition.signed && isSigned}"/>
<div class="comment-counter pull-right">
    <a href="${g.createLink(mapping: "petitionSign", params: petition.encodeAsLinkProperties())}"
       type="button"
       class="petition-sign petition-sign-${petition.id} ${activeButton?'active':''}"
       data-petitionId="${petition.id}"
       data-petitionUserId="${(petition instanceof org.kuorum.rest.model.search.kuorumElement.SearchPetitionRSDTO)?petition.ownerId:petition.user.id}"
       data-campaignValidationActive="${petition.checkValidationActive}"
%{--       data-campaignGroupValidationActive="${petition.groupValidation?g.createLink(mapping: "campaignCheckGroupValidation", params: petition.encodeAsLinkProperties()):''}"--}%
       data-loggedUser="${sec.username()}"
    >
        <span class="${activeButton?'fas':'fal'} fa-microphone" aria-hidden="true"></span>
        <span class="number">${petition.signs}</span>
    </a>
</div>