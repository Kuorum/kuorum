<g:set var="line" value="${lines.next()}"/>
<table class="table table-hover table-bordered csv">
    <thead>
    <tr>
        <th></th>
        <th>
            <select class="form-control" name="columnOption">
                <option value=""><g:message code="tools.contact.import.table.columnOption.notImported"/> </option>
                <option value="name"><g:message code="tools.contact.import.table.columnOption.name"/> </option>
                <option value="email" selected><g:message code="tools.contact.import.table.columnOption.email"/> </option>
                <option value="tag"><g:message code="tools.contact.import.table.columnOption.tag"/> </option>
            </select>
            <input type="hidden" name="realPos" value="${emailPos}"/>
        </th>
        <g:each in="${line.values}" var="val" status="i">
            <g:if test="${i != emailPos}">
                <th class="${emptyColumns.contains(i)?'hide':''}">
                    <select class="form-control" name="columnOption">
                        <option value=""><g:message code="tools.contact.import.table.columnOption.notImported"/> </option>
                        <option value="name" ${i == namePos?'selected':''}><g:message code="tools.contact.import.table.columnOption.name"/> </option>
                        <option value="email" ${i == emailPos?'selected':''}><g:message code="tools.contact.import.table.columnOption.email"/> </option>
                        <option value="surname" ${i == surnamePos?'selected':''}><g:message code="tools.contact.import.table.columnOption.surname"/> </option>
                        <option value="tag"><g:message code="tools.contact.import.table.columnOption.tag"/> </option>
                    </select>
                    <input type="hidden" name="realPos" value="${i}"/>
                </th>
            </g:if>
        </g:each>
    </tr>
    </thead>
    <tbody>
    <g:set var="exampleLinesShowed" value="${0}"/>
    <g:while test="${line}">
        <tr>
            <th scope="row">
                <g:message code="tools.contact.import.table.row" args="[exampleLinesShowed]"/>
                <span class="notImport"><input type="checkbox" value="${exampleLinesShowed+1}" name="notImport"> <g:message code="tools.contact.import.table.row.notImport"/></span>
            </th>
            <td>${line.values[emailPos]}</td>
            <g:each in="${line.values}" var="columnValue" status="i">
                <g:if test="${i != emailPos}">
                    <td class="${emptyColumns.contains(i)?'hide':''}">${columnValue}</td>
                </g:if>
            </g:each>
        </tr>
        <g:if test="${lines.hasNext() && exampleLinesShowed < 6}">
            <g:set var="line" value="${lines.next()}"/>
            <%exampleLinesShowed++%>
        </g:if>
        <g:else>
            <g:set var="line" value=""/>
        </g:else>
    </g:while>

    </tbody>
</table>
