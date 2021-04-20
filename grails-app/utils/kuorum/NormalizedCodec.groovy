package kuorum

import java.text.Normalizer

/**
 * Handle the operations of the hashtag
 */
class NormalizedCodec {

    static encode = {target->
        String res = target.toString().toLowerCase()
        res = res.trim()
        res = res.replaceAll(" ","_")
        res = Normalizer.normalize(res, Normalizer.Form.NFD)
        res = res.replaceAll("[^a-zA-Z0-9_\\.]","")
        res
    }

    static decode = {target->
        //TODO
        // More or less
        target.replaceAll("-"," ")
    }
}

