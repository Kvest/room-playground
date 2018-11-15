package com.kvest.roomplayground.db

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kvest.roomplayground.db.dao.ItemDao
import com.kvest.roomplayground.db.dao.StatefulItemDao
import com.kvest.roomplayground.db.entity.ItemEntity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class StatefulItemDaoTest {
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var db: PlaygroundDb
    private lateinit var dao: ItemDao
    private lateinit var statefulItemDao: StatefulItemDao

    @Before
    fun initDb() {
        db = createDB(InstrumentationRegistry.getContext())
        dao = db.itemDao()
        statefulItemDao = db.statefulItemDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun baseTest() {
        dao.insertAll(
            listOf(
                ItemEntity(1L, "Item1"),
                ItemEntity(2L, "Item2"),
                ItemEntity(3L, "Item3")
            )
        )

        val all = statefulItemDao.getAll()
        assertEquals(3, all.size)
        all.forEach {
             assertNull(it.state)
        }
    }
}