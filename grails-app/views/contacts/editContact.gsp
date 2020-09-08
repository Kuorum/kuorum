<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <g:set var="contactName" value="${contact.name} ${contact.surname?:''}"/>
    <title><g:message code="page.title.contacts.edit" args="[contactName]"/></title>
    <meta name="layout" content="basicPlainLayout">
    <!-- Schema.org markup for Google+ -->
    <r:require modules="contacts"/>
</head>

<content tag="mainContent">
    <ol class="breadcrumb">
        <li><g:link mapping="politicianContacts"> <g:message code="tools.contact.title"/> </g:link></li>
        <li class="active">${contactName} </li>
    </ol>
    <div class="box-ppal edit-contact-header clearfix" itemscope itemtype="http://schema.org/Person">
        <div>
            <span class="user-img"><img src="${image.contactImgSrc(contact:contact)}" alt="${contactName} " itemprop="image"></span>
            <h3 class="title">
                <g:if test="${contactUser}">
                    <g:link mapping="userShow" params="${contactUser.encodeAsLinkProperties()}">${contactName}</g:link>
                    <g:link mapping="userShow" params="${contactUser.encodeAsLinkProperties()}" target="_blank"><span class="fal fa-desktop fa-sm"></span><span class="sr-only">Profile</span></g:link>
                </g:if>
                <g:else>
                    ${contactName}
                </g:else>
            </h3>
            <p class="email">
                <contactUtil:printContactMail contact="${contact}"/>
            </p>
            <p class="followers">
                <span class="fal fa-users"></span><g:message code="tools.contact.list.contact.followers" args="[contact.numFollowers]"/>
            </p>
            <ul class="social-links">
                <g:if test="${contact?.social?.facebook}">
                    <li><a href="${contact.social.facebook}" target="_blank"><span class="fab fa-facebook"></span><span class="sr-only">Facebook</span></a></li>
                </g:if>
                <g:if test="${contact?.social?.twitter}">
                    <li><a href="${contact.social.twitter}" target="_blank"><span class="fab fa-twitter"></span><span class="sr-only">Twitter</span></a></li>
                </g:if>
                <g:if test="${contact?.social?.linkedIn}">
                    <li><a href="${contact.social.linkedIn}" target="_blank"><span class="fab fa-linkedin"></span><span class="sr-only">Linkedin</span></a></li>
                </g:if>
            </ul>
            <ul class="btns">
                <!-- ejemplo deshabilitado -->

                <g:if test="${contactUser}">
                    <li><userUtil:followButton user="${contactUser}" /> </li>
                </g:if>
                <g:else>
                    <li><a href="#" class="btn btn-blue inverted disabled"><span class="far fa-plus"></span> Follow</a></li>
                </g:else>
                <li>
                <li>
                    <g:link
                            type="button"
                            class="btn btn-blue inverted"
                            value="Unsubscribe"
                            controller="contacts"
                            data-toggle="tooltip"
                            data-placement="bottom"
                            rel="tooltip"
                            title=""
                            data-original-title="${message(code:'tools.contact.edit.unsubscribe.tooltip')}"
                            mapping="politicianContactUnsubscribe"
                            params="[contactId: contact.id]"
                    > <g:message code="tools.contact.edit.unsubscribe.label"/> </g:link>
                </li>
                %{--<li><a href="#" class="btn btn-blue inverted disabled"><span class="fal fa-envelope"></span> Contact</a></li>--}%
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
            <li role="presentation"><a href="#files" data-toggle="tab"><g:message code="tools.contact.edit.tabs.files"/></a></li>
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
                        <g:message code="tools.contact.edit.tabs.activity.openRate" args="[contactUtil.openRate(contact:contact)]"/>
                    </li>
                    <li class="posts">
                        <g:message code="tools.contact.edit.tabs.activity.clickRate" args="[contactUtil.clickRate(contact:contact)]"/>
                    </li>
                </ul>
                <ul class="activity des-engagement clearfix">
                    <li class="posts">
                        <span><g:message code="org.kuorum.rest.model.contact.ContactStatusRSDTO.INACTIVE"/> </span>
                        <ul><li><g:message code="tools.contact.edit.tabs.activity.info.openRate.smaller" args="[10]"/></li></ul>
                    </li>
                    <li class="posts">
                        <span><g:message code="org.kuorum.rest.model.contact.ContactStatusRSDTO.READER"/></span>
                        <ul><li><g:message code="tools.contact.edit.tabs.activity.info.openRate.bigger" args="[10]"/></li><li> <g:message code="tools.contact.edit.tabs.activity.info.clickRate.smaller" args="[3]"/> </li></ul>
                    </li>
                    <li class="posts">
                        <span><g:message code="org.kuorum.rest.model.contact.ContactStatusRSDTO.SUPPORTER"/></span>
                        <ul><li><g:message code="tools.contact.edit.tabs.activity.info.openRate.bigger" args="[10]"/></li><li><g:message code="tools.contact.edit.tabs.activity.info.clickRate.bigger" args="[3]"/></li></ul>
                    </li>
                    <li class="posts">
                        <span><g:message code="org.kuorum.rest.model.contact.ContactStatusRSDTO.BROADCASTER"/></span>
                        <ul><li><g:message code="tools.contact.edit.tabs.activity.info.openRate.bigger" args="[70]"/></li></ul>
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
                                <div class="input-group-addon"><span class="fab fa-facebook fa-fw"></span></div>
                                <input type="text" value="${contact?.social?.facebook}" class="form-control" id="socialFb" disabled>
                            </div>
                        </div>
                        <div class="form-group col-md-4">
                            <label for="socialTw">Twitter</label>
                            <div class="input-group">
                                <div class="input-group-addon"><span class="fab fa-twitter fa-fw"></span></div>
                                <input type="text" value="${contact?.social?.twitter}" class="form-control" id="socialTw" disabled>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-md-4">
                            <label for="socialLk">Linkedin</label>
                            <div class="input-group">
                                <div class="input-group-addon"><span class="fab fa-linkedin fa-fw"></span></div>
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
                            <input type="submit" value="${g.message(code:'default.save')}" class="btn btn-blue inverted">
                        </div>
                    </div>
                </g:form>
            </div>
            <div class="tab-pane" id="files">
                <h4 class="sr-only"><g:message code="tools.contact.edit.tabs.files"/></h4>
                <div class="textareaContainer col-sm-8 col-md-7">
                    <formUtil:uploadContactFiles contact="${contact}"/>
                </div>
            </div>
        </div>
    </div>
</content>