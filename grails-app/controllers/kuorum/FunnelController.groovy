package kuorum

import kuorum.core.model.OfferType

class FunnelController {

    /**
     * Funnel Step1
     */
    def funnelSuccessfulStories() {}
    /**
     * Funnel Step2
     */
    def funnelOffers() {}
    /**
     * Funnel Step3
     */
    def funnelPay() {
        OfferType offerType
        try{
            offerType= OfferType.valueOf(params.offerType)
        }catch (Exception e){
            flash.error="No se ha detectado la oferta"  //Por aqui no debería pasar nunca
            redirect mapping:"funnelOffers"
            return
        }
        Double totalPrice=0
        boolean yearly = false
        switch (offerType){
            case OfferType.BASIC_YEARLY:
            case OfferType.PREMIUM_YEARLY:
                totalPrice = offerType.price *12
                yearly = true
                break;
            case OfferType.CITY_HALL:
            case OfferType.BASIC_MONTHLY:
            case OfferType.PREMIUM_MONTHLY:
            default:
                totalPrice = offerType.price
                yearly = false
        }

        [
                offerType:offerType,
                totalPrice:totalPrice,
                yearly:yearly
        ]
    }
    /**
     * Funnel Step4
     */
    def funnelSubscriptionPaid() {}
}
