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
        <li><a href="#">Contacts</a></li>
        <li class="active">Import contacts</li>
    </ol>


    <div class="container-fluid box-ppal import-contacts csv">
        <g:form mapping="politicianContactImportCSVSave" name="importContacts-02">
            <h1>${fileName}</h1>
            <p>The following table shows the first rows of the file you uploaded. Select the field that corresponds to each column and start the import.</p>
            <div class="table-responsive">
                <g:set var="line" value="${lines.next()}"/>
                <table class="table table-hover table-bordered csv">
                    <thead>
                    <tr>
                        <th></th>
                        <g:each in="${line.columns}">
                            <th>
                                <select class="form-control" name="columnOption">
                                    <option value="">-- Not imported --</option>
                                    <option value="name">Name</option>
                                    <option value="email">Mail</option>
                                    <option value="tag">Tag</option>
                                </select>
                            </th>
                        </g:each>
                    </tr>
                    </thead>
                    <tbody>
                    <g:set var="exampleLinesShowed" value="${0}"/>
                    <g:while test="${line}">
                        <tr>
                            <th scope="row"> Row ${exampleLinesShowed} <input type="checkbox" id="row01"/> do not import</th>
                            <g:each in="${line.columns}" var="columnName" status="i">
                                    <td>${line[i]}</td>
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

            <label for="tagsField">Add tags to each person</label>
            <input id="tagsField" type="text" value="tory,journalist,anti-gun" data-urlTags="${g.createLink(mapping:'politicianContactTagsAjax')}">

            <input type="submit" value="Start import" class="btn btn-blue inverted btn-lg">
        </g:form>
    </div>

</content>