package com.sitapuramargram.quizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.sitapuramargram.quizapp.databinding.ActivityMainBinding
import com.sitapuramargram.quizapp.databinding.ActivityQuizQuestionsBinding

class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var activityQuizQuestionsBinding: ActivityQuizQuestionsBinding
    private var mCurrentPosition: Int =1
    private lateinit var mQuestionList: ArrayList<Question>
    private var mSelectedOptionPosition: Int = 0
    private var mCorrectAnswer: Int = 0
    private var mUserName:String?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_quiz_questions)
        activityQuizQuestionsBinding = ActivityQuizQuestionsBinding.inflate(layoutInflater)
        var view: View = activityQuizQuestionsBinding.root
        setContentView(view)

        mUserName = intent.getStringExtra(Constants.USER_NAME)

        mQuestionList = Constants.getQuestions()
        activityQuizQuestionsBinding.progressBar.max = mQuestionList.size
        setQuestion()

        activityQuizQuestionsBinding.tvOptionOne.setOnClickListener(this)
        activityQuizQuestionsBinding.tvOptionTwo.setOnClickListener(this)
        activityQuizQuestionsBinding.tvOptionThree.setOnClickListener(this)
        activityQuizQuestionsBinding.tvOptionFour.setOnClickListener(this)
        activityQuizQuestionsBinding.btnSubmit.setOnClickListener(this)


    }

    private fun setQuestion(){
        val question = mQuestionList.get(mCurrentPosition-1)

        defaultOptionsView()

        if(mCurrentPosition == mQuestionList.size){
            activityQuizQuestionsBinding.btnSubmit.text = "FINISH"
        }
        else{
            activityQuizQuestionsBinding.btnSubmit.text = "SUBMIT"
        }

        activityQuizQuestionsBinding.progressBar.progress = mCurrentPosition
        activityQuizQuestionsBinding.tvProgress.text = "$mCurrentPosition/${activityQuizQuestionsBinding.progressBar.max}"

        activityQuizQuestionsBinding.tvQuestion.text = question.question
        activityQuizQuestionsBinding.ivImage.setImageResource(question.image)
        activityQuizQuestionsBinding.tvOptionOne.text = question.optionOne
        activityQuizQuestionsBinding.tvOptionTwo.text = question.optionTwo
        activityQuizQuestionsBinding.tvOptionThree.text = question.optionThree
        activityQuizQuestionsBinding.tvOptionFour.text = question.optionFour

    }

    private fun defaultOptionsView(){

        val options = ArrayList<TextView>()
        options.add(0,activityQuizQuestionsBinding.tvOptionOne)
        options.add(1,activityQuizQuestionsBinding.tvOptionTwo)
        options.add(2,activityQuizQuestionsBinding.tvOptionThree)
        options.add(3,activityQuizQuestionsBinding.tvOptionFour)

        for( option in options){

            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(this,R.drawable.default_option_border_bg)

        }

    }

    private fun selectedOptionsView(tv: TextView, selectedOptionNum: Int){

        defaultOptionsView()
        mSelectedOptionPosition = selectedOptionNum

        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface,Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(this,R.drawable.selected_option_border_bg)


    }
    private fun answerView(answer: Int, drwableView: Int){

        when(answer){

            1 -> activityQuizQuestionsBinding.tvOptionOne.background = ContextCompat.getDrawable(this,drwableView)
            2 -> activityQuizQuestionsBinding.tvOptionTwo.background = ContextCompat.getDrawable(this,drwableView)
            3 -> activityQuizQuestionsBinding.tvOptionThree.background = ContextCompat.getDrawable(this,drwableView)
            4 -> activityQuizQuestionsBinding.tvOptionFour.background = ContextCompat.getDrawable(this,drwableView)

        }

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_option_one -> selectedOptionsView(activityQuizQuestionsBinding.tvOptionOne,1)
            R.id.tv_option_two -> selectedOptionsView(activityQuizQuestionsBinding.tvOptionTwo,2)
            R.id.tv_option_three -> selectedOptionsView(activityQuizQuestionsBinding.tvOptionThree,3)
            R.id.tv_option_four -> selectedOptionsView(activityQuizQuestionsBinding.tvOptionFour,4)
            R.id.btn_submit -> {
                if(mSelectedOptionPosition==0) {
                    mCurrentPosition++

                    when{
                        mCurrentPosition<=mQuestionList.size ->{
                            setQuestion()
                        }else ->{
                        val intent = Intent(this,ResultActivity::class.java)
                        intent.putExtra(Constants.USER_NAME,mUserName)
                        intent.putExtra(Constants.CORRECT_ANSWER,mCorrectAnswer)
                        intent.putExtra(Constants.TOTAL_QUESTIONS,mQuestionList.size)
                        startActivity(intent)
                        }
                    }

                } else{

                    val question = mQuestionList.get(mCurrentPosition-1)
                    if(question.correctAnswer != mSelectedOptionPosition){
                        answerView(mSelectedOptionPosition,R.drawable.wrong_option_border_bg)
                    }
                    else{
                        mCorrectAnswer++
                    }
                    answerView(question.correctAnswer,R.drawable.correct_option_border_bg)

                    if(mCurrentPosition==mQuestionList.size){
                        activityQuizQuestionsBinding.btnSubmit.text = "FINISH"
                    }
                    else{
                        activityQuizQuestionsBinding.btnSubmit.text = "GO TO NEXT QUESTION"
                    }
                    mSelectedOptionPosition =0



                }

            }

        }
    }
}