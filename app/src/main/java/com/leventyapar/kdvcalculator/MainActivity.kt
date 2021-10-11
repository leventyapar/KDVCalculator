package com.leventyapar.kdvcalculator

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.leventyapar.kdvcalculator.databinding.ActivityMainBinding
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.math.round
import kotlin.math.roundToLong

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.calculateButton.setOnClickListener{
            calculateKDV()
        }
        binding.KDVOptionsRadioGroup.setOnCheckedChangeListener { radioGroup, i ->
            if(i == R.id.anotherKDVrate_radio_button){
                binding.anotherKDVrateTextView.visibility = View.VISIBLE
            }
            else{
                binding.anotherKDVrateTextView.visibility = View.INVISIBLE
            }
        }
            
        
    }

    fun calculateKDV(){
        val amountString = binding.amount.text.toString()
        val amount = amountString.toDoubleOrNull()

        if(amount == null){
            Toast.makeText(applicationContext, "Lütfen Bir Tutar Giriniz", Toast.LENGTH_LONG).show()
            binding.result1TextView.text = ""
            binding.result2TextView.text = ""
            return
        }
        if(binding.KDVOptionsRadioGroup.checkedRadioButtonId == R.id.anotherKDVrate_radio_button
            && binding.anotherKDVrateTextView.text.isEmpty()){
            binding.result1TextView.text = ""
            binding.result2TextView.text = ""
            Toast.makeText(this,"Lütfen Bir KDV Oranı Giriniz",Toast.LENGTH_LONG).show()
            return
        }

        val selectedRadioButtonId = binding.KDVOptionsRadioGroup.checkedRadioButtonId
        val KDVrate = when(selectedRadioButtonId){
            R.id.one_percent_radio_button -> 0.01
            R.id.eight_percent_radio_button -> 0.08
            R.id.eighteen_percent_radio_button -> 0.18
            R.id.anotherKDVrate_radio_button ->  binding.anotherKDVrateTextView.text.toString().toDouble() / 100
            else -> 0.00
        }

        if(binding.includeKDVSwitch.isChecked){
            var withoutKDVamount = amount / (1 + KDVrate)
            var amountKDV = withoutKDVamount * KDVrate

            val formattedWithoutKDVamount = NumberFormat.getCurrencyInstance(Locale("tr","TR")).format(withoutKDVamount)
            val formattedAmountKDV = NumberFormat.getCurrencyInstance(Locale("tr","TR")).format(amountKDV)

            binding.result1TextView.text = "KDV Hariç Net Tutar : ${formattedWithoutKDVamount}"
            binding.result2TextView.text = "KDV Tutarı : ${formattedAmountKDV}"

        }
        else{
            var amountKDV  = KDVrate * amount
            var amountWithKDV = amount + amountKDV

            val formattedAmountWithKDV = NumberFormat.getCurrencyInstance(Locale("tr","TR")).format(amountWithKDV)
            val formattedAmountKDV = NumberFormat.getCurrencyInstance(Locale("tr","TR")).format(amountKDV)

            binding.result1TextView.text = "KDV Dahil Tutar : ${formattedAmountWithKDV} "
            binding.result2TextView.text = "KDV Tutarı : ${formattedAmountKDV}"
        }
    }

}


