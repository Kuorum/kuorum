import kuorum.users.KuorumUser

class BootStrap {


    def fixtureLoader

    def init = { servletContext ->
        environments {
            development {
//                KuorumUser.collection.getDB().dropDatabase()
//                fixtureLoader.load("testData")
            }
            test{
                KuorumUser.collection.getDB().dropDatabase()
                fixtureLoader.load("testData")
            }
            production{

            }
        }
    }
    def destroy = {
    }
}
