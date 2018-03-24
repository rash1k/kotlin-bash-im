package ru.geekbrains.bashim.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface BashImApiService {

    @GET("api/get")
    //Заворачиваем в Observable чтобы легко было работать в Activity
    fun searchQuotes(
            @Query("site") site: String,
            @Query("name") name: String,
            @Query("num") num: String): io.reactivex.Observable<List<Quote>>


    @GET("api/sources")
    //Заворачиваем в Observable чтобы легко было работать в Activity
    //Метод возвращает вложенный масив так-как API сервера
    // возвращает в JSON вложенный масив
    fun searchSourcesOfQuotes(): io.reactivex.Observable<List<List<QuoteOfSource>>>

    @GET("api/random")
    fun searchRandom(@Query("num") num: String): io.reactivex.Observable<List<Quote>>


    //Создаем объект компаньон для фабричного метода создания Retrofit
    //как единой точки запросов в сеть
    companion object Factory {
        fun create(): BashImApiService {

            //Создаем GSON объект для парсинга JSON в наши объекты(Quote,QuoteOfSource) в data пакете
            //Но наш сайт не поддерживает спецификацию RFC 4627 и для этого мы ее отключаем setLenient()
            //Если сайт с хорошей API то передавать gson в addConverterFactory() не нужно
            val gson: Gson = GsonBuilder().setLenient().create()

            //Потом создаем сам Retrofit для работы с сетью.
            //-> Далее добавляем фабрику для возврата объекта Call который содержит
            //преднастроенные адреса вызовов для запроса например Call.()...
            // Но мы будем использовать RxJava чтобы за нас делала все Rx-библиотека
            val retrofit: Retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl("http://umorili.herokuapp.com/")
                    .build()

            return retrofit.create(BashImApiService::class.java)
        }
    }
}