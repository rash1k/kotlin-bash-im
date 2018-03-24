package ru.geekbrains.bashim.data

//Класс для создания SearchRepository
object SearchRepositoryProvider {

    fun provideSearchRepository(): SearchRepository {
        return SearchRepository(BashImApiService.create())
    }
}