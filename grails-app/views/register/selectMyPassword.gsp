<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title>${_domainName} </title>
    <meta name="layout" content="homeLayout">
    <!-- Schema.org markup for Google+ -->
    <meta itemprop="name" content="${_domainName}">
    <meta itemprop="description" content="${g.message(code:"layout.head.meta.description", args:[kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name])}">
    <meta itemprop="image" content="${resource(dir: 'images', file: 'home1.jpg')}" />
    <meta itemprop="image" content="${resource(dir: 'images', file: 'logo@2x.png')}" />
    <parameter name="hideFooter" value="false"/>
</head>

<content tag="mainContent">
    <section id="main" role="main" class="sign home clearfix">
        <div class="row main">
            <div class="container-fluid">
                <div class="col-sm-10 col-sm-offset-1 col-md-6 col-md-offset-1 col-lg-6 col-lg-offset-1">
                    <h1 class="resetTitle"><g:message code="register.confirm.success"/></h1>
                    <h2 class="resetTitle"><g:message code="register.password.stablish"/></h2>
                </div>
                <div class="col-sm-12 col-md-4">
                    %{--<form action="#" method="post" name="pass-go-for" id="pass-go-for" class="login pass" role="form">--}%
                    <formUtil:validateForm bean="${command}" form="formResetPassword"/>
                    <g:form name="formResetPassword" mapping="registerPassword" class="login pass" role="form">
                        <div class="form-group">
                            <formUtil:input command="${command}" field="password" type="password" />
                        </div>
                        <div class="form-group">
                            <formUtil:input command="${command}" field="password2" type="password"/>
                        </div>
                        <div class="form-group">
                            <g:hiddenField name="userId" value="${userId}" />
                            <g:hiddenField name="token" value="${token}" />
                            <g:submitButton name="Submit" value="Establecer contraseña" class="btn btn-lg" />
                        </div>
                    </g:form>
                </div>
            </div><!-- /.conatiner-fluid -->
        </div><!-- /.row.main -->
    </section>
</content>
</html>