package core.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UsersList {

    private Integer id;
    private String username;
    private String email;
    private String password;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    private String role;
    private String phone;

    @JsonProperty("is_active")
    private Boolean active;

    @JsonProperty("last_login_at")
    private String lastLoginAt;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("updated_at")
    private String updatedAt;

    @JsonProperty("registration_code")
    private String registrationCode;

    @JsonProperty("code_used_at")
    private String codeUsedAt;

    @JsonProperty("telegram_id")
    private String telegramId;

    @JsonProperty("telegram_username")
    private String telegramUsername;

    @JsonProperty("language_code")
    private String languageCode;

    public UsersList() {
    }

    // getters
    public Integer getId() { return id; }

    public String getUsername() { return username; }

    public String getEmail() { return email; }

    public String getPassword() { return password; }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public String getRole() { return role; }

    public String getPhone() { return phone; }

    public Boolean isActive() { return active; }

    public String getLastLoginAt() { return lastLoginAt; }

    public String getCreatedAt() { return createdAt; }

    public String getUpdatedAt() { return updatedAt; }

    public String getRegistrationCode() { return registrationCode; }

    public String getCodeUsedAt() { return codeUsedAt; }

    public String getTelegramId() { return telegramId; }

    public String getTelegramUsername() { return telegramUsername; }

    public String getLanguageCode() { return languageCode; }
}

