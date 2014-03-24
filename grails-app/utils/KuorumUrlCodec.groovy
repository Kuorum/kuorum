

import java.text.Normalizer

/**
 * Handle the operations of the hashtag
 */
class KuorumUrlCodec {
    static encode = {String target->
        String res = target.replaceAll(" ","-")
        res = Normalizer.normalize(res, Normalizer.Form.NFD)
        res = res.replaceAll("[^a-zA-Z0-9\\-]","")

        res.toLowerCase()
    }

    static decode = {target->
        //TODO
    }
}
