package com.applifehack.knowledge.ui.admin.crawler.health

import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.data.entity.PostType
import com.applifehack.knowledge.data.network.response.CatResp
import com.applifehack.knowledge.ui.admin.BaseCrawler
import com.applifehack.knowledge.util.CategoryType
import com.applifehack.knowledge.util.UnsplashHelper
import com.facebook.stetho.json.ObjectMapper
import com.google.gson.Gson
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.net.URL
import java.util.*

class Brainyquote(val quoteType:String) :BaseCrawler() {

    override suspend fun getPosts(doc: Document,categoryType:CategoryType): List<Post>  = coroutineScope {
        val cat = CatResp().getCatByType(categoryType)
        var elements = doc.select("div.clearfix")
        elements.forEach {

           // var mediaElement = async {extractImage(it)}.await()
           // var linksElement = async { extractLink(it) }.await()
            var titleElement = it.select("a[title]")
            var content = titleElement[0].text()
            var author = titleElement[1].text()
//            var imageUrl = async {
//               UnsplashHelper().getPhotoUrl("$quoteType")
//
//            }.await()

            if ( content?.isNotEmpty()==true&&author?.isNotEmpty()==true) {
                var idPost = formatId(content)
                if (idPost?.isEmpty() != true) {
                    result.add(
                        Post()
                            .apply {
                                title = content

                                id = "$className$idPost"
                                type = PostType.QUOTE.type
                                this.author = author
                               // authorUrl = "$className.com"
                                authorUrl = "https://en.wikipedia.org/wiki/$author"

                                catId = cat.cat_id
                                catName = cat.cat_name + "Quote"
                                createdDate = Date()
                                quote_type = quoteType
                                this.imageUrl = imageUrl
                                author_type =  quoteType

                            })
                }

            }

        }

        result
    }

    override  fun extractImage(el: Element): String? {

        return ""

    }


    override  fun extractTitle(el: Element): String?{
        return el.select("a[title]").attr("title")

    }


    override fun extractLink(el: Element): String? {
        return el.select("a[href]")[0].attr("abs:href")

    }



}
