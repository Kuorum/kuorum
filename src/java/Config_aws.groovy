grails.serverURL="https://kuorum.org"


mail.mandrillapp.key=System.getenv("MANDRILLAPP_KEY")

recaptcha.providers.google.siteKey=System.getenv("GOOGLE_RECAPTCHA_SITE_KEY")
recaptcha.providers.google.secretKey=System.getenv("GOOGLE_RECAPTCHA_SECRET_KEY")

kuorum.keys.google.api.js=System.getenv("GOOGLE_API_JS")

kuorum.upload.serverPath="/tmp"

kuorum.amazon.bucketName = System.getenv("PUBLIC_BUCKET_NAME")
kuorum.amazon.bucketRegion = System.getenv("BUCKET_REGION")

kuorum.keys.favicon=System.getenv("FAVICON_KEY")


kuorum.rest.url="https://${System.getenv("API_DOMAIN")}"
kuorum.rest.apiPath="/api"
kuorum.rest.authPath="/oauth/token"
kuorum.rest.client_id=System.getenv("KUORUM_OAUTH_CLIENT_ID")
kuorum.rest.client_secret=System.getenv("KUORUM_OAUTH_CLIENT_SECRET")

grails.resources.mappers.multidomain.enabled = false
grails.resources.mappers.multidomain.protocol = "https://"
grails.resources.mappers.multidomain.suffixDomain = "${System.getenv("UPLOADED_RESOURCES_DOMAIN")}/static"
grails.resources.mappers.multidomain.numberDomains = 5


//## AMAZON CDN
grails.resources.mappers.amazoncdn.enabled=System.getenv("AMAZON_CDN_ENABLED")
grails.resources.mappers.amazoncdn.bucket = System.getenv("PUBLIC_BUCKET_NAME")
grails.resources.mappers.amazoncdn.bucketRegion = System.getenv("BUCKET_REGION")
grails.resources.mappers.amazoncdn.path = "/web${System.getenv("STATICS_VERSION_PATH")}"
grails.resources.mappers.amazoncdn.host = "https://${System.getenv("UPLOADED_RESOURCES_DOMAIN")}"
grails.resources.mappers.baseurl.default = "https://${System.getenv("UPLOADED_RESOURCES_DOMAIN")}/web${System.getenv("STATICS_VERSION_PATH")}"


grails.mongo.replicaSet=["${System.getenv("MONGO_HOST")}:${System.getenv( "MONGO_PORT")}"]

kuorum.memcache.host="${System.getenv("MEMCACHE_URL")}:${System.getenv( "MEMCACHE_PORT")}"
