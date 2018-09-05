<g:if test="${userNews}">
    <h4><g:message code="politician.knownFor"/></h4>
    <section id="known-for-carousel" class="carousel news slide" data-ride="carousel">
        <div class="carousel-inner" role="listbox">
            <g:each in="${userNews}" var="userNew" status="i">
                <div class="item ${!i?"active":""}" role="option">
                    <div class="col-xs-12 col-sm-4">
                        <a href="${userNew.url}" target="_blank">
                            <div class="img-container" style="background-image: url(${userNew.image});">
                                <g:if test="${userNew.logoSource}">
                                    <img src="${userNew.logoSource}" alt="Europapress" class="logo">
                                </g:if>
                            </div>
                            <div class="carousel-caption">
                                <h3>${userNew.title}</h3>
                            </div>
                        </a>
                    </div>
                </div>
            </g:each>
        </div>


        <!-- Controls -->
        <a class="left carousel-control" href="#known-for-carousel" role="button" data-slide="prev">
            <!-- <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span> -->
            <span class="fa-stack fa-lg">
                <span class="fas fa-circle fa-stack-2x"></span>
                <span class="fal fa-chevron-left fa-stack-1x fa-inverse"></span>
            </span>
            <span class="sr-only">Previous</span>
        </a>
        <a class="right carousel-control" href="#known-for-carousel" role="button" data-slide="next">
            <!-- <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span> -->
            <span class="fa-stack fa-lg">
                <span class="fas fa-circle fa-stack-2x"></span>
                <span class="fal fa-chevron-right fa-stack-1x fa-inverse"></span>
            </span>
            <span class="sr-only">Next</span>
        </a>
    </section>
</g:if>

%{--<g:if test="${userNews}">--}%
    %{--<h4><g:message code="politician.knownFor"/></h4>--}%
    %{--<ul class='known-for'>--}%
        %{--<g:each in="${userNews}" var="userNew">--}%
            %{--<li>--}%
                %{--<g:if test="${userNew.url}">--}%
                    %{--<a href="${userNew.url}" target="_blank">--}%
                        %{--${userNew.title}--}%
                    %{--</a>--}%
                %{--</g:if>--}%
                %{--<g:else>--}%
                    %{--${userNew.title}--}%
                %{--</g:else>--}%
            %{--</li>--}%
        %{--</g:each>--}%
    %{--</ul>--}%
%{--</g:if>--}%