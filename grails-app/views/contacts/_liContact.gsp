<li>
    <span class="user-img"><img src="${image.contactImgSrc(contact:contact)}" alt="${contact.name}" itemprop="image"></span>
    <h3 class="title"><a href="#">${contact.name}</a></h3>
    <p class="email"><span class="fa fa-envelope-o"></span> <a href="#">${contact.email}</a></p>
    <p class="followers">
        <span class="fa fa-user"></span> <g:message code="tools.contact.list.contact.followers" args="[contact.numFollowers]"/>
        <a href="#"><span class="fa fa-external-link fa-fw"></span><span class="sr-only"><g:message code="tools.contact.list.contact.edit"/></span></a>
    </p>
    <div class="container-lists">
        <form action="${g.createLink(mapping: 'politicianContactAddTagsAjax',params: [contactId:contact.id])}" class="addTag off">
            <a href="#" role="button" class="tag label label-info addTagBtn"><span class="fa fa-tag"></span><g:message code="tools.contact.list.contact.editTags"/></a>
            <ul>
                <g:each in="${contact.tags}" var="tag">
                    <li><a href="#" class="tag label label-info">${tag}</a></li>
                </g:each>
            </ul>
            <label for="tagsField_${contact.id}" class="sr-only"><g:message code="tools.contact.list.contact.saveTags"/> </label>
            <input id="tagsField_${contact.id}" name="tags" class="tagsField" type="text" data-urlTags="${g.createLink(mapping:'politicianContactTagsAjax')}" value="${contact.tags.join(",")}">
            <input type="submit" value="Save tags" class="btn btn-blue inverted" id="inputAddTags">
        </form>
        <div class="engagement-container clearfix">
            <h4><g:message code="tools.contact.list.contact.engagement"/></h4>
            <ul class="engagement">
                <li class="${contact.status == org.kuorum.rest.model.contact.ContactStatusRSDTO.INACTIVE?'active':''}"><a href="#"><g:message code="org.kuorum.rest.model.contact.ContactStatusRSDTO.INACTIVE"/> </a></li>
                <li class="${contact.status == org.kuorum.rest.model.contact.ContactStatusRSDTO.READER?'active':''}"><a href="#"><g:message code="org.kuorum.rest.model.contact.ContactStatusRSDTO.READER"/> </a></li>
                <li class="${contact.status == org.kuorum.rest.model.contact.ContactStatusRSDTO.SUPPORTER?'active':''}"><a href="#"><g:message code="org.kuorum.rest.model.contact.ContactStatusRSDTO.SUPPORTER"/> </a></li>
                <li class="${contact.status == org.kuorum.rest.model.contact.ContactStatusRSDTO.BROADCASTER?'active':''}"><a href="#"><g:message code="org.kuorum.rest.model.contact.ContactStatusRSDTO.BROADCASTER"/> </a></li>
            </ul>
        </div>
    </div>
    <a href="#" class="contactStats">
        <span class="fa fa-edit"></span>
        <span class="sr-only"><g:message code="tools.contact.list.contact.edit"/></span>
    </a>
    <a href="#" role="button" class="contactDelete">
        <span class="fa fa-trash"></span>
        <span class="sr-only"><g:message code="tools.contact.list.contact.delete"/></span>
    </a>
</li>
