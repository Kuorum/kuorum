modules = {

    basic {
        resource url:'js/jquery.min.js'
        resource url:'js/bootstrap.min.js'
    }


    application {
        dependsOn "basic"
        resource url:'js/jquery.timeago.es.js'
        resource url:'js/jquery-te-1.4.0.min.js'
        resource url:'css/jquery-te-1.4.0.css'
        resource url:'js/jquery.noty.packaged.min.js'
        resource url:'js/jquery.noty.theme.js'
        resource url:'js/layouts/top.js'
        resource url:'js/layouts/center.js'
        resource url:'js/bootstrap-progressbar.min.js'
        resource url:'js/jquery.slimscroll.min.js'
        resource url:'js/custom.js'
    }

    customFileUploader{
        dependsOn 'application'
        resource url:'js/customFileUploader.js'
        resource url:'css/customUploader.css'
        resource url:'js/jquery.Jcrop.min.js'
        resource url:'css/jquery.Jcrop.min.css'
    }
}