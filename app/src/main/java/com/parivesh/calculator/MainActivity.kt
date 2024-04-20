package com.parivesh.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity : AppCompatActivity() {
    private lateinit var resultTextView: TextView
    private lateinit var faqButton: Button
    private var firstNumber: Long = 0L
    private var operator = ""
    private var currentResult: Long = 0L
    private var isEqualTapped = false
    private val maxSupportedNumber: Long = Long.MAX_VALUE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        resultTextView = findViewById(R.id.textView)
        faqButton = findViewById(R.id.about)

        faqButton.setOnClickListener {
            onAboutButtonClicked()
        }
    }

    private fun onAboutButtonClicked() {
        val dialog = BottomSheetDialog(this)
        val bottomView = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
        dialog.setContentView(bottomView)
        dialog.show()
        bottomView.findViewById<ImageButton>(R.id.closeButton).setOnClickListener {
            dialog.dismiss()
        }
    }

    fun onClearButtonClick(view: View) {
        firstNumber = 0L
        operator = ""
        currentResult = 0L
        resultTextView.text = "0"
        isEqualTapped = false
    }

    fun onDeleteButtonClick(view: View) {
        firstNumber /= 10L
        resultTextView.text = firstNumber.toString()
        isEqualTapped = false
    }

    private fun performOperation() {
        when (operator) {
            "+" -> {
                currentResult += firstNumber
            }

            "-" -> {
                currentResult -= firstNumber
            }

            "*" -> {
                currentResult *= firstNumber
            }

            "/" -> {
                currentResult /= firstNumber
            }

            "" -> currentResult = firstNumber
        }
    }

    private fun toastLargeNumberError() {
        Toast.makeText(this, "Can't perform $maxSupportedNumber", Toast.LENGTH_LONG).show()
    }

    fun onEqualButtonClicked(view: View) {
        // Perform the final operation
        if (operator == "/" && firstNumber == 0L) {
            Toast.makeText(this, "divident can't be zero", Toast.LENGTH_LONG).show()
            operator = ""
            return
        }
        performOperation()
        firstNumber = currentResult
        // Reset the operator
        operator = ""

        // Update the TextView
        resultTextView.text = currentResult.toString()
        isEqualTapped = true
    }

    fun onOperatorButtonClick(view: View) {
        // Perform the previous operation before updating the operator
        performOperation()
        // Reset the firstNumber and update the operator
        firstNumber = 0
        if (view is Button) {
            operator = view.text.toString()
        }
        // Update the TextView
        resultTextView.text = currentResult.toString()
        isEqualTapped = false
    }

    fun onNumberButtonClick(view: View) {

        if (isEqualTapped) {
            firstNumber = 0
            isEqualTapped = false
        }
        if (view is Button) {
            val number = view.text.toString().toIntOrNull() ?: 0
            // Update the firstNumber based on the tapped number
            if (firstNumber <= (maxSupportedNumber - number) / 10) {
                firstNumber = firstNumber * 10 + number
                // Update the TextView
                resultTextView.text = firstNumber.toString()
            } else {
                toastLargeNumberError()
            }
        }
    }
}