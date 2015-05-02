package kuorum.postalCodeHandlers

import grails.transaction.Transactional


class FiveDigitPostalCodeHandlerService extends AbstractDigitPostalCodeHandlerService{

    private static final Integer POSTAL_CODE_SIZE = 5

    private static final PostalCodeHandlerType HANDLER_TYPE=PostalCodeHandlerType.STANDARD_FIVE_DIGITS;

    @Override
    protected Integer getPostalCodeSize() {
        return POSTAL_CODE_SIZE
    }

    @Override
    PostalCodeHandlerType getType() {
        return HANDLER_TYPE;
    }
}
