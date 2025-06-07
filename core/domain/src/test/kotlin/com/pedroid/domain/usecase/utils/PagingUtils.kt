package com.pedroid.domain.usecase.utils

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
object PagingUtils {

    // Utilitário para coletar PagingData em testes
    suspend fun <T : Any> collectPagingData(
        flow: Flow<PagingData<T>>,
        dispatcher: CoroutineDispatcher = UnconfinedTestDispatcher()
    ): List<T> {
        val differ = AsyncPagingDataDiffer(
            diffCallback = object : DiffUtil.ItemCallback<T>() {
                override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
                override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
                    oldItem == newItem
            },
            updateCallback = object : ListUpdateCallback {
                override fun onInserted(position: Int, count: Int) = Unit
                override fun onRemoved(position: Int, count: Int) = Unit
                override fun onMoved(fromPosition: Int, toPosition: Int) = Unit
                override fun onChanged(position: Int, count: Int, payload: Any?) = Unit
            },
            workerDispatcher = dispatcher
        )

        flow.collect { pagingData ->
            differ.submitData(pagingData)
        }

        return differ.snapshot().items
    }
}