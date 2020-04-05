class AmazonCdnResourceMapper {

    def map(resource, config) {
        if (config?.enabled){
//            File file;
//            if (resource.originalResource != null){
//                file = ((org.springframework.core.io.UrlResource) resource.originalResource).file;
//            }else{
//                file = new File("${resource.workDir}${resource.actualUrl}");
//            };
            String keyName = "${config.path}${resource.actualUrl}"
            String path = keyName.replace("//", "/").replaceAll(/^\//, "")
            resource.linkOverride = "https://${config.host}/${path}".toString();
            log.info("Generating URL =>${resource.linkOverride}")
        }
    }
}