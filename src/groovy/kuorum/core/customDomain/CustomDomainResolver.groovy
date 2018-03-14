package kuorum.core.customDomain

class CustomDomainResolver {
    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    public static void setDomain(String domain) {
        CONTEXT.set(domain);
    }

    public static String getDomain() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
