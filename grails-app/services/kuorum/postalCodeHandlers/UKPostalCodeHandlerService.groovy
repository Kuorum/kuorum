package kuorum.postalCodeHandlers

import grails.transaction.Transactional

@Transactional
class UKPostalCodeHandlerService implements PostalCodeHandler{



    private static final Integer POSTAL_CODE_SIZE = 6

    private static final PostalCodeHandlerType HANDLER_TYPE=PostalCodeHandlerType.UK;

    @Override
    String standardizePostalCode(String postalCode) {
        return postalCode?.toUpperCase()
    }

    @Override
    String getPrefixProvincePostalCode(String postalCode) {
        String provincePrefix = postalCode.replaceAll(/([a-zA-Z]*)\d*.*/) { fullMatch, prefix->
            return "$prefix"
        }
        provincePrefix

    }

    @Override
    PostalCodeHandlerType getType() {
        return HANDLER_TYPE;
    }

    @Override
    Boolean validate(String postalCode) {
        //TODO Estudiar el maldito modelo de UK
        return true
    }
}
