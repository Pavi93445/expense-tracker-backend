    package com.example.expenseTracker.Repository;

    import java.util.Optional;

    import org.springframework.data.jpa.repository.JpaRepository;

    import com.example.expenseTracker.Entity.ProfileEntity;

    public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {

        Optional<ProfileEntity> findByEmail(String email);
    }