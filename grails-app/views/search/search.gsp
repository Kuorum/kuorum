<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.login"/> </title>
    <meta name="layout" content="normalLayout">
</head>



<content tag="mainContent">
    Total Results: ${docs.numResults}<br/>
    <g:if test="${docs.suggest}">
        Quiso decir: <g:link mapping="searcherSearch" params="[word:docs.suggest.suggestedQuery]" > ${docs.suggest.suggestedQuery} </g:link> => ${docs.suggest.hits}
    </g:if>
    <hr/>
    <ul>
    <g:each in="${docs.elements}" var="doc">
        <g:if test="${doc instanceof kuorum.core.model.solr.SolrKuorumUser}">
            <li>
                Usuario:<g:link mapping="userShow" params="${doc.encodeAsLinkProperties()}">${raw(doc.highlighting.name)} </g:link>
            </li>
        </g:if>
        <g:elseif test="${doc instanceof kuorum.core.model.solr.SolrPost}">
            <li>Post[${raw(doc.highlighting.hashtagLaw)}]:<g:link mapping="postShow" params="${doc.encodeAsLinkProperties()}">${raw(doc.highlighting.name)} </g:link> </li>
            <p><strong>Extracto</strong>: ${raw(doc.highlighting.text)}</p>
        </g:elseif>
        <g:elseif test="${doc instanceof kuorum.core.model.solr.SolrLaw}">
            <li>Law <g:link mapping="lawShow" params="${doc.encodeAsLinkProperties()}">${raw(doc.highlighting.hashtag)} </g:link>: ${raw(doc.highlighting.name)} </li>
            <p><strong>Extracto</strong>: ${raw(doc.highlighting.text)}</p>
        </g:elseif>
        <g:else>
            <sec:ifAllGranted roles="ROLE_ADMIN">
                <li style="background: red">WAAARRNNN => ${doc} (${doc.name} - ${doc.id})</li>
            </sec:ifAllGranted>
        </g:else>
    </g:each>
    </ul>
</content>