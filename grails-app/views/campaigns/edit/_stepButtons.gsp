<fieldset class="buttons">
    <div class="text-right">
        <ul class="form-final-options">
            <g:if test="${mappings.saveAndSentButtons}">
                <g:render template="/campaigns/edit/stepButtonsSaveAndSend" model="[mappings:mappings, status:status, command: command, numberRecipients:numberRecipients]"/>
            </g:if>
            <g:else>
                <g:render template="/campaigns/edit/stepButtonsNext" model="[mappings:mappings]"/>
            </g:else>
        </ul>
    </div>
</fieldset>