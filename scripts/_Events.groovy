import grails.util.Environment

includeTargets << grailsScript("_GrailsInit")
includeTargets << grailsScript('_GrailsPackage')
includeTargets << grailsScript('_GrailsWar')
includeTargets << grailsScript('_GrailsEvents')


eventConfigureTomcat = {tomcat ->

    if(Environment.current == Environment.DEVELOPMENT) {
//        depends(compile, createConfig)
        def path = config.kuorum.upload.serverPath
        def ctx=tomcat.host.findChild(serverContextPath)
        if ( ctx.aliases ) {
            ctx.aliases += ",/uploadedImages=${path}"
            System.out.print("####Added: Imagenes en : ${path}");
        } else {
            System.out.println("####New: Imagenes en : ${path}");
            ctx.aliases = "/uploadedImages=${path}"
        }

    }
}

eventPackagingEnd = {  ->
    System.out.println("############# Packagin end")
}

eventCreateWarStart = { warName, File stagingDir ->
    System.out.println("############# War start")
}
eventCreateWarEnd = {warName, File stagingDir ->
    System.out.println("############# War end : ${config.grails.serverURL}")
}
