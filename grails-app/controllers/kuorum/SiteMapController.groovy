package kuorum

import com.mongodb.DBCursor
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.core.model.AvailableLanguage
import kuorum.users.KuorumUser
import org.kuorum.rest.model.communication.debate.DebateRSDTO
import org.kuorum.rest.model.communication.petition.PetitionRSDTO
import org.kuorum.rest.model.communication.post.PostRSDTO
import org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO
import payment.campaign.DebateService
import payment.campaign.PetitionService
import payment.campaign.PostService

class SiteMapController {

    private static final FORMAT_DATE_SITEMAP="yyyy-MM-dd"

    PostService postService
    PetitionService petitionService

    static searchQueries =[
            es:[
                    basicSearch:[
                            "",
                            "admin"
                    ],
                    userSearch:[
                            "",
                            "admin"
                    ],
                    debateSearch:[
                            "",
                            "admin"
                    ],
                    postSearch:[
                            "",
                            "admin"
                    ],
                    eventSearch:[
                            "",
                            "admin"
                    ],
                    usersByCause:[]
            ],
            en:[
                    basicSearch:[
                            "",
                            "admin"
                    ],
                    userSearch:[
                            "",
                            "admin"
                    ],
                    debateSearch:[
                            "",
                            "admin"
                    ],
                    postSearch:[
                            "admin",
                            ""
                    ],
                    eventSearch:[
                            "",
                            "admin"
                    ],
                    usersByCause:[
                    ]
            ]
    ]

    DebateService debateService

    def sitemapIndex(){
//        AvailableLanguage language = AvailableLanguage.fromLocaleParam(CustomDomainResolver.domainRSDTO.language)
        render(contentType: 'text/xml', encoding: 'UTF-8', ) {
            mkp.yieldUnescaped '<?xml version="1.0" encoding="UTF-8"?>'
            sitemapindex(xmlns: "http://www.sitemaps.org/schemas/sitemap/0.9") {
                sitemap {
                    loc(g.createLink(mapping: 'sitemapLandings', absolute: true))
                }
                sitemap {
                    loc(g.createLink(mapping: 'sitemapFooters', absolute: true))
                }
                sitemap {
                    loc(g.createLink(mapping: 'sitemapSearchs', absolute: true))
                }
//                sitemap {
//                    loc(g.createLink(mapping: 'sitemapUsersIdx', params:[lang: lang], absolute: true))
//                }
            }
        }
    }

    def sitemapSearchs(){
        AvailableLanguage language = AvailableLanguage.fromLocaleParam(CustomDomainResolver.domainRSDTO.language)
        String lang = language.locale.language
        def relevantSearchs = ["", "admin"]
        render(contentType: 'application/xml', encoding: 'UTF-8') {
            mkp.yieldUnescaped '<?xml version="1.0" encoding="UTF-8"?>'
            urlset(xmlns: "http://www.sitemaps.org/schemas/sitemap/0.9",
                    'xmlns:xhtml': "http://www.w3.org/1999/xhtml") {

                relevantSearchs.each { searchText ->
                    url {
                        loc(g.createLink( mapping:'searcherSearch', absolute: true, params: [word:searchText]))
                        changefreq('daily')
                        priority(0.5)
                    }
                    List<kuorum.core.model.solr.SolrType> solrTypes = kuorum.core.model.solr.SolrType.values() - kuorum.core.model.solr.SolrType.NEWSLETTER
                    solrTypes.each{solrType ->
                        url {
                            loc(g.createLink( mapping:"searcherSearch${solrType}", absolute: true, params: [word:searchText]))
                            changefreq('daily')
                            priority(0.5)
//                    "xhtml:link rel=\"alternate\" hreflang=\"es\" href=\"${g.createLink( mapping: 'searcherSearch', absolute: true, params: [lang:'es'])}\""()
//                    "xhtml:link rel=\"alternate\" hreflang=\"en\" href=\"${g.createLink( mapping: 'searcherSearch', absolute: true, params: [lang:'en'])}\""()
                        }

                    }
                }
            }
        }
    }

    def sitemapLandings() {
        def highPriority = [
                'landingServices',
                'login',
                'loginAuth',
                'register',
                'resetPassword']
        render(contentType: 'application/xml', encoding: 'UTF-8') {
            mkp.yieldUnescaped '<?xml version="1.0" encoding="UTF-8"?>'
            urlset(xmlns: "http://www.sitemaps.org/schemas/sitemap/0.9",
                    'xmlns:xhtml': "http://www.w3.org/1999/xhtml") {
                url {
                    loc(g.createLink(mapping: 'home', absolute: true))
                    changefreq('monthly')
                    priority(1.0)
//                    "xhtml:link rel=\"alternate\" hreflang=\"es\" href=\"${g.createLink( mapping: 'home', absolute: true, params: [lang:'es'])}\""()
//                    "xhtml:link rel=\"alternate\" hreflang=\"en\" href=\"${g.createLink( mapping: 'home', absolute: true, params: [lang:'en'])}\""()
                }
                highPriority.each { mapping ->
                    url {
                        loc(g.createLink(mapping: mapping, absolute: true))
                        changefreq('yearly')
                        priority(0.9)
//                        "xhtml:link rel=\"alternate\" hreflang=\"es\" href=\"${g.createLink( mapping: mapping, absolute: true, params: [lang:'es'])}\""()
//                        "xhtml:link rel=\"alternate\" hreflang=\"en\" href=\"${g.createLink( mapping: mapping, absolute: true, params: [lang:'en'])}\""()
                    }
                }
            }
        }
    }
    def sitemapFooters() {
        def footerMappings = [
//                'footerAboutKuorum',
//                'footerContactUs',
//                'footerOurTeam',
//                'footerUserGuides',
//                'footerPress',
//                'footerHistory',
                'footerPrivacyPolicy',
                'footerTermsUse'
        ]
        render(contentType: 'application/xml', encoding: 'UTF-8') {
            mkp.yieldUnescaped '<?xml version="1.0" encoding="UTF-8"?>'
            urlset(xmlns:"http://www.sitemaps.org/schemas/sitemap/0.9",
                    'xmlns:xhtml':"http://www.w3.org/1999/xhtml") {
                footerMappings.each{mapping ->
                    url {
                        loc(g.createLink( mapping: mapping, absolute: true))
                        changefreq('yearly')
                        priority(0.7)
//                        "xhtml:link rel=\"alternate\" hreflang=\"es\" href=\"${g.createLink( mapping: mapping, absolute: true, params: [lang:'es'])}\""()
//                        "xhtml:link rel=\"alternate\" hreflang=\"en\" href=\"${g.createLink( mapping: mapping, absolute: true, params: [lang:'en'])}\""()
                    }
                }
            }
        }
    }

    def sitemapUsersIndex() {
//        AvailableLanguage language = AvailableLanguage.fromLocaleParam(CustomDomainResolver.domainRSDTO.language)
        Calendar startDate = Calendar.getInstance()
        startDate.clear()
        startDate.set(Calendar.YEAR, 2013) // First Kuorum User
        startDate.set(Calendar.MONTH, 1)
        startDate.set(Calendar.DAY_OF_MONTH, 1)
        Calendar endDate = Calendar.getInstance()
        endDate.add(Calendar.MONTH, 0)
        render(contentType: 'text/xml', encoding: 'UTF-8', ) {
            mkp.yieldUnescaped '<?xml version="1.0" encoding="UTF-8"?>'
            sitemapindex(xmlns: "http://www.sitemaps.org/schemas/sitemap/0.9") {
                while (startDate.before(endDate)) {
                    def ranges = getDateRanges(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH))
                    def numUsers = KuorumUser.collection.count(
                            [
                                    'alias':['$ne':null],
                                    'alias':['$ne':''],
                                    'alias':['$exists':true],
//                                    'language':"${language}",
                                    'dateCreated':[
                                            '$gte':ranges.startRange,
                                            '$lt':ranges.endRange
                                    ]
                            ])
                    if (numUsers >0 ){
                        sitemap {
                            loc(g.createLink(mapping: 'sitemapUsers', params: [year: startDate.get(Calendar.YEAR), month: startDate.get(Calendar.MONTH) + 1], absolute: true))
                        }
                    }
                    startDate.add(Calendar.MONTH, 1)
                }
            }
        }
    }
    def sitemapUsers() {
    //TODO: Pensar si salvar a un fichero o a MONGO en vez de generarlo al vuelo
//        AvailableLanguage language = AvailableLanguage.fromLocaleParam(CustomDomainResolver.domainRSDTO.language)
        Integer year= Integer.parseInt(params.year)
        Integer month=Integer.parseInt(params.month)-1
        def ranges = getDateRanges(year, month)
        render(contentType: 'text/xml', encoding: 'UTF-8') {
            mkp.yieldUnescaped '<?xml version="1.0" encoding="UTF-8"?>'
            urlset(xmlns: "http://www.sitemaps.org/schemas/sitemap/0.9",
                    'xmlns:xsi': "http://www.w3.org/2001/XMLSchema-instance",
                    'xsi:schemaLocation': "http://www.sitemaps.org/schemas/sitemap/0.9 http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd") {

                DBCursor cursor = KuorumUser.collection.find(
                        [
                                'alias':['$ne':null],
                                'alias':['$ne':''],
                                'alias':['$exists':true],
//                                'language':"${language}",
                                'dateCreated':[
                                        '$gte':ranges.startRange,
                                        '$lt':ranges.endRange
                                ]
                        ],
                        [_id:1, alias: 1, lastUpdated:1])
                while(cursor.hasNext()){
                    def politicianData = cursor.next()
                    KuorumUser kuorumUser = politicianData as KuorumUser
                    url {
                        loc(g.createLink( mapping: 'userShow', params:[userAlias:politicianData.alias], absolute: true))
                        changefreq('weekly')
                        priority(0.5)
                        lastmod(politicianData.lastUpdated.format(FORMAT_DATE_SITEMAP))
//                        "xhtml:link rel=\"alternate\" hreflang=\"es\" href=\"${g.createLink( mapping: 'debateShowLang', absolute: true, params: [userAlias:politicianData.alias,lang:'es'])}\""()
//                        "xhtml:link rel=\"alternate\" hreflang=\"en\" href=\"${g.createLink( mapping: 'debateShowLang', absolute: true, params: [userAlias:politicianData.alias,lang:'en'])}\""()
                    }

                    List<PostRSDTO> posts= postService.findAllPosts(kuorumUser.id.toString())
                    posts.each { post ->
                        if (CampaignStatusRSDTO.SENT.equals(post?.newsletter?.status?:null)){
                            url {
                                loc(g.createLink(mapping: 'postShow', params: post.encodeAsLinkProperties(), absolute: true))
                                changefreq('weekly')
                                priority(0.3)
                                lastmod(post.datePublished.format(FORMAT_DATE_SITEMAP))
//                            "xhtml:link rel=\"alternate\" hreflang=\"es\" href=\"${g.createLink( mapping: 'postShow', absolute: true, params: post.encodeAsLinkProperties()+ [lang:'es'])}\""()
//                            "xhtml:link rel=\"alternate\" hreflang=\"en\" href=\"${g.createLink( mapping: 'postShow', absolute: true, params: post.encodeAsLinkProperties()+ [lang:'en'])}\""()
                            }
                        }
                    }

                    List<PetitionRSDTO> petitions= petitionService.findAllPetitionsByUser(kuorumUser.id.toString())
                    petitions.each { petition ->
                        if (CampaignStatusRSDTO.SENT.equals(petition?.newsletter?.status?:null)){
                            url {
                                loc(g.createLink(mapping: 'petitionShow', params: petition.encodeAsLinkProperties(), absolute: true))
                                changefreq('weekly')
                                priority(0.3)
                                lastmod(petition.datePublished.format(FORMAT_DATE_SITEMAP))
//                            "xhtml:link rel=\"alternate\" hreflang=\"es\" href=\"${g.createLink( mapping: 'postShow', absolute: true, params: post.encodeAsLinkProperties()+ [lang:'es'])}\""()
//                            "xhtml:link rel=\"alternate\" hreflang=\"en\" href=\"${g.createLink( mapping: 'postShow', absolute: true, params: post.encodeAsLinkProperties()+ [lang:'en'])}\""()
                            }
                        }
                    }

                    List<DebateRSDTO> debates = debateService.findAllDebates(kuorumUser.id.toString())
                    debates.each { debate ->
                        if (CampaignStatusRSDTO.SENT.equals(debate?.newsletter?.status?:null)) {

                            url {
                                loc(g.createLink(mapping: 'debateShow', params: debate.encodeAsLinkProperties(), absolute: true))
                                changefreq('weekly')
                                priority(0.3)
                                lastmod(debate.datePublished.format(FORMAT_DATE_SITEMAP))
                                //                            "xhtml:link rel=\"alternate\" hreflang=\"es\" href=\"${g.createLink( mapping: 'debateShow', absolute: true, params: debate.encodeAsLinkProperties()+ [lang:'es'])}\""()
                                //                            "xhtml:link rel=\"alternate\" hreflang=\"en\" href=\"${g.createLink( mapping: 'debateShow', absolute: true, params: debate.encodeAsLinkProperties()+ [lang:'en'])}\""()
                            }
                        }
                    }
                }
            }
        }
    }

    private getDateRanges(Integer year, Integer month){
        Calendar startRange = Calendar.getInstance()
        startRange.set(Calendar.YEAR, year)
        startRange.set(Calendar.MONTH, month)
        Calendar endRange = Calendar.getInstance()
        endRange.setTimeInMillis(startRange.getTimeInMillis())
        endRange.add(Calendar.MONTH, 1)
        return [
                startRange: startRange.time,
                endRange: endRange.time
        ]
    }
}
