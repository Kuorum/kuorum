<section id="main" role="main" class="landing search clearfix">
    <div class="full-video">
        <!-- Linkedin is not recovering properly the image -->
        <video autoplay loop poster="https://s3-eu-west-1.amazonaws.com/kuorumorg/static/video/landingSearch.png" id="bgvid">
            <source src="https://s3-eu-west-1.amazonaws.com/kuorumorg/static/video/landingSearch.webm" type="video/webm">
            <source src="https://s3-eu-west-1.amazonaws.com/kuorumorg/static/video/landingSearch.mp4" type="video/mp4">
        </video>
        %{--<img src="${r.resource(dir:'images', file:'background-search.jpg')}" class="hide" itemprop="image"/>--}%

        <div class="container-fluid">
            <div class="row">
                <div class="col-sm-12">
                    <h1 class="hidden-xs"><g:message code="search.noLogged.landing.title"/></h1>
                    <h1 class="hidden-sm hidden-md hidden-lg"><g:message code="search.noLogged.landing.title.short"/></h1>
                    <h2><g:message code="search.noLogged.landing.subTitle"/></h2>

                    %{--<g:link mapping="register" class="btn btn-white"><g:message code="login.head.register"/> </g:link>--}%
                    <a href="#results" class="btn btn-white smooth"><g:message code="search.noLogged.landing.examplesButton"/></a>
                    <g:form mapping="landingSearch" method="GET" name="findRepresentatives" id="findRepresentatives" class="form-inline searchRep" role="search" fragment="results">
                        <div class="form-group input-group">

                            <div class="open-filter">
                                <a data-target="#" href="#" class="dropdown-toggle" id="open-filter-search" data-toggle="dropdown" role="button">
                                    <span class="fa fa-kuorumSearchType-${searchParams.searchType} fa-lg"></span>
                                    <span class="hidden-xs hidden-sm">
                                       <g:message code="search.head.placeHolder.${searchParams.searchType}"/>
                                    </span>
                                    <span class="hidden-xs hidden-sm fa fa-caret-down fa-lg"></span>
                                </a>
                                <ul id="filters" class="dropdown-menu dropdown-menu-left" aria-labelledby="open-filter-search" role="menu">
                                    <g:each in="${kuorum.core.model.search.SearchType.values()}" var="searchType">
                                        <li>
                                            <a href="#${searchType}" class="search-${searchType} ${searchType==searchParams.searchType?'active':''}">
                                                <span class="fa fa-kuorumSearchType-${searchType} fa-lg"></span>
                                                <span class="search-filter-text">${message(code:'search.head.placeHolder.'+searchType)}</span>
                                            </a>
                                        </li>
                                    </g:each>
                                </ul>
                            </div>

                            <formUtil:input field="word" id="suggestDiscoverWord" cssClass="form-control" command="${searchParams}" labelCssClass="sr-only" showLabel="true"/>

                            <input type="hidden" name="searchType" id="srch-type" value="${searchParams.searchType}" />
                            <input type="hidden" name="regionCode" id="srch-regionCode" value="${params.regionCode}" />


                            <script>
                                function getSearchType(){
                                    console.log($("#srch-type").val())
                                    return $("#srch-type").val()
                                }
                                $(function(){
                                    var a = $('#suggestDiscoverWord').autocomplete({
                                        paramName:"word",
                                        params:{type:'POLITICIAN', searchType:getSearchType()},
                                        serviceUrl:kuorumUrls.searchSuggest,
                                        minChars:1,
                                        width:330,
                                        noCache: true, //default is false, set to true to disable caching
                                        onSearchStart: function (query) {
                                            $('.loadingSearch').show()
                                            query.searchType = getSearchType()
                                        },
                                        onSearchComplete: function (query, suggestions) {
                                            $('.loadingSearch').hide()
                                            $('#srch-regionCode').val("");
                                        },
                                        formatResult:function (suggestion, currentValue) {
                                            var format = ""
                                            if (suggestion.type=="SUGGESTION" || suggestion.type=="REGION"){
                                                format =  suggestion.value
                                            }else if(suggestion.type=="USER"){
                                                format = "<img class='user-img' alt='"+suggestion.data.name+"' src='"+suggestion.data.urlAvatar+"'>"
                                                format +="<span class='name'>"+suggestion.data.name+"</span>"
                                                format +="<span class='user-type'>"+suggestion.data.role.i18n+"</span>"
                                            }else if(suggestion.type=="PROJECT"){
                                                format = "<span class='statusProject'>"+suggestion.data.status.i18n+"</span>"
                                                format += suggestion.data.title
                                                format += " <strong>"+suggestion.data.hashtag+"</strong>"
                                            }
                                            return format
                                        },
                                        searchUserText:function(userText){
                                            window.location = location.protocol + '//' + location.host + location.pathname
                                                    +"?type=POLITICIAN"
                                                    +"&searchType="+getSearchType()
                                                    +"&word="+encodeURIComponent(userText)
                                                    +"#results"
                                        },
                                        onSelect: function(suggestion){
                                            var location = location.protocol + '//' + location.host + location.pathname
                                                    +"?type=POLITICIAN"
                                                    +"?searchType="+getSearchType()
                                                    +"&word="+encodeURIComponent(suggestion.value)
                                            if(suggestion.type=="REGION"){
                                                location +="&regionCode="+suggestion.data.iso3166_2
                                            }
                                            location += "#results";
                                            window.location = location

                                        },
                                        triggerSelectOnValidInput:false,
                                        deferRequestBy: 100 //miliseconds
                                    });
                                });

                            </script>
                        </div>
                        <button type="submit" class="btn btn-blue"><g:message code="search.noLogged.landing.search"/></button>
                    </g:form>
                </div>
            </div>
        </div>
    </div>
</section>