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
    <p>estás sólo a un paso de completar tu registro
        <g:if env="development" test="${flash.chainedParams?.link}">
            <a href="${flash.chainedParams.link}">Link de confirmacion para desarrollo</a>
        </g:if>
    </p>
</content>



<content tag="mainContent">
    <div class="max600 text-center">
        <p>Ahora te contamos lo que va a pasar</p>
        <p>Lorem ipsum dolor sit amet, labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea consectetur adipisicing elit, sed do eiusmod tempor incididunt ut commodo consequat.</p>
        <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>
        <img src="${g.resource(dir:'images', file: 'screen.png')}" class="screen">
        <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>
        <a href="#" class="btn btn-lg">Iniciar tour</a>
    </div>
</content>
