package core.settings;

public enum ApiEndpoints {
    HEALTH("/heath"),
    AUTH("/auth/login"),
    REGISTER("/auth/register"),
    USERS("/users"),
    HOTELS("/hotels");

    private final String path;

    ApiEndpoints(String path) { this.path = path; }

    public String getPath() { return path; }
}
