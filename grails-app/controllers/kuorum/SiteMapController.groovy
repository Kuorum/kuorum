package kuorum

import com.mongodb.DBCursor
import kuorum.core.model.UserType
import kuorum.post.PostService
import kuorum.users.KuorumUser
import org.kuorum.rest.model.communication.debate.PageDebateRSDTO
import org.kuorum.rest.model.communication.post.PagePostRSDTO
import payment.campaign.DebateService

class SiteMapController {

    private static final FORMAT_DATE_SITEMAP="yyyy-MM-dd"

    PostService postService;
    DebateService debateService;

    def sitemapIndex(){
        render(contentType: 'text/xml', encoding: 'UTF-8', ) {
            mkp.yieldUnescaped '<?xml version="1.0" encoding="UTF-8"?>'
            sitemapindex(xmlns: "http://www.sitemaps.org/schemas/sitemap/0.9") {
                sitemap {
                    loc(g.createLink(mapping: 'sitemap', absolute: true))
                }
                def continentsCode = ["AF", "AS", "EU", "NA", "OC", "SA"]
                continentsCode.each{continentCode ->
                    sitemap {
                        loc(g.createLink(mapping: 'sitemapCountry', params: [countryCode:continentCode], absolute: true))
                    }
                }
            }
        }
    }

    def sitemap() {
        //TODO: Pensar si salvar a un fichero o a MONGO en vez de generarlo al vuelo

        render(contentType: 'application/xml', encoding: 'UTF-8') {
            mkp.yieldUnescaped '<?xml version="1.0" encoding="UTF-8"?>'
            urlset(xmlns:"http://www.sitemaps.org/schemas/sitemap/0.9",
                    'xmlns:xhtml':"http://www.w3.org/1999/xhtml") {
                url {
                    loc(g.createLink( mapping: 'home', absolute: true))
                    changefreq('monthly')
                    priority(1.0)
                }

                def highPriority = [
                        'landingServices',
                        'landingTechnology',
                        'landingEnterprise',
                        'landingGovernments',
                        'landingOrganization',
                        'landingCaseStudy',
                        'landingCaseStudy001',
                        'landingCaseStudy002',
                        'landingCaseStudy003',
                        'landingCaseStudy004',
                        'register']
                highPriority.each{mapping ->
                    url {
                        loc(g.createLink( mapping: mapping, absolute: true, lang:'en'))
                        changefreq('yearly')
                        priority(0.9)
                        "xhtml:link rel=\"alternate\" hreflang=\"es\" href=\"${g.createLink( mapping: mapping, absolute: true, params: [lang:'es'])}\""()
                        "xhtml:link rel=\"alternate\" hreflang=\"en\" href=\"${g.createLink( mapping: mapping, absolute: true, params: [lang:'en'])}\""()
                    }
                }

                def footerMappings = [
                        'footerAboutKuorum',
                        'footerContactUs',
                        'footerOurTeam',
                        'footerUserGuides',
                        'footerPress',
                        'footerHistory',
                        'footerBlog',
                        'footerBlog001'
                ]
                footerMappings.each{mapping ->
                    url {
                        loc(g.createLink( mapping: mapping, absolute: true, lang:'en'))
                        changefreq('yearly')
                        priority(0.7)
                        "xhtml:link rel=\"alternate\" hreflang=\"es\" href=\"${g.createLink( mapping: mapping, absolute: true, params: [lang:'es'])}\""()
                        "xhtml:link rel=\"alternate\" hreflang=\"en\" href=\"${g.createLink( mapping: mapping, absolute: true, params: [lang:'en'])}\""()
                    }
                }

                Integer page = 0;
                Integer size = 100;
                PagePostRSDTO pagePostRSDTO = postService.findAllPosts(page, size)
                while (pagePostRSDTO.getData()) {
                    pagePostRSDTO.data.each { post ->
                        url {
                            loc(g.createLink(mapping: 'langPostShow', params: post.encodeAsLinkProperties()+ [lang:'en'], absolute: true))
                            changefreq('weekly')
                            priority(0.3)
                            lastmod(post.datePublished.format(FORMAT_DATE_SITEMAP))
                            "xhtml:link rel=\"alternate\" hreflang=\"es\" href=\"${g.createLink( mapping: 'langPostShow', absolute: true, params: post.encodeAsLinkProperties()+ [lang:'es'])}\""()
                            "xhtml:link rel=\"alternate\" hreflang=\"en\" href=\"${g.createLink( mapping: 'langPostShow', absolute: true, params: post.encodeAsLinkProperties()+ [lang:'en'])}\""()
                        }
                    }
                    page++;
                    pagePostRSDTO = postService.findAllPosts(page, size)
                }

                page = 0;
                size = 100;
                PageDebateRSDTO pageDebateRSDTO = debateService.findAllDebates(page, size)
                while (pageDebateRSDTO.getData()) {
                    pageDebateRSDTO.data.each { debate ->
                        url {
                            loc(g.createLink(mapping: 'langDebateShow', params: debate.encodeAsLinkProperties()+ [lang:'en'], absolute: true))
                            changefreq('weekly')
                            priority(0.3)
                            lastmod(debate.datePublished.format(FORMAT_DATE_SITEMAP))
                            "xhtml:link rel=\"alternate\" hreflang=\"es\" href=\"${g.createLink( mapping: 'langDebateShow', absolute: true, params: debate.encodeAsLinkProperties()+ [lang:'es'])}\""()
                            "xhtml:link rel=\"alternate\" hreflang=\"en\" href=\"${g.createLink( mapping: 'langDebateShow', absolute: true, params: debate.encodeAsLinkProperties()+ [lang:'en'])}\""()
                        }
                    }
                    page++;
                    pageDebateRSDTO = debateService.findAllDebates(page, size)
                }
            }
        }
    }

    def sitemapCountry() {

        String countryCode = params.countryCode
        render(contentType: 'text/xml', encoding: 'UTF-8') {
            mkp.yieldUnescaped '<?xml version="1.0" encoding="UTF-8"?>'
            urlset(xmlns: "http://www.sitemaps.org/schemas/sitemap/0.9",
                    'xmlns:xsi': "http://www.w3.org/2001/XMLSchema-instance",
                    'xsi:schemaLocation': "http://www.sitemaps.org/schemas/sitemap/0.9 http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd") {

                DBCursor cursor = KuorumUser.collection.find([
                    userType:UserType.POLITICIAN.toString(),
                    'professionalDetails.region.iso3166_2':['$regex':"^${countryCode}"]
                ],[_id:1, alias: 1, lastUpdated:1])
                while(cursor.hasNext()){
                    def politicianData = cursor.next()
                    url {
                        loc(g.createLink( mapping: 'langUserShow', params:[userAlias:politicianData.alias], absolute: true))
                        changefreq('weekly')
                        priority(0.5)
                        lastmod(politicianData.lastUpdated.format(FORMAT_DATE_SITEMAP))
                        "xhtml:link rel=\"alternate\" hreflang=\"es\" href=\"${g.createLink( mapping: 'debateShowLang', absolute: true, params: [userAlias:politicianData.alias,lang:'es'])}\""()
                        "xhtml:link rel=\"alternate\" hreflang=\"en\" href=\"${g.createLink( mapping: 'debateShowLang', absolute: true, params: [userAlias:politicianData.alias,lang:'en'])}\""()
                    }
                }
            }
        }
    }
}
