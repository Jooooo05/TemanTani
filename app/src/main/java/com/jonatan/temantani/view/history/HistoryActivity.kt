package com.jonatan.temantani.view.history

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jonatan.temantani.R
import com.jonatan.temantani.databinding.ActivityHistoryBinding
import com.jonatan.temantani.view.ViewModelFactory
import com.jonatan.temantani.view.analisis.AnalyzeActivity
import com.jonatan.temantani.view.analisis.ResultActivity

class HistoryActivity : AppCompatActivity() {
    private val historyViewModel by viewModels<HistoryViewModel> {
        ViewModelFactory.getInstance(application)
    }
    private val listAdapter by lazy { ListHistoryAdapter(::moveToResult) }
    private lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.history_title)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        setupRecyclerView()
        observeHistory()
    }

    override fun onResume() {
        super.onResume()
        historyViewModel.getAllHistory()
    }

    private fun setupRecyclerView() {
        binding.rvHistory.apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(this@HistoryActivity)
        }
    }

    private fun observeHistory() {
        historyViewModel.history.observe(this) { listHistory ->
            listAdapter.listHistory = listHistory
            showTVNoHistory(listHistory.isEmpty())
        }
    }

    private fun showTVNoHistory(isNotFound: Boolean) {
        binding.tvNoHistory.visibility = if (isNotFound) View.VISIBLE else View.GONE
    }

    private fun moveToResult(data: HistoryEntity) {
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra(ResultActivity.EXTRA_HISTORY, ResultActivity.HISTORY_REQUEST_CODE)
            putExtra(ResultActivity.EXTRA_LOCAL_DATA, data)
        }
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
