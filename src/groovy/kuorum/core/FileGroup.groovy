package kuorum.core

/**
 */
public enum FileGroup {

    USER_AVATAR("UsersFiles", 102400 * 3 /*300 KB */),
    LAW_IMAGE("LawsFiles", 102400 /*100 KB */),
    POST_IMAGE("PoliticianFiles", 102400 /*100 KB */)

    String folderPath
    Long maxSize

    FileGroup(String folderPath, Long maxSize){
        this.folderPath = folderPath
        this.maxSize = maxSize
    }

}