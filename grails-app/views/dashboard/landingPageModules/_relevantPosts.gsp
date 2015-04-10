<section role="complementary" class="homeSub">
<div class="row">
<h1><g:message code="landingPage.recommendedPost.title"/> </h1>
<ul class="kakareo-list">
    <g:each in="${recommendedPost}" var="post">
        <li itemscope itemtype="http://schema.org/Article" class="col-md-4">
            <g:render template="/cluck/cluck" model="[post:post, displayingColumnC:true, displayingHorizontalModule:true]"/>
        </li>
    </g:each>
</ul>
</div>
<div class="homeMore two">
    <a href="#" class="btn btn-lg open-sign-form" data-toggle="modal" data-target="#registro"><g:message code="landingPage.register.form.submit"/> </a>
    <a href="#" class="btn btn-blue btn-lg" data-toggle="modal" data-target="#registro"><g:message code="login.intro.login"/> </a>
</div>
</section>