package kuorum.files

import org.kuorum.rest.model.domain.DomainRSDTO
import org.lesscss.LessCompiler
import org.lesscss.deps.org.apache.commons.io.FileUtils
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

class LessCompilerService implements ApplicationContextAware {

    ApplicationContext applicationContext

    AmazonFileService amazonFileService;

    def grailsApplication

    private static final String PARAM_MAIN_COLOR="@mainColor"
    private static final String PARAM_MAIN_COLOR_HOVER="@mainColorHover"
    private static final String PARAM_SECOND_COLOR="@secondColor"
    private static final String PARAM_SECOND_COLOR_HOVER="@secondColorHover"
    private static final String PARAM_STATIC_ROOT_URL="@staticUrlRoot"
    private static final String PARAM_MAIN_TEXT_COLOR="@mainTextColor"
    private static final String PARAM_MAIN_WARN_COLOR="@warnColor"

    String compileCssForDomain(DomainRSDTO domain){
        // Instantiate the LESS compiler with some compiler options

        String domainName = domain.domain
        String temporalPath = "${grailsApplication.config.kuorum.upload.serverPath}"
        LessCompiler lessCompiler = new LessCompiler(Arrays.asList("--relative-urls", "--strict-math=on"));
        lessCompiler.customJs

        File domainLessFile=buildLessFile(domain)

        File customDomainCss = new File("${temporalPath}/${domainName}.css")
        // Or compile LESS input file to CSS output file
        lessCompiler.compile(domainLessFile, customDomainCss);

        amazonFileService.uploadDomainCss(customDomainCss, domainName);
        customDomainCss.delete();
        domainLessFile.delete();
    }

    private File buildLessFile(DomainRSDTO domain){
        String temporalPath = "${grailsApplication.config.kuorum.upload.serverPath}"
        File lessMergedFile = new File("${temporalPath}/${domain.domain}.less")
        File lessDomainVarsFile = new File("${temporalPath}/${domain.domain}_vars.less")
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

    private String buildLessParams(DomainRSDTO domainRSDTO){
        StringBuilder sb = new StringBuilder();
        appendLessProperty(sb,PARAM_MAIN_COLOR,domainRSDTO.mainColor)
        appendLessProperty(sb,PARAM_MAIN_COLOR_HOVER,domainRSDTO.mainColorShadowed)
        appendLessProperty(sb,PARAM_SECOND_COLOR,domainRSDTO.secondaryColor)
        appendLessProperty(sb,PARAM_SECOND_COLOR_HOVER,domainRSDTO.secondaryColorShadowed)
        appendLessProperty(sb,PARAM_MAIN_TEXT_COLOR,"#fff")
        appendLessProperty(sb,PARAM_MAIN_WARN_COLOR,"#990000")
        appendLessProperty(sb,PARAM_STATIC_ROOT_URL,"\"${amazonFileService.getStaticRootDomainPath(domainRSDTO.domain)}\"")
        return sb.toString();
    }

    private void appendLessProperty(StringBuilder sb, String key, String value){
        sb.append(key).append(": ").append(value).append(";").append("\n")
    }

    String getUrlDomainCss(String domain){
        amazonFileService.getDomainCssUrl(domain)
    }
}
