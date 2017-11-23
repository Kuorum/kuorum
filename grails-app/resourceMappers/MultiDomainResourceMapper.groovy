import org.grails.plugin.resource.mapper.MapperPhase


class MultiDomainResourceMapper {

    private Integer numberResources = 0;
    def phase = MapperPhase.DISTRIBUTION

    def map(resource, config) {
        Boolean enabled = config?.enabled?Boolean.parseBoolean(config.enabled):false;
        if (enabled){
            Integer numberDomains = Integer.parseInt(config.numberDomains);
            Integer serverNumber = (numberResources % numberDomains)+1
            resource.linkOverride = "${config.protocol}${serverNumber}-${config.suffixDomain}${resource.linkUrl}".toString();
            log.info("Generating URL =>${resource.linkOverride}")
            numberResources++
        }
    }

}