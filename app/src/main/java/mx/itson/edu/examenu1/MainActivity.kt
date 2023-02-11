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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Buttons
        val btnC: Button = findViewById(R.id.btnGC)
        val btnF: Button = findViewById(R.id.btnGF)
        val btnK: Button = findViewById(R.id.btnGK)
        val btnBorrar: Button = findViewById(R.id.btnReset)

        // Edit Texts
        val txtC: EditText = findViewById(R.id.etC)
        val txtF: EditText = findViewById(R.id.etF)
        val txtK: EditText = findViewById(R.id.etK)


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

        btnC.setOnClickListener{
           calcularGrados(txtC, arrayOf(txtF,txtK))
            hideKeyboard()
        }

        btnF.setOnClickListener{
            calcularGrados(txtF, arrayOf(txtC,txtK))
            hideKeyboard()
        }

        btnK.setOnClickListener {
            calcularGrados(txtK, arrayOf(txtC, txtF))
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
    fun setTextWatcher(editText: EditText, actionTextViews: Array<TextView>) {
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

    fun calcularGrados(editText: EditText, actionTextViews: Array<TextView>){
        var grados: Double = 0.0
        var sucess: Boolean = true
        try{
            grados = editText.text.toString().toDouble()
        }catch(e: Exception){
            actionTextViews[1].text = ""
            actionTextViews[0].text = ""
            sucess = false
            val toast = Toast.makeText(applicationContext, "Seleccione el grado correspondiente al que llenó para llenar los otros 2!", Toast.LENGTH_LONG)
            toast.show()
        }

        if(sucess){
            val decimalFormat = DecimalFormat("#.##")
            when (editText.id) {
                // Para cuando se llene los celsius
                R.id.etC -> {
                    // Calculamos Celsius a Fahrenheit
                    actionTextViews[0].text = decimalFormat.format((grados * 1.8 + 32)).toString()
                    // Calculamos Celsius a Kelvin
                    actionTextViews[1].text = decimalFormat.format((grados + 273.15)).toString()
                }
                // Para cuando se llene los Fahrenheit
                R.id.etF -> {
                    // Calculamos Fahrenheit a Celsius
                    actionTextViews[0].text = decimalFormat.format((grados - 32)/1.8).toString()
                    // Calculamos Fahrenheit a Kelvin
                    actionTextViews[1].text = decimalFormat.format(((5*(grados - 32))/9)+273.15).toString()
                }
                // Para cuando se llene los Kelvin
                R.id.etK -> {
                    // Calculamos Kelvin a Celsius
                    actionTextViews[0].text = decimalFormat.format(grados - 273.15).toString()

                    // Calculamos Kelvin a Fahrenheit
                    actionTextViews[1].text = decimalFormat.format(((9*(grados - 273.15))/5)+32).toString()
                }
            }
        }
    }
}