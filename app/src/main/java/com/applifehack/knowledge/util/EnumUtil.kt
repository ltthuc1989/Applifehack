package com.applifehack.knowledge.util



enum class SortBy(val type:String){
    MOST_POPUPLAR("mostPopular"),
    NEWEST("newest"),
    OLDEST("oldest")

}
enum class PostDatabaseKey(val type:String){
    ID("id"),TITLE("title"),DESCRIPTION("description"),
    CREATED_DATE_TEXT("createdDateText"),LIKES_COUNT("likesCount"),
    IMG_LINK("imgLink"),CAT_ID("catId"),WEB_LINK("webLink"),
    VIDEO_URL("video_url"),REDIRECT_LINK("redirect_link"),
    TYPE("type"),VIEW_COUNT("viewCount"),CATEGORY_NAME("catName"),
    DURATION("duration"),AUTHOR("author"),QUOTE_TYPE("quote_type")

}

enum class PostStatus(val type:String){
    POSTED("posted"),PARSED("parsed"),PUBLISH("publish")
}
enum class CategoryType(val type:String){
    HAPPINESS("happiness"),
    QUOTE("quote"),
    INSPIRATION("inspiration"),
    LIVING_HEALTHY("living_healthy"),
    SELF_IMPROVEMENT("self_improvement"),
    HACK("hack"),
    FACTS("fact"),
    MEANING_PICTURE("picture")


}
enum class ShareType(val type:Int){
    NONE(-1),SHARING(0),SHARED(1)
}