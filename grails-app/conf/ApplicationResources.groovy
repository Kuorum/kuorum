modules = {



    basic {
        resource url:'js/jquery.min.js'
        resource url:'js/bootstrap.min.js'
    }

    application {
        dependsOn "basic"
        resource url:'js/jquery.validate.min.js'
        resource url:'js/jquery.timeago.es.js'
        resource url:'js/jquery-te-1.4.0.min.js'
        resource url:'css/jquery-te-1.4.0.css'
        resource url:'js/jquery.noty.packaged.min.js'
        resource url:'js/jquery.noty.theme.js'
        resource url:'js/layouts/top.js'
        resource url:'js/layouts/center.js'
        resource url:'js/bootstrap-progressbar.min.js'
        resource url:'js/jquery.slimscroll.min.js'
        resource url:'js/icheck.min.js'
        resource url:'js/jquery.touchSwipe.min.js'
        resource url:'js/jquery.autocomplete.js'
        resource url:'js/jquery.bgswitcher.js' //HOME
        resource url:'js/jquery.dynamiclist.min.js' //Dynamic input
        resource url:'js/custom.js'
    }

    customFileUploader{
        dependsOn 'application'
        resource url:'js/customFileUploader.js'
        resource url:'css/customUploader.css'
        resource url:'js/jquery.Jcrop.min.js'
        resource url:'css/jquery.Jcrop.min.css'
    }

    social{
        resource url:'js/social/facebook.js'
        resource url:'js/social/twitter.js'
        resource url:'js/social/googlePlus.js'
    }

    tour{
        dependsOn 'basic'
        resource url:'js/bootstrap-tour.min.js'
        resource url:'css/bootstrap-tour.min.css'
        resource url:'js/tour_dashboard.js'
        resource url:'js/tour_law.js'
        resource url:'js/tour_post.js'
    }

    lawStats{
        dependsOn 'basic'
        resource url:'js/chart.min.js'
        resource url:'js/jquery-jvectormap-1.2.2.min.js'
        resource url:'js/jquery-jvectormap-es.js'
        resource url:'js/excanvas.min.js'
        resource url:'js/map.js'
    }
}