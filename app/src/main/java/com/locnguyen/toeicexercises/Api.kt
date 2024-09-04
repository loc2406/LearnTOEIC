package com.locnguyen.toeicexercises

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class Api(private val baseUrl: String = "https://mytest.eupgroup.net/resapi/") {

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

    fun getListExams(level: Double, prepareExecute: (() -> Unit)?, execute : ((String?) -> Unit)?){
        prepareExecute?.invoke()
        api?.getListExam(level)?.enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                val responseMessage = response.message() // tin nhắn trả về khi call dữ liệu
               /*
               * Code trả về khi call dữ liệu
               * - 2xx: call dữ liệu thành công
               *- 4xx: call dữ liệu thất bại, lỗi bắt nguồn từ mobile dev
               *- 5xx: call dữ liệu thất bại, lỗi bắt nguồn từ backend
               * */
                val responseCode = response.code()

                execute?.let { exe ->
                    if (response.isSuccessful){
                        response.body()?.let{ res ->
                            exe.invoke(res)
                        } ?: exe.invoke("")
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
               execute?.invoke(null)
            }
        })
    }

    interface CallAPI{
        @GET("Questions/listJLpt")
        fun getListExam(@Query("level") level: Double): Call<String>
    }
}