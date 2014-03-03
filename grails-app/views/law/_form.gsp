<%@ page import="kuorum.law.Law" %>



<div class="fieldcontain ${hasErrors(bean: lawInstance, field: 'description', 'error')} ">
	<label for="description">
		<g:message code="law.description.label" default="Description" />
		
	</label>
	<g:textField name="description" value="${lawInstance?.description}" />
</div>

<div class="fieldcontain ${hasErrors(bean: lawInstance, field: 'hashtag', 'error')} ">
	<label for="hashtag">
		<g:message code="law.hashtag.label" default="Hashtag" />
		
	</label>
	<g:textField name="hashtag" value="${lawInstance?.hashtag}" />
</div>

<div class="fieldcontain ${hasErrors(bean: lawInstance, field: 'introduction', 'error')} ">
	<label for="introduction">
		<g:message code="law.introduction.label" default="Introduction" />
		
	</label>
	<g:textField name="introduction" value="${lawInstance?.introduction}" />
</div>


<div class="fieldcontain ${hasErrors(bean: lawInstance, field: 'realName', 'error')} ">
	<label for="realName">
		<g:message code="law.realName.label" default="Real Name" />
		
	</label>
	<g:textField name="realName" value="${lawInstance?.realName}" />
</div>

<div class="fieldcontain ${hasErrors(bean: lawInstance, field: 'region', 'error')} ">
    <label for="realName">
        <g:message code="law.realName.label" default="Real Name" />

    </label>
    <g:select name="region" from="${regions}" optionKey="id" optionValue="name"/>
</div>


<div class="fieldcontain ${hasErrors(bean: lawInstance, field: 'shortName', 'error')} ">
	<label for="shortName">
		<g:message code="law.shortName.label" default="Short Name" />
		
	</label>
	<g:textField name="shortName" value="${lawInstance?.shortName}" />
</div>

