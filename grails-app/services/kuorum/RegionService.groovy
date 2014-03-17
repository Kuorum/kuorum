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
    Region getRegionByPostalCode(Region country, String postalCode) {
        String headPostalCode = postalCode[0..1]

        def regexCondition = ['$regex': "^${country.iso3166_2}"]
        def criteria = ['iso3166_2': regexCondition,'postalCode':headPostalCode]
        def regionId = Region.collection.find(criteria,[_id:1])
        Region region = null
        if (regionId.count()>0){
            Region province = Region.get(regionId.first()._id)
            region = province.superRegion
            log.info("Founded ${province} with postalcode $postalCode on country $country")
        }
        region
    }
}
