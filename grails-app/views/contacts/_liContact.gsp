<li>
    <span class="user-img"><img src="${image.contactImgSrc(contact:contact)}" alt="${contact.name}" itemprop="image"></span>
    <h3 class="title">
        <g:link mapping="politicianContactEdit" params="[contactId:contact.id]" class="contactStats">${contact.name}</g:link>
        <g:link mapping="politicianContactEdit" params="[contactId:contact.id]" class="contactStats" target="_blank">

            <span class="fa fa-external-link fa-sm"></span><span class="sr-only"><g:message code="tools.contact.list.contact.edit"/></span>
        </g:link>
    </h3>
    <p class="email"><span class="fa fa-envelope-o"></span> ${contact.email}</p>
    <p class="followers">
        <span class="fa fa-user"></span> <g:message code="tools.contact.list.contact.followers" args="[contact.numFollowers]"/>
    </p>
    <div class="container-lists">
        <g:render template="/contacts/inputs/editContactTags" model="[contact:contact]"/>
        <g:render template="/contacts/inputs/showContactEngagement" model="[contact:contact]"/>
    </div>
    <g:link mapping="politicianContactEdit" params="[contactId:contact.id]" class="contactStats">
        <span class="fa fa-edit"></span>
        <span class="sr-only"><g:message code="tools.contact.list.contact.edit"/></span>
    </g:link>
    <a href="#" class="contactDelete" id="delete">
        <span class="fa fa-trash"></span> <span class="sr-only">Delete</span>
    </a>
</li>

<!-- MODAL CONFIRM -->
<div class="modal fade in" id="contactDeleteConfirm" tabindex="-1" role="dialog" aria-labelledby="contactDeleteTitle" aria-hidden="true">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                    <span aria-hidden="true" class="fa fa-times-circle-o fa"></span><span class="sr-only"><g:message code="modalDefend.close"/></span>
                </button>
                <h4 id="contactDeleteTitle">
                    <g:message code="tools.massMailing.deleteContactModal.title"/>
                </h4>
            </div>
            <div class="modal-body">
                <g:link mapping="politicianContactRemoveAjax" params="[contactId:contact.id]" role="button" class="btn btn-blue inverted btn-lg deleteContactBtn">
                    <g:message code="tools.massMailing.deleteContactModal.button"/>
                </g:link>
            </div>
        </div>
    </div>
</div>