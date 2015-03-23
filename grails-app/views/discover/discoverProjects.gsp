<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.discover.projects"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config discover" />
    <parameter name="idMainContent" value="" />
</head>

<content tag="leftMenu">
    <h1>
        <g:message code="discover.menu.discover"/>
    </h1>
    %{--<p><g:message code="discover.menu.projects.description"/></p>--}%
    %{--<g:render template="discoverLeftMenu" model="[activeMapping:'discoverProjects', dynamicDiscoverProjects:dynamicDiscoverProjects]"/>--}%
    <ul id="filter-menu">
        <li>
            <div class="btn-group">
                <button type="button" class="btn btn-white dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                    <g:message code="kuorum.core.model.ProjectStatusType.${searchParams.projectStatusType?:'empty'}"/> <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" role="menu">
                    <li><a href="#" data-value="" data-formInput="projectStatusType">
                        <g:message code="kuorum.core.model.ProjectStatusType.empty"/>
                    </a></li>
                    <g:each in="${kuorum.core.model.ProjectStatusType.values()}" var="projectStatusType">
                        <li><a href="#" data-value="${projectStatusType}" data-formInput="projectStatusType">
                            <g:message code="kuorum.core.model.ProjectStatusType.${projectStatusType}"/>
                        </a></li>
                    </g:each>
                </ul>
            </div>
        </li>
        <li>
            <div class="btn-group">
                <button type="button" class="btn btn-white dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                    <g:message code="kuorum.core.model.CommissionType.${searchParams.commissionType?:'empty'}"/> <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" role="menu">
                    <li><a href="#" data-value="" data-formInput="commissionType">
                        <g:message code="kuorum.core.model.CommissionType.empty"/>
                    </a></li>
                    <g:each in="${kuorum.core.model.CommissionType.values()}" var="commission">
                        <li><a href="#" data-value="${commission}" data-formInput="commissionType">
                            <g:message code="kuorum.core.model.CommissionType.${commission}"/>
                        </a></li>
                    </g:each>
                </ul>
            </div>
        </li>
        %{--<li>--}%
            %{--<div class="btn-group">--}%
                %{--<button type="button" class="btn btn-white dropdown-toggle" data-toggle="dropdown" aria-expanded="false">--}%
                    %{--País <span class="caret"></span>--}%
                %{--</button>--}%
                %{--<ul class="dropdown-menu" role="menu">--}%
                    %{--<li><a href="#">Elemento 1</a></li>--}%
                    %{--<li><a href="#">Elemento 2</a></li>--}%
                    %{--<li><a href="#">Elemento 3</a></li>--}%
                    %{--<li><a href="#">...</a></li>--}%
                %{--</ul>--}%
            %{--</div>--}%
        %{--</li>--}%
        %{--<li>--}%
            %{--<div class="btn-group">--}%
                %{--<button type="button" class="btn btn-white dropdown-toggle" data-toggle="dropdown" aria-expanded="false">--}%
                    %{--Región <span class="caret"></span>--}%
                %{--</button>--}%
                %{--<ul class="dropdown-menu" role="menu">--}%
                    %{--<li><a href="#">Elemento 1</a></li>--}%
                    %{--<li><a href="#">Elemento 2</a></li>--}%
                    %{--<li><a href="#">Elemento 3</a></li>--}%
                    %{--<li><a href="#">...</a></li>--}%
                %{--</ul>--}%
            %{--</div>--}%
        %{--</li>--}%
        %{--<li>--}%
            %{--<div class="btn-group">--}%
                %{--<button type="button" class="btn btn-white dropdown-toggle" data-toggle="dropdown" aria-expanded="false">--}%
                    %{--Ciudad <span class="caret"></span>--}%
                %{--</button>--}%
                %{--<ul class="dropdown-menu" role="menu">--}%
                    %{--<li><a href="#">Elemento 1</a></li>--}%
                    %{--<li><a href="#">Elemento 2</a></li>--}%
                    %{--<li><a href="#">Elemento 3</a></li>--}%
                    %{--<li><a href="#">...</a></li>--}%
                %{--</ul>--}%
            %{--</div>--}%
        %{--</li>--}%
        %{--<li>--}%
            %{--<div class="btn-group">--}%
                %{--<button type="button" class="btn btn-white dropdown-toggle" data-toggle="dropdown" aria-expanded="false">--}%
                    %{--Partido <span class="caret"></span>--}%
                %{--</button>--}%
                %{--<ul class="dropdown-menu" role="menu">--}%
                    %{--<li><a href="#">Elemento 1</a></li>--}%
                    %{--<li><a href="#">Elemento 2</a></li>--}%
                    %{--<li><a href="#">Elemento 3</a></li>--}%
                    %{--<li><a href="#">...</a></li>--}%
                %{--</ul>--}%
            %{--</div>--}%
        %{--</li>--}%
    </ul>
</content>

<content tag="mainContent">
    <div class="clearfix">
        <h1>
            <g:message code="discover.title.discover.projects"/>
        </h1>
    </div>
    <ul id="project-list-id" class="kakareo-list project clearfix">
        <g:render template="discoverProjectList" model="[projects:projects]"/>
    </ul>

    <nav:loadMoreLink
            mapping="discoverProjects"
            mappingParams="[iso3166_2:params.iso3166_2]"
            parentId="project-list-id"
            pagination="${searchParams}"
            formId="discover-project-form"
            numElements="${projects.size()}"
    >
        <input type="hidden" name="commissionType" value="${searchParams.commissionType}"/>
        <input type="hidden" name="projectStatusType" value="${searchParams.projectStatusType}"/>
    </nav:loadMoreLink>
</content>
