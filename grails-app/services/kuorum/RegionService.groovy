package kuorum

import grails.transaction.Transactional

class RegionService {

    /**
     *
     * @param country => Region with subRegions where you want to find the postal code
     * @param postalCode => 5 digits
     * @return If not found returns null
     */
    @Transactional(readOnly = true)
    Region findProvinceByPostalCode(Region country, String postalCode) {
        String headPostalCode = postalCode[0..1]
        Region province = findRegionByPostalCode(country, headPostalCode)
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

    Region findRegionOrProvinceByPostalCode(Region country, String postalCode){
        Region region = findRegionByPostalCode(country, postalCode);
        if (!region){
            region = findProvinceByPostalCode(country, postalCode);
        }
        region;
    }

    @Transactional(readOnly = true)
    Region findRegionByName(String regionName) {
        java.util.regex.Pattern regex = java.util.regex.Pattern.compile("$regionName", java.util.regex.Pattern.CASE_INSENSITIVE)
        def res= Region.collection.find([name:regex])
        res[0]
    }
}
