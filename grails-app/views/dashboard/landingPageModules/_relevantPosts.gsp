<section role="complementary" class="homeSub">
<div class="row">
<h1>Impulsa las propuestas ciudadanas</h1>
<ul class="kakareo-list">
    <g:each in="${recommendedPost}" var="post">
        <li itemscope itemtype="http://schema.org/Article" class="col-md-4">
            <g:render template="/cluck/cluck" model="[post:post]"/>
        </li>
    </g:each>
</ul>
</div>
<div class="homeMore two">
    <a href="#" class="btn btn-lg" data-toggle="modal" data-target="#registro">Regístrate</a>
    <a href="#" class="btn btn-blue btn-lg" data-toggle="modal" data-target="#entrar">Entra</a>
</div>
</section>