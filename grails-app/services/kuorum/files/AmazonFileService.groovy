package kuorum.files

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.*
import kuorum.KuorumFile
import kuorum.core.FileGroup
import kuorum.core.FileType
import kuorum.core.exception.KuorumException
import kuorum.users.KuorumUser

class AmazonFileService extends LocalFileService{

    private static final String BUCKET_TMP_FOLDER= "tmp"

    private static final long UPLOAD_PART_SIZE = 5242880; // Set part size to 5 MB.


    public KuorumFile uploadTemporalFile(InputStream inputStream, KuorumUser kuorumUser, String fileName, FileGroup fileGroup) throws KuorumException{
        KuorumFile kuorumFile = uploadLocalTemporalFile(inputStream, kuorumUser, fileName, fileGroup, kuorumUser.alias)
        uploadAmazonFile(kuorumFile, Boolean.TRUE)
        kuorumFile
    }

    @Override
    KuorumFile uploadTemporalFile(InputStream inputStream, KuorumUser kuorumUser, String fileName, FileGroup fileGroup, String path) throws KuorumException {
        KuorumFile kuorumFile = uploadLocalTemporalFile(inputStream, kuorumUser, fileName, fileGroup, path)
        uploadAmazonFile(kuorumFile, Boolean.TRUE)
        kuorumFile
    }

    @Override
    List<KuorumFile> listFilesFromPath(FileGroup fileGroup, String path) {
        String accessKey = grailsApplication.config.kuorum.amazon.accessKey
        String secretKey = grailsApplication.config.kuorum.amazon.secretKey
        String bucketName = grailsApplication.config.kuorum.amazon.bucketName;

        AWSCredentials credentials = new BasicAWSCredentials(accessKey,secretKey);
        AmazonS3 s3Client = new AmazonS3Client(credentials);

        String searchPath = fileGroup.folderPath+"/"+cleanPath(path)
        ObjectListing listing = s3Client.listObjects( bucketName, searchPath );
        List<S3ObjectSummary> summaries = listing.getObjectSummaries();
        while (listing.isTruncated()) {
            listing = s3Client.listNextBatchOfObjects (listing);
            summaries.addAll (listing.getObjectSummaries());
        }

        List<KuorumFile> files = listing.objectSummaries.collect{S3ObjectSummary s3o ->
            String encodedKey = s3o.key.split("/").collect{java.net.URLEncoder.encode(it, "UTF-8")}.join("/")
            String url = "https://${bucketName}.s3.amazonaws.com/${encodedKey}"
            return KuorumFile.findByUrl(url)
        }
        return files
    }

    KuorumFile convertTemporalToFinalFile(KuorumFile kuorumFile){
        if (kuorumFile?.temporal){
            String localTempPath = calculateLocalStoragePath(kuorumFile)
            File org = new File("${localTempPath}/${kuorumFile.fileName}")
            uploadAmazonFile(kuorumFile, Boolean.FALSE)

            org.delete();
            deleteParentIfEmpty(org);
            deleteAmazonFile(kuorumFile, true)

            kuorumFile.temporal = Boolean.FALSE
            kuorumFile.fileType = FileType.AMAZON_IMAGE
            kuorumFile.local = Boolean.FALSE
            kuorumFile.save(flush: true)
            log.info("Se ha subido la imagen a Amazon. URL del exterior: ${kuorumFile.url}")

        }
        kuorumFile
    }

    protected void uploadAmazonFile(KuorumFile kuorumFile, Boolean asTemporal){
        if (kuorumFile.fileType == FileType.IMAGE){
            String accessKey = grailsApplication.config.kuorum.amazon.accessKey
            String secretKey = grailsApplication.config.kuorum.amazon.secretKey
            String bucketName = grailsApplication.config.kuorum.amazon.bucketName;
            String keyName    = generateAmazonKey(kuorumFile, asTemporal)
            String filePath   = "${calculateLocalStoragePath(kuorumFile)}/${kuorumFile.fileName}";

            AWSCredentials credentials = new BasicAWSCredentials(accessKey,secretKey);

            AmazonS3 s3Client = new AmazonS3Client(credentials);


            // Create a list of UploadPartResponse objects. You get one of these for each part upload.
            List<PartETag> partETags = new ArrayList<PartETag>();


            File file = new File(filePath);
            long contentLength = file.length();
            long partSize = UPLOAD_PART_SIZE;

            // Step 1: Initialize.
            InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName, keyName);
            initRequest.putCustomRequestHeader("Cache-Control", "max-age=${3600*24*30}") // 30 days = 2592000 secs
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
                CompleteMultipartUploadRequest compRequest = new
                CompleteMultipartUploadRequest(
                        bucketName,
                        keyName,
                        initResponse.getUploadId(),
                        partETags);

                CompleteMultipartUploadResult uploadResult = s3Client.completeMultipartUpload(compRequest);
                String finalUrl = uploadResult.getLocation().replaceAll("%2F", "/")

                kuorumFile.temporal = asTemporal
                kuorumFile.url =finalUrl
                kuorumFile.urlThumb = finalUrl
                kuorumFile.fileType = asTemporal?FileType.IMAGE:FileType.AMAZON_IMAGE
                kuorumFile.local = !asTemporal;
                kuorumFile.storagePath = keyName
                kuorumFile.save(flush: true)
                log.info("Se ha subido la imagen a Amazon. URL del exterior: ${kuorumFile.url}")
            } catch (Exception e) {
                s3Client.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, keyName, initResponse.getUploadId()));
                log.error("Ha habido un error subiendo el fichero a Amazon.",e);
            }
        }
    }

    protected KuorumFile postProcessCroppingImage(KuorumFile  kuorumFile){
        deleteAmazonFile(kuorumFile, true)
        uploadAmazonFile(kuorumFile, true)
        return kuorumFile
    }

    protected void deleteAmazonFile(KuorumFile file, boolean temporal = false){
        if (file && file.fileType==FileType.AMAZON_IMAGE){
            String accessKey = grailsApplication.config.kuorum.amazon.accessKey
            String secretKey = grailsApplication.config.kuorum.amazon.secretKey
            String bucketName = grailsApplication.config.kuorum.amazon.bucketName;
            AWSCredentials credentials = new BasicAWSCredentials(accessKey,secretKey)
            AmazonS3 s3client = new AmazonS3Client(credentials);
//            AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
            s3client.deleteObject(new DeleteObjectRequest(bucketName, generateAmazonKey(file, temporal)));
//            s3client.deleteObject(new DeleteObjectRequest(bucketName, file.storagePath)); //OLD IMAGES NOT HAS storagePath
        }
    }

    private String generateAmazonKey(KuorumFile file, boolean temporal = false){
        if (file.relativePath && file.relativePath.size()>1){ // relativePath !="/"
            return "${temporal?BUCKET_TMP_FOLDER:file.fileGroup.folderPath}/${cleanPath(file.relativePath)}/${file.fileName}"
        }else{
            return "${temporal?BUCKET_TMP_FOLDER:file.fileGroup.folderPath}/${file.fileName}"
        }
    }

    private String cleanPath(String path){
        if (path && path.startsWith("/")){
            return path.substring(1)
        }else if (path){
            return path
        }else{
            return "";
        }

    }

    @Override
    InputStream readFile(KuorumFile kuorumFile) {
        String accessKey = grailsApplication.config.kuorum.amazon.accessKey
        String secretKey = grailsApplication.config.kuorum.amazon.secretKey
        String bucketName = grailsApplication.config.kuorum.amazon.bucketName;
        AWSCredentials credentials = new BasicAWSCredentials(accessKey,secretKey)
        AmazonS3 s3Client = new AmazonS3Client(credentials);
        S3Object object = s3Client.getObject( new GetObjectRequest(bucketName, kuorumFile.storagePath));
        InputStream objectData = object.getObjectContent();
//        objectData.close();
        return objectData
    }

    private String DOMAIN_PATH = "domains"
    private String DOMAIN_CUSTOM_CSS_FILE = "custom.css"
    String getDomainCssUrl(String domain){
        String keyName    = "${DOMAIN_PATH}/${domain}/${DOMAIN_CUSTOM_CSS_FILE}"
        String bucketName = grailsApplication.config.kuorum.amazon.bucketName;
        return "https://${bucketName}.s3.amazonaws.com/${keyName}"
    }

    void uploadDomainCss(File file, String domain){
        String accessKey = grailsApplication.config.kuorum.amazon.accessKey
        String secretKey = grailsApplication.config.kuorum.amazon.secretKey
        String bucketName = grailsApplication.config.kuorum.amazon.bucketName;
        String keyName    = "${DOMAIN_PATH}/${domain}/${DOMAIN_CUSTOM_CSS_FILE}"

        AWSCredentials credentials = new BasicAWSCredentials(accessKey,secretKey);

        AmazonS3 s3Client = new AmazonS3Client(credentials);


        // Create a list of UploadPartResponse objects. You get one of these for each part upload.
        List<PartETag> partETags = new ArrayList<PartETag>();


        long contentLength = file.length();
        long partSize = UPLOAD_PART_SIZE;

        // Step 1: Initialize.
        InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName, keyName);
        initRequest.putCustomRequestHeader("Cache-Control", "max-age=${3600*24*30}") // 30 days = 2592000 secs
        initRequest.putCustomRequestHeader("Expires", "Thu, 15 Apr 2050 00:00:00 GMT")
        ObjectMetadata metadata = new ObjectMetadata();
//        metadata.setContentEncoding("gzip");
        metadata.setContentType("text/css");
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
            CompleteMultipartUploadRequest compRequest = new
                    CompleteMultipartUploadRequest(
                    bucketName,
                    keyName,
                    initResponse.getUploadId(),
                    partETags);

            CompleteMultipartUploadResult uploadResult = s3Client.completeMultipartUpload(compRequest);
            String finalUrl = uploadResult.getLocation().replaceAll("%2F", "/")

            log.info("Se ha subido el nuevo CSS del dominio ${domain}: ${finalUrl}")
        } catch (Exception e) {
            s3Client.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, keyName, initResponse.getUploadId()));
            log.error("Ha habido un error subiendo el fichero a Amazon.",e);
        }
    }
}
