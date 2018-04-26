<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.contacts.new"/></title>
    <meta name="layout" content="paymentPlainLayout">
    <!-- Schema.org markup for Google+ -->
</head>

<content tag="mainContent">
    <ol class="breadcrumb">
        <li><g:link mapping="politicianContacts"><g:message code="page.title.contacts"/></g:link></li>
        <li class="active"><g:message code="page.title.contacts.new"/></li>
    </ol>
    <div class="box-ppal edit-contact clearfix">
        <formUtil:validateForm bean="${command}" form="newContact"/>
        <g:form method="POST" mapping="politicianContactNew"  name="newContact" role="form" class="clearfix">
            <fieldset>
                <div class="row">
                    <div class="form-group col-md-4">
                        <formUtil:input command="${command}" field="name" showLabel="true"/>
                    </div>
                    <div class="form-group col-md-4">
                        <formUtil:input command="${command}" field="surname" showLabel="true"/>
                    </div>
                </div>
                <div class="row">
                    <div class="form-group col-md-4">
                        <formUtil:input command="${command}" field="email" showLabel="true"/>
                    </div>
                </div>
            </fieldset>
            <fieldset>
                <input type="submit" value="${g.message(code: 'tools.contact.new.save')}" class="btn btn-blue inverted">
            </fieldset>
        </g:form>
    </div>
</content>