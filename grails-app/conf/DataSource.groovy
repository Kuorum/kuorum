//dataSource {
//    pooled = true
//    driverClassName = "org.h2.Driver"
//    username = "sa"
//    password = ""
//}
//hibernate {
//    cache.use_second_level_cache = true
//    cache.use_query_cache = false
//    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory' // Hibernate 3
//    cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory' // Hibernate 4
//}

// environment specific settings
def env = System.getenv()

environments {
    development {
        logSql = true
        grails{
            mongo {
//                replicaSet = [ "localhost:27017"]
//                replicaSet = [ "localhost:8000"]
                replicaSet = [ "1.2.3.4:27017"]
                username = ""
                password = ""
                databaseName = "Kuorum"
                options {
                    connectionsPerHost = 1000 // The maximum number of connections allowed per host
                    threadsAllowedToBlockForConnectionMultiplier = 10
                    maxWaitTime = 120000 // Max wait time of a blocking thread for a connection.
                    connectTimeout = 0 // The connect timeout in milliseconds. 0 == infinite
                    socketTimeout = 0 // The socket timeout. 0 == infinite
                    socketKeepAlive = false // Whether or not to have socket keep alive turned on
//                    writeConcern = new com.mongodb.WriteConcern(0, 0, false) // Specifies the number of servers to wait for on the write operation, and exception raising behavior
//                    sslEnabled = false // Specifies if the driver should use an SSL connection to Mongo
                }
            }
        }
    }
    aws {
        logSql = true
        grails{
            mongo {
                replicaSet = [ "mongo.kuorum.local:27017"]
//                replicaSet = [ "localhost:8000"]
//                replicaSet = [ "192.168.0.150:27017"]
//                replicaSet = [ "10.100.0.53:27017"]
                username = ""
                password = ""
                databaseName = "Kuorum"
                options {
                    connectionsPerHost = 1000 // The maximum number of connections allowed per host
                    threadsAllowedToBlockForConnectionMultiplier = 10
                    maxWaitTime = 120000 // Max wait time of a blocking thread for a connection.
                    connectTimeout = 0 // The connect timeout in milliseconds. 0 == infinite
                    socketTimeout = 0 // The socket timeout. 0 == infinite
                    socketKeepAlive = false // Whether or not to have socket keep alive turned on
//                    writeConcern = new com.mongodb.WriteConcern(0, 0, false) // Specifies the number of servers to wait for on the write operation, and exception raising behavior
//                    sslEnabled = false // Specifies if the driver should use an SSL connection to Mongo
                }
            }
        }
    }
    test {
        logSql = true
        grails{
            mongo {
                host = "localhost"
                port = "27017"
                username = ""
                password = ""
                databaseName = "KuorumTest"
    //                options {
    //                    w = 1
    //                    wtimeout = 0
    //                    fsync = true
    //                }
            }
        }
    }

    preproduction {
        grails{
            mongo {
		replicaSet = [ "192.168.0.150:27017"]
                port = "27017"
                username = ""
                password = ""
                databaseName = "Kuorum"
                options {
//                    w = 1
//                    wtimeout = 0
//                    fsync = true
                    autoConnectRetry = true
                    connectTimeout = 3000
                    socketTimeout = 60000
                    maxWaitTime = 5000
                    socketKeepAlive= true
                    connectionsPerHost = 100
                    threadsAllowedToBlockForConnectionMultiplier = 50
                    maxAutoConnectRetryTime = 5
                }
            }
        }
    }

    production {
        grails{
            mongo {
                replicaSet = [ "192.168.0.50:27017"]
//                host = "localhost"
//                port = "27017"
                username = ""
                password = ""
                databaseName = "Kuorum"
                    options {
//                        w = 1
//                        wtimeout = 0
//                        fsync = true
                        autoConnectRetry = true
                        connectTimeout = 3000
                        socketTimeout = 60000
                        maxWaitTime = 5000
                        socketKeepAlive= true
                        connectionsPerHost = 100
                        threadsAllowedToBlockForConnectionMultiplier = 50
                        maxAutoConnectRetryTime = 5
                    }
            }
        }
    }
}

/*
environments {
    development {
        dataSource {
            dbCreate = "create-drop" // one of 'create', 'create-drop', 'update', 'validate', ''
            url = "jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
        }
    }
    production {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:prodDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
            properties {
               maxActive = -1
               minEvictableIdleTimeMillis=1800000
               timeBetweenEvictionRunsMillis=1800000
               numTestsPerEvictionRun=3
               testOnBorrow=true
               testWhileIdle=true
               testOnReturn=false
               validationQuery="SELECT 1"
               jdbcInterceptors="ConnectionState"
            }
        }
    }
}
*/
