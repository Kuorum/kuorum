package kuorum

import com.fasterxml.jackson.core.type.TypeReference
import kuorum.core.model.AvailableLanguage
import kuorum.users.KuorumUser
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.geolocation.RegionRSDTO
import org.kuorum.rest.model.geolocation.RegionTypeDTO

class RegionService {

    RestKuorumApiService restKuorumApiService


    /**
     * If user has not province defined, returns null
     * @param user
     * @return
     */
    RegionRSDTO findUserRegion(KuorumUser user){
        findRegionById(user?.personalData?.provinceCode,user?.language?.locale)
    }

    List<String> buildListOfParentsCodes(RegionRSDTO region){
        List<String> regionCodes = []
        if (region){
            String code = region.iso3166;
            while (code.length()>2){
                regionCodes << code
                code = code.substring(0, code.length() -2)
            }
        }
        regionCodes.reverse()
    }

    private RegionRSDTO findParentRegion(RegionRSDTO regionRSDTO){
        if (regionRSDTO.getIso3166().length()<=2 || regionRSDTO.regionType==RegionTypeDTO.SUPRANATIONAL){
            return null;
        }
        String parentCode=regionRSDTO.getIso3166().substring(0, regionRSDTO.getIso3166().length() - 2)
        RegionRSDTO parent=findRegionBySuggestedId(parentCode);
        return  parent;

    }

    /**
     *
     * When it is used the suggester region, it returns a special ID. It is necessary to transform it to a Region
     *
     * @param suggestedId
     * @return
     */
    RegionRSDTO findRegionBySuggestedId(String isoCode){

        try{
            def response = restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.REGION_GET,
                    [:],
                    [isoCode:isoCode],
                    new TypeReference<RegionRSDTO>(){}

            )
            return response.data
        }catch (Exception e){
            log.error("Error recovering region '${isoCode}'",e)
            return null
        }

    }

    RegionRSDTO findRegionById(String isoCode, Locale locale){

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

    List<RegionRSDTO> suggestRegions(String prefixRegionName, AvailableLanguage language = AvailableLanguage.en_EN){
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.REGION_SUGGEST,
                [:],
                [regionName:prefixRegionName,lang: language.getLocale().language],
                new TypeReference<List<RegionRSDTO>>(){}
        )
        return response.data
    }

    RegionRSDTO findMostAccurateRegion(String regionName, AvailableLanguage language= AvailableLanguage.en_EN){
        try{
            Map params = [regionName:regionName, lang: language.getLocale().language]
            def response = restKuorumApiService.get(
                    RestKuorumApiService.ApiMethod.REGION_FIND,
                    [:],
                    params,
                    new TypeReference<RegionRSDTO>(){}
            )
            return response.data
        }catch (Exception kuorumException){
            return null
        }
    }
}
