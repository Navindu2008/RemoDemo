package com.example.roomdemo

import android.util.Patterns
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomdemo.db.Subscriber
import com.example.roomdemo.db.SubscriberRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class SubscriberViewModel(private val repository: SubscriberRepository):ViewModel(),Observable {
    @Bindable
    val inputName = MutableLiveData<String>()
    @Bindable
    val inputEmail=MutableLiveData<String>()

    @Bindable
    val saveOrUpdateButtonText = MutableLiveData<String>()
    @Bindable
    val clearAllOrDeleteButtonText=MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()

    val message: LiveData<Event<String>>
        get() = statusMessage

    init{
        saveOrUpdateButtonText.value="save"
        clearAllOrDeleteButtonText.value="Clear All"
    }

    val subscribers = repository.subscribers
    private  var isUpdateOrDelete = false
    private lateinit var subscriberToUpdateOrDelete : Subscriber

    fun saveOrUpdate(){
        if(inputEmail.value==null){
            statusMessage.value = Event("enter Email ")
        }else  if(inputName.value==null){
            statusMessage.value = Event("enter Name ")
       // }else if(Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()){
          //  statusMessage.value = Event("enter proper Email ")
        }
        else {


            if (isUpdateOrDelete) {
                subscriberToUpdateOrDelete.name = inputName.value!!
                subscriberToUpdateOrDelete.email = inputEmail.value!!
                update(subscriberToUpdateOrDelete)

            } else {

                val name = inputName.value!!
                val email = inputEmail.value!!
                insert(Subscriber(0, name, email))

                inputEmail.postValue("")
                inputName.postValue("")
            }
        }
    }

    fun clearAllOrDelete(){
        if(isUpdateOrDelete){
            delete(subscriberToUpdateOrDelete)
        }else{clearAll()}

    }

    fun insert(subscriber: Subscriber) {

        viewModelScope.launch(Dispatchers.IO) {
          var newRowId =  repository.insert(subscriber)
            if(newRowId > -1) {


                statusMessage.postValue(Event("subscriber inserted successfully"))
            }else{

                statusMessage.postValue(Event("Error Occurred"))
            }
        }

    }

    fun update(subscriber: Subscriber) {
        viewModelScope.launch(Dispatchers.IO) {
          val noOfRows=  repository.update(subscriber)
            if (noOfRows >0) {

                inputName.postValue("")
                inputEmail.postValue("")
                isUpdateOrDelete = false

                saveOrUpdateButtonText.postValue("Save")
                clearAllOrDeleteButtonText.postValue("ClearAll")
                statusMessage.postValue(Event("subscriber updated successfully"))
            }else {
                statusMessage.postValue(Event("Error in updated "))
            }
        }

    }

    fun delete(subscriber: Subscriber) {
        viewModelScope.launch(Dispatchers.IO) {
            val noOfRows=  repository.delete(subscriber)
            if (noOfRows >0) {
                inputName.postValue("")
                inputEmail.postValue("")
                isUpdateOrDelete = false

                saveOrUpdateButtonText.postValue("Save")
                clearAllOrDeleteButtonText.postValue("ClearAll")
                statusMessage.postValue(Event("subscriber deleted successfully"))
            }
            else
            {
                statusMessage.postValue(Event("error in  delete"))
            }
        }

    }

    fun clearAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
            statusMessage.postValue(Event("all subscriber deleted successfully"))
        }

    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    fun initUpdateAndDelete(subscriber: Subscriber){
        inputName.value = subscriber.name
        inputEmail.value= subscriber.email
        isUpdateOrDelete=true
        subscriberToUpdateOrDelete= subscriber
        saveOrUpdateButtonText.value="Update"
        clearAllOrDeleteButtonText.value="Delete"


    }
}