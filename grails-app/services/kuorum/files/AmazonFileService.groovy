package kuorum.files

import com.amazonaws.AmazonServiceException
import com.amazonaws.auth.*
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.*
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


    RestKuorumApiService restKuorumApiService;

    public KuorumFile uploadTemporalFile(InputStream inputStream, KuorumUserSession userSession, String fileName, FileGroup fileGroup) throws KuorumException {
        KuorumFile kuorumFile = uploadLocalTemporalFile(inputStream, userSession, fileName, fileGroup, userSession.alias)
        uploadAmazonFile(kuorumFile, Boolean.TRUE)
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

    KuorumFile convertTemporalToFinalFile(KuorumFile kuorumFile) {
        if (kuorumFile?.temporal) {
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
            } else {
                uploadAmazonFile(kuorumFile, Boolean.FALSE)
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


    protected void copyAmazonFileFromTemporal(KuorumFile file, String destinationKey, Boolean attachTimestamp = false) {
        String sourceKey = file.storagePath
        if (sourceKey != destinationKey) {
            KuorumFile oldFile = KuorumFile.findByStoragePath(destinationKey);
            if (oldFile) {
                oldFile.delete()
            }
            String bucketName = grailsApplication.config.kuorum.amazon.bucketName;
            AmazonS3 s3Client = buildAmazonClient()

            try {
                // Copy the object into a new object in the same bucket.
                CopyObjectRequest copyObjRequest = new CopyObjectRequest(bucketName, sourceKey, bucketName, destinationKey);
                s3Client.copyObject(copyObjRequest);
                String finalUrl = buildAmazonUrl(destinationKey, attachTimestamp)
                file.temporal = false
                file.url = finalUrl
                file.urlThumb = finalUrl
                file.fileType = FileType.AMAZON_IMAGE
                file.local = false
                file.storagePath = destinationKey
                file.save(flush: true)
                log.info("Copy Amazon file success")

                DeleteObjectRequest deleteTemporalFileAmazonRequest = new DeleteObjectRequest(bucketName, sourceKey)
                s3Client.deleteObject(deleteTemporalFileAmazonRequest)

            }
            catch (AmazonServiceException e) {
                // The call was transmitted successfully, but Amazon S3 couldn't process
                // it, so it returned an error response.
                log.error("Error copying", e)
            }
        }
    }

    protected void uploadAmazonFile(KuorumFile kuorumFile, Boolean asTemporal) {
        if (kuorumFile.fileType == FileType.IMAGE) {
            String bucketName = grailsApplication.config.kuorum.amazon.bucketName;
            String keyName = generateAmazonKey(kuorumFile, asTemporal)
            String filePath = "${calculateLocalStoragePath(kuorumFile)}/${kuorumFile.fileName}";

            AmazonS3 s3Client = buildAmazonClient();


            // Create a list of UploadPartResponse objects. You get one of these for each part upload.
            List<PartETag> partETags = new ArrayList<PartETag>();


            File file = new File(filePath);
            long contentLength = file.length();
            long partSize = UPLOAD_PART_SIZE;

            // Step 1: Initialize.
            InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName, keyName);
            initRequest.putCustomRequestHeader("Cache-Control", "max-age=${3600 * 24 * 30}") // 30 days = 2592000 secs
            initRequest.putCustomRequestHeader("Expires", "Thu, 15 Apr 2050 00:00:00 GMT")

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
//                String finalUrl = uploadResult.getLocation().replaceAll("%2F", "/")
                String finalUrl = buildAmazonUrl(uploadResult.getKey())

                kuorumFile.temporal = asTemporal
                kuorumFile.url = finalUrl
                kuorumFile.urlThumb = finalUrl
                kuorumFile.fileType = asTemporal ? FileType.IMAGE : FileType.AMAZON_IMAGE
                kuorumFile.local = !asTemporal;
                kuorumFile.storagePath = keyName
                kuorumFile.save(flush: true)
                log.info("Se ha subido la imagen a Amazon. URL del exterior: ${kuorumFile.url}")
            } catch (Exception e) {
                s3Client.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, keyName, initResponse.getUploadId()));
                log.error("Ha habido un error subiendo el fichero a Amazon.", e);
            }
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
            s3client.deleteObject(new DeleteObjectRequest(bucketName, generateAmazonKey(file, temporal)));
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
    private String DOMAIN_CUSTOM_LOGO_FILE = "logo.png"
    private String DOMAIN_CUSTOM_FAVICON_FOLDER = "favicon"

    String getDomainCssUrl(String domain) {
        buildAmazonDomainUrl(domain, DOMAIN_CUSTOM_CSS_FILE)
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

    private AmazonS3 buildAmazonClient() {
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
