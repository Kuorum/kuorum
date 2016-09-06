package kuorum

import org.kuorum.rest.model.contact.ContactRSDTO

class ContactTagLib {
    static defaultEncodeAs = [taglib: 'raw']
    //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]

    static namespace = "contactUtil"

    def engagement = {attrs ->
        ContactRSDTO contact = attrs.concat
        out << g.render(template: "/contacts/contactEngagement", model:[contact:contact])
    }
}
