package kuorum.mail

import kuorum.users.KuorumUser

class MailUserData {
    KuorumUser user
    def bindings = [:]

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        MailUserData that = (MailUserData) o

        return user == that.user
    }

    int hashCode() {
        return user.hashCode()
    }

    String toString(){
        "Mail to: $user"
    }
}
