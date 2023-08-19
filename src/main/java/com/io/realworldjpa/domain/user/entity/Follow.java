package com.io.realworldjpa.domain.user.entity;

import com.io.realworldjpa.global.util.Generated;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "user_follow")
@EntityListeners(AuditingEntityListener.class)
public class Follow implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @JoinColumn(name = "from_id")
    @ManyToOne(fetch = LAZY)
    private User from;

    @JoinColumn(name = "to_id")
    @ManyToOne(fetch = LAZY)
    private User to;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    protected Follow() {}

    public Follow(User from, User to) {
        this.from = from;
        this.to = to;
    }

    public long getId() {
        return id;
    }

    public User getFrom() {
        return from;
    }

    public User getTo() {
        return to;
    }

    @Override
    @Generated
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("from", from.getId())
                .append("target", to.getId())
                .toString();
    }

    @Override
    @Generated
    public boolean equals(Object o) {
        return o instanceof Follow follow
                && Objects.equals(this.from, follow.from)
                && Objects.equals(this.to, follow.to);
    }

    @Override
    @Generated
    public int hashCode() {
        return Objects.hash(this.id, this.from, this.to);
    }
}
