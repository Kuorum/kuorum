package kuorum.files

import grails.transaction.Transactional
import kuorum.KuorumFile
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.domain.DomainService
import org.kuorum.rest.model.domain.DomainRSDTO

import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

@Transactional
class DomainResourcesService {

    DomainService domainService
    AmazonFileService amazonFileService
    FaviconService faviconService

    private String RAW_STORAGE_PATH = "domains/%s/landingSlider/landing-%d.jpg"

    Path unzipFile(File zipFile, Path temp) {
        Path outputFolder = Files.createTempDirectory(temp, "faviconTemp");
        byte[] buffer = new byte[1024];
        try {
            ZipInputStream zis =
                    new ZipInputStream(new FileInputStream(zipFile));
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                String fileName = ze.getName();
                File newFile = new File(outputFolder.toString() + File.separator + fileName);
                System.out.println("file unzip : " + newFile.getAbsoluteFile());
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return outputFolder;
    }


    def uploadLogoFile(InputStream customLogo) {

        try {
            Path temp = Files.createTempDirectory("logoTemp");
            File logoFile = new File("/${temp}/logo.png")
            logoFile << customLogo
            DomainRSDTO domain = domainService.getConfig(CustomDomainResolver.domain)
            String amazonLogoUrl = amazonFileService.uploadDomainLogo(logoFile, CustomDomainResolver.domain)
            // Updating the config, updates the version. That allows to recover the new favicon and the new logo
            DomainRSDTO domainRSDTO = domainService.updateConfig(domain)
            faviconService.createFavicon("${amazonLogoUrl}?v=${domainRSDTO.version}", temp, domain)
            temp.toAbsolutePath().deleteDir()
        }
        catch (Exception e) {
            log.error("Ha fallado la subida del logo", e)
            throw e
        }
    }

    def uploadCarouselImages(KuorumFile slideFile1, KuorumFile slideFile2, KuorumFile slideFile3, String domain) {

        try {
            uploadCarouselImage(slideFile1, 1, domain)
            uploadCarouselImage(slideFile2, 2, domain)
            uploadCarouselImage(slideFile3, 3, domain)
        }
        catch (Exception e) {
            log.error("Ha fallado la subida de la imagen", e)
        }
    }

    private void uploadCarouselImage(KuorumFile file, int pos, String domain) {
        String destinationKey = String.format(RAW_STORAGE_PATH, domain, pos)
        amazonFileService.copyAmazonFileFromTemporal(file, destinationKey)
    }

    KuorumFile getSlidePath(String domain, int pos) {
        String storagePath = String.format(RAW_STORAGE_PATH, domain, pos)
        KuorumFile slideFile = KuorumFile.findByStoragePath(storagePath)
        return slideFile
    }
}