package kuorum.postalCodeHandlers
/**
 * Created by iduetxe on 2/05/15.
 */
public interface PostalCodeHandler {
    String standardizePostalCode(String postalCode)
    String getPrefixProvincePostalCode(String postalCode)
    Boolean validate(String postalCode)
    PostalCodeHandlerType getType()


}