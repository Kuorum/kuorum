var tour3
$(document).ready(function() {

    // tour 3
    $(function() {
        tour3 = new Tour({
            name: 'tour3',
            storage: false,
            debug: true,
            backdrop: true,
            template: '<div class="popover tour"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div><div class="popover-navigation"><button class="btn btn-grey" data-role="next">Continuar</button></div></div>',
            onEnd : function(tour3){
                window.location.href = urls.home;
            }
        });

        tour3.addSteps([
            {
                element: 'section.boxes.noted.likes form  a',
                placement: 'bottom',
                title: i18n.tour.tour3.step2_title,
                content:i18n.tour.tour3.step2_content
            },
            {
                element: 'article.kakareo footer ul.actions-kak li.kakareo-number a.action.cluck:first',
                placement: 'bottom',
                template: '<div class="popover tour"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div><div class="popover-navigation"><button class="btn" data-role="end">Finalizar</button></div></div>',
                title: i18n.tour.tour3.step2_title,
                content:i18n.tour.tour3.step2_content
            }
        ]);

        // Initialize the tour
        tour3.init();

        // Start the tour
        if ($(location).attr('pathname') == urls.tour.tour3){
            tour3.start();
        }

    });


});