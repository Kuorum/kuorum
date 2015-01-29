package kuorum.core.model

/**
 * Represents the possible vote types when users vote projects
 *
 * NOT_VOTED actually is not used. Is here for ... just in case
 */
enum VoteType {
    POSITIVE,NEGATIVE,ABSTENTION, NOT_VOTED
}
