package com.example.roomdemo.db

class SubscriberRepository(private val DAO : SubscriberDAO) {

    val subscribers = DAO.getAllsub()

    suspend fun insert(subscriber: Subscriber):Long{
       return DAO.insertSubscriber(subscriber)
    }


    suspend fun update(subscriber: Subscriber):Int {
       return DAO.updateSubscriber(subscriber)
    }

    suspend fun delete(subscriber: Subscriber):Int{
       return DAO.deleteSubscriber(subscriber)
    }

    suspend fun deleteAll(){
        DAO.deleteAll()
    }


}