
/**
 * Converts an object to a query  params on a URL.
 *
 * Returns a Map<String,String>
 */
class QueryParamsCodec {
    static encode = {def target->
        convertObjectToQueryParams("",target)
    }
    static decode = {target->
        //TODO
        return null;
    }

    public static Map<String, String> convertObjectToQueryParams(String path, def obj){
        Map<String, String> data = [:]
        if (obj){
            if (obj && (getWrapperTypes().contains(obj.class) || obj instanceof Enum) ){
                data.put(path, obj.toString())
            }else {
                def filtered = ['class', 'active', 'metaClass']
                path = path?"$path.":""
                obj.properties.findAll { !filtered.contains(it.key) }.collect { k, v ->
                    if (v instanceof Collection) {
                        v.eachWithIndex { it, i ->
                            data.putAll(convertObjectToQueryParams("$path$k[$i]", it))
                        }
                    } else {
                        data.putAll(convertObjectToQueryParams("$path$k", v))
                    }
                }
            }
        }
        data
    }
    private static Set<Class<?>> getWrapperTypes()
    {
        Set<Class<?>> ret = new HashSet<Class<?>>()
        ret.add(Boolean.class)
        ret.add(Character.class)
        ret.add(Byte.class)
        ret.add(Short.class)
        ret.add(Integer.class)
        ret.add(Long.class)
        ret.add(Float.class)
        ret.add(Double.class)
        ret.add(Void.class)
        ret.add(String.class)
        return ret
    }
}
