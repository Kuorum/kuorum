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
    <div class="container-fluid box-ppal import-contacts">
        <g:form mapping="politicianContactImportCSV" name="importContacts-01"  enctype="multipart/form-data" useToken="true">
            <label class="sr-only">Select CSV File</label>
            <input name="fileContacts" type="file" class="filestyle" data-icon="false" data-buttonText="Select CSV file" data-buttonName="btn-blue inverted" data-buttonBefore="true" data-placeholder="No file was selected yet">
            <div class="form-group text-center">
                <input type="submit" value="Upload file" class="btn inverted btn-lg">
            </div>
        </g:form>
        <p>It was never so easy! Go to your email provider (Outlook, Gmail, Yahoo!, etc.), look for the option “Export  contacts in CSV format”, download the file and upload it here. We will help you manage your communities and increase their engagement. <a href="#">Contact us</a> if you find any problem.</p>
    </div>
    <div class="container-fluid box-ppal import-contacts-videos">
        <div class="row">
            <article class="col-md-5">
                <h1>How to export contacts from Gmail</h1>
                <iframe src="https://www.youtube.com/embed/H-17hUKyAQE" frameborder="0" allowfullscreen class="youtube"></iframe>
            </article>
            <article class="col-md-5 col-md-offset-2">
                <h1>How to export contacts from Outlook</h1>
                <iframe src="https://www.youtube.com/embed/ElQEINBkyTI" frameborder="0" allowfullscreen class="youtube"></iframe>
            </article>
        </div>
    </div>
</content>