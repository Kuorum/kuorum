

<header>
    <sec:ifLoggedIn>LOGAFO <sec:username/> <g:link mapping="login">LOG OUT</g:link></sec:ifLoggedIn>
    <sec:ifNotLoggedIn>NO LOGADO <g:link mapping="login">LOG IN</g:link> </sec:ifNotLoggedIn>
</header>