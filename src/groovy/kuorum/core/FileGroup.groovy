package kuorum.core

/**
 */
public enum FileGroup {

    USER_AVATAR("UsersFiles", 1024 * 1000 *1 /*1 MB */, '1', 110),
    USER_PROFILE("UsersFiles", 1024 * 1000 *1/*1 MB */, '728/328', 110),
    PROJECT_IMAGE("ProjectsFiles", 1024 * 1000 *2 /*1 MB */, '16/9', 738),
    POST_IMAGE("PoliticianFiles", 1024 *1000 *1 /*1 MB */,'16/9',738),
    MASS_MAIL_IMAGE("MassFiles", 1024 *1000 *1 /*1 MB */,'5/1',738),
    CUSTOM_TEMPLATE_IMAGE("CustomTemplate", 1024 *1000 *1 /*1 MB */,'5/1',738),
    PDF("PDFFiles", 1024 * 1000 *10 /*10 MB */,'',0),
    //TODO: Revisar el campo 'folderPath' para este caso
    YOUTUBE("YoutubeFiles",0,'',0)

    static final Long MIN_SIZE_IMAGE = 1024 * 10

    String folderPath
    Long maxSize
    String aspectRatio
    Integer imageWidth


    FileGroup(String folderPath, Long maxSize, String aspectRatio, Integer imageWidth){
        this.folderPath = folderPath
        this.maxSize = maxSize
        this.aspectRatio = aspectRatio
        this.imageWidth = imageWidth
    }

}