package com.example.domaacessivel.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.domaacessivel.R
import java.util.Locale

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    companion object {
        private const val REQUEST_CODE_SPEECH_INPUT = 1
    }

    private lateinit var textoparafala: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textoparafala = TextToSpeech(
            this,
            this
        ); //  o primeiro parâmetro indica onde o TextToSpeech será utilizado e o segundo parâmetro especifica quem será notificado quando o TextToSpeech estiver inicializado.

        var btnTeste = findViewById(R.id.botaoTeste) as Button;

        btnTeste.setOnClickListener {
            falarMensagem("Você clicou no botão ${btnTeste.text}")
        }

        var btnFale = findViewById(R.id.botaoFale) as Button;

        btnFale.setOnClickListener {
            startVoiceRecognition()
        }

    }


    private fun startVoiceRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale("pt", "BR"))
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Fale agora...")

        startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
    }

    private fun falarMensagem(mensagem: String) {
        textoparafala.speak(mensagem, TextToSpeech.QUEUE_FLUSH, null);
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            var locale = Locale("pt", "BR")
            val result =
                textoparafala.setLanguage(locale) // Locale.getDefault() pega o idioma da região
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) { // Idioma da região não é suportado
                textoparafala.speak(
                    "O idioma local não é suportado",
                    TextToSpeech.QUEUE_FLUSH,
                    null
                ); // "Queue_flush" faz com que todos os sons parem e esse comece
            } else {
                textoparafala.speak(
                    "A acessibilidade acaba de ser iniciada",
                    TextToSpeech.QUEUE_FLUSH,
                    null
                );
            }
        } else {
            textoparafala.speak(
                "A inicialização da acessibilidade não funcionou, tente novamente",
                TextToSpeech.QUEUE_FLUSH,
                null
            );
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == Activity.RESULT_OK) {
                val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                val spokenText = result?.get(0)

            if (spokenText!=null) {
                // Processar o texto reconhecido aqui
                if (spokenText.contains("open" ) || spokenText.contains("")) {
                    // Se o texto reconhecido contém "abrir", clique no botão
                    val btnTeste = findViewById<Button>(R.id.botaoTeste)
                    btnTeste.performClick()
                }
            }
        }
    }
}
// Agora você pode processar o texto reconhecido, por exemplo, executar uma ação com base nele