package com.applifehack.knowledge.data.network.response

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.applifehack.knowledge.data.entity.Post
import com.applifehack.knowledge.ui.admin.crawler.YoutubeChannel
import com.applifehack.knowledge.ui.admin.crawler.discovery.*
import com.applifehack.knowledge.ui.admin.crawler.happiness.*
import com.applifehack.knowledge.ui.admin.crawler.health.*
import com.applifehack.knowledge.ui.admin.crawler.health.Health
import com.applifehack.knowledge.ui.admin.crawler.inspiration.*
import com.applifehack.knowledge.ui.admin.crawler.selfhelp.*
import com.applifehack.knowledge.util.CategoryType
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize
import org.jsoup.nodes.Document
import java.util.*
import kotlin.collections.HashMap

@Parcelize
@IgnoreExtraProperties
data class RssCatResp constructor(
    var feed: String? = "",
    var thumb_url: String? = "",
    var title: String? = "",
    var domain: String? = "",
    var author_name: String? = "",
    var feedPageUrl: String? = "",
    var type: String = "article",
    var youtubeHtml: String = "",
    var json:Boolean = false,
    var cat_type :String = CategoryType.LIVING_HEALTHY.type
) : Parcelable {

    var event : MutableLiveData<String>?=null

    suspend fun cSSQuery(doc: Document?): List<Post> {
        when (title) {

            //discovery
            "didyouknowfacts" -> {
                return Didyouknowfacts().getPosts(doc!!,CategoryType.DISCOVERY)
            }
            "howstuffworks"->{
                return Howstuffworks().getPosts(doc!!,CategoryType.DISCOVERY)
            }
            "livescience"->{
                return Livescience().getPosts(doc!!,CategoryType.DISCOVERY)
            }
            "newscientist"->{
                return Newscientist().getPosts(doc!!,CategoryType.DISCOVERY)
            }
            "sciencedaily"->{
                return Sciencedaily().getPosts(doc!!,CategoryType.DISCOVERY)
            }
            "sciencefocus"->{
                return Sciencefocus().getPosts(doc!!,CategoryType.DISCOVERY)
            }//

            // happiness
            "goodnews"->{
                return GoodNews().getPosts(doc!!,CategoryType.HAPPINESS)
            }
            "lifehack"->{
                return Lifehack().getPosts(doc!!,CategoryType.HAPPINESS)
            }
            "livehappy"->{
                return LiveHappy().getPosts(doc!!,CategoryType.HAPPINESS)
            }
            "peacerevolution"->{
                return Peacerevolution().getPosts(doc!!,CategoryType.HAPPINESS)
            }
            "positive"->{
                return Positive().getPosts(doc!!,CategoryType.HAPPINESS)
            }

            "tinybuddha"->{
                return TinyBuddha().getPosts(doc!!,CategoryType.HAPPINESS)
            }
            "verywellmind"->{
                return Verywellmind().getPosts(doc!!,CategoryType.HAPPINESS)
            }
            "wikihow"->{
                return Wikihow().getPosts(doc!!,CategoryType.HAPPINESS)
            }
            // health
            "marthastewart" -> {

                return Marthastewart()
                    .getPosts(doc!!, CategoryType.LIVING_HEALTHY)

            }
            "besthealthmag"->{
                return Besthealthmag().getPosts(doc!!,CategoryType.LIVING_HEALTHY)
            }
            "health.com" -> {
                return Health()
                    .getPosts(doc!!, CategoryType.LIVING_HEALTHY)
            }


            "bestlifeonline"->{
                return Bestlifeonline()
                    .getPosts(doc!!,CategoryType.LIVING_HEALTHY)
            }
            "eatingwell"->{
                return Eatingwell().getPosts(doc!!,CategoryType.LIVING_HEALTHY)
            }
            "alive"->{
                return Alive()
                    .getPosts(doc!!,CategoryType.LIVING_HEALTHY)
            }
            "health24"->{
                return Health24Api(
                    feed!!
                ).getPosts(CategoryType.LIVING_HEALTHY)
            }
            "thehealthy"->{
                return Thehealthy().getPosts(doc!!,CategoryType.LIVING_HEALTHY)
            }
            // Inspiration
            "addictedInspire"->{
                return AddtictedInspire().getPosts(doc!!,CategoryType.INSPIRATION)
            }

            "motivation"->{
                return Motivation().getPosts(doc!!,CategoryType.INSPIRATION)
            }
            "pickthebrainInspire"->{
                return PickthebrainInspire().getPosts(doc!!,CategoryType.INSPIRATION)
            }
            "SuccessconsInspire"->{
                return SuccessconsInspire().getPosts(doc!!,CategoryType.INSPIRATION)
            }
            "WealthygorillaInspire"->{
                return WealthygorillaInspire().getPosts(doc!!,CategoryType.INSPIRATION)
            }
            //Self help
            "lifeoptimeze"->{
                return Lifeoptimeze()
                    .getPosts(doc!!,CategoryType.PERSONAL_GROWTH)
            }

            "AddictedSelfHelp"->{
                return AddictedSelfHelp().getPosts(doc!!,CategoryType.PERSONAL_GROWTH)

            }
            "PickTheBrainSelf"->{
                return PickTheBrainSelf().getPosts(doc!!,CategoryType.PERSONAL_GROWTH)

            }
            "ReThink"->{
                return Rethink().getPosts(doc!!,CategoryType.PERSONAL_GROWTH)

            }
            "Success"->{
                return Success().getPosts(doc!!,CategoryType.PERSONAL_GROWTH)

            }
            "ThroughCatalog"->{
                return ThroughCatalog().getPosts(doc!!,CategoryType.PERSONAL_GROWTH)

            }
            "WealthygorillaSelf"->{
                return WealthygorillaSelf().getPosts(doc!!,CategoryType.PERSONAL_GROWTH)

            }
            //quote
            "QuoteSuccess"->{
                return Brainyquote("Success").getPosts(doc!!,CategoryType.QUOTE)
            }
            "QuoteMotivational"->{
                return Brainyquote("Motivational").getPosts(doc!!,CategoryType.QUOTE)
            }
            "QuoteFriendship"->{
                return Brainyquote("Friendship").getPosts(doc!!,CategoryType.QUOTE)
            }
            "QuoteLife"->{
                return Brainyquote("Life").getPosts(doc!!,CategoryType.QUOTE)
            }
            "QuoteLove"->{
                return Brainyquote("Love").getPosts(doc!!,CategoryType.QUOTE)
            }

            else -> {
                return YoutubeChannel(title,author_name,cat_type,feed!!).getPosts(doc!!,CategoryType.LIVING_HEALTHY)
            }
        }
        return emptyList()
    }

    fun toMap(): Map<String, Any?> {
        val result = HashMap<String, Any?>()
        result["feed"] = feed
        result["feedPageUrl"] = feedPageUrl
        result["author_name"] = author_name
        result["thumb_url"] = thumb_url
        result["title"] = title
        result["type"] = type

        return result
    }



}

@Parcelize
@IgnoreExtraProperties
data class CatResp constructor(
    var cat_name: String? = "",
    var cat_thumb_url: String? = "",
    var cat_id: String? = "",
    var cat_desc: String? = "",
    var cat_type: String? = "",
    var created_date:Date?=null,
    var editing :Boolean = false
) : Parcelable {

    fun getCatByType(type: CategoryType,mediaType:String?=""): CatResp {
        when (type) {
            CategoryType.LIVING_HEALTHY -> {
                return CatResp().apply {
                    cat_id = "0"
                    cat_type = type.type
                    cat_name = "Living Healthy"

                }
            }


            CategoryType.PERSONAL_GROWTH -> {
                return CatResp().apply {
                    cat_id = "1"
                    cat_type = type.type
                    cat_name = "Personal Growth"

                }
            }

            CategoryType.HAPPINESS -> {
                return CatResp().apply {
                    cat_id = "2"
                    cat_type = type.type
                    cat_name = "Happiness"

                }
            }

            CategoryType.INSPIRATION -> {
                return CatResp().apply {
                    cat_id = "3"
                    cat_type = type.type
                    cat_name = "Inspiration"

                }
            }

            CategoryType.QUOTE -> {
                return CatResp().apply {
                    cat_id = "4"
                    cat_type = type.type
                    cat_name = mediaType

                }
            }


            CategoryType.DISCOVERY -> {
                return CatResp().apply {
                    cat_id = "5"
                    cat_type = type.type
                    cat_name = "Discovery"

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
    fun getCatByType(type: String,mediaType:String?=""): CatResp {
        when (type) {
            CategoryType.LIVING_HEALTHY.type -> {
                return CatResp().apply {
                    cat_id = "0"
                    cat_type = type
                    cat_name = "Living Healthy"

                }
            }


            CategoryType.PERSONAL_GROWTH.type -> {
                return CatResp().apply {
                    cat_id = "1"
                    cat_type = type
                    cat_name = "Personal Growth"

                }
            }

            CategoryType.HAPPINESS.type -> {
                return CatResp().apply {
                    cat_id = "2"
                    cat_type = type
                    cat_name = "Happiness"

                }
            }

            CategoryType.INSPIRATION.type -> {
                return CatResp().apply {
                    cat_id = "3"
                    cat_type = type
                    cat_name = "Inspiration"

                }
            }

            CategoryType.QUOTE.type -> {
                return CatResp().apply {
                    cat_id = "4"
                    cat_type = type
                    cat_name = mediaType

                }
            }


            CategoryType.DISCOVERY.type -> {
                return CatResp().apply {
                    cat_id = "5"
                    cat_type = type
                    cat_name = "Discovery"

                }
            }
            else -> {
                return CatResp().apply {
                    cat_id = "0"
                    cat_type = type

                }
            }
        }
    }
    fun toMap(): Map<String, Any?> {
        val result = HashMap<String, Any?>()
        result["cat_id"] = cat_id
        result["cat_desc"] = cat_desc
        result["cat_name"] = cat_name
        result["cat_thumb_url"] = cat_thumb_url
        result["cat_type"] = cat_type
        result["cat_created_date"] = com.google.firebase.Timestamp(created_date!!)
        result["editing"] = true

        return  result

    }

}
@Parcelize
@IgnoreExtraProperties
data class QuoteResp constructor(val quote_name:String?="",var quote_id:String?=""):Parcelable{
    fun toMap(): Map<String, Any?> {
        val result = HashMap<String, Any?>()
        result["quote_name"] = quote_name
        result["quote_id"] = quote_id
        result["editing"] = true
        return result
    }

}

