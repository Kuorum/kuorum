package kuorum.files

import kuorum.KuorumFile
import kuorum.core.FileGroup
import kuorum.core.FileType
import kuorum.core.exception.KuorumException
import kuorum.register.KuorumUserSession
import kuorum.users.KuorumUser

/**
 * Created by iduetxe on 23/09/15.
 */
public interface FileService {

    public KuorumFile uploadTemporalFile(InputStream inputStream, KuorumUser kuorumUser, String fileName, FileGroup fileGroup) throws KuorumException;
    public KuorumFile uploadTemporalFile(InputStream inputStream, KuorumUser kuorumUser, String fileName, FileGroup fileGroup, String path) throws KuorumException;

    List<KuorumFile> listFilesFromPath(FileGroup fileGroup, String path)
    /**
     * Converts a normal file to temporal file.
     * @param KuorumFile
     * @return
     */
    public KuorumFile convertFinalFileToTemporalFile(KuorumFile kuorumFile);


    public KuorumFile createYoutubeKuorumFile(String youtubeUrl, KuorumUserSession user);


    public void deleteKuorumFile(KuorumFile file);

    /**
     * Converts a temporal file to normal file.
     * @param KuorumFile
     * @return
     */
    public KuorumFile convertTemporalToFinalFile(KuorumFile kuorumFile)

    /**
     * Deletes all temporal files uploaded by the user @user.
     *
     * Deletes on DB and on file system
     *
     * @param user
     */
    void deleteTemporalFiles(KuorumUserSession user);
    @Deprecated
    void deleteTemporalFiles(KuorumUser user);

    /**
     * Creates a KuorumFile that points to an external source populated with all data
     * @param url
     * @return
     */
    public KuorumFile createExternalFile(KuorumUser owner, String url, FileGroup fileGroup, FileType fileType)


    public InputStream readFile(KuorumFile kuorumFile)

    /**
     * Crops an image
     * @param kuorumFile
     * @param x -> 0 is left corner
     * @param y -> 0 is top corner
     * @param h -> Height
     * @param w -> weight
     * @return
     */
    KuorumFile cropImage(KuorumFile kuorumFile, def x, def y, def h, def w);
}