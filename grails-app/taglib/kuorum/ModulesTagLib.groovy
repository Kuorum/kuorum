package kuorum

class ModulesTagLib {
    static defaultEncodeAs = 'raw'
    //static encodeAsForTags = [tagName: 'raw']
    def springSecurityService
    def postService
    def kuorumUserService

    static namespace = "modulesUtil"


    def delayedModule = { attrs ->

        def params = attrs.params
        def mapping = attrs.mapping
        def elementId = attrs.elementId

        def link = createLink(mapping: mapping, params: params)

        def delayScript =  """
                    \$(function(){
                        \$.ajax({
                            url: "${link}",
                            context: document.body
                        }).done(function(data) {
                            \$("#${elementId}" ).html( data);
                            \$("#${elementId}").hide()
                            \$("#${elementId}").removeClass('hidden')
                            \$("#${elementId}").show('slow')
                        });
                    });
        """
        out << "<div id=\"${elementId}\" class=\"hidden\"></div>"
        r.script( [:],delayScript)
    }

    private static final Integer MAX_LENGTH_TEXT = 250
    def shortText = { attrs ->
        Integer maxLength = attrs.maxLength!=null?Integer.parseInt(attrs.maxLength):MAX_LENGTH_TEXT
        String orgText = attrs.text.encodeAsRemovingHtmlTags()
        String text = orgText.substring(0, Math.min(orgText.length(), maxLength))


        if (text && text.length() < orgText.length()){
            text += " ..."
        }
        out << text

    }
}
