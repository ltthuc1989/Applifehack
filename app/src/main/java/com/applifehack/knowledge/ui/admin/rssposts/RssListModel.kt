package com.applifehack.knowledge.ui.admin.rssposts

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class RssListModel(val type: String?, val feedPageUrl: String?, val feedUrl: String?): Parcelable