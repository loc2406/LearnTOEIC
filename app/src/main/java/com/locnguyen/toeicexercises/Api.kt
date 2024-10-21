package com.locnguyen.toeicexercises

import android.util.Log
import com.locnguyen.toeicexercises.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


class Api() {

    private val baseUrl: String = "http://10.11.96.170:3000"

    private var api: CallAPI? = null
        get(){
            return field ?: let{
                field = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(CallAPI::class.java)

                field
            }
        }

//    fun getListExams(level: Double, prepareExecute: (() -> Unit)?, execute : ((String?) -> Unit)?){
//        prepareExecute?.invoke()
//        api?.getListExam(level)?.enqueue(object: Callback<String> {
//            override fun onResponse(call: Call<String>, response: Response<String>) {
//                val responseMessage = response.message() // tin nhắn trả về khi call dữ liệu
//               /*
//               * Code trả về khi call dữ liệu
//               * - 2xx: call dữ liệu thành công
//               *- 4xx: call dữ liệu thất bại, lỗi bắt nguồn từ mobile dev
//               *- 5xx: call dữ liệu thất bại, lỗi bắt nguồn từ backend
//               * */
//                val responseCode = response.code()
//
//                execute?.let { exe ->
//                    if (response.isSuccessful){
//                        response.body()?.let{ res ->
//                            exe.invoke(res)
//                        } ?: exe.invoke("")
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<String>, t: Throwable) {
//               execute?.invoke(null)
//            }
//        })
//    }

//    suspend fun allUsers(): List<User> {
//        return withContext(Dispatchers.IO) {
//            try {
//                val response = api!!.allUser().execute()
//                if (response.isSuccessful) {
//                    response.body() ?: listOf(User(1, "Empty"))
//                } else {
//                    listOf(User(1, "Empty1"))
//                }
//            } catch (e: Exception) {
//                Log.d("CALLAPI", e.message.toString())
//                listOf(User(1, "Empty2"))
//            }
//        }
//    }


    interface CallAPI{
//        @GET("Questions/listJLpt")
//        fun getListExam(@Query("level") level: Double): Call<String>

        @GET("/users")
        fun allUser(): Call<List<User>>
    }
}