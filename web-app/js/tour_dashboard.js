var tour_dashboard
$(document).ready(function() {

    // tour 1
    $(function() {
        tour_dashboard = new Tour({
            name: 'tour_dashboard',
            storage: false,
            debug: true,
            backdrop: true,
            template: '<div class="popover tour"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div><div class="popover-navigation"><button class="btn btn-grey" data-role="next">Continuar</button></div></div>',
            onEnd : function(tour_dashboard){
                window.location.href = urls.tour.tour_post;
                $("body").append("<div class='tour-backdrop'></div>")
            }
        });

        tour_dashboard.addSteps([
            {
                element: '#brand',
                placement: 'bottom',
                title: i18n.tour.tour_dashboard.step1_title,
                content: i18n.tour.tour_dashboard.step1_content
            },
            {
                element: '#list-kakareos-id li:first',
                placement: 'bottom',
                title: i18n.tour.tour_dashboard.step2_title,
                content: i18n.tour.tour_dashboard.step2_content
            },
            {
                element: '#list-kakareos-id li:first footer ul.actions-kak li.kakareo-number a.action.cluck:first',
                placement: 'bottom',
                title: i18n.tour.tour_dashboard.step3_title,
                content:i18n.tour.tour_dashboard.step3_content
            },
            {
                element: '#list-kakareos-id li:first footer ul.actions-kak li.more-actions',
                placement: 'bottom',
                title: i18n.tour.tour_dashboard.step4_title,
                content:i18n.tour.tour_dashboard.step4_content
            },
            {
                element: '#list-kakareos-id li:first div.link-wrapper h1 a',
                placement: 'bottom',
                title: i18n.tour.tour_dashboard.step5_title,
                content:i18n.tour.tour_dashboard.step5_content
            },
            {
                element: '#list-kakareos-id li:first footer ul.actions-kak li.read-later',
                placement: 'bottom',
                title: i18n.tour.tour_dashboard.step6_title,
                content:i18n.tour.tour_dashboard.step6_content
            },
            {
                element: 'section.boxes.guay:first',
                placement: 'left',
                template: '<div class="popover tour"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div><div class="popover-navigation"><button class="btn btn-grey" data-role="end">Continuar</button></div></div>',
                title: i18n.tour.tour_dashboard.step7_title,
                content:i18n.tour.tour_dashboard.step7_content
            }
        ]);

        // Initialize the tour
        tour_dashboard.init();

        // Start the tour

        if ($(location).attr('pathname') == urls.tour.tour_dashboard){
            tour_dashboard.start();
        }

    });


});