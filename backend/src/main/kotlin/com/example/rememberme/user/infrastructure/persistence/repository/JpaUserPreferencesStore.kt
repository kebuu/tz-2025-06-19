package com.example.rememberme.user.infrastructure.persistence.repository

import com.example.rememberme.user.infrastructure.persistence.model.DbUserPreferences
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface JpaUserPreferencesStore : JpaRepository<DbUserPreferences, UUID> {
    fun findByUserId(userId: UUID): DbUserPreferences?
}
