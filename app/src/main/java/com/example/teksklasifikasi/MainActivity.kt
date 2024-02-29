package com.example.teksklasifikasi

import android.R.id.input
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.teksklasifikasi.databinding.ActivityMainBinding
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.text.nlclassifier.BertNLClassifier
import org.tensorflow.lite.task.text.nlclassifier.BertNLClassifier.BertNLClassifierOptions


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var arrayAdapter: ArrayAdapter<*>
    private var modelFile = "model_ind_3.tflite"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var mListView = binding.lvResults

        val results = mutableListOf("")

        binding.btnDukun.setOnClickListener {
            val predict = initializationModels(binding.etInput.text.toString())
            results.add(predict)
            arrayAdapter = ArrayAdapter(applicationContext, R.layout.list_text, results)
            mListView.adapter = arrayAdapter
            Log.i("HASIL", "$predict")
        }
        binding.btnReset.setOnClickListener {
            results.clear()
            arrayAdapter = ArrayAdapter(applicationContext, R.layout.list_text, results)
            mListView.adapter = arrayAdapter
        }

    }

    fun initializationModels(input: String): String {
        // Initialization
        val options = BertNLClassifierOptions.builder()
            .setBaseOptions(BaseOptions.builder().setNumThreads(4).build())
            .build()
        val classifier = BertNLClassifier.createFromFileAndOptions(applicationContext, modelFile, options)

        // Run inference
        val results: List<Category> = classifier.classify(input)
        // Find the max score
        val final_result = results.sortedByDescending { it.score }[0].label
        return final_result
    }
}