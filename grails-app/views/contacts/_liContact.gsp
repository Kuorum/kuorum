<li>
    <div class="contact-info">
        <formUtil:checkBox
            command="${bulkActionContactsCommand}"
            field="listIds"
            label=" "
            value="${contact.id}"
        />

        <span class="user-img"><img src="${image.contactImgSrc(contact:contact)}" alt="${contact.name} ${contact.surname}" itemprop="image"></span>
        <h3 class="title">
            <g:link mapping="politicianContactEdit" params="[contactId:contact.id]" class="contactStats">${contact.name} ${contact.surname}</g:link>
            <g:link mapping="politicianContactEdit" params="[contactId:contact.id]" target="_blank">

                <span class="fal fa-external-link fa-sm"></span><span class="sr-only"><g:message code="tools.contact.list.contact.edit"/></span>
            </g:link>
        </h3>
        <p class="email"><contactUtil:printContactMail contact="${contact}"/></p>
        <p class="followers">
            <span class="fal fa-users"></span><g:message code="tools.contact.list.contact.followers" args="[contact.numFollowers]"/>
        </p>
    </div>
    <div class="container-lists">
        <g:render template="/contacts/inputs/editContactTags" model="[contact:contact]"/>
        <g:render template="/contacts/inputs/showContactEngagement" model="[contact:contact]"/>
    </div>
    <ul class="list-actions">
        <li>
            <g:link mapping="politicianContactEdit" params="[contactId:contact.id]" class="contactStats">
                <span class="fal fa-edit"></span>
                <span class="sr-only"><g:message code="tools.contact.list.contact.edit"/></span>
            </g:link>
        </li>
        <g:if test="${contact.email}" var="hasEmail">
            <li>
                <g:link mapping="politicianContactRemoveAjax" params="[contactId:contact.id]" role="button" class="contactDelete">
                    <span class="fal fa-trash"></span>
                    <span class="sr-only"><g:message code="tools.contact.list.contact.delete"/></span>
                </g:link>
            </li>
        </g:if>
    </ul>
</li>
