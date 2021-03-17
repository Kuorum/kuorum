import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.*
import org.grails.plugin.resource.mapper.MapperPhase

class AmazonCdnResourceMapper {

    private static final long UPLOAD_PART_SIZE = 5242880; // Set part size to 5 MB.
    def phase = MapperPhase.DISTRIBUTION

    def map(resource, config) {
        Boolean cdnActive =
                config?.enabled == null ? false:
                config.enabled instanceof Boolean? config.enabled :
                config.enabled instanceof String? Boolean.parseBoolean(config?.enabled) :
                false

        if (cdnActive){
            String keyName = "${config.path}${resource.actualUrl}".replace("//","/").replaceAll(/^\//,"")
            try{
                if (resource.actualUrl.contains("bundle")){
                    File file;
                    if (resource.originalResource != null){
                        file = ((org.springframework.core.io.UrlResource) resource.originalResource).file;
                    }else{
                        file = new File("${resource.workDir}${resource.actualUrl}");
                    };
                    String path = uploadFileToAmazon(file, resource.contentType, keyName, config);
                    resource.linkOverride = "${config.host}/${path}".toString();
                    log.info("Uploaded ${keyName} URL =>${resource.linkOverride}")
                }else{
                    log.info("File ${keyName} was uploaded on deploy process")
                    resource.linkOverride = "${config.host}/${keyName}".toString();
                }
            }catch(Exception e){
                log.error("Error deploying resource on AMAZON : ${resource.linkOverride} :: ${e.getMessage()}")
            }
        }
    }

    // CODE COPIED FROM AMAZON FILE SERVICE
    private String uploadFileToAmazon(File file, String contentType, String key, def config) {
        String bucketName = config.bucket;
        String keyName = key.replace("//","/").replaceAll(/^\//,"")

        AmazonS3 s3Client = buildAmazonClient(config.accessKey, config.secretKey, config.bucketRegion)


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
        if (contentType != null){
            metadata.setContentType(contentType);
        }else{
            metadata.setContentType(getContetnTypeFromExtension(key));
        }
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
//            String finalUrl = uploadResult.getLocation().replaceAll("%2F", "/")
//            String finalUrl = uploadResult.getKey()
//            return finalUrl;
            return keyName;
        } catch (Exception e) {
            s3Client.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, keyName, initResponse.getUploadId()));
            log.error("Ha habido un error subiendo el fichero a Amazon.", e);
            return null;
        }
    }

    private String getContetnTypeFromExtension(String fileName){
        String[] parts = fileName.split("\\.")
        String ext = parts[parts.length-1]
        String contentType;
        switch (ext){
            case "woff2":
                contentType ="font/woff2"
                break
            case "woff":
                contentType ="application/font-woff"
                break
            case "eot":
                contentType ="application/vnd.ms-fontobject"
                break
            case "ttf":
                contentType ="application/font-sfnt"
                break
            case "txt":
                contentType ="text/plain"
                break
            default:
                contentType = "application/octet-stream";
        }
        return contentType;
    }

    private String buildAmazonUrl(String relativePath) {
        String bucketName = grailsApplication.config.kuorum.amazon.bucketName;
        return "https://${bucketName}.s3.amazonaws.com/${relativePath}"
    }

//    public checkIfSOurcesFileExists( def config){
//        AmazonS3 s3Client = buildAmazonClient(config.accessKey, config.secretKey)
//        var listResponse = s3Client.ListObjectsV2(new ListObjectsV2Request
//                {
//                    BucketName = config.bucket,
//                    Prefix = "folder1/folder2"
//                });
//    }

    private AmazonS3 buildAmazonClient(accessKey, secretKey, bucketRegion) {

        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        AmazonS3 s3Client = new AmazonS3Client(credentials);
        s3Client.withRegion(bucketRegion)
        return s3Client
    }

}