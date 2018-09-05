<ul class="actionIcons">
    %{--<li><g:link mapping="politicianContactImportGmail" role="button" class="actionIcons" elementId="gmail"><g:message--}%
            %{--code="tools.contact.import.options.gmail"/></g:link></li>--}%
    <li><contactUtil:importSocialContact provider="google">Gmail</contactUtil:importSocialContact></li>
    <li><contactUtil:importSocialContact provider="yahoo">Yahoo!</contactUtil:importSocialContact></li>
    <li><contactUtil:importSocialContact provider="outlook">Outlook</contactUtil:importSocialContact></li>
    <li class="fontIcon">
        <g:link mapping="politicianContactImportCSV" role="button" class="mail" elementId="uploadCsv">
            <span class="fas file-upload"></span>
            <span class="label"><g:message code="tools.contact.import.options.csv"/></span>
        </g:link>
    </li>

</ul>