package com.kvest.roomplayground.db

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kvest.roomplayground.db.dao.StateDao
import com.kvest.roomplayground.db.dao.StatefulItemDao
import com.kvest.roomplayground.db.entity.ItemEntity
import com.kvest.roomplayground.ext.waitValue
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
    private lateinit var dao: StatefulItemDao
    private lateinit var stateDao: StateDao

    @Before
    fun initDb() {
        db = createDB(InstrumentationRegistry.getContext())
        dao = db.statefulItemDao()
        stateDao = db.stateDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun stateCreationTest() {
        assertNull(stateDao.getState(1L))
        assertNull(stateDao.getState(2L))
        assertNull(stateDao.getState(3L))

        dao.insertAll(
            listOf(
                ItemEntity(1L, "Item1"),
                ItemEntity(2L, "Item2"),
                ItemEntity(3L, "Item3")
            )
        )

        val all = dao.getAll()
        assertEquals(3, all.size)
        all.forEach {
             assertEquals(0, it.state.count)
             assertTrue(it.state.changeTime > 0)
        }
        assertNotNull(stateDao.getState(1L))
        assertNotNull(stateDao.getState(2L))
        assertNotNull(stateDao.getState(3L))
    }

    @Test
    fun stateDeletionTest() {
        assertNull(stateDao.getState(1L))
        assertNull(stateDao.getState(2L))
        assertNull(stateDao.getState(3L))

        val items = listOf(
            ItemEntity(1L, "Item1"),
            ItemEntity(2L, "Item2"),
            ItemEntity(3L, "Item3")
        )
        dao.insertAll(items)

        assertNotNull(stateDao.getState(1L))
        assertNotNull(stateDao.getState(2L))
        assertNotNull(stateDao.getState(3L))

        //delete one item
        dao.deleteItem(2L)
        assertNull(stateDao.getState(2L))
        assertNotNull(stateDao.getState(1L))
        assertNotNull(stateDao.getState(3L))

        //delete all items
        dao.deleteItems(*items.toTypedArray())
        assertNull(stateDao.getState(1L))
        assertNull(stateDao.getState(2L))
        assertNull(stateDao.getState(3L))
    }

    @Test
    fun insertAllTest() {
        val items = listOf(
            ItemEntity(0L, "Item0"),
            ItemEntity(1L, "Item1"),
            ItemEntity(2L, "Item2")
        )
        dao.insertAll(items)

        val increments = arrayOf(10, -7, 22)
        increments.forEachIndexed { index, count ->
            dao.incrementCount(index.toLong(), count)
        }

        val all = dao.getAll()
        assertEquals(3, all.size)
        all.forEachIndexed { index, it ->
            assertEquals(items[index], it.item)
            //check state
            assertEquals(increments[index], it.state.count)
            assertTrue(it.state.changeTime > 0)
        }

        val oldItem = dao.getItem(0L)
        val item = ItemEntity(0L, "new Item0")
        dao.insertAll(listOf(item))
        val newItem = dao.getItem(0L)

        assertEquals(item, newItem?.item)
        assertNotEquals(oldItem?.state, newItem?.state)
    }

    @Test
    fun updateTest() {
        dao.update(ItemEntity(0L, "Item0"), ItemEntity(1L, "Item1"), ItemEntity(2L, "Item2"))
        assertNull(dao.getItem(0L))
        assertNull(dao.getItem(1L))
        assertNull(dao.getItem(2L))

        val items = listOf(
            ItemEntity(0L, "Item0"),
            ItemEntity(1L, "Item1"),
            ItemEntity(2L, "Item2")
        )
        dao.insertAll(items)

        dao.incrementCount(0L, 10)
        dao.incrementCount(1L, -10)
        dao.incrementCount(2L, -20)

        val all = dao.getAll()
        assertEquals(3, all.size)
        all.forEachIndexed { index, it ->
            assertEquals(items[index], it.item)
        }

        val newItems = listOf(
            ItemEntity(0L, "new Item0"),
            ItemEntity(1L, "new Item1"),
            ItemEntity(2L, "new Item2")
        )
        dao.update(*newItems.toTypedArray())

        dao.getAll().forEachIndexed { index, it ->
            assertEquals(newItems[index], it.item)
            assertEquals(all[index].state, it.state)
        }
    }

    @Test
    fun listenAllSortedTest() {
        val liveItems = dao.listenAllSorted()

        assertEquals(0, liveItems.waitValue()?.size)

        val items = listOf(
            ItemEntity(0L, "Item0"),
            ItemEntity(1L, "Item1"),
            ItemEntity(2L, "Item2")
        )
        dao.insertAll(items)

        assertEquals(3, liveItems.waitValue()?.size)
        assertEquals(listOf(0L, 1L, 2L), liveItems.waitValue()?.map { it.item.id })

        dao.incrementCount(1L, -22)
        assertEquals(listOf(0L, 2L, 1L), liveItems.waitValue()?.map { it.item.id })

        dao.incrementCount(0L, 10)
        assertEquals(listOf(2L, 1L, 0L), liveItems.waitValue()?.map { it.item.id })

        dao.incrementCount(2L, -1)
        dao.incrementCount(0L, -2)
        assertEquals(listOf(1L, 0L, 2L), liveItems.waitValue()?.map { it.item.id })
    }
}