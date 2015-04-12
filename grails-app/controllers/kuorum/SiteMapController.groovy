package kuorum

import kuorum.core.model.CommissionType
import kuorum.project.Project
import kuorum.post.Post

class SiteMapController {

    private static final FORMAT_DATE_SITEMAP="yyyy-MM-dd"

    def siteMap() {
        //TODO: Pensar si salvar a un fichero o a MONGO en vez de generarlo al vuelo

        render(contentType: 'text/xml', encoding: 'UTF-8') {
            mkp.yieldUnescaped '<?xml version="1.0" encoding="UTF-8"?>'
            urlset(xmlns: "http://www.sitemaps.org/schemas/sitemap/0.9",
                    'xmlns:xsi': "http://www.w3.org/2001/XMLSchema-instance",
                    'xsi:schemaLocation': "http://www.sitemaps.org/schemas/sitemap/0.9 http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd") {
                url {
                    loc(g.createLink(absolute: true, mapping: 'home'))
                    changefreq('monthly')
                    priority(1.0)
                }

                def footerMappings = [
                        'footerWhatIsKuorum',
                        'footerUsingMyVote',
                        'footerUserGuide',
                        'footerHistories',
                        'footerPurposes',
                        'footerQuestions',
                        'footerCitizens',
                        'footerOrganizations',
                        'footerPoliticians',
                        'footerDevelopers',
                        'footerKuorumStore',
                        'footerPrivacyPolicy',
                        'footerTermsUse',
                        'footerTermsAds'
                ]
                footerMappings.each{mapping ->
                    url {
                        loc(g.createLink(absolute: true, mapping: mapping))
                        changefreq('yearly')
                        priority(0.4)
                    }
                }

                url {
                    loc(g.createLink(absolute: true, mapping: 'politicians'))
                    changefreq('yearly')
                    priority(0.6)
                }
                url {
                    loc(g.createLink(absolute: true, mapping: 'register'))
                    changefreq('yearly')
                    priority(0.7)
                }
//                url {
//                    loc(g.createLink(absolute: true, mapping: 'tourStart'))
//                    changefreq('yearly')
//                    priority(0.4)
//                }

                url {
                    loc(g.createLink(absolute: true, mapping: 'discover'))
                    changefreq('weekly')
                    priority(0.9)
                }

//                Region.list().each {region ->
//                    String regionName = region.name.encodeAsKuorumUrl()
//                    url {
//                        loc(g.createLink(absolute: true, mapping: 'projects', params:[regionName:regionName]))
//                        changefreq('weekly')
//                        priority(0.9)
//                    }
//                    CommissionType.values().each { commission ->
//                        String commissionUrl = message(code:"${CommissionType.canonicalName}.${commission}")
//                        commissionUrl = commissionUrl.toLowerCase().encodeAsKuorumUrl() //toLowerCase is necessary because ... I don't know. If is not present, codec doesn't work
//                        url {
//                            loc(g.createLink(absolute: true, mapping: 'projects', params:[regionName:regionName, commission:commissionUrl]))
//                            changefreq('weekly')
//                            priority(0.9)
//                        }
//                    }
//                }
                //DYNAMIC ENTRIES
                Project.list().each {project->
                    url {
                        loc(g.createLink(absolute: true, mapping:'projectShow', params:project.encodeAsLinkProperties()))
                        changefreq('weekly')
                        priority(0.8)
                        lastmod(project.dateCreated.format(FORMAT_DATE_SITEMAP))
                    }
                }

                Post.list().each {post->
                    def dates = [
                            post.debates?post.debates.last().dateCreated:null,
                            post.comments?post.comments.last().dateCreated:null,
                            post.dateCreated
                            ]
                    Date lastModified = dates.max{a, b -> a  <=> b }
                    url {
                        loc(g.createLink(absolute: true, mapping:'postShow', params:post.encodeAsLinkProperties()))
                        changefreq('weekly')
                        priority(0.7)
                        lastmod(lastModified?.format(FORMAT_DATE_SITEMAP))
                        log.info("Creada la URL del post (${post.id}): ${post.title}")
                    }
                }
            }
        }
    }
}
