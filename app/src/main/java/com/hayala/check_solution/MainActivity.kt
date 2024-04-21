package com.hayala.check_solution

import android.annotation.SuppressLint
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
    private var number_1 = 0
    private var number_2 = 0
    private var operation = ' '
    private var probability = true
    private var result = 0
    private val decFormat = DecimalFormat("#.##")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonRight = binding.btnRight
        buttonWrong = binding.btnWrong
        buttonStart = binding.btnStart
        textResult = binding.txtResult

        buttonStart.setOnClickListener {
            val (number1, number2, operation) = onButtonStartPressed()
            this.number_1 = number1
            this.number_2 = number2
            this.operation = operation
        }
        /*buttonRight.setOnClickListener { onButtonRightPressed() }
        buttonWrong.setOnClickListener { onButtonWrongPressed() }*/
    }


    @SuppressLint("SetTextI18n")
    private fun onButtonStartPressed(): Triple<Int, Int, Char> {
        number_1 = Random.nextInt(10, 100)
        number_2 = Random.nextInt(10, 100)
        operation = listOf('*', '/', '-', '+').random()
        // editValue.setText("")
        // editValue.setBackgroundColor(editBackgroundColor)
        binding.txtOperation.text = operation.toString()
        binding.txtFirstOperand.text = number_1.toString()
        binding.txtSecondOperand.text = number_2.toString()
        // buttonStart.isEnabled = false

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
                else -> textResult.text = "Error"
            }
        }
        else {
            result = Random.nextInt(10, 9801)
            textResult.text = result.toString()
        }


        return Triple(number_1, number_2, operation)
    }

    private fun onButtonRightPressed() {

    }

    private fun onButtonWrongPressed() {

    }
}