<ul class="actionIcons">
    <li><contactUtil:importSocialContact provider="google">Gmail</contactUtil:importSocialContact></li>
    <li><contactUtil:importSocialContact provider="yahoo">Yahoo!</contactUtil:importSocialContact></li>
    <li><contactUtil:importSocialContact provider="outlook">Outlook</contactUtil:importSocialContact></li>
    <li class="fontIcon">
        <g:link mapping="politicianContactImportCSV" role="button" class="mail" elementId="uploadCsv">
            <span class="fal fa-file-upload"></span>
            <span class="label"><g:message code="tools.contact.import.options.csv"/></span>
        </g:link>
    </li>

</ul>