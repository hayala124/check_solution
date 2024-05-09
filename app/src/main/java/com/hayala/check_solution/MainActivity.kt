package com.hayala.check_solution

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import com.hayala.check_solution.databinding.ActivityMainBinding
import java.io.Serializable
import java.text.DecimalFormat
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var state: State
    private var second = ArrayList<Int>()
    private var probability = true
    private val decFormat = DecimalFormat("#.##")
    private var startTime: Long = 0
    private var isRunning = false
    private val handler = Handler(Looper.getMainLooper())
    private var right_answer = true
    private var wrong_answer = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        binding.btnStart.setOnClickListener { onButtonStartPressed() }
        binding.btnRight.setOnClickListener { onButtonRightPressed() }
        binding.btnWrong.setOnClickListener { onButtonWrongPressed() }

        state = if (savedInstanceState == null) {
            State(
                countOfSolvedExamples = 0,
                countRightAnswers = 0,
                countWrongAnswers = 0,
                amountTimeAfterReceivingExample = 0,
                minimumTime = 0,
                maximumTime = 0,
                averageTime = 0.00,
                percentageOfRightAnswers = 0.00,
                firstOperand = 0,
                secondOperand = 0,
                operation = '+',
                //result_1 = 0,
                result_2 = 0.00,
                enabledOfButtonRight = false,
                enabledOfButtonWrong = false,
                enabledOfButtonStart = true
            )
        }
        else {
            savedInstanceState.getSerializable(KEY_STATE) as State
        }
        setState()
    }

    private fun setState() = with(binding) {
        txtAllExamples.setText(state.countOfSolvedExamples.toString())
        txtNumberRight.setText(state.countRightAnswers.toString())
        txtNumberWrong.setText(state.countWrongAnswers.toString())
        txtTimeNow.setText(state.amountTimeAfterReceivingExample.toString())
        txtTimeMin.setText(state.minimumTime.toString())
        txtTimeMax.setText(state.maximumTime.toString())
        txtTimeAverage.setText(String.format("%.2f", state.averageTime))
        txtPercentageCorrectAnswers.setText(String.format("%.2f%%", state.percentageOfRightAnswers))
        txtFirstOperand.setText(state.firstOperand.toString())
        txtSecondOperand.setText(state.secondOperand.toString())
        txtOperation.setText(state.operation.toString())
       // txtResult.setText(state.result_1.toString())
        txtResult.setText(String.format("%.2f", state.result_2))
        btnRight.isEnabled = if (state.enabledOfButtonRight) true else false
        btnWrong.isEnabled = if (state.enabledOfButtonWrong) true else false
        btnStart.isEnabled = if (state.enabledOfButtonStart) true else false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(KEY_STATE, state)
    }
    class State (
        var countOfSolvedExamples: Int,
        var countRightAnswers: Int,
        var countWrongAnswers: Int,
        var amountTimeAfterReceivingExample: Int,
        var minimumTime: Int,
        var maximumTime: Int,
        var averageTime: Double,
        var percentageOfRightAnswers: Double,
        var firstOperand: Int,
        var secondOperand: Int,
        var operation: Char,
       // var result_1: Int,
        var result_2: Double,
        var enabledOfButtonRight: Boolean,
        var enabledOfButtonWrong: Boolean,
        var enabledOfButtonStart: Boolean
    ): Serializable

    companion object {
        @JvmStatic private val KEY_STATE = "STATE"
    }

    private fun startTimer() {
        if (!isRunning) {
            startTime = System.currentTimeMillis()
            isRunning = true
            updateTimerDisplay()
        }
        setState()
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
            state.amountTimeAfterReceivingExample = seconds
            handler.postDelayed({ updateTimerDisplay() }, 1000)
            setState()
        }
    }

    private fun onButtonStartPressed() {
        state.firstOperand = Random.nextInt(10, 100)
        state.secondOperand = Random.nextInt(10, 100)
        state.operation = listOf('*', '/', '-', '+').random()

        probability = Random.nextBoolean();
        if (probability) {
            state.result_2 = when(state.operation) {
                '+' -> (state.firstOperand + state.secondOperand).toDouble()
                '-' -> (state.firstOperand - state.secondOperand).toDouble()
                '*' -> (state.firstOperand * state.secondOperand).toDouble()
                else ->  (String.format("%.2f", (state.firstOperand.toDouble() / state.secondOperand.toDouble()))).toDouble()
            }
        } else {
            val result_1 = Random.nextInt(-99, 9801)
            state.result_2 = result_1.toDouble()
        }
        startTimer()

        state.enabledOfButtonStart = !state.enabledOfButtonStart
        state.enabledOfButtonRight = !state.enabledOfButtonRight
        state.enabledOfButtonWrong = !state.enabledOfButtonWrong
        setState()
    }

    private fun onButtonRightPressed() {
        checkedExamles()
        checkCorrectnessOfExample()
        if (right_answer == true) {
            state.countRightAnswers++
        } else {
            state.countWrongAnswers++
        }
        changesAfterAnswer()
        setState()
    }

    private fun onButtonWrongPressed() {
        checkedExamles()
        checkCorrectnessOfExample()
        if (wrong_answer == true) {
            state.countRightAnswers++
        } else {
            state.countWrongAnswers++
        }
        changesAfterAnswer()
        setState()
    }

    private fun changesAfterAnswer() {
        state.percentageOfRightAnswers = state.countRightAnswers * 100 / state.countOfSolvedExamples.toDouble()
        stopTimer()
        getTime()
    }
    private fun getTime() {
        second.add(state.amountTimeAfterReceivingExample)

        state.maximumTime = second.maxOrNull().toString().toInt()
        state.minimumTime = second.minOrNull().toString().toInt()
        state.averageTime = second.average().toString().toDouble()
        /*max_time = second.maxOrNull().toString().toInt()
        min_time = second.minOrNull().toString().toInt()
        average_time = second.average().toString().toDouble()

        textMinTime.text = min_time.toString()
        textMaxTime.text = max_time.toString()
        textAverageTime.text = String.format("%.2f", average_time)*/
    }

    private fun checkedExamles() {
        state.countOfSolvedExamples++
    }

    private fun checkCorrectnessOfExample() {
        if ((state.operation == '+' && state.result_2 == (state.firstOperand + state.secondOperand).toDouble()) ||
            (state.operation == '-' && state.result_2 == (state.firstOperand - state.secondOperand).toDouble()) ||
            (state.operation == '*' && state.result_2 == (state.firstOperand * state.secondOperand).toDouble()) ||
            (state.operation == '/' && state.result_2 == (String.format("%.2f", state.firstOperand.toDouble() / state.secondOperand.toDouble())).toDouble())
        ) {
            right_answer = true
            wrong_answer = false
        } else {
            right_answer = false
            wrong_answer = true
        }
        state.enabledOfButtonRight = !state.enabledOfButtonRight
        state.enabledOfButtonWrong = !state.enabledOfButtonWrong
        state.enabledOfButtonStart = !state.enabledOfButtonStart
    }
}