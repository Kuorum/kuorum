package kuorum

class ModulesTagLib {
    static defaultEncodeAs = 'raw'
    //static encodeAsForTags = [tagName: 'raw']

    static namespace = "modulesUtil"

    def delayedModule={attrs ->

        def params = attrs.params
        def mapping = attrs.mapping
        def elementId = attrs.elementId

        def link = createLink(mapping:mapping, params: params)

        out << """
                <script>
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
                </script>
                <div id="${elementId}" class="hidden"></div>
        """
    }
}
