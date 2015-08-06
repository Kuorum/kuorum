package kuorum.core.model

/**
 * Created by iduetxe on 3/03/15.
 */
public enum OfferType {
    BASIC_MONTHLY (0.00, "basic"),
    BASIC_YEARLY (0.00, "basic"),
    PREMIUM_MONTHLY(39.99, "premium"),
    PREMIUM_YEARLY(29.99, "premium"),
    CITY_HALL(416.67, "cityHall");

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

    public Boolean isYearlyPay(){
        switch (this){
            case OfferType.BASIC_YEARLY:
            case OfferType.PREMIUM_YEARLY:
            case OfferType.CITY_HALL:
                return true
                break;
            case OfferType.BASIC_MONTHLY:
            case OfferType.PREMIUM_MONTHLY:
            default:
                return false
        }
    }

    public Double getFinalPrice(){
        isYearlyPay()?this.price*12:this.price
    }
}