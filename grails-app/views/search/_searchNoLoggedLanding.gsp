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

                    <g:link mapping="register" class="btn btn-white"><g:message code="login.head.register"/> </g:link>
                    <g:form mapping="searcherLanding" method="GET" name="findRepresentatives" id="findRepresentatives" class="form-inline searchRep" role="search" fragment="results">
                        <div class="form-group">
                            <formUtil:input field="word" id="suggestDiscoverWord" cssClass="form-control" command="${searchParams}" labelCssClass="sr-only" showLabel="true"/>


                            <script>
                                $(function(){
                                    var a = $('#suggestDiscoverWord').autocomplete({
                                        paramName:"word",
                                        params:{type:'POLITICIAN'},
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
                                            window.location = location.protocol + '//' + location.host + location.pathname+"?type=POLITICIAN&word="+encodeURIComponent(userText)+"#results"
                                        },
                                        onSelect: function(suggestion){
                                            if(suggestion.type=="USER"){
                                                window.location = suggestion.data.url
                                            }else if(suggestion.type=="PROJECT"){
                                                window.location = suggestion.data.url
                                            }else{
                                                window.location = location.protocol + '//' + location.host + location.pathname+"?type=POLITICIAN&word="+encodeURIComponent(suggestion.value)+"#results"
                                            }
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