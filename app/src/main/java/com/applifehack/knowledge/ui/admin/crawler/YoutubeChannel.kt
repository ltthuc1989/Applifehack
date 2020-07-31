package com.applifehack.knowledge.ui.admin.crawler

import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.data.entity.PostType
import com.applifehack.knowledge.data.network.response.CatResp
import com.applifehack.knowledge.data.network.response.RssCatResp
import com.applifehack.knowledge.ui.admin.BaseCrawler
import com.applifehack.knowledge.util.CategoryType
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.net.URL
import java.util.*

class YoutubeChannel(var titlePost:String?,var author_name:String?,var categoryType: String,var feed:String) :BaseCrawler() {

    override suspend fun getPosts(doc: Document,Type: CategoryType): List<Post>  = coroutineScope {
        val cat = CatResp().getCatByType(categoryType)
        var elements = doc.select("div.compact-media-item")
        elements.forEach {


            var id = parseYoutubeId(it)
            var mediaElement = formatYoutubeThumbnail(id)
            var viewCount = async {parseViewCounts(it)}
            var duration = async {parseDuration(it)}
            var authoElement = async { parseAuthor(it) }
            var titleElement = async {parseYoutubeTitle(it)}
            val author = authoElement.await()
            val title = titleElement.await()
            var time = duration.await()
            var vCount = viewCount.await()

            if (mediaElement.isNotEmpty()  && title.isNotEmpty()) {
                var idPost = formatId(title)
                if (!id.isNullOrEmpty()) {
                    result.add(
                        Post()
                            .apply {
                                this.title = title

                                imageUrl = mediaElement
                                this.id = "${titlePost}$idPost"
                                type = PostType.VIDEO.type
                                this.author = author

                                video_url = id
                                this.video_views= vCount
                                this.duration = time

                                catId = cat.cat_id
                                catName = cat.cat_name
                                createdDate = Date()
                                author_type =  "Video-$categoryType"

                            })
                }

            }
        }
        result
    }


   private fun parseAuthor(el: Element):String{
      return try{
         val autho= el.select("div.compact-media-item-byline").text()
          if(autho?.isEmpty()==true) "LifeKnowledge" else autho
      }catch (ex:Exception){
          "LifeKnowledge"
      }
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