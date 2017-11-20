package kuorum

import com.mongodb.DBCursor
import kuorum.core.model.AvailableLanguage
import kuorum.core.model.UserType
import kuorum.post.PostService
import kuorum.users.KuorumUser
import org.kuorum.rest.model.communication.debate.DebateRSDTO
import org.kuorum.rest.model.communication.debate.PageDebateRSDTO
import org.kuorum.rest.model.communication.post.PagePostRSDTO
import org.kuorum.rest.model.communication.post.PostRSDTO
import org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO
import payment.campaign.DebateService

class SiteMapController {

    private static final FORMAT_DATE_SITEMAP="yyyy-MM-dd"

    PostService postService;
    DebateService debateService;

    static searchQueries =[
            es:[
                    basicSearch:[
                            "",
                            "Ignacio Garcia Peredo",
                            "Helena Galan",
                            "nomascorrupcion",
                    ],
                    userSearch:[
                            "",
                            "Ignacio Garcia Peredo",
                            "Helena Galan",
                            "nomascorrupcion",
                            "no mas corrupcion",
                    ],
                    debateSearch:[
                            "",
                            "toledo",
                            "toledo participa",
                            "extremadura",
                            "Ignacio Garcia Peredo"
                    ],
                    postSearch:[
                            "",
                            "extremadura",
                            "Ignacio García Peredo"
                    ],
                    usersByCause:[
                            "atencionomnicanal",
                            "participacion",
                            "transparencia",
                    ]
            ],
            en:[
                    basicSearch:[
                            ""
                    ],
                    userSearch:[
                            ""
                    ],
                    debateSearch:[
                            ""
                    ],
                    postSearch:[
                            ""
                    ],
                    usersByCause:[
                    ]
            ]
    ]

    def sitemapIndex(){
        String lang = params.lang
        render(contentType: 'text/xml', encoding: 'UTF-8', ) {
            mkp.yieldUnescaped '<?xml version="1.0" encoding="UTF-8"?>'
            sitemapindex(xmlns: "http://www.sitemaps.org/schemas/sitemap/0.9") {
                sitemap {
                    loc(g.createLink(mapping: 'sitemapLandings', params:[lang: lang], absolute: true))
                }
                sitemap {
                    loc(g.createLink(mapping: 'sitemapFooters', params:[lang: lang], absolute: true))
                }
                sitemap {
                    loc(g.createLink(mapping: 'sitemapSearchs', params:[lang: lang], absolute: true))
                }
                Calendar startDate = Calendar.getInstance();
                startDate.set(Calendar.YEAR, 2013) // First Kuorum User
                startDate.set(Calendar.MONTH, 1)
                startDate.set(Calendar.DAY_OF_MONTH, 1)
                Calendar endDate = Calendar.getInstance();
                endDate.add(Calendar.MONTH, 0)
                while (startDate.before(endDate)){
                    sitemap {
                        loc(g.createLink(mapping: 'sitemapUsers', params:[lang: lang, year:startDate.get(Calendar.YEAR), month:startDate.get(Calendar.MONTH)+1], absolute: true))
                    }
                    startDate.add(Calendar.MONTH, 1)
                }

            }
        }
    }

    def sitemapSearchs(){
        String lang = params.lang
        render(contentType: 'application/xml', encoding: 'UTF-8') {
            mkp.yieldUnescaped '<?xml version="1.0" encoding="UTF-8"?>'
            urlset(xmlns: "http://www.sitemaps.org/schemas/sitemap/0.9",
                    'xmlns:xhtml': "http://www.w3.org/1999/xhtml") {

                searchQueries[lang].basicSearch.each { searchText ->
                    url {
                        loc(g.createLink( mapping:'searcherSearch', absolute: true, params: [word:searchText]))
                        changefreq('daily')
                        priority(0.5)
//                    "xhtml:link rel=\"alternate\" hreflang=\"es\" href=\"${g.createLink( mapping: 'searcherSearch', absolute: true, params: [lang:'es'])}\""()
//                    "xhtml:link rel=\"alternate\" hreflang=\"en\" href=\"${g.createLink( mapping: 'searcherSearch', absolute: true, params: [lang:'en'])}\""()
                    }
                }


                searchQueries[lang].userSearch.each { searchText ->
                    url {
                        loc(g.createLink( mapping:'searcherSearchKUORUM_USER', absolute: true, params: [word:searchText]))
                        changefreq('daily')
                        priority(0.5)
//                    "xhtml:link rel=\"alternate\" hreflang=\"es\" href=\"${g.createLink( mapping: 'searcherSearch', absolute: true, params: [lang:'es'])}\""()
//                    "xhtml:link rel=\"alternate\" hreflang=\"en\" href=\"${g.createLink( mapping: 'searcherSearch', absolute: true, params: [lang:'en'])}\""()
                    }
                }

                searchQueries[lang].debateSearch.each { searchText ->
                    url {
                        loc(g.createLink( mapping:'searcherSearchDEBATE', absolute: true, params: [word:searchText]))
                        changefreq('daily')
                        priority(0.5)
//                    "xhtml:link rel=\"alternate\" hreflang=\"es\" href=\"${g.createLink( mapping: 'searcherSearch', absolute: true, params: [lang:'es'])}\""()
//                    "xhtml:link rel=\"alternate\" hreflang=\"en\" href=\"${g.createLink( mapping: 'searcherSearch', absolute: true, params: [lang:'en'])}\""()
                    }
                }

                searchQueries[lang].postSearch.each { searchText ->
                    url {
                        loc(g.createLink( mapping:'searcherSearchPOST', absolute: true, params: [word:searchText]))
                        changefreq('daily')
                        priority(0.5)
//                    "xhtml:link rel=\"alternate\" hreflang=\"es\" href=\"${g.createLink( mapping: 'searcherSearch', absolute: true, params: [lang:'es'])}\""()
//                    "xhtml:link rel=\"alternate\" hreflang=\"en\" href=\"${g.createLink( mapping: 'searcherSearch', absolute: true, params: [lang:'en'])}\""()
                    }
                }
                searchQueries[lang].usersByCause.each { searchText ->
                    url {
                        loc(g.createLink( mapping:'searcherSearchKUORUM_USERByCAUSE', absolute: true, params: [word:searchText]))
                        changefreq('daily')
                        priority(0.5)
//                    "xhtml:link rel=\"alternate\" hreflang=\"es\" href=\"${g.createLink( mapping: 'searcherSearch', absolute: true, params: [lang:'es'])}\""()
//                    "xhtml:link rel=\"alternate\" hreflang=\"en\" href=\"${g.createLink( mapping: 'searcherSearch', absolute: true, params: [lang:'en'])}\""()
                    }
                }
            }
        }
    }

    def sitemapLandings() {
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
                'footerAboutKuorum',
                'footerContactUs',
                'footerOurTeam',
                'footerUserGuides',
                'footerPress',
                'footerHistory',
                'footerPrivacyPolicy',
                'footerTermsUse',
                'footerBlog',
                'footerBlog001'
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

                sitemapindex(xmlns: "http://www.sitemaps.org/schemas/sitemap/0.9") {
                    sitemap {
                        loc(g.createLink(mapping: 'sitemapUsers', params:[lang:'es'], absolute: true))
                    }
                }
            }
        }
    }

    def sitemapUsers() {
    //TODO: Pensar si salvar a un fichero o a MONGO en vez de generarlo al vuelo
        AvailableLanguage language = AvailableLanguage.fromLocaleParam(params.lang)
        Integer year= Integer.parseInt(params.year)
        Integer month=Integer.parseInt(params.month)-1
        Calendar startRange = Calendar.getInstance();
        startRange.set(Calendar.YEAR, year)
        startRange.set(Calendar.MONTH, month)
        Calendar endRange = Calendar.getInstance()
        endRange.setTimeInMillis(startRange.getTimeInMillis());
        endRange.add(Calendar.MONTH, 1)
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
                                'language':"${language}",
                                'dateCreated':[
                                        '$gte':startRange.getTime(),
                                        '$lt':endRange.getTime()
                                ]
                        ],
                        [_id:1, alias: 1, lastUpdated:1])
                while(cursor.hasNext()){
                    def politicianData = cursor.next()
                    KuorumUser kuorumUser = politicianData as KuorumUser
                    url {
                        loc(g.createLink( mapping: 'userShow', params:[userAlias:politicianData.alias, lang:language.locale.language], absolute: true))
                        changefreq('weekly')
                        priority(0.5)
                        lastmod(politicianData.lastUpdated.format(FORMAT_DATE_SITEMAP))
//                        "xhtml:link rel=\"alternate\" hreflang=\"es\" href=\"${g.createLink( mapping: 'debateShowLang', absolute: true, params: [userAlias:politicianData.alias,lang:'es'])}\""()
//                        "xhtml:link rel=\"alternate\" hreflang=\"en\" href=\"${g.createLink( mapping: 'debateShowLang', absolute: true, params: [userAlias:politicianData.alias,lang:'en'])}\""()
                    }

                    List<PostRSDTO> posts= postService.findAllPosts(kuorumUser)
                    posts.each { post ->
                        if (CampaignStatusRSDTO.SENT.equals(post?.newsletter?.status?:null)){
                            url {
                                loc(g.createLink(mapping: 'postShow', params: post.encodeAsLinkProperties()+ [lang:language.locale.language], absolute: true))
                                changefreq('weekly')
                                priority(0.3)
                                lastmod(post.datePublished.format(FORMAT_DATE_SITEMAP))
//                            "xhtml:link rel=\"alternate\" hreflang=\"es\" href=\"${g.createLink( mapping: 'postShow', absolute: true, params: post.encodeAsLinkProperties()+ [lang:'es'])}\""()
//                            "xhtml:link rel=\"alternate\" hreflang=\"en\" href=\"${g.createLink( mapping: 'postShow', absolute: true, params: post.encodeAsLinkProperties()+ [lang:'en'])}\""()
                            }
                        }
                    }

                    List<DebateRSDTO> debates = debateService.findAllDebates(kuorumUser)
                    debates.each { debate ->
                        if (CampaignStatusRSDTO.SENT.equals(debate?.newsletter?.status?:null)) {

                            url {
                                loc(g.createLink(mapping: 'debateShow', params: debate.encodeAsLinkProperties() + [lang: language.locale.language], absolute: true))
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
}
