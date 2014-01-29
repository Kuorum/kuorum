import kuorum.users.RoleUser

//beans{
fixture {
//    build{
        roleUser        (RoleUser,authority: 'ROLE_USER')
        roleAdvanced    (RoleUser,authority: 'ROLE_ADVANCED')
        roleAdmin       (RoleUser,authority: 'ROLE_ADMIN')
        roleEnterprise  (RoleUser,authority: 'ROLE_ENTERPRISE')
//    }
}

post {
//    roleUser.save(failOnError: true, flush:true)
//    roleAdvanced.save(failOnError: true, flush:true)
//    roleAdmin.save(failOnError: true, flush:true)
//    roleEnterprise.save(failOnError: true, flush:true)
}