package kuorum.core

/**
 */
public enum FileGroup {

    USER_AVATAR("UsersFiles", 1024 * 1000 /*10 MB */, '1', 110),
    LAW_IMAGE("LawsFiles", 1024000 /*10 MB */, '123/62', 738),
    POST_IMAGE("PoliticianFiles", 1024000 /*10 MB */,'123/62',738)

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