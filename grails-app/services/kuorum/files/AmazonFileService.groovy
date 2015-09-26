package kuorum.files

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.CompleteMultipartUploadResult
import com.amazonaws.services.s3.model.PutObjectRequest
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
import kuorum.core.FileType;

@Transactional
class AmazonFileService  extends LocalFileService{

    KuorumFile convertTemporalToFinalFile(KuorumFile kuorumFile){
        if (kuorumFile?.temporal){
            String serverPath = grailsApplication.config.kuorum.upload.serverPath
            String rootUrl = "${grailsApplication.config.grails.serverURL}${grailsApplication.config.kuorum.upload.relativeUrlPath}"

            def fileLocation = generatePath(kuorumFile)
            def serverStoragePath = "$serverPath/$fileLocation"
            String finalUrl ="$rootUrl/$fileLocation/${kuorumFile.fileName}"

            File org = new File("${kuorumFile.storagePath}/${kuorumFile.fileName}")

            String user = "AKIAIZSHI3RG6BC4HZ4Q"
            String pass = "u7+VfxLmiuv4Kgolunm1IqTZ8koqM6/Esh073mSu"
            String existingBucketName  = "kuorum.org";
            String keyName             = "${kuorumFile.fileGroup.folderPath}/${kuorumFile.fileName}";
            String filePath            = "${kuorumFile.storagePath}/${kuorumFile.fileName}";

            AWSCredentials credentials = new BasicAWSCredentials(user,pass);

            AmazonS3 s3Client = new AmazonS3Client(credentials);


            // Create a list of UploadPartResponse objects. You get one of these
            // for each part upload.
            List<PartETag> partETags = new ArrayList<PartETag>();


            File file = new File(filePath);
            long contentLength = file.length();
            long partSize = 5242880; // Set part size to 5 MB.

//            s3Client.putObject(new PutObjectRequest(existingBucketName, keyName,file)
//                    .withCannedAcl(CannedAccessControlList.PublicRead));

            // Step 1: Initialize.
            InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(existingBucketName, keyName);
            InitiateMultipartUploadResult initResponse = s3Client.initiateMultipartUpload(initRequest);



            try {
                // Step 2: Upload parts.
                long filePosition = 0;
                for (int i = 1; filePosition < contentLength; i++) {
                    // Last part can be less than 5 MB. Adjust part size.
                    partSize = Math.min(partSize, (contentLength - filePosition));

                    // Create request to upload a part.
                    UploadPartRequest uploadRequest = new UploadPartRequest()
                            .withBucketName(existingBucketName).withKey(keyName)
                            .withUploadId(initResponse.getUploadId()).withPartNumber(i)
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
                        existingBucketName,
                        keyName,
                        initResponse.getUploadId(),
                        partETags);

                CompleteMultipartUploadResult uploadResult = s3Client.completeMultipartUpload(compRequest);
                finalUrl = uploadResult.getLocation()

            } catch (Exception e) {
                s3Client.abortMultipartUpload(new AbortMultipartUploadRequest(
                        existingBucketName, keyName, initResponse.getUploadId()));
            }


            File destDir = new File(serverStoragePath)
            destDir.mkdirs()
            File dest = new File("$serverStoragePath/${kuorumFile.fileName}")

            try{
                if(org.renameTo(dest)){
                    deleteParentIfEmpty(org)
                    kuorumFile.temporal = Boolean.FALSE
                    kuorumFile.storagePath = ""
                    kuorumFile.url =finalUrl
                    kuorumFile.urlThumb = finalUrl
                    kuorumFile.fileType = FileType.IMAGE
                    kuorumFile.local = Boolean.FALSE
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
}
