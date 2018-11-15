package com.kvest.roomplayground.db

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kvest.roomplayground.db.dao.StateDao
import com.kvest.roomplayground.db.entity.ItemStateEntity
import org.junit.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StateDaoTest {
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var db: PlaygroundDb
    private lateinit var dao: StateDao

    @Before
    fun initDb() {
        db = createDB(InstrumentationRegistry.getContext())
        dao = db.stateDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testIncrement() {
        dao.insertAll(
            listOf(
                ItemStateEntity(1L, 0, 0),
                ItemStateEntity(2L, 10, 0),
                ItemStateEntity(3L, 10, 0)
            )
        )

        assertEquals(0 , dao.getState(1L)?.count)
        assertEquals(10 , dao.getState(2L)?.count)
        assertEquals(10 , dao.getState(3L)?.count)

        dao.incrementCount(2L, 7)
        assertEquals(0 , dao.getState(1L)?.count)
        assertEquals(17 , dao.getState(2L)?.count)
        assertEquals(10 , dao.getState(3L)?.count)

        dao.incrementCount(1L, -7)
        dao.incrementCount(1L, -10)
        assertEquals(-17 , dao.getState(1L)?.count)
        assertEquals(17 , dao.getState(2L)?.count)
        assertEquals(10 , dao.getState(3L)?.count)

        dao.incrementCount(3L)
        assertEquals(-17 , dao.getState(1L)?.count)
        assertEquals(17 , dao.getState(2L)?.count)
        assertEquals(11 , dao.getState(3L)?.count)
    }

    @Test
    fun testDelete() {
        val itemsToInsert = listOf(
            ItemStateEntity(1L, 0, 0),
            ItemStateEntity(2L, 10, 0),
            ItemStateEntity(3L, 10, 0)
        )
        dao.insertAll(
            itemsToInsert
        )

        val items = dao.getStates()
        assertEquals(itemsToInsert.size, items.size)
        itemsToInsert.forEach { item ->
            assertEquals(item.count, items.first { it.id == item.id }.count)
        }

        dao.delete(2L)
        assertNotNull(dao.getState(1L))
        assertNull(dao.getState(2L))
        assertNotNull(dao.getState(3L))

        dao.delete(1L)
        dao.delete(3L)
        assertNull(dao.getState(1L))
        assertNull(dao.getState(2L))
        assertNull(dao.getState(3L))
    }


    @Test
    fun testChangeTime() {
        dao.insertAll(
            listOf(
                ItemStateEntity(1L, 0, 0),
                ItemStateEntity(2L, 10, 0),
                ItemStateEntity(3L, 10, 0)
            )
        )

        val changeTime1 = dao.getState(1L)?.changeTime ?: 0
        assertTrue(changeTime1 > 0)
        val changeTime2 = dao.getState(2L)?.changeTime ?: 0
        assertTrue(changeTime2 > 0)
        val changeTime3 = dao.getState(3L)?.changeTime ?: 0
        assertTrue(changeTime3 > 0)

        dao.incrementCount(3L)
        dao.incrementCount(2L)
        dao.incrementCount(1L)

        val newChangeTime1 = dao.getState(1L)?.changeTime ?: 0
        assertTrue(newChangeTime1 > changeTime1)
        val newChangeTime2 = dao.getState(2L)?.changeTime ?: 0
        assertTrue(newChangeTime2 > changeTime2)
        val newChangeTime3 = dao.getState(3L)?.changeTime ?: 0
        assertTrue(newChangeTime3 > changeTime3)

        assertTrue(newChangeTime1 > newChangeTime2)
        assertTrue(newChangeTime2 > newChangeTime3)
    }
}