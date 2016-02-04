package kuorum.core.model

/**
 * Created by iduetxe on 3/03/15.
 */
public enum OfferType {
    BASIC(9.00),
    PREMIUM(29.00),
    LOCAL_GROUP(199.00),
    CAMPAIGNER(5000.0);

    Double price
    OfferType(Double price){
        this.price = price
    }

    public Double getPrice(){
        return price;
    }
}