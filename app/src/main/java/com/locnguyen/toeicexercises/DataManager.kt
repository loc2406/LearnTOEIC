package com.locnguyen.toeicexercises

import android.content.Context
import android.util.Log
import com.locnguyen.toeicexercises.database.TheoryDb
import com.locnguyen.toeicexercises.model.Example
import com.locnguyen.toeicexercises.model.Grammar
import com.locnguyen.toeicexercises.model.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

object DataManager {

    private val _isDataLoaded: MutableStateFlow<Boolean> by lazy {MutableStateFlow(false)}
    val isDataLoaded: StateFlow<Boolean> by lazy { _isDataLoaded}

    private val _err: MutableStateFlow<String?> by lazy {MutableStateFlow(null)}
    val err: StateFlow<String?> by lazy { _err}

    private val _words: MutableStateFlow<List<Word>> by lazy {MutableStateFlow(emptyList())}
    val words: StateFlow<List<Word>> by lazy { _words}

    private val _grammars: MutableStateFlow<List<Grammar>> by lazy {MutableStateFlow(emptyList())}
    val grammars: StateFlow<List<Grammar>> by lazy { _grammars}

    private val _examples: MutableStateFlow<List<Example>> by lazy {MutableStateFlow(emptyList())}
    val examples: StateFlow<List<Example>> by lazy { _examples}

    suspend fun initData(context: Context){
        withContext(Dispatchers.IO){
            try{
                val db = MyApplication.instance.getDbInstance()

                _words.value = db.getListWord()
                _examples.value = db.getListExamples()
                _grammars.value = db.getListGrammars()

                _isDataLoaded.value = true
            }catch (e: Exception){
                _err.value = context.getString(R.string.Load_data_failed)
            }
        }
    }
}