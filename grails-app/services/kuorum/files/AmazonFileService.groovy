package kuorum.files

import com.amazonaws.AmazonClientException
import com.amazonaws.AmazonServiceException
import com.amazonaws.auth.*
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.*
import grails.plugin.springsecurity.SpringSecurityService
import kuorum.KuorumFile
import kuorum.core.FileGroup
import kuorum.core.FileType
import kuorum.core.exception.KuorumException
import kuorum.register.KuorumUserSession
import kuorum.util.rest.RestKuorumApiService

//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
class AmazonFileService extends LocalFileService {

    private static final String BUCKET_TMP_FOLDER = "tmp"

    private static final long UPLOAD_PART_SIZE = 5242880; // Set part size to 5 MB.

    private Regions AWS_REGION = Regions.EU_WEST_1;

    SpringSecurityService springSecurityService;


    RestKuorumApiService restKuorumApiService;

    public KuorumFile uploadTemporalFile(InputStream inputStream, KuorumUserSession userSession, String fileName, FileGroup fileGroup) throws KuorumException {
        KuorumFile kuorumFile = uploadLocalTemporalFile(inputStream, userSession, fileName, fileGroup, userSession.alias)
        uploadAmazonFile(kuorumFile, Boolean.TRUE, userSession)
        kuorumFile
    }

    @Override
    KuorumFile uploadTemporalFile(InputStream inputStream, KuorumUserSession userSession, String fileName, FileGroup fileGroup, String path) throws KuorumException {
        KuorumFile kuorumFile = uploadLocalTemporalFile(inputStream, userSession, fileName, fileGroup, path)
        uploadAmazonFile(kuorumFile, Boolean.TRUE)
        kuorumFile
    }

    @Override
    List<KuorumFile> listFilesFromPath(FileGroup fileGroup, String path) {

        String bucketName = grailsApplication.config.kuorum.amazon.bucketName;
        AmazonS3 s3Client = buildAmazonClient()

        String searchPath = fileGroup.folderPath + "/" + cleanPath(path)
        ObjectListing listing = s3Client.listObjects(bucketName, searchPath);
        List<S3ObjectSummary> summaries = listing.getObjectSummaries();
        while (listing.isTruncated()) {
            listing = s3Client.listNextBatchOfObjects(listing);
            summaries.addAll(listing.getObjectSummaries());
        }

        List<KuorumFile> files = listing.objectSummaries.collect { S3ObjectSummary s3o ->
            String encodedKey = s3o.key.split("/").collect { java.net.URLEncoder.encode(it, "UTF-8") }.join("/")
            String url = buildAmazonUrl(encodedKey)
            return KuorumFile.findByUrl(url)
        }
        return files
    }

    /**
     * FileGroups promoted by a dedicated backend endpoint, which stores the file and returns its final URL.
     * Any other FileGroup is promoted with an S3 copy out of the "tmp" folder (see copyAmazonFileFromTemporal).
     */
    private static final List<FileGroup> GROUPS_PROMOTED_BY_BACKEND = [
            FileGroup.USER_AVATAR, FileGroup.USER_PROFILE, FileGroup.PROJECT_IMAGE, FileGroup.MASS_MAIL_IMAGE
    ]

    KuorumFile convertTemporalToFinalFile(KuorumFile kuorumFile) {
        if (kuorumFile?.temporal) {
            if (!GROUPS_PROMOTED_BY_BACKEND.contains(kuorumFile.fileGroup)) {
                // e.g. FileGroup.ASSOCIATION_IMAGE_PROFILE (ContestApplication's collaborating association logo).
                // The backend only offers the "tmp" upload for them, so re-uploading would leave the file in "tmp" again.
                if (!copyAmazonFileFromTemporal(kuorumFile, generateFinalAmazonKey(kuorumFile))) {
                    throw new KuorumException("The temporal file can not be moved to its final path", "error.file.empty")
                }
                File localFile = new File("${calculateLocalStoragePath(kuorumFile)}/${kuorumFile.fileName}")
                localFile.delete()
                deleteParentIfEmpty(localFile)
                return kuorumFile
            }
            String localTempPath = calculateLocalStoragePath(kuorumFile)
            File org = new File("${localTempPath}/${kuorumFile.fileName}")
            if (!org.exists()) {
                log.info("File not exits. This request comes from other server. Downloading it from Amazon")
                org = downloadAmazonFileToLocal(kuorumFile);
            }
            if (kuorumFile.fileGroup == FileGroup.USER_AVATAR) {
                String fileUrl = uploadAvatar(kuorumFile, org)
                kuorumFile.setUrl(fileUrl)
            } else if (kuorumFile.fileGroup == FileGroup.USER_PROFILE) {
                String fileUrl = uploadImgProfile(kuorumFile, org)
                kuorumFile.setUrl(fileUrl)
            } else if (kuorumFile.fileGroup == FileGroup.PROJECT_IMAGE) {
                String fileUrl = uploadCampaignImage(kuorumFile, org)
                kuorumFile.setUrl(fileUrl)
            } else if (kuorumFile.fileGroup == FileGroup.MASS_MAIL_IMAGE) {
                String fileUrl = uploadNewsletterImage(kuorumFile, org)
                kuorumFile.setUrl(fileUrl)
            }

            org.delete();
            deleteParentIfEmpty(org);
            deleteAmazonFile(kuorumFile, true)

            kuorumFile.temporal = Boolean.FALSE
            kuorumFile.fileType = FileType.AMAZON_IMAGE
            kuorumFile.local = Boolean.FALSE
//            kuorumFile.save(flush: true)
            kuorumFile.save()
            log.info("Se ha subido la imagen a Amazon. URL del exterior: ${kuorumFile.url}")

        }
        kuorumFile
    }

    private File downloadAmazonFileToLocal(KuorumFile file) {
        log.info("Downloading ${file.relativePath} from Amazon")
        File localFile = File.createTempFile("Kuorum_", "amazon");
        String sourceKey = file.storagePath
        String bucketName = grailsApplication.config.kuorum.amazon.bucketName;
        AmazonS3 s3Client = buildAmazonClient()

        try {
            // Copy the object into a new object in the same bucket.
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, sourceKey);
            S3Object s3Object = s3Client.getObject(getObjectRequest);
            java.nio.file.Files.copy(
                    s3Object.getObjectContent(),
                    localFile.toPath(),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        }
        catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            log.error("Error downloading", e)
        }
        return localFile;
    }

    private String uploadAvatar(KuorumFile kuorumFile, File avatarFile) {
        String fileName = java.net.URLEncoder.encode(kuorumFile.fileName, "UTF-8")
        Map<String, String> params = [userId: kuorumFile.user.id.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.putFile(
                RestKuorumApiService.ApiMethod.USER_IMG_AVATAR,
                params,
                query,
                avatarFile,
                fileName
        )
    }

    private String uploadImgProfile(KuorumFile kuorumFile, File avatarFile) {
        String fileName = java.net.URLEncoder.encode(kuorumFile.fileName, "UTF-8")
        Map<String, String> params = [userId: kuorumFile.user.id.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.putFile(
                RestKuorumApiService.ApiMethod.USER_IMG_PROFILE,
                params,
                query,
                avatarFile,
                fileName
        )
    }

    private String uploadCampaignImage(KuorumFile kuorumFile, File avatarFile) {
        String fileName = java.net.URLEncoder.encode(kuorumFile.fileName, "UTF-8")
        Map<String, String> params = [campaignId: kuorumFile.campaignId.toString(), userId: kuorumFile.user.id.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.putFile(
                RestKuorumApiService.ApiMethod.ACCOUNT_CAMPAIGN_PICTURE,
                params,
                query,
                avatarFile,
                fileName
        )
    }

    private String uploadNewsletterImage(KuorumFile kuorumFile, File avatarFile) {
        String fileName = java.net.URLEncoder.encode(kuorumFile.fileName, "UTF-8")
        Map<String, String> params = [campaignId: kuorumFile.campaignId.toString(), userId: kuorumFile.user.id.toString()]
        Map<String, String> query = [:]
        def response = restKuorumApiService.putFile(
                RestKuorumApiService.ApiMethod.ACCOUNT_MASS_MAILING_PICTURE,
                params,
                query,
                avatarFile,
                fileName
        )
    }


    /**
     * Moves a temporal file out of the "tmp" folder with a server side S3 copy and points the KuorumFile to destinationKey.
     * S3 and Mongo can not share a transaction, so the order is compensating: copy, save, and only then delete the tmp object.
     * If the copy or the save fails the file keeps its temporal state and the tmp object is kept. A final object created by
     * this call is deleted on a failed save, but never one that already existed in destinationKey (e.g. a domain carousel
     * slide that is being replaced).
     * @return true if the file has been moved
     */
    protected boolean copyAmazonFileFromTemporal(KuorumFile file, String destinationKey, Boolean attachTimestamp = false) {
        String sourceKey = file.storagePath.encodeAsAmazonS3Key();
        if (sourceKey == destinationKey) {
            return false
        }
        KuorumFile oldFile = KuorumFile.findByStoragePath(destinationKey);
        if (oldFile && oldFile.id != file.id) {
            oldFile.delete()
        }
        String bucketName = grailsApplication.config.kuorum.amazon.bucketName;
        AmazonS3 s3Client = buildAmazonClient()

        boolean destinationExisted
        try {
            destinationExisted = s3Client.doesObjectExist(bucketName, destinationKey)
            // Copy the object into a new object in the same bucket.
            s3Client.copyObject(new CopyObjectRequest(bucketName, sourceKey, bucketName, destinationKey));
        } catch (AmazonClientException e) {
            log.error("Error copying from '" + sourceKey + "' to '" + destinationKey + "'", e)
            return false
        }

        Map previousState = [temporal: file.temporal, url: file.url, urlThumb: file.urlThumb,
                             fileType: file.fileType, local: file.local, storagePath: file.storagePath]
        String finalUrl = buildAmazonUrl(destinationKey, attachTimestamp)
        boolean saved = false
        try {
            file.temporal = false
            file.url = finalUrl
            file.urlThumb = finalUrl
            file.fileType = FileType.AMAZON_IMAGE
            file.local = false
            file.storagePath = destinationKey
            saved = file.save(flush: true) != null // save() returns null on a validation error, it does not throw
        } catch (Exception e) {
            log.error("KuorumFile ${file.id} could not be saved after copying it to '${destinationKey}'", e)
        }
        if (!saved) {
            previousState.each { String property, value -> file."${property}" = value }
            if (!destinationExisted && !deleteAmazonKeyQuietly(destinationKey)) {
                log.error("ORPHAN_S3_KEY ${destinationKey} (KuorumFile ${file.id}) could not be rolled back")
            }
            return false
        }
        log.info("Copy Amazon file success")
        deleteAmazonKeyQuietly(sourceKey)
        return true
    }

    /**
     * Key of a file once it has left the "tmp" folder. It is unique per KuorumFile (its id) and deterministic:
     * two users uploading "logo.png" can not collide, and a retry copies onto the same key instead of creating another object.
     */
    protected String generateFinalAmazonKey(KuorumFile file) {
        return "${file.fileGroup.folderPath}/${file.userId}/${file.id}/${file.fileName}"
    }

    /** Best effort delete of an S3 object. Never throws; a failure is logged. @return true if it has been deleted */
    protected boolean deleteAmazonKeyQuietly(String key) {
        try {
            String bucketName = grailsApplication.config.kuorum.amazon.bucketName
            buildAmazonClient().deleteObject(new DeleteObjectRequest(bucketName, key))
            return true
        } catch (Exception e) {
            log.warn("Amazon object '${key}' could not be deleted", e)
            return false
        }
    }

    protected void uploadAmazonFile(KuorumFile kuorumFile, Boolean asTemporal) {
        uploadAmazonFile(kuorumFile, asTemporal, springSecurityService.principal)
    }
    protected void uploadAmazonFile(KuorumFile kuorumFile, Boolean asTemporal, KuorumUserSession user) {
        // Excludes YOUTUBE links (no binary to upload). Cannot require fileType == IMAGE: the very
        // first call (temp upload) already flips it to AMAZON_IMAGE, so a later re-upload of the same
        // KuorumFile - e.g. promoting a temporal file to its final URL, or re-uploading after a crop -
        // would otherwise silently no-op and leave the temporal URL persisted forever.
        if (kuorumFile.fileType != FileType.YOUTUBE) {
            String filePath = "${calculateLocalStoragePath(kuorumFile)}/${kuorumFile.fileName}";
            File file = new File(filePath);

            String fileName = java.net.URLEncoder.encode(kuorumFile.fileName, "UTF-8")
            Map<String, String> params = [userId: user.id.toString()]
            Map<String, String> query = [:]
            def response = restKuorumApiService.putFile(
                    RestKuorumApiService.ApiMethod.FILE_UPLOAD,
                    params,
                    query,
                    file,
                    fileName
            )

            String finalUrl = response
            kuorumFile.temporal = asTemporal
            kuorumFile.url = finalUrl
            kuorumFile.urlThumb = finalUrl
            kuorumFile.fileType = FileType.AMAZON_IMAGE
            kuorumFile.local = !asTemporal;
            kuorumFile.storagePath = new URL(finalUrl).getPath()
            kuorumFile.save(flush: true)
            log.info("Se ha subido la imagen a Amazon. URL del exterior: ${kuorumFile.url}")
        }
    }

    String uploadDomainLogo(File file, String domain) {
        String keyName = "${DOMAIN_PATH}/${domain}/${DOMAIN_CUSTOM_LOGO_FILE}"
        String urlLogo = uploadFileToAmazon(file, "image/png", keyName, true)
        log.info("Se ha subido un nuevo al dominio ${domain} => ${urlLogo}")
        return urlLogo
    }

    String uploadDomainFaviconFile(File file, String domain) {
        String keyName = "${DOMAIN_PATH}/${domain}/${DOMAIN_CUSTOM_FAVICON_FOLDER}/${file.name}"
        String contentType = file.name == 'favicon.ico' ? "image/x-icon" : "image/png"
        String urlLogo = uploadFileToAmazon(file, contentType, keyName)
        return urlLogo
    }

    private String uploadFileToAmazon(File file, String contentType, String key, Boolean attachTimestamp = false) {
        String bucketName = grailsApplication.config.kuorum.amazon.bucketName;
        String keyName = key

        AmazonS3 s3Client = buildAmazonClient()


        // Create a list of UploadPartResponse objects. You get one of these for each part upload.
        List<PartETag> partETags = new ArrayList<PartETag>();


        long contentLength = file.length();
        long partSize = UPLOAD_PART_SIZE;

        // Step 1: Initialize.
        InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName, keyName);
        initRequest.putCustomRequestHeader("Cache-Control", "max-age=${3600 * 24 * 30}") // 30 days = 2592000 secs
        initRequest.putCustomRequestHeader("Expires", "Thu, 15 Apr 2050 00:00:00 GMT")
        ObjectMetadata metadata = new ObjectMetadata();
//        metadata.setContentEncoding("gzip");
//        metadata.setContentType("text/css");
        metadata.setContentType(contentType);
        metadata.setLastModified(new Date())

        initRequest.setObjectMetadata(metadata)

        InitiateMultipartUploadResult initResponse = s3Client.initiateMultipartUpload(initRequest);



        try {
            // Step 2: Upload parts.
            long filePosition = 0;
            for (int i = 1; filePosition < contentLength; i++) {
                // Last part can be less than 5 MB. Adjust part size.
                partSize = Math.min(partSize, (contentLength - filePosition));

                // Create request to upload a part.
                UploadPartRequest uploadRequest = new UploadPartRequest()
                        .withBucketName(bucketName)
                        .withKey(keyName)
                        .withUploadId(initResponse.getUploadId())
                        .withPartNumber(i)
                        .withFileOffset(filePosition)
                        .withFile(file)
                        .withPartSize(partSize);

                // Upload part and add response to our list.
                partETags.add(s3Client.uploadPart(uploadRequest).getPartETag());

                filePosition += partSize;
            }

            // Step 3: Complete.
            CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(
                    bucketName,
                    keyName,
                    initResponse.getUploadId(),
                    partETags);

            CompleteMultipartUploadResult uploadResult = s3Client.completeMultipartUpload(compRequest);
            String finalUrl = uploadResult.getLocation().replaceAll("%2F", "/")

//            log.info("Se ha subido el nuevo archivo del dominio")
            return buildAmazonUrl(keyName, attachTimestamp)
//            return finalUrl;
        } catch (Exception e) {
            s3Client.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, keyName, initResponse.getUploadId()));
            log.error("Ha habido un error subiendo el fichero a Amazon.", e);
            return null;
        }
    }

    protected KuorumFile postProcessCroppingImage(KuorumFile kuorumFile) {
        deleteAmazonFile(kuorumFile, true)
        uploadAmazonFile(kuorumFile, true)
        return kuorumFile
    }

    protected void deleteAmazonFile(KuorumFile file, boolean temporal = false) {
        if (file && file.fileType == FileType.AMAZON_IMAGE) {
            String bucketName = grailsApplication.config.kuorum.amazon.bucketName;
            AmazonS3 s3client = buildAmazonClient()
            // A file promoted with promoteTemporalToFinalKey lives in its own final key, not in the folder/relativePath one
            String key = (!temporal && file.storagePath == generateFinalAmazonKey(file)) ? file.storagePath : generateAmazonKey(file, temporal)
            s3client.deleteObject(new DeleteObjectRequest(bucketName, key));
//            s3client.deleteObject(new DeleteObjectRequest(bucketName, file.storagePath)); //OLD IMAGES NOT HAS storagePath
        }
    }

    private String generateAmazonKey(KuorumFile file, boolean temporal = false) {
        if (file.relativePath && file.relativePath.size() > 1) { // relativePath !="/"
            return "${temporal ? BUCKET_TMP_FOLDER : file.fileGroup.folderPath}/${cleanPath(file.relativePath)}/${file.fileName}"
        } else {
            return "${temporal ? BUCKET_TMP_FOLDER : file.fileGroup.folderPath}/${file.fileName}"
        }
    }

    private String cleanPath(String path) {
        if (path && path.startsWith("/")) {
            return path.substring(1)
        } else if (path) {
            return path
        } else {
            return "";
        }

    }

    void uploadDomainCss(File file, String domain) {
        String keyName = "${DOMAIN_PATH}/${domain}/${DOMAIN_CUSTOM_CSS_FILE}"
        uploadFileToAmazon(file, "text/css", keyName)
    }


    private String DOMAIN_PATH = "domains"
    private String DOMAIN_CUSTOM_CSS_FILE = "custom.css"
    private String DOMAIN_CUSTOM_TRICKS_CSS_FILE = "custom-tricks.css"
    private String DOMAIN_CUSTOM_LOGO_FILE = "logo.png"
    private String DOMAIN_CUSTOM_FAVICON_FOLDER = "favicon"

    String getDomainCssUrl(String domain) {
        buildAmazonDomainUrl(domain, DOMAIN_CUSTOM_CSS_FILE)
    }

    String getDomainCssTricksUrl(String domain) {
        buildAmazonDomainUrl(domain, DOMAIN_CUSTOM_TRICKS_CSS_FILE)
    }


    String getDomainLogoUrl(String domain) {
        buildAmazonDomainUrl(domain, DOMAIN_CUSTOM_LOGO_FILE)
    }

    String getStaticRootDomainPath(String domain) {
        buildAmazonDomainUrl(domain, "")[0..-2]
    }

    private String buildAmazonDomainUrl(String domain, String key) {
        String keyName = "${DOMAIN_PATH}/${domain}/${key}"
        return buildAmazonUrl(keyName)
    }


    private String buildAmazonUrl(String relativePath, Boolean attachTimestamp = false) {
        String cdnHost = grailsApplication.config.grails.resources.mappers.amazoncdn.host;
        if (attachTimestamp){
            Date date= new Date();
            return "${cdnHost}/${relativePath}?timestamp=${date.time}"
        }else{
            return "${cdnHost}/${relativePath}"
        }
    }

    protected AmazonS3 buildAmazonClient() {
        // Login with credentials
        String accessKey = grailsApplication.config.kuorum?.amazon?.accessKey
        String secretKey = grailsApplication.config.kuorum?.amazon?.secretKey
        String bucketRegion = grailsApplication.config.kuorum.amazon.bucketRegion;
        AWSCredentialsProvider credentialsProvider = null;
        if (accessKey && !accessKey.equals("NO_ACCESS_KEY")){
            log.warn("Using credentials from the properties file [$accessKey]. You should use the instance credentials")
            AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
            credentialsProvider = new AWSStaticCredentialsProvider(credentials);
        }else{
            credentialsProvider = new DefaultAWSCredentialsProviderChain();
        }

        AmazonS3ClientBuilder clientBuilder = AmazonS3ClientBuilder
                .standard()
                .withCredentials(credentialsProvider)
                .withRegion(bucketRegion);


        AmazonS3 s3Client = clientBuilder.build();
        return s3Client
    }
}
