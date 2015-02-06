
<form
        action="${createLink(mapping: 'searcherSearch')}"
        id="search-form"
        class="navbar-form navbar-left"
        role="search"
        method="get"
>
    <div class="input-group">

        <input type="text" class="form-control" placeholder="${message(code:'search.head.placeHolder')}" name="srch-term" id="srch-term">
        <div class="input-group-btn">
            <button class="btn search" type="submit"><span class="fa fa-search"></span></button>
        </div>


        %{--<div id="filterSign"></div>--}%
        <input type="hidden" name="type" id="srch-type" value="${params.type?:''}"/>
        %{--<input type="hidden" name="wordOrg" id="srch-orgTerm" value="${params.word}"/>--}%
        %{--<g:each in="${kuorum.core.model.solr.SolrSubType.values()}" var="subType">--}%
            %{--<input name="subTypes" type="checkbox" value="${subType}" class="hidden" data-type="${subType.solrType}" ${searchParams?.subTypes?.contains(subType)?'checked':''}/>--}%
        %{--</g:each>--}%



        %{--<a data-target="#" href="#" class="dropdown-toggle" id="open-filter-search" data-toggle="dropdown" role="button"><span class="sr-only">Filtra tu búsqueda</span> <span class="fa fa-caret-down fa-lg"></span></a>--}%
        %{--<ul id="filters" class="dropdown-menu dropdown-menu-right" aria-labelledby="open-filter-search" role="menu">--}%
            %{--<li><a href="#" class="entodo">${message(code:'search.head.placeHolder')}</a></li>--}%
            %{--<li><a href="#" class="enleyes">${message(code:'search.head.placeHolder.projects')}<span class="fa fa-briefcase"></span></a></li>--}%
            %{--<li><a href="#" class="enpersonas">${message(code:'search.head.placeHolder.users')}<span class="fa fa-user"></span></a></li>--}%
        %{--</ul>--}%
    </div>
</form>
<script>
    function getFileterType(){
        return $("#srch-type").val()
    }
    $(function(){
        var a = $('#srch-term').autocomplete({
            paramName:"word",
            params:{type:getFileterType},
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
                    window.location = urls.search+"?word="+encodeURIComponent(suggestion.value)
                }
            },
            triggerSelectOnValidInput:false,
            deferRequestBy: 100 //miliseconds
        });
    });

</script>