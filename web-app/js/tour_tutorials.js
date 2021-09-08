var tour_tutorial

var tourTutorial = {
    tour_tutorial,

    youtubeIframe: function(i, step){
        return '<iframe width="560" height="315" src="https://www.youtube.com/embed/'+step.youtubeVideo+'" title="'+step.title+'" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>';
    },

    youtubeListIframe: function (i,step){
        return '<iframe width="560" height="315" src="https://www.youtube.com/embed?listType=playlist&list='+step.youtubeList+'&index='+(i+1)+'" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>';
    },

    _buildVideoIframe: function(i,step){
        if (step.youtubeList){
            return this.youtubeListIframe(i,step);
        }else{
            return this.youtubeIframe(i,step);
        }
    },
    _buildTemplateButtons: function(i, step){
        var buttonsEnd = '<button class="btn btn-grey cancel" data-role="next">'+i18n.tour.repeat+'</button><button class="btn" data-role="end">'+i18n.tour.gotIt+'</button>';
        var buttonsNext = '<button class="btn btn-grey cancel" data-role="end">'+i18n.tour.skip+'</button><button class="btn" data-role="next">'+i18n.tour.next+'</button>';
        var buttons = buttonsNext;
        if (step.endStep){
            buttons = buttonsEnd;
        }
        return buttons;
    },
    buildTemplateYoutubePlayList: function(i,step){
        // console.log(step)
        return '<div class="popover tour-video"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-contents">' +
            tourTutorial._buildVideoIframe(i,step) +
            '</div><div class="popover-navigation">'+tourTutorial._buildTemplateButtons(i,step)+'</div></div>';
    },

    buildTemplate: function(i,step) {
        return '<div class="popover tour-video"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-contents">' +
            tourTutorial._buildVideoIframe(i,step) +
            '</div><div class="popover-navigation">'+tourTutorial._buildTemplateButtons(i,step)+'</div></div>';
    },

    playListWithLanguage: function (){
        var lang = $("html").attr("lang");
        if (lang == "es" || lang == "ca" ){
            return tourTutorial.stepsWithSpanishPlayList;
        }else{
            return tourTutorial.stepsWithEnglishPlayList;
        }
    },

    stepsWithEnglishPlayList: [
        {
            element: '#navigation-profile',
            placement: 'left',
            title: "",
            youtubeVideo:"GMsOROqc_8g",
            youtubeList:"PL1NowrrmLoB5uZ4i7E17QZrJM3Z8e8PW8",
        },{
            element: '#navigation-profile',
            placement: 'left',
            title: "",
            youtubeVideo:"VCJNFSCGEn8",
            youtubeList:"PL1NowrrmLoB5uZ4i7E17QZrJM3Z8e8PW8",
        },{
            element: '#navigation-profile',
            placement: 'left',
            title: "",
            youtubeVideo:"pPJsZLPDt5k",
            youtubeList:"PL1NowrrmLoB5uZ4i7E17QZrJM3Z8e8PW8",
        },{
            element: '#navigation-profile',
            placement: 'left',
            title: "",
            youtubeVideo:"t7uqhg07vfI",
            youtubeList:"PL1NowrrmLoB5uZ4i7E17QZrJM3Z8e8PW8",
        },{
            element: '#navigation-profile',
            placement: 'left',
            title: "",
            youtubeVideo:"VMml2wcmN6k",
            youtubeList:"PL1NowrrmLoB5uZ4i7E17QZrJM3Z8e8PW8",
        },{
            element: '#navigation-profile',
            placement: 'left',
            title: "",
            youtubeVideo:"a1ha5jHozw0",
            youtubeList:"PL1NowrrmLoB5uZ4i7E17QZrJM3Z8e8PW8",
        },{
            element: '#navigation-profile',
            placement: 'left',
            title: "",
            youtubeVideo:"B1x36DNN4k8",
            youtubeList:"PL1NowrrmLoB5uZ4i7E17QZrJM3Z8e8PW8",
        },{
            element: '#navigation-profile',
            placement: 'left',
            title: "",
            youtubeVideo:"Cb8tMpmLxL0",
            youtubeList:"PL1NowrrmLoB5uZ4i7E17QZrJM3Z8e8PW8",
            endStep: true
        }
    ],

    stepsWithSpanishPlayList: [
        {
            element: '#navigation-profile',
            placement: 'left',
            title: "",
            youtubeVideo:"xKWdqJ1Z37o",
            youtubeList:"PL1NowrrmLoB6AERCJbOJpEJfKpbjBM8Yq",
        },{
            element: '#navigation-profile',
            placement: 'left',
            title: "",
            youtubeVideo:"v3TMowUB2vQ",
            youtubeList:"PL1NowrrmLoB6AERCJbOJpEJfKpbjBM8Yq",
        },{
            element: '#navigation-profile',
            placement: 'left',
            title: "",
            youtubeVideo:"j0XtPUFRN4o",
            youtubeList:"PL1NowrrmLoB6AERCJbOJpEJfKpbjBM8Yq",
        },{
            element: '#navigation-profile',
            placement: 'left',
            title: "",
            youtubeVideo:"m_FfNfs6pzM",
            youtubeList:"PL1NowrrmLoB6AERCJbOJpEJfKpbjBM8Yq",
        },{
            element: '#navigation-profile',
            placement: 'left',
            title: "",
            youtubeVideo:"A3uFEbNwL7g",
            youtubeList:"PL1NowrrmLoB6AERCJbOJpEJfKpbjBM8Yq",
        },{
            element: '#navigation-profile',
            placement: 'left',
            title: "",
            youtubeVideo:"e5m7E4YfPgA",
            youtubeList:"PL1NowrrmLoB6AERCJbOJpEJfKpbjBM8Yq",
        },{
            element: '#navigation-profile',
            placement: 'left',
            title: "",
            youtubeVideo:"r9Omp0RajHU",
            youtubeList:"PL1NowrrmLoB6AERCJbOJpEJfKpbjBM8Yq",
        },{
            element: '#navigation-profile',
            placement: 'left',
            title: "",
            youtubeVideo:"B_QCb_fNfn4",
            youtubeList:"PL1NowrrmLoB6AERCJbOJpEJfKpbjBM8Yq",
            endStep: true
        }
    ],

    init: function (){
        tourTutorial.tour_tutorial = new Tour({
            name: 'tour_tutorial',
            storage: false,
            keyboard: true,
            backdrop: true,
            template: tourTutorial.buildTemplateYoutubePlayList,
            onEnd : function(tour_tutorial){
                //window.location.href = kuorumUrls.home;
            }
        });
        tourTutorial.tour_tutorial.addSteps(tourTutorial.playListWithLanguage());
        // Initialize the tour
        tourTutorial.tour_tutorial.init();
        tourTutorial.tour_tutorial.start();
        // tour_tutorial.restart();
    }
}

$(document).ready(function() {
    tourTutorial.init();
});