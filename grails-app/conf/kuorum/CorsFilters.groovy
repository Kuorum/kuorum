package kuorum

class CorsFilters {
    def filters = {
        all(controller:'*', action:'*') {
            before = {
                response.setHeader("Access-Control-Allow-Origin", "*")
            }
        }
    }
}
