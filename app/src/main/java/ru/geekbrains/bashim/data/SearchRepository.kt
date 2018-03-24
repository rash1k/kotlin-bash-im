package ru.geekbrains.bashim.data

import io.reactivex.Observable


//Этот класс нам нужен для того чтобы мы из MainActivity могли пользоваться Retrofit
//И для того чтобы везде не реализовывать BashImApiService
//Как бы класс прослойка и методы делегируют выполнение BashImApiService
class SearchRepository(private val apiService: BashImApiService) {

    fun searchQuotes(site: String, name: String): io.reactivex.Observable<List<Quote>> {
        return apiService.searchQuotes(site, name, num = "50")
    }

    fun searchQuotesRandom(): io.reactivex.Observable<List<Quote>> {
        return apiService.searchRandom(num = "10")
    }

    fun searchSourceOfQuotes(): Observable<List<List<QuoteOfSource>>> {
        return apiService.searchSourcesOfQuotes()
    }


}