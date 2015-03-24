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
                    <g:set var="facetsStatus" value="${result.getFacets().subType}"/>
                    <g:each in="${kuorum.core.model.ProjectStatusType.values()}" var="projectStatusType">
                        <g:set var="facet" value="${facetsStatus.find{it.facetName=="$projectStatusType"}}"/>
                        <li><a href="#" data-value="${projectStatusType}" data-formInput="projectStatusType">
                            <g:message code="kuorum.core.model.ProjectStatusType.${projectStatusType}"/> (${facet?.hits?:0})
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
                    <g:set var="facetsCommission" value="${result.getFacets().commissions}"/>
                    <g:each in="${kuorum.core.model.CommissionType.values()}" var="commission">
                        <g:set var="facet" value="${facetsCommission.find{it.facetName=="$commission"}}"/>
                        %{--<g:if test="${facet}">--}%
                            <li><a href="#" data-value="${commission}" data-formInput="commissionType">
                                <g:message code="kuorum.core.model.CommissionType.${commission}"/> (${facet?.hits?:0})
                            </a></li>
                        %{--</g:if>--}%
                    </g:each>
                </ul>
            </div>
        </li>

        <li>
            <div class="btn-group">
                <button type="button" class="btn btn-white dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                    ${searchParams.regionName?:g.message(code: 'discover.menu.filter.regionEmtpy')} <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" role="menu">
                    <li><a href="#" data-value="" data-formInput="commissionType">
                        <g:message code="discover.menu.filter.regionEmtpy"/>
                    </a></li>
                    <g:set var="facetsRegionName" value="${result.getFacets().regionName}"/>
                    <g:each in="${facetsRegionName}" var="facet">
                    %{--<g:if test="${facet}">--}%
                        <li><a href="#" data-value="${facet.facetName}" data-formInput="regionName">
                            ${facet.facetName} (${facet?.hits?:0})
                        </a></li>
                    %{--</g:if>--}%
                    </g:each>
                </ul>
            </div>
        </li>

        %{--<g:each in="${result.getFacets()}" var="facets">--}%
            %{--<li>--}%
                %{--<div class="btn-group">--}%
                    %{--<button type="button" class="btn btn-white dropdown-toggle" data-toggle="dropdown" aria-expanded="false">--}%
                        %{--<g:message code="kuorum.core.model.CommissionType.${searchParams.commissionType?:'empty'}"/> <span class="caret"></span>--}%
                    %{--</button>--}%
                    %{--<ul class="dropdown-menu" role="menu">--}%
                        %{--<li><a href="#" data-value="" data-formInput="commissionType">--}%
                            %{--<g:message code="kuorum.core.model.CommissionType.empty"/>--}%
                        %{--</a></li>--}%
                        %{--<g:each in="${facets.value}" var="facet">--}%
                            %{--<li><a href="#" data-value="${facet.facetName}" data-formInput="commissionType">--}%
                                %{--${facet.facetName} (${facet.hits})--}%
                            %{--</a></li>--}%
                        %{--</g:each>--}%
                    %{--</ul>--}%
                %{--</div>--}%
            %{--</li>--}%
        %{--</g:each>--}%
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
    <li>
        <g:link mapping="discoverProjects" class="cancel">
            <g:message code="discover.menu.clearFilter"/>
        </g:link>
    </li>
    </ul>
</content>

<content tag="mainContent">
    <div class="clearfix">
        <h1>
            <g:message code="discover.title.discover.projects"/>
        </h1>
    </div>
    <g:if test="${result.getNumResults()}">
        <ul id="project-list-id" class="kakareo-list project clearfix">
            <g:render template="discoverProjectList" model="[projects:result.getElements()]"/>
        </ul>
    </g:if>
    <g:else>
       <div class="box-ppal condition clearfix">
            <h1><g:message code="discover.projects.noResults.title"/> </h1>
            <h2><g:message code="discover.projects.noResults.subTitle"/> </h2>
        </div>
    </g:else>
    <nav:loadMoreLink
            mapping="discoverProjects"
            mappingParams="[iso3166_2:params.iso3166_2]"
            parentId="project-list-id"
            pagination="${searchParams}"
            formId="discover-project-form"
            numElements="${result.getNumResults()}"
    >
        <input type="hidden" name="commissionType" value="${searchParams.commissionType}"/>
        <input type="hidden" name="projectStatusType" value="${searchParams.projectStatusType}"/>
        <input type="hidden" name="regionName" value="${searchParams.regionName}"/>
    </nav:loadMoreLink>
</content>
