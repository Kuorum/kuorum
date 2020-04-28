package kuorum
/**
 * Handle the operations of the hashtag
 */
class S3FileCodec {

    static encode = {String url->
        [
                name:url.split("/").last().split('\\?').first(),
                icon: getFaIconOfExtension(url.split("\\.").last().split('\\?').first()),
                delete:url+"/delete", // Is the mapping
                url:url
        ]
    }

    static decode = {target->
        return target.url
    }


    private static String getFaIconOfExtension(String extension){
        switch (extension){
            case "pdf": return 'fal fa-file-pdf';
            case "doc":
            case "docx": return 'fal fa-file-word';
            case "ppt":
            case "pptx": return 'fal fa-file-powerpoint';
            case "xlsx":
            case "xls": return 'fal fa-file-excel';
            case "zip":
            case "rar": return 'fal fa-file-archive';
            case "jpg":
            case "jpeg":
            case "JPG":
            case "JPEG":
            case "png":
            case "PNG":
            case "gif":
            case "GIF": return 'fal fa-file-image';
            default: return "";
        }
    }
}

