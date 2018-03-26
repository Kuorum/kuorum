package kuorum.files

import org.lesscss.LessCompiler
import org.lesscss.deps.org.apache.commons.io.FileUtils
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

class LessCompilerService implements ApplicationContextAware {

    ApplicationContext applicationContext

    AmazonFileService amazonFileService;

    def grailsApplication

    private static final String PARAM_MAIN_COLOR="@mainColor"
    private static final String PARAM_STATIC_ROOT_URL="@staticUrlRoot"

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
        sb.append(PARAM_MAIN_COLOR).append(": ").append("#00FF00").append(";").append("\n")
        sb.append(PARAM_STATIC_ROOT_URL).append(": ").append("\"").append(amazonFileService.getStaticRootDomainPath(domain)).append("\"").append(";").append("\n")
        return sb.toString();
    }

    String getUrlDomainCss(String domain){
        amazonFileService.getDomainCssUrl(domain)
    }
}
