package kuorum.files

import kuorum.KuorumFile
import kuorum.core.FileGroup
import kuorum.core.exception.KuorumException
import kuorum.users.KuorumUser

/**
 * Created by iduetxe on 23/09/15.
 */
public interface FileService {

    public KuorumFile uploadTemporalFile(InputStream inputStream, KuorumUser kuorumUser, String fileName, FileGroup fileGroup) throws KuorumException;
    /**
     * Converts a normal file to temporal file.
     * @param KuorumFile
     * @return
     */
    public KuorumFile convertFinalFileToTemporalFile(KuorumFile kuorumFile);


    public KuorumFile createYoutubeKuorumFile(String youtubeUrl, KuorumUser user);


    /**
     * Converts a temporal file to normal file.
     * @param KuorumFile
     * @return
     */
    public KuorumFile convertTemporalToFinalFile(KuorumFile kuorumFile)
}