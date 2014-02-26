
<%@ page import="kuorum.law.Law" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'law.label', default: 'Law')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-law" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-law" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
					
						<g:sortableColumn property="dateCreated" title="${message(code: 'law.dateCreated.label', default: 'Date Created')}" />
					
						<g:sortableColumn property="description" title="${message(code: 'law.description.label', default: 'Description')}" />
					
						<g:sortableColumn property="hashtag" title="${message(code: 'law.hashtag.label', default: 'Hashtag')}" />
					
						<g:sortableColumn property="introduction" title="${message(code: 'law.introduction.label', default: 'Introduction')}" />
					
						<g:sortableColumn property="open" title="${message(code: 'law.open.label', default: 'Open')}" />
					
						<g:sortableColumn property="realName" title="${message(code: 'law.realName.label', default: 'Real Name')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${lawInstanceList}" status="i" var="lawInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${lawInstance.id}">${fieldValue(bean: lawInstance, field: "dateCreated")}</g:link></td>
					
						<td>${fieldValue(bean: lawInstance, field: "description")}</td>
					
						<td>${fieldValue(bean: lawInstance, field: "hashtag")}</td>
					
						<td>${fieldValue(bean: lawInstance, field: "introduction")}</td>
					
						<td><g:formatBoolean boolean="${lawInstance.open}" /></td>
					
						<td>${fieldValue(bean: lawInstance, field: "realName")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${lawInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>
