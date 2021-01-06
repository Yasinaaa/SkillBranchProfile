package ru.skillbranch.skillarticles.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ru.skillbranch.skillarticles.data.local.entities.ArticleCounts
import ru.skillbranch.skillarticles.data.local.entities.Category
import ru.skillbranch.skillarticles.data.local.entities.CategoryData
import ru.skillbranch.skillarticles.data.local.entities.CategoryWithArticles

@Dao
interface CategoriesDao : BaseDao<Category> {
    @Transaction
    fun upsert(list: List<Category>){
        insert(list).mapIndexed {index, recordResult ->
            if(recordResult == -1L) list[index] else null
        }.filterNotNull().also{
            if(it.isNotEmpty()) update(it)
        }
    }


    @Query("""
        SELECT category.title as title, category.icon as icon, category.category_id as category_id,
        COUNT(article.category_id) as articles_count
        FROM article_categories as category
        INNER JOIN articles AS article ON category.category_id = article.category_id
        GROUP BY category.category_id ORDER BY articles_count DESC
    """)
    fun findAllCategoriesData(): LiveData<List<CategoryData>>


    @Query("""
           SELECT * from article_categories WHERE category_id = :id 
        """)
    fun findCategoryWithArticles(id:String):List<CategoryWithArticles>

}