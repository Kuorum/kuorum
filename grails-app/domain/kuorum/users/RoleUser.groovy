package kuorum.users

class RoleUser {

    Long id
	String authority

	static mapping = {
		cache true
	}

	static constraints = {
		authority blank: false, unique: true
	}
}
