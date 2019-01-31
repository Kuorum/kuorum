package kuorum

import com.fasterxml.jackson.core.type.TypeReference
import grails.transaction.Transactional
import kuorum.core.model.AvailableLanguage
import kuorum.core.model.RegionType
import kuorum.postalCodeHandlers.PostalCodeHandler
import kuorum.postalCodeHandlers.PostalCodeHandlerType
import kuorum.users.KuorumUser
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.geolocation.RegionRSDTO
import org.springframework.beans.factory.annotation.Autowired

class RegionService {

    RestKuorumApiService restKuorumApiService

    /**
     *
     * @param country => Region with subRegions where you want to find the postal code
     * @param postalCode => 5 digits
     * @return If not found returns null
     */
    @Transactional(readOnly = true)
    Region findProvinceByPostalCode(Region country, String postalCode) {
        Region region = findRegionByPostalCode(country, postalCode)
        Region province = null
        if (region){
            province = region.superRegion
            while (province.regionType != RegionType.COUNTY && province){
                province = province.superRegion
            }

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
            region = findRegionByPostalCode(country, postalCode)
            if (!region){
                region = findProvinceByPostalCode(country, postalCode)
            }
        }
        region
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
        Region userRegion = null
        if (user.personalData?.province){
            Region province = user.personalData.province
            Region country = findCountry(province)
            userRegion = user.personalData.province
//            userRegion = findMostSpecificRegionByPostalCode(country, user.personalData.postalCode)
            if (!userRegion){
                //Para los paises metidos sin sus codigos postales :/
                userRegion = country
            }
        }
        if(userRegion){
            //Recover most accurate info from API
            userRegion = findRegionById(userRegion.iso3166_2,user.language.locale)
        }
        return userRegion
    }

    List<Region> findRegionsList(Region region){
        List<Region> regions = []
        if (region){
            regions << region

            while (region.superRegion){
                regions << region.superRegion
                region = region.superRegion
            }
        }
        regions.reverse()
    }

    private static final Integer COUNTRY_CODE_LENGTH = 3*2-1
    Region findCountry(Region region){
        if (region?.iso3166_2){
            String countryCode = region.iso3166_2.subSequence(0,COUNTRY_CODE_LENGTH)
            return Region.findByIso3166_2(countryCode)
        }else{
            return null
        }
    }


    private static final Integer STATE_CODE_LENGTH = 3*3-1
    Region findState(Region region){
        if (region?.iso3166_2){
            String stateCode = region.iso3166_2.subSequence(0,STATE_CODE_LENGTH)
            return Region.findByIso3166_2(stateCode)
        }else{
            return null
        }
    }

    @Autowired
    List<PostalCodeHandler> postalCodeHandlersList

    private Map<PostalCodeHandlerType, PostalCodeHandler> postalCodeHandlers = [:]

    private void populateHandlers(){
        if (!postalCodeHandlers){
            for (PostalCodeHandler postalCodeHandler : postalCodeHandlersList){
                postalCodeHandlers.put(postalCodeHandler.getType(), postalCodeHandler)
            }
        }
    }

    PostalCodeHandler getPostalCodeHandler(Region country){
        PostalCodeHandlerType postalCodeHandlerType = null
        populateHandlers()
        try{
            postalCodeHandlerType = PostalCodeHandlerType.valueOf(country["postalCodeHandlerType"])
        }catch (Exception e){
            postalCodeHandlerType = PostalCodeHandlerType.STANDARD_FIVE_DIGITS
            log.warn("No se ha reconocido el handler del codigo postal del pais ${country}. Valor detectado: ${country?country["postalCodeHandlerType"]:'null'}")
        }
        postalCodeHandlers[postalCodeHandlerType]
    }

    List<Region> findPoliticianRegions(){
//        def regexCondition = ['$regex': "^${country.iso3166_2}"]
//        def criteria = ['iso3166_2': regexCondition,'regionType':RegionType.COUNTY.toString()]
//        def regionIds = Region.collection.find(criteria,[_id:1])
//        regionIds.collect{Region.get(it)}
        Region.findAllByRegionType(RegionType.STATE)
    }

    /**
     *
     * When it is used the suggester region, it returns a special ID. It is necessary to transform it to a Region
     *
     * @param suggestedId
     * @return
     */
    Region findRegionBySuggestedId(String isoCode){

        try{
            def response = restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.REGION_GET,
                    [:],
                    [isoCode:isoCode],
                    new TypeReference<RegionRSDTO>(){}

            )
            return Region.findByIso3166_2(response.data.iso3166)
        }catch (Exception e){
            log.error("Error recovering region '${isoCode}'",e)
            return null
        }

    }

    Region findRegionById(String isoCode, Locale locale){
        RegionRSDTO regionRSDTO = findRegionDataById(isoCode, locale)
        Region region = new Region(regionRSDTO.properties)
        region.iso3166_2=regionRSDTO.iso3166
        region
    }

    RegionRSDTO findRegionDataById(String isoCode, Locale locale){

        try{
            def response = restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.REGION_GET,
                    [:],
                    [isoCode:isoCode,lang:locale?.language?:"en"],
                    new TypeReference<RegionRSDTO>(){}

            )
            return response.data
        }catch (Exception e){
            log.warn("Error recovering region '${isoCode}'")
            return null
        }

    }

    List<Region> suggestRegions(String prefixRegionName, AvailableLanguage language = AvailableLanguage.en_EN){
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.REGION_SUGGEST,
                [:],
                [regionName:prefixRegionName,lang: language.getLocale().language],
                new TypeReference<List<RegionRSDTO>>(){}
        )
        List regions = response.data.collect{val ->
            Region region = new Region(val.properties)
            region.iso3166_2=val.iso3166
            region
        }
        return regions
    }

    Region findMostAccurateRegion(String regionName, Region country = null, AvailableLanguage language= AvailableLanguage.en_EN){
        try{
            Map params = [regionName:regionName, lang: language.getLocale().language]
            if (country){
                params.put("countryCode", country.iso3166_2.substring(3,5))
            }
            def response = restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.REGION_FIND,
                    [:],
                    params,
                    new TypeReference<RegionRSDTO>(){}
            )
            return Region.findByIso3166_2(response.data.iso3166)
        }catch (Exception kuorumException){
            return null
        }
    }
}
