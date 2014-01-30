class BootStrap {


    def fixtureLoader

    def init = { servletContext ->
        environments {
            development {
                kuorum.users.User.collection.getDB().dropDatabase()
                fixtureLoader.load("testData")
            }
            test{
                kuorum.users.User.collection.getDB().dropDatabase()
                fixtureLoader.load("testData")
            }
            production{

            }
        }
    }
    def destroy = {
    }
}
