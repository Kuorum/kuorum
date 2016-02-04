package kuorum.files

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.CompleteMultipartUploadResult
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.amazonaws.services.s3.model.GetObjectRequest
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.services.s3.model.S3Object
import grails.transaction.Transactional
import kuorum.KuorumFile

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.UploadPartRequest
import kuorum.core.FileGroup
import kuorum.core.FileType
import kuorum.core.exception.KuorumException
import kuorum.users.KuorumUser;

class AmazonFileService extends LocalFileService{

    private static final String BUCKET_TMP_FOLDER= "tmp"

    private static final long UPLOAD_PART_SIZE = 5242880; // Set part size to 5 MB.


    public KuorumFile uploadTemporalFile(InputStream inputStream, KuorumUser kuorumUser, String fileName, FileGroup fileGroup) throws KuorumException{
        KuorumFile kuorumFile = uploadLocalTemporalFile(inputStream, kuorumUser, fileName, fileGroup)
        uploadAmazonFile(kuorumFile, Boolean.TRUE)
        kuorumFile
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
            String keyName    = "${asTemporal?BUCKET_TMP_FOLDER:kuorumFile.fileGroup.folderPath}/${kuorumFile.fileName}";
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
            s3client.deleteObject(new DeleteObjectRequest(bucketName, "${temporal?BUCKET_TMP_FOLDER:file.fileGroup.folderPath}/${file.fileName}"));
//            s3client.deleteObject(new DeleteObjectRequest(bucketName, file.storagePath)); //OLD IMAGES NOT HAS storagePath
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
}
