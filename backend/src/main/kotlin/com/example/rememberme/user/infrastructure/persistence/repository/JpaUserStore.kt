package com.example.rememberme.user.infrastructure.persistence.repository

import com.example.rememberme.user.infrastructure.persistence.model.DbUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface JpaUserStore : JpaRepository<DbUser, UUID>
