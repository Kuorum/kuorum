package kuorum.files

import grails.transaction.Transactional
import kuorum.KuorumFile
import kuorum.core.FileGroup
import kuorum.core.FileType
import kuorum.core.exception.KuorumException
import kuorum.users.KuorumUser

import javax.servlet.http.HttpServletResponse

@Transactional
class LocalFileService implements FileService{

    def grailsApplication
    def burningImageService
    private static final TMP_PATH = "/tmp"
    private static final MODAL_BOX_WIDTH=558

    public KuorumFile uploadTemporalFile(InputStream inputStream, KuorumUser kuorumUser, String fileName, FileGroup fileGroup) throws KuorumException{
        String temporalPath = "${grailsApplication.config.kuorum.upload.serverPath}${TMP_PATH}"
        String rootUrl = "${grailsApplication.config.grails.serverURL}${grailsApplication.config.kuorum.upload.relativeUrlPath}${TMP_PATH}"


        KuorumFile kuorumFile = new KuorumFile()
        kuorumFile.user = kuorumUser
        kuorumFile.temporal = Boolean.TRUE
        kuorumFile.fileGroup = fileGroup
        kuorumFile.fileName = "TEMPORAL"
        kuorumFile.originalName = fileName
        kuorumFile.storagePath = "TEMPORAL"
        kuorumFile.url ="http://TEMPORAL.com"
        kuorumFile.urlThumb = kuorumFile.url
        kuorumFile.save()//The ID is necessary

        def fileLocation = generatePath(kuorumFile)
        kuorumFile.fileName = "${kuorumFile.id}.${getExtension(fileName)}".toLowerCase()
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

        burningImageService.doWith("${kuorumFile.storagePath}/${kuorumFile.fileName}", kuorumFile.storagePath)
                .execute {
            it.crop(x,y,w,h)
//            it.crop(x,y,h,w)
        }
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
                url:youtubeUrl,
                urlThumb: urlThumb,
                fileGroup:FileGroup.YOUTUBE,
                fileType:FileType.YOUTUBE
        )
        multimedia.setUser(user)
        multimedia.save()
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

    void deleteFile(KuorumFile kuorumFile){
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
        subFolders += res
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
}
