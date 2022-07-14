<g:if test="${contact}">
    <g:set var="contactLink"
           value="${g.createLink(mapping: 'politicianContactEdit', params: [contactId: contact.id])}"/>
    <g:render template="/campaigns/columnCModules/campaignFiles"
              model="[campaignFiles: contactFiles, title: g.message(code: 'campaign.show.contact-files.title'), subtitle: g.message(code: 'campaign.show.contact-files.subtitle', args: [contactLink, contact.name])]"/>
</g:if>