package com.applifehack.knowledge.ui.admin.crawler.inspiration

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

class Motivation :BaseCrawler() {

    override suspend fun getPosts(doc: Document,categoryType:CategoryType): List<Post>  = coroutineScope {
        val cat = CatResp().getCatByType(categoryType)
        var elements = doc.select("div.tile")
        elements.forEach {

            var mediaElement = async {extractImage(it)}
            var linksElement = async { extractLink(it) }
            var titleElement = async { extractTitle(it) }
            var media = mediaElement.await()
            var links = linksElement.await()
            var title = titleElement.await()
            if (media?.isNotEmpty()==true && links?.isNotEmpty() ==true && title?.isNotEmpty()==true) {
                var idPost = formatId(title)
                if (idPost?.isEmpty() != true) {
                    result.add(
                        Post()
                            .apply {
                                title = "$title"
                                redirect_link = links
                                imageUrl = media
                                id = "$className$idPost"
                                type = PostType.ARTICLE.type
                                author = "$className"
                                authorUrl = "https://motivation.com/"

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

            return "www.motivation.com"+el.select("a img[src~=(?i)\\.(png|jpe?g)]").attr("abs:src")
        }catch (ex:Exception){
            ex.printStackTrace()
            return ""
        }
        return ""

    }


    override  fun extractTitle(el: Element): String?{
        try{
            return el.select("h2").text()
        }catch (ex:Exception){
            ex.printStackTrace()

        }
        return ""

    }

    override fun extractLink(el: Element): String? {
        try {
            return el.select("a[href]").attr("abs:href")
        }catch (ex:Exception){
            ex.printStackTrace()
        }
        return ""

    }
}
