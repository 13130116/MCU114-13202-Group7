package com.example.final2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MoodAdapter(private val context: Context, private val moods: List<Mood>) :
    RecyclerView.Adapter<MoodAdapter.MoodViewHolder>() {

    class MoodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val moodIcon: ImageView = view.findViewById(R.id.item_mood_icon)
        val moodDate: TextView = view.findViewById(R.id.item_mood_date)
        val moodContent: TextView = view.findViewById(R.id.item_mood_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoodViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mood_history, parent, false)
        return MoodViewHolder(view)
    }

    override fun getItemCount() = moods.size

    override fun onBindViewHolder(holder: MoodViewHolder, position: Int) {
        val mood = moods[position]

        holder.moodDate.text = mood.date
        holder.moodContent.text = mood.content

        // 根據儲存的圖案名稱，來決定要顯示哪張圖
        mood.moodIcon?.let { iconName ->
            // 這行程式碼會把 "happy" 這樣的文字，轉換成 R.drawable.happy 這樣的圖片資源
            val resourceId = context.resources.getIdentifier(iconName, "drawable", context.packageName)

            if (resourceId != 0) { // 如果找到了對應的圖片
                holder.moodIcon.setImageResource(resourceId)
            } else { // 如果因為某些原因找不到圖 (例如圖檔被刪掉或改名)
                holder.moodIcon.setImageResource(R.drawable.sparkle) // 就顯示一個預設的光點圖案
            }
        } ?: run {
            // 如果 moodIcon 是 null (也就是舊的、沒有圖案的紀錄)
            holder.moodIcon.setImageResource(R.drawable.sparkle) // 同樣顯示預設圖案
        }
    }
}
