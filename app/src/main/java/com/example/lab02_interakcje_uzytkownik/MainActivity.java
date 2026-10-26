package com.example.lab02_interakcje_uzytkownik;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText poleWzrostu;
    private EditText poleWagi;
    private RadioGroup wyborPlci;
    private CheckBox zapiszPomiary;
    private TextView tekstWyniku;
    private Button przyciskOblicz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        poleWzrostu = findViewById(R.id.poleWzrostu);
        poleWagi = findViewById(R.id.poleWagi);
        wyborPlci = findViewById(R.id.wyborPlci);
        zapiszPomiary = findViewById(R.id.zapiszPomiary);
        tekstWyniku = findViewById(R.id.tekstWyniku);
        przyciskOblicz = findViewById(R.id.przyciskOblicz);

        przyciskOblicz.setOnClickListener(v -> obliczBMI());

        wyborPlci.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.opcjaKobieta) {
                Toast.makeText(
                        MainActivity.this,
                        "Wybrano: Kobieta",
                        Toast.LENGTH_SHORT
                ).show();
            } else if (checkedId == R.id.opcjaMezczyzna) {
                Toast.makeText(
                        MainActivity.this,
                        "Wybrano: Mężczyzna",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        zapiszPomiary.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    String komunikat = isChecked
                            ? "Zaznaczono zapisywanie danych"
                            : "Zapisywanie danych wyłączone";

                    Toast.makeText(
                            MainActivity.this,
                            komunikat,
                            Toast.LENGTH_SHORT
                    ).show();
                }
        );
    }

    private void obliczBMI() {
        String heightText = poleWzrostu.getText().toString().trim();
        String weightText = poleWagi.getText().toString().trim();

        if (heightText.isEmpty()) {
            poleWzrostu.setError("Podaj wzrost!");
            poleWzrostu.requestFocus();
            return;
        }

        if (weightText.isEmpty()) {
            poleWagi.setError("Podaj wagę!");
            poleWagi.requestFocus();
            return;
        }

        heightText = heightText.replace(',', '.');
        weightText = weightText.replace(',', '.');

        double heightCm;
        double weightKg;

        try {
            heightCm = Double.parseDouble(heightText);
            weightKg = Double.parseDouble(weightText);
        } catch (NumberFormatException e) {
            Toast.makeText(
                    this,
                    "Wprowadź poprawne liczby.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        if (!Double.isFinite(heightCm)
                || !Double.isFinite(weightKg)
                || heightCm <= 0
                || weightKg <= 0
                || heightCm > 300
                || weightKg > 1000) {
            Toast.makeText(
                    this,
                    "Podaj poprawny wzrost i wagę.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        int genderId = wyborPlci.getCheckedRadioButtonId();

        if (genderId == -1) {
            Toast.makeText(
                    this,
                    "Wybierz płeć.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        double heightM = heightCm / 100.0;
        double bmi = weightKg / (heightM * heightM);

        String category;

        if (bmi < 18.5) {
            category = "Niedowaga";
        } else if (bmi < 25.0) {
            category = "Waga prawidłowa";
        } else if (bmi < 30.0) {
            category = "Nadwaga";
        } else {
            category = "Otyłość";
        }

        String result = String.format(
                Locale.getDefault(),
                "BMI: %.2f\nKategoria: %s",
                bmi,
                category
        );

        tekstWyniku.setText(result);

        Toast.makeText(
                this,
                "Obliczanie zakończone!",
                Toast.LENGTH_SHORT
        ).show();

        if (zapiszPomiary.isChecked()) {
            Toast.makeText(
                    this,
                    "Wybrano opcję zapisu danych.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}
