package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.WhereJoinTable;
import ru.practicum.util.EventState;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "events")
public class Event {
    private String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private LocalDateTime created;

    private String description;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    private boolean paid;

    private int participantLimit;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    private boolean requestModeration;

    @Enumerated(EnumType.STRING)
    private EventState state;

    private String title;

    private long views;

    @WhereJoinTable(clause = "status='CONFIRMED'")
    @ManyToMany
    @JoinTable(name = "requests",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    List<User> participants;
}
