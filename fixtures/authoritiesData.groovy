import kuorum.users.RoleUser

//beans{
fixture {
//    build{
        roleUser            (RoleUser,authority: 'ROLE_USER')
        rolePremium         (RoleUser,authority: 'ROLE_PREMIUM')
        rolePolitician      (RoleUser,authority: 'ROLE_POLITICIAN')
        roleAdmin           (RoleUser,authority: 'ROLE_ADMIN')
        roleHistoryStats    (RoleUser,authority: 'ROLE_HISTORY_STATS')
        roleQuestionStats   (RoleUser,authority: 'ROLE_QUESTION_STATS')
        rolePurposeStats    (RoleUser,authority: 'ROLE_PURPOSE_STATS')
        roleIncompleteUser  (RoleUser,authority: 'ROLE_INCOMPLETE_USER')
        rolePasswordChanged  (RoleUser,authority: 'ROLE_PASSWORDCHANGED')
//    }
}

post {
}