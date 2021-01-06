package ru.skillbranch.skillarticles.extensions.data

import ru.skillbranch.skillarticles.data.local.entities.Article
import ru.skillbranch.skillarticles.data.models.ArticleData
import ru.skillbranch.skillarticles.data.remote.res.ArticleDataRes
import java.util.*

fun ArticleDataRes.toArticle(): Article = Article(
    id = id,
    poster = poster,
    categoryId = category.categoryId,
    description = description,
    title = title,
    author = author,
    date = date,
    updatedAt = Date()

)