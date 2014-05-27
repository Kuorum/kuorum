import grails.util.Environment

includeTargets << grailsScript("_GrailsInit")


eventConfigureTomcat = {tomcat ->

    if(Environment.current == Environment.DEVELOPMENT) {
        def path = "C:\\Users\\abbyrodd\\Documents\\Kuorum\\uploadImages"
        System.out.print("####EVENT: Imagenes en : /uploadedImages");
        def ctx=tomcat.host.findChild(serverContextPath)
        if ( ctx.aliases ) {
            ctx.aliases += ",/uploadedImages=${path}"
            System.out.print("####Added");
        } else {
            System.out.println("####New");
            ctx.aliases = "/uploadedImages=${path}"
        }

    }
}
