package com.applifehack.knowledge.ui.admin.rsspost

import com.applifehack.knowledge.data.entity.Post

import com.applifehack.knowledge.data.network.response.RssCatResp
import org.jsoup.nodes.Document

class URLWraper (var rssCatResp: RssCatResp,var pageIndex:Int) {


    fun pageUrl() :String? = if(pageIndex==1) rssCatResp.feed else
        "${rssCatResp.feedPageUrl}$pageIndex"
    fun cssQuery (document: Document):List<Post>{
        return rssCatResp.cSSQuery(document)
    }
}