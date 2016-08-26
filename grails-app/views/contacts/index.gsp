<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.contacts"/></title>
    <meta name="layout" content="paymentPlainLayout">
    <!-- Schema.org markup for Google+ -->
    <meta itemprop="name" content="${g.message(code:"kuorum.name")}">
    <meta itemprop="description" content="${g.message(code:"layout.head.meta.description")}">
    <meta itemprop="image" content="${resource(dir: 'images', file:'landingSearch-rrss.png', absolute:true)}" />
    <meta itemprop="image" content="${resource(dir: 'images', file: 'logo@2x.png')}" />
</head>

<content tag="mainContent">
    <ol class="breadcrumb">
        <li class="active"><g:message code="tools.contact.title"/></li>
    </ol>

    <div>

        <!-- FILTRO Y BUSCADOR DE CONTACTOS -->
        <div class="box-ppal">
            <form class="form-horizontal">
                <fieldset class="form-group" id="boxfiltersContacts">
                    <div class="col-sm-3">
                        <label for="filterContactsOptions" class="sr-only">Filter Contacts:</label>
                        <select name="filterContactsOptions" class="form-control input-lg" id="filterContactsOptions">
                            <option value="saved" name="saved" id="saved">- Saved filters -</option>
                            <option value="option1" name="option1" id="option1">Option 1</option>
                            <option value="option2" name="option2" id="option2">Option 2</option>
                            <option value="option3" name="option3" id="option3">Option 3</option>
                            <option value="new" id="newFilter">Create new filter</option>
                        </select>
                    </div>
                    <div class="col-sm-3">
                        <!-- para activarlo añadir clase "on" -->
                        <a href="#" role="buttom" id="filContacts">
                            <span class="fa fa-filter fa-lg"></span>
                            <span class="sr-only">Filter contacts</span>
                        </a>
                        <span id="infoContacts">
                            3.240 contacts <span class="fa fa-filter fa-lg"></span><span class="fa fa-search fa-lg"></span>
                        </span>
                    </div>
                    <div class="col-sm-3 col-md-2 col-lg-3">
                        <div class="searchContainer">
                            <input type="text" class="form-control searchContacts" name="searchContacts" id="searchContacts">
                        </div>
                    </div>
                    <div class="col-sm-3 col-md-4 col-lg-3">
                        <ul>
                            <li><a href="#" class="btn btn-blue inverted"><span class="fa fa-plus fa-lg"></span> New contact</a></li>
                            <li>
                                <a href="#" role="button" id="openContactsOptions" class="btn btn-blue inverted dropdown-toggle" data-toggle="dropdown">Import <span class="fa fa-caret-down fa-lg"></span></a>
                                <ul id="contactsOptions" class="dropdown-menu dropdown-menu-right" aria-labelledby="openContactsOptions" role="menu">
                                    <li><g:link mapping="politicianContactImport">CSV file</g:link></li>
                                    <li><a href="#">Gmail</a></li>
                                    <li><a href="#">Yahoo!</a></li>
                                    <li><a href="#">Outlook</a></li>
                                </ul>
                            </li>
                        </ul>
                        <div>
                </fieldset>
            </form>
        </div>
        <!-- LISTADO DE CONTACTOS -->
        <div class="box-ppal list-contacts">
            <div id="contactsOrderOptions" class="box-order-options clearfix">
                <span>Order by:</span>
                <ul>
                    <!-- añadir la clase "active asc" o "active desc" al <a> -->
                    <li><a href="#" role="button" class="sort" data-sort="timestamp">Name</a></li>
                    <li><a href="#" role="button" class="sort" data-sort="title">Email</a></li>
                    <li><a href="#" role="button" class="sort" data-sort="recip-number">Followers</a></li>
                    <li><a href="#" role="button" class="sort" data-sort="open-number">Engagement</a></li>
                </ul>
                <div class="pag-list-contacts">
                    <ul>
                        <li><a href="#" role="button"><span class="fa fa-caret-left"></span><span class="sr-only">Anteriores</span></a></li>
                        <li><a href="#" role="button"><span class="fa fa-caret-right"></span><span class="sr-only">Posteriores</span></a></li>
                    </ul>
                    <span class="counterList">101 - 200 of 347,902</span>
                </div>
            </div>
            <ul id="contactsList" class="list">
                <g:each in="${contacts.data}" var="contact">
                    <g:render template="liContact" model="[contact:contact]"/>
                </g:each>
            </ul>

            <div class="pag-list-contacts clearfix">
                <ul>
                    <li><a href="#" role="button"><span class="fa fa-caret-left"></span><span class="sr-only">Anteriores</span></a></li>
                    <li><a href="#" role="button"><span class="fa fa-caret-right"></span><span class="sr-only">Posteriores</span></a></li>
                </ul>
                <span class="counterList">101 - 200 of 347,902</span>
            </div>
        </div>

        <h2>Contacts: ${contacts.total}</h2>
        <ul>

        </ul>
    </div>
</content>