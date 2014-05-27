import grails.util.Environment

includeTargets << grailsScript("_GrailsInit")
includeTargets << grailsScript('_GrailsPackage')


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
