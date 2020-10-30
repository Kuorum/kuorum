
<g:set var="personalCodeText" value="REPLACE"/>
<g:if test="${contact.personalCode}">
    <g:set var="personalCodeText" value="${g.message(code: 'tools.contact.edit.personalCode.noVisible')}"/>
    <sec:ifSwitched>
        <g:set var="personalCodeText" value="${contact.personalCode}"/>
    </sec:ifSwitched>
</g:if>
<g:else>
    <g:set var="personalCodeText" value="${g.message(code: 'tools.contact.edit.personalCode.noGenerated')}"/>
</g:else>

<label for='personalCode' class=''>Personal code</label>
<input type="text" name="personalCode" id="personalCode" class="form-control input-lg"  value="${personalCodeText}" disabled />
<g:if test="${contact.personalCode}">
    <g:link mapping="politicianContactPersonalCodeRemove" params="[contactId: contact.getId()]" class="btn btn-grey inverted delete" data-toggle="tooltip" data-placement="top" title="${g.message(code:'tools.contact.edit.personalCode.remove.label')}">
        <span class="fas fa-key"></span>
    </g:link>
</g:if>
<g:else>
    <g:link mapping="politicianContactPersonalCodeGenerate" params="[contactId: contact.getId()]" class="btn btn-grey inverted" data-toggle="tooltip" data-placement="top" title="${g.message(code:'tools.contact.edit.personalCode.generate.label')}">
        <span class="fas fa-key"></span>
    </g:link>
</g:else>
