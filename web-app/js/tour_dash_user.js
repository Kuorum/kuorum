$(document).ready(function() {

    // tour
    $(function() {
        var tour_dash_user = new Tour({
            name: 'tour_dash_user',
            storage: false,
            keyboard: true,
            backdrop: true,
            template: '<div class="popover tour"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div><div class="popover-navigation"><button class="btn btn-grey cancel" data-role="end">Skip tour</button><button class="btn" data-role="next">Next</button></div></div>',
            onEnd : function(tour_dash_user){
                window.location.href = urls.home;
            }
        });

        tour_dash_user.addSteps([
            {
                element: '.causes-list > li:first-child > article',
                placement: function() {
                    if ($(window).width() < 769) {
                        return 'bottom';
                    }
                    else {
                        return 'right';
                    }
                },
                title: i18n.tour.step1.title,
                content: i18n.tour.step1.content
            },
            {
                element: '#right-panel-politicalLeaningIndex',
                placement: function() {
                    if ($(window).width() < 769) {
                        return 'top';
                    }
                    else {
                        return 'left';
                    }
                },
                title: i18n.tour.step2.title,
                content: i18n.tour.step2.content
            },
            {
                element: '#extendedPolitician',
                placement: function() {
                    if ($(window).width() < 769) {
                        return 'top';
                    }
                    else {
                        return 'left';
                    }
                },
                title: i18n.tour.step3.title,
                content: i18n.tour.step3.content
            },
            {
                element: '#module-card-ipdb-recruitment',
                placement: function() {
                    if ($(window).width() < 769) {
                        return 'top';
                    }
                    else {
                        return 'left';
                    }
                },
                title: i18n.tour.step4.title,
                content: i18n.tour.step4.content,
                template: '<div class="popover tour"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div><div class="popover-navigation"><button class="btn btn-grey cancel" data-role="next">Repeat</button><button class="btn" data-role="end">Got it!</button></div></div>',
                next: 0
            }
        ]);
        
        // Initialize the tour
        tour_dash_user.init();

        // Start the tour
        $('tour_dash_user.html').ready(function() {
            tour_dash_user.start();
        });

        

    });


});