package kuorum.core

/**
 */
public enum FileGroup {

    USER_AVATAR("UsersFiles", 1024 * 1000 *1 /*1 MB */, '1', 500,500),
    USER_PROFILE("UsersFiles", 1024 * 1000 *4/*4 MB */, '20/9', 800,360),
    DOMAIN_SLIDE_IMAGE("DomainSlides", 1024 * 1000 * 5 /* 5 MB */, '16/9', 800,450 ),
    PROJECT_IMAGE("ProjectsFiles", 1024 * 1000 *4 /*4 MB */, '16/9', 800,450),
    POST_IMAGE("PoliticianFiles", 1024 *1000 *4 /*4 MB */,'16/9',800,450),
    MASS_MAIL_IMAGE("MassFiles", 1024 *1000 *4 /*4 MB */,'5/1',640, 128),
    CUSTOM_TEMPLATE_IMAGE("CustomTemplate", 1024 *1000 *3 /*3 MB */,'',0,0),
    PDF("PDFFiles", 1024 * 1000 *10 /*10 MB */,'',0,0),
    //TODO: Revisar el campo 'folderPath' para este caso
    YOUTUBE("YoutubeFiles",0,'',0,0)

    static final Long MIN_SIZE_IMAGE = 1024 * 1 // 1KB

    String folderPath
    Long maxSize
    String aspectRatio
    Integer imageWidth
    Integer imageHeight


    FileGroup(String folderPath, Long maxSize, String aspectRatio, Integer imageWidth, Integer imageHeight){
        this.folderPath = folderPath
        this.maxSize = maxSize
        this.aspectRatio = aspectRatio
        this.imageWidth = imageWidth
        this.imageHeight = imageHeight
    }

}