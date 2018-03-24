package ru.geekbrains.bashim.data

import com.google.gson.annotations.SerializedName


data class Quote(
        @SerializedName("site") val site: String,
        @SerializedName("name") val name: String,
        @SerializedName("desc") val desc: String,
        @SerializedName("link") val link: String,
        @SerializedName("elementPureHtml") val htmlText: String)


data class QuoteOfSource(
        @SerializedName("site") val site: String,
        @SerializedName("name") val name: String,
        @SerializedName("url") val url: String,
        @SerializedName("parsel") val parsel: String,
        @SerializedName("encoding") val encoding: String,
        @SerializedName("linkpar") val linkpar: String,
        @SerializedName("desc") val desc: String)
