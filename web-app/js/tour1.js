var tour1
$(document).ready(function() {

    // tour 1
    $(function() {
        tour1 = new Tour({
            name: 'tour1',
            storage: false,
            debug: true,
            backdrop: true,
            template: '<div class="popover tour"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div><div class="popover-navigation"><button class="btn btn-grey" data-role="next">Continuar</button></div></div>',
            onEnd : function(tour1){
                window.location.href = urls.tour.tour2;
            }
        });

        tour1.addSteps([
            {
                element: '#brand',
                placement: 'bottom',
                title: i18n.tour.tour1.step1_title,
                content: i18n.tour.tour1.step1_content
            },
            {
                element: '#list-kakareos-id li:first div.user:first span.action',
                placement: 'bottom',
                title: i18n.tour.tour1.step2_title,
                content: i18n.tour.tour1.step2_content
            },
            {
                element: '#list-kakareos-id li:first footer ul.actions-kak li.kakareo-number a.action.cluck:first',
                placement: 'bottom',
                title: i18n.tour.tour1.step3_title,
                content:i18n.tour.tour1.step3_content
            },
            {
                element: '#list-kakareos-id li:first footer ul.actions-kak li.more-actions',
                placement: 'bottom',
                title: i18n.tour.tour1.step4_title,
                content:i18n.tour.tour1.step4_content
            },
            {
                element: '#list-kakareos-id li:first div.link-wrapper h1 a',
                placement: 'bottom',
                title: i18n.tour.tour1.step5_title,
                content:i18n.tour.tour1.step5_content
            },
            {
                element: '#list-kakareos-id li:first footer ul.actions-kak li.read-later',
                placement: 'bottom',
                title: i18n.tour.tour1.step6_title,
                content:i18n.tour.tour1.step6_content
            },
            {
                element: 'section.boxes.guay:first > h1',
                placement: 'bottom',
                template: '<div class="popover tour"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div><div class="popover-navigation"><button class="btn btn-grey" data-role="end">Continuar</button></div></div>',
                title: i18n.tour.tour1.step7_title,
                content:i18n.tour.tour1.step7_content
            }
        ]);

        // Initialize the tour
        tour1.init();

        // Start the tour

        if ($(location).attr('pathname') == urls.tour.tour1){
            tour1.start();
        }

    });


});