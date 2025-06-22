package com.example.rememberme.user.infrastructure.persistence.repository

import com.example.rememberme.shared.domain.Id
import com.example.rememberme.user.domain.Email
import com.example.rememberme.user.domain.Pseudo
import com.example.rememberme.user.domain.User
import com.example.rememberme.user.domain.UserPreferences
import com.example.rememberme.user.domain.spi.UserRepository
import com.example.rememberme.user.infrastructure.persistence.model.DbUser
import com.example.rememberme.user.infrastructure.persistence.model.DbUserPreferences
import org.springframework.stereotype.Component

@Component
class JpaUserRepository(
    private val jpaUserStore: JpaUserStore,
    private val jpaUserPreferencesStore: JpaUserPreferencesStore
) : UserRepository {
    override fun findAll(): List<User> {
        return jpaUserStore.findAll().map { dbUser ->
            mapToUser(dbUser)
        }
    }

    override fun findById(id: Id<User>): User? {
        return jpaUserStore.findUserById(id.value)?.let { dbUser ->
            mapToUser(dbUser)
        }
    }

    override fun save(user: User) {
        val dbUser = DbUser(
            id = user.id.value,
            email = user.email.value,
            pseudo = user.pseudo.value
        )
        jpaUserStore.save(dbUser)

        // Save preferences if they exist
        user.preferences.missingDailyMemoryReminderTime?.let {
            val dbUserPreferences = DbUserPreferences(
                userId = user.id.value,
                missingDailyMemoryReminderTime = it
            )
            jpaUserPreferencesStore.save(dbUserPreferences)
        }
    }

    private fun mapToUser(dbUser: DbUser): User {
        val preferences = dbUser.preferences?.let {
            UserPreferences(missingDailyMemoryReminderTime = it.missingDailyMemoryReminderTime)
        } ?: UserPreferences()

        return User(
            id = Id(dbUser.id),
            email = Email(dbUser.email),
            pseudo = Pseudo(dbUser.pseudo),
            preferences = preferences
        )
    }
}
