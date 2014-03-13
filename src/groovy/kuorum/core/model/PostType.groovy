package kuorum.core.model

/**
 * Created by iduetxe on 12/02/14.
 */
public enum PostType {

    PURPOSE("propuesta"),QUESTION("pregunta"),HISTORY("historia")

    String urlText
    PostType(String urlText){
        this.urlText = urlText
    }
}