package kuorum.files

import grails.transaction.Transactional
import groovyx.net.http.RESTClient
import org.apache.commons.io.IOUtils
import org.kuorum.rest.model.domain.DomainRSDTO

import java.nio.file.Path

@Transactional
class FaviconService {

    AmazonFileService amazonFileService
    DomainResourcesService domainResourcesService

    def grailsApplication

    void createFavicon(String domainLogoUrl, Path temp, DomainRSDTO domain) {
        String zipFileUrl = getFaviconResourcesZipUrl(domainLogoUrl, domain)
        File fileZip = downloadFaviconZip(zipFileUrl, temp)
        Path tempFaviconFolder = domainResourcesService.unzipFile(fileZip, temp)
        uploadFaviconFiles(tempFaviconFolder, domain)
    }

    private String getFaviconResourcesZipUrl(String domainLogoUrl, DomainRSDTO domain) {
        RESTClient http = new RESTClient('https://realfavicongenerator.net/api')
        http.ignoreSSLIssues()
        String uri = 'https://realfavicongenerator.net/api/favicon'
        def bodyParams = buildBodyParams(domainLogoUrl, domain)
        def response = http.post(
                path: uri,
                headers: ["User-Agent": "Kuorum Web"],
                query: [:],
                body: bodyParams,
                requestContentType: groovyx.net.http.ContentType.JSON
        )
        response
        String zipFileUrlRaw = response.data.favicon_generation_result.favicon.package_url
        return zipFileUrlRaw
    }

    private File downloadFaviconZip(String zipFileUrlRaw, Path temp) {
        File fileZip = new File("/${temp}/favicon.zip")
        try {
            RESTClient httpZip = new RESTClient('https://realfavicongenerator.net/files')
            httpZip.ignoreSSLIssues()
            def responseZip = httpZip.get(path: zipFileUrlRaw,
                    headers: ["User-Agent": "Kuorum Web"],
                    query: [:])
            final ByteArrayInputStream responseStream = (ByteArrayInputStream) responseZip.data
            IOUtils.copy(responseStream, new FileOutputStream(fileZip))
            responseStream.close()
        } catch (Exception e) {
            log.error(e)
            fileZip = null
            //TODO: HANDLE ERROR
        }
        return fileZip
    }

    private void uploadFaviconFiles(Path tempFavicon, DomainRSDTO domain) {
        List<File> files = Arrays.asList(tempFavicon.toFile().listFiles())
        for (File f : files) {
            amazonFileService.uploadDomainFaviconFile(f, domain.domain)
            log.info("Se ha subido un nuevo favicon del dominio : ${f.name}")
        }
    }

    private def buildBodyParams(String domainLogoUrl, DomainRSDTO domain) {
        String apiKey = grailsApplication.config.kuorum.keys.favicon
        String hexBackgroundColor = "#000000"
//        String hexBackgroundColor = domain.getSecondaryColor()
        return [
                "favicon_generation": [
                        "api_key"       : apiKey,
                        "master_picture": [
                                "type": "url",
                                "url" : domainLogoUrl
                        ],
                        "files_location": [
                                "type": "path",
                                "path": "/favicon"
                        ],
                        "favicon_design": [
                                "desktop_browser"  : [],
                                "ios"              : [
                                        "picture_aspect"  : "background_and_margin",
                                        "margin"          : "4",
                                        "background_color": hexBackgroundColor,
                                        "startup_image"   : [
                                                "master_picture": [
                                                        "type": "url",
                                                        "url" : domainLogoUrl
                                                ],
                                        ],
                                        "assets"          : [
                                                "ios6_and_prior_icons"     : false,
                                                "ios7_and_later_icons"     : true,
                                                "precomposed_icons"        : false,
                                                "declare_only_default_icon": true
                                        ]
                                ],
                                "windows"          : [
                                        "picture_aspect"  : "white_silhouette",
                                        "background_color": hexBackgroundColor,
                                        "assets"          : [
                                                "windows_80_ie_10_tile"      : true,
                                                "windows_10_ie_11_edge_tiles": [
                                                        "small"    : false,
                                                        "medium"   : true,
                                                        "big"      : true,
                                                        "rectangle": false
                                                ]
                                        ]
                                ],
                                "firefox_app"      : [
                                        "picture_aspect"        : "circle",
                                        "keep_picture_in_circle": "true",
                                        "circle_inner_margin"   : "5",
                                        "background_color"      : hexBackgroundColor,
                                        "manifest"              : [
                                                "app_name"       : domain.name,
                                                "app_description": domain.slogan,
                                                "developer_name" : "Kuorum S.L.",
                                                "developer_url"  : "https://kuorum.org"
                                        ]
                                ],
                                "android_chrome"   : [
                                        "picture_aspect"  : "shadow",
                                        "background_color": hexBackgroundColor,
                                        "manifest"        : [
                                                "name"             : domain.name,
                                                "display"          : "standalone",
                                                "orientation"      : "portrait",
                                                "start_url"        : "/",
                                                "existing_manifest": ""
                                        ],
                                        "assets"          : [
                                                "legacy_icon"         : true,
                                                "low_resolution_icons": false
                                        ],
                                        "theme_color"     : "#4972ab"
                                ],
                                "safari_pinned_tab": [
                                        "picture_aspect": "black_and_white",
                                        "threshold"     : 60,
                                        "theme_color"   : "#ffffff"
                                ],
                                "coast"            : [
                                        "picture_aspect"  : "background_and_margin",
                                        "background_color": hexBackgroundColor,
                                        "margin"          : "12%"
                                ],
                                "open_graph"       : [
                                        "picture_aspect"  : "background_and_margin",
                                        "background_color": hexBackgroundColor,
                                        "margin"          : "12%",
                                        "ratio"           : "1.91:1"
                                ],
                                "yandex_browser"   : [
                                        "background_color": hexBackgroundColor,
                                        "manifest"        : [
                                                "show_title": true,
                                                "version"   : "1.0"
                                        ]
                                ]
                        ],
                        "settings"      : [
                                "compression"             : "3",
                                "scaling_algorithm"       : "Mitchell",
                                "error_on_image_too_small": true,
                                "readme_file"             : true,
                                "html_code_file"          : false,
                                "use_path_as_is"          : false
                        ],
                        "versioning"    : [
                                "param_name" : "ver",
                                "param_value": "15Zd8"
                        ]
                ]
        ]
    }
}



