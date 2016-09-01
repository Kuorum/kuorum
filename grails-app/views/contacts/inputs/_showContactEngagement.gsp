<div class="engagement-container clearfix">
    <h4><g:message code="tools.contact.list.contact.engagement"/></h4>
    <ul class="engagement">
        <li class="${contact.status == org.kuorum.rest.model.contact.ContactStatusRSDTO.INACTIVE?'active':''}"><a href="#"><g:message code="org.kuorum.rest.model.contact.ContactStatusRSDTO.INACTIVE"/> </a></li>
        <li class="${contact.status == org.kuorum.rest.model.contact.ContactStatusRSDTO.READER?'active':''}"><a href="#"><g:message code="org.kuorum.rest.model.contact.ContactStatusRSDTO.READER"/> </a></li>
        <li class="${contact.status == org.kuorum.rest.model.contact.ContactStatusRSDTO.SUPPORTER?'active':''}"><a href="#"><g:message code="org.kuorum.rest.model.contact.ContactStatusRSDTO.SUPPORTER"/> </a></li>
        <li class="${contact.status == org.kuorum.rest.model.contact.ContactStatusRSDTO.BROADCASTER?'active':''}"><a href="#"><g:message code="org.kuorum.rest.model.contact.ContactStatusRSDTO.BROADCASTER"/> </a></li>
    </ul>
</div>