package com.hayala.check_solution

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import com.hayala.check_solution.databinding.ActivityMainBinding
import java.text.DecimalFormat
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var buttonStart: Button
    private lateinit var buttonRight: Button
    private lateinit var buttonWrong: Button
    private lateinit var textResult: TextView
    private lateinit var txtRightAnswer: TextView
    private lateinit var txtWrongAnswer: TextView
    private lateinit var textMinTime: TextView
    private lateinit var textMaxTime: TextView
    private lateinit var textAverageTime: TextView
    private var number_1 = 0
    private var time = 0
    private var min_time = 0
    private var max_time = 0
    private var average_time = 0
    private var number_2 = 0
    private var percent = 0.00
    private var right = 0
    private var wrong = 0
    private var right_answer = true
    private var wrong_answer = false
    private var operation = ' '
    private var probability = true
    private var result = 0
    private val decFormat = DecimalFormat("#.##")
    private var numberOfCheckedExample = 0

    private var startTime: Long = 0
    private var isRunning = false
    private val handler = Handler(Looper.getMainLooper())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonRight = binding.btnRight
        buttonWrong = binding.btnWrong
        buttonStart = binding.btnStart
        textResult = binding.txtResult
        txtRightAnswer = binding.txtNumberRight
        txtWrongAnswer = binding.txtNumberWrong
        textMinTime = binding.txtTimeMin
        textMaxTime = binding.txtTimeMax
        textAverageTime = binding.txtTimeAverage

        buttonStart.setOnClickListener { onButtonStartPressed() }
        buttonRight.setOnClickListener { onButtonRightPressed() }
        buttonWrong.setOnClickListener { onButtonWrongPressed() }
    }

    private fun startTimer() {
        if (!isRunning) {
            startTime = System.currentTimeMillis()
            isRunning = true
            buttonStart.isEnabled = false
            buttonWrong.isEnabled = true
            buttonRight.isEnabled = true
            updateTimerDisplay()
        }
    }

    private fun stopTimer() {
        if (isRunning) {
            isRunning = false
        }
    }

    private fun updateTimerDisplay() {
        if (isRunning) {
            val elapsedTime = System.currentTimeMillis() - startTime
            val seconds = (elapsedTime / 1000).toInt()
            textMinTime.text = "$seconds"
            handler.postDelayed({ updateTimerDisplay() }, 1000)
        }
    }

    private fun onButtonStartPressed() {
        number_1 = Random.nextInt(10, 100)
        number_2 = Random.nextInt(10, 100)
        operation = listOf('*', '/', '-', '+').random()
        // editValue.setText("")
        binding.txtOperation.text = operation.toString()
        binding.txtFirstOperand.text = number_1.toString()
        binding.txtSecondOperand.text = number_2.toString()

        probability = Random.nextBoolean();
        if (probability) {
            when (operation) {
                '+' -> textResult.text = (number_1 + number_2).toString()
                '-' -> textResult.text = (number_1 - number_2).toString()
                '*' -> textResult.text = (number_1 * number_2).toString()
                '/' -> {
                    if (number_1 % number_2 == 0)
                        textResult.text = (number_1 / number_2).toString()
                    else if (number_1 % number_2 != 0) {
                        textResult.text =
                            decFormat.format(number_1.toDouble() / number_2.toDouble())
                    }
                }
            }
        } else {
            result = Random.nextInt(-99, 9801)
            textResult.text = result.toString()
        }

        buttonStart.isEnabled = false
        buttonRight.isEnabled = true
        buttonWrong.isEnabled = true
        startTimer()
    }


    private fun onButtonRightPressed() {
        checkedExamles()
        check()
        if (right_answer == true) {
            changesForRightAnswer()
        } else {
            changesForWrongAnswer()
        }
        calculatePercentageRightAnswers()
        stopTimer()
    }

    private fun onButtonWrongPressed() {
        checkedExamles()
        check()
        if (wrong_answer == true) {
            changesForRightAnswer()
        } else {
            changesForWrongAnswer()
        }
        calculatePercentageRightAnswers()
        stopTimer()
    }

    private fun calculatePercentageRightAnswers() {
        percent = right * 100 / numberOfCheckedExample.toDouble()
        binding.txtPercentageCorrectAnswers.text = String.format("%.2f%%", percent)
    }

    private fun checkedExamles() {
        numberOfCheckedExample++
        binding.txtAllExamples.text = numberOfCheckedExample.toString()
    }

    private fun changesForRightAnswer() {
        right++
        txtRightAnswer.text = right.toString()
    }

    private fun changesForWrongAnswer() {
        wrong++
        txtWrongAnswer.text = wrong.toString()
    }

    private fun check() {
        if ((operation == '+' && textResult.text == (number_1 + number_2).toString()) ||
            (operation == '-' && textResult.text == (number_1 - number_2).toString()) ||
            (operation == '*' && textResult.text == (number_1 * number_2).toString()) ||
            (operation == '/' && ((number_1 % number_2 == 0 && textResult.text == (number_1 / number_2).toString()) ||
                    (number_1 % number_2 != 0 && textResult.text == decFormat.format(number_1.toDouble() / number_2.toDouble()))))
        ) {
            right_answer = true
            wrong_answer = false
        } else {
            right_answer = false
            wrong_answer = true
        }
        buttonRight.isEnabled = false
        buttonWrong.isEnabled = false
        buttonStart.isEnabled = true
    }
}