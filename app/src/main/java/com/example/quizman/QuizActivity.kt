package com.example.quizman

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.quizman.databinding.ActivityQuizBinding
import com.example.quizman.databinding.ScoreDialogBinding
import com.example.quizman.databinding.TimeDialogBinding

class QuizActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        var questionModelList: List<QuestionModel> = listOf()
        var time: String = " "
    }

    lateinit var binding: ActivityQuizBinding

    var currentQuestionIndex = 0
    var selectedAnswer = ""
    var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuizBinding.inflate(layoutInflater)
        binding.apply {
            btn0.setOnClickListener(this@QuizActivity)
            btn1.setOnClickListener(this@QuizActivity)
            btn2.setOnClickListener(this@QuizActivity)
            btn3.setOnClickListener(this@QuizActivity)
            nextbtn.setOnClickListener(this@QuizActivity)
        }
        setContentView(binding.root)
        loadQuestions()
        startTimer()
    }

    private fun startTimer() {
        val totalTimeMillis = time.trim().toInt() * 60 * 1000L
        object : CountDownTimer(totalTimeMillis, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                val minutes = seconds / 60
                val remainingSeconds = seconds % 60
                binding.questionTimer.text = String.format("%02d:%02d", minutes, remainingSeconds)
            }



            override fun onFinish() {


                val dialogBinding2 = TimeDialogBinding.inflate(layoutInflater)

                dialogBinding2.apply {
                    returnBtn.setOnClickListener{
                        finishQuiz()
                    }
                }

                AlertDialog.Builder(this@QuizActivity)
                    .setView(dialogBinding2.root)
                    .setCancelable(false)
                    .show()
            }
        }.start()
    }

    private fun loadQuestions() {
        selectedAnswer = ""

        if (currentQuestionIndex == questionModelList.size) {
            finishQuiz()
            return
        }

        binding.apply {
            questionIndi.text = "Question ${currentQuestionIndex + 1} / ${questionModelList.size}"
            questionProgress.progress =
                ((currentQuestionIndex.toFloat() / questionModelList.size.toFloat()) * 100).toInt()

            questionText.text = questionModelList[currentQuestionIndex].question
            btn0.text = questionModelList[currentQuestionIndex].options[0]
            btn1.text = questionModelList[currentQuestionIndex].options[1]
            btn2.text = questionModelList[currentQuestionIndex].options[2]
            btn3.text = questionModelList[currentQuestionIndex].options[3]

            btn0.setBackgroundColor(getColor(R.color.gray))
            btn1.setBackgroundColor(getColor(R.color.gray))
            btn2.setBackgroundColor(getColor(R.color.gray))
            btn3.setBackgroundColor(getColor(R.color.gray))
        }
    }

    override fun onClick(view: View?) {
        val clickedBtn = view as Button

        if (clickedBtn.id == R.id.nextbtn) {
            if (selectedAnswer.isEmpty()) {
                Toast.makeText(applicationContext, "Please select an answer to continue", Toast.LENGTH_SHORT).show()
                return
            }

            if (selectedAnswer == questionModelList[currentQuestionIndex].correct) {
                score++
            }

            currentQuestionIndex++
            loadQuestions()
        } else {
            binding.apply {
                btn0.setBackgroundColor(getColor(R.color.gray))
                btn1.setBackgroundColor(getColor(R.color.gray))
                btn2.setBackgroundColor(getColor(R.color.gray))
                btn3.setBackgroundColor(getColor(R.color.gray))
            }

            selectedAnswer = clickedBtn.text.toString()
            clickedBtn.setBackgroundColor(getColor(R.color.orange))
        }
    }

    private fun finishQuiz() {
        val totalQuestion = questionModelList.size
        val percentage = ((score.toFloat() / totalQuestion.toFloat()) * 100).toInt()
        val dialogBinding = ScoreDialogBinding.inflate(layoutInflater)
        dialogBinding.apply {
            scoreProgress.progress = percentage
            scoreText.text = "$percentage %"
            if (percentage > 50 || percentage == 50) {
                scoreTitle.text = "Congrats! You have Passed"
                scoreTitle.setTextColor(Color.BLUE)
            } else {
                scoreTitle.text = "Oops! You have Failed"
                scoreTitle.setTextColor(Color.RED)
            }
            scoreSubtitle.text = "$score out of $totalQuestion are correct"

            finisBtn.setOnClickListener {
                finish()
            }
        }

        AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(false)
            .show()
    }
}
