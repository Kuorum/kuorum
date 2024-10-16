package kuorum.files

import kuorum.web.admin.domain.KuorumWebFont
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
    private static final String PARAM_MAIN_TITLES_FONT ="@fontTitle"
    private static final String PARAM_MAIN_TITLES_FONT_WEIGHT ="@fontTitleWeight"
    private static final String PARAM_MAIN_TEXT_FONT ="@fontText"

    String compileCssForDomain(DomainRSDTO domain){
        // Instantiate the LESS compiler with some compiler options
        log.info("Updaring CSS of domain: ${domain.name} [${domain.domain}]")
        String domainName = domain.domain
        File workingFolder = new File("${grailsApplication.config.kuorum.upload.serverPath}"+"/buildKuorum/"+domainName);
        workingFolder.mkdirs()
        LessCompiler lessCompiler = new LessCompiler(Arrays.asList("--relative-urls", "--strict-math=on"));
//        lessCompiler.customJs

        File domainLessFile=buildLessFile(domain, workingFolder)

        File customDomainCss = new File("${workingFolder}/${domainName}.css")
        buildModulesFolder(workingFolder)
        // Or compile LESS input file to CSS output file
        lessCompiler.compile(domainLessFile, customDomainCss);

        amazonFileService.uploadDomainCss(customDomainCss, domainName);
        customDomainCss.delete();
        domainLessFile.delete();
        FileUtils.deleteDirectory(workingFolder);
    }

    private File buildLessFile(DomainRSDTO domain, File workingFolder){
        File lessMergedFile = new File("${workingFolder.toString()}/${domain.domain}.less")
        File lessDomainVarsFile = new File("${workingFolder.toString()}/${domain.domain}_vars.less")
        lessMergedFile.deleteOnExit()
        lessDomainVarsFile.deleteOnExit()

        String params = buildLessParams(domain);
        File customDomainLess = applicationContext.getResource("less/customDomainCss.less").file
        FileUtils.writeStringToFile(lessDomainVarsFile, params, "UTF-8");
        mergeFiles(Arrays.asList(lessDomainVarsFile, customDomainLess), lessMergedFile);
        lessDomainVarsFile.delete();
        return lessMergedFile;

    }

    private File buildModulesFolder(File workingFolder){
        File tmp = new File(workingFolder.toString()+"/modules")
        File parent = new File(applicationContext.getResource("less/customDomainCss.less").file.parent+"/modules")
        tmp.mkdirs()
        org.apache.commons.io.FileUtils.copyDirectory(parent, tmp)
        return tmp
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
        appendLessProperty(sb,PARAM_MAIN_TITLES_FONT,KuorumWebFont.build(domainRSDTO.webFontCombinationName).titleFontName)
        appendLessProperty(sb,PARAM_MAIN_TITLES_FONT_WEIGHT,KuorumWebFont.build(domainRSDTO.webFontCombinationName).titleFontWeight)
        appendLessProperty(sb, PARAM_MAIN_TEXT_FONT, KuorumWebFont.build(domainRSDTO.webFontCombinationName).textFontName)
        return sb.toString();
    }

    private void appendLessProperty(StringBuilder sb, String key, String value) {
        sb.append(key).append(": ").append(value).append(";").append("\n")
    }

    String getUrlDomainCss(String domain) {
        amazonFileService.getDomainCssUrl(domain)
    }

    String getUrlDomainCssTricks(String domain) {
        amazonFileService.getDomainCssTricksUrl(domain)
    }
}
