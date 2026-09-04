package com.ki_bun.pioneer.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Update
    suspend fun updateAll(item: List<Item>)

    @Delete
    suspend fun delete(item: Item)

    @Query("SELECT * from items ORDER BY position")
    fun getAllItems(): Flow<List<Item>>

    @Query("SELECT * FROM items")
    suspend fun getAllItemsOnce(): List<Item>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Item>)

}