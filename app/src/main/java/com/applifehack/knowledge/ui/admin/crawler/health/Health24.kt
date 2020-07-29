package com.applifehack.knowledge.ui.admin.crawler.health

import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.data.entity.PostType
import com.applifehack.knowledge.data.network.response.CatResp
import com.applifehack.knowledge.ui.admin.BaseCrawler
import com.applifehack.knowledge.util.CategoryType
import com.applifehack.knowledge.util.FormatterUtil
import com.ezyplanet.core.util.extension.fromJson
import com.google.gson.Gson
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.net.URL
import java.util.*


data class Health24(
    val ArticleList: List<Article>
)

data class Article(
    val Title: String,
    val URL: String,
    val Thumbnail:String
)
class  Health24Api(val endPoint:String) :BaseCrawler(){
    override suspend fun getPosts(doc: Document, catType: CategoryType): List<Post> = coroutineScope {
        val cat = CatResp().getCatByType(catType)
       val result = mutableListOf<Post>()
        async {
            val request = URL(endPoint).readText()
             Gson().fromJson<Health24>(request)
        }.await()?.ArticleList?.forEach { post->
           // var temp= doc.select("div.vjs-poster")[0].attr("style")
             //  val mediaElement = FormatterUtil.extractUrls(temp)?.get(0)
                var idPost = formatId(post.Title)
                if (idPost?.isEmpty() != true) {
                    result.add(
                        Post()
                            .apply {
                                title = post.Title
                                redirect_link = post.URL
                                imageUrl = post.Thumbnail
                                id = "$className$idPost"
                                type = PostType.ARTICLE.type
                                author = "$className"
                                authorUrl = "$className.com"

                                catId = cat.cat_id
                                catName = cat.cat_name
                                createdDate = Date()
                                author_type = author

                            })

            }
        }
         result
    }

 suspend fun getPosts(catType: CategoryType): List<Post> = coroutineScope {
        val cat = CatResp().getCatByType(catType)
        val result = mutableListOf<Post>()
        async {
            val request = URL(endPoint).readText()
            Gson().fromJson<Health24>(request)
        }.await()?.ArticleList?.forEach { post->
           // val doc = Jsoup.connect(post.URL).get()
           // var temp= doc.select("div.vjs-poster")[0].attr("style")
            //val mediaElement = FormatterUtil.extractUrls(temp)?.get(0)
            var idPost = formatId(post.Title)
            if (idPost?.isEmpty() != true) {
                result.add(
                    Post()
                        .apply {
                            title = post.Title
                            redirect_link = post.URL
                            imageUrl = post.Thumbnail
                            id = "$className$idPost"
                            type = PostType.ARTICLE.type
                            author = "$className"
                            authorUrl = "$className.com"

                            catId = cat.cat_id
                            catName = cat.cat_name
                            createdDate = Date()


                        })

            }
        }
        result
    }



    override fun extractImage(el: Element): String? {
        TODO("Not yet implemented")
    }

    override fun extractTitle(el: Element): String? {
        TODO("Not yet implemented")
    }

    override fun extractLink(el: Element): String? {
        TODO("Not yet implemented")
    }
}