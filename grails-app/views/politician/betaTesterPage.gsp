
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="homeLayout">
</head>


<content tag="mainContent">
    <div class="container-fluid">
        <section id="main" class="col-xs-12" role="main">
            <g:if test="${user.requestedPoliticianBetaTester}">
                <div class="container-fluid politician-messages box-ppal">
                    <h1><g:message code="dashboard.politician.temporal.betaTester.thanks"/> </h1>
                </div>
            </g:if>
            <g:else>
                <div class="container-fluid politician-messages box-ppal">
                    <h1 class="alert"><g:message code="dashboard.politician.temporal.betaTester.request.title"/></h1>
                    <g:link mapping="politicianRequestBetaTester" class="btn btn-lg"><g:message code="dashboard.politician.temporal.betaTester.request.button"/></g:link>
                </div>
            </g:else>
        </section>
    </div>
</content>

