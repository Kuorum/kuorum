$(function(){
    campaignFunctions.callToActionMobile();

    /*****************
     * Create list of participative budgets
     *****************/
    $(".get-participatory-budgets").on('click', function (e) {
        e.preventDefault();
        var ajaxLink = $(this).attr('href');
        getParticipatoryBudgetList(ajaxLink);
    });

    $(".modal-pb .close").on("click", function () {
        $('.modal-pb').hide()
    });

});

function getParticipatoryBudgetList(ajaxLink) {
    pageLoadingOn();
    $.ajax({
        type: "GET",
        url: ajaxLink,
        success: function(jsonData){
            var $modal = $('.modal-pb');
            var $modalContent = $('.modal-pb .modal-content .pb-list');
            $modalContent.html(jsonData);
            $modal.show()
        },
        error:function(){
            display.error("Sorry: Error doing operation");
        },
        complete: function () {
            pageLoadingOff();
        }
    });
}

var campaignFunctions={
    callToActionMobile:function(){
        if($("section#main .comment-box").length > 0 && $("section#main .leader-post .header .call-to-action-mobile").length >0){
            // Debate not published has no comment-boxes
            $(window).scroll(function () {
                var upperLimit = $("section#main .comment-box").offset();
                var buttonPosition = $("section#main .leader-post .header .call-to-action-mobile").offset();
                if (buttonPosition.top > upperLimit.top) {
                    if ($(".call-to-action-mobile").is(":visible")) {
                        $('.call-to-action-mobile').toggleClass('hidden');
                    }
                }
                else if($(".call-to-action-mobile").hasClass("hidden")){
                    if((buttonPosition.top + 150) < upperLimit.top){
                        $('.call-to-action-mobile').toggleClass('hidden');
                    }
                }
            });
        }else{
            // Debate not published => Call to action hide due to is not possible to add comments
            $(".leader-post .call-to-action-mobile").hide()
        }
    }
}