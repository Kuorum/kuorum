<g:if test="${showOnly}">
    <!-- Show only -->
    <fieldset class="form-group showOnly" id="toFilters">
        <label for="to" class="col-sm-2 col-md-1 control-label"><g:message code="tools.massMailing.fields.filter.to"/> :</label>
        <div class="col-sm-4">
            <g:each in="${filters}" var="filter">
                <g:if test="${command.filterId == filter.id}">
                    ${filter.name}
                </g:if>
            </g:each>
            <g:if test="${command.filterId == anonymousFilter?.id}">
                ${anonymousFilter.name}
            </g:if>
            <g:elseif test="${command.filterId == null}">
                <g:message code="tools.massMailing.fields.filter.to.all"/>
            </g:elseif>
        </div>
        <div class="col-sm-4">
            <span class="amountRecipients">
                <g:each in="${filters}" var="filter">
                    <g:if test="${command.filterId == filter.id}">
                        ${filter.amountOfContacts}
                    </g:if>
                </g:each>
                <g:if test="${command.filterId == anonymousFilter?.id}">
                    ${anonymousFilter.amountOfContacts}
                </g:if>
                <g:elseif test="${command.filterId == null}">
                    ${totalContacts?:0}
                </g:elseif>
            </span>
            <g:message code="tools.massMailing.fields.filter.recipients"/>
            %{--<span class="fa fa-filter fa-lg"></span>--}%
        </div>
        <input type="hidden" name="filterId" id="recipients" value="${(command.filterId) ? command.filterId : ((anonymousFilter?.id) ? anonymousFilter.id : 0)}" />
        <input type="hidden" name="filterEdited" value="0" />
        <div id="newFilterContainer">

        </div>
    </fieldset>
</g:if>
<g:else>
    <fieldset class="form-group" id="toFilters">
        <label for="to" class="col-sm-2 col-md-1 control-label"><g:message code="tools.massMailing.fields.filter.to"/> :</label>
        <div class="col-sm-4 col-md-3">
            <select name="filterId" class="form-control input-lg" id="recipients">
                %{--<g:if test="${totalContacts}">--}%
                    <option value="0" data-amountContacts="${totalContacts?:0}"><g:message code="tools.massMailing.fields.filter.to.all"/></option>
                %{--</g:if>--}%
                <g:each in="${filters}" var="filter">
                    <option value="${filter.id}" ${command.filterId == filter.id?'selected':''} data-amountContacts="${filter.amountOfContacts}">${filter.name}</option>
                </g:each>
                <g:if test="${anonymousFilter}">
                    <option value="${anonymousFilter.id}" ${command.filterId == anonymousFilter.id?'selected':''} data-amountContacts="${anonymousFilter.amountOfContacts}" data-anononymus="true">${anonymousFilter.name}</option>
                </g:if>
                %{--<g:if test="${totalContacts}">--}%
                    <option value="-2" data-amountContacts="-"><g:message code="tools.massMailing.fields.filter.to.createNew"/></option>
                %{--</g:if>--}%
            </select>
        </div>
        <div class="col-sm-4">
            <g:link mapping="politicianContactFilterData" role="button" elementId="filterContacts" title="${g.message(code:'tools.contact.filter.conditions.open')}">
                <span class="fa fa-filter fa-lg"></span>
                <span class="sr-only"><g:message code="tools.massMailing.fields.filter.button"/></span>
            </g:link>
            <g:link mapping="politicianContactsSearch" elementId="infoToContacts">
                <span class="amountRecipients"></span>
                <g:message code="tools.massMailing.fields.filter.recipients"/>
                %{--<span class="fa fa-filter fa-lg"></span>--}%
            </g:link>
            <g:if test="${!hideSendTestButton}">
                <g:link mapping="politicianMassMailingSendTest" absolute="true" class="btn ${hightLigthTestButtons?'btn-blue':'btn-grey'} pull-right" elementId="sendTest" title="${g.message(code:'tools.massMailing.sendTest')}">
                    <span class="fa fa-envelope"></span>
                </g:link>
            </g:if>
        </div>
    </fieldset>

    <div id="newFilterContainer">
        <g:render template="/contacts/filter/listFilterFieldSet" model="[filters:filters,anonymousFilter:anonymousFilter]"/>
    </div>
</g:else>