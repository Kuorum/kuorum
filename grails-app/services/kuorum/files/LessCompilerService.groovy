package kuorum.files

import org.lesscss.LessCompiler
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

class LessCompilerService implements ApplicationContextAware {

    ApplicationContext applicationContext

    AmazonFileService amazonFileService;

    def grailsApplication

    String compileCssForDomain(String domain){
        // Instantiate the LESS compiler with some compiler options

        String temporalPath = "${grailsApplication.config.kuorum.upload.serverPath}"
        LessCompiler lessCompiler = new LessCompiler(Arrays.asList("--relative-urls", "--strict-math=on"));

        // Compile LESS input string to CSS output string
        String css = lessCompiler.compile("@mainColor: #FF0000;");

        def customDomainLess = applicationContext.getResource("less/customDomainCss.less")

        File customDomainCss = new File("${temporalPath}/${domain}.css")
        // Or compile LESS input file to CSS output file
        lessCompiler.compile(customDomainLess.file, customDomainCss);

        amazonFileService.uploadDomainCss(customDomainCss, domain);
        customDomainCss.delete();
    }

    String getUrlDomainCss(String domain){
        amazonFileService.getDomainCssUrl(domain)
    }
}
