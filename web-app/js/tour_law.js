var tour_law
$(document).ready(function() {

    // tour law
    $(function() {
        tour_law = new Tour({
            name: 'tour_law',
            storage: false,
            debug: true,
            backdrop: true,
            template: '<div class="popover tour"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div><div class="popover-navigation"><button class="btn btn-grey" data-role="next">Continuar</button></div></div>',
            onEnd : function(tour_law){
                window.location.href = urls.home;
                $("body").append("<div class='tour-backdrop'></div>")
            }
        });

        tour_law.addSteps([
            {
                element: 'section.boxes.vote div.voting',
                placement: 'left',
                title: i18n.tour.tour_law.step1_title,
                content:i18n.tour.tour_law.step1_content
            },
            {
                element: 'aside.participate div.row',
                placement: 'top',
                template: '<div class="popover tour"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div><div class="popover-navigation"><button class="btn" data-role="end">Finalizar</button></div></div>',
                title: i18n.tour.tour_law.step2_title,
                content:i18n.tour.tour_law.step2_content
            }
        ]);

        // Initialize the tour
        tour_law.init();

        // Start the tour
        if ($(location).attr('pathname') == urls.tour.tour_law){
            tour_law.start();
        }

    });


});