package com.applifehack.knowledge.ui.admin.crawler.health

import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.data.entity.PostType
import com.applifehack.knowledge.data.network.response.CatResp
import com.applifehack.knowledge.ui.admin.BaseCrawler
import com.applifehack.knowledge.util.CategoryType
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.util.*

class Bestlifeonline : BaseCrawler() {


    override suspend fun getPosts(doc: Document, categoryType: CategoryType): List<Post> = coroutineScope {

        val cat = CatResp().getCatByType(categoryType)
        var elements = doc.select("div.thumbnail")
        elements.forEach {

            var mediaElement = async { extractImage(it)}.await()
            var linksElement = async { extractLink(it) }.await()
            var titleElement = async { extractTitle(it) }.await()
            if (mediaElement?.isNotEmpty()==true && linksElement?.isNotEmpty()==true && titleElement?.isNotEmpty()==true) {
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
                                author = "BestLife"
                                authorUrl = "${className?.toLowerCase()}.com"

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

        return el.select("div.thumbnail img[src]").attr("src")

    }


    override  fun extractTitle(el: Element): String?{
        return el.select("a[title]").attr("title")

    }

    override fun extractLink(el: Element): String? {
        return el.select("a[href]").attr("abs:href")
    }
}