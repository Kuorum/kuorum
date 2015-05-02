package kuorum.postalCodeHandlers

import grails.transaction.Transactional


abstract class AbstractDigitPostalCodeHandlerService implements PostalCodeHandler{

    protected abstract Integer getPostalCodeSize();

    @Override
    String standardizePostalCode(String postalCode) {
        postalCode.padLeft( getPostalCodeSize(), '0' )
    }

    @Override
    String getPrefixProvincePostalCode(String postalCode) {
        postalCode[0..1]
    }

    Boolean validate(String postalCode){
        postalCode.isNumber()
    }
}
