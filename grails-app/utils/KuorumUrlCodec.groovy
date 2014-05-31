

import java.text.Normalizer

/**
 * Handle the operations of the hashtag
 */
class KuorumUrlCodec {
    static encode = {target->
        String res = target.toString().toLowerCase()
        res = res.replaceAll(" ","-")
        res = Normalizer.normalize(res, Normalizer.Form.NFD)
        res.replaceAll("[^a-zA-Z0-9\\-]","")
    }

    static decode = {target->
        //TODO
        // More or less
        target.replaceAll("-"," ")
    }
}
