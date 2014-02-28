package kuorum.post

import grails.converters.JSON
import org.bson.types.ObjectId

class PostController {

    def index() {}

    def show(String postId){
        render Post.get(new ObjectId(postId)) as JSON
    }
}
