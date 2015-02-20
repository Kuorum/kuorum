package kuorum.notifications

class Notice {

    Date firstDashboard = new Date()
    Integer reloadDashboard = 0
    Integer timesInMonth = 0
    NoticeType noticeType

    static constraints = {
        noticeType nullable:true
    }

}
