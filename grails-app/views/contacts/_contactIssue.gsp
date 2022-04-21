<li class="col-xs-12 contact-issue">
    <div class="col-md-2 contact-issue-date">
        <label class="hidden-md hidden-lg"><g:message
                code="tools.contact.edit.tabs.contactIssues.table.labels.date"/>:</label>
        <span data-toggle="tooltip" data-placement="top"
              title="${g.formatDate([type: "datetime", style: "SHORT", date: contactIssue.dateCreated])}">${kuorumDate.humanDate([date: contactIssue.dateCreated])}</span>
    </div>

    <div class="col-md-1 contact-issue-resolver">
        <label class="hidden-md hidden-lg"><g:message
                code="tools.contact.edit.tabs.contactIssues.table.labels.resolver"/>:</label>
        ${contactIssue.resolver}
    </div>

    <div class="col-md-1 contact-issue-campaign">
        <label class="hidden-md hidden-lg"><g:message
                code="tools.contact.edit.tabs.contactIssues.table.labels.campaign"/>:</label>
        <g:link mapping="campaignShow"
                params="${contactIssue.lightCampaign.encodeAsLinkProperties()}">${contactIssue.lightCampaign.title}</g:link>
    </div>

    <div class="col-md-2 contact-issue-type">
        <label class="hidden-md hidden-lg"><g:message
                code="org.kuorum.rest.model.contact.ContactIssueTypeDTO.label"/>:</label>
        <g:message code="org.kuorum.rest.model.contact.ContactIssueTypeDTO.${contactIssue.issueType}"/>
    </div>

    <div class="col-md-5 contact-issue-note">
        <label class="hidden-md hidden-lg"><g:message
                code="tools.contact.edit.tabs.contactIssues.table.labels.note"/>:</label>
        ${contactIssue.note}
    </div>

    <div class="col-md-1 contact-issue-delete">
        <label class="hidden-md hidden-lg"><g:message
                code="tools.contact.edit.tabs.contactIssues.table.labels.delete"/>:</label>
        <g:link mapping="politicianContactDeleteIssues" params="[contactId: contact.id, issueId: contactIssue.id]"
                class="btn btn-grey-light"><span class="fal fa-trash"></span></g:link>
    </div>
</li>