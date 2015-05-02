package kuorum.postalCodeHandlers

class SixDigitPostalCodeHandlerService extends AbstractDigitPostalCodeHandlerService{

    private static final Integer POSTAL_CODE_SIZE = 6

    private static final PostalCodeHandlerType HANDLER_TYPE=PostalCodeHandlerType.STANDARD_SIX_DIGITS;

    @Override
    protected Integer getPostalCodeSize() {
        return POSTAL_CODE_SIZE
    }

    @Override
    PostalCodeHandlerType getType() {
        return HANDLER_TYPE;
    }
}
