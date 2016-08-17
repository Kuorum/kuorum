<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.contacts.import"/></title>
    <meta name="layout" content="paymentPlainLayout">
    <!-- Schema.org markup for Google+ -->
    <meta itemprop="name" content="${g.message(code:"kuorum.name")}">
    <meta itemprop="description" content="${g.message(code:"layout.head.meta.description")}">
    <meta itemprop="image" content="${resource(dir: 'images', file:'landingSearch-rrss.png', absolute:true)}" />
    <meta itemprop="image" content="${resource(dir: 'images', file: 'logo@2x.png')}" />
</head>

<content tag="mainContent">
    <ol class="breadcrumb">
        <li><g:link mapping="politicianContacts"> <g:message code="tools.contact.title"/> </g:link></li>
        <li class="active"><g:message code="tools.contact.import.title"/></li>
    </ol>


    <div class="container-fluid box-ppal import-contacts csv">
        <g:form mapping="politicianContactImportCSVSave" name="importContacts-02">
            <h1>${fileName}</h1>
            <p><g:message code="tools.contact.import.firstRowAdvise"/></p>
            <div class="table-responsive">
                <g:set var="line" value="${lines.next()}"/>
                <table class="table table-hover table-bordered csv">
                    <thead>
                    <tr>
                        <th></th>
                        <g:each in="${line.values}" var="val" status="i">
                            <th class="${emptyColumns.contains(i)?'hide':''}">
                                <select class="form-control" name="columnOption">
                                    <option value=""><g:message code="tools.contact.import.table.columnOption.notImported"/> </option>
                                    <option value="name"><g:message code="tools.contact.import.table.columnOption.name"/> </option>
                                    <option value="email"><g:message code="tools.contact.import.table.columnOption.email"/> </option>
                                    <option value="tag"><g:message code="tools.contact.import.table.columnOption.tag"/> </option>
                                </select>
                            </th>
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
                            <g:each in="${line.values}" var="columnValue" status="i">
                                    <td class="${emptyColumns.contains(i)?'hide':''}">${columnValue}</td>
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
            </div>

            <label for="tagsField"><g:message code="tools.contact.import.addTags"/> </label>
            <input name="tags" id="tagsField" type="text" value="" data-urlTags="${g.createLink(mapping:'politicianContactTagsAjax')}">

            <input type="submit" value="${g.message(code: 'tools.contact.import.start')}" class="btn btn-blue inverted btn-lg">
        </g:form>
    </div>

</content>