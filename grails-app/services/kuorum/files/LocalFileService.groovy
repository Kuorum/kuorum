package kuorum.files

import grails.transaction.Transactional
import kuorum.KuorumFile
import kuorum.core.FileGroup
import kuorum.core.FileType
import kuorum.core.exception.KuorumException
import kuorum.users.KuorumUser
import pl.burningice.plugins.image.BurningImageService

import javax.servlet.http.HttpServletResponse

@Transactional
class LocalFileService implements FileService{

    def grailsApplication
    BurningImageService burningImageService
    private static final TMP_PATH = "/tmp"
    private static final MODAL_BOX_WIDTH=558

    public KuorumFile uploadTemporalFile(InputStream inputStream, KuorumUser kuorumUser, String fileName, FileGroup fileGroup) throws KuorumException{
        return uploadLocalTemporalFile(inputStream, kuorumUser, fileName, fileGroup, kuorumUser.alias)
    }

    @Override
    KuorumFile uploadTemporalFile(InputStream inputStream, KuorumUser kuorumUser, String fileName, FileGroup fileGroup, String path) throws KuorumException {
        return uploadLocalTemporalFile(inputStream, kuorumUser, fileName, fileGroup, path)
    }

    @Override
    List<KuorumFile> listFilesFromPath(FileGroup fileGroup, String path) {
        //TODO
        return null
    }
/**
     * This class saves the inputStream in a local storage.
     *
     * NOTE: The option super.uploadTemporalFile not works properly
     *
     * @param inputStream
     * @param kuorumUser
     * @param fileName
     * @param fileGroup
     * @return
     * @throws KuorumException
     */
    protected KuorumFile uploadLocalTemporalFile(InputStream inputStream, KuorumUser kuorumUser, String fileName, FileGroup fileGroup, String path) throws KuorumException{
        String temporalPath = "${grailsApplication.config.kuorum.upload.serverPath}${TMP_PATH}"
        String rootUrl = "${grailsApplication.config.grails.serverURL}${grailsApplication.config.kuorum.upload.relativeUrlPath}${TMP_PATH}"

        if (!path.startsWith("/")){
            path = "/"+path
        }

        KuorumFile kuorumFile = new KuorumFile()
        kuorumFile.user = kuorumUser
        kuorumFile.temporal = Boolean.TRUE
        kuorumFile.fileGroup = fileGroup
        kuorumFile.fileName = "TEMPORAL"
        kuorumFile.originalName = fileName
        kuorumFile.relativePath = path
        kuorumFile.alt = fileName
        kuorumFile.storagePath = "TEMPORAL"
        kuorumFile.url ="http://TEMPORAL.com"
        kuorumFile.urlThumb = kuorumFile.url
        kuorumFile.save()//The ID is necessary

        def fileLocation = generatePath(kuorumFile)
        kuorumFile.fileName = "${kuorumFile.originalName}".toLowerCase()
        kuorumFile.storagePath = "$temporalPath/$fileLocation"
        kuorumFile.url ="$rootUrl/$fileLocation/$kuorumFile.fileName"
        kuorumFile.urlThumb = kuorumFile.url
        kuorumFile.save()

        log.info("Subiendo nuevo fichero a ${kuorumFile.storagePath}.  URL del exterior: ${kuorumFile.url}")

        def storagePathDirectory = new File(kuorumFile.storagePath)
        if (!storagePathDirectory.exists()) {
            log.info("Creating new directories ${kuorumFile.storagePath}")
            storagePathDirectory.mkdirs()
        }


        File file = new File("${kuorumFile.storagePath}/${kuorumFile.fileName}")
        upload(inputStream,file)
        if (file.bytes.length <= 0){
            log.error("Ocurre algo raro con el fichero subido. Si tamanio es inferior a 0: ${file.bytes.length}")
            file.delete()
            throw new KuorumException("No se ha podido guardar el fiechero", "error.file.empty")
        }else if (file.bytes.length> fileGroup.maxSize){
            log.debug("El fichero ha excedido el tamanio permitido: ${file.bytes.length}> ${fileGroup.maxSize}")
            file.delete()
            throw new KuorumException("Subiendo un fichero demasiado grande", "error.file.maxSizeExceded")
        }
        kuorumFile
    }

    /**
     * Recorta la imagen seg�n las medidas indicadas
     * @param kuorumFile
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    KuorumFile cropImage(KuorumFile kuorumFile, def x, def y, def h, def w){

        String folderPath = calculateLocalStoragePath(kuorumFile)
        String filePath = "${folderPath}/${kuorumFile.fileName}"
        burningImageService.doWith(filePath, folderPath)
                .execute {pl.burningice.plugins.image.engines.Action it ->
//            log.info(it.loadedImage.size.getWidth())
//            log.info(it.loadedImage.size.getHeight())
//            it.crop(x,y,h,w)
//            it.crop(x,y,w-0.00000000001,h-0.00000000001) // HEIGHT COMES WITH AN ERROR. Reducing the size 0.00000000001, the proportion is more or less the same and fix the problem
            it.crop(x,y,w-2,h-2);
//            if (kuorumFile.fileGroup.imageWidth >0 && kuorumFile.fileGroup.imageWidth < it.loadedImage.size.getWidth() ){
//                it.scaleAccurate(kuorumFile.fileGroup.imageWidth, kuorumFile.fileGroup.imageHeight)
//            }
        }
        postProcessCroppingImage(kuorumFile)
//        def imageWidth = fileGroup.imageWidth
//        burningImageService.doWith("${kuorumFile.storagePath}/${kuorumFile.fileName}", kuorumFile.storagePath)
//                .execute {
//            Float ratio = imageWidth  / it.loadedImage.getSize().width
////            if (it.loadedImage.getSize().width > withLightBox){
//            it.scaleAccurate(imageWidth, (it.loadedImage.getSize().height * ratio).round().intValue())
////            }
//
//        }
        return kuorumFile
    }

    protected String calculateLocalStoragePath(KuorumFile kuorumFile){
        String serverPath = grailsApplication.config.kuorum.upload.serverPath
        String temporalPath = "${serverPath}${TMP_PATH}"
        def fileLocation = generatePath(kuorumFile)
        "${kuorumFile.temporal?temporalPath:serverPath}/${fileLocation}"
    }

    protected KuorumFile postProcessCroppingImage(KuorumFile kuorumFile){

    }
    /**
     * Converts a temporal file to normal file.
     * @param KuorumFile
     * @return
     */
    KuorumFile convertTemporalToFinalFile(KuorumFile kuorumFile){
        if (kuorumFile?.temporal){
            String serverPath = grailsApplication.config.kuorum.upload.serverPath
            String rootUrl = "${grailsApplication.config.grails.serverURL}${grailsApplication.config.kuorum.upload.relativeUrlPath}"

            def fileLocation = generatePath(kuorumFile)
            def serverStoragePath = "$serverPath/$fileLocation"
            def finalUrl ="$rootUrl/$fileLocation/${kuorumFile.fileName}"

            File org = new File("${kuorumFile.storagePath}/${kuorumFile.fileName}")
            File destDir = new File(serverStoragePath)
            destDir.mkdirs()
            File dest = new File("$serverStoragePath/${kuorumFile.fileName}")

            try{
                if(org.renameTo(dest)){
                    deleteParentIfEmpty(org)
                    kuorumFile.temporal = Boolean.FALSE
                    kuorumFile.storagePath = serverStoragePath
                    kuorumFile.url =finalUrl
                    kuorumFile.urlThumb = finalUrl
                    kuorumFile.save()
                    log.info("Se ha movido el fichero de '${org.absolutePath}' a '${dest.absolutePath}. URL del exterior: ${kuorumFile.url}")
                }else{
                    log.error("No se ha podido mover el fichero de '${org.absolutePath}' a '${dest.absolutePath}")
                }
            }catch (Exception e){
                log.error("Hubo algun problema moviendo el fichero del temporal al final",e)
            }
        }
        kuorumFile
    }
    /**
     * Converts a normal file to temporal file.
     * @param KuorumFile
     * @return
     */
    KuorumFile convertFinalFileToTemporalFile(KuorumFile kuorumFile){
        if (kuorumFile){
            kuorumFile.temporal = true;
            kuorumFile.save();
        }
        kuorumFile
    }
    public KuorumFile createYoutubeKuorumFile(String youtubeUrl, KuorumUser user){
        def fileName = youtubeUrl.decodeYoutubeName()
        String urlThumb = this.recoverBestYoutubeQuality(fileName)
        KuorumFile multimedia = new KuorumFile(
                local:Boolean.FALSE,
                temporal:Boolean.FALSE,
                storagePath:null,
                alt:null,
                fileName:fileName,
                originalName: fileName,
                url:fileName.encodeAsYoutubeName(),
                urlThumb: urlThumb,
                fileGroup:FileGroup.YOUTUBE,
                fileType:FileType.YOUTUBE
        )
        multimedia.setUser(user)
        multimedia.save()
    }

    void deleteKuorumFile(KuorumFile file) {
        if (file){
            if (file.fileType == FileType.AMAZON_IMAGE){
                deleteAmazonFile(file);
            }else if (file.local){
                deleteFile(file);
            }

        }
    }

    private static final List<String> YOUTUBE_THUMBS=["maxresdefault.jpg","mqdefault.jpg", "0.jpg"]
    String recoverBestYoutubeQuality(String youtubeId) {
        String baseUrl= "https://img.youtube.com/vi/${youtubeId}/";
        for (String thumbName : YOUTUBE_THUMBS){
            String thumbUrl = baseUrl + thumbName;
            if (checkYoutubeThumb(thumbUrl)){
                return thumbUrl;
            }else{
                log.info("No se ha encontrado imagen ${thumbUrl} asociada al video de youtube")
            }
        }
        throw new KuorumException("No existe imagen de thumb para este video de youtube")
    }

    private boolean checkYoutubeThumb(String urlString){
        URL u = new URL(urlString);
        HttpURLConnection huc =  (HttpURLConnection)  u.openConnection();
        huc.setRequestMethod("GET");
        huc.connect();
        return huc.getResponseCode() == HttpServletResponse.SC_OK;
    }
/**
     * Deletes all temporal files uploaded by the user @user.
     *
     * Deletes on DB and on file system
     *
     * @param user
     */
    void deleteTemporalFiles(KuorumUser user){
        KuorumFile.findAllByUserIdAndTemporal(user.id, Boolean.TRUE).each {KuorumFile kuorumFile ->
            deleteFile(kuorumFile)
        }
    }

    //TODO: Hacer bien las herenceias que esto es una castaña
    protected void deleteAmazonFile(KuorumFile file){

    }

    protected void deleteFile(KuorumFile kuorumFile){
        if (kuorumFile.local){
            File file = new File("${kuorumFile.storagePath}/${kuorumFile.fileName}")
            if (!file.exists()){
                kuorumFile.delete()
            }else{
                if (file.delete()){
                    kuorumFile.delete()
                    deleteParentIfEmpty(file)
                }else{
                    log.error("Error deleting file ${file.absolutePath}")
                    kuorumFile["errorDeleting"]=true
                    kuorumFile.save()
                }
            }
        }

    }

    private void deleteParentIfEmpty(File file){
        File parent = new File(file.parent)
        if (parent.delete()){
            deleteParentIfEmpty(parent)
        }
    }

    /**
     * Returns the relative path for an ID. Prefixed by the fileGroup.
     * The returned path is without slashes
     *
     * Exapmle: ID = 123, GROUP=PROJECT_FILE
     *
     * ProjectsFiles/00/00/00/01/23
     *
     * @param fileGroup
     * @param id
     * @return
     */
    private String generatePath(KuorumFile kuorumFile){

        String res = kuorumFile.id.toString()

        String subFolders = ""
        while(res.size()>2){
            subFolders += res.substring(0,2)+"/"
            res = res.substring(2)
        }
        subFolders += res + kuorumFile.relativePath
        "${kuorumFile.fileGroup.folderPath}/${subFolders}"
    }

    private String getExtension(String fileName){
        fileName?fileName.split("\\.").last():""
    }

    private void upload(InputStream inputStream, File file) {

        try {
            if (file.exists()){
                file.delete()
            }
            log.info("Cargando fichero subido en :${file.absolutePath}")
            file << inputStream
        } catch (Exception e) {
            //TODO: Gestion Errores
            log.error("Error guardando fichero: "+e.getMessage(), e)
            throw new KuorumException("No se ha podido guardar el fiechero", "error.file.fileUploadServerError")
        }

    }

    @Override
    public KuorumFile createExternalFile(KuorumUser owner, String url, FileGroup fileGroup, FileType fileType){
        KuorumFile file = new KuorumFile()
        String fileName = url.split("/").last()
        file.setFileName(fileName)
        file.setFileGroup(fileGroup)
        file.setFileType(fileType)
        file.setTemporal(false)
        file.setAlt(fileName)
        file.setLocal(false)
        file.setUrl(url)
        file.setUser(owner)
        file.setOriginalName(fileName)
        file.setUrlThumb(url)
        file.save()
    }

    @Override
    InputStream readFile(KuorumFile kuorumFile) {
        //TODO: Read LOCAL FILE
        return null
    }
}
