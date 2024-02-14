package kuorum

class CorsFilters {

    String X_FRAME_OPTIONS = "DENY" // DENY | SAMEORIGIN
    def filters = {
        corsFilter(controller:'*', action:'*') {
            before = {
                response.setHeader("Access-Control-Allow-Origin", "*")
                response.setHeader("X-Frame-Options", X_FRAME_OPTIONS)
            }
        }
    }
}
