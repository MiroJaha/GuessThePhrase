package com.example.guessthephrase

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var phraseView : TextView
    private lateinit var note : ConstraintLayout
    private lateinit var myRV : RecyclerView
    private lateinit var charButton : Button
    private lateinit var phraseButton : Button
    private lateinit var charEntry : EditText
    private lateinit var phraseEntry : EditText

    private lateinit var list : ArrayList<String>
    private lateinit var noRepeat : ArrayList<String>
    private lateinit var phrase : String
    private var countGussiesPhrase = 10
    private var countGussiesChar = 10
    private lateinit var stars : CharArray
    private lateinit var phraseString : String


    private fun show(str:ArrayList<String>,check:Boolean){

        myRV.adapter = RecyclerViewAdapter(str,check)
        myRV.layoutManager = LinearLayoutManager(this)
        if(str.size!=0)
            myRV.smoothScrollToPosition(str.size - 1)

    }


   private fun saveNewChar(){
       phraseString="Phrase: "
       for(i in stars){
           phraseString += i
       }
       phraseView.text=phraseString
   }


    private fun playAgain(){

        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setMessage("The Correct Phrase was $phrase \nWould You Like To Play Again:")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ -> this.recreate() }
        //.setNegativeButton("No"){dialog,_ -> dialog.cancel()}

        val alert=dialogBuilder.create()
        alert.setTitle("Game Over!!")
        alert.show()

    }


    private fun killButton(button:Button,edit:EditText){
        button.isEnabled = false
        button.isClickable = false
        edit.isEnabled = false
        edit.isClickable = false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        phraseView = findViewById(R.id.phraseHide)
        note = findViewById(R.id.mainL)
        charButton = findViewById(R.id.CharButton)
        phraseButton = findViewById(R.id.PhraseButton)
        charEntry = findViewById(R.id.CharEntry)
        phraseEntry = findViewById(R.id.PhraseEntry)
        myRV = findViewById(R.id.rvMain)

        val phraseList= listOf(
            "UP IN THE AIR",
            "KILL TWO BIRDS WITH ONE STONE",
            "PIECE OF CAKE",
            "BREAK A LEG"
        )

        phrase=phraseList[Random.nextInt(phraseList.size)]
        stars=phrase.toCharArray()
        phraseString="Phrase: "
        for(i in stars){
            if(i != ' ') {
                stars[stars.indexOf(i)] = '*'
                phraseString+='*'
            }
            else {
                stars[stars.indexOf(i)] = ' '
                phraseString+=' '
            }
        }

        if(savedInstanceState!=null){
            countGussiesChar= savedInstanceState.getInt("countGussiesChar", 0)
            countGussiesPhrase= savedInstanceState.getInt("countGussiesPhrase", 0)
            list = savedInstanceState.getStringArrayList("RecycleView")!!
            noRepeat = savedInstanceState.getStringArrayList("NoR")!!
            phrase = savedInstanceState.getString("answer")!!
            stars = savedInstanceState.getCharArray("phraseView")!!
            phraseString = savedInstanceState.getString("pString")!!
        }
        else {
            list = arrayListOf()
            noRepeat = arrayListOf()
        }


        if(countGussiesChar<=0 && countGussiesPhrase<=0||!stars.contains('*')){
            countGussiesChar=10
            countGussiesPhrase=10
            list= arrayListOf()
            noRepeat = arrayListOf()
            phrase=phraseList[Random.nextInt(phraseList.size)]
            stars=phrase.toCharArray()
            phraseString="Phrase: "
            for(i in stars){
                if(i != ' ') {
                    stars[stars.indexOf(i)] = '*'
                    phraseString+='*'
                }
                else {
                    stars[stars.indexOf(i)] = ' '
                    phraseString+=' '
                }
            }
        }
        else
            show(list,true)

        phraseView.text=phraseString

        charButton.setOnClickListener {
           if(charEntry.text.isNotBlank()){
               if (charEntry.text.toString().uppercase() !in noRepeat) {
                   var index=0
                   for(i in phrase) {
                       if (i.toString() == charEntry.text.toString().uppercase())
                           stars[index] = i
                       index++
                   }

                   saveNewChar()
                   if(phrase.contains(charEntry.text.toString().uppercase())) {
                       list.add("You Guessed ${charEntry.text.toString().uppercase()} Correct")
                       show(list,true)
                   }
                   else{
                       countGussiesChar--
                       list.add("You Guessed ${charEntry.text.toString().uppercase()} Wrong\n" +
                               "$countGussiesChar Character Guesses Remaining")
                       show(list,false)

                   }
                   noRepeat.add(charEntry.text.toString().uppercase())
                   charEntry.text=null
               }
               else
               {
                   Snackbar.make(note, "Please Don't Repeat the Litter", Snackbar.LENGTH_LONG).show()
                   charEntry.text=null
               }
           }
            else {
                Snackbar.make(note, "Please Enter Valid Value", Snackbar.LENGTH_LONG).show()
                charEntry.text=null
            }
            if(countGussiesChar<=0&&countGussiesPhrase<=0||!stars.contains('*'))
                playAgain()
            if(countGussiesChar<=0)
                killButton(charButton,charEntry)
        }

        phraseButton.setOnClickListener {
            if(phraseEntry.text.isNotBlank()){
                var index=0
                if (phrase == phraseEntry.text.toString().uppercase()) {
                    list.add("You Guessed ${phraseEntry.text.toString().uppercase()} Correct")
                    show(list, true)
                    for(i in phrase)
                        stars[index++]=i
                }
                else{
                    countGussiesPhrase--
                    list.add("You Guessed ${phraseEntry.text.toString().uppercase()} Wrong\n" +
                            "$countGussiesPhrase Phrase Guesses Remaining")
                    show(list,false)

                }
                phraseEntry.text=null
            }
            else {
                Snackbar.make(note, "Please Enter Valid Value", Snackbar.LENGTH_LONG).show()
                phraseEntry.text=null
            }
            if(countGussiesChar<=0&&countGussiesPhrase<=0||!stars.contains('*'))
                playAgain()
            if(countGussiesPhrase<=0)
                killButton(phraseButton,phraseEntry)
        }

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("countGussiesChar",countGussiesChar)
        outState.putInt("countGussiesPhrase",countGussiesPhrase)
        outState.putStringArrayList("RecycleView",list)
        outState.putStringArrayList("NoR",noRepeat)
        outState.putString("answer",phrase)
        outState.putCharArray("phraseView",stars)
        outState.putString("pString",phraseString)
    }
}