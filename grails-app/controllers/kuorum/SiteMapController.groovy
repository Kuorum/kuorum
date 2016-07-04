package kuorum

import com.mongodb.DBCursor
import kuorum.core.model.UserType
import kuorum.post.Post
import kuorum.project.Project
import kuorum.users.KuorumUser

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

                def highPriority = [
                        'landingPoliticians',
                        'landingCitizens',
                        'landingPrices',
                        'register']
                highPriority.each{mapping ->
                    url {
                        loc(g.createLink(absolute: true, mapping: mapping))
                        changefreq('yearly')
                        priority(0.9)
                    }
                }

                def footerMappings = [
                        'footerTechnology',
                        'footerPoliticians',
                        'footerGovernment',
                        'footerCitizens',
                        'footerDevelopers',
                        'footerAboutUs',
                        'footerVision',
                        'footerImpact',
                        'footerTeam',
                        'footerInformation',
                        'footerWidget',
                        'footerPrivacyPolicy',
                        'footerTermsUse',
                ]
                footerMappings.each{mapping ->
                    url {
                        loc(g.createLink(absolute: true, mapping: mapping))
                        changefreq('yearly')
                        priority(0.7)
                    }
                }

//                url {
//                    loc(g.createLink(absolute: true, mapping: 'discover'))
//                    changefreq('weekly')
//                    priority(0.9)
//                }

                //DYNAMIC ENTRIES
                Project.list().each {project->
                    url {
                        loc(g.createLink(absolute: true, mapping:'projectShow', params:project.encodeAsLinkProperties()))
                        changefreq('weekly')
                        priority(0.3)
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
                        priority(0.2)
                        lastmod(lastModified?.format(FORMAT_DATE_SITEMAP))
                        log.info("Creada la URL del post (${post.id}): ${post.title}")
                    }
                }
            }
        }
    }

    def siteMapCountry() {

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
                        loc(g.createLink(absolute: true, mapping:'userShow', params:[userAlias:politicianData.alias]))
                        changefreq('weekly')
                        priority(0.5)
                        lastmod(politicianData.lastUpdated.format(FORMAT_DATE_SITEMAP))
                    }
                }
            }
        }
    }
}
