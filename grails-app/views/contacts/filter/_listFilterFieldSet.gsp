<%@ page import="org.kuorum.rest.model.contact.filter.condition.ConditionFieldTypeRDTO; org.kuorum.rest.model.contact.filter.condition.TextConditionOperatorTypeRDTO; org.kuorum.rest.model.contact.filter.condition.ConditionTextRDTO;  org.kuorum.rest.model.contact.filter.FilterRDTO" %>
<r:require modules="contacts"/>
%{--<g:each in="${filters}" var="filter">--}%
    %{--<g:render template="/contacts/filter/filterFieldSet" model="[filter:filter]"/>--}%
%{--</g:each>--}%

<div id="filterData">
    <g:if test="${anonymousFilter}">
        <g:render template="/contacts/filter/filterFieldSet" model="[filter:anonymousFilter]"/>
    </g:if>
</div>

<!-- MODAL CONTACT -->
<div class="modal fade in" id="filtersInfo" tabindex="-1" role="dialog" aria-labelledby="filtersRecipients" aria-hidden="true">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                    <span aria-hidden="true" class="fal fa-times-circle fa"></span><span class="sr-only">Cerrar</span>
                </button>
                <h4 id="filtersRecipients"><g:message code="tools.contact.filter.quickList.title"/></h4>
            </div>
            <div class="modal-body">
                <div class="scroll-container">
                    <table class="table-bordered">
                        <thead>
                        <tr>
                            <th><g:message code="org.kuorum.rest.model.contact.filter.condition.ConditionFieldTypeRDTO.NAME"/> </th>
                            <th><g:message code="org.kuorum.rest.model.contact.filter.condition.ConditionFieldTypeRDTO.EMAIL"/></th>
                        </tr>
                        </thead>
                        <tbody>
                              %{--BODY--}%
                        </tbody>
                        <tfoot>
                        <tr>
                            <td colspan="2"><g:message code="tools.contact.filter.quickList.footer"/> </td>
                        </tr>
                        </tfoot>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>


<!-- MODAL CONTACT -->
<div class="modal fade in" id="filtersDelete" tabindex="-1" role="dialog" aria-labelledby="filtersDeleteTitle" aria-hidden="true">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                    <span aria-hidden="true" class="fal fa-times-circle fa"></span><span class="sr-only">Cerrar</span>
                </button>
                <h4 id="filtersDeleteTitle"><g:message code="tools.contact.filter.delete.title"/>: '<span class="filter-name">XXX</span>'</h4>
            </div>
            <div class="modal-body">
                <div class="clearfix">
                    ¿Estas seguro que quieres borrar el filtro '<span class="filter-name">XXX</span>' con <span class="filter-ammount">37</span> contactos?
                </div>
                <div class="clearfix">
                    <g:link mapping="politicianContactFilterDelete" class="btn btn-blue inverted" elementId="deleteFilterButton">BORRAR</g:link>
                </div>
            </div>
        </div>
    </div>
</div>