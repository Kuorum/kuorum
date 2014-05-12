<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="editPostLayout">
</head>

<content tag="intro">
</content>

<content tag="mainContent">
 PAGAR
</content>

<content tag="cColumn">
    <g:render template="/modules/recommendedPosts" model="[recommendedPost:[post], title:message(code:'post.promote.columnC.postPromoted.title'),specialCssClass:'']"/>
    <section class="boxes plan">
        <h1>
            <g:message code="post.promote.step2.columnC.brief.title" args="[amount, numMails]"/>
            <span class="pull-right"><span class="fa fa-user"></span> + ${numMails}</span>
        </h1>
        <p><g:message code="post.promote.step2.columnC.brief.description" args="[amount, numMails]"/></p>
        <a href="#" class="btn btn-grey btn-lg btn-block disabled">
            <g:message code="post.promote.step2.columnC.brief.amount" args="[amount, numMails]"/>
        </a>
        <p class="text-center">
            <small>
                <g:message code="post.promote.step2.columnC.brief.footer" args="[amount, numMails]"/>
            </small>
        </p>
        %{--<a href="#" class="up-plan">--}%
            %{--Saltar a un plan superior--}%
        %{--</a>--}%
    </section>
</content>