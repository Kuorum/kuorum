package kuorum.core.model

/**
 * Representa el compromiso al que se "compromete" el político al defender un post
 */
public enum CommitmentType {


    SPEECH_OWNER_IN_CONGRESS(PostType.HISTORY),
    SPEECH_POLITICIAN_IN_CONGRESS(PostType.HISTORY),
    GOOD_EXPLANATION_HISTORY(PostType.HISTORY),

    ANSWERED(PostType.QUESTION),
    ASKED_ON_CONGRESS(PostType.QUESTION),
    GOOD_EXPLANATION_QUESTION(PostType.QUESTION),

    ADDED_AS_AMENDMENT(PostType.PURPOSE),
    FUTURE_AMENDMENT(PostType.PURPOSE),
    GOOD_EXPLANATION_PURPOSE(PostType.PURPOSE);

    PostType associatedPostType

    CommitmentType(PostType associatedPostType){
        this.associatedPostType = associatedPostType
    }

    public static final List<CommitmentType> recoverCommitmentTypesByPostType(PostType postType){
        CommitmentType.findAll{it.associatedPostType == postType}
    }

}