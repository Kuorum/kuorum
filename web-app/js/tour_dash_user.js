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
                window.location.href = "dashboard-user.html";
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
                title: '1/4 SUPPORT CAUSES',
                content: 'Wellcome to your dashboard. Here we’ll show you the causes that politicians around the world are defending. You can show your support by clicking in the heart icon.'
            },
            {
                element: '.pol-leaning',
                placement: function() {
                    if ($(window).width() < 769) {
                        return 'top';
                    }
                    else {
                        return 'left';
                    }
                },
                title: '2/4 MONITOR IDEOLOGY',
                content: 'We’ll calculate your political leaning index depending on the causes you support. This will help us to put you in contact with politicians and influencers that match your ideology.'
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
                title: '3/4 FOLLOW POLITICIANS',
                content: 'Follow politicians and decide what kind of events to be nitified aobut. You can also follow influencers and coordinate the next lobbying campaign. There’s no more excuses to change the world.'
            },
            {
                element: '.panel.collaborate',
                placement: function() {
                    if ($(window).width() < 769) {
                        return 'top';
                    }
                    else {
                        return 'left';
                    }
                },
                title: '4/4 BECOME AN EDITOR',
                content: 'Some politicians don’t know yet about Kuorum. While they discover the advantages of transparency, we’ll monitor their activity for them. Want to become part of our team? Apply here ;)',
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