package ru.geekbrains.bashim.ui

import android.content.Intent
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
import ru.geekbrains.bashim.data.QuoteOfSource
import ru.geekbrains.bashim.data.SearchRepository
import ru.geekbrains.bashim.data.SearchRepositoryProvider


private const val tag: String = "MainActivity"

class MainActivity : AppCompatActivity(),
        SourceChangeListener {

    override fun changeQuotes(position: Int) {
        Log.d(tag, "${adapter.get(position)}")
        val intent = Intent(applicationContext, QuotesActivity::class.java)
        intent.putExtra(INTENT_EXTRA_NAME, adapter[position].name)
        intent.putExtra(INTENT_EXTRA_SITE, adapter[position].site)
        startActivity(intent)
    }

    @BindView(R.id.list)
    internal lateinit var listView: RecyclerView

    private lateinit var adapter: SourceOfQuotesAdapter
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val repository: SearchRepository = SearchRepositoryProvider.provideSearchRepository()

    private val listOfSourceOfQuotes: MutableList<QuoteOfSource> = mutableListOf()
    private val list: MutableList<Quote> = mutableListOf()

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
* Далее мы в цикле перебираем источники и добавляем в изменяемый список "listOfSourceOfQuotes"
* */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Initialization ButterKnife
        setContentView(R.layout.activity_main)

        ButterKnife.bind(this)
        val llm = LinearLayoutManager(this)

        llm.orientation = LinearLayoutManager.VERTICAL
        listView.layoutManager = llm

        compositeDisposable.add(repository.searchSourceOfQuotes()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { result: List<List<QuoteOfSource>> ->
                    result.forEach { listOfSourceOfQuotes.addAll(it) }
                    for ((index, value) in result.withIndex()) {
                        listOfSourceOfQuotes.addAll(index, value)
                    }

                    adapter = SourceOfQuotesAdapter(listOfSourceOfQuotes, this)
                    adapter.addListener(this)

                    listView.adapter = adapter



                    Log.d(tag, listOfSourceOfQuotes.toString())
                })
    }
}