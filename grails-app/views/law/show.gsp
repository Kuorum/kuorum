
<%@ page import="kuorum.law.Law" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'law.label', default: 'Law')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-law" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-law" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list law">
			
				<g:if test="${lawInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="law.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${lawInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${lawInstance?.description}">
				<li class="fieldcontain">
					<span id="description-label" class="property-label"><g:message code="law.description.label" default="Description" /></span>
					
						<span class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${lawInstance}" field="description"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${lawInstance?.hashtag}">
				<li class="fieldcontain">
					<span id="hashtag-label" class="property-label"><g:message code="law.hashtag.label" default="Hashtag" /></span>
					
						<span class="property-value" aria-labelledby="hashtag-label"><g:fieldValue bean="${lawInstance}" field="hashtag"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${lawInstance?.introduction}">
				<li class="fieldcontain">
					<span id="introduction-label" class="property-label"><g:message code="law.introduction.label" default="Introduction" /></span>
					
						<span class="property-value" aria-labelledby="introduction-label"><g:fieldValue bean="${lawInstance}" field="introduction"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${lawInstance?.open}">
				<li class="fieldcontain">
					<span id="open-label" class="property-label"><g:message code="law.open.label" default="Open" /></span>
					
						<span class="property-value" aria-labelledby="open-label"><g:formatBoolean boolean="${lawInstance?.open}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${lawInstance?.realName}">
				<li class="fieldcontain">
					<span id="realName-label" class="property-label"><g:message code="law.realName.label" default="Real Name" /></span>
					
						<span class="property-value" aria-labelledby="realName-label"><g:fieldValue bean="${lawInstance}" field="realName"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${lawInstance?.region}">
				<li class="fieldcontain">
					<span id="region-label" class="property-label"><g:message code="law.region.label" default="Region" /></span>
					
						<span class="property-value" aria-labelledby="region-label"><g:fieldValue bean="${lawInstance}" field="region"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${lawInstance?.shortName}">
				<li class="fieldcontain">
					<span id="shortName-label" class="property-label"><g:message code="law.shortName.label" default="Short Name" /></span>
					
						<span class="property-value" aria-labelledby="shortName-label"><g:fieldValue bean="${lawInstance}" field="shortName"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:lawInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${lawInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
