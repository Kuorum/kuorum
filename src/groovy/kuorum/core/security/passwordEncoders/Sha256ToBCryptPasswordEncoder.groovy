package kuorum.core.security.passwordEncoders

import grails.plugin.springsecurity.authentication.encoding.BCryptPasswordEncoder
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder

/**
 * Extracted from web
 * @link http://burtbeckwith.com/blog/?p=2017
 */
class Sha256ToBCryptPasswordEncoder implements org.springframework.security.authentication.encoding.PasswordEncoder {

    protected MessageDigestPasswordEncoder sha256PasswordEncoder;
    protected BCryptPasswordEncoder bcryptPasswordEncoder;

    public String encodePassword(String rawPass, Object salt) {
        return bcryptPasswordEncoder.encodePassword(rawPass, null);
    }

    public boolean isPasswordValid(String encPass,
                                   String rawPass, Object salt = null) {
        if (encPass.startsWith('$2a$10$') && encPass.length() == 60) {
            // already bcrypt
            return bcryptPasswordEncoder.isPasswordValid(encPass, rawPass, null);
//            return bcryptPasswordEncoder.isPasswordValid(encPass, rawPass, salt);
        }

        if (encPass.length() == 64) {
            return sha256PasswordEncoder.isPasswordValid(encPass, rawPass, null);
//            return sha256PasswordEncoder.isPasswordValid(encPass, rawPass, salt);
        }

        // TODO
        return false;
    }

    /**
     * Dependency injection for the bcrypt password encoder
     * @param encoder the encoder
     */
    public void setBcryptPasswordEncoder(BCryptPasswordEncoder encoder) {
        bcryptPasswordEncoder = encoder;
    }

    /**
     * Dependency injection for the SHA-256 password encoder
     * @param encoder the encoder
     */
    public void setSha256PasswordEncoder(
            MessageDigestPasswordEncoder encoder) {
        sha256PasswordEncoder = encoder;
    }
}
