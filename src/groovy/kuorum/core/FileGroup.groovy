package kuorum.core

/**
 */
public enum FileGroup {

    USER_AVATAR("UsersFiles", 1 , '1', 500,500),
    USER_PROFILE("UsersFiles", 4, '20/9', 1600,720),
    DOMAIN_SLIDE_IMAGE("DomainSlides", 5, '16/9', 1600,900 ),
    PROJECT_IMAGE("ProjectsFiles", 4, '16/9', 1600,900),
    POST_IMAGE("PoliticianFiles", 4,'16/9',1600,900),
    MASS_MAIL_IMAGE("MassFiles", 4,'5/1',640, 128),
    CUSTOM_TEMPLATE_IMAGE("CustomTemplate", 3,'',0,0),
    PDF("PDFFiles", 1024 * 1000 *10 /*10 MB */,'',0,0),
    //TODO: Revisar el campo 'folderPath' para este caso
    YOUTUBE("YoutubeFiles",0,'',0,0)

    static final Long MIN_SIZE_IMAGE = 1024 * 1 // 1KB

    String folderPath
    Long maxSize
    Long maxSizeMegas
    String aspectRatio
    Integer imageWidth
    Integer imageHeight


    FileGroup(String folderPath, Long maxSize, String aspectRatio, Integer imageWidth, Integer imageHeight){
        this.folderPath = folderPath
        this.maxSize = 1024 * 1000 * maxSize
        this.maxSizeMegas = maxSize
        this.aspectRatio = aspectRatio
        this.imageWidth = imageWidth
        this.imageHeight = imageHeight
    }

}