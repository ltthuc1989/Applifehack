package com.applifehack.knowledge.ui.admin.crawler.discovery

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

class Livescience :BaseCrawler() {

    override suspend fun getPosts(doc: Document,categoryType:CategoryType): List<Post>  = coroutineScope {
        val cat = CatResp().getCatByType(categoryType)
        var elements = doc.select("div.listingResult")
        elements.forEach {

            var mediaElement = async {extractImage(it)}.await()
            var linksElement = async { extractLink(it) }.await()
            var titleElement = async { extractTitle(it) }.await()
            if (mediaElement?.isNotEmpty()==true && linksElement?.isNotEmpty() ==true && titleElement?.isNotEmpty()==true) {
                var idPost = formatId(titleElement)
                if (idPost?.isEmpty() != true) {
                    result.add(
                        Post()
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


                            })
                }

            }

        }

        result
    }

    override  fun extractImage(el: Element): String? {
        try {

            FormatterUtil.extractUrls(el.select("img").toString())?.forEach {
                if (it.contains("1024")) return it
            }
        }catch (ex:Exception){
            ex.printStackTrace()
            return ""
        }
        return ""

    }


    override  fun extractTitle(el: Element): String?{
        try{
        return el.select("h3").text()
        }catch (ex:Exception){
            ex.printStackTrace()

        }
        return ""

    }

    override fun extractLink(el: Element): String? {
        try {
            return el.select("a[href]")[0].attr("abs:href")
        }catch (ex:Exception){
            ex.printStackTrace()
        }
        return ""

    }
}
