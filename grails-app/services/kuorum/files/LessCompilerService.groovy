package kuorum.files

import kuorum.domain.DomainService
import org.kuorum.rest.model.domain.DomainConfigRSDTO
import org.lesscss.LessCompiler
import org.lesscss.deps.org.apache.commons.io.FileUtils
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

class LessCompilerService implements ApplicationContextAware {

    ApplicationContext applicationContext

    AmazonFileService amazonFileService;

    DomainService domainService;

    def grailsApplication

    private static final String PARAM_MAIN_COLOR="@mainColor"
    private static final String PARAM_MAIN_COLOR_HOVER="@mainColorHover"
    private static final String PARAM_SECOND_COLOR="@secondColor"
    private static final String PARAM_SECOND_COLOR_HOVER="@secondColorHover"
    private static final String PARAM_STATIC_ROOT_URL="@staticUrlRoot"
    private static final String PARAM_MAIN_TEXT_COLOR="@mainTextColor"

//    private static final String PARAM_BTN_COLOR_HOVER="@mainColorHover";                 //="${domainConfigRSDTO.secondaryColor} !important";
//    private static final String PARAM_STATISTICS="@statisticsColor";                    //="${domainConfigRSDTO.mainColor}";
//    private static final String PARAM_SEARCH_BORDER="@searchBorderColor";               //="${domainConfigRSDTO.secondaryColor}";
//    private static final String PARAM_SEARCH_COLOR="@searchColor";                      //="${domainConfigRSDTO.mainColor}";
//    private static final String PARAM_PHONE="@phoneColor";                              //="${domainConfigRSDTO.secondaryColor}";
//    private static final String PARAM_TEXT_MENU="@textMenuColor";                       //="${domainConfigRSDTO.mainColor} !important";
//    private static final String PARAM_SIGN_IN="@signInFrame";                           //="#B9B0C5";
//    private static final String PARAM_SIGN_IN_BTN="@signInButton";                      //="${domainConfigRSDTO.mainColor} !important";
//    private static final String PARAM_SIGN_IN_BTN_HOVER="@signInButtonHover";           //="${domainConfigRSDTO.secondaryColor} !important";
//    private static final String PARAM_SHOW_PASS="@showPasswordCheckboxColor";           //="#B9B0C5";
//    private static final String PARAM_LINE_COLOR="@profileLineColor";                   //="${domainConfigRSDTO.mainColor}";
//    private static final String PARAM_MENU="@dashboardMenuColor";                       //="${domainConfigRSDTO.mainColor}";
//    private static final String PARAM_ICONS="@dashboardIconsColor";                     //="${domainConfigRSDTO.mainColor}";
//    private static final String PARAM_BORDER="@dashboardBorderIconsColor";              //="${domainConfigRSDTO.mainColor}";
//    private static final String PARAM_PROGRESS_BAR="@progressBarColor";                 //="${domainConfigRSDTO.mainColor}";
//    private static final String PARAM_CAMPAIGN_ICON_HOVER="@campaignIconHoverColor";    //="${domainConfigRSDTO.mainColor}";
//    private static final String PARAM_CAMPAIGN_ICON_ACTIVE="@campaignIconActiveColor";  //="${domainConfigRSDTO.mainColor}";
//    private static final String PARAM_CAMPAIGN_ICON_ACTIVE_BORDER="@campaignBorderIconActiveColor";              //="${domainConfigRSDTO.mainColor}";
//    private static final String PARAM_CAMPAIGN_ICON="@campaignIconColor";               //="${domainConfigRSDTO.secondaryColor}";
//    private static final String PARAM_CAMPAIGN_ICON_BORDER="@mainColor";  //="${domainConfigRSDTO.secondaryColor}";
//    private static final String PARAM_CAMPAIGN_ARROW="@campaignArrowColor";             //="${domainConfigRSDTO.secondaryColor}";
//    private static final String PARAM_FOOTER_ICONS="@footerIconsColor";                 //="${domainConfigRSDTO.mainColor}";

    String compileCssForDomain(String domain){
        // Instantiate the LESS compiler with some compiler options

        String temporalPath = "${grailsApplication.config.kuorum.upload.serverPath}"
        LessCompiler lessCompiler = new LessCompiler(Arrays.asList("--relative-urls", "--strict-math=on"));
        lessCompiler.customJs

        File domainLessFile=buildLessFile(domain)

        File customDomainCss = new File("${temporalPath}/${domain}.css")
        // Or compile LESS input file to CSS output file
        lessCompiler.compile(domainLessFile, customDomainCss);

        amazonFileService.uploadDomainCss(customDomainCss, domain);
        customDomainCss.delete();
        domainLessFile.delete();
    }

    private File buildLessFile(String domain){
        String temporalPath = "${grailsApplication.config.kuorum.upload.serverPath}"
        File lessMergedFile = new File("${temporalPath}/${domain}.less")
        File lessDomainVarsFile = new File("${temporalPath}/${domain}_vars.less")
        lessMergedFile.deleteOnExit()
        lessDomainVarsFile.deleteOnExit()

        String params = buildLessParams(domain);
        File customDomainLess = applicationContext.getResource("less/customDomainCss.less").file
        FileUtils.writeStringToFile(lessDomainVarsFile, params, "UTF-8");
        mergeFiles(Arrays.asList(lessDomainVarsFile, customDomainLess), lessMergedFile);
        lessDomainVarsFile.delete();
        return lessMergedFile;

    }

    private void mergeFiles(List<File> files, File mergedFile) {

        FileWriter fstream = null;
        BufferedWriter out = null;
        try {
            fstream = new FileWriter(mergedFile, true);
            out = new BufferedWriter(fstream);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        for (File f : files) {
            log.info("merging: " + f.absolutePath);
            FileInputStream fis;
            try {
                fis = new FileInputStream(f);
                BufferedReader inFile = new BufferedReader(new InputStreamReader(fis));

                String aLine;
                while ((aLine = inFile.readLine()) != null) {
                    out.write(aLine);
                    out.newLine();
                }

                inFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String buildLessParams(String domain){
        StringBuilder sb = new StringBuilder();
        DomainConfigRSDTO domainConfigRSDTO = domainService.getConfig(domain);
        appendLessProperty(sb,PARAM_MAIN_COLOR,domainConfigRSDTO.mainColor)
        appendLessProperty(sb,PARAM_MAIN_COLOR_HOVER,domainConfigRSDTO.mainColorShadowed)
        appendLessProperty(sb,PARAM_SECOND_COLOR,domainConfigRSDTO.secondaryColor)
        appendLessProperty(sb,PARAM_SECOND_COLOR_HOVER,domainConfigRSDTO.secondaryColorShadowed)
        appendLessProperty(sb,PARAM_MAIN_TEXT_COLOR,"#fff")
        appendLessProperty(sb,PARAM_STATIC_ROOT_URL,"\"${amazonFileService.getStaticRootDomainPath(domain)}\"")
//        appendLessProperty(sb,PARAM_BTN_COLOR_HOVER,"${domainConfigRSDTO.secondaryColor} !important")
//        appendLessProperty(sb,PARAM_STATISTICS,"${domainConfigRSDTO.mainColor}")
//        appendLessProperty(sb,PARAM_SEARCH_BORDER,"${domainConfigRSDTO.secondaryColor}")
//        appendLessProperty(sb,PARAM_SEARCH_COLOR,"${domainConfigRSDTO.mainColor}")
//        appendLessProperty(sb,PARAM_PHONE,"${domainConfigRSDTO.secondaryColor}")
//        appendLessProperty(sb,PARAM_TEXT_MENU,"${domainConfigRSDTO.mainColor} !important")
//        appendLessProperty(sb,PARAM_SIGN_IN,"#B9B0C5")
//        appendLessProperty(sb,PARAM_SIGN_IN_BTN,"${domainConfigRSDTO.mainColor} !important")
//        appendLessProperty(sb,PARAM_SIGN_IN_BTN_HOVER,"${domainConfigRSDTO.secondaryColor} !important")
//        appendLessProperty(sb,PARAM_SHOW_PASS,"#B9B0C5")
//        appendLessProperty(sb,PARAM_LINE_COLOR,"${domainConfigRSDTO.mainColor}")
//        appendLessProperty(sb,PARAM_MENU,"${domainConfigRSDTO.mainColor}")
//        appendLessProperty(sb,PARAM_ICONS,"${domainConfigRSDTO.mainColor}")
//        appendLessProperty(sb,PARAM_BORDER,"${domainConfigRSDTO.mainColor}")
//        appendLessProperty(sb,PARAM_PROGRESS_BAR,"${domainConfigRSDTO.mainColor}")
//        appendLessProperty(sb,PARAM_CAMPAIGN_ICON_HOVER,"${domainConfigRSDTO.mainColor}")
//        appendLessProperty(sb,PARAM_CAMPAIGN_ICON_ACTIVE,"${domainConfigRSDTO.mainColor}")
//        appendLessProperty(sb,PARAM_CAMPAIGN_ICON_ACTIVE_BORDER,"${domainConfigRSDTO.mainColor}")
//        appendLessProperty(sb,PARAM_CAMPAIGN_ICON,"${domainConfigRSDTO.secondaryColor}")
//        appendLessProperty(sb,PARAM_CAMPAIGN_ICON_BORDER,"${domainConfigRSDTO.secondaryColor}")
//        appendLessProperty(sb,PARAM_CAMPAIGN_ARROW,"${domainConfigRSDTO.secondaryColor}")
//        appendLessProperty(sb,PARAM_FOOTER_ICONS,"${domainConfigRSDTO.mainColor}")
        return sb.toString();
    }

    private void appendLessProperty(StringBuilder sb, String key, String value){
        sb.append(key).append(": ").append(value).append(";").append("\n")
    }

    String getUrlDomainCss(String domain){
        amazonFileService.getDomainCssUrl(domain)
    }
}
