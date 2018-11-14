package com.kvest.roomplayground.db

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import com.kvest.roomplayground.db.dao.ItemDao
import com.kvest.roomplayground.db.entity.ItemEntity
import com.kvest.roomplayground.ext.waitValue
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import java.lang.IllegalStateException

@RunWith(AndroidJUnit4::class)
class ItemDaoTest {
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var db: PlaygroundDb
    private lateinit var dao: ItemDao

    @Before
    fun initDb() {
        db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), PlaygroundDb::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.itemDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testInsertAll() {
        val itemsToInsert = listOf(
            ItemEntity(1, "Item1"),
            ItemEntity(2, "Item2"),
            ItemEntity(3, "Item3")
        )
        dao.insertAll(itemsToInsert)

        val items = dao.getItems()
        assertEquals(itemsToInsert.size, items.size)
        itemsToInsert.forEach {
            assertTrue(items.contains(it))
        }

        //test replace strategy
        val newItems = listOf(
            ItemEntity(2, "new Item2"),
            ItemEntity(4, "Item4")
        )
        dao.insertAll(newItems)
        assertEquals(4, dao.getItems().size)
        assertEquals(itemsToInsert.first { it.id == 1L }, dao.listenItem(1).waitValue())
        assertEquals(newItems.first { it.id == 2L }, dao.listenItem(2).waitValue())
        assertEquals(itemsToInsert.first { it.id == 3L }, dao.listenItem(3).waitValue())
        assertEquals(newItems.first { it.id == 4L }, dao.listenItem(4).waitValue())
    }

    @Test
    fun testEmpty() {
        assertEquals(0, dao.getItems().size)
        assertNull(dao.getItem(1))

        assertEquals(0, dao.listenItems().waitValue()?.size)
        assertNull(dao.listenItem(1).waitValue())
    }

    @Test
    fun testListenItems() {
        assertEquals(0, dao.listenItems().waitValue()?.size)

        val itemsToInsert = listOf(
            ItemEntity(1, "Item1"),
            ItemEntity(2, "Item2"),
            ItemEntity(3, "Item3")
        )
        dao.insertAll(itemsToInsert)

        val items = dao.listenItems().waitValue() ?: throw IllegalStateException("Items are null")
        assertEquals(itemsToInsert.size, items.size)
        itemsToInsert.forEach {
            assertTrue(items.contains(it))
        }
    }

    @Test
    fun testGetItem() {
        assertNull(dao.getItem(1))
        assertNull(dao.getItem(2))
        assertNull(dao.getItem(3))

        val itemsToInsert = listOf(
            ItemEntity(1, "Item1"),
            ItemEntity(2, "Item2"),
            ItemEntity(3, "Item3")
        )
        dao.insertAll(itemsToInsert)

        assertEquals(itemsToInsert.first { it.id == 1L }, dao.getItem(1))
        assertEquals(itemsToInsert.first { it.id == 2L }, dao.getItem(2))
        assertEquals(itemsToInsert.first { it.id == 3L }, dao.getItem(3))
    }

    @Test
    fun testListenItem() {
        assertNull(dao.listenItem(1).waitValue())
        assertNull(dao.listenItem(2).waitValue())
        assertNull(dao.listenItem(3).waitValue())

        val itemsToInsert = listOf(
            ItemEntity(1, "Item1"),
            ItemEntity(2, "Item2"),
            ItemEntity(3, "Item3")
        )
        dao.insertAll(itemsToInsert)

        assertEquals(itemsToInsert.first { it.id == 1L }, dao.listenItem(1).waitValue())
        assertEquals(itemsToInsert.first { it.id == 2L }, dao.listenItem(2).waitValue())
        assertEquals(itemsToInsert.first { it.id == 3L }, dao.listenItem(3).waitValue())
    }

    @Test
    fun testUpdate() {
        val itemsToInsert = listOf(
            ItemEntity(1, "Item1"),
            ItemEntity(2, "Item2"),
            ItemEntity(3, "Item3")
        )
        dao.insertAll(itemsToInsert)

        assertEquals(itemsToInsert.first { it.id == 1L }, dao.getItem(1))
        assertEquals(itemsToInsert.first { it.id == 2L }, dao.getItem(2))
        assertEquals(itemsToInsert.first { it.id == 3L }, dao.getItem(3))

        val newItem2 = ItemEntity(2L, "Some new value")
        dao.update(newItem2)
        assertEquals(itemsToInsert.first { it.id == 1L }, dao.getItem(1))
        assertEquals(newItem2, dao.getItem(2))
        assertEquals(itemsToInsert.first { it.id == 3L }, dao.getItem(3))

        val newItem3 = ItemEntity(3L, "Some new value for item 3")
        dao.update(newItem3)
        assertEquals(itemsToInsert.first { it.id == 1L }, dao.getItem(1))
        assertEquals(newItem2, dao.getItem(2))
        assertEquals(newItem3, dao.getItem(3))
    }

    @Test
    fun testDelete() {
        val itemsToInsert = listOf(
            ItemEntity(1, "Item1"),
            ItemEntity(2, "Item2"),
            ItemEntity(3, "Item3")
        )
        dao.insertAll(itemsToInsert)

        assertEquals(itemsToInsert.first { it.id == 1L }, dao.getItem(1))
        assertEquals(itemsToInsert.first { it.id == 2L }, dao.getItem(2))
        assertEquals(itemsToInsert.first { it.id == 3L }, dao.getItem(3))

        dao.delete(itemsToInsert.first { it.id == 2L })
        assertEquals(itemsToInsert.first { it.id == 1L }, dao.getItem(1))
        assertNull(dao.getItem(2))
        assertEquals(itemsToInsert.first { it.id == 3L }, dao.getItem(3))

        dao.delete(itemsToInsert.first { it.id == 1L })
        dao.delete(itemsToInsert.first { it.id == 3L })
        assertEquals(0, dao.listenItems().waitValue()?.size)
    }
}