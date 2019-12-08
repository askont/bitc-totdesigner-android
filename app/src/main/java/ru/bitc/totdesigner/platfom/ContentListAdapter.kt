package ru.bitc.totdesigner.platfom

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.bitc.totdesigner.R

class ContentListAdapter(val items: ArrayList<TestQuestDTO>, val context: ContentListFragment) : RecyclerView.Adapter<QuestViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestViewHolder
        = QuestViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.quest_item, parent, false))


    override fun onBindViewHolder(holder: QuestViewHolder, position: Int) {
        holder?.bind(items[position])
    }
}

class QuestViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    private var nameTV: TextView? = null
    private var imageIV: ImageView? = null

    init {
        nameTV = itemView.findViewById(R.id.tv_quest_name)
        imageIV = itemView.findViewById(R.id.iv_quest_image)
    }

    @SuppressLint("ResourceType")
    fun bind(quest: TestQuestDTO) {
        nameTV?.text = quest.name

        imageIV?.setImageResource(quest.imgResId)
    }

}
