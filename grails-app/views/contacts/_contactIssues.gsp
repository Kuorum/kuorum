<g:set var="command" value="${new kuorum.web.commands.payment.contact.ContactIssueCommand()}"/>
<formUtil:validateForm form="addContactIssueForm" bean="${command}" dirtyControl="true"/>
<g:form mapping="politicianContactAddIssues" params="[contactId: contact.id]" name="addContactIssueForm">
    <fieldset class="row">
        <div class="form-group col-md-3">
            <formUtil:selectAjax field="campaignId" command="${command}" showLabel="true" defaultEmpty="true"
                                 ajaxOptionsMapping="politicianCampaignsMyActives"/>
        </div>

        <div class="form-group col-md-2">
            <formUtil:selectEnum field="issueType" command="${command}" showLabel="true" defaultEmpty="true"/>
        </div>

        <div class="form-group col-md-6">
            <formUtil:input field="note" command="${command}" showLabel="true"/>
        </div>

        <div class="form-group col-md-1 contact-issue-add-button">
            <input type="submit" value="${g.message(code: 'default.save')}" class="btn btn-blue inverted">
        </div>
    </fieldset>
</g:form>
<h4 class="center"><g:message code="tools.contact.edit.tabs.contactIssues.table.title"/></h4>
<ul class="contact-issues">
    <li class="col-xs-12 contact-issue header hidden-sm hidden-xs">
        <div class="col-md-2 contact-issue-date"><g:message
                code="tools.contact.edit.tabs.contactIssues.table.labels.date"/></div>

        <div class="col-md-1 contact-issue-resolver"><g:message
                code="tools.contact.edit.tabs.contactIssues.table.labels.resolver"/></div>

        <div class="col-md-1 contact-issue-campaign"><g:message
                code="tools.contact.edit.tabs.contactIssues.table.labels.campaign"/></div>

        <div class="col-md-2 contact-issue-type"><g:message
                code="org.kuorum.rest.model.contact.ContactIssueTypeDTO.label"/></div>

        <div class="col-md-6 contact-issue-note"><g:message
                code="tools.contact.edit.tabs.contactIssues.table.labels.note"/></div>
    </li>
    <g:each in="${contactIssues}" var="contactIssue">
        <g:render template="contactIssue" model="[contactIssue: contactIssue, contact: contact]"/>
    </g:each>
</ul>