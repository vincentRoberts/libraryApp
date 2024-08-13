package utils

import controller.UserAPI
import models.User
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import persistence.JSONSerializer
import java.io.File

internal class UserUtilityTests {

    private lateinit var userAPI: UserAPI

    @BeforeEach
    fun setup() {
        userAPI = UserAPI(JSONSerializer(File("testUsers.json")))
        userAPI.addUser(User("testuser1", "password123", "testuser1@example.com"))
        userAPI.addUser(User("testuser2", "password456", "testuser2@example.com"))
    }

    @Test
    fun addUserIncreasesUserCount() {
        val initialCount = userAPI.numberOfUsers()
        userAPI.addUser(User("testuser3", "password789", "testuser3@example.com"))
        Assertions.assertEquals(initialCount + 1, userAPI.numberOfUsers())
    }

    @Test
    fun removeUserDecreasesUserCount() {
        val initialCount = userAPI.numberOfUsers()
        userAPI.removeUser(0)
        Assertions.assertEquals(initialCount - 1, userAPI.numberOfUsers())
    }

    @Test
    fun removeUserReturnsCorrectUser() {
        val user = userAPI.removeUser(0)
        Assertions.assertNotNull(user)
        Assertions.assertEquals("testuser1", user?.username)
    }

    @Test
    fun updateUserUpdatesUserDataCorrectly() {
        val updatedUser = User("updatedUser", "newpassword", "newemail@example.com")
        userAPI.updateUser(0, updatedUser)
        val user = userAPI.findUser(0)
        Assertions.assertNotNull(user)
        Assertions.assertEquals("updatedUser", user?.username)
        Assertions.assertEquals("newemail@example.com", user?.email)
    }

    @Test
    fun findUserByIndexReturnsCorrectUser() {
        val user = userAPI.findUser(0)
        Assertions.assertNotNull(user)
        Assertions.assertEquals("testuser1", user?.username)
    }

    @Test
    fun searchByUsernameReturnsCorrectUsers() {
        val users = userAPI.searchByUsername("testuser1")
        Assertions.assertEquals(1, users.size)
        Assertions.assertEquals("testuser1", users[0].username)
    }

    @Test
    fun searchByUsernameReturnsEmptyListWhenNotFound() {
        val users = userAPI.searchByUsername("nonexistentuser")
        Assertions.assertTrue(users.isEmpty())
    }

    @Test
    fun numberOfUsersReturnsCorrectCount() {
        val count = userAPI.numberOfUsers()
        Assertions.assertEquals(2, count)
    }
}
