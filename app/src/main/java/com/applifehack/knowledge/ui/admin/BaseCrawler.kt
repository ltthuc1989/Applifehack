package com.applifehack.knowledge.ui.admin
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.util.CategoryType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
abstract class BaseCrawler {
    val result = mutableListOf<Post>()

    var className :String?=""
    var authorName :String?=""

    constructor(authorName:String?=""){
        className = this::class.java.simpleName.toLowerCase()
        this.authorName = authorName

    }

    protected fun formatId(string: String?):String?{
        var temp=string?.split("\\s+".toRegex())
        var result =""
        temp?.forEach {
            if(it.isNotEmpty()) {
                result+=it[0]
            }
        }
        return result
    }
    protected fun parseYoutubeId(el: Element) :String{
        return try {
            val href= el.select("a[href]")[0].attr("href")
            href.split("v=")[1]
        }catch (ex:Exception){
            ""
        }
    }
    protected fun parseYoutubeTitle(el:Element) :String{
        return try{
            el.select("h4").text()
        }catch (ex:Exception){
            ""
        }
    }
    protected fun formatYoutubeThumbnail(id:String):String{
        return "https://i.ytimg.com/vi/$id/hqdefault.jpg"
    }

    protected fun parseViewCounts(el:Element) :String {
        return try{
            val temp = el.select("div.compact-media-item-stats").text()
            temp.split("\\s+".toRegex())[0]
        }catch (ex:Exception){
            ""
        }
    }

    protected fun parseDuration(el:Element) : String {
        return try {
            el.select("ytm-thumbnail-overlay-time-status-renderer").text()




        }catch (ex:Exception){
            ""
        }
    }
    abstract suspend fun getPosts(doc: Document,catType: CategoryType):List<Post>

   abstract  fun  extractImage(el: Element) :String?
    abstract   fun  extractTitle(el: Element) :String?
    abstract   fun  extractLink(el: Element) :String?
}