package com.example.threadhandlerandprogressbar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class ProgressFragment : Fragment() {

    private val viewModel: WorkViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frag_progress, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvStatus: TextView = view.findViewById(R.id.tvProgressStatus)
        val tvPercent: TextView = view.findViewById(R.id.tvProgressPercent)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
        val progressLayout: FrameLayout = view.findViewById(R.id.progressLayout)


        viewModel.status.observe(viewLifecycleOwner) { status ->
            tvStatus.text = status
            when (status) {
                "Working..." -> {
                    progressLayout.visibility = View.VISIBLE
                }
                "Preparing..." -> {
                    progressLayout.visibility = View.INVISIBLE
                }
                else -> {
                    progressLayout.visibility = View.INVISIBLE
                }
            }
        }


        viewModel.progress.observe(viewLifecycleOwner) { progress ->
            progressBar.progress = progress
            tvPercent.text = "$progress%"
        }
    }
}