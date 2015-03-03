/**
 * Created by iduetxe on 22/06/14.
 */
public class RemovingScriptTagsCodec {
    static encode = {target->
        target.replaceAll("(?i)<[/]{0,1}script[^>]*>", '')
    }

    static decode = {target->
        //TODO
        target
    }
}
