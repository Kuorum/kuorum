<%@ page import="kuorum.core.model.solr.SolrType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.search"/> </title>
    <meta name="layout" content="landingLayout"/>
    <parameter name="showHeadSearch" value="false"/>
    <parameter name="special-cssClass" value="noResults"/>
    <parameter name="transparentHead" value="true"/>
</head>

<content tag="videoAndRegister">
    <g:render template="/search/searchNoLoggedLanding" model="[searchParams:searchParams]"/>
</content>

<content tag="special">
    <section role="complementary" class="no-results" id="results-tag">
        <h1><g:message code="landingSearch.noResults.ohSh"/> </h1>
    </section>
</content>

<content tag="mainContent">
    <section role="complementary" class="landing editors">
        <div class="full-video">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-sm-12">
                        <h1><g:message code="landingSearch.noResults.title"/></h1>
                        <h2><g:message code="landingEditors.videoAndRegister.subTitle"/></h2>
                        <g:link mapping="landingEditors" class="btn btn-white" fragment="ipdb-description"><g:message code="landingEditors.videoAndRegister.whatsIPDB" encodeAs="RAW"/> </g:link>
                        <sec:ifNotLoggedIn>
                            <formUtil:validateForm bean="${command}" form="joinEditors"/>
                            <g:form mapping="register" autocomplete="off" method="post" name="joinEditors" class="form-inline" role="form" novalidate="novalidate">
                                <input type="hidden" name="editor" value="true"/>
                                <fieldset>
                                    <div class="form-group">
                                        <formUtil:input
                                                command="${command}"
                                                field="name"
                                                labelCssClass="sr-only"
                                                showLabel="true"
                                                showCharCounter="false"
                                                required="true"/>
                                    </div>

                                    <div class="form-group">
                                        <formUtil:input
                                                command="${command}"
                                                field="email"
                                                type="email"
                                                showLabel="true"
                                                labelCssClass="sr-only"
                                                required="true"/>
                                    </div>
                                    <!-- para el botón, lo que prefieras, <button> o <input>-->
                                    <button type="submit" class="btn"><g:message code="landingEditors.videoAndRegister.form.submit"/> </button>
                                    <!--                                <input type="submit" class="btn" value="Start your free trial">-->
                                </fieldset>
                                <p><g:message code="register.conditions" args="[g.createLink(mapping: 'footerTermsUse')]"/></p>
                            </g:form>
                        </sec:ifNotLoggedIn>
                        <sec:ifLoggedIn>
                            <g:form mapping="editorRequestRights" autocomplete="off" method="POST" name="joinEditors" class="form-inline" role="form" novalidate="novalidate">
                            %{--Chapu para centrar--}%
                                <div class="col-sm-3"></div>
                                <fieldset class="col-sm-6">
                                    <button type="submit" class="btn"><g:message code="landingEditors.videoAndRegister.form.submit"/> </button>
                                </fieldset>
                            </g:form>
                        </sec:ifLoggedIn>
                    </div>
                </div>
            </div>
        </div>
    </section>
</content>

</html>
