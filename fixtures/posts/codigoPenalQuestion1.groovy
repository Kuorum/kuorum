import kuorum.core.model.PostType
import kuorum.post.Post

fixture {

    codigoPenalQuestion1(Post){
        law=codigoPenal
        numClucks=1
        numVotes=1
        owner=carmen
        ownerPersonalData=carmenData
        postType=PostType.QUESTION
        title="Muy bien. Todos a la cárcel"
        text="""
            Mi principal idea es que todos vayan a la cárcel, pero eso es muy caro. No sería mejor trabajos para la sociedad o...
            directamente matarile??
            """
    }
}