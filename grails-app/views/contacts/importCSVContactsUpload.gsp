<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.contacts.import"/></title>
    <meta name="layout" content="paymentPlainLayout">
    <!-- Schema.org markup for Google+ -->
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
                ${raw(table)}
            </div>

            <label for="tagsField"><g:message code="tools.contact.import.addTags"/> </label>
            <input name="tags" class="input-lg tagsField" id="tagsField" type="text" value="" data-urlTags="${g.createLink(mapping:'politicianContactTagsAjax')}">

            <input type="submit" value="${g.message(code: 'tools.contact.import.start')}" class="btn btn-blue inverted btn-lg">
        </g:form>
    </div>

</content>