package kuorum.users

import org.apache.commons.lang.builder.HashCodeBuilder

class KuorumUserRoleUser implements Serializable {

	private static final long serialVersionUID = 1

	KuorumUser kuorumUser
	RoleUser roleUser

	boolean equals(other) {
		if (!(other instanceof KuorumUserRoleUser)) {
			return false
		}

		other.kuorumUser?.id == kuorumUser?.id &&
			other.roleUser?.id == roleUser?.id
	}

	int hashCode() {
		def builder = new HashCodeBuilder()
		if (kuorumUser) builder.append(kuorumUser.id)
		if (roleUser) builder.append(roleUser.id)
		builder.toHashCode()
	}

	static KuorumUserRoleUser get(long kuorumUserId, long roleUserId) {
		KuorumUserRoleUser.where {
			kuorumUser == KuorumUser.load(kuorumUserId) &&
			roleUser == RoleUser.load(roleUserId)
		}.get()
	}

	static KuorumUserRoleUser create(KuorumUser kuorumUser, RoleUser roleUser, boolean flush = false) {
		new KuorumUserRoleUser(kuorumUser: kuorumUser, roleUser: roleUser).save(flush: flush, insert: true)
	}

	static boolean remove(KuorumUser u, RoleUser r, boolean flush = false) {

		int rowCount = KuorumUserRoleUser.where {
			kuorumUser == KuorumUser.load(u.id) &&
			roleUser == RoleUser.load(r.id)
		}.deleteAll()

		rowCount > 0
	}

	static void removeAll(KuorumUser u) {
		KuorumUserRoleUser.where {
			kuorumUser == KuorumUser.load(u.id)
		}.deleteAll()
	}

	static void removeAll(RoleUser r) {
		KuorumUserRoleUser.where {
			roleUser == RoleUser.load(r.id)
		}.deleteAll()
	}

	static mapping = {
		id composite: ['roleUser', 'kuorumUser']
		version false
	}
}
