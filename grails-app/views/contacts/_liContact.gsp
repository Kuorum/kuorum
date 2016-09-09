<li>
    <span class="user-img"><img src="${image.contactImgSrc(contact:contact)}" alt="${contact.name}" itemprop="image"></span>
    <h3 class="title">
        <g:link mapping="politicianContactEdit" params="[contactId:contact.id]" class="contactStats">${contact.name}</g:link>
        <g:link mapping="politicianContactEdit" params="[contactId:contact.id]" class="contactStats" target="_blank">

            <span class="fa fa-external-link fa-sm"></span><span class="sr-only"><g:message code="tools.contact.list.contact.edit"/></span>
        </g:link>
    </h3>
    <p class="email"><span class="fa fa-envelope-o"></span> <a href="#">${contact.email}</a></p>
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
    <g:link mapping="politicianContactRemoveAjax" params="[contactId:contact.id]" role="button" class="contactDelete">
        <span class="fa fa-trash"></span>
        <span class="sr-only"><g:message code="tools.contact.list.contact.delete"/></span>
    </g:link>
</li>
