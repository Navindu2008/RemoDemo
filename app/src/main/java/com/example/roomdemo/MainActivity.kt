package com.example.roomdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Dao
import com.example.roomdemo.databinding.ActivityMainBinding
import com.example.roomdemo.db.Subscriber
import com.example.roomdemo.db.SubscriberDatabase
import com.example.roomdemo.db.SubscriberRepository
import kotlinx.coroutines.selects.select

class MainActivity : AppCompatActivity() {

    private lateinit var  binding: ActivityMainBinding
    private lateinit var subscriberViewModel: SubscriberViewModel
    private lateinit var adaptor: MyRecylerViewAdaptor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        val dao  = SubscriberDatabase.getInstance(application).subscriberDAO
        val repository = SubscriberRepository(dao)
        val factory= SubscriberViewDataModelFactory(repository)

        subscriberViewModel = ViewModelProvider(this,factory).get(SubscriberViewModel::class.java)
        binding.myViewModel= subscriberViewModel
        binding.lifecycleOwner= this
       initRecylerView()

        subscriberViewModel.message.observe(this, Observer { it.getContentIfNotHandled()?.let {
            Toast.makeText(this,it,Toast.LENGTH_LONG).show()
        } })


    }

    private fun displaySubscribersList(){
        subscriberViewModel.subscribers.observe(this, Observer {
        Log.i("MyTag",it.toString())

            adaptor.setList(it)
            adaptor.notifyDataSetChanged()


        })
    }


    private fun initRecylerView(){
        binding.subscriberRecyclerView.layoutManager = LinearLayoutManager(this)
        adaptor = MyRecylerViewAdaptor(
            {selectedItem: Subscriber->listItemClicked(selectedItem)})
        binding.subscriberRecyclerView.adapter=adaptor
        displaySubscribersList()
    }

    private fun listItemClicked(subscriber: Subscriber){
       // Toast.makeText(this,"selected name is ${subscriber.name}",Toast.LENGTH_LONG).show()
        subscriberViewModel.initUpdateAndDelete(subscriber)

    }
}