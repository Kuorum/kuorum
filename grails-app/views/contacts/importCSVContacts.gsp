<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.contacts.import"/></title>
    <meta name="layout" content="basicPlainLayout">
    <!-- Schema.org markup for Google+ -->
</head>

<content tag="mainContent">
    <ol class="breadcrumb">
        <li><g:link mapping="politicianContacts"> <g:message code="tools.contact.title"/> </g:link></li>
        <li><g:link mapping="politicianContactImport"> <g:message code="tools.contact.import.title"/> </g:link></li>
        <li class="active"><g:message code="tools.contact.import.options.csv"/></li>
    </ol>
    <div class="container-fluid box-ppal import-contacts">
        <g:form mapping="politicianContactImportCSV" name="importContacts-01"  enctype="multipart/form-data" useToken="true">
            <label class="sr-only"><g:message code="tools.contact.import.csv.selectFile"/></label>
            <input name="fileContacts" type="file" class="filestyle" data-icon="false" data-buttonText="${message(code: 'tools.contact.import.csv.selectFile')}" data-buttonName="btn-blue inverted" data-buttonBefore="true" data-placeholder="${message(code: 'tools.contact.import.csv.selectFile.noSelection')}">
            <div class="form-group text-center">
                <label class="checkbox-inline">
                    <input type="checkbox" name='conditions' value='true'/>
                    <span class="check-box-icon"></span>
                    <span class="label-checkbox"><g:message code="tools.contact.import.csv.selectFile.conditions"/> </span>
                </label>
            </div>
            <div class="form-group text-center">
                <input type="submit" value="${message(code: 'tools.contact.import.csv.selectFile.upload')}" class="btn inverted btn-lg">
            </div>
        </g:form>
        <p><g:message code="tools.contact.import.csv.text" args="['mailto:info@kuorum.org']"/> </p>
    </div>
    <div class="container-fluid box-ppal import-contacts-videos">
        <div class="row">
            <article class="col-md-5">
                <h1><g:message code="tools.contact.import.csv.export.gmail"/> </h1>
                <iframe src="https://www.youtube.com/embed/iLE9p4JO3_o" frameborder="0" allowfullscreen class="youtube"></iframe>
            </article>
            <article class="col-md-5 col-md-offset-2">
                <h1><g:message code="tools.contact.import.csv.export.outlook"/></h1>
                <iframe src="https://www.youtube.com/embed/c4ngLtafcaU" frameborder="0" allowfullscreen class="youtube"></iframe>
            </article>
        </div>
    </div>
</content>