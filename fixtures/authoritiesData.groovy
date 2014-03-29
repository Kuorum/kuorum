import kuorum.users.RoleUser

//beans{
fixture {
//    build{
        roleUser        (RoleUser,authority: 'ROLE_USER')
        rolePremium    (RoleUser,authority: 'ROLE_PREMIUM')
        rolePolitician  (RoleUser,authority: 'ROLE_POLITICIAN')
        roleAdmin       (RoleUser,authority: 'ROLE_ADMIN')
//    }
}

post {
}