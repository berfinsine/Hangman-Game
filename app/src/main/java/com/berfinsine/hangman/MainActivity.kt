package com.berfinsine.hangman

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.berfinsine.hangman.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var falseCount=0
    private var gameOverFlag=true
    private lateinit var word:String
    private lateinit var targetWord:String
    private lateinit var indexes:MutableList<Int>
    private var randomNumber=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startGame()
        for(letter in 'a'..'z'){
            val buttonId=resources.getIdentifier(letter.toString(),"id",packageName)
            val button=findViewById<View>(buttonId)
            button.setOnClickListener {
                indexes=findIndexes(binding, word, letter)
                targetWord=displayLetters(indexes, targetWord, letter)
                button.visibility=View.GONE

            }
        }

    }

    private fun startGame() {
        callBackButtons()
        falseCount=0
        binding.hangman_game.setImageResource(0)
        randomNumber=Random.nextInt(0,500)
        word=Words.DICTIONARY[randomNumber]
        createBlanks(word.length,binding)
        targetWord=binding.word.text.toString()

    }

    private fun callBackButtons() {
        for(letter in 'a'..'z'){
            val buttonId=resources.getIdentifier(letter.toString(),"id",packageName)
            val button=findViewById<View>(buttonId)
            button.visibility=View.VISIBLE
        }
    }

    private fun createBlanks(size:Int,binding: ActivityMainBinding) {
        binding.word.text="_ ".repeat(size)
    }

    private fun findIndexes(binding: ActivityMainBinding,word:String,letter:Char):MutableList<Int>{
        val indexes= mutableListOf<Int>()

        word.mapIndexed { index, char ->
            if (char == letter) {
                indexes.add(index)
            }
        }
        if(indexes.size==0){
            if(falseCount==10){
                gameOverFlag=false
                showGameOverDialog(gameOverFlag)
            }
            falseCount++
            updateImage(binding,falseCount)
        }

        return indexes
    }

    private fun updateImage(binding: ActivityMainBinding, falseCount: Int) {
        val imageName="hangman_$falseCount"
        val imageResourceId=resources.getIdentifier(imageName,"drawable",packageName)
        binding.hangman_game.setImageResource(imageResourceId)

    }


    private fun displayLetters(indexes:MutableList<Int>,targetWord:String,letter: Char):String{
        val stringBuilder=StringBuilder(targetWord)
        if(indexes.size>0){
            indexes.map {index->
                stringBuilder.setCharAt(index*2,letter.uppercaseChar())
                binding.word.text=stringBuilder.toString()
            }
        }

        if(!stringBuilder.contains("_")){
            gameOverFlag=true
            showGameOverDialog(gameOverFlag)
        }

        return stringBuilder.toString()

    }

    private fun showGameOverDialog(gameOverFlag: Boolean) {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)

        if (gameOverFlag) {
            builder.setTitle("KAZANDIN!")
            builder.setMessage("Tebrikler! Oyunu kazandın!")

            builder.setPositiveButton("Tekrar Oyna") { dialog, which ->
                startGame()
            }
            builder.setNegativeButton("Çıkış") { dialog, which ->
                System.exit(0)
            }

        }

        else{
            builder.setTitle("OYUN BİTTİ!")
            builder.setMessage("Oyunu kaybettin. Kelime ${word.uppercase()}")

            builder.setPositiveButton("Tekrar Oyna") { dialog, which ->
                startGame()
            }
            builder.setNegativeButton("Çıkış") { dialog, which ->
                System.exit(0)
            }

        }

        builder.show()

    }
}