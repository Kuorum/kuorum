<nav:ifPageProperty pageProperty="showHeadSearch">
    <form
            itemprop="potentialAction" itemscope itemtype="http://schema.org/SearchAction"
            action="${createLink(mapping: 'searcherSearch')}"
            id="search-form"
            class="navbar-form navbar-left"
            role="search"
            method="get"
    >
        <meta itemprop="target" content="${createLink(mapping: 'searcherSearch', absolute: true)+'?word={query}'}"/>
        <div class="input-group">

            <g:set var="searchFilterType" value="${params.searchType?:kuorum.core.model.search.SearchType.ALL}"/>
            <div class="open-filter">
                <a data-target="#" href="#" class="dropdown-toggle" id="open-filter-search" data-toggle="dropdown" role="button">
                    <span class="fa fa-kuorumSearchType-${searchFilterType} fa-lg"></span>
                    <span class="sr-only">Filtra tu búsqueda</span>
                </a>
                <ul id="filters" class="dropdown-menu dropdown-menu-left" aria-labelledby="open-filter-search" role="menu">
                    <g:each in="${kuorum.core.model.search.SearchType.values()}" var="searchType">
                        <li>
                            <a href="#${searchType}" class="search-${searchType} ${searchFilterType==searchType?'active':''}">
                                <span class="fa fa-kuorumSearchType-${searchType} fa-lg"></span>
                                <span class="search-filter-text">${message(code:'search.head.placeHolder.'+searchType)}</span>
                            </a>
                        </li>
                    </g:each>
                </ul>
            </div>
            <input
                    type="text"
                    class="form-control"
                    placeholder="${message(code:'search.head.placeHolder')}"
                    itemprop="query-input"
                    name="word"
                    id="srch-term"
                    value="${params.word}">
            <div class="input-group-btn">
                <button class="btn search" type="submit"><span class="fa fa-search"></span></button>
            </div>

            <input type="hidden" name="type" id="srch-userType" value="${params.type?:kuorum.core.model.solr.SolrType.KUORUM_USER}"/>
            <input type="hidden" name="searchType" id="srch-type" value="${params.searchType}" />
            <input type="hidden" name="regionCode" id="srch-regionCode" value="${params.regionCode}" />

        </div>
    </form>
    <script>
        function getSearchType(){
            return $("#srch-type").val()
        }
        function getFileterType(){
            return $("#srch-userType").val()
        }
        $(function(){
            var a = $('#srch-term').autocomplete({
                paramName:"word",
                params:{type:getFileterType(), searchType:getSearchType()},
                serviceUrl:kuorumUrls.searchSuggest,
                minChars:1,
                width:330,
                noCache: true, //default is false, set to true to disable caching
                onSearchStart: function (query) {
                    $('.loadingSearch').show()
                    query.searchType = getSearchType()
                    query.type = getFileterType()
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
                    window.location = kuorumUrls.search+"?word="+encodeURIComponent(userText)
                },
                onSelect: function(suggestion){
                    var location = kuorumUrls.search
                            +"?type="+getFileterType()
                            +"&searchType="+getSearchType()
                            +"&word="+encodeURIComponent(suggestion.value)
                    if(suggestion.type=="REGION"){
                        location +="&regionCode="+suggestion.data.iso3166_2
                    }
                    window.location = location
                },
                triggerSelectOnValidInput:false,
                deferRequestBy: 100 //miliseconds
            });
        });

    </script>
</nav:ifPageProperty>