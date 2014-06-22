/**
 * Created by iduetxe on 22/06/14.
 */
public class RemovingHtmlTagsCodec{
    static encode = {target->
        target.replaceAll("<(.|\n)*?>", '')
    }

    static decode = {target->
        //TODO
        target
    }
}
