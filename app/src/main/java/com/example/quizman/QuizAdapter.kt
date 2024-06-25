package com.example.quizman

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quizman.databinding.QuizItemBinding

class QuizAdapter(private val quizModelList: List<QuizModel>) :
    RecyclerView.Adapter<QuizAdapter.MyViewHolder>() {

    class MyViewHolder(private val binding: QuizItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: QuizModel) {
            binding.apply {
                quiztitle.text = model.title
                quizsubtitle.text = model.subtitle
                quiztime.text = "${model.time} mins"
                root.setOnClickListener {
                    val intent = Intent(root.context, QuizActivity::class.java).apply {
                        QuizActivity.questionModelList = model.questionList
                        QuizActivity.time = model.time
                    }
                    root.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = QuizItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = quizModelList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(quizModelList[position])
    }
}
