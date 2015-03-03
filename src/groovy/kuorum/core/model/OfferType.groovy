package kuorum.core.model

/**
 * Created by iduetxe on 3/03/15.
 */
public enum OfferType {
    BASIC_MONTHLY (14.99, "basic"),
    BASIC_YEARLY (9.99, "basic"),
    PREMIUM_MONTHLY(59.99, "premium"),
    PREMIUM_YEARLY(49.99, "premium"),
    CITY_HALL(1500, "cityHall");

    Double price
    String group
    OfferType(Double price, String group){
        this.price = price
        this.group = group
    }

    public Double getPrice(){
        return price;
    }

    public String getGroup(){
        return group;
    }
}