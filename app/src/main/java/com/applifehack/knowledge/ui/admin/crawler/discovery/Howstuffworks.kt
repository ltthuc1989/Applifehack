package com.applifehack.knowledge.ui.admin.crawler.discovery

import android.util.Log
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.data.entity.PostType
import com.applifehack.knowledge.data.network.response.CatResp
import com.applifehack.knowledge.ui.admin.BaseCrawler
import com.applifehack.knowledge.util.CategoryType
import com.applifehack.knowledge.util.FormatterUtil
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.util.*
//feedurl https://didyouknowfacts.com/category/science/page/
class Howstuffworks :BaseCrawler() {
    private  val TAG = this::class.simpleName

    override suspend fun getPosts(doc: Document,categoryType:CategoryType): List<Post>  = coroutineScope {
        val cat = CatResp().getCatByType(categoryType)
        var elements = doc.select("a[class*=grid-tile]")
        elements.forEach {

            var mediaElement = async {extractImage(it)}.await()
            var linksElement = async { extractLink(it) }.await()
            var titleElement = async { extractTitle(it) }.await()
            Log.d("$TAG[getPost]", "$mediaElement\n $linksElement\n ${titleElement}")
            if (mediaElement?.isNotEmpty()==true && linksElement?.isNotEmpty() ==true && titleElement?.isNotEmpty()==true) {
                var idPost = formatId(titleElement)
                if (idPost?.isEmpty() != true) {
                    val post =   Post()
                        .apply {
                            title = titleElement
                            redirect_link = linksElement
                            imageUrl = mediaElement
                            id = "$className$idPost"
                            type = PostType.ARTICLE.type
                            author = "$className"
                            authorUrl = "$className.com"

                            catId = cat.cat_id
                            catName = cat.cat_name
                            createdDate = Date()

                            author_type = author
                        }
                    Log.d("$TAG[getPost] Post = ", "${post.toString()}")
                    result.add(post)

                }

            }

        }

        result
    }

    override  fun extractImage(el: Element): String? {
        val imgSearch = el.select("img").toString()
        if(imgSearch.isNotEmpty()) {
            FormatterUtil.extractUrls(el.select("img").toString())?.forEach {
                return it
            }
        } else {
            FormatterUtil.extractUrls(el.select("a[data-background]").toString())?.forEach {
                return it
            }
        }

        return ""

    }


    override  fun extractTitle(el: Element): String?{
        return el.select("[class*=mb-0]").text()

    }

    override fun extractLink(el: Element): String? {
        return el.select("a").attr("abs:href")

    }
}
