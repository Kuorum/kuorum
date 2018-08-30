package kuorum.numberCodecs
/**
 * Handle the operations of the hashtag
 */
class ReducedPriceCodec {


    private static Integer milesDivisor = Math.pow(10, 6)
    private static Integer thousandsDivisor = Math.pow(10, 3)

    static encode = {Number target->
        if (target >= milesDivisor){
            String format = "%.1fM";
            if (target % milesDivisor == 0){
                format = "%.0fM"
            }
            return String.format(format, target / milesDivisor)
        }else if (target >= thousandsDivisor){
            String format = "%.1fK";
            if (target % thousandsDivisor == 0){
                format = "%.0fK"
            }

            return String.format(format, target / thousandsDivisor)
        }
        return target.toString();
    }

    static decode = {target->
        if (target.endsWith("K")){
            Integer num = Integer.parseInt(target.substring(0, target.length() - 1))
            return num * thousandsDivisor;
        }else if (target.endsWith("M")){
            Integer num = Integer.parseInt(target.substring(0, target.length() - 1))
            return num * milesDivisor;
        }else{
            return Integer.parseInt(target)
        }
    }
}
