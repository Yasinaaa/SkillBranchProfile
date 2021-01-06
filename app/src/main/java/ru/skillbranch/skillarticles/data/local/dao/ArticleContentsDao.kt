package ru.skillbranch.skillarticles.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import ru.skillbranch.skillarticles.data.local.entities.ArticleContent
import ru.skillbranch.skillarticles.data.local.entities.ArticleItem

@Dao
interface ArticleContentsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(obj:ArticleContent) : Long
}