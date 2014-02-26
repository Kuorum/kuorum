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

<div class="fieldcontain ${hasErrors(bean: lawInstance, field: 'open', 'error')} ">
	<label for="open">
		<g:message code="law.open.label" default="Open" />
		
	</label>
	<g:checkBox name="open" value="${lawInstance?.open}" />
</div>

<div class="fieldcontain ${hasErrors(bean: lawInstance, field: 'realName', 'error')} ">
	<label for="realName">
		<g:message code="law.realName.label" default="Real Name" />
		
	</label>
	<g:textField name="realName" value="${lawInstance?.realName}" />
</div>
<fieldset class="embedded"><legend><g:message code="law.region.label" default="Region" /></legend>
<div class="fieldcontain ${hasErrors(bean: lawInstance, field: 'region.id', 'error')} ">
	<label for="region.id">
		<g:message code="law.region.id.label" default="Id" />
		
	</label>
	
</div>

<div class="fieldcontain ${hasErrors(bean: lawInstance, field: 'region.iso3166_2', 'error')} ">
	<label for="region.iso3166_2">
		<g:message code="law.region.iso3166_2.label" default="Iso31662" />
		
	</label>
	<g:textField name="iso3166_2" value="${regionInstance?.iso3166_2}" />
</div>

<div class="fieldcontain ${hasErrors(bean: lawInstance, field: 'region.name', 'error')} ">
	<label for="region.name">
		<g:message code="law.region.name.label" default="Name" />
		
	</label>
	<g:textField name="name" value="${regionInstance?.name}" />
</div>

<div class="fieldcontain ${hasErrors(bean: lawInstance, field: 'region.superRegion', 'error')} ">
	<label for="region.superRegion">
		<g:message code="law.region.superRegion.label" default="Super Region" />
		
	</label>
	
</div>

<div class="fieldcontain ${hasErrors(bean: lawInstance, field: 'region.version', 'error')} ">
	<label for="region.version">
		<g:message code="law.region.version.label" default="Version" />
		
	</label>
	<g:field type="number" name="version" value="${regionInstance.version}" />
</div>
</fieldset>
<div class="fieldcontain ${hasErrors(bean: lawInstance, field: 'shortName', 'error')} ">
	<label for="shortName">
		<g:message code="law.shortName.label" default="Short Name" />
		
	</label>
	<g:textField name="shortName" value="${lawInstance?.shortName}" />
</div>

