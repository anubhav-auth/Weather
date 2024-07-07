package com.example.weather.data

import android.util.Log
import com.example.weather.data.model.ipQuery.IPQuery
import com.example.weather.data.model.location.LocationQueryData
import com.example.weather.data.model.weather.WeatherData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRepositoryImplementation(
    private val api: Api
): WeatherRepository {
    override suspend fun getWeatherData(
        key: String,
        q: String,
        days: Int,
        aqi: String,
        alerts: String
    ): Flow<Response<WeatherData>> {
        return flow{

            val weatherFromApi = try {
                api.getWeatherData(key, q, days, aqi, alerts)
            }catch (e:Exception){
                e.printStackTrace()
                emit(Response.Error(message = "problem loading"))
                return@flow
            }
            emit(Response.Success(weatherFromApi))
        }
    }

    override suspend fun locationQuery(key: String, q: String): Flow<Response<LocationQueryData>> {
        return flow {
            val locationQueryData = try {
                api.locationQuery(key, q)
            }catch (e:Exception){
                e.printStackTrace()
                emit(Response.Error(message = "Problem Loading"))
                return@flow
            }
            emit(Response.Success(locationQueryData))
        }
    }

    override suspend fun ipQuery(q: String, key: String): Flow<Response<IPQuery>> {
        return flow {
            val ipQueryData = try {
                api.ipQuery(q,key)
            }catch (e:Exception){
                e.printStackTrace()
                Log.d("mytag", "imhere${e.message}")
                emit(Response.Error(message = "Problem Loading"))
                return@flow
            }
            emit(Response.Success(ipQueryData))
        }
    }

}