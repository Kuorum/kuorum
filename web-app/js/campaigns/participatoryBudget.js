$(function () {
    
    $(".participatory-budget-status .participatory-budget-steps-info a").on("click",function (e) {
        e.preventDefault();
        var status=$(this).attr("data-status")
        var statusText=$(this).attr("data-status-text")

        var link = $("#modalEditParticipatoryBudgetStatusButtonOk").attr("href")
        link = link.replace(/status=.*/, "status=" + status);
        $("#modalEditParticipatoryBudgetStatusButtonOk").attr("href", link)


        $("#modalEditParticipatoryBudgetStatus .modal-body p strong").html(statusText)
        $("#modalEditParticipatoryBudgetStatus").modal("show")

    });

    $("#participatory-budget-district-proposals-list-tab-tag a").on("click", function(e){
        var $a = $(this)
        $a.parent().parent().children().removeClass("active")
        $a.parent().addClass("active")
        var districtId = $a.attr("data-districtId");
        $("#participatoryBudget-districtProposals-list > div").hide()
        var divId = "#proposal-district-"+districtId
        var ulId;
        if ($(divId + "ul.nav-pills-lvl2 > li > a.active").length > 0){
            var selector = $(divId + "ul.nav-pills-lvl2 > li > a.active").attr("data-listSelector")
            ulId = divId +"> ul.search-list."+selector;
        }else{
            ulId = divId+" > ul.search-list.random"
        }
        if ($(ulId+ " > li").length <= 0){
            participatoryBudgetHelper.loadMoreDistrictProposals(ulId)
        }else{
            $(divId).show()
        }

    })

    $("#participatoryBudget-districtProposals-list ul.nav-pills-lvl2 > li > a").on("click", function(e){
        e.preventDefault();
        var $a = $(this)
        var direction = $a.attr("data-direction")
        if (typeof direction !== typeof undefined && direction !== false){
            participatoryBudgetHelper.clickOrderTabDirection($a)
        }else{
            participatoryBudgetHelper.clickOrderTabRandomSeed($a)
        }
    })

    $("#participatoryBudget-districtProposals-list").on("click",'.load-more-district-proposals', function(e){
        e.preventDefault();
        var ul = $(this).parent()
        ul.attr("id", guid())
        var ulIdSelector = "#"+ul.attr("id")
        var page = parseInt(ul.attr("data-page")) +1;
        ul.attr("data-page", page)
        participatoryBudgetHelper.loadMoreDistrictProposals(ulIdSelector)
        $(this).remove(); // Removes the button. The loadMoreDistrictProposals creates new button
    })

    $("#participatory-budget-district-proposals-list-tab-tag > li:first a").click();

    $(".call-to-action-add-proposal").on("click", "a.btn.BALLOT, a.btn.TECHNICAL_REVIEW, a.btn.BALLOT, a.btn.CLOSED, a.btn.RESULTS", function(e){
        e.preventDefault()
        moveSmooth("#participatory-budget-district-proposals-list-tab-tag")
    });
    $(".leader-post .footer .comment-counter").on("click", function(e){
        e.preventDefault()
        moveSmooth("#participatory-budget-district-proposals-list-tab-tag")
    });

    $(".call-to-action-add-proposal").on("click", "a.btn.ADDING_PROPOSALS", participatoryBudgetHelper.bindActionClickAddDistrictProposal);

    participatoryBudgetHelper.moveAndOpenDistrict(window.location.hash);


    noLoggedCallbacks['participatoryBudgetAddDistrictProposalAction']= function(){
        var buttonId = $('#registro').find("form").attr("data-buttonId");
        var $button = $("#"+buttonId);
        var link = $button.attr("href")
        window.location = link;
    };

});

var participatoryBudgetHelper={

    loadMoreDistrictProposals: function(ulIdSelector){
        // pageLoadingOn("Load more districts")
        var urlLoadMoreDistrictProposals = new URL($(ulIdSelector).attr("data-loadProposals"))
        var params = {
            page : $(ulIdSelector).attr("data-page")
        }
        var direction = $(ulIdSelector).attr("data-direction")
        if (typeof direction !== typeof undefined && direction !== false){
            params['direction']=direction
        }
        var randomSeed = $(ulIdSelector).attr("data-randomSeed")
        if (typeof randomSeed !== typeof undefined && randomSeed !== false){
            params['randomSeed']=randomSeed
        }
        for (var key in params) {
            urlLoadMoreDistrictProposals.searchParams.append(key, params[key])
        }

        $(ulIdSelector).append("<li class='loading'></li>")
        $(ulIdSelector).parent().show()
        $.get( urlLoadMoreDistrictProposals)
            .done(function(data, staus, xhr) {
                var moreResults = $.parseJSON(xhr.getResponseHeader('moreResults')); //Para que sea un bool)
                $(ulIdSelector).append(data)
                if (moreResults){
                    $(ulIdSelector).append("" +
                        "<li class='col-xs-12 center load-more-district-proposals load-more'> " +
                        "<a href='#' class='loadMore' >"+i18n.seeMore+" " +
                        "<span class='fal fa-angle-down'></span>"+
                        "</a>"+
                        "</li>")
                }
                prepareYoutubeVideosClick();
                loadAjaxUserRatings();
            })
            .fail(function(messageError) {
                display.warn("Error");
            })
            .always(function() {
                // pageLoadingOff("Load more districts");
                $(ulIdSelector).find(".loading").remove()
                // $liLoading.remove()
            });
    },

    clickOrderTabDirection:function($a){
        var districtId = $a.attr("data-districtId");
        var selector = $a.attr("data-listSelector")
        var divId = "#proposal-district-"+districtId
        var ulId = divId +"> ul.search-list."+selector;
        if ($(ulId+ " > li").length <= 0 || $a.parent().hasClass("active") && $(ulId+ " > li").length > 0){ // No clicked || Changing order
            $a.parent().parent().children().removeClass("active") // Removes active of other tab links
            $a.parent().addClass("active"); // Set as active the current tab

            var $spanDirection = $a.find(".fal");
            if ($spanDirection.length==0){
                $a.append("<span class='fal'></span>")
                $spanDirection = $a.find(".fal");
            }
            var direction = $a.attr("data-direction")
            if (direction == "DESC"){
                $spanDirection.removeClass("fa-angle-down")
                $spanDirection.addClass("fa-angle-up")
                $(ulId).attr("data-direction",'ASC');
                $a.attr("data-direction",'ASC');
            }else{
                $spanDirection.removeClass("fa-angle-up")
                $spanDirection.addClass("fa-angle-down")
                $(ulId).attr("data-direction",'DESC');
                $a.attr("data-direction",'DESC');
            }
            $(ulId).empty();
            $(ulId).attr("data-page", 0)
            $(divId +"> ul.search-list").hide()
            $(ulId).show()
            participatoryBudgetHelper.loadMoreDistrictProposals(ulId)
        }else{
            $a.parent().parent().children().removeClass("active") // Removes active of other tab links
            $a.parent().addClass("active"); // Set as active the current tab
            $(divId +"> ul.search-list").hide()
            $(ulId).show()
        }
    },

    clickOrderTabRandomSeed:function($a){
        $a.parent().parent().children().removeClass("active") // Removes active of other tab links
        $a.parent().addClass("active"); // Set as active the current tab
        var districtId = $a.attr("data-districtId");
        var selector = $a.attr("data-listSelector")
        var divId = "#proposal-district-"+districtId
        var ulId = divId +"> ul.search-list."+selector;
        $(divId +"> ul.search-list").hide()
        $(ulId).show()
        if ($(ulId+ " > li").length <= 0){
            participatoryBudgetHelper.loadMoreDistrictProposals(ulId)
        }

    },

    moveAndOpenDistrict:function(districtName){
        var hash = normalizeHash(districtName)
        if ($(hash).length > 0){
            $(hash).click();
            window.setTimeout(function(){moveSmooth("#participatory-budget-district-proposals-list-tab-tag")}, 1000);
        }

    },

    bindActionClickAddDistrictProposal : function(e){
        var $button = $(this);
        var loggedUser = $button.attr("data-loggedUser");
        if (loggedUser == undefined || loggedUser == ""){
            e.preventDefault();
            e.stopPropagation();
            event.stopPropagation();

            // NO LOGGED
            var buttonId = guid();
            $button.attr("id", buttonId);
            $('#registro').find("form").attr("callback", "participatoryBudgetAddDistrictProposalAction");
            $('#registro').find("form").attr("data-buttonId", buttonId);
            $('#registro').modal('show');
        }
    }
}