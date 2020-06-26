package kuorum

class CorsFilters {
    def filters = {
        corsFilter(controller:'*', action:'*') {
            before = {
                response.setHeader("Access-Control-Allow-Origin", "*")
            }
        }
    }
}
