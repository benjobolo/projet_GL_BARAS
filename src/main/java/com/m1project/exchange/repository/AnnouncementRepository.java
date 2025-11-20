
package com.m1project.exchange.repository;

import com.m1project.exchange.entity.Announcement;
import com.m1project.exchange.entity.AnnouncementType;
import com.m1project.exchange.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    List<Announcement> findByOwner(User owner);

    List<Announcement> findByType(AnnouncementType type);

    List<Announcement> findByAvailableTrue();

    List<Announcement> findByTitleContainingIgnoreCase(String keyword);
}