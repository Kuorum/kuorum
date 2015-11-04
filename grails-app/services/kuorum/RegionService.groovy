package kuorum

import grails.transaction.Transactional
import kuorum.core.model.RegionType
import kuorum.postalCodeHandlers.FiveDigitPostalCodeHandlerService
import kuorum.postalCodeHandlers.PostalCodeHandler
import kuorum.postalCodeHandlers.PostalCodeHandlerType
import kuorum.users.KuorumUser
import org.apache.catalina.core.ApplicationContext
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired

class RegionService {

    /**
     *
     * @param country => Region with subRegions where you want to find the postal code
     * @param postalCode => 5 digits
     * @return If not found returns null
     */
    @Transactional(readOnly = true)
    Region findProvinceByPostalCode(Region country, String postalCode) {
        Region region = findRegionByPostalCode(country, postalCode)
        Region province = null;
        if (region){
            province = region.superRegion;
            while (province.regionType != RegionType.COUNTY && province){
                province = province.superRegion
            };

        }else{
            String headPostalCode = getPostalCodeHandler(country).getPrefixProvincePostalCode(postalCode)
            province = findRegionByPostalCode(country, headPostalCode)
        }
        province
    }

    @Transactional(readOnly = true)
    Region findRegionByPostalCode(Region country, String postalCode) {
        def regexCondition = ['$regex': "^${country.iso3166_2}"]
        def criteria = ['iso3166_2': regexCondition,'postalCodes':postalCode]
        def regionId = Region.collection.find(criteria,[_id:1])
        Region region = null
        if (regionId.count()>0){
            region = Region.get(regionId.first()._id)
            log.info("Founded ${region} with postalcode $postalCode on country $country")
        }
        region
    }

    Region findMostSpecificRegionByPostalCode(Region country, String postalCode){
        Region region = null
        if (getPostalCodeHandler(country).validate(postalCode)){
            region = findRegionByPostalCode(country, postalCode);
            if (!region){
                region = findProvinceByPostalCode(country, postalCode);
            }
        }
        region;
    }

    @Transactional(readOnly = true)
    Region findRegionByName(String regionName) {
        java.util.regex.Pattern regex = java.util.regex.Pattern.compile("$regionName", java.util.regex.Pattern.CASE_INSENSITIVE)
        def res= Region.collection.find([name:regex])
        res[0]
    }

    /**
     * If user has not province defined, returns null
     * @param user
     * @return
     */
    Region findUserRegion(KuorumUser user){
        Region userRegion = null;
        if (user.personalData?.province){
            Region province = user.personalData.province
            Region country = findCountry(province)
            userRegion = findMostSpecificRegionByPostalCode(country, user.personalData.postalCode)
            if (!userRegion){
                //Para los paises metidos sin sus codigos postales :/
                userRegion = country
            }
        }
        return userRegion;
    }
    List<Region> findUserRegions(KuorumUser user){
        //The province is recovering on register, so it is not defined its most specific region
        // In the future the userRegion will be recover from user
        Region userRegion = findUserRegion(user)
        List<Region> regions = []
        if (userRegion){
            regions << userRegion

            while (userRegion.superRegion){
                regions << userRegion.superRegion
                userRegion = userRegion.superRegion
            }
        }
        regions.reverse()
    }

    Boolean isRelevantRegionForUser(KuorumUser user, Region region){
        Region userRegion = findUserRegion(user);
        userRegion && region && userRegion.iso3166_2.startsWith(region.iso3166_2)
    }

    private static final int REGION_ISO31662_CODE_LENGTH = 2 // EU == 2
    private static final int REGION_ISO31662_SEPARATOR_LENGTH = 1 // EU == 2
    private static final int COUNTRY_ISO31662_CODE_LENGTH = REGION_ISO31662_CODE_LENGTH*2+REGION_ISO31662_SEPARATOR_LENGTH
    Region findCountry(Region region){
        Region country = region;
        while (country.iso3166_2.length()>COUNTRY_ISO31662_CODE_LENGTH){
            country = country.superRegion
        }
        country
    }

    @Autowired
    List<PostalCodeHandler> postalCodeHandlersList;

    private Map<PostalCodeHandlerType, PostalCodeHandler> postalCodeHandlers = [:]

    private void populateHandlers(){
        if (!postalCodeHandlers){
            for (PostalCodeHandler postalCodeHandler : postalCodeHandlersList){
                postalCodeHandlers.put(postalCodeHandler.getType(), postalCodeHandler)
            }
        }
    }

    public PostalCodeHandler getPostalCodeHandler(Region country){
        PostalCodeHandlerType postalCodeHandlerType = null
        populateHandlers()
        try{
            postalCodeHandlerType = PostalCodeHandlerType.valueOf(country["postalCodeHandlerType"])
        }catch (Exception e){
            postalCodeHandlerType = PostalCodeHandlerType.STANDARD_FIVE_DIGITS;
            log.warn("No se ha reconocido el handler del codigo postal del pais ${country}. Valor detectado: ${country?country["postalCodeHandlerType"]:'null'}")
        }
        postalCodeHandlers[postalCodeHandlerType]
    }

    public List<Region> findPoliticianRegions(){
//        def regexCondition = ['$regex': "^${country.iso3166_2}"]
//        def criteria = ['iso3166_2': regexCondition,'regionType':RegionType.COUNTY.toString()]
//        def regionIds = Region.collection.find(criteria,[_id:1])
//        regionIds.collect{Region.get(it)}
        Region.findAllByRegionType(RegionType.STATE);
    }
}
