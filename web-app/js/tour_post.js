var tour_post
$(document).ready(function() {

    // tour 3
    $(function() {
        tour_post = new Tour({
            name: 'tour_post',
            storage: false,
            debug: true,
            backdrop: true,
            template: '<div class="popover tour"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div><div class="popover-navigation"><button class="btn btn-grey" data-role="next">Continuar</button></div></div>',
            onEnd : function(tour_post){
                window.location.href = urls.tour.tour_law;
                $("body").append("<div class='tour-backdrop'></div>")
            }
        });

        tour_post.addSteps([
            {
                element: 'section.boxes.noted.likes form  a',
                placement: 'bottom',
                template: '<div class="popover tour"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div><div class="popover-navigation"><button class="btn btn-grey" data-role="end">Continuar</button></div></div>',
                title: i18n.tour.tour_post.step1_title,
                content:i18n.tour.tour_post.step1_content
            }
//            ,
//            {
//                element: 'article.kakareo footer ul.actions-kak li.kakareo-number a.action.cluck:first',
//                placement: 'bottom',
//                template: '<div class="popover tour"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div><div class="popover-navigation"><button class="btn btn-grey" data-role="end">Continuar</button></div></div>',
//                title: i18n.tour.tour_post.step2_title,
//                content:i18n.tour.tour_post.step2_content
//            }
        ]);

        // Initialize the tour
        tour_post.init();

        // Start the tour
        if ($(location).attr('pathname') == urls.tour.tour_post){
            tour_post.start();
        }

    });


});