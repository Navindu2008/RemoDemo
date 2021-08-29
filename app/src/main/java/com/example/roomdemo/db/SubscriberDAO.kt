package com.example.roomdemo.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SubscriberDAO {

   @Insert
     fun insertSubscriber(subscriber: Subscriber):Long
    @Update
     fun updateSubscriber(subscriber: Subscriber) : Int

    @Delete
     fun deleteSubscriber(subscriber: Subscriber) :Int

    @Query("Delete from subscriber_data_table")
     fun deleteAll()


    @Query("Select * from subscriber_data_table")
     fun getAllsub():LiveData<List<Subscriber>>

}