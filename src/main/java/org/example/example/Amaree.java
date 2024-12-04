package org.example.example;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "")
public class AwareUserRole {

    @Id
    @Column(name = "USERNAME", length = 256, nullable = false)
    private String username;

    @Column(name = "PROFILE", length = 256)
    private String profile;

    @Column(name = "FIRST_NAME", length = 256)
    private String firstName;

    @Column(name = "LAST_NAME", length = 256)
    private String lastName;

    @Column(name = "CREDIT_CATALOG", length = 10)
    private String creditCatalog;

    @Column(name = "RATE_CATALOG", length = 10)
    private String rateCatalog;

    @Column(name = "WWR_DR_CATALOG", length = 10)
    private String wwrDrCatalog;

    @Column(name = "EQUITY_CATALOG", length = 10)
    private String equityCatalog;

    @Column(name = "DFA_CATALOG", length = 10)
    private String dfaCatalog;

    @Column(name = "IM_SCHEDULE_CATALOG", length = 10)
    private String imScheduleCatalog;

    @Column(name = "SIMMP_CATALOG", length = 10)
    private String simmpCatalog;

    @Column(name = "STT_CATALOG", length = 10)
    private String sttCatalog;

    @Column(name = "INITIAL_DATE")
    private LocalDateTime initialDate;

    @Column(name = "MODIFICATION_DATE")
    private LocalDateTime modificationDate;

    @Column(name = "WINDOWS_LOGIN", length = 14)
    private String windowsLogin;

    @Column(name = "ADMIN", length = 10)
    private String admin;

    @Column(name = "C2_INSIDER_CATALOG", length = 10)
    private String c2InsiderCatalog;

    @Column(name = "VARSVAR_CATALOG", length = 10)
    private String varsvarCatalog;

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCreditCatalog() {
        return creditCatalog;
    }

    public void setCreditCatalog(String creditCatalog) {
        this.creditCatalog = creditCatalog;
    }

    public String getRateCatalog() {
        return rateCatalog;
    }

    public void setRateCatalog(String rateCatalog) {
        this.rateCatalog = rateCatalog;
    }

    public String getWwrDrCatalog() {
        return wwrDrCatalog;
    }

    public void setWwrDrCatalog(String wwrDrCatalog) {
        this.wwrDrCatalog = wwrDrCatalog;
    }

    public String getEquityCatalog() {
        return equityCatalog;
    }

    public void setEquityCatalog(String equityCatalog) {
        this.equityCatalog = equityCatalog;
    }

    public String getDfaCatalog() {
        return dfaCatalog;
    }

    public void setDfaCatalog(String dfaCatalog) {
        this.dfaCatalog = dfaCatalog;
    }

    public String getImScheduleCatalog() {
        return imScheduleCatalog;
    }

    public void setImScheduleCatalog(String imScheduleCatalog) {
        this.imScheduleCatalog = imScheduleCatalog;
    }

    public String getSimmpCatalog() {
        return simmpCatalog;
    }

    public void setSimmpCatalog(String simmpCatalog) {
        this.simmpCatalog = simmpCatalog;
    }

    public String getSttCatalog() {
        return sttCatalog;
    }

    public void setSttCatalog(String sttCatalog) {
        this.sttCatalog = sttCatalog;
    }

    public LocalDateTime getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(LocalDateTime initialDate) {
        this.initialDate = initialDate;
    }

    public LocalDateTime getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(LocalDateTime modificationDate) {
        this.modificationDate = modificationDate;
    }

    public String getWindowsLogin() {
        return windowsLogin;
    }

    public void setWindowsLogin(String windowsLogin) {
        this.windowsLogin = windowsLogin;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getC2InsiderCatalog() {
        return c2InsiderCatalog;
    }

    public void setC2InsiderCatalog(String c2InsiderCatalog) {
        this.c2InsiderCatalog = c2InsiderCatalog;
    }

    public String getVarsvarCatalog() {
        return varsvarCatalog;
    }

    public void setVarsvarCatalog(String varsvarCatalog) {
        this.varsvarCatalog = varsvarCatalog;
    }
}
