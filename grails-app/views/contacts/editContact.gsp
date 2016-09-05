<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.contacts.edit" args="[contact.name]"/></title>
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
        <li class="active">${contact.name}</li>
    </ol>
    <div class="box-ppal edit-contact-header clearfix" itemscope itemtype="http://schema.org/Person">
        <div>
            <span class="user-img"><img src="${image.contactImgSrc(contact:contact)}" alt="${contact.name}" itemprop="image"></span>
            <h3 class="title"><a href="#">${contact.name}</a></h3>
            <p class="email"><span class="fa fa-envelope-o"></span> <a href="#">${contact.email}</a></p>
            <p class="followers">
                <span class="fa fa-user"></span><g:message code="tools.contact.list.contact.followers" args="[contact.numFollowers]"/>
            <!-- opción de editar si procede -->
                <a href="#"><span class="fa fa-external-link"></span><span class="sr-only">Editar</span></a>
            </p>
            <ul class="social-links">
                <li><a href="#"><span class="fa fa-facebook-square"></span><span class="sr-only">Facebook</span></a></li>
                <li><a href="#"><span class="fa fa-twitter-square"></span><span class="sr-only">Twitter</span></a></li>
                <li><a href="#"><span class="fa fa-google-plus-square"></span><span class="sr-only">Google+</span></a></li>
                <li><a href="#"><span class="fa fa-linkedin-square"></span><span class="sr-only">Linkedin</span></a></li>
            </ul>
            <ul class="btns">
                <!-- ejemplo deshabilitado -->
                <g:if test="${contactUser}">
                    <li><userUtil:followButton user="${contactUser}" /> </li>
                </g:if>
                <g:else>
                    <li><a href="#" class="btn btn-blue inverted disabled"><span class="fa fa-plus"></span> Follow</a></li>
                </g:else>

                <li><a href="#" class="btn btn-blue inverted disabled"><span class="fa fa-envelope-0"></span> Contact</a></li>
            </ul>
        </div>
        <div class="container-lists">
            <g:render template="/contacts/inputs/editContactTags" model="[contact:contact]"/>
            <g:render template="/contacts/inputs/showContactEngagement" model="[contact:contact]"/>
        </div>
    </div>
    <div class="box-ppal edit-contact clearfix">
        <ul class="nav nav-tabs simple" data-tabs="tabs">
            <li role="presentation" class="active"><a href="#details" data-toggle="tab"><g:message code="tools.contact.edit.tabs.basic"/></a></li>
            <li role="presentation"><a href="#activity" data-toggle="tab"><g:message code="tools.contact.edit.tabs.activity"/></a></li>
            <li role="presentation"><a href="#socialNetwork" data-toggle="tab"><g:message code="tools.contact.edit.tabs.socialNetworks"/></a></li>
            <li role="presentation"><a href="#notes" data-toggle="tab"><g:message code="tools.contact.edit.tabs.notes"/></a></li>
        </ul>
        <div id="tabs-edit-contact" class="tab-content">
            <div class="tab-pane active" id="details">
                <h4 class="sr-only"><g:message code="tools.contact.edit.tabs.basic"/></h4>
                <formUtil:validateForm bean="${command}" form="editBasicContactForm"/>
                <g:form mapping="politicianContactEdit" params="[contactId:contact.id]" name="editBasicContactForm">
                    <g:render template="inputs/basicContactInputs" model="[command:command, contact: contact]" />
                </g:form>
            </div>
            <div class="tab-pane" id="activity">
                <h4 class="sr-only"><g:message code="tools.contact.edit.tabs.activity"/></h4>
                <ul class="activity">
                    <li class="posts">
                        <g:message code="tools.contact.edit.tabs.activity.campaignSent" args="[contact.stats.numMails]"/>
                    </li>
                    <li class="posts">
                        <g:message code="tools.contact.edit.tabs.activity.openRate" args="[contact.stats.opens]"/>
                    </li>
                    <li class="posts">
                        <g:message code="tools.contact.edit.tabs.activity.clickRate" args="[contact.stats.clicks]"/>
                    </li>
                </ul>
                <ul class="activity des-engagement clearfix">
                    <li class="posts">
                        <span>inactive</span>
                        <ul><li>Open rate &#60; 10%</li></ul>
                    </li>
                    <li class="posts">
                        <span>reader</span>
                        <ul><li>Open rate &#62; 10%</li><li> Click rate &#60; 3%</li></ul>
                    </li>
                    <li class="posts">
                        <span>supporter</span>
                        <ul><li>Open rate &#62; 10%</li><li>Click rate &#60; 3%</li></ul>
                    </li>
                    <li class="posts">
                        <span>broadcaster</span>
                        <ul><li>Open rate &#62; 70%</li></ul>
                    </li>
                </ul>
            </div>
            <div class="tab-pane" id="socialNetwork">
                <h4 class="sr-only"><g:message code="tools.contact.edit.tabs.socialNetworks"/></h4>
                <form>
                    <div class="row">
                        <div class="form-group col-md-4">
                            <label for="socialFb">Facebook</label>
                            <div class="input-group">
                                <div class="input-group-addon"><span class="fa fa-facebook"></span></div>
                                <input type="text" value="${contact?.social?.facebook}" class="form-control" id="socialFb" disabled>
                            </div>
                        </div>
                        <div class="form-group col-md-4">
                            <label for="socialTw">Twitter</label>
                            <div class="input-group">
                                <div class="input-group-addon"><span class="fa fa-twitter"></span></div>
                                <input type="text" value="${contact?.social?.twitter}" class="form-control" id="socialTw" disabled>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-md-4">
                            <label for="socialGp">Google+</label>
                            <div class="input-group">
                                <div class="input-group-addon"><span class="fa fa-google-plus"></span></div>
                                <input type="text" value="${contact?.social?.googlePlus}" class="form-control" id="socialGp" disabled>
                            </div>
                        </div>
                        <div class="form-group col-md-4">
                            <label for="socialLk">Linkedin</label>
                            <div class="input-group">
                                <div class="input-group-addon"><span class="fa fa-linkedin"></span></div>
                                <input type="text" value="${contact?.social?.linkedIn}" class="form-control" id="socialLk" disabled>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-4 col-md-offset-8">
                            %{--<input type="submit" value="Save" class="btn btn-blue inverted" disabled>--}%
                        </div>
                    </div>
                </form>
            </div>
            <div class="tab-pane" id="notes">
                <h4 class="sr-only"><g:message code="tools.contact.edit.tabs.notes"/></h4>
                <g:form mapping="politicianContactEditUpdateNote" params="[contactId:contact.id]" name="updateContactNotes">
                    <div class="row">
                        <div class="form-group col-md-8">
                            <label for="notesContact"><g:message code="tools.contact.edit.tabs.notes"/></label>
                            <textarea id="notesContact" class="form-control" name="notes">${contact.notes}</textarea>
                        </div>
                        <div class="form-group col-md-2 col-md-offset-2">
                            <input type="submit" value="Save" class="btn btn-blue inverted">
                        </div>
                    </div>
                </g:form>
            </div>
        </div>
    </div>
</content>