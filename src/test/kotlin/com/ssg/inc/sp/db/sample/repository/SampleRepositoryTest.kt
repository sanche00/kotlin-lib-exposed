package com.ssg.inc.sp.db.sample.repository

import com.ssg.inc.sp.db.exposed.DBFactory
import com.ssg.inc.sp.db.exposed.Profile
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SampleRepositoryTest {
    private val dbFactory: DBFactory = DBFactory(Profile.LOCAL)
    private val repository:SampleRepository = SampleRepository(dbFactory)
    @BeforeEach
    fun before() {
    }
    @Test
    fun getAllTest() = runBlocking {

        val ret = repository.getAll();
        println(ret)
    }
}