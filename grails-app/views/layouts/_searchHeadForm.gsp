<nav:ifPageProperty pageProperty="showHeadSearch">
    <form
            action="${createLink(mapping: 'searcherSearch')}"
            id="search-form"
            class="navbar-form navbar-left"
            role="search"
            method="get"
    >
        <div class="input-group">

            <div class="open-filter">
                <a data-target="#" href="#" class="dropdown-toggle" id="open-filter-search" data-toggle="dropdown" role="button">
                    <span class="sr-only">Filtra tu búsqueda</span> <span class="fa fa-navicon fa-lg"></span>
                    %{--<span class="sr-only">Filtra tu búsqueda</span> <span class="fa fa-caret-down fa-lg"></span>--}%
                </a>
                <ul id="filters" class="dropdown-menu dropdown-menu-left" aria-labelledby="open-filter-search" role="menu">
                    <g:each in="${kuorum.core.model.search.SearchType.values()}" var="searchType">
                        <li>
                            <a href="#${searchType}" class="search-${searchType}">
                                <span class="fa fa-navicon fa-lg"></span>
                                <span class="search-filter-text">${message(code:'search.head.placeHolder.'+searchType)}</span>
                            </a>
                        </li>
                    </g:each>
                </ul>
            </div>
            <input type="text" class="form-control" placeholder="${message(code:'search.head.placeHolder')}" name="word" id="srch-term" value="${params.word}">
            <div class="input-group-btn">
                <button class="btn search" type="submit"><span class="fa fa-search"></span></button>
            </div>


            %{--<div id="filterSign"></div>--}%
            <input type="hidden" name="searchType" id="srch-type" value="${params.searchType}" />
            <input type="hidden" name="type" id="srch-userType" value="${params.type?:kuorum.core.model.solr.SolrType.POLITICIAN}"/>
            %{--<input type="hidden" name="wordOrg" id="srch-orgTerm" value="${params.word}"/>--}%
            %{--<g:each in="${kuorum.core.model.solr.SolrType.values()}" var="type">--}%
                %{--<input name="subTypes" type="checkbox" value="${type}" class="hidden" data-type="${type}" ${searchParams?.type==type?'checked':''}/>--}%
            %{--</g:each>--}%

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
                serviceUrl:urls.searchSuggest,
                minChars:1,
                width:330,
                noCache: false, //default is false, set to true to disable caching
                onSearchStart: function (query) {
                    $('.loadingSearch').show()
                },
                onSearchComplete: function (query, suggestions) {
                    $('.loadingSearch').hide()
                },
                formatResult:function (suggestion, currentValue) {
                    var format = ""
                    if (suggestion.type=="SUGGESTION"){
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
                    window.location = urls.search+"?word="+encodeURIComponent(userText)
                },
                onSelect: function(suggestion){
                    if(suggestion.type=="USER"){
                        window.location = suggestion.data.url
                    }else if(suggestion.type=="PROJECT"){
                        window.location = suggestion.data.url
                    }else{
                        window.location = urls.search+"?word="+encodeURIComponent(suggestion.value)+"&type="+$("#srch-type").val();
                    }
                },
                triggerSelectOnValidInput:false,
                deferRequestBy: 100 //miliseconds
            });
        });

    </script>
</nav:ifPageProperty>