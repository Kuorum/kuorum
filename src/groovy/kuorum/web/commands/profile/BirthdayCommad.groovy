package kuorum.web.commands.profile

/**
 * Created by iduetxe on 7/04/14.
 */
class BirthdayCommad {
    Integer year
    Integer month
    Integer day

    Date getDate(){
        Date.parse('yyyy/MM/dd', "${this.year}/${this.month}/${this.day}")
    }
    static constraints = {
        year min:1900
        month nullable: false, min:1, max: 12
        day nullable: false, min: 1, max: 31,validator: {val, obj ->
            try{
                Date date = obj.date
                if (date[Calendar.YEAR]==obj.year && date[Calendar.MONTH] == obj.month -1 && date[Calendar.DAY_OF_MONTH]==val){
                    Date now = new Date()
                    if (date < now){
                        return Boolean.TRUE
                    }else{
                        return "notCorrectBirthday"
                    }
                }else
                    return "notCorrectBirthday"
            }catch (Exception e){
                return "notCorrectBirthday"
            }
        }
    }
}
