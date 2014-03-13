package kuorum.post

import grails.converters.JSON
import org.bson.types.ObjectId

class PostController {

    def index() {
        [postInstanceList:Post.list()]
    }

    def show(String postId){
        Post post = Post.get(new ObjectId(postId))
        [postInstance:post]
    }

    def edit(String postId){
        Post post = Post.get(new ObjectId(postId))
        [postInstance:post]
    }

    def create(){
        [postInstance:new Post()]
    }
}
