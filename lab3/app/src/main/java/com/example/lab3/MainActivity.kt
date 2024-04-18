package com.example.lab3

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.inputmethod.InputBinding
import com.example.lab3.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import javax.net.ssl.SSLSessionBindingEvent
import javax.net.ssl.SSLSessionBindingListener
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    binding.playOfflineBtn.setOnClickListener {
        createOfflineGame()
    }

        binding.createOnlineGameBtn.setOnClickListener {
            createOnLineGane()
        }

        binding.joinOnlineGameBtn.setOnClickListener {
            joinOnlineGame()
        }

    }

    fun createOfflineGame(){
        GameData.saveGameModel(
            GameModel(
                gameStatus =GameStatus.JOINED
            )
        )
        startgame()
    }

    fun createOnLineGane()
    {
        GameData.myID = "X"
        GameData.saveGameModel(
            GameModel(
                gameStatus =GameStatus.CREATED,
                gameId = Random.nextInt(1000..9999).toString()
            )
        )
        startgame()
    }

    fun joinOnlineGame()
    {
    var gameId = binding.gameIdInput.text.toString()
        if(gameId.isNotEmpty()){
            binding.gameIdInput.setError("Please enter gameId")
            return
        }
        GameData.myID = "O"
        Firebase.firestore.collection("games")
            .document(gameId)
            .get()
            .addOnSuccessListener {
                val model = it?.toObject(GameModel::class.java)
                if(model==null)
                {
                    binding.gameIdInput.setError("Please enter valid game Id")
                }else{
                    model.gameStatus = GameStatus.JOINED
                    GameData.saveGameModel(model)
                    startgame()
                }
            }
    }
    fun startgame(){
        startActivity(Intent(this,GameActivity::class.java))
    }
}