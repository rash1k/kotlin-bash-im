package ru.geekbrains.bashim.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import butterknife.BindView
import butterknife.ButterKnife
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.geekbrains.bashim.R
import ru.geekbrains.bashim.data.Quote
import ru.geekbrains.bashim.data.SearchRepository
import ru.geekbrains.bashim.data.SearchRepositoryProvider


private const val tag: String = "QuotesActivity"
const val INTENT_EXTRA_NAME: String = "intent_name"

const val INTENT_EXTRA_SITE: String = "intent_site"

class QuotesActivity : AppCompatActivity() {

    @BindView(R.id.list)
    internal lateinit var listView: RecyclerView
    private lateinit var adapter: SourceOfQuotesAdapter

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val repository: SearchRepository = SearchRepositoryProvider.provideSearchRepository()
    private val list: MutableList<Quote> = mutableListOf()
//    private val list: MutableList<QuoteOfSource> = mutableListOf()


    /*
* Что позволяет RxJava. Она позволяет выносить какие-то методы в background потоки и возвращать
* результат в основной поток(UI)
*
* compositeDisposable.add -> Добавляем часть(источник) в контейнер RxJava.
* Добавляем репозиторий который ищет "источники цитат"
*
* observeOn()-> Мы подписываемся на Android планировщик для главного потока и
* говорим что главный источник сообщений будет для нас UI-поток
*
*subscribeOn-> Далее мы подписываемся на все операции Input-Output с помощью планировщика задач
*и даем ему задачу поиска "источников цитат"
*
*subscribe-> Далее подписываемся на результат после того как отработает поиск "источников цитат"
*
* Далее мы в цикле перебираем источники и добавляем в изменяемый список "list"
* */


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Initialization ButterKnife
        setContentView(R.layout.activity_main)

        ButterKnife.bind(this)
        val llm = LinearLayoutManager(this)

        llm.orientation = LinearLayoutManager.VERTICAL
        listView.layoutManager = llm

        val name = intent.getStringExtra(INTENT_EXTRA_NAME)
        val site = intent.getStringExtra(INTENT_EXTRA_SITE)

        Log.d(tag, "name: $name site: $site")

//         compositeDisposable.add(repository.searchQuotes(name, site)
        compositeDisposable.add(repository.searchQuotesRandom()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { result ->
                    list.addAll(result)

                    listView.adapter = QuotesAdapter(list, this)
                    //                    Log.d(tag, list.toString())
                })

        /* compositeDisposable.add(repository.searchSourceOfQuotes()
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribeOn(Schedulers.io())
                 .subscribe { result: List<List<QuoteOfSource>> ->
                     result.forEach { list.addAll(it) }
                     *//*for ((index, value) in result.withIndex()) {
                        listOfSourceOfQuotes.addAll(index, value)
                    }*//*

                    adapter = SourceOfQuotesAdapter(list, this)

                    listView.adapter = adapter
                })*/
    }
}