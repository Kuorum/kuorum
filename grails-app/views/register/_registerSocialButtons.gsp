<ul class="socialGo clearfix">
%{--<li><a href="#" class="btn tw"><span class="fa fa-twitter fa-lg"></span> Entrar con Twitter</a></li>--}%
    <g:if test="${_domainLoginSettings.providerFacebook}">
        <li>
            <a href="#" onclick="return false;" provider="facebook" id="facebook-connect-link" class="btn btn-lg fb">
                <span class="fab fa-facebook-f fa-lg"></span>
                <g:message code="login.rrss.facebook"/>
            </a>
        </li>
    </g:if>
    <g:if test="${_domainLoginSettings.providerGoogle}">
        <li>
            <a href="#" onclick="return false;" provider="google" id="google-connect-link" class="btn btn-gog btn-lg">
                %{--<span class="google-logo"></span>--}%
                <g:message code="login.rrss.google"/>
            </a>
        </li>
    </g:if>
    <g:if test="${_domainLoginSettings.providerAoc}">
        <li>
            <a href="#" onclick="return false;" provider="google" id="aoc-connect-link"
               class="btn btn-aoc btn-lg btn-blue inverted">
                %{--<span class="google-logo"></span>--}%
                <g:message code="login.rrss.aoc"/>
            </a>
        </li>
    </g:if>

%{--<li><a href="#" class="btn lin"><span class="fab fa-linkedin-in fa-lg"></span> Entrar con LinkedIn</a></li>--}%
</ul>

%{--<script src="http://localhost:8181/kuorumRest/login/social/socialLogin.js" ></script>--}%
<r:require module="loginApi"/>
