package mx.itson.edu.examenu1

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    var grades: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Buttons
        val btnC: Button = findViewById(R.id.btnGC)
        val btnF: Button = findViewById(R.id.btnGF)
        val btnK: Button = findViewById(R.id.btnGK)
        val btnBorrar: Button = findViewById(R.id.btnReset)
        btnC.tag = "btnC"
        btnF.tag = "btnF"
        btnK.tag = "btnK"

        // Edit Texts
        val txtC: EditText = findViewById(R.id.etC)
        val txtF: EditText = findViewById(R.id.etF)
        val txtK: EditText = findViewById(R.id.etK)
        txtC.tag = "txtC"
        txtF.tag = "txtF"
        txtK.tag = "txtK"

        setTextWatcher(txtC, arrayOf(txtF,txtK))
        setTextWatcher(txtF, arrayOf(txtC,txtK))
        setTextWatcher(txtK, arrayOf(txtC,txtF))

        val hideKeyboard = {
            val view = this.currentFocus
            if (view != null) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
        val arrayTextsViews = arrayOf(txtC,txtF,txtK)
        btnC.setOnClickListener{
            if(determinateNumberOfTxtUsed(arrayTextsViews) <= 2){
                txtC.setText(calculatorGrades(getGrades(arrayTextsViews), determinateConversion(arrayTextsViews,btnC)))
            } else {
                Toast.makeText(applicationContext, "Presione el botón borrar para hacer otros calculos", Toast.LENGTH_LONG).show()
            }
            hideKeyboard()
        }

        btnF.setOnClickListener{
            if(determinateNumberOfTxtUsed(arrayTextsViews) <= 2){
                txtF.setText(calculatorGrades(getGrades(arrayTextsViews), determinateConversion(arrayTextsViews,btnF)))
            } else {
                Toast.makeText(applicationContext, "Presione el botón borrar para hacer otros calculos", Toast.LENGTH_LONG).show()
            }
            hideKeyboard()
        }

        btnK.setOnClickListener {
            if(determinateNumberOfTxtUsed(arrayTextsViews) <= 2){
                txtK.setText(calculatorGrades(getGrades(arrayTextsViews), determinateConversion(arrayTextsViews,btnK)))
            } else {
                Toast.makeText(applicationContext, "Presione el botón borrar para hacer otros calculos", Toast.LENGTH_LONG).show()
            }
            hideKeyboard()
        }

        btnBorrar.setOnClickListener{
            txtF.isEnabled = true
            txtC.isEnabled = true
            txtK.isEnabled = true

            txtF.text.clear()
            txtC.text.clear()
            txtK.text.clear()
        }
    }

    private fun determinateNumberOfTxtUsed(textsViews: Array<EditText>): Int{
        var count = 0
        textsViews.forEach {
            if(it.text.toString() != "") count++
        }
        return count
    }

    private fun getGrades(textsViews: Array<EditText>):Double {
        textsViews.forEach {
            if(it.text.toString() != "") return it.text.toString().toDouble()
        }
        return 0.0
    }

    private fun determinateConversion(textsViews: Array<EditText>, btnPressed: Button): String {

        textsViews.forEach {
            if (it.text.toString() != "") {
                return when {
                    it.tag == "txtC" && btnPressed.tag == "btnF" -> "C_to_F"
                    it.tag == "txtC" && btnPressed.tag == "btnK" -> "C_to_K"
                    it.tag == "txtF" && btnPressed.tag == "btnC" ->  "F_to_C"
                    it.tag == "txtF" && btnPressed.tag == "btnF" -> "C_to_K"
                    it.tag == "txtK" && btnPressed.tag == "btnC" -> "K_to_C"
                    it.tag == "txtK" && btnPressed.tag == "btnF" -> "K_to_F"
                    else -> ""
                }
            }
        }
        return ""
    }

    private fun setTextWatcher(editText: EditText, actionTextViews: Array<TextView>) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Acción a realizar después de que se haya producido un cambio en el texto del EditText
                if (editText.text.toString() != "") {
                    actionTextViews.forEach {
                        it.isEnabled = false
                    }
                } else {
                    actionTextViews.forEach {
                        it.isEnabled = true
                    }
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Acción a realizar antes de que se produzca un cambio en el texto del EditText

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    private fun calculatorGrades(grades: Double, conversion: String): String? {
        val decimalFormat = DecimalFormat("#.##")
        return when (conversion) {
            // Celsius a Fahrenheit
            "C_to_F" -> decimalFormat.format((grades * 1.8 + 32))
            // Celsius a Kelvin
            "C_to_K" -> decimalFormat.format((grades + 273.15))
            // Fahrenheit a Celsius
            "F_to_C" -> decimalFormat.format((grades - 32) / 1.8)
            // Fahrenheit a Kelvin
            "F_to_K" -> decimalFormat.format(((5 * (grades - 32)) / 9) + 273.15)
            // Kelvin a Celsius
            "K_to_C" -> decimalFormat.format(grades - 273.15)
            // Kelvin a Fahrenheit
            "K_to_F" -> decimalFormat.format(((9 * (grades - 273.15)) / 5) + 32)
            else -> ""
        }
    }
}