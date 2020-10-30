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
        <p class="email">
            <g:if test="${contact.personalCode}">
                <span class="fal fa-key" rel="tooltip" data-toggle="tooltip" data-placement="bottom" title="" data-original-title="${g.message(code:'tools.contact.edit.personalCode.labelActive')}"></span>
            </g:if>
            <contactUtil:printContactMail contact="${contact}"/>
        </p>
        <g:if test="${contact.externalId}">
            <p class="externalId">
                <span class="fal fa-id-card"></span> ${contact.externalId}
            </p>
        </g:if>
        <g:else>
            <p class="followers">
                <span class="fal fa-users"></span><g:message code="tools.contact.list.contact.followers" args="[contact.numFollowers]"/>
            </p>
        </g:else>

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
        <g:set var="removeIcon" value="fa-eraser"/>
        <g:if test="${!contact.isFollower}" var="hasEmail">
            <g:set var="removeIcon" value="fa-trash"/>
        </g:if>
        <li>
            <g:link mapping="politicianContactRemoveAjax" params="[contactId:contact.id]" role="button" class="contactDelete">
                <span class="fal ${removeIcon}"></span>
                <span class="sr-only"><g:message code="tools.contact.list.contact.delete"/></span>
            </g:link>
        </li>
    </ul>
</li>
