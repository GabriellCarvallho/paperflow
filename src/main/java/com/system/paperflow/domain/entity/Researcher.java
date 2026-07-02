package com.system.paperflow.domain.entity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Researcher {

    private String username;
    private String email;
    private String password;
    private String institution;

    private final Set<Profile> profiles;

    public Researcher(String username, String email, String password, String institution) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.institution = institution;
        this.profiles = new HashSet<>();
        
    }

    public void addProfile(Profile profile) {
        if (profile == null) {
            return;
        }

        profiles.removeIf(existingProfile -> existingProfile.getClass().equals(profile.getClass()));
        profiles.add(profile);
    }

    public <T extends Profile> Optional<T> getProfile(Class<T> profileClass) {
        return profiles.stream().filter(profileClass::isInstance).map(profileClass::cast).findFirst();
    }

    public boolean hasProfile(Class<? extends Profile> profileClass) {
        return getProfile(profileClass).isPresent();
    }

    public boolean isCoordinator() {
        return hasProfile(CoordinatorProfile.class);
    }

    public boolean isReviewer() {
        return hasProfile(ReviewerProfile.class);
    }

    public Set<Profile> getProfiles() {
        return Collections.unmodifiableSet(profiles);
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getInstitution() {
        return institution;
    }

}
