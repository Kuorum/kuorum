<%@ page import="kuorum.post.Post" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'post.label', default: 'Post')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>
<a href="#show-post" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                           default="Skip to content&hellip;"/></a>

<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]"/></g:link></li>
        <li><g:link class="create" action="create"><g:message code="default.new.label"
                                                              args="[entityName]"/></g:link></li>
    </ul>
</div>

<div id="show-post" class="content scaffold-show" role="main">
    <h1><g:message code="default.show.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <ol class="property-list post">

        <g:if test="${postInstance?.dateCreated}">
            <li class="fieldcontain">
                <span id="dateCreated-label" class="property-label"><g:message code="post.dateCreated.label"
                                                                               default="Date Created"/></span>

                <span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate
                        date="${postInstance?.dateCreated}"/></span>

            </li>
        </g:if>

        <g:if test="${postInstance?.defender}">
            <li class="fieldcontain">
                <span id="defender-label" class="property-label"><g:message code="post.defender.label"
                                                                            default="Defender"/></span>

                <span class="property-value" aria-labelledby="defender-label"><g:link controller="kuorumUser"
                                                                                      action="show"
                                                                                      id="${postInstance?.defender?.id}">${postInstance?.defender?.encodeAsHTML()}</g:link></span>

            </li>
        </g:if>

        <g:if test="${postInstance?.firstCluck}">
            <li class="fieldcontain">
                <span id="firstCluck-label" class="property-label"><g:message code="post.firstCluck.label"
                                                                              default="First Cluck"/></span>

                <span class="property-value" aria-labelledby="firstCluck-label"><g:link controller="cluck" action="show"
                                                                                        id="${postInstance?.firstCluck?.id}">${postInstance?.firstCluck?.encodeAsHTML()}</g:link></span>

            </li>
        </g:if>

        <g:if test="${postInstance?.law}">
            <li class="fieldcontain">
                <span id="law-label" class="property-label"><g:message code="post.law.label" default="Law"/></span>

                <span class="property-value" aria-labelledby="law-label">
                    <g:link controller="law" action="show"
                                                                                 id="${postInstance?.law?.id}">${postInstance?.law?.encodeAsHTML()}</g:link></span>

            </li>
        </g:if>

        <g:if test="${postInstance?.numClucks}">
            <li class="fieldcontain">
                <span id="numClucks-label" class="property-label"><g:message code="post.numClucks.label"
                                                                             default="Num Clucks"/></span>

                <span class="property-value" aria-labelledby="numClucks-label"><g:fieldValue bean="${postInstance}"
                                                                                             field="numClucks"/></span>

            </li>
        </g:if>

        <g:if test="${postInstance?.numVotes}">
            <li class="fieldcontain">
                <span id="numVotes-label" class="property-label"><g:message code="post.numVotes.label"
                                                                            default="Num Votes"/></span>

                <span class="property-value" aria-labelledby="numVotes-label"><g:fieldValue bean="${postInstance}"
                                                                                            field="numVotes"/></span>

            </li>
        </g:if>
        <g:if test="${postInstance?.owner}">
            <li class="fieldcontain">
                <span id="owner-label" class="property-label"><g:message code="post.owner.label"
                                                                         default="Owner"/></span>

                <span class="property-value" aria-labelledby="owner-label"><g:link controller="kuorumUser" action="show"
                                                                                   id="${postInstance?.owner?.id}">${postInstance?.owner?.name.encodeAsHTML()}</g:link></span>

            </li>
        </g:if>

        <g:if test="${postInstance?.photo}">
            <li class="fieldcontain">
                <span id="photo-label" class="property-label"><g:message code="post.photo.label"
                                                                         default="Photo"/></span>

                <span class="property-value" aria-labelledby="photo-label"><g:fieldValue bean="${postInstance}"
                                                                                         field="photo"/></span>

            </li>
        </g:if>

        <g:if test="${postInstance?.postType}">
            <li class="fieldcontain">
                <span id="postType-label" class="property-label"><g:message code="post.postType.label"
                                                                            default="Post Type"/></span>

                <span class="property-value" aria-labelledby="postType-label"><g:fieldValue bean="${postInstance}"
                                                                                            field="postType"/></span>

            </li>
        </g:if>

        <g:if test="${postInstance?.published}">
            <li class="fieldcontain">
                <span id="published-label" class="property-label"><g:message code="post.published.label"
                                                                             default="Published"/></span>

                <span class="property-value" aria-labelledby="published-label"><g:formatBoolean
                        boolean="${postInstance?.published}"/></span>

            </li>
        </g:if>

        <g:if test="${postInstance?.text}">
            <li class="fieldcontain">
                <span id="text-label" class="property-label"><g:message code="post.text.label" default="Text"/></span>

                <span class="property-value" aria-labelledby="text-label"><g:fieldValue bean="${postInstance}"
                                                                                        field="text"/></span>

            </li>
        </g:if>

        <g:if test="${postInstance?.title}">
            <li class="fieldcontain">
                <span id="title-label" class="property-label"><g:message code="post.title.label"
                                                                         default="Title"/></span>

                <span class="property-value" aria-labelledby="title-label"><g:fieldValue bean="${postInstance}"
                                                                                         field="title"/></span>

            </li>
        </g:if>

        <g:if test="${postInstance?.victory}">
            <li class="fieldcontain">
                <span id="victory-label" class="property-label"><g:message code="post.victory.label"
                                                                           default="Victory"/></span>

                <span class="property-value" aria-labelledby="victory-label"><g:formatBoolean
                        boolean="${postInstance?.victory}"/></span>

            </li>
        </g:if>

    </ol>
    <g:form url="[resource: postInstance, action: 'delete']" method="DELETE">
        <fieldset class="buttons">
            <g:link mapping="postEdit" params="[postId:postInstance.id, postTypeUrl:postInstance.postType.urlText, hashtag:postInstance.law.hashtag.decodeHashtag()]">
                <g:message code="default.button.edit.label" default="Edit"/>
            </g:link>
            <g:actionSubmit class="delete" action="delete"
                            value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                            onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
        </fieldset>
    </g:form>
</div>
</body>
</html>
