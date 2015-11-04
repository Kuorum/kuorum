dataSource {
    pooled = true
    driverClassName = "org.h2.Driver"
    username = "sa"
    password = ""
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory' // Hibernate 3
//    cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory' // Hibernate 4
}

// environment specific settings
def env = System.getenv()

environments {
    development {
        logSql = true
        grails{
            mongo {
                host = "localhost"
                port = "27017"
                username = ""
                password = ""
                databaseName = "KuorumDev"
    //                options {
    //                    w = 1
    //                    wtimeout = 0
    //                    fsync = true
    //                }
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

    production {
        grails{
            mongo {
                //host = "172.31.40.27"
                host = "localhost"
                port = "27017"
                username = ""
                password = ""
                databaseName = "Kuorum"
    //                options {
    //                    w = 1
    //                    wtimeout = 0
    //                    fsync = true
    //                }
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
