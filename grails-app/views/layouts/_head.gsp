

<header>
    <sec:ifLoggedIn>LOGAFO <sec:username/>
        <sec:ifAnyGranted roles="ROLE_ADMIN">
            <g:link mapping="lawCreate" > Crear ley</g:link>
        </sec:ifAnyGranted>
        <g:link mapping="login">LOG OUT</g:link>
    </sec:ifLoggedIn>
    <sec:ifNotLoggedIn>NO LOGADO <g:link mapping="login">LOG IN</g:link> </sec:ifNotLoggedIn>
</header>