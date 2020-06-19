package com.applifehack.knowledge.data.network.response

import android.os.Parcelable
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.data.entity.PostType
import com.applifehack.knowledge.util.CategoryType
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

import java.util.*

@Parcelize
@IgnoreExtraProperties
data class RssCatResp constructor(
    val feed: String? = "",
    val thumb_url: String? = "",
    val title: String? = "",
    val domain: String? = "",
    val author_name: String? = "",
    var feedPageUrl: String? = "",
    var type :String ="article",
    var youtubeHtml :String =""
) : Parcelable {

    fun cSSQuery(doc: Document): List<Post> {
        val result = mutableListOf<Post>()

        when (title) {
            "marthastewart" -> {
                val cat = CatResp().getCatByType(CategoryType.LIVING_HEALTHY)
                var elements = doc.select("div.card-list-item-inner")
                elements.forEach {

                    var mediaElement = it.select("a img[src~=(?i)\\.(png|jpe?g)]").attr("abs:src")
                    var linksElement = it.select("a[href]").attr("abs:href")
                    var titleElement = it.select("h3").text()
                    if (mediaElement.isNotEmpty() && linksElement.isNotEmpty() && titleElement.isNotEmpty()) {
                        var idPost = formatId(titleElement)
                        if (idPost?.isEmpty() != true) {
                            result.add(
                                Post()
                                    .apply {
                                        title = titleElement
                                        redirect_link = linksElement
                                        imageUrl = mediaElement
                                        id = "marthastewart$idPost"
                                        type = PostType.ARTICLE.type
                                        author = "marthastewart"
                                        authorUrl = "marthastewart.com"

                                        catId = cat.cat_id
                                        catName = cat.cat_name
                                        createdDate = Date()




                                    })
                        }
                    }

                }
            }
            "health.com" -> {
                val cat = CatResp().getCatByType(CategoryType.LIVING_HEALTHY)
                var elements = doc.select("div.category-page-item")
                elements.forEach {

                    var mediaElement = it.select("a img[src~=(?i)\\.(png|jpe?g)]").attr("abs:src")
                    var linksElement = it.select("a[href]")[0].attr("abs:href")
                    var titleElement = it.select("a[data-tracking-content-headline]")[0].attr("data-tracking-content-headline")
                    if (mediaElement.isNotEmpty() && linksElement.isNotEmpty() && titleElement.isNotEmpty()) {
                        var idPost = formatId(titleElement)
                        if (idPost?.isEmpty() != true) {
                            result.add(
                                Post()
                                    .apply {
                                        title = titleElement
                                        redirect_link = linksElement
                                        imageUrl = mediaElement
                                        id = "health$idPost"
                                        type = PostType.ARTICLE.type
                                        author = "health"
                                        authorUrl = "health.com"

                                        catId = cat.cat_id
                                        catName = cat.cat_name
                                        createdDate = Date()


                                    })
                        }

                    }
                }
            }
            "drsamrobbins" -> {
                val cat = CatResp().getCatByType(CategoryType.LIVING_HEALTHY)
                var elements = doc.select("div.yt-lockup-content")
                elements.forEach {

                    var id = parseYoutubeId(it)
                    var mediaElement = formatYoutubeThumbnail(id)
                    var viewCount = parseViewCounts(it)
                    var duration = parseDuration(it)

                    var titleElement = parseYoutubeTitle(it)
                    if (mediaElement.isNotEmpty()  && titleElement.isNotEmpty()) {

                        if (!id.isNullOrEmpty()) {
                            result.add(
                                Post()
                                    .apply {
                                        title = titleElement

                                        imageUrl = mediaElement
                                        this.id = "health$id"
                                        type = PostType.VIDEO.type
                                        author = "Dr Sam Robbins"
                                        authorUrl = "https://www.drsamrobbins.com/"
                                        video_url = id
                                        this.viewCount = viewCount
                                        this.duration = duration

                                        catId = cat.cat_id
                                        catName = cat.cat_name
                                        createdDate = Date()


                                    })
                        }

                    }
                }
            }
            else -> {

            }
        }
        return result
    }
    private fun formatId(string: String):String?{
        var temp=string.split("\\s+".toRegex())
        var result =""
        temp.forEach {
            result+=it[0]
        }
        return result
    }
    private fun parseYoutubeId(el: Element) :String{
        return try {
            val href= el.select("h3.yt-lockup-title >a[href]").attr("href")
            href.split("=")[1]
        }catch (ex:Exception){
            ""
        }
    }
    private fun parseYoutubeTitle(el:Element) :String{
        return try{
            el.select("a[title]").attr("title")
        }catch (ex:Exception){
            ""
        }
    }
    private fun formatYoutubeThumbnail(id:String):String{
        return "https://i.ytimg.com/vi/$id/hqdefault.jpg"
    }

    private fun parseViewCounts(el:Element) :Int {
        return try{
            val temp = el.select("ul.yt-lockup-meta-info >li:gt(0)").text()
            temp.split("\\s+".toRegex())[0].replace(".","").toInt()
        }catch (ex:Exception){
            0
        }
    }

    private fun parseDuration(el:Element) : String {
        return try {
            var duration = ""
        val temp = el.select("span.accessible-description").text()
            temp.split("\\s+".toRegex())?.forEach {
                if(it.matches(".*\\d.*".toRegex()))  duration=it.replace(".","")
            }
             duration


        }catch (ex:Exception){
            ""
        }
    }
}

@Parcelize
@IgnoreExtraProperties
data class CatResp constructor(
    var cat_name: String? = "",
    var cat_thumb_url: String? = "",
    var cat_id: String? = "",
    var cat_desc: String? = "",
    var cat_type: String? = ""
) : Parcelable {

    fun getCatByType(type: CategoryType): CatResp {
        when (type) {
            CategoryType.HAPPINESS -> {
                return CatResp().apply {
                    cat_id = "2"
                    cat_type = type.type

                }
            }
            CategoryType.LIVING_HEALTHY -> {
                return CatResp().apply {
                    cat_id = "0"
                    cat_type = type.type
                    cat_name = "Living&Healthy"

                }
            }
            else -> {
                return CatResp().apply {
                    cat_id = "0"
                    cat_type = type.type

                }
            }
        }
    }



}

