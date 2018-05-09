package kuorum.files

import grails.transaction.Transactional
import groovyx.net.http.RESTClient
import org.apache.commons.io.IOUtils
import org.kuorum.rest.model.domain.DomainRSDTO

import java.nio.file.Files
import java.nio.file.Path

@Transactional
class FaviconService {

    AmazonFileService amazonFileService
    DomainResourcesService domainResourcesService

    def grailsApplication

    void createFavicon (String domainLogoUrl, Path temp, DomainRSDTO domain){

        RESTClient http = new RESTClient('https://realfavicongenerator.net/api')
        http.ignoreSSLIssues()
        String uri = 'https://realfavicongenerator.net/api/favicon'
        def bodyParams = buildBodyParams(domainLogoUrl, domain);
        def response = http.post(
                path:uri,
                headers: ["User-Agent": "Kuorum Web"],
                query:[:],
                body:  bodyParams,


                requestContentType : groovyx.net.http.ContentType.JSON
        )
        response;




        String zipFileUrlRaw = response.data.favicon_generation_result.favicon.package_url;
        File fileZip = new File("/${temp}/favicon.zip")
        Path tempFavicon = Files.createTempDirectory(temp, "faviconTemp");
        try {
            RESTClient httpZip = new RESTClient('https://realfavicongenerator.net/files')
            httpZip.ignoreSSLIssues()
            def responseZip = httpZip.get(path: zipFileUrlRaw,
                    headers: ["User-Agent": "Kuorum Web"],
                    query: [:])
            final ByteArrayInputStream responseStream = (ByteArrayInputStream) responseZip.data
            IOUtils.copy(responseStream, new FileOutputStream(fileZip));
            responseStream.close()
        } catch (Exception e) {
            log.error(e)
            //TODO: HANDLE ERROR
        }

        domainResourcesService.unzipFile(fileZip, tempFavicon)

        List<File> files = Arrays.asList(tempFavicon.toFile().listFiles());
        for (File f : files) {
            amazonFileService.uploadDomainFaviconFile(f, domain.domain)
            log.info("Se ha subido un nuevo favicon del dominio")
        }
        temp.toAbsolutePath().deleteDir()
    }

    private def buildBodyParams(String domainLogoUrl, DomainRSDTO domain) {
        String apiKey = grailsApplication.config.kuorum.favicon.key
        return [
                "favicon_generation": [
                        "api_key": apiKey,
                        "master_picture": [
                                "type": "url",
                                "url": domainLogoUrl
                        ],
                        "files_location": [
                                "type": "path",
                                "path": "/favicon"
                        ],
                        "favicon_design": [
                                "desktop_browser": [],
                                "ios": [
                                        "picture_aspect": "background_and_margin",
                                        "margin": "4",
                                        "background_color": domain.getSecondaryColor(),
                                        "startup_image": [
                                                "master_picture": [
                                                        "type": "url",
                                                        "url": domainLogoUrl
                                                ],
//                                        "background_color": "#ffffff"
                                        ],
                                        "assets": [
                                                "ios6_and_prior_icons": false,
                                                "ios7_and_later_icons": true,
                                                "precomposed_icons": false,
                                                "declare_only_default_icon": true
                                        ]
                                ],
                                "windows": [
                                        "picture_aspect": "white_silhouette",
                                        "background_color": "#00aba9",
                                        "assets": [
                                                "windows_80_ie_10_tile": true,
                                                "windows_10_ie_11_edge_tiles": [
                                                        "small": false,
                                                        "medium": true,
                                                        "big": true,
                                                        "rectangle": false
                                                ]
                                        ]
                                ],
                                "firefox_app": [
                                        "picture_aspect": "circle",
                                        "keep_picture_in_circle": "true",
                                        "circle_inner_margin": "5",
                                        "background_color": "#ffffff",
                                        "manifest": [
                                                "app_name": "My sample app",
                                                "app_description": "Yet another sample application",
                                                "developer_name": "Philippe Bernard",
                                                "developer_url": "http://stackoverflow.com/users/499917/philippe-b"
                                        ]
                                ],
                                "android_chrome": [
                                        "picture_aspect": "shadow",
                                        "manifest": [
                                                "name": "My sample app",
                                                "display": "standalone",
                                                "orientation": "portrait",
                                                "start_url": "/homepage.html",
                                                "existing_manifest": "{\"name\": \"Yet another app\"}"
                                        ],
                                        "assets": [
                                                "legacy_icon": true,
                                                "low_resolution_icons": false
                                        ],
                                        "theme_color": "#4972ab"
                                ],
                                "safari_pinned_tab": [
                                        "picture_aspect": "black_and_white",
                                        "threshold": 60
                                        //                                "theme_color": "#ffffff"
                                ],
                                "coast": [
                                        "picture_aspect": "background_and_margin",
                                        "background_color": "#ffffff",
                                        "margin": "12%"
                                ],
                                "open_graph": [
                                        "picture_aspect": "background_and_margin",
                                        //                                "background_color": "#ffffff",
                                        "margin": "12%",
                                        "ratio": "1.91:1"
                                ],
                                "yandex_browser": [
                                        //                                "background_color": "background_color",
                                        "manifest": [
                                                "show_title": true,
                                                "version": "1.0"
                                        ]
                                ]
                        ],
                        "settings": [
                                "compression": "3",
                                "scaling_algorithm": "Mitchell",
                                "error_on_image_too_small": true,
                                "readme_file": true,
                                "html_code_file": false,
                                "use_path_as_is": false
                        ],
                        "versioning": [
                                "param_name": "ver",
                                "param_value": "15Zd8"
                        ]
                ]
        ]
    }
}



