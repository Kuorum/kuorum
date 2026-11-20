package kuorum.core.customDomain

import org.kuorum.rest.model.domain.DomainRSDTO
import spock.lang.Specification
import spock.lang.Unroll

class CustomDomainResolverSponsorSpec extends Specification {

    // A model that declares the sponsor property (like the kuorumRestModel versions that include it)
    static class DomainWithSponsor extends DomainRSDTO {
        String sponsor
    }

    def cleanup() {
        GroovySystem.metaClassRegistry.removeMetaClass(CustomDomainResolver)
    }

    private void domain(DomainRSDTO domainRSDTO) {
        CustomDomainResolver.metaClass.static.getDomainRSDTO = { -> domainRSDTO }
    }

    void "the sponsor of a domain whose model has no sponsor property is null, and it does not fail (older model/back, rollback)"() {
        given:
        domain(new DomainRSDTO())

        expect:
        CustomDomainResolver.sponsor == null
    }

    void "no domain configuration means no sponsor"() {
        given:
        domain(null)

        expect:
        CustomDomainResolver.sponsor == null
    }

    @Unroll
    void "sponsor '#configured' -> '#expected'"() {
        given:
        domain(new DomainWithSponsor(sponsor: configured))

        expect:
        CustomDomainResolver.sponsor == expected

        where:
        configured   || expected
        "Sponsor"    || "Sponsor"
        "  Sponsor " || "Sponsor"
        ""           || null
        "   "        || null
        null         || null
    }
}
