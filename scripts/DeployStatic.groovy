import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.*
import org.apache.commons.io.FileUtils

import java.nio.file.Files
import java.nio.file.Paths

includeTargets << grailsScript("_GrailsInit")
includeTargets << grailsScript("_GrailsSettings")


public class AmazonStaticUploader{

//    String enabled
//    String upload
//    String host
    String accessKey
    String secretKey
    String bucket = "static-kuorum-824311007164"  //System.getenv("PUBLIC_BUCKET_NAME") // TODO: From environment
    String regionName = "eu-west-1" //System.getenv("BUCKET_REGION") // TODO: FROM Environment
//    String path
    private static final long UPLOAD_PART_SIZE = 5242880

    // CODE COPIED FROM AMAZON FILE SERVICE
    String uploadFileToAmazon(File file, String key) {
        System.out.println("Uploading ${key} to ${bucket} on region ${regionName}")
        String keyName = key.replace("//","/").replaceAll(/^\//,"")

        AmazonS3 s3Client = buildAmazonClient()


        // Create a list of UploadPartResponse objects. You get one of these for each part upload.
        List<PartETag> partETags = new ArrayList<PartETag>();


        long contentLength = file.length();
        long partSize = UPLOAD_PART_SIZE;

        // Step 1: Initialize.
        InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucket, keyName);
        initRequest.putCustomRequestHeader("Cache-Control", "max-age=${3600 * 24 * 30}") // 30 days = 2592000 secs
        initRequest.putCustomRequestHeader("Expires", "Thu, 15 Apr 2050 00:00:00 GMT")
        ObjectMetadata metadata = new ObjectMetadata();
//        metadata.setContentEncoding("gzip");
//        metadata.setContentType("text/css");
        String contentType = URLConnection.guessContentTypeFromName(file.getName());
        if (contentType == null){
            contentType = getContentTypeFromExtension(file.getName());
        }
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
                        .withBucketName(bucket)
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
                    bucket,
                    keyName,
                    initResponse.getUploadId(),
                    partETags);

            CompleteMultipartUploadResult uploadResult = s3Client.completeMultipartUpload(compRequest);
//            String finalUrl = uploadResult.getLocation().replaceAll("%2F", "/")
//            String finalUrl = uploadResult.getKey()
//            return finalUrl;
            return keyName;
        } catch (Exception e) {
            s3Client.abortMultipartUpload(new AbortMultipartUploadRequest(bucket, keyName, initResponse.getUploadId()));
            System.out.println("Ha habido un error subiendo el fichero ${keyName} a Amazon :: ${e.getMessage()}");
            return null;
        }
    }

    private AmazonS3 buildAmazonClient() {

//        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        AWSCredentialsProvider credentials = new DefaultAWSCredentialsProviderChain();

        AmazonS3 s3Client = new AmazonS3Client(credentials);
        s3Client.setRegion(com.amazonaws.regions.Region.getRegion(Regions.fromName(regionName)))
        return s3Client
    }

    private String getContentTypeFromExtension(String fileName){
        String[] parts = fileName.split("\\.")
        if (parts.length<=1){
            return "application/octet-stream";
        }
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
            case "svg":
                contentType ="image/svg+xml"
                break
            default:
                contentType = "application/octet-stream";
        }
        return contentType;
    }
}

private cleanupFolder(File temporalDir) {
    FileUtils.deleteDirectory(temporalDir)
}

def unzipFile(File file, File temporalDir) {
    cleanupFolder(temporalDir)
    def zipFile = new java.util.zip.ZipFile(file)
    zipFile.entries().each { it ->
        def path = Paths.get("${temporalDir.absolutePath}/${it.name}")
        if(it.directory){
            Files.createDirectories(path)
        }
        else {
            def parentDir = path.getParent()
            if (!Files.exists(parentDir)) {
                Files.createDirectories(parentDir)
            }
            Files.copy(zipFile.getInputStream(it), path)
        }
    }
}

File findFile(String fileName, File rootDir){
    try {
        boolean recursive = true;
        Collection files = FileUtils.listFiles(rootDir, null, recursive);

        for (Iterator iterator = files.iterator(); iterator.hasNext();) {
            File file = (File) iterator.next();
            if (file.getName().equals(fileName))
                return file;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

target(deployStatic: "The description of the script goes here!") {
    depends(packageApp)
    File rootDir = new File(".");
    String fileWarName = "ROOT.war"
    String BUCKET_NAME = System.getProperty("PUBLIC_BUCKET_NAME")
    String BUCKET_REGION = System.getProperty("BUCKET_REGION")
    String VERSION_PATH = System.getProperty("STATICS_VERSION_PATH")
    String versionPath = "/web/${VERSION_PATH}"
    System.out.println("${System.getProperty("grails.env")} :: Searching ${fileWarName} in ${rootDir.absolutePath}")
//    System.getProperties().list(System.out)
    def war = findFile(fileWarName, rootDir);
    File temporalDir = File.createTempDir("kuorum","war");
    unzipFile(war, temporalDir)

    def dirs =[
            css: new File(temporalDir.path+"/css"),
            js: new File(temporalDir.path+"/js"),
            images: new File(temporalDir.path+"/images"),
            fonts: new File(temporalDir.path+"/fonts"),
    ]

    AmazonStaticUploader amazonStaticUploader = new AmazonStaticUploader(
//            accessKey : config.grails.resources.mappers.amazoncdn.accessKey,
//            secretKey : config.grails.resources.mappers.amazoncdn.secretKey,
//            bucket : config.grails.resources.mappers.amazoncdn.bucket,
//            regionName : config.grails.resources.mappers.amazoncdn.bucketRegion
            bucket : BUCKET_NAME,
            regionName : BUCKET_REGION
    )
    String cdnPath = System.getenv("BUCKET_REGION")

    dirs.values().each{resourcesDir ->
        resourcesDir.eachFileRecurse (groovy.io.FileType.FILES) { file ->
            String key ="${versionPath}/${file.absolutePath.replaceAll("${temporalDir.path}","")}";
            key = amazonStaticUploader.uploadFileToAmazon(file, key);
            System.out.println("Uplaoded ${key} to bucket ${BUCKET_NAME}")
        }
    }
//    cleanupFolder(temporalDir)
}

setDefaultTarget(deployStatic)
