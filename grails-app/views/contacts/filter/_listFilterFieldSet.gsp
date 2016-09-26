<%@ page import="org.kuorum.rest.model.contact.filter.ConditionRDTO; org.kuorum.rest.model.contact.filter.FilterRDTO" %>
<r:require modules="filterContacts"/>
%{--<g:each in="${filters}" var="filter">--}%
    %{--<g:render template="/contacts/filter/filterFieldSet" model="[filter:filter]"/>--}%
%{--</g:each>--}%
%{--<g:render template="/contacts/filter/filterFieldSet" model="[filter:new org.kuorum.rest.model.contact.filter.ExtendedFilterRSDTO([id:-2, filterConditions:[new org.kuorum.rest.model.contact.filter.ConditionRDTO()]])]"/>--}%

<div id="filterData">

</div>

<!-- MODAL CONTACT -->
<div class="modal fade in" id="filtersInfo" tabindex="-1" role="dialog" aria-labelledby="filtersRecipients" aria-hidden="true">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                    <span aria-hidden="true" class="fa fa-times-circle-o fa"></span><span class="sr-only">Cerrar</span>
                </button>
                <h4 id="filtersRecipients"><g:message code="tools.contact.filter.quickList.title"/></h4>
            </div>
            <div class="modal-body">
                <div class="scroll-container">
                    <table class="table-bordered">
                        <thead>
                        <tr>
                            <th><g:message code="org.kuorum.rest.model.contact.filter.ConditionFieldTypeRDTO.NAME"/> </th>
                            <th><g:message code="org.kuorum.rest.model.contact.filter.ConditionFieldTypeRDTO.EMAIL"/></th>
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