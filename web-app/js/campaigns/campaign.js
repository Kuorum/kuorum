$(function(){
    campaignFunctions.callToActionMobile();
});

var campaignFunctions={
    callToActionMobile:function(){
        if($("section#main .comment-box").length > 0 && $("section#main .leader-post .header .call-to-action-mobile").length > 0){
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