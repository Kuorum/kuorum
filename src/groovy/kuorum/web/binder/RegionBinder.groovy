package kuorum.web.binder

import kuorum.RegionService
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.grails.databinding.BindingHelper
import org.grails.databinding.DataBindingSource
import org.kuorum.rest.model.geolocation.RegionRSDTO

/**
 * Binder For regions.
 *
 * It use the ID recovered from suggester instead name
 */
class RegionBinder implements BindingHelper {
    @Override
    RegionRSDTO getPropertyValue(Object obj, String propertyName, DataBindingSource source) {
        RegionBinder.bindRegion obj, propertyName, source
    }

    public static RegionRSDTO bindRegion (Object obj, String propertyName, DataBindingSource source){
        String fieldId = propertyName+".id"
        if (source[fieldId]){
            Object appContext = ServletContextHolder.servletContext.getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT)
            RegionService regionService = (RegionService)appContext.regionService
            RegionRSDTO region = regionService.findRegionBySuggestedId(source[fieldId])
            return region;
        }
    }
}