package kuorum

class AjaxTagLIbTagLib {
    static defaultEncodeAs = 'raw'
    //static encodeAsForTags = [tagName: 'raw']

    static namespace = "ajax"

    def appenderLink={attrs ->
        String mapping = attrs.mapping
        def params = attrs.params?:[]
        def parentId = attrs.parentId

        def link = g.createLink(mapping: mapping, params: params)

    }

    def activeMenuCss = { attrs ->
        out << "${params.controller} ${params.action}"

    }
}
