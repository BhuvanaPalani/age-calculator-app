package se.linerotech.module201.project4

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

class AgeCalculatorActivity : AppCompatActivity() {

    companion object {
        private const val MIN_MONTH = 1
        private const val MAX_MONTH = 12
        private const val MONTH_INDEX_OFFSET = 1

        private const val MILLIS_IN_SECOND = 1000L
        private const val SECONDS_IN_MINUTE = 60L
        private const val MINUTES_IN_HOUR = 60L
        private const val HOURS_IN_DAY = 24L
        private const val MILLIS_PER_DAY = MILLIS_IN_SECOND * SECONDS_IN_MINUTE * MINUTES_IN_HOUR * HOURS_IN_DAY
        private const val DAYS_IN_WEEK = 7.0
        private const val AVG_DAYS_IN_MONTH = 30.44
        private const val AVG_DAYS_IN_YEAR = 365.25

        private const val DECIMAL_PATTERN = "#.##"
    }

    private lateinit var editTextYear: EditText
    private lateinit var editTextMonth: EditText
    private lateinit var textViewYears: TextView
    private lateinit var textViewMonths: TextView
    private lateinit var textViewWeeks: TextView
    private lateinit var textViewDays: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_age_calculator)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        editTextYear = findViewById(R.id.editTextYear)
        editTextMonth = findViewById(R.id.editTextMonth)
        textViewYears = findViewById(R.id.textViewYears)
        textViewMonths = findViewById(R.id.textViewMonths)
        textViewWeeks = findViewById(R.id.textViewWeeks)
        textViewDays = findViewById(R.id.textViewDays)

        editTextYear.doAfterTextChanged { calculateAge() }
        editTextMonth.doAfterTextChanged { calculateAge() }
    }

    private fun calculateAge() {
        val yearInput = editTextYear.text.toString()
        val monthInput = editTextMonth.text.toString()

        val birthYear = yearInput.toIntOrNull()
        val birthMonth = monthInput.toIntOrNull()

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)

        val isValid =
            !yearInput.isBlank() &&
                !monthInput.isBlank() &&
                birthYear != null &&
                birthMonth != null &&
                birthMonth in MIN_MONTH..MAX_MONTH &&
                birthYear <= currentYear

        if (!isValid) {
            clearOutputs()
            return
        }

        val birthDate = Calendar.getInstance().apply {
            set(birthYear!!, birthMonth!! - MONTH_INDEX_OFFSET, 1)
        }
        val now = Calendar.getInstance()
        val differenceInMillis = now.timeInMillis - birthDate.timeInMillis

        if (differenceInMillis < 0) {
            clearOutputs()
            return
        }

        val differenceInDays = differenceInMillis / MILLIS_PER_DAY.toDouble()
        val differenceInWeeks = differenceInDays / DAYS_IN_WEEK
        val differenceInMonths = differenceInDays / AVG_DAYS_IN_MONTH
        val differenceInYears = differenceInDays / AVG_DAYS_IN_YEAR

        val numberFormat = NumberFormat.getNumberInstance(Locale.US) as DecimalFormat
        numberFormat.applyPattern(DECIMAL_PATTERN)

        textViewYears.text = numberFormat.format(differenceInYears)
        textViewMonths.text = numberFormat.format(differenceInMonths)
        textViewWeeks.text = numberFormat.format(differenceInWeeks)
        textViewDays.text = numberFormat.format(differenceInDays)
    }

    private fun clearOutputs() {
        textViewYears.text = ""
        textViewMonths.text = ""
        textViewWeeks.text = ""
        textViewDays.text = ""
    }
}
