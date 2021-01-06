package ru.skillbranch.skillarticles.data.local.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import ru.skillbranch.skillarticles.data.local.entities.Article
import ru.skillbranch.skillarticles.data.local.entities.ArticleFull
import ru.skillbranch.skillarticles.data.local.entities.ArticleItem
import ru.skillbranch.skillarticles.data.local.entities.ArticleWithShareLink

@Dao
interface ArticlesDao:BaseDao<Article> {

    @Transaction
    fun upsert(list: List<Article>){
        insert(list).mapIndexed {index, recordResult ->
            if(recordResult == -1L) list[index] else null
        }.filterNotNull().also{
            if(it.isNotEmpty()) update(it)
        }
    }

    @Query("SELECT * FROM ARTICLES")
    fun findArticles():LiveData<List<Article>>

    @Query("SELECT * FROM ARTICLES WHERE ID = :id")
    fun findArticleById(id:String):LiveData<Article>

    @Query("SELECT * FROM ARTICLEITEM")
    fun findArticleItems(): LiveData<List<ArticleItem>>

    @Query("""
        SELECT * FROM ARTICLEITEM WHERE category_id IN (:categoryIds)
    """)
    fun findArticleItemsByCategoryIds(categoryIds:List<String>):LiveData<List<ArticleItem>>

    @Query("""
        SELECT * FROM ARTICLEITEM
        INNER JOIN article_tag_x_ref as refs ON refs.a_id = id          
        WHERE refs.t_id = :tagId
    """)
    fun findArticlesByTagId(tagId:String):LiveData<List<ArticleItem>>

    @RawQuery(observedEntities = [ArticleItem::class])
    fun findArticlesByRaw(simpleSQLiteQuery: SimpleSQLiteQuery): DataSource.Factory<Int, ArticleItem>

    @Query("""
        SELECT * FROM ARTICLEFULL WHERE id = :articleId
    """)
    fun findFullArticle(articleId:String):LiveData<ArticleFull>

    @Query("""
        SELECT * FROM ARTICLEFULL WHERE id = :articleId
    """)
    fun findArticleWithShareLink(articleId:String):ArticleWithShareLink

}