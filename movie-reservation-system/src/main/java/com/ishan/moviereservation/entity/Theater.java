package com.ishan.moviereservation.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "theaters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"screens"})
@EqualsAndHashCode(callSuper = true, exclude = {"screens"})
public class Theater extends BaseEntity {

    @NotBlank(message = "Theater name is required")
    @Size(max = 255, message = "Theater name must not exceed 255 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    // Relationships
    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Screen> screens = new ArrayList<>();

    // Custom constructor for basic fields
    public Theater(String name, String address) {
        this.name = name;
        this.address = address;
    }

    // Helper methods
    public void addScreen(Screen screen) {
        screens.add(screen);
        screen.setTheater(this);
    }

    public void removeScreen(Screen screen) {
        screens.remove(screen);
        screen.setTheater(null);
    }
}
