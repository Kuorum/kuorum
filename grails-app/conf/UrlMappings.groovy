import grails.util.Environment
import kuorum.core.exception.KuorumException
import kuorum.core.model.AvailableLanguage
import org.springframework.security.access.AccessDeniedException

class UrlMappings {

    static excludes = ['/robots.txt']

    static List<String> RESERVED_PATHS = ['j_spring_security_facebook_redirect', 'register', 'login','js','images','css', 'fonts']
    static List<String> VALID_LANGUAGE_PATHS = AvailableLanguage.values().collect{it.locale.language}
	static mappings = {

        /**********************/
        /***** I18N URLs ******/
        /**********************/

        /**/
        /** NEW LANDIGNS **/
        name landingServices:       "/$lang/leaders-in-engagement"  {controller="redirect"; action= "redirect301"; newMapping='en_landingServices'; constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_landingServices:    "/en"                           (controller: "landing", action: "landingServices"){lang = "en"; mappingName="landingServices"}
        name es_landingServices:    "/es"                           (controller: "landing", action: "landingServices"){lang = "es"; mappingName="landingServices"}
                                    "/en/leaders-in-engagement"     {controller="redirect"; action= "redirect301"; newMapping='en_landingServices'}
                                    "/es/lideres-en-participacion"  {controller="redirect"; action= "redirect301"; newMapping='es_landingServices'}
                                    "/es/leaders-in-engagement"     {controller="redirect"; action= "redirect301"; newMapping='es_landingServices'}
                                    "/es/best-email-marketing"      {controller="redirect"; action= "redirect301"; newMapping='es_landingServices'}
                                    "/es/win-your-election"         {controller="redirect"; action= "redirect301"; newMapping='es_landingServices'}
                                    "/es/services/leaders"          {controller="redirect"; action= "redirect301"; newMapping='es_landingServices'}
                                    "/$lang/best-email-marketing"   {controller="redirect"; action= "redirect301"; newMapping='en_landingServices'; constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                    "/$lang/win-your-election"      {controller="redirect"; action= "redirect301"; newMapping='en_landingServices'; constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                    "/$lang/services/leaders"       {controller="redirect"; action= "redirect301"; newMapping='en_landingServices'; constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                    "/services/editors"             {controller="redirect"; action= "redirect301"; newMapping='en_landingServices'}

        name landingTechnology:     "/$lang/email-blasts-surveys-and-debates"       {controller="redirect"; action= "redirect301"; newMapping='en_landingTechnology';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_landingTechnology:  "/en/technology-for-debates-surveys-and-events" (controller: "landing", action: "landingTechnology"){lang="en"; mappingName="landingTechnology"}
        name es_landingTechnology:  "/es/tecnologia-para-debates-encuestas-eventos" (controller: "landing", action: "landingTechnology"){lang="es"; mappingName="landingTechnology"}
                                    "/es/email-blasts-surveys-and-debates"          {controller="redirect"; action= "redirect301"; newMapping='es_landingTechnology'}
                                    "/email-blasts-surveys-and-debates"             {controller="redirect"; action= "redirect301"; newMapping='en_landingTechnology'}
                                    "/technology"                                   {controller="redirect"; action= "redirect301"; newMapping='en_landingTechnology'}

        name landingEnterprise:     "/$lang/employee-engagement"                {controller="redirect"; action= "redirect301"; newMapping='en_landingEnterprise';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_landingEnterprise:  "/en/innovation-challenges-for-empoyees"    (controller: "landing", action: "landingEnterprise"){lang="en"; mappingName="landingEnterprise"}
        name es_landingEnterprise:  "/es/retos-innovacion-para-empleados"       (controller: "landing", action: "landingEnterprise"){lang="es"; mappingName="landingEnterprise"}
                                    "/es/employee-engagementment"               { controller="redirect"; action= "redirect301"; newMapping='es_landingEnterprise'}
                                    "/employee-engagement"                      { controller="redirect"; action= "redirect301"; newMapping='en_landingEnterprise'}
                                    "/enterprises"                              { controller="redirect"; action= "redirect301"; newMapping='en_landingEnterprise'}
                                    "/$lang/influential-brands"                 { controller="redirect"; action= "redirect301"; newMapping='en_landingEnterprise';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                    "/es/influential-brands"                    { controller="redirect"; action= "redirect301"; newMapping='es_landingEnterprise'}
                                    "/$lang/corporate-innovation"               { controller="redirect"; action= "redirect301"; newMapping='en_landingEnterprise';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                    "/es/corporate-innovation"                  { controller="redirect"; action= "redirect301"; newMapping='es_landingEnterprise'}

        name landingGovernments:    "/$lang/transparency-and-participation"         {controller="redirect"; action= "redirect301"; newMapping='en_landingGovernments';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_landingGovernments: "/en/tools-for-participation-and-transparency"  (controller: "landing", action: "landingGovernments"){lang="en"; mappingName="landingGovernments"}
        name es_landingGovernments: "/es/herramientas-de-participacion-ciudadana"   (controller: "landing", action: "landingGovernments"){lang="es"; mappingName="landingGovernments"}
                                    "/es/herramientas-participacion-y-transparencia"    {controller="redirect"; action= "redirect301"; newMapping='es_landingGovernments'}
                                    "/es/transparency-and-participation"            {controller="redirect"; action= "redirect301"; newMapping='es_landingGovernments'}
                                    "/transparency-and-participation"               {controller="redirect"; action= "redirect301"; newMapping='en_landingGovernments'}
                                    "/governments"                                  {controller="redirect"; action= "redirect301"; newMapping='en_landingGovernments'}
                                    "/en/services/government"                       {controller="redirect"; action= "redirect301"; newMapping='en_landingGovernments'}
                                    "/advocate-better"                              {controller="redirect"; action= "redirect301"; newMapping='en_landingGovernments'}
                                    "/$lang/services/who-should-i-vote-for"         {controller="redirect"; action= "redirect301"; newMapping='en_landingGovernments';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                    "/es/services/who-should-i-vote-for"            {controller="redirect"; action= "redirect301"; newMapping='es_landingGovernments'}
                                    "/services/politicians"                         {controller="redirect"; action= "redirect301"; newMapping='en_landingGovernments'}

        name landingOrganization:   "/$lang/fundraising-tools"      {controller="redirect"; action= "redirect301"; newMapping='en_landingOrganization';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_landingOrganization:"/en/fundraising-tools"         (controller: "landing", action: "landingOrganization"){lang="en"; mappingName="landingOrganization"}
        name es_landingOrganization:"/es/fideliza-socios-y-donantes"(controller: "landing", action: "landingOrganization"){lang="es"; mappingName="landingOrganization"}
                                    "/es/fundraising-tools"         {controller="redirect"; action= "redirect301"; newMapping='es_landingOrganization'}
                                    "/fundraising-tools"            {controller="redirect"; action= "redirect301"; newMapping='en_landingOrganization'}
                                    "/organizations"                {controller="redirect"; action= "redirect301"; newMapping='en_landingOrganization'}
                                    "/$lang/services/corporations"  {controller="redirect"; action= "redirect301"; newMapping='en_landingOrganization';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                    "/$lang/services/organizations" {controller="redirect"; action= "redirect301"; newMapping='en_landingOrganization';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                    "/es/services/organizations"    {controller="redirect"; action= "redirect301"; newMapping='es_landingOrganization'}
                                    "/es/services/corporations"     {controller="redirect"; action= "redirect301"; newMapping='es_landingOrganization'}
                                    "/$lang/influential-brands"     {controller="redirect"; action= "redirect301"; newMapping='en_landingOrganization';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                    "/es/influential-brands"        {controller="redirect"; action= "redirect301"; newMapping='es_landingOrganization'}
                                    "/kuorum/organizaciones"        {controller="redirect"; action= "redirect301"; newMapping='es_landingOrganization'}

        name landingPrices:         "/$lang/prices"                 {controller="redirect"; action= "redirect301"; newMapping='en_landingPrices';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_landingPrices:      "/en/prices"                    (controller: "dashboard", action: "landingPrices"){lang="en"; mappingName="landingPrices"}
        name es_landingPrices:      "/es/precios"                   (controller: "dashboard", action: "landingPrices"){lang="es"; mappingName="landingPrices"}
                                    "/es/prices"                    {controller="redirect"; action= "redirect301"; newMapping='es_landingPrices'}
                                    "/prices"                       {controller="redirect"; action= "redirect301"; newMapping='en_landingPrices'}

        name landingCaseStudy:      "/$lang/successful-stories"                                                     {controller="redirect"; action= "redirect301"; newMapping='en_landingCaseStudy';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_landingCaseStudy:   "/en/successful-stories"                                                        (controller: "landing", action: "landingCaseStudy"){lang="en"; mappingName="landingCaseStudy"}
        name es_landingCaseStudy:   "/es/casos-de-exito"                                                            (controller: "landing", action: "landingCaseStudy"){lang="es"; mappingName="landingCaseStudy"}
                                    "/es/successful-stories"                                                        {controller="redirect"; action= "redirect301"; newMapping='es_landingCaseStudy'}
                                    "/successful-stories"                                                           {controller="redirect"; action= "redirect301"; newMapping='en_landingCaseStudy'}

        name landingCaseStudy001:   "/$lang/successful-stories/toledo-city-council-digitalises-participation"       {controller="redirect"; action= "redirect301"; newMapping='en_landingCaseStudy001';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_landingCaseStudy001:"/en/successful-stories/toledo-city-council-digitalises-participation"          (controller: "landing", action: "caseStudy001"){lang="en"; mappingName="landingCaseStudy001"}
        name es_landingCaseStudy001:"/es/casos-de-exito/ayuntamiento-toledo-digitaliza-la-participacion"            (controller: "landing", action: "caseStudy001"){lang="es"; mappingName="landingCaseStudy001"}
                                    "/successful-stories/toledo-city-council-digitalises-participation"             {controller="redirect"; action= "redirect301"; newMapping='en_landingCaseStudy001'}

        name landingCaseStudy002:   "/successful-stories/national-media-group-gamifies-user-experience"             {controller="redirect"; action= "redirect301"; newMapping='en_landingCaseStudy002'}
        name en_landingCaseStudy002:"/en/successful-stories/national-media-group-gamifies-user-experience"          (controller: "landing", action: "caseStudy002"){lang="en"; mappingName="landingCaseStudy002"}
        name es_landingCaseStudy002:"/es/casos-de-exito/unidad-editorial-gamifica-la-experiencia-de-usuario"        (controller: "landing", action: "caseStudy002"){lang="es"; mappingName="landingCaseStudy002"}
                                    "/$lang/successful-stories/national-media-group-gamifies-user-experience"       {controller="redirect"; action= "redirect301"; newMapping='en_landingCaseStudy002';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}

        name landingCaseStudy003:   "/successful-stories/oxfam-gives-visibility-to-its-advocacy-campaigns"          { controller="redirect"; action= "redirect301"; newMapping='en_landingCaseStudy003'}
        name en_landingCaseStudy003:"/en/successful-stories/oxfam-gives-visibility-to-its-advocacy-campaigns"       (controller: "landing", action: "caseStudy003"){lang="en"; mappingName="landingCaseStudy003"}
        name es_landingCaseStudy003:"/es/casos-de-exito/oxfam-da-visibilidad-a-sus-campanas-de-incidencia"          (controller: "landing", action: "caseStudy003"){lang="es"; mappingName="landingCaseStudy003"}
                                    "/$lang/successful-stories/oxfam-gives-visibility-to-its-advocacy-campaigns"    { controller="redirect"; action= "redirect301"; newMapping='en_landingCaseStudy003'; constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}

        name landingCaseStudy004:   "/successful-stories/kaunas-citizens-design-their-cycle-lane"                   { controller="redirect"; action= "redirect301"; newMapping='en_landingCaseStudy004'}
        name en_landingCaseStudy004:"/en/successful-stories/kaunas-citizens-design-their-cycle-lane"                (controller: "landing", action: "caseStudy004"){lang="en"; mappingName="landingCaseStudy004"}
        name es_landingCaseStudy004:"/es/casos-de-exito/los-ciudadanos-de-kaunas-disenan-su-carril-bici"            (controller: "landing", action: "caseStudy004"){lang="es"; mappingName="landingCaseStudy004"}
                                    "/$lang/successful-stories/kaunas-citizens-design-their-cycle-lane"             {controller="redirect"; action= "redirect301"; newMapping='en_landingCaseStudy004';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}

        name landingCaseStudy005:   "/successful-stories/community-engagement-guide-to-figh-depopulation"                   { controller="redirect"; action= "redirect301"; newMapping='en_landingCaseStudy005'}
        name en_landingCaseStudy005:"/en/successful-stories/community-engagement-guide-to-figh-depopulation"                (controller: "landing", action: "caseStudy005"){lang="en"; mappingName="landingCaseStudy005"}
        name es_landingCaseStudy005:"/es/casos-de-exito/guia-de-participacion-ciudadana-para-luchar-contra-la-despoblacion"      (controller: "landing", action: "caseStudy005"){lang="es"; mappingName="landingCaseStudy005"}
                                    "/$lang/successful-stories/community-engagement-guide-to-figh-depopulation"             {controller="redirect"; action= "redirect301"; newMapping='en_landingCaseStudy005';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}

        name home:              "/"         { controller="redirect"; action= "redirect301"; newMapping='en_home'}
        name en_home:           "/en"       (controller: "landing", action:"landingServices"){lang="en"; mappingName="home"}
        name es_home:           "/es"       (controller: "landing", action:"landingServices"){lang="es"; mappingName="home"}
                                "/$lang"    { controller="redirect"; action= "redirect301"; newMapping='en_home';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}

        name footerAboutKuorum:     "/what-is-kuorum"       {controller="redirect"; action= "redirect301"; newMapping='en_footerAboutKuorum'}
        name en_footerAboutKuorum:  "/en/what-is-kuorum"    (controller: "footer", action: "footerAboutUs"){lang="en"; mappingName="footerAboutKuorum"}
        name es_footerAboutKuorum:  "/es/que-es-kuorum"     (controller: "footer", action: "footerAboutUs"){lang="es"; mappingName="footerAboutKuorum"}
                                    "/es/what-is-kuorum"             {controller="redirect"; action= "redirect301"; newMapping='es_footerAboutKuorum'}
                                    "/$lang/what-is-kuorum"          {controller="redirect"; action= "redirect301"; newMapping='en_footerAboutKuorum';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                    "/es/services/what-is-kuorum" {controller="redirect"; action= "redirect301"; newMapping='es_footerAboutKuorum'}
                                    "/$lang/services/what-is-kuorum" {controller="redirect"; action= "redirect301"; newMapping='en_footerAboutKuorum';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}

        name footerContactUs:       "/contact"          {controller="redirect"; action= "redirect301"; newMapping='en_footerContactUs'}
        name en_footerContactUs:    "/en/contact"       (controller: "footer", action: "footerContactUs"){lang="en"; mappingName="footerContactUs"}
        name es_footerContactUs:    "/es/contacta"      (controller: "footer", action: "footerContactUs"){lang="es"; mappingName="footerContactUs"}
                                    "/es/contact"       {controller="redirect"; action= "redirect301"; newMapping='es_footerContactUs'}
                                    "/$lang/contact"    {controller="redirect"; action= "redirect301"; newMapping='en_footerContactUs';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}

        name footerOurTeam:         "/team"         {controller="redirect"; action= "redirect301"; newMapping='en_footerOurTeam'}
        name en_footerOurTeam:      "/en/team"      (controller: "footer", action: "footerOurTeam"){lang="en"; mappingName="footerOurTeam"}
        name es_footerOurTeam:      "/es/equipo"    (controller: "footer", action: "footerOurTeam"){lang="es"; mappingName="footerOurTeam"}
                                    "/es/team"           {controller="redirect"; action= "redirect301"; newMapping='es_footerOurTeam'}
                                    "/es/about/our-team" {controller="redirect"; action= "redirect301"; newMapping='es_footerOurTeam'}
                                    "/$lang/team"        {controller="redirect"; action= "redirect301"; newMapping='en_footerOurTeam';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                    "/$lang/about/our-team"{controller="redirect"; action= "redirect301"; newMapping='en_footerOurTeam';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                    "/kuorum/our-team"   {controller="redirect"; action= "redirect301"; newMapping='en_footerOurTeam'}
                                    "/kuorum/nuestro-equipo"   {controller="redirect"; action= "redirect301"; newMapping='es_footerOurTeam'}

        name footerUserGuides:      "/$lang/user-guides"    {controller="redirect"; action= "redirect301"; newMapping='footerUserGuides';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_footerUserGuides:   "/en/user-guides"       (controller: "footer", action: "footerUserGuides"){lang="en"; mappingName="footerUserGuides"}
        name es_footerUserGuides:   "/es/guias-de-usuario"  (controller: "footer", action: "footerUserGuides"){lang="es"; mappingName="footerUserGuides"}
                                    "/user-guides"          {controller="redirect"; action= "redirect301"; newMapping='en_footerUserGuides'}
                                    "/es/user-guides"       {controller="redirect"; action= "redirect301"; newMapping='es_footerUserGuides'}
                                    "/kuorum/guia-del-usuario"       {controller="redirect"; action= "redirect301"; newMapping='es_footerUserGuides'}

        name footerPress:           "/$lang/press-and-media"{controller="redirect"; action= "redirect301"; newMapping='en_footerPress';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_footerPress:        "/en/press-services"    (controller: "footer", action: "footerPress"){lang="en"; mappingName="footerPress"}
        name es_footerPress:        "/es/servicios-prensa"  (controller: "footer", action: "footerPress"){lang="es"; mappingName="footerPress"}
                                    "/es/press-and-media"   {controller="redirect"; action= "redirect301"; newMapping='es_footerPress'}
                                    "/press-and-media"      {controller="redirect"; action= "redirect301"; newMapping='en_footerPress'}
                                    "/press/widget"         {controller="redirect"; action= "redirect301"; newMapping='en_footerPress'}
                                    "/$lang/press"          {controller="redirect"; action= "redirect301"; newMapping='en_footerPress';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                    "/$lang/press/widget"   {controller="redirect"; action= "redirect301"; newMapping='en_footerPress';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                    "/es/press"             {controller="redirect"; action= "redirect301"; newMapping='es_footerPress'}
                                    "/es/press/widget"      {controller="redirect"; action= "redirect301"; newMapping='es_footerPress'}

        name footerHistory:         "/$lang/our-story"      {controller="redirect"; action= "redirect301"; newMapping='en_footerHistory';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_footerHistory:      "/en/our-story"         (controller: "footer", action: "footerHistory"){lang="en"; mappingName="footerHistory"}
        name es_footerHistory:      "/es/quienes-somos"     (controller: "footer", action: "footerHistory"){lang="es"; mappingName="footerHistory"}
                                    "/es/quiens-somos"      {controller="redirect"; action= "redirect301"; newMapping='es_footerHistory'}
                                    "/es/our-story"         {controller="redirect"; action= "redirect301"; newMapping='es_footerHistory'}
                                    "/en/about/our-story"   {controller="redirect"; action= "redirect301"; newMapping='en_footerHistory'}
                                    "/es/about/our-story"   {controller="redirect"; action= "redirect301"; newMapping='es_footerHistory'}
                                    "/our-story"            {controller="redirect"; action= "redirect301"; newMapping='en_footerHistory'}
                                    "/$lang/about/our-story"{controller="redirect"; action= "redirect301"; newMapping='en_footerHistory';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        /**/
        /*** CUSTOM BLOG ARTICLES ***/
        name footerBlog:            "/$lang/blog"   {controller="redirect"; action= "redirect301"; newMapping='en_footerBlog';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_footerBlog:         "/en/blog"      (controller: "footer", action: "footerBlog"){lang="en"; mappingName="footerBlog"}
        name es_footerBlog:         "/es/blog"      (controller: "footer", action: "footerBlog"){lang="es"; mappingName="footerBlog"}
                                    "/blog"         {controller="redirect"; action= "redirect301"; newMapping='en_footerBlog'}
                                    "/es/blog/2017/03/14/por-que-no-puedes-vivir-sin-employer-branding"     {controller="redirect"; action= "redirect301"; newMapping='en_footerBlog'}
                                    "/es/blog/2015/09/29/yolanda-roman-la-politica-es-el-arte-de-poner-de"  {controller="redirect"; action= "redirect301"; newMapping='es_footerBlog'}
                                    "/es/blog/2014/08/20/que-puedo-hacer-para-cambiar-mi-pais-10-cosas"     {controller="redirect"; action= "redirect301"; newMapping='es_footerBlog'}
                                    "/es/blog/2015/09/19/astrid-alemany-las-herramientas-de-participacion"  {controller="redirect"; action= "redirect301"; newMapping='es_footerBlog'}
                                    "/es/blog/2015/09/19/nacho-corredor-es-falso-que-los-jovenes-no-esten"  {controller="redirect"; action= "redirect301"; newMapping='es_footerBlog'}
                                    "/es/blog/2016/11/11/mitos-y-mentiras-sobre-campanas-politicas-online"  {controller="redirect"; action= "redirect301"; newMapping='es_footerBlog'}
                                    "/es/blog/2015/09/21/gloria-ostos-el-consultor-politico-no-puede"       {controller="redirect"; action= "redirect301"; newMapping='es_footerBlog'}
                                    "/es/blog/2013/12/04/seria-muy-sano-que-alguien-pidiera-disculpas"      {controller="redirect"; action= "redirect301"; newMapping='es_footerBlog'}
                                    "/es/blog/2015/08/31/rafa-rubio-la-comunicacion-politica-tiene-un"      {controller="redirect"; action= "redirect301"; newMapping='es_footerBlog'}
                                    "/es/blog/2014/09/17/necesitamos-leyes-afincadas-en-la-realidad"        {controller="redirect"; action= "redirect301"; newMapping='es_footerBlog'}
                                    "/es/blog/2015/05/07/no-me-conformo-con-vivir-en-una-sociedad-con"      {controller="redirect"; action= "redirect301"; newMapping='es_footerBlog'}
                                    "/es/blog/2014/05/11/fabio-gandara-pumar"                               {controller="redirect"; action= "redirect301"; newMapping='es_footerBlog'}
                                    "/es/blog/2013/09/24/the-revival-of-democratic-politics-will-come-from" {controller="redirect"; action= "redirect301"; newMapping='es_footerBlog'}
                                    "/es/blog/2014/10/16/aportar-matices-a-esa-critica-de-todos-los"        {controller="redirect"; action= "redirect301"; newMapping='es_footerBlog'}
                                    "/es/blog/2015/09/08/angela-paloma-las-sociedades-avanzan-a-mayor"      {controller="redirect"; action= "redirect301"; newMapping='es_footerBlog'}
                                    "/es/blog/2015/06/16/hay-que-cortar-de-raiz-la-separacion-que-hay"      {controller="redirect"; action= "redirect301"; newMapping='es_footerBlog'}
                                    "/es/blog/2015/09/19/maria-rosa-rotondo-el-lobby-es-alinear-los"        {controller="redirect"; action= "redirect301"; newMapping='es_footerBlog'}
                                    "/en/blog/2016/09/12/kuorums-web-whiz-antonio-martos-ortega"            {controller="redirect"; action= "redirect301"; newMapping='en_footerBlog'}
                                    "/es/blog/2015/09/19/david-redoli-si-un-politico-no-comunica-bien/"     {controller="redirect"; action= "redirect301"; newMapping='en_footerBlog'}
                                    "/en/blog/2016/09/19/optimise-your-kuorum-profile/"                     {controller="redirect"; action= "redirect301"; newMapping='en_footerBlog'}
                                    "/en/blog/2016/11/11/myths-and-lies-about-online-political-campaigns/"  {controller="redirect"; action= "redirect301"; newMapping='en_footerBlog'}

        name footerBlog001:         "/$lang/blog/ii-international-conference-on-online-political-communication" {controller="redirect"; action= "redirect301"; newMapping='en_footerBlog001';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_footerBlog001:      "/en/blog/ii-international-conference-on-online-political-communication"    (controller: "footer", action: "footerBlog001"){lang="en"; mappingName="footerBlog001"}
        name es_footerBlog001:      "/es/blog/ii-conferencia-internacional-de-comunicacion-politica-digital"    (controller: "footer", action: "footerBlog001"){lang="es"; mappingName="footerBlog001"}
                                    "/blog/ii-international-conference-on-online-political-communication"       {controller="redirect"; action= "redirect301"; newMapping='en_footerBlog001'}
        /* END CUSTOM BLOG ARTICLES */

        name footerPrivacyPolicy:   "/$lang/legal/privacy-policy"       {controller="redirect"; action= "redirect301"; newMapping='en_footerPrivacyPolicy';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_footerPrivacyPolicy:"/en/legal/privacy-policy"          (controller:"footer", action: "privacyPolicy"){lang="en"; mappingName="footerPrivacyPolicy"}
        name es_footerPrivacyPolicy:"/es/legal/politica-privacidad"     (controller:"footer", action: "privacyPolicy"){lang="es"; mappingName="footerPrivacyPolicy"}
                                    "/legal/privacy-policy"             {controller="redirect"; action= "redirect301"; newMapping='en_footerPrivacyPolicy'}
                                    "/es/legal/privacy-policy"          {controller="redirect"; action= "redirect301"; newMapping='es_footerPrivacyPolicy'}
                                    "/legal"                            {controller="redirect"; action= "redirect301"; newMapping='en_footerPrivacyPolicy'}
                                    "/politica-privacidad"              {controller="redirect"; action= "redirect301"; newMapping='es_footerPrivacyPolicy'}
                                    "/kuorum/politica-privacidad"       {controller="redirect"; action= "redirect301"; newMapping='es_footerPrivacyPolicy'}

        name footerTermsUse:        "/$lang/legal/terms-of-use"         {controller="redirect"; action= "redirect301"; newMapping='en_footerTermsUse'}
        name en_footerTermsUse:     "/en/legal/terms-of-use"            (controller:"footer", action: "termsUse"){lang="en"; mappingName="footerTermsUse"}
        name es_footerTermsUse:     "/es/legal/condiciones-de-uso"      (controller:"footer", action: "termsUse"){lang="es"; mappingName="footerTermsUse"}
                                    "/es/legal/terms-of-use"            {controller="redirect"; action= "redirect301"; newMapping='es_footerTermsUse'}
                                    "/legal/terms-of-use"               {controller="redirect"; action= "redirect301"; newMapping='en_footerTermsUse'}
                                    "/condiciones-de-uso"               {controller="redirect"; action= "redirect301"; newMapping='es_footerTermsUse'}
                                    "/kuorum/condiciones-de-uso"        {controller="redirect"; action= "redirect301"; newMapping='es_footerTermsUse'}

        name register:              "/$lang/sign-up"    {controller="redirect"; action= "redirect301"; newMapping='en_register';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_register:           "/en/sign-up"       (controller: "register"){action = [GET:"index", POST:"register"]}{lang="en"; mappingName="register"}
        name es_register:           "/es/registro"      (controller: "register"){action = [GET:"index", POST:"register"]}{lang="es"; mappingName="register"}
                                    "/es/sign-up"       {controller="redirect"; action= "redirect301"; newMapping='es_register'}
                                    "/sign-up"          {controller="redirect"; action= "redirect301"; newMapping='en_register'}
                                    "/registro"         {controller="redirect"; action= "redirect301"; newMapping='es_register'}

        name ajaxRegister:          "/ajax/sign-up"     (controller: "register", action:"ajaxRegister")
        name ajaxRegisterCheckEmail:"/ajax/sign-up/checkEmail"(controller: "register", action:"checkEmail")
        name ajaxRegisterRRSSOAuth: "/ajax/sign-up/rrssOAuth"(controller: "register", action:"registerRRSSOAuthAjax")

        name registerPressKit:      "/$lang/sign-up/pressKit"   {controller="redirect"; action= "redirect301"; newMapping='en_registerPressKit';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_registerPressKit:   "/en/press-kit"             (controller: "register",action:"downloadCaseStudy"){lang="en"; mappingName="registerPressKit"}
        name es_registerPressKit:   "/es/press-kit"             (controller: "register",action:"downloadCaseStudy"){lang="es"; mappingName="registerPressKit"}
                                    "/en/sign-up/pressKit"      {controller="redirect"; action= "redirect301"; newMapping='en_registerPressKit'}
                                    "/es/sign-up/pressKit"      {controller="redirect"; action= "redirect301"; newMapping='es_registerPressKit'}

        name registerSuccess:       "/$lang/sign-up/success"    {controller="redirect"; action= "redirect301"; newMapping='en_registerSuccess';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_registerSuccess:    "/en/sign-up/success"       (controller: "register",action:"registerSuccess"){lang="en"; mappingName="registerSuccess"}
        name es_registerSuccess:    "/es/registro/satisfactorio"(controller: "register",action:"registerSuccess"){lang="es"; mappingName="registerSuccess"}

        name registerPassword:      "/$lang/sign-up/establece-password" {controller="redirect"; action= "redirect301"; newMapping='en_registerPassword';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_registerPassword:   "/en/sign-up/set-password"          (controller: "register", action:"selectMyPassword"){lang="en"; mappingName="registerPassword"}
        name es_registerPassword:   "/es/registro/establece-password"   (controller: "register", action:"selectMyPassword"){lang="es"; mappingName="registerPassword"}
                                    "/registro/establece-password"      {controller="redirect"; action= "redirect301"; newMapping='en_registerPassword'}

        name registerResendMail:    "/$lang/sign-up/no-valid"   {controller="redirect"; action= "redirect301"; newMapping='en_registerResendMail';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_registerResendMail: "/en/sign-up/no-valid"      (controller: "register"){action=[GET:"resendRegisterVerification", POST:"resendVerification"];lang="en"; mappingName="registerResendMail"}
        name es_registerResendMail: "/es/registro/no-verificado"(controller: "register"){action=[GET:"resendRegisterVerification", POST:"resendVerification"];lang="es"; mappingName="registerResendMail"}
                                    "/registro/no-verificado"   {controller="redirect"; action= "redirect301"; newMapping='en_registerResendMail'}

        name resetPassword:         "/$lang/sign-in/recover-password"   {controller="redirect"; action= "redirect301"; newMapping='en_resetPassword';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_resetPassword:      "/en/sign-in/recover-password"      (controller: "register"){action=[GET:"forgotPassword", POST:"forgotPasswordPost"];lang="en"; mappingName="resetPassword"}
        name es_resetPassword:      "/es/registro/password-olvidado"    (controller: "register"){action=[GET:"forgotPassword", POST:"forgotPasswordPost"];lang="es"; mappingName="resetPassword"}

        name registerVerifyAccount:         "/register/verifyRegistration"      {controller="redirect";  action= "redirect301"; newMapping='en_registerVerifyAccount';}
        name en_registerVerifyAccount:      "/en/register/verify-registration"  (controller: "register", action:"verifyRegistration"){lang="en"; mappingName="registerVerifyAccount"}
        name es_registerVerifyAccount:      "/es/registro/verificar-cuenta"     (controller: "register", action:"verifyRegistration"){lang="es"; mappingName="registerVerifyAccount"}

        name validateResetPasswordAjax:"/ajax/forgot-password" (controller:"register", action: "ajaxValidationForgotPassword")

        name resetPasswordSent:     "/$lang/sign-up/verification-sent"  {controller="redirect"; action= "redirect301"; newMapping='en_resetPasswordSent';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_resetPasswordSent:  "/en/sign-up/verification-sent"     (controller: "register", action:"forgotPasswordSuccess"){lang="en"; mappingName="resetPasswordSent"}
        name es_resetPasswordSent:  "/es/registro/enviada-verificacion" (controller: "register", action:"forgotPasswordSuccess"){lang="es"; mappingName="resetPasswordSent"}


        name resetPasswordChange:   "/$lang/sign-up/change-pass"    {controller="redirect"; action= "redirect301"; newMapping='en_resetPasswordChange';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_resetPasswordChange:"/en/sign-up/change-password"   (controller: "register"){action=[GET:"resetPassword", POST:"resetPassword"];lang="en"; mappingName="resetPasswordChange"}
        name es_resetPasswordChange:"/es/registro/cambiar-password" (controller: "register"){action=[GET:"resetPassword", POST:"resetPassword"];lang="es"; mappingName="resetPasswordChange"}

        name ajaxRequestADemo:          "/ajax/requestADemo"(controller: "register", action: "requestADemo")


        name login:     "/$lang/log-in" {controller="redirect"; action= "redirect301"; newMapping='en_login';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_login:  "/en/log-in"    (controller:"login", action:"index"){lang="en"; mappingName="login"}
        name es_login:  "/es/entrar"    (controller:"login", action:"index"){lang="es"; mappingName="login"}
                        "/log-in"       {controller="redirect"; action= "redirect301"; newMapping='en_login'}
                        "/es/log-in"    {controller="redirect"; action= "redirect301"; newMapping='es_login'}
                        "/entrar"       {controller="redirect"; action= "redirect301"; newMapping='es_login'}

        name ajaxLoginCheck:"/ajax/checkLogin"  (controller:"login", action:"checkEmailAndPass")
        name ajaxLoginModal:"/ajax/login/modal-auth"  (controller:"login", action:"modalAuth")

        name loginAuth:     "/$lang/sign-in"    {controller="redirect"; action= "redirect301"; newMapping='en_loginAuth';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_loginAuth:  "/en/sign-in"       (controller:"login", action:"auth"){lang="en"; mappingName="loginAuth"}
        name es_loginAuth:  "/es/entra"         (controller:"login", action:"auth"){lang="es"; mappingName="loginAuth"}
                            "/sign-in"          {controller="redirect"; action= "redirect301"; newMapping='en_loginAuth'}
                            "/es/sign-in"       {controller="redirect"; action= "redirect301"; newMapping='es_loginAuth'}
                            "/autenticarse"     {controller="redirect"; action= "redirect301"; newMapping='es_loginAuth'}
                            "/login/auth"       {controller="redirect"; action= "redirect301"; newMapping='en_loginAuth'}

        name authError:     "/login/authfail"       (controller:"login", action:"authfail")

        name loginFull:     "/$lang/confirmar-usuario"  {controller="redirect"; action= "redirect301"; newMapping='en_loginFull';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_loginFull:  "/en/check-user"            (controller:"login", action:"full"){lang="en"; mappingName="loginFull"}
        name es_loginFull:  "/es/confirmar-usuario"     (controller:"login", action:"full"){lang="es"; mappingName="loginFull"}
                            "/confirmar-usuario"        {controller="redirect"; action= "redirect301"; newMapping='en_loginFull'}

        name logout:    "/logout"       (controller:"logout", action:"index")
                        "/salir"        {controller="redirect"; action= "redirect301"; newMapping='logout'}

        name searcherSearch:        "/$lang/search"     {controller="redirect"; action= "redirect301"; newMapping='en_searcherSearch';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_searcherSearch:     "/en/search/$word?" (controller: "search", action:"search"){lang="en"; mappingName="searcherSearch"}
        name es_searcherSearch:     "/es/buscar/$word?" (controller: "search", action:"search"){lang="es"; mappingName="searcherSearch"}
                                    "/search"           {controller="redirect"; action= "redirect301"; newMapping='en_searcherSearch'}
                                    "/es/search"        {controller="redirect"; action= "redirect301"; newMapping='es_searcherSearch'}
                                    "/buscar"           {controller="redirect"; action= "redirect301"; newMapping='es_searcherSearch'}

        name searcherSearchByCAUSE:   "/$lang/search/cause"          {controller="redirect"; action= "redirect301"; newMapping='en_searcherSearchByCAUSE';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_searcherSearchByCAUSE:"/en/search/cause/$word?"      (controller: "search", action:"search"){lang="en"; searchType="CAUSE"; mappingName="searcherSearchByCAUSE"}
        name es_searcherSearchByCAUSE:"/es/buscar/causa/$word?"      (controller: "search", action:"search"){lang="es"; searchType="CAUSE"; mappingName="searcherSearchByCAUSE"}

        name searcherSearchByREGION:   "/$lang/search/users/from/$word?"{controller="redirect"; action= "redirect301"; newMapping='en_searcherSearchByREGION';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_searcherSearchByREGION:"/en/search/from/$word?"         (controller: "search", action:"search"){lang="en"; searchType="REGION"; mappingName="searcherSearchByREGION"}
        name es_searcherSearchByREGION:"/es/buscar/en/$word?"           (controller: "search", action:"search"){lang="es"; searchType="REGION"; mappingName="searcherSearchByREGION"}

        name searcherSearchKUORUM_USER:     "/$lang/search/users"          {controller="redirect"; action= "redirect301"; newMapping='en_searcherSearchKUORUM_USER';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_searcherSearchKUORUM_USER:  "/en/search/users/$word?"      (controller: "search", action:"search"){lang="en"; type="KUORUM_USER"; mappingName="searcherSearchKUORUM_USER"}
        name es_searcherSearchKUORUM_USER:  "/es/buscar/usuarios/$word?"   (controller: "search", action:"search"){lang="es"; type="KUORUM_USER"; mappingName="searcherSearchKUORUM_USER"}

        name searcherSearchKUORUM_USERByCAUSE:   "/$lang/search/users/cause/$word?"          {controller="redirect"; action= "redirect301"; newMapping='en_searcherSearchKUORUM_USERByCAUSE';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_searcherSearchKUORUM_USERByCAUSE:"/en/search/users/cause/$word?"             (controller: "search", action:"search"){lang="en"; searchType="CAUSE";type="KUORUM_USER"; mappingName="searcherSearchKUORUM_USERByCAUSE"}
        name es_searcherSearchKUORUM_USERByCAUSE:"/es/buscar/usuarios/causa/$word?"          (controller: "search", action:"search"){lang="es"; searchType="CAUSE";type="KUORUM_USER"; mappingName="searcherSearchKUORUM_USERByCAUSE"}

        name searcherSearchKUORUM_USERByREGION:   "/$lang/search/users/from/$word?"          {controller="redirect"; action= "redirect301"; newMapping='en_searcherSearchKUORUM_USERByREGION';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_searcherSearchKUORUM_USERByREGION:"/en/search/users/from/$word?"             (controller: "search", action:"search"){lang="en"; searchType="REGION";type="KUORUM_USER"; mappingName="searcherSearchKUORUM_USERByREGION"}
        name es_searcherSearchKUORUM_USERByREGION:"/es/buscar/usuarios/en/$word?"          (controller: "search", action:"search"){lang="es"; searchType="REGION";type="KUORUM_USER"; mappingName="searcherSearchKUORUM_USERByREGION"}

        name searcherSearchPOST:   "/$lang/search/post"                {controller="redirect"; action= "redirect301"; newMapping='en_searcherSearchPOST';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_searcherSearchPOST:"/en/search/post/$word?"            (controller: "search", action:"search"){lang="en"; type="POST"; mappingName="searcherSearchPOST"}
        name es_searcherSearchPOST:"/es/buscar/publicacion/$word?"     (controller: "search", action:"search"){lang="es"; type="POST"; mappingName="searcherSearchPOST"}

        name searcherSearchPOSTByCAUSE:   "/$lang/search/post/cause/$word?"          {controller="redirect"; action= "redirect301"; newMapping='en_searcherSearchPOSTByCAUSE';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_searcherSearchPOSTByCAUSE:"/en/search/post/cause/$word?"             (controller: "search", action:"search"){lang="en"; searchType="CAUSE";type="POST"; mappingName="searcherSearchPOSTByCAUSE"}
        name es_searcherSearchPOSTByCAUSE:"/es/buscar/publicacion/causa/$word?"      (controller: "search", action:"search"){lang="es"; searchType="CAUSE";type="POST"; mappingName="searcherSearchPOSTByCAUSE"}

        name searcherSearchPOSTByREGION:   "/$lang/search/post/from/$word?"          {controller="redirect"; action= "redirect301"; newMapping='en_searcherSearchPOSTByREGION';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_searcherSearchPOSTByREGION:"/en/search/post/from/$word?"             (controller: "search", action:"search"){lang="en"; searchType="REGION";type="POST"; mappingName="searcherSearchPOSTByREGION"}
        name es_searcherSearchPOSTByREGION:"/es/buscar/publicacion/en/$word?"      (controller: "search", action:"search"){lang="es"; searchType="REGION";type="POST"; mappingName="searcherSearchPOSTByREGION"}

        name searcherSearchDEBATE:   "/$lang/search/debate"         {controller="redirect"; action= "redirect301"; newMapping='en_searcherSearchDEBATE';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_searcherSearchDEBATE:"/en/search/debate/$word?"     (controller: "search", action:"search"){lang="en"; type="DEBATE"; mappingName="searcherSearchDEBATE"}
        name es_searcherSearchDEBATE:"/es/buscar/debate/$word?"     (controller: "search", action:"search"){lang="es"; type="DEBATE"; mappingName="searcherSearchDEBATE"}

        name searcherSearchDEBATEByCAUSE:   "/$lang/search/debate/cause/$word?"          {controller="redirect"; action= "redirect301"; newMapping='en_searcherSearchDEBATEByCAUSE';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_searcherSearchDEBATEByCAUSE:"/en/search/debate/cause/$word?"             (controller: "search", action:"search"){lang="en"; searchType="CAUSE";type="DEBATE"; mappingName="searcherSearchDEBATEByCAUSE"}
        name es_searcherSearchDEBATEByCAUSE:"/es/buscar/debate/causa/$word?"          (controller: "search", action:"search"){lang="es"; searchType="CAUSE";type="DEBATE"; mappingName="searcherSearchDEBATEByCAUSE"}

        name searcherSearchDEBATEByREGION:   "/$lang/search/debate/from/$word?"          {controller="redirect"; action= "redirect301"; newMapping='en_searcherSearchDEBATEByREGION';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_searcherSearchDEBATEByREGION:"/en/search/debate/from/$word?"             (controller: "search", action:"search"){lang="en"; searchType="REGION";type="DEBATE"; mappingName="searcherSearchDEBATEByREGION"}
        name es_searcherSearchDEBATEByREGION:"/es/buscar/debate/en/$word?"          (controller: "search", action:"search"){lang="es"; searchType="REGION";type="DEBATE"; mappingName="searcherSearchDEBATEByREGION"}


        name searcherSearchEVENT:   "/$lang/search/event"         {controller="redirect"; action= "redirect301"; newMapping='en_searcherSearchEVENT';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_searcherSearchEVENT:"/en/search/event/$word?"     (controller: "search", action:"search"){lang="en"; type="EVENT"; mappingName="searcherSearchEVENT"}
        name es_searcherSearchEVENT:"/es/buscar/evento/$word?"     (controller: "search", action:"search"){lang="es"; type="EVENT"; mappingName="searcherSearchEVENT"}

        name searcherSearchEVENTByCAUSE:   "/$lang/search/event/cause/$word?"          {controller="redirect"; action= "redirect301"; newMapping='en_searcherSearchEVENTByCAUSE';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_searcherSearchEVENTByCAUSE:"/en/search/event/cause/$word?"             (controller: "search", action:"search"){lang="en"; searchType="CAUSE";type="EVENT"; mappingName="searcherSearchEVENTByCAUSE"}
        name es_searcherSearchEVENTByCAUSE:"/es/buscar/evento/causa/$word?"          (controller: "search", action:"search"){lang="es"; searchType="CAUSE";type="EVENT"; mappingName="searcherSearchEVENTByCAUSE"}

        name searcherSearchEVENTByREGION:   "/$lang/search/event/from/$word?"          {controller="redirect"; action= "redirect301"; newMapping='en_searcherSearchEVENTByREGION';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_searcherSearchEVENTByREGION:"/en/search/event/from/$word?"             (controller: "search", action:"search"){lang="en"; searchType="REGION";type="EVENT"; mappingName="searcherSearchEVENTByREGION"}
        name es_searcherSearchEVENTByREGION:"/es/buscar/evento/en/$word?"          (controller: "search", action:"search"){lang="es"; searchType="REGION";type="EVENT"; mappingName="searcherSearchEVENTByREGION"}

        name searcherSearchSURVEY:   "/$lang/search/survey"         {controller="redirect"; action= "redirect301"; newMapping='en_searcherSearchEVENT';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_searcherSearchSURVEY:"/en/search/survey/$word?"     (controller: "search", action:"search"){lang="en"; type="SURVEY"; mappingName="searcherSearchSURVEY"}
        name es_searcherSearchSURVEY:"/es/buscar/encuesta/$word?"     (controller: "search", action:"search"){lang="es"; type="SURVEY"; mappingName="searcherSearchSURVEY"}

        name searcherSearchSURVEYByCAUSE:   "/$lang/search/survey/cause/$word?"          {controller="redirect"; action= "redirect301"; newMapping='en_searcherSearchSURVEYByCAUSE';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_searcherSearchSURVEYByCAUSE:"/en/search/survey/cause/$word?"             (controller: "search", action:"search"){lang="en"; searchType="CAUSE";type="SURVEY"; mappingName="searcherSearchSURVEYByCAUSE"}
        name es_searcherSearchSURVEYByCAUSE:"/es/buscar/encuesta/causa/$word?"          (controller: "search", action:"search"){lang="es"; searchType="CAUSE";type="SURVEY"; mappingName="searcherSearchSURVEYByCAUSE"}

        name searcherSearchSURVEYByREGION:   "/$lang/search/survey/from/$word?"          {controller="redirect"; action= "redirect301"; newMapping='en_searcherSearchSURVEYByREGION';constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name en_searcherSearchSURVEYByREGION:"/en/search/survey/from/$word?"             (controller: "search", action:"search"){lang="en"; searchType="REGION";type="SURVEY"; mappingName="searcherSearchSURVEYByREGION"}
        name es_searcherSearchSURVEYByREGION:"/es/buscar/encuesta/en/$word?"          (controller: "search", action:"search"){lang="es"; searchType="REGION";type="SURVEY"; mappingName="searcherSearchSURVEYByREGION"}


        /**********************/
        /***** LOGGED URLs ****/ //Language no matters
        /**********************/
        name dashboard:                     "/dashboard" (controller: "dashboard", action:"dashboard")
        name dashboardSkipUploadContacts:   "/dashboard/skipContacts" (controller: "dashboard", action:"skipContacts")
        name dashboardPoliticiansSeeMore:   "/ajax/dashboard/politicians/see-more" (controller: "dashboard", action:"dashboardPoliticians")
        name dashboardCampaignsSeeMore:     "/ajax/dashboard/campaigns/see-more" (controller: "dashboard", action:"dashboardCampaigns")

        name debateCreate:      "/account/debate/new" (controller: "debate"){action = [GET: "create", POST: "saveSettings"]}
        name debateEdit:        "/account/$userAlias/d/$urlTitle-$campaignId/edit-settings" (controller: "debate"){action = [GET: "editSettingsStep", POST: "saveSettings"]}
        name debateEditEvent:   "/account/$userAlias/d/$urlTitle-$campaignId/edit-event" (controller: "event"){action = [GET: "editEvent", POST: "updateEvent"]}
        name debateEditContent: "/account/$userAlias/d/$urlTitle-$campaignId/edit-content" (controller: "debate"){action = [GET: "editContentStep", POST: "saveContent"]}

        name debateRemove:      "/ajax/account/$userAlias/d/$urlTitle-$campaignId/remove" (controller: "debate", action: "remove")
        name debateShow:        "/$userAlias/$urlTitle-$campaignId"         (controller: "campaign", action: "show"){constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it) && !UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                "/$userAlias/-$campaignId"                  (controller: "campaign", action: "show"){constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it) && !UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}

        name debateProposalNew: "/ajax/addProposal"(controller: "debateProposal", action: "addProposal")
        name debateProposalDelete:"/ajax/deleteProposal"(controller: "debateProposal", action: "deleteProposal")
        name debateProposalPin: "/ajax/pinProposal"(controller: "debateProposal", action: "pinProposal")
        name debateProposalLike:"/ajax/likeProposal"(controller: "debateProposal", action: "likeProposal")
        name debateProposalComment: "/ajax/proposalComment/add"(controller: "debateProposal", action: "addComment")
        name debateProposalDeleteComment: "/ajax/proposalComment/delete"(controller: "debateProposal", action: "deleteComment")
        name debateProposalVoteComment: "/ajax/proposalComment/vote"(controller: "debateProposal", action: "voteComment")

        name eventBookTicket:           "/ajax/$userAlias/event/$urlTitle-$campaignId/book"(controller:"event", action: "bookTicket")
        name eventConfirmAssistance:    "/account/event/$campaignId/confirm"(controller:"event", action: "checkIn")
        name eventAssistanceReport:     "/ajax/account/event/$campaignId/report"(controller:"event", action: "sendReport")

        name postLike:              "/ajax/likePost"(controller: "post", action: "likePost")
        name postRemove:            "/ajax/account/$userAlias/p/$urlTitle-$campaignId/remove" (controller: "post", action: "remove")
        name postCreate:            "/account/post/new" (controller: "post"){action = [GET: "create", POST: "saveSettings"]}
        name postEdit:              "/account/$userAlias/p/$urlTitle-$campaignId/edit-settings" (controller: "post"){action = [GET: "editSettingsStep", POST: "saveSettings"]}
        name postEditEvent:         "/account/$userAlias/p/$urlTitle-$campaignId/edit-event" (controller: "event"){action = [GET: "editEvent", POST: "updateEvent"]}
        name postEditContent:       "/account/$userAlias/p/$urlTitle-$campaignId/edit-content" (controller: "post"){action = [GET: "editContentStep", POST: "saveContent"]}
        name postShow:              "/$userAlias/$urlTitle-$campaignId"           (controller: "campaign", action: "show"){constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it) && !UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                    "/$userAlias/-$campaignId"                    (controller: "campaign", action: "show"){constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it) && !UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}

        name surveyRemove:          "/ajax/account/$userAlias/s/$urlTitle-$campaignId/remove" (controller: "survey", action: "remove")
        name surveyCreate:          "/account/survey/new" (controller: "survey"){action = [GET: "create", POST: "saveSettings"]}
        name surveyEditSettings:    "/account/$userAlias/s/$urlTitle-$campaignId/edit-settings" (controller: "survey"){action = [GET: "editSettingsStep", POST: "saveSettings"]}
        name surveyEditContent:     "/account/$userAlias/s/$urlTitle-$campaignId/edit-content" (controller: "survey"){action = [GET: "editContentStep", POST: "saveContent"]}
        name surveyEditQuestions:   "/account/$userAlias/s/$urlTitle-$campaignId/edit-questions" (controller: "survey"){action = [GET: "editQuestionsStep", POST: "saveQuestions"]}
        name surveyShow:            "/$userAlias/$urlTitle-$campaignId"                          (controller: "campaign", action: "show"){constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it) && !UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name surveySaveAnswer:      "/ajax/$userAlias/$urlTitle-$campaignId/saveAnswer"          (controller: "survey", action: "saveAnswer"){constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it) && !UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}

        name eventCreate:           "/account/event/new" (controller: "event"){action = [GET: "create", POST: "saveSettings"]}
        // REDIRECTS (OLD URLS) - DEPRECATED

        name campaignShow:          "/$userAlias/$urlTitle-$campaignId" (controller: "campaign", action: "show"){constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it) && !UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}

        name widgetJs:      "/widget.js"(controller: "widget", action:"kuorumWidgetjs")
        name widgetRatePolitician:     "/widget/ratePolitician" (controller: "rating", action:"widgetRatePolitician")
        name widgetComparative:        "/widget/comparation"    (controller: "rating", action:"widgetComparativePoliticianInfo")


        name langUserShow:          "/$lang/$userAlias"     {controller="redirect"; action= "redirect301User"; newMapping='userShow'; constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it) && !UrlMappings.VALID_LANGUAGE_PATHS.contains(it)}); lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name userShow:              "/$userAlias"           (controller: "kuorumUser", action: "show") {constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it) && !UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name secUserShow:           "/sec/$userAlias"       (controller: "kuorumUser", action: "secShow")


        name userFollowers:     "/ajax/$userAlias/followers" (controller: "kuorumUser", action: "userFollowers")
        name userFollowing:     "/ajax/$userAlias/following"  (controller: "kuorumUser", action: "userFollowing")
        name userUnsubscribe:   "/unsubscribe/$userId"  (controller: "contacts"){action=[GET:"unsubscribe", POST:"unsubscribeConfirm"]}

        name bulkActionRemoveContactsAjax:          "/ajax/contact/remove" (controller:"contacts", action: "removeContactsBulkAction")
        name bulkActionAddTagsContactsAjax:         "/ajax/contact/addTags" (controller:"contacts", action: "addTagsBulkAction")
        name bulkActionRemoveTagsContactsAjax:      "/ajax/contact/removeTags" (controller: "contacts", action: "removeTagsBulkAction")

        name userFollowAndRegister:          "/$userAlias/subscribe" (controller: "kuorumUser", action: "subscribeTo")
        name ajaxRegisterContact:            "/ajax/contact"(controller: "register", action: "contactRegister");

        name userRate:                  "/ajax/$userAlias/rate"(controller: "rating", action:"ratePolitician")
        name userHistoricRate:          "/ajax/$userAlias/historicRate"(controller: "rating", action:"historicPoliticianRate")
        name userLoadRate:              "/ajax/$userAlias/loadRate"(controller: "rating", action:"loadRating")
        name comparingPoliticianRate:   "/ajax/user/compareRate"(controller: "rating", action:"comparingPoliticianRateData")

        name suggestSearcher:       "/ajax/search/suggestions/all"(controller: "search", action:"suggest")
        name suggestRegions:        "/ajax/search/suggestions/regions"(controller: "search", action:"suggestRegions")
        name suggestTags:           "/ajax/search/suggestions/tags"(controller: "search", action:"suggestTags")
        name suggestAlias:          "/ajax/search/suggestions/alias"(controller: "search", action:"suggestAlias")

        name profileEditAccountDetails:     "/config/account-details"                                   (controller: "profile"){action =[GET:"editAccountDetails", POST:"updateAccountDetails"]}
        name profileEditUser:               "/edit-profile"                                    (controller: "profile"){action =[GET:"editUser", POST:"editUserSave"]}
        name profileEditCommissions:        "/edit-profile/edit-commission"                    (controller: "profile"){action =[GET:"editCommissions", POST:"editCommissionsSave"]}
        name profileChangePass:             "/edit-profile/change-password"                    (controller: "profile"){action =[GET:"changePassword", POST:"changePasswordSave"]}
        name profileSetPass:                "/edit-profile/create-password"                    (controller: "profile"){action =[GET:"setPassword", POST:"setPasswordSave"]}
        name profileChangeEmail:            "/edit-profile/change-email"                       (controller: "profile"){action =[GET:"changeEmail", POST:"changeEmailSave"]}
        name profileChangeEmailSent:        "/edit-profile/change-email/request-received"      (controller: "profile", action :"changeEmailConfirmSent")
        name profileChangeEmailResend:      "/edit-profile/change-email/resend-email"          (controller: "profile", action :"updateUserEmail")
        name profileChangeEmailConfirm:     "/edit-profile/change-email/confirm"               (controller: "profile", action: "changeEmailConfirm")
        name profileSocialNetworks:         "/edit-profile/social-networks"                    (controller: "profile"){action=[GET:"socialNetworks",POST:"socialNetworksSave"]}
        name profileEmailNotifications:     "/edit-profile/email-notifications"                (controller: "profile"){action=[GET:"configurationEmails",POST:"configurationEmailsSave"]}
        name profileDeleteAccount:          "/edit-profile/delete-account"                     (controller: "profile"){action=[GET:"deleteAccount", POST:"deleteAccountPost"]}
        name profileCauses:                 "/edit-profile/causes"                             (controller: "profile"){action=[GET:"editCauses", POST:"updateCauses"]}
        name profileNews:                   "/edit-profile/news"                               (controller: "profile"){action=[GET:"editNews", POST:"updateNews"]}
        name profileQuickNotes:             "/edit-profile/quick-notes"                        (controller: "profile"){action=[GET:"editQuickNotes", POST:"updateQuickNotes"]}
        name profileProfessionalDetails:    "/edit-profile/professional-details"               (controller: "profile"){action=[GET:"editProfessionalDetails", POST:"updateProfessionalDetails"]}
        name profilePictures:               "/edit-profile/pictures"                           (controller: "profile"){action=[GET:"editPictures", POST: "updatePictures"]}
        name profileNewsletterConfig:       "/config/newsletter-config"                        (controller: "profile"){action=[GET:"editNewsletterConfig", POST: "updateNewsletterConfig"]}
        name profileNewsletterConfigRequestEmailSender:     "/ajax/config/newsletter-config/requestSender"   (controller: "profile"){action=[POST: "requestedEmailSender"]}

        name customProcessRegisterStep2:         "/edit-profile/sign-up/step2"      (controller: "customRegister"){action = [GET:"step2", POST:"step2Save"]}
        name customProcessRegisterStep3:         "/edit-profile/sign-up/step3"      (controller: "customRegister", action :"step3")


        name profileMailing : "/notifications/mailing" (controller: "profile", action:"showUserEmails")

        name causeSupport:         "/ajax/cause/$causeName/support" (controller:"causes", action: "supportCause")

        name ajaxHeadNotificationsChecked:  "/ajax/notificaiones/check"(controller:"notification", action:"notificationChecked")
        name ajaxHeadNotificationsSeeMore:  "/ajax/notificaiones/seeMore"(controller:"notification", action:"notificationSeeMore")
        name ajaxHeadMessagesChecked:       "/ajax/mensajes/check"(controller:"layouts", action:"headNotificationsChecked")
        name ajaxFollow:                    "/ajax/kuorumUser/follow"(controller:"kuorumUser", action:"follow")
        name ajaxUnFollow:                  "/ajax/kuorumUser/unFollow"(controller:"kuorumUser", action:"unFollow")
        name ajaxRequestPolitician:         "/ajax/politico/solicitud-kuorum"(controller:"kuorumUser", action:"follow")
        name ajaxCropImage:                 "/ajax/file/crop"(controller:"file", action:"cropImage")
        name ajaxUploadFile:                "/ajax/file/upload" (controller:'file', action:"uploadImage")
        name ajaxUploadFilePDF:             "/ajax/file/uploadPDF" (controller:'file', action:"uploadPDF")

        name adminPrincipal:        "/admin"                          (controller:"admin", action: "index")
        name adminTestMail:         "/admin/mailing/test"           (controller:"mailTesting", action: "index")
        name adminSearcherIndex:    "/admin/searcher/indexar"       (controller:"admin", action: "solrIndex")
        name adminSearcherFullIndex:"/admin/searcher/full-index"    (controller:"admin", action:"fullIndex")
        name adminEditorsMonitoring:"/admin/editors/monitoring"    (controller:"admin", action:"editorsMonitoring")

        name editorCreatePolitician:                        "/editor/user/politician/create-politician"             (controller:"editorUser"){action =[GET:"createPolitician", POST:"saveCreatePolitician"]}
        name editorEditUserProfile:                         "/editor/user/$userAlias/editar/profile"                (controller:"editorUser"){action =[GET:"editUser", POST:"updateUser"]}
        name editorEditSocialNetwork:                       "/editor/user/$userAlias/editar/social-network"         (controller:"editorUser"){action =[GET:"editUserSocialNetwork", POST:"updateUserSocialNetwork"]}
        name editorEditNews:                                "/editor/user/$userAlias/editar/news"                   (controller:"editorPolitician"){action =[GET:"editNews", POST:"updateNews"]}
        name editorEditPoliticianProfessionalDetails:       "/editor/user/$userAlias/editar/professionalDetails"    (controller:"editorPolitician"){action =[GET:"editProfessionalDetails", POST:"updateProfessionalDetails"]}
        name editorEditPoliticianQuickNotes:                "/editor/user/$userAlias/editar/quick-notes"            (controller:"editorPolitician"){action =[GET:"editQuickNotes", POST:"updateQuickNotes"]}
        name editorEditPoliticianCauses:                    "/editor/user/$userAlias/editar/causes"                 (controller:"editorPolitician"){action =[GET:"editCauses", POST:"updateCauses"]}
        name editorKuorumAccountEdit:                       "/editor/user/$userAlias/editar/account-details"        (controller:"editorUser"){action =[GET:"editAdminAccountDetails", POST:"updateAdminAccountDetails"]}
        name editorAdminUserRights:                         "/editor/user/$userAlias/editar/rights"                 (controller:"admin"){action =[GET:"editUserRights", POST:"updateUserRights"]}
        name editorAdminEmailSender:                         "/editor/user/$userAlias/editar/email-sender"                 (controller:"admin"){action =[GET:"editUserEmailSender", POST:"updateUserEmailSender"]}

        name ajaxDeleteRecommendedUser: "/ajax/kuorumUser/deleteRecommendedUser"(controller: 'recommendedUserInfo', action: 'deleteRecommendedUser')

        name politicianContactProfiling:                "/account/contact-profiling" (controller:"politician", action: "betaTesterPage")
        name politicianContacts:                        "/account/contacts" (controller:"contacts", action: "index")
        name politicianContactsSearch:                  "/ajax/account/contacts" (controller:"contacts", action: "searchContacts")
        name politicianContactExport:                   "/account/contacts/export" (controller:"contacts", action: "exportContacts")
        name politicianContactImport:                   "/account/contacts/import" (controller:"contacts", action: "importContacts")
        name politicianContactImportCSV:                "/account/contacts/import/csv" (controller:"contacts"){action =[GET:"importCSVContacts", POST:"importCSVContactsUpload"]}
        name politicianContactImportCSVSave:            "/account/contacts/import/csv_save" (controller:"contacts", action: "importCSVContactsSave")
        name politicianContactImportGmail:              "/account/contacts/import/gmail" (controller:"googleContacts", action: "index")
        name politicianContactImportOutlook:            "/account/contacts/import/outlook" (controller:"outlookContacts", action: "index")
        name politicianContactImportSuccess:            "/account/contacts/import/success" (controller:"contacts", action: "importSuccess")
        name politicianContactTagsAjax:                 "/ajax/account/contacts/tags" (controller:"contacts", action: "contactTags")
        name politicianContactAddTagsAjax:              "/ajax/account/contacts/$contactId/tags" (controller:"contacts", action: "addTagsToContact")
        name politicianContactRemoveAjax:               "/ajax/account/contacts/$contactId/remove" (controller:"contacts", action: "removeContact")
        name politicianContactFilterNew:                "/ajax/account/contacts/filters/new" (controller:"contactFilters", action: "newFilter")
        name politicianContactFilterUpdate:             "/ajax/account/contacts/filters/update" (controller:"contactFilters", action: "updateFilter")
        name politicianContactFilterRefresh:            "/ajax/account/contacts/filters/refresh" (controller:"contactFilters", action: "refreshFilter")
        name politicianContactFilterData:               "/ajax/account/contacts/filters/data" (controller:"contactFilters", action: "getFilterData")
        name politicianContactFilterDelete:             "/ajax/account/contacts/filters/delete" (controller:"contactFilters", action: "deleteFilter")
        name politicianContactEdit:                     "/account/contacts/$contactId/edit" (controller:"contacts"){action=[GET:"editContact", POST:"updateContact"]}
        name politicianContactEditUpdateNote:           "/ajax/account/contacts/$contactId/edit/updateNote" (controller:"contacts",action:"updateContactNotes")
        name politicianContactNew:                      "/account/contacts/new" (controller:"contacts"){action =[GET:"newContact", POST:"saveContact"]}
        name politicianInbox:                           "/account/inbox" (controller:"politician", action: "betaTesterPage")
        name politicianCampaigns:                       "/account/campaigns" (controller:"newsletter", action: "index")
        name politicianCampaignsNew:                    "/account/campaigns/new" (controller:"newsletter", action: "newCampaign")
        name politicianCampaignsLists:                  "/ajax/account/campaigns/lists" (controller:"campaign", action: "findLiUserCampaigns")
        name politicianCampaignsExport:                 "/account/campaigns/export" (controller:"newsletter", action: "exportCampaigns")
        name politicianCampaignsUploadImages:           "/ajax/account/campaign/$campaignId/uploadImages" (controller:"file", action: "uploadCampaignImages");
        name politicianCampaignsListImages:             "/ajax/account/campaign/$campaignId/listImages" (controller:"file", action: "getCampaignImages");
        name politicianMassMailingNew:                  "/account/mass-mailing/new" (controller:"newsletter"){ action=[GET:"createNewsletter", POST:'saveMassMailingSettings']}
        name politicianMassMailingSettings:             "/account/mass-mailing/$campaignId/edit-settings" (controller: "newsletter"){ action=[GET:"editSettingsStep", POST: 'saveMassMailingSettings']}
        name politicianMassMailingTemplate:             "/account/mass-mailing/$campaignId/edit-template" (controller: "newsletter"){ action=[GET:"editTemplateStep", POST: 'saveMassMailingTemplate']}
        name politicianMassMailingContent:              "/account/mass-mailing/$campaignId/edit-content" (controller: "newsletter") { action=[GET:"editContentStep", POST: 'saveMassMailingContent']}
        name politicianMassMailingShow:                 "/account/mass-mailing/$campaignId" (controller:"newsletter"){ action=[GET:"showCampaign", POST:'updateCampaign']}
        name politicianMassMailingSendTest:             "/account/mass-mailing/$campaignId/test" (controller:"newsletter", action: "sendMassMailingTest")
        name politicianMassMailingRemove:               "/ajax/account/mass-mailing/$campaignId/remove" (controller:"newsletter", action:"removeCampaign")
        name politicianMassMailingTrackEvents:          "/ajax/account/mass-mailing/$newsletterId/trackEvents" (controller:"newsletter", action: "showTrackingMails")
        name politicianMassMailingTrackEventsReport:    "/ajax/account/mass-mailing/$newsletterId/trackEvents/report" (controller:"newsletter", action: "sendReport")
        name politicianMassMailingHtml:                 "/account/mass-mailing/$campaignId/html" (controller:"newsletter", action: "showMailCampaign")
        name politicianMassMailingSaveTimeZone:         "/account/mass-mailing/saveTimeZone" (controller: "newsletter"){action = [POST:"saveTimeZone"]}
        name politicianCampaignStatsShow:               "/account/campaign/$campaignId" (controller:"newsletter", action:"showCampaignStats")
        name politicianMassMailingDebateStatsReport:    "/ajax/account/debate/$campaignId/report" (controller:"debate", action: "sendReport")
        name politicianMassMailingSurveyStatsReport:    "/ajax/account/survey/$campaignId/report" (controller:"survey", action: "sendReport")

        name politicianTeamManagement:                  "/account/team-management" (controller:"politician", action: "betaTesterPage")

        name paymentStart:                              "/payment" (controller:"payment")
        name paymentGateway:                            "/payment/gateway" (controller:"payment"){action=[GET:'paymentGateway',POST:'paymentGatewaySubmitSubscription']}
        name paymentGatewayPlan:                        "/ajax/payment/gateway/plan" (controller:"payment", action: "getInfoPlan")
        name paymentGatewaySavePaymentMethod:           "/ajax/payment/gateway/paymentMethod" (controller:"payment", action: "savePaymentMethod")
        name paymentSuccess:                            "/payment/success" (controller:"payment", action: 'paymentSuccess')
        name paymentPromotionalCodeValidation:          "/ajax/payment/promotional-code" (controller: "payment"){action = [POST:"promotionalCodeValidation"]}

        "/account/contacts/oauth/$provider/success" (controller: "contactsOAuth", action: "onSuccess")
        "/account/contacts/oauth/$provider/failure" (controller: "contactsOAuth", action: "onFailure")

        "/googleContacts/loadContactsFromGoogle" (controller: "googleContacts", action: "loadContactsFromGoogle")

        "/admin/updateMailChimp" (controller: "admin", action: "updateMailChimp")

        /**********************/
        /***** DEPRECATED *****/
        /**********************/

        name tourStart:           "/tour" (controller:"tour", action: "index")
        name tour_dashboard:      "/tour/dashboard" (controller:"tour", action: "tour_dashboard")

        // ADMIN STATS
        name adminStats:            "/admin/estadisticas"           (controller:"adminStats", action: "stats")
        name adminStatsPieChart:    "/admin/estadisticas/pie-chart" (controller:"adminStats", action: "statsDataPieChart")

        /**********************/
        /*** END DEPRECATED ***/
        /**********************/

        name sitemapIndex:  "/$lang/sitemap" (controller: "siteMap", action: "sitemapIndex")

        name sitemapLandings:   "/$lang/sitemap/landings" (controller: "siteMap", action: "sitemapLandings")
        name sitemapFooters:    "/$lang/sitemap/footers"   (controller: "siteMap", action: "sitemapFooters")
        name sitemapSearchs:    "/$lang/sitemap/searchs"  (controller: "siteMap", action: "sitemapSearchs")
        name sitemapUsersIdx:   "/$lang/sitemap/users"    (controller: "siteMap", action: "sitemapUsersIndex")
        name sitemapUsers:      "/$lang/sitemap/users/$year/$month"    (controller: "siteMap", action: "sitemapUsers")


        // TODO: REVIEW BASIC URL -> RegisterController:sendConfirmationEmail || Reset password and others
        "/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }


        "403" (controller: "error", action: "forbidden")
        "404" (controller: "error", action: "notFound")
        "401" (controller: "error", action: "notAuthorized")


        Environment.executeForCurrentEnvironment {
            development {
                "500" (controller: "error", action: "kuorumExceptionHandler", exception: KuorumException)
                "500" (controller: "error", action: "notAuthorized", exception: AccessDeniedException)
                "500" (controller: "error", action: "internalError")
//                "500"(view:'/error')
            }
            test{
                "500" (controller: "error", action: "kuorumExceptionHandler", exception: KuorumException)
                "500" (controller: "error", action: "notAuthorized", exception: AccessDeniedException)
                "500" (controller: "error", action: "internalError")
            }
            production{
                "500" (controller: "error", action: "kuorumExceptionHandler", exception: KuorumException)
                "500" (controller: "error", action: "notAuthorized", exception: AccessDeniedException)
                "500" (controller: "error", action: "internalError")
            }


        }
//        "500"(view:'/error')
	}

    static exceptionMappings = {
        "Access is denied" org.springframework.security.access.AccessDeniedException { ex ->
            controller = "error"
            action = "notAuthorized"
            exception = ex
        }
    }
}
