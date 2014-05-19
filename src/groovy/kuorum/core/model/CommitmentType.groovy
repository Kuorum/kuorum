package kuorum.core.model

/**
 * Representa el compromiso al que se "compromete" el político al defender un post
 */
public enum CommitmentType {


    SPEECH_OWNER_IN_CONGRESS(PostType.HISTORY),
    SPEECH_POLITICIAN_IN_CONGRESS(PostType.HISTORY),
    SPEECH_OWNER_PRIVATE(PostType.HISTORY),

    ANSWERED(PostType.QUESTION),
    ASKED_ON_CONGRESS(PostType.QUESTION),
    OWNER_ASK_IN_CONGRESS(PostType.QUESTION),

    ADDED_AS_AMENDMENT(PostType.PURPOSE),
    AMENDMENT_PROPOSAL(PostType.PURPOSE),
    OWNER_EXPLAIN_IN_CONGRESS(PostType.PURPOSE);

    PostType associatedPostType

    CommitmentType(PostType associatedPostType){
        this.associatedPostType = associatedPostType
    }

    public static final List<CommitmentType> recoverCommitmentTypesByPostType(PostType postType){
        CommitmentType.findAll{it.associatedPostType == postType}
    }

}