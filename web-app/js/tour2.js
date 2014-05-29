var tour2
$(document).ready(function() {

    // tour 2
    $(function() {
        tour2 = new Tour({
            name: 'tour2',
            storage: false,
            debug: true,
            backdrop: true,
            template: '<div class="popover tour"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div><div class="popover-navigation"><button class="btn btn-grey" data-role="next">Continuar</button></div></div>',
            onEnd : function(tour2){
                window.location.href = urls.tour.tour3;
            }
        });

        tour2.addSteps([
            {
                element: 'section.boxes.vote div.voting',
                placement: 'bottom',
                title: i18n.tour.tour2.step1_title,
                content:i18n.tour.tour2.step1_content
            },
            {
                element: 'aside.participate span.fa-question-circle',
                placement: 'bottom',
                template: '<div class="popover tour"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div><div class="popover-navigation"><button class="btn btn-grey" data-role="end">Continuar</button></div></div>',
                title: i18n.tour.tour2.step2_title,
                content:i18n.tour.tour2.step2_content
            }
        ]);

        // Initialize the tour
        tour2.init();

        // Start the tour
        if ($(location).attr('pathname') == urls.tour.tour2){
            tour2.start();
        }

    });


});