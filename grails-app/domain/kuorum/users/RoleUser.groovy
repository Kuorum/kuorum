package kuorum.users

import org.bson.types.ObjectId

class RoleUser {

    ObjectId id
	String authority

	static mapping = {
		cache true
	}

	static constraints = {
		authority blank: false, unique: true
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof RoleUser)) return false

        RoleUser roleUser = (RoleUser) o

        authority == roleUser.authority
    }

    int hashCode() {
        authority != null ? authority.hashCode() : 0
    }
}
