<aside class="row preFooter" role="complementary">
    <div class="container-fluid">
        <div class="intro">
            <h1><g:message code="register.layoutFooter.usersModules.intro.title"/> </h1>
            <p><g:message code="register.layoutFooter.usersModules.intro.description"/></p>
        </div>
        <h2><g:message code="register.layoutFooter.usersModules.title"/> </h2>
        <ul class="user-list clearfix">
            <g:each in="${users}" var="user" status="i">
                <li><img class="user-img" alt="${user.name}" src="${image.userImgSrc(user:user)}"></li>
            </g:each>
        </ul>
    </div>
</aside>