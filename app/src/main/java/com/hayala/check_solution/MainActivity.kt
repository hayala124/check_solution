package com.hayala.check_solution

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    private var number_1 = 0
    private var number_2 = 0
    private var right = 0
    private var wrong = 0
    private var right_answer = true
    private var wrong_answer = false
    private var operation = ' '
    private var probability = true
    private var result = 0
    private val decFormat = DecimalFormat("#.##")
    private var numberOfCheckedExample = 0


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

        buttonStart.setOnClickListener {
            val (number1, number2, operation) = onButtonStartPressed()
            this.number_1 = number1
            this.number_2 = number2
            this.operation = operation
        }
        buttonRight.setOnClickListener { onButtonRightPressed() }
        buttonWrong.setOnClickListener { onButtonWrongPressed() }
    }

    private fun onButtonStartPressed(): Triple<Int, Int, Char> {
        number_1 = Random.nextInt(10, 100)
        number_2 = Random.nextInt(10, 100)
        operation = listOf('*', '/', '-', '+').random()
        // editValue.setText("")
        // editValue.setBackgroundColor(editBackgroundColor)
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
                        textResult.text = decFormat.format(number_1.toDouble() / number_2.toDouble())
                    }
                }
            }
        }
        else {
            result = Random.nextInt(-99, 9801)
            textResult.text = result.toString()
        }
        buttonStart.isEnabled = false
        buttonRight.isEnabled = true
        buttonWrong.isEnabled = true
        return Triple(number_1, number_2, operation)
    }

    private fun onButtonRightPressed() {
        checkedExamles()
        check()
        if (right_answer == true) {
            changesForRightAnswer()
        }
        else {
            changesForWrongAnswer()
        }
    }

    private fun onButtonWrongPressed() {
        checkedExamles()
        check()
        if (wrong_answer == true) {
            changesForRightAnswer()
        }
        else {
            changesForWrongAnswer()
        }
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
        }
        else {
            right_answer = false
            wrong_answer = true
        }
        buttonRight.isEnabled = false
        buttonWrong.isEnabled = false
        buttonStart.isEnabled = true
    }
}