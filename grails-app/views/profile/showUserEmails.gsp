<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.editUser"/> </title>
    <meta name="layout" content="leftMenuConfigLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenuAccount" model="[user:user, activeMapping:'profileMailing', menu:menu]"/>
</content>

<content tag="mainContent">
    <h1>EMAILS RECIVIDOS: ${mails.totalMails}</h1>
    <ul>
        <g:each in="${mails.mails}" var="email">
            <li>
                <hr/>
                <div>
                    <div class="email-from">From: ${email.from.name} [${email.from.email}]</div>
                    <div class="to">To: ${email.to.name} [${email.to.email}]</div>
                    <div class="subject">Subject: ${email.subject}</div>
                    <div class="body" style="border:1px solid black;">Body ${raw(email.body)}</div>
                    <div class="tags">Tags: ${email.tags}</div>
                </div>
            </li>
        </g:each>
    </ul>
</content>
