package com.applifehack.knowledge.ui.admin.rssposts

import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.data.network.response.RssCatResp
import com.prof.rssparser.Article
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


data class PostContent(
        val article: Article
) {



    fun formatPubDate(): String? {
        val pubDateString = try {
            val sourceDateString = article.pubDate

            val sourceSdf = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
            val date = sourceSdf.parse(sourceDateString)

            val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            sdf.format(date)

        } catch (e: ParseException) {
            e.printStackTrace()
            article.pubDate
        }
        return pubDateString

    }

    fun getCatName(): String? {
        var categories = ""
        for (i in 0 until article.categories.size) {
            categories = if (i == article.categories.size - 1) {
                categories + article.categories[i]
            } else {
                categories + article.categories[i] + ", "
            }
        }
        return categories
    }
    fun getTitle():String?{
        return article.title
    }
    fun getImage():String?{
        return article.image
    }
    fun getLink():String?{
        return article?.link
    }

    fun toPost(postId:String,rssCatResp: RssCatResp): Post {
        val post= Post()
        post.title = getTitle()
        post.imageUrl = getImage()
        post.description =article?.description
       // post.createdDate = Timestamp.now()
        post.authorUrl= rssCatResp.domain
        post.redirect_link = article?.link
        post.id = postId
        post.author = rssCatResp.author_name

        return  post

    }

}