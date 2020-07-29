package com.applifehack.knowledge.ui.admin.crawler.health

import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.data.entity.PostType
import com.applifehack.knowledge.data.network.response.CatResp
import com.applifehack.knowledge.ui.admin.BaseCrawler
import com.applifehack.knowledge.util.CategoryType
import kotlinx.coroutines.coroutineScope
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.util.*

class Eatingwell : BaseCrawler() {


    override suspend fun getPosts(doc: Document, categoryType: CategoryType): List<Post> = coroutineScope {

        val cat = CatResp().getCatByType(categoryType)
        var elements = doc.select("div.cardsContainer")
        elements.forEach {

            var mediaElement = it.select("a img[src~=(?i)\\.(png|jpe?g)]").attr("abs:src")
            var linksElement = it.select("a[href]").attr("abs:href")
            var titleElement = it.select("a[title]").attr("title")
            if (mediaElement.isNotEmpty() && linksElement.isNotEmpty() && titleElement.isNotEmpty()) {
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

    override fun extractImage(el: Element): String? {
        return el.select("a img[src~=(?i)\\.(png|jpe?g)]").attr("abs:src")
    }

    override fun extractTitle(el: Element): String? {
        return el.select("a[title]").attr("title")
    }

    override fun extractLink(el: Element): String? {
        return el.select("a[href]").attr("abs:href")
    }
}