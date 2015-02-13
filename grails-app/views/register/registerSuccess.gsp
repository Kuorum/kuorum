<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="register1ColumnLayout">
</head>


<content tag="headButtons">
    <ul class="nav navbar-nav navbar-right">
        %{--<li class="underline">--}%
            %{--<g:link mapping="footerWhatIsKuorum" class="navbar-link">--}%
                %{--<g:message code="page.title.footer.whatIsKuorum"/>--}%
            %{--</g:link>--}%
        %{--</li>--}%
        <li class="underline">
            <g:link mapping="tourStart" class="navbar-link">
                <g:message code="register.head.tour"/>
            </g:link>
        </li>
    </ul>
</content>

<content tag="intro">
    <h1>Confirma tu cuenta de email</h1>
    <p>estás sólo a un paso de completar tu registro</p>
</content>



<content tag="mainContent">
    <div class="max600 text-center">
        <p><g:message code="register.success.top"/></p>
        <img src="${g.resource(dir:'images', file: 'screen.png')}" class="screen">
        <p><g:message code="register.success.bottom"/></p>
        <g:link mapping="tourStart" class="btn btn-lg"> Iniciar tour</g:link>
    </div>
</content>
