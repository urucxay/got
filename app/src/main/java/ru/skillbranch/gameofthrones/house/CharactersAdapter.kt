package ru.skillbranch.gameofthrones.house

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.character_list_item.*
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem

class CharactersAdapter(
    private val listener: (CharacterItem) -> Unit
) : ListAdapter<CharacterItem, CharactersAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.character_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character = getItem(position)
        holder.bind(character, listener)
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), LayoutContainer {
        override val containerView: View?
            get() = itemView

        fun bind(character: CharacterItem, listener: (CharacterItem) -> Unit) {
            tv_character_name.text = character.name.ifEmpty { "Information is unknown" }
            tv_character_title.text =
                character.titles.joinToString(" • ").ifEmpty { "Information is unknown" }
            val avatar = when (character.house) {
                "Stark" -> R.drawable.stark_icon
                "Lannister" -> R.drawable.lanister_icon
                "Targaryen" -> R.drawable.targaryen_icon
                "Baratheon" -> R.drawable.baratheon_icon
                "Greyjoy" -> R.drawable.greyjoy_icon
                "Martell" -> R.drawable.martel_icon
                "Tyrell" -> R.drawable.tyrel_icon
                else -> -1
            }
            Glide.with(itemView.context)
                .load(avatar)
                .into(iv_character_avatar)

            itemView.setOnClickListener { listener(character) }
        }
    }
}

object DiffCallback : DiffUtil.ItemCallback<CharacterItem>() {
    override fun areItemsTheSame(oldItem: CharacterItem, newItem: CharacterItem): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: CharacterItem, newItem: CharacterItem): Boolean =
        oldItem == newItem
}