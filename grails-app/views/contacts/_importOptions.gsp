<ul class="actionIcons">
    <li><g:link mapping="politicianContactImportGmail" role="button" class="actionIcons" elementId="gmail"><g:message code="tools.contact.import.options.gmail"/></g:link></li>
    <li><oauth:connect provider="yahoo" role="button" class="actionIcons" elementId="yahoo">Yahoo!</oauth:connect></li>
    <li><oauth:connect provider="outlook" id="yahoo-connect-link" role="button" class="actionIcons" elementId="outlook"><g:message code="tools.contact.import.options.outlook"/></oauth:connect></li>
    <li class="fontIcon">
        <g:link mapping="politicianContactImportCSV" role="button" class="actionIcons">
                <span class="fa fa-file-o"></span>
                <span class="label"><g:message code="tools.contact.import.options.csv"/></span>
        </g:link></li>
</ul>