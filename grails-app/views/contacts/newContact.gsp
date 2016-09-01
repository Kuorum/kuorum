<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.contacts.new"/></title>
    <meta name="layout" content="paymentPlainLayout">
    <!-- Schema.org markup for Google+ -->
    <meta itemprop="name" content="${g.message(code:"kuorum.name")}">
    <meta itemprop="description" content="${g.message(code:"layout.head.meta.description")}">
    <meta itemprop="image" content="${resource(dir: 'images', file:'landingSearch-rrss.png', absolute:true)}" />
    <meta itemprop="image" content="${resource(dir: 'images', file: 'logo@2x.png')}" />
</head>

<content tag="mainContent">
    <ol class="breadcrumb">
        <li><g:link mapping="politicianContacts"><g:message code="page.title.contacts"/></g:link></li>
        <li class="active"><g:message code="page.title.contacts.new"/></li>
    </ol>
    <div class="box-ppal edit-contact clearfix">
        <form>
            <fieldset>
                <div class="form-group">
                    <label for="name">Name</label>
                    <input type="text" name="name" class="form-control input-lg" id="name" required="" placeholder="Pepito Pérez" aria-required="true">
                </div>
                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="text" name="email" class="form-control input-lg" id="email" required="" placeholder="pepito.perez@gmail.com" aria-required="true">
                </div>
            </fieldset>
            <fieldset>
                <input type="submit" value="Save contact" class="btn btn-blue inverted">
            </fieldset>
        </form>
    </div>
</content>